package bd.flighsAnalysis.delayPerCompany;


import java.io.Serializable;


import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.bson.Document;

import com.mongodb.spark.api.java.MongoSpark;

import bd.flighsAnalysis.flightRecord.FlightRecord;
import bd.flighsAnalysis.flightRecord.FlightRecordParser;
import scala.Tuple2;

public class DelayPerCompanyJob implements Serializable {
	

	private static final long serialVersionUID = 176876875L;
	private JavaSparkContext sc;
	

	public void close() {
		this.sc.close();
	}


	private JavaRDD<FlightRecord> loadData(String pathToFile) {
		SparkSession spark = SparkSession.builder()
			      .master("local")
			      .appName("MongoSparkConnectorIntro")
			      .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/sparkdb.airline2delayHRS")
			      .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/sparkdb.airline2delayHRS")
			      .getOrCreate();

		sc = new JavaSparkContext(spark.sparkContext());
		
		
		JavaRDD<String> rows = sc.textFile(pathToFile);
		

		FlightRecordParser parser = new FlightRecordParser();
		JavaRDD<FlightRecord> records = rows.map(parser::parseLine).filter(r -> r != null);

		
		return records;

	}

	private void execJob(String pathToFile) {
		JavaRDD<FlightRecord> records = loadData(pathToFile);
		
		
		MongoSpark.save(
			records.mapToPair(r -> new Tuple2<>(r.getAirlineId(), r.getArrDelay()))
					.reduceByKey((a, b) -> b > 0 ? a + (b/60.f) : a) // only consider positive delay (in hours)
					.mapToPair(Tuple2::swap) // sort by value is not supported by spark, workaround: swap -> sort -> swap
					.sortByKey(true)
					.mapToPair(Tuple2::swap)
					.map(p -> Document.parse("{airlineid: " + p._1 + ", delay: " + p._2 + "}"))
		);
				//.coalesce(1)
				//.saveAsTextFile("delay per company res");
				 
		
				
	}


	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: <filetxt>");
			System.exit(1);
		}

		DelayPerCompanyJob job = new DelayPerCompanyJob();

		long start = System.currentTimeMillis();
		job.execJob(args[0]);
		long end = System.currentTimeMillis();
		
		System.out.println("\n\n\n\n\n\n\n");
		System.out.println("Elapsed Time:    " + (end-start)/1000.0) ;
		System.out.println("\n\n\n\n\n\n\n");
		
		job.close();

	}
}

