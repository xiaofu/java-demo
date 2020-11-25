package com.github.xiaofu.demo.aws;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import java.util.Arrays;

import org.bson.BsonDocument;
import org.bson.Document;
public class DocDBDemo {

	public static void main(String[] args) {
		 
		   String connectionString = "mongodb://keteuser:kete20201125@docdb-2020-11-25-03-12-25.cluster-cbzkjpxwsrha.docdb.cn-northwest-1.amazonaws.com.cn:27000/?replicaSet=rs0&readPreference=secondaryPreferred&retryWrites=false";
	       
	        MongoClientURI clientURI = new MongoClientURI(connectionString);
	        MongoClient mongoClient = new MongoClient(clientURI);
	        Document doc = new Document("name", "MongoDB")
	                .append("type", "database")
	                .append("count", 1)
	                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
	                .append("info", new Document("x", 203).append("y", 102));
	        for (Document string :  mongoClient.getDatabase("file_base_info").getCollection("mytest").find()) {
				System.out.println(string.toJson());
			}
	       
	}

}
