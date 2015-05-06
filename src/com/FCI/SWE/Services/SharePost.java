package com.FCI.SWE.Services;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.FCI.SWE.ServicesModels.UserEntity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class SharePost implements IPost{
	@POST
	@Path("/SharePostService")	
    public boolean SharePostService(@FormParam("uname")String active_user_name ,@FormParam("PostID") String postID ) 
	{ 
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("Post");
		PreparedQuery pq = datastore.prepare(gaeQuery);		
		String owner = "", content = "",feeling = "" , privacy = "", type = "Share";  boolean ok = false ;
		
		for (Entity entity : pq.asIterable())
		{
			if (entity.getProperty("PostID").toString().equals(postID))
			{
				owner = entity.getProperty("Owner").toString();
				content = entity.getProperty("Content").toString();
				feeling = entity.getProperty("Feeling").toString();
				privacy = entity.getProperty("Privacy").toString(); 
				ok = true ;
				break;
			}
		}
		
		if(ok){
			ok = false ;
			if(privacy.equals("public")){				
				ok = true ;
			}
			else if(privacy.equals("private"))
			{
				if(!(UserEntity.CheckFriend(owner, active_user_name)) ){
					type += ("/" + owner) ;
					ok = true ;
				}
			}
			else if(privacy.contains("custom") && privacy.contains("active_user_name")) ok = true ;
		}if(ok){			

			List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
	        Entity newPost = new Entity("Post", list.size() + 1);
	        
	        newPost.setProperty("PostID", list.size() + 1);
	        newPost.setProperty("Owner", active_user_name);
	        newPost.setProperty("Content", content);
	        newPost.setProperty("Feeling", feeling);	        
	        newPost.setProperty("Privacy", privacy);
	        newPost.setProperty("Type", type);
	        datastore.put(newPost) ;
 		}
		return ok;
	}

	@Override
	public boolean LikePostService(String active_user_name, String postID) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
