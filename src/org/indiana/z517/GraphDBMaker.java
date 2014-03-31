package org.indiana.z517;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexManager;

public class GraphDBMaker {
	
	private GraphDBUtils graphTool;
	private IndexManager indexMan;
	private Transaction tx;
	private ResourceIterator<Node> iterator;
	
	public GraphDBMaker() {
		graphTool = new GraphDBUtils();
		graphTool.initDB();					// set up graph database
	}
	
	private String createUserNode(String user) {
		String returnValue = "Username: " + "'" + user + "' already exists";
		
		Node userNode = null;
		Label label = DynamicLabel.label("User");
		tx = GraphDBUtils.dbService.beginTx();		// begin a transaction to engage with graph database
				
		try {
			iterator = GraphDBUtils.dbService.findNodesByLabelAndProperty(label, "username", user).iterator();
			while (iterator.hasNext())
				userNode = iterator.next();
			
			if (userNode == null) {
				userNode = GraphDBUtils.dbService.createNode(label);
				userNode.setProperty("username", user);
				
				returnValue = "Username: " + "'" + user + "' successfully created.";
			}
			
			tx.success();
		} finally {
			iterator.close();
			tx.close();
		}
		
		return returnValue;
	}
	
	private String createTweetNode(long tweetID, String timestamp, String message, String location) {
		
		// no need of checking if tweet exists
		tx = GraphDBUtils.dbService.beginTx();
		Label label = DynamicLabel.label("Tweet");
		Node tweetNode = null;
		
		try {
			tweetNode = GraphDBUtils.dbService.createNode(label);
			tweetNode.setProperty("tweetID", tweetID);
			tweetNode.setProperty("timestamp", timestamp);
			tweetNode.setProperty("message", message);
			tweetNode.setProperty("location", location);
			
			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}

		return "Tweet node successfully created.";
	}
	  
	private String createHashTagNode(String hashTag) {
		String returnValue = "HashTag: '" + hashTag + "' already exists!";
		
		tx = GraphDBUtils.dbService.beginTx();
		Node hashTagNode = null;
		Label label = DynamicLabel.label("HashTag");
		
		try {
			iterator = GraphDBUtils.dbService.findNodesByLabelAndProperty(label, "HashTagName", hashTag).iterator();
			while (iterator.hasNext()) {
				hashTagNode = iterator.next();
			}
			
			// create HashTag node only if no such HashTag was found in db
			if (hashTagNode == null) {
				hashTagNode = GraphDBUtils.dbService.createNode(label);
				hashTagNode.setProperty("HashTagName", hashTag);
				
				returnValue = "HashTag: '" + hashTag + "' successfully created.";
			}
			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}
		
		return returnValue;
	}
	
	private String createLinkNode(String link) {
		String returnValue = "Link: '" + link + "' already exists!";
		
		tx = GraphDBUtils.dbService.beginTx();
		Node linkNode = null;
		Label label = DynamicLabel.label("Link");
		
		try {
			iterator = GraphDBUtils.dbService.findNodesByLabelAndProperty(label, "LinkName", link).iterator();
			while (iterator.hasNext()) {
				linkNode = iterator.next();
			}
			
			// create new link node only if link does not exist
			
//			Error, check this and other similar methods too
			if (linkNode == null) {
				linkNode = GraphDBUtils.dbService.createNode(label);
				linkNode.setProperty("LinkName", link);
				
				returnValue = "Link: '" + link + "' successfully created.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			iterator.close();
			tx.close();
		}
		
		return returnValue;
	}
	
	public void closeShop() {
		graphTool.shutdown();
	}
	
	public static void main(String[] args) {
		GraphDBMaker graph = new GraphDBMaker();
		
		System.out.println(graph.createUserNode("abdpatel"));
				
		System.out.println(graph.createTweetNode(24532324, "2014-03-30 12:00:04", "This is a tweet!", "California"));
		
		System.out.println(graph.createHashTagNode("Hoosiers"));
		System.out.println(graph.createHashTagNode("coffee"));
		
		System.out.println(graph.createLinkNode("http://www.imdb.com"));
		System.out.println(graph.createLinkNode("http://www.imdb.com"));
		
		graph.closeShop();
	}
	
}
