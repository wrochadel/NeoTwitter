package org.indiana.z517;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexManager;

public class GraphDBMaker {
	
	private GraphDBUtils graphTool;
	IndexManager indexMan;
	
	public GraphDBMaker() {
		graphTool = new GraphDBUtils();
		graphTool.initDB();					// set up graph database
	}
	
	private boolean createUserNode(String user) {
		Node userNode = null;
		Label label = DynamicLabel.label("User");
		Transaction tx = GraphDBUtils.dbService.beginTx();		// begin a transaction to engage with graph database
		
		try {
			// if user exists, do not create new User node
			if ( (Node) GraphDBUtils.dbService.findNodesByLabelAndProperty(label, "username", user).iterator() == null) {
				System.out.println("It does return NULL!");
			} else
				System.out.println("this shouldn't be printed!");
			
			tx.success();
		} finally {
			tx.close();
		}
		
		// else create new user node
		
		return false;
	}
	
	/*createTweetNode
	createHashTagNode
	createLinksNode*/
	
	public void closeShop() {
		graphTool.shutdown();
	}
	
	public static  void main(String[] args) {
		GraphDBMaker graph = new GraphDBMaker();
		graph.createUserNode("abdpatel");
		
		
	}
	
}
