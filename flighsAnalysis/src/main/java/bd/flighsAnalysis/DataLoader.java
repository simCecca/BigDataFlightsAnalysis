package bd.flighsAnalysis;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;


public class DataLoader {
	

	public <T> JavaRDD<T> loadData(JavaSparkContext sc, String path, Parser<T> parser) {
		
		
		JavaRDD<String> rows = sc.textFile(path);
		
		JavaRDD<T> records = rows.map(parser::parseLine).filter(r -> r != null);

		return records;
	}
	
}
