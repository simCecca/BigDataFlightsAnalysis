package bd.flighsAnalysis.departureDelay;

import java.io.Serializable;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import bd.flighsAnalysis.DataLoader;
import bd.flighsAnalysis.flightRecord.FlightRecord;
import bd.flighsAnalysis.flightRecord.FlightRecordParser;
import scala.Tuple2;

public class DepartureDelay implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 80000003L;
	private JavaSparkContext sc;
	
	public void close() {
		this.sc.close();
	}
	
	private void execJob(String pathToFile) {
		sc = new JavaSparkContext();
		JavaRDD<FlightRecord> flightRecords = new DataLoader().loadData(sc, pathToFile, new FlightRecordParser());
		
		flightRecords.mapToPair(pair -> new Tuple2<>( pair.getOriginAirportIATA() + ", " + pair.getOriginCityName(),
				new Tuple2<>(pair.getDepDelayMins(), 1)))
		.reduceByKey((a,b) -> new Tuple2<>(a._1 + b._1, a._2 + b._2))
		.mapValues(totDelay2flights -> totDelay2flights._1 / totDelay2flights._2)
		.mapToPair(Tuple2::swap)
		.sortByKey(false)
		.mapToPair(Tuple2::swap)
		.coalesce(1)
		.saveAsTextFile("DepartureDelay");
	}
	
	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: <filetxt>");
			System.exit(1);
		}

		DepartureDelay dd =  new DepartureDelay();
		
		long start = System.currentTimeMillis();
		dd.execJob(args[0]);
		long end = System.currentTimeMillis();
		
		System.out.println("\n\n\n\n\n\n\n");
		System.out.println("DepartuteDelay Time:    " + (end-start)/1000.0) ;

		dd.close();
	}
}
