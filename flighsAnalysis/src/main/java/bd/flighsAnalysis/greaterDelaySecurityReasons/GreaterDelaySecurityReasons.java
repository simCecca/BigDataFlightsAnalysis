package bd.flighsAnalysis.greaterDelaySecurityReasons;

import java.io.Serializable;
import java.util.Date;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import bd.flighsAnalysis.DataLoader;
import bd.flighsAnalysis.flightRecord.FlightRecord;
import bd.flighsAnalysis.flightRecord.FlightRecordParser;
import scala.Tuple2;

public class GreaterDelaySecurityReasons implements Serializable{
	
	private static final long serialVersionUID = 80000003L;
	private JavaSparkContext sc;
	
	public void close() {
		this.sc.close();
	}
	
	private void execJob(String pathToFile) {
		sc = new JavaSparkContext();
		JavaRDD<FlightRecord> flightRecords = new DataLoader().loadData(sc, pathToFile, new FlightRecordParser());
		
		flightRecords.mapToPair(pair -> new Tuple2<Date,Float>(pair.getDate(), pair.getSecurityDelay()))
		.reduceByKey((a,b) -> a + b)
		.mapToPair(Tuple2::swap)
		.sortByKey(false)
		.mapToPair(Tuple2::swap)
		.coalesce(1)
		.saveAsTextFile("securityAnalisys");
		
		
	}
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: <filetxt>");
			System.exit(1);
		}

		GreaterDelaySecurityReasons gd =  new GreaterDelaySecurityReasons();
		
		long start = System.currentTimeMillis();
		gd.execJob(args[0]);
		long end = System.currentTimeMillis();
		
		System.out.println("\n\n\n\n\n\n\n");
		System.out.println("DepartuteDelay Time:    " + (end-start)/1000.0) ;

		gd.close();
	}
	

}
