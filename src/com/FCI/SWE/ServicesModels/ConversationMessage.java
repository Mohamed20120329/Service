package com.FCI.SWE.ServicesModels;

import java.util.Date;
import java.util.Vector;

import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Transaction;

public class ConversationMessage {

	public Long conversationId;
	public String title;
	public UserEntity owner;
	public Vector<IEntity> entities;
	
	public ConversationMessage(String title, String owner){
		
		this.title = title;
		this.owner = UserEntity.searchSingleUser(owner);
		entities = new Vector<IEntity>();
	}
	
	public Boolean saveConversation() {
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try{
		Entity entity = new Entity("Conversation");
		entity.setProperty("title", title);
		entity.setProperty("owner", owner.getUniqueField());
		conversationId = datastore.put(entity).getId();
		txn.commit();
		}finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
		if(conversationId == null)return false;
		return true;
		
	}
	
	public Boolean attachUser(UserEntity user){
		entities.add(user);
		if(UserConversationRelation.createRelation(user, conversationId))return true;
		return false;
	}
	
	public Boolean send(String message){
		if(!Message.sendInstantMessage(owner.getUniqueField(), conversationId, message))
			return false;
		
		for(IEntity entity : entities){
			Date date = new Date();
			String params = "";
			JSONObject obj = new JSONObject();
			obj.put("conversation", conversationId);
			String currentTime = Long.toString(date.getTime());
			if(!entity.notifyUser("Message", currentTime, obj.toJSONString()))
				return false;
		}
		return true;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean setOwner(String user){
		owner = UserEntity.searchSingleUser(user);
		if(owner == null)return false;
		return true;
	}
	
	public Long getConversationid() {
		return conversationId;
	}
	
	private void setConversationId(Long id){
		conversationId = id;
	}
}
