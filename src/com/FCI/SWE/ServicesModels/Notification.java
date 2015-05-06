package com.FCI.SWE.ServicesModels;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

public class Notification {
	private String type;
	private String timestamp;
	private UserEntity user;
	private String params;
	
	public String getType() {
		return type;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public UserEntity getUser() {
		return user;
	}

	public String getParams() {
		return params;
	}

	public static String getClassName(String action){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("NotificationCommand");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		for(Entity entity : list){
			String key = entity.getKey().toString();
			if(key.contains(action)){
				return entity.getProperty("class").toString();
			}
		}
		return null;

	}
	
	
	
	public static Notification getNotification(String id){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("Notification");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		for(Entity entity : list){
			String entityId = Long.toString(entity.getKey().getId());
			if(entityId.equals(id)){
				Notification notification = new Notification();
				notification.type = entity.getProperty("type").toString();
				notification.params = entity.getProperty("params").toString();
				notification.user = UserEntity.searchSingleUser(entity.getProperty("user").toString());
				notification.timestamp = entity.getProperty("timestamp").toString();
				return notification;
			}
		}
		return null;

	}
	
	public static String getAllNotifications(String user){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("Notification");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		JSONArray array = new JSONArray();
		for(Entity entity : list){
			if(entity.getProperty("user").toString().equals(user)){
				JSONObject object = new JSONObject();
				object.put("id", entity.getKey().getId());
				object.put("timestamp", entity.getProperty("timestamp").toString());
				object.put("params", entity.getProperty("params").toString());
				object.put("type", entity.getProperty("type").toString());
				array.add(object);
			}
		}
		return array.toJSONString();
		
	}
}
