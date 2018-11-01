package bd.flighsAnalysis;

import org.bson.Document;

public class MongoHelper {
	
	
	public static Document toDocument(Object...objs) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (int i = 0; i < objs.length; i++) {
			if (i % 2 == 0)
				builder.append(objs[i] + ": ");
			else {
				if (objs[i].getClass() == Float.class || objs[i].getClass() == Integer.class || objs[i].getClass() == Double.class)
					builder.append(""+ objs[i] + "");
				else
					builder.append("'"+ objs[i] + "'");
				if (i != objs.length - 1)
					builder.append(", ");
			}
		}
		builder.append("}");
		return Document.parse(builder.toString());
	}
}
