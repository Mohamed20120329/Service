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

public class CreatePage implements IPage{
	
	@POST
	@Path("/CreatePageService")
	public void CreatePage(@FormParam("PageName") String PageName,
			@FormParam("Category") String Category, @FormParam("uname") String cur_user){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("Page");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
        Entity newPost = new Entity("Page", list.size() + 1);
        
		newPost.setProperty("PageName", PageName);
		newPost.setProperty("Category", Category);
		newPost.setProperty("Fans", cur_user);
		newPost.setProperty("LikesCount", 1);
		
		datastore.put(newPost);
	}

	@Override
	public boolean LikePage(String PageName, String fan) {
		// TODO Auto-generated method stub
		return false;
	}

}
