package com.FCI.SWE.Services;

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

public class FriendPost implements ICreatePost{
	
	public void SearchHashTag(String Content){
		if(Content.contains("#")){				
			String hashTag ="";
			int indexstart=Content.indexOf("#");
			for(int i = indexstart+1 ; i < Content.length(); i ++){
				if(Content.charAt(i)==' ') break ;
				hashTag += Content.charAt(i) ;
			}			
			System.out.println(hashTag);
			SaveHashTagPost(Content,hashTag) ;	
		}				
	}
	@POST
	@Path("/FriendPostService")	
	public boolean FriendPost(@FormParam("Owner") String owner, @FormParam("Friend") String friend ,
			@FormParam("Content") String content, @FormParam("Feeling") String feeling,
			@FormParam("Privacy") String privacy)
	{				
        
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("Post");

		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
        Entity newPost = new Entity("Post", list.size() + 1);
        newPost.setProperty("PostID", list.size() + 1);
        newPost.setProperty("Owner", owner);
		friend = "Friend/" + friend ;
		newPost.setProperty("Type", friend);
		newPost.setProperty("Content", content);
		newPost.setProperty("Feeling", feeling);		
		newPost.setProperty("Privacy", privacy);
		datastore.put(newPost);
		
		SearchHashTag(content) ;
		
		return true;
	}
	@Override
	public boolean UserPost(String owner, String content, String feeling,
			String privacy) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void SaveHashTagPost(String Content, String hashTag) {
		// TODO Auto-generated method stub
		
	}

}
