package org.ts.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private MongoDB(){}

    public static MongoClient getClient(){
        if(mongoClient == null){
            ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/?retryWrites=false");
            mongoClient = MongoClients.create(connectionString);
        }
        return mongoClient;
    }

    public static MongoDatabase getDatabase(){
        if(database == null){
            try{
                mongoClient = MongoDB.getClient();
                database = mongoClient.getDatabase("nexora");
            } catch (MongoClientException e){
                System.err.println("Error connecting to the database: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return database;
    }
}
