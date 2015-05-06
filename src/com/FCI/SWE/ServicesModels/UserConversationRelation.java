package com.FCI.SWE.ServicesModels;

import java.util.List;
import java.util.Vector;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

public class UserConversationRelation {
	public static Boolean createRelation(IEntity user, Long conversationId){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try{
		Entity entity = new Entity("UserConversation");
		entity.setProperty("conversation", conversationId);
		entity.setProperty("user", user.getUniqueField());
		datastore.put(entity);
		txn.commit();
		
		}finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
		return true;
	}
	
	public static Vector<IEntity> getUsersInConversation(Long conversationId){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Vector<IEntity> entities = new Vector<IEntity>();
		Query gaeQuery = new Query("UserConversation");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		for(Entity entity : list){
			Long gid = Long.parseLong(entity.getProperty("conversation").toString());
			if(gid.equals(conversationId)){
				entities.add(UserEntity.searchSingleUser(entity.getProperty("user").toString()));
			}
		}
		
		return entities;
	}
}
