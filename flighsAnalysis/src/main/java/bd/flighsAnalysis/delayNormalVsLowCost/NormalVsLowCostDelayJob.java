package bd.flighsAnalysis.delayNormalVsLowCost;

import java.io.Serializable;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import bd.flighsAnalysis.DataLoader;
import bd.flighsAnalysis.airlineRecord.AirlineRecord;
import bd.flighsAnalysis.airlineRecord.AirlineRecordParser;
import bd.flighsAnalysis.flightRecord.FlightRecord;
import bd.flighsAnalysis.flightRecord.FlightRecordParser;
import scala.Tuple2;

public class NormalVsLowCostDelayJob implements Serializable {
	
	
	private static final long serialVersionUID = 1L;


	private void execJob(JavaSparkContext sc, String pathToFile, String pathToAirline) {
		JavaRDD<FlightRecord> flightRecords = new DataLoader().loadData(sc, pathToFile, new FlightRecordParser());
		JavaRDD<AirlineRecord> airlineRecord = new DataLoader().loadData(sc, pathToAirline, new AirlineRecordParser());

		JavaPairRDD<String, Boolean> airlineId2LowCost = airlineRecord.mapToPair(r -> new Tuple2<>(r.getAirlineId(), r.isLowCost()));
		
		flightRecords.mapToPair(r -> new Tuple2<>(r.getAirlineId(), r.getArrDelay() + r.getDepDelayMins()))
		.join(airlineId2LowCost) // (airlineid, (delay, lowcost))
		.mapToPair(t -> new Tuple2<>(t._2._2, new Tuple2<>(t._2._1, 1))) // (low cost, (delay, 1))
		.reduceByKey((a, b) -> new Tuple2<>(a._1 + b._1, a._2 + b._2)) // (low cost, (tot delay, num flights))
		.mapValues(delay2numFlights -> delay2numFlights._1 / delay2numFlights._2) // average delay per flight
		.coalesce(1)
		.saveAsTextFile("lowCost");
		

	}


	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Usage: <flights> <airlines>");
			System.exit(1);
		}
		
		SparkSession spark = SparkSession.builder()
			      .master("local")
			      .appName("MongoSparkConnectorIntro")
			      .getOrCreate();

		JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

		
		NormalVsLowCostDelayJob job = new NormalVsLowCostDelayJob();

		long start = System.currentTimeMillis();
		job.execJob(sc, args[0], args[1]);
		long end = System.currentTimeMillis();
		
		System.out.println("\n\n\n\n\n\n\n");
		System.out.println("Elapsed Time:    " + (end-start)/1000.0) ;
		System.out.println("\n\n\n\n\n\n\n");
		
	}
}