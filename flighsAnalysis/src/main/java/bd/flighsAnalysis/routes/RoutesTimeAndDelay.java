package bd.flighsAnalysis.routes;


import java.io.Serializable;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import bd.flighsAnalysis.DataLoader;
import bd.flighsAnalysis.flightRecord.FlightRecord;
import bd.flighsAnalysis.flightRecord.FlightRecordParser;
import scala.Tuple2;

public class RoutesTimeAndDelay implements Serializable {
	
	private static final float TWO_HOURS = 120;
	
	private static final long serialVersionUID = 1L;

	private Tuple2<String, String> getAirPortNames(FlightRecord r) {
		String origin = r.getOriginCityName() + ", " + r.getOriginAirportIATA();
		String dest = r.getDestCityName() + ", " + r.getDestAirportIATA();
		return new Tuple2<>(origin, dest);
	}

	private void execJob(JavaSparkContext sc, String pathToFile) {
		JavaRDD<FlightRecord> flightRecords = new DataLoader().loadData(sc, pathToFile, new FlightRecordParser());

		
		flightRecords.mapToPair(r -> new Tuple2<>(getAirPortNames(r),
				r.getAirTime() + r.getDepDelayMins() + r.getArrDelay() + TWO_HOURS))
		.mapValues(totDelay -> new Tuple2<>(totDelay, 1))
		.reduceByKey((accum, current) -> current._1 < 0 ? accum : new Tuple2<>(accum._1 + current._1, accum._2 + current._2)) // Consider only valid data: airTime > 0 (see FlightRecordParser)
		.mapValues(sumAndCount -> (sumAndCount._1 / sumAndCount._2))
		.coalesce(1)
		.foreach(f -> {
			Neo4jHelper h = new Neo4jHelper("bolt://localhost:7687", "cecio", "cecio");
			h.save(f._1._1, f._2, f._1._2);
			h.close();	
		});
		

	}


	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: <filetxt>");
			System.exit(1);
		}
		
		SparkSession spark = SparkSession.builder()
			      .master("local")
			      .appName("MongoSparkConnectorIntro")
			      .getOrCreate();

		JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

		
		RoutesTimeAndDelay job = new RoutesTimeAndDelay();

		long start = System.currentTimeMillis();
		job.execJob(sc, args[0]);
		long end = System.currentTimeMillis();
		
		System.out.println("\n\n\n\n\n\n\n");
		System.out.println("Elapsed Time:    " + (end-start)/1000.0) ;
		System.out.println("\n\n\n\n\n\n\n");
		
	}
}

