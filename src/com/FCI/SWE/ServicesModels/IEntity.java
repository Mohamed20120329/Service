package com.FCI.SWE.ServicesModels;

import org.json.simple.JSONObject;

public abstract class IEntity {

	public String name;
	public Long id;
	
	public IEntity(){
		
	}
	
	public IEntity(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	
	
	public void setName(String name){
		this.name = name;
	}
	
	public abstract Boolean notifyUser(String notificationType,
			String notificationTime, String params);
	
	public abstract String getUniqueField();
	
	public abstract Boolean saveEntity();
	
	public abstract IEntity getSingleEntity(String name);
	
	public abstract JSONObject toJson();
	
	

}
