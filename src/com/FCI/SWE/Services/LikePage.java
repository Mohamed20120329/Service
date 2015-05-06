package com.FCI.SWE.Services;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class LikePage implements IPage{
	@POST
	@Path("/LikePageService")
	public boolean LikePage(@FormParam("PageName") String PageName, @FormParam("uname") String fan){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("Page");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		for(Entity entity : pq.asIterable()){
			if(entity.getProperty("PageName").toString().equals(PageName)){	
				
				String fans =""; 
				fans = entity.getProperty("Fans").toString() ; 
				if(fans.contains(fan)) return false ;
				fans += "/" + fan ;	  
				entity.setProperty("Fans", fans);
				int count = Integer.parseInt(entity.getProperty("LikesCount").toString());				
				entity.setProperty("LikesCount", count+1);
				datastore.put(entity);
				return true ;
			}
		}		
		return false ;
	}

	@Override
	public void CreatePage(String PageName, String Category, String cur_user) {
		// TODO Auto-generated method stub
		
	}

}
