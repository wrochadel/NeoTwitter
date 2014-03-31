package org.indiana.z517;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;

public class GraphDBUtils {

	protected static GraphDatabaseService dbService;
	
	private final String DB_PATH = "/var/lib/neo4j-c2.0.1/data/birdeye.db";
	
	protected void initDB() {
		
		// clear the old graph db
		clearDB();
		
		// start a graph database service
		dbService = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		
		registerShutdownHook(dbService);
		
	}
	
	protected void clearDB() {
		try {
			FileUtils.deleteRecursively(new File(DB_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void shutdown() {
		System.out.println();
		System.out.println("Shutting down database now...");
		dbService.shutdown();
		System.out.println("Database successfully shut down.");
	}
	
	private void registerShutdownHook(final GraphDatabaseService serveDB) {
        
		/*Registers a shutdown hook for the Neo4j instance so that it
        shuts down nicely when the VM exits (even if you "Ctrl-C" the
        running application)*/
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				serveDB.shutdown();
			}
		});
		
	}
	
}
