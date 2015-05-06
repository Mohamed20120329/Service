package com.FCI.SWE.Services;


public class Users{
	public String mesg ;
	/*
	public boolean update(Message obj)
	{
		String sender = obj.getUpdate(this) ;
		if(sender != null) 
		{
			String type = "Message";
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query notiQuery = new Query("Notification");
			PreparedQuery preq = datastore.prepare(notiQuery);
			List<Entity> list = preq.asList(FetchOptions.Builder.withDefaults());
			Entity noti = new Entity("Notification", list.size() + 1);
			noti.setProperty("Type", type);
			noti.setProperty("Sender", obj.SendFrom);
			datastore.put(noti);
			return true;
		}
		return false;
	}
	
	public void setMassage(Message obj) 
	{
        this.mesg= obj.toString();
    }
	*/
	
}
