package bd.flighsAnalysis.delayCauseMins;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;


import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import com.mongodb.spark.api.java.MongoSpark;

import bd.flighsAnalysis.DataLoader;
import bd.flighsAnalysis.MongoHelper;
import bd.flighsAnalysis.flightRecord.FlightRecord;
import bd.flighsAnalysis.flightRecord.FlightRecordParser;
import scala.Tuple2;

public class DelayCauseMinsJob implements Serializable {
	
	private static final long serialVersionUID = 1L;


	/** 
	 * 1) ((&lt reason &gt, year), delayMins)<br />
	 * 2) sum up the delays <br />
	 * 3) write to mongo <br />
	 * @param pathToFile
	 */
	private void execJob(JavaSparkContext sc, String pathToFile) {
		JavaRDD<FlightRecord> flightRecords = new DataLoader().loadData(sc, pathToFile, new FlightRecordParser());
		
		
		
		MongoSpark.save(
			flightRecords.flatMapToPair(r -> {
				
					Calendar cal = Calendar.getInstance();
					cal.setTime(r.getDate());
					Integer year = cal.get(Calendar.YEAR);
				
					return Arrays.asList(new Tuple2<>(new Tuple2<>("weather", year), r.getWeatherDelay()),
							new Tuple2<>(new Tuple2<>("nas", year), r.getNasDelay()),
							new Tuple2<>(new Tuple2<>("carrier", year), r.getCarrierDelay()),
							new Tuple2<>(new Tuple2<>("lateAircraft", year), r.getLateAircraftDelay())).iterator();})
			.reduceByKey((acc, curr) -> acc + (curr/60.f))
			.map(tuple -> MongoHelper.toDocument("reason", tuple._1._1, "year", tuple._1._2, "delay", tuple._2))
		);
						

	}


	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: <filetxt>");
			System.exit(1);
		}
		
		SparkSession spark = SparkSession.builder()
			      .master("local")
			      .appName("MongoSparkConnectorIntro")
			      .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/sparkdb.dealyCause")
			      .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/sparkdb.delayCause")
			      .getOrCreate();

		JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

		
		DelayCauseMinsJob job = new DelayCauseMinsJob();

		long start = System.currentTimeMillis();
		job.execJob(sc, args[0]);
		long end = System.currentTimeMillis();
		
		System.out.println("\n\n\n\n\n\n\n");
		System.out.println("Elapsed Time:    " + (end-start)/1000.0) ;
		System.out.println("\n\n\n\n\n\n\n");
		
	}
}

