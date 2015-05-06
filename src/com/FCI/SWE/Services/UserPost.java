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

public class UserPost implements ICreatePost{
	
	
	public void SaveHashTagPost(String Content, String hashTag){
		boolean ok = false ;
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("PostHashTag");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		System.out.println("SavehashTag");
		for (Entity entity : pq.asIterable()){
			if(entity.getProperty("HashTag").toString().equals(hashTag)){
				int hash_count = Integer.parseInt(entity.getProperty("HashTagCount").toString() );
				hash_count ++ ;
				entity.setProperty("HashTagCount", hash_count);
				entity.setProperty("HashTagPost", entity.getProperty("HashTagPost").toString()+"/"+Content);				
				ok = true ;
				break ;
			}
		}
		
		if(!ok){
			Entity newHashTag = new Entity("PostHashTag", list.size() + 1);
			newHashTag.setProperty("HashTag", hashTag);
			newHashTag.setProperty("HashTagCount", 1);
			newHashTag.setProperty("HashTagPost", Content);
			datastore.put(newHashTag) ;
		}        
	}
	
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
	@Path("/UserPostService")
	public boolean UserPost(@FormParam("Owner") String owner, @FormParam("Content") String content,
			@FormParam("Feeling") String feeling, @FormParam("Privacy") String privacy){

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("Post");

		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
        Entity newPost = new Entity("Post", list.size() + 1);
        newPost.setProperty("PostID", list.size() + 1);
        
		newPost.setProperty("Owner", owner);
		newPost.setProperty("Content", content);
		newPost.setProperty("Feeling", feeling);		
		newPost.setProperty("Privacy", privacy);
		
		newPost.setProperty("Type", "none");
		
		datastore.put(newPost);
		SearchHashTag(content) ;
		return true;
	}

	@Override
	public boolean FriendPost(String owner, String friend, String content,
			String feeling, String privacy) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
