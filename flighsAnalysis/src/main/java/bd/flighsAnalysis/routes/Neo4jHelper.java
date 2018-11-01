package bd.flighsAnalysis.routes;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import static org.neo4j.driver.v1.Values.parameters;

import java.io.Serializable;

public class Neo4jHelper implements AutoCloseable, Serializable
{
	static final long serialVersionUID = -2600415529712815745L;

	transient private final Driver driver;

	public Neo4jHelper( String uri, String user, String password ) {
		driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}

	public void save(String start, float duration, String dest) {
		try ( Session session = driver.session() ) {
			session.writeTransaction( new TransactionWork<String>() {
				@Override
				public String execute( Transaction tx ) {
					tx.run( "MERGE (a:Airport {name: $start}) " +
							"MERGE (b:Airport {name: $dest}) " +
							"MERGE (a)-[r:Distance {duration: $duration}]->(b)",
							parameters("start", start, "duration", ""+duration, "dest", dest) );
					return "done";
				}
			} );
			//System.out.println( greeting );
		}
	}

}