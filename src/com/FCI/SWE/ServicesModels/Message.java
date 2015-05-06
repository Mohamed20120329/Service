package com.FCI.SWE.ServicesModels;

import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class Message {
	
	
	public static String getAllMessages(String conversation){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("Message");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		JSONArray array = new JSONArray();
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		for(Entity entity : list){
			if(entity.getProperty("conversation").toString().equals(conversation)){
				JSONObject obj = new JSONObject();
				obj.put("sender", entity.getProperty("sender").toString());
				obj.put("message", entity.getProperty("message").toString());
				obj.put("timestamp", entity.getProperty("timestamp").toString());
				array.add(obj);
			}
		}
		return array.toJSONString();
	}
	
	public static Boolean sendInstantMessage(String sender, Long conversationId , String message){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		
		Entity entity = new Entity("Message");
		entity.setProperty("sender", sender);
		entity.setProperty("conversation", conversationId);
		entity.setProperty("message", message);
		Date  date = new Date();
		entity.setProperty("timestamp", date.getTime());
		if(datastore.put(entity)!= null)return true;
		return false;
	}
}
