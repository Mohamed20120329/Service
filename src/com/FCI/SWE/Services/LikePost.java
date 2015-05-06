package com.FCI.SWE.Services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class LikePost implements IPost{
	
	@POST
	@Path("/LikePostService")
	public boolean LikePostService(@FormParam("uname")String active_user_name ,
			@FormParam("postID") String postID) 
	{ 
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("LikePost");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		if (list.size() == 0)
		{
			Entity likePost = new Entity("LikePost", list.size()+1);
	        likePost.setProperty("uname", active_user_name);
	        likePost.setProperty("postID", postID);
	        likePost.setProperty("numberOfLikes", 1 );
			datastore.put(likePost);
			return true; 
		}
		boolean ok1 = false;
		long check = 0;
		String[] arrayofusers = null;
		String delim = "[,]";
		long numoflikes = 0;
		String usersLikes = "";
		long index = 0;
		for (Entity entity : pq.asIterable())
		{
			if (entity.getProperty("postID").toString().equals(postID))
			{
				arrayofusers = entity.getProperty("uname").toString().split(delim);
				usersLikes = entity.getProperty("uname").toString();
				numoflikes = Long.parseLong(entity.getProperty("numberOfLikes").toString()); 
				break;
			}
			else
			{
				check++;
			}
			index++;
		}
		if (check == list.size())
		{
			Entity likePost = new Entity("LikePost", list.size()+1);
	        likePost.setProperty("uname", active_user_name);
	        likePost.setProperty("postID", postID);
	        likePost.setProperty("numberOfLikes", 1 );
			datastore.put(likePost);
			return true; 
		}
		else
		{
			for (int i = 0 ; i < arrayofusers.length ; i++)
			{
				if (arrayofusers[i].toString().equals(active_user_name))
				{
					ok1 = true;
					break;
				}
			}
			if (ok1)
			return false;
			++numoflikes;
			Entity likePost = new Entity("LikePost", index+1);
	        likePost.setProperty("uname", usersLikes+","+active_user_name);
	        likePost.setProperty("postID", postID);
	        likePost.setProperty("numberOfLikes",numoflikes);
			datastore.put(likePost);
			return true;
	}
	}
	public ArrayList<String> allUserOf ()
	{
		ArrayList<String> all = new ArrayList<>();						
		return all;
	}
	@Override
	public boolean SharePostService(String active_user_name, String postID) {
		// TODO Auto-generated method stub
		return false;
	}

}
