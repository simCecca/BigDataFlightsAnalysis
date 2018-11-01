package bd.flighsAnalysis.weatherDelayCause;


import java.io.Serializable;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import com.mongodb.spark.api.java.MongoSpark;

import bd.flighsAnalysis.DataLoader;
import bd.flighsAnalysis.DateHelper;
import bd.flighsAnalysis.MongoHelper;
import bd.flighsAnalysis.flightRecord.FlightRecord;
import bd.flighsAnalysis.flightRecord.FlightRecordParser;
import bd.flighsAnalysis.weatherRecord.WeatherRecord;
import bd.flighsAnalysis.weatherRecord.WeatherRecordParser;
import scala.Tuple2;
import scala.Tuple3;

public class WeatherDelayCauseJob implements Serializable {
	

	
	private static final long serialVersionUID = -6933941317055927976L;
	

	private void execJob(JavaSparkContext sc, String flightPath, String weatherPath) {
		JavaRDD<FlightRecord> flightRecords = new DataLoader().loadData(sc, flightPath, new FlightRecordParser());
		JavaRDD<WeatherRecord> weatherRecords = new DataLoader().loadData(sc, weatherPath, new WeatherRecordParser());
		
		JavaPairRDD<Tuple3<Integer, Integer, Integer>, Float> date2delay = flightRecords.filter(r -> r.getWeatherDelay() > 0 && r.getDestAirportIATA().equals("JFK")) // Only flights whose delay is caused by the weather
		.mapToPair(r -> new Tuple2<>(DateHelper.getDateTuple(r.getDate()), r.getWeatherDelay())); // tuple: ((year, month, day), delay)
		
		// tuple: ((year, month, day), weatherRecord)
		JavaPairRDD<Tuple3<Integer, Integer, Integer>, WeatherRecord> date2weather = weatherRecords.mapToPair(r -> new Tuple2<>(DateHelper.getDateTuple(r.getDate()), r));
		
		MongoSpark.save(
			date2weather.join(date2delay)
			.mapToPair(tuple -> tuple._2)
			.reduceByKey((acc, cur) -> acc + cur/60.f)
			.coalesce(1)
			.map(t -> MongoHelper.toDocument("events", t._1.getEvents(), "precipitations_mm", t._1.getPrecipitation(),
					"temp", t._1.getTemperature(), "wind", t._1.getWind(), "delay", t._2)));
		
	}


	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Usage: <filetxt_flights> <filetxt_weather>");
			System.exit(1);
		}
		
		SparkSession spark = SparkSession.builder()
			      .master("local")
			      .appName("MongoSparkConnectorIntro")
			      .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/sparkdb.weatherDelayCause")
			      .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/sparkdb.weatherDelayCause")
			      .getOrCreate();

		JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

		WeatherDelayCauseJob job = new WeatherDelayCauseJob();

		long start = System.currentTimeMillis();
		job.execJob(sc, args[0], args[1]);
		long end = System.currentTimeMillis();
		
		System.out.println("\n\n\n\n\n\n\n");
		System.out.println("Elapsed Time:    " + (end-start)/1000.0) ;
		System.out.println("\n\n\n\n\n\n\n");
		
		sc.close();

	}
}

