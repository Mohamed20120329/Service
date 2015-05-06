package com.FCI.SWE.Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import com.FCI.SWE.Models.User;
import com.FCI.SWE.ServicesModels.UserEntity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * This class contains REST services, also contains action function for web
 * application
 * 
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 *
 */
@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class UserServices {

	/*
	 * @GET
	 * 
	 * @Path("/index") public Response index() { return Response.ok(new
	 * Viewable("/jsp/entryPoint")).build(); }
	 */

	/*
	 * @POST
	 * 
	 * @Path("/SearchService") public String searchFriend(@FormParam("uname")
	 * String uname){
	 * 
	 * }
	 */

	/**
	 * Registration Rest service, this service will be called to make
	 * registration. This function will store user data in data store
	 * 
	 * @param uname
	 *            provided user name
	 * @param email
	 *            provided user email
	 * @param pass
	 *            provided password
	 * @return Status json
	 */
	@POST
	@Path("/RegistrationService")
	public String registrationService(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) {
		UserEntity user = new UserEntity(uname, email, pass);
		user.saveUser();
		JSONObject object = new JSONObject();
		object.put("Status", " Registeration OK");
		return object.toString();
	}

	/**
	 * Login Rest Service, this service will be called to make login process
	 * also will check user data and returns new user from datastore
	 * 
	 * @param uname
	 *            provided user name
	 * @param pass
	 *            provided user password
	 * @return user in json format
	 */
	@POST
	@Path("/LoginService")
	public String loginService(@FormParam("uname") String uname,
			@FormParam("password") String pass) {
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.getUser(uname, pass);
		if (user == null) {
			object.put("Status", "Failed");

		} else {
			object.put("Status", "Login OK");
			object.put("name", user.getName());
			object.put("email", user.getEmail());
			object.put("password", user.getPass());
			object.put("id", user.getId());
		}
		return object.toString();

	}

	@POST
	@Path("/GetFriendRequestService")
	public String GetFriendRequestService(@FormParam("uname")String active_user_name) {
		
	    String friends ="";
		JSONObject obj = new JSONObject() ;
		boolean ok = false ;
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("Request");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("frinedName").toString().equals(active_user_name)) {	
				if(ok) friends += "," ;
				friends += entity.getProperty("uname").toString() ;					
				ok = true ;	
			}
		}
		
		if(ok) {			
			obj.put("Status", "true") ;
		}
		else{			
			obj.put("Status", "Failed") ;
		}
		obj.put("uname", friends) ;
		return obj.toString();	
	}
/*
 * 
 */
	@POST
	@Path("/AddFriendRequestService")
	public String AddFriendRequestService(
			@FormParam("friendName") String friendName, @FormParam("uname")String active_user_name) { // add friend
		
		UserEntity user = new UserEntity();
		JSONObject json = new JSONObject();

		if (user.saveRequest(friendName, active_user_name)) {
			json.put("Status", "true");
		} else
			json.put("Status", "Failed");

		return json.toString();
	}

	@POST
	@Path("/AcceptFriendService")
	public Boolean AcceptFriendService(
			@FormParam("friendName") String friendName, @FormParam("uname") String current_active_user) { //

		Boolean ok = false;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("Request");

		PreparedQuery pq = datastore.prepare(gaeQuery);
        List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
        long index = 0 , c = 0;
        
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("frinedName").toString().equals(current_active_user)
					&& entity.getProperty("uname").toString().equals(friendName)
					&&entity.getProperty("status").toString().equals("pending"))
			{		
				ok = true;
				index = c;
				break;
			}
			c++;
		}
		
		if (!ok)
		{			
			return false;
		}
		Entity friend = new Entity("Request", index+1);
		friend.setProperty("frinedName", friendName);
     	friend.setProperty("uname", current_active_user); 	
		friend.setProperty("status", "Accepted");
		
	    datastore.put(friend);
		return true;
	}

	@POST
	@Path("/RefuseFriendService")
	public Boolean RefuseFriendService(
			@FormParam("friendName") String friendName) { //

		Boolean ok = false;

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("Request");

		PreparedQuery pq = datastore.prepare(gaeQuery);

		String active_user_name = User.getCurrentActiveUser().getName()
				.toString();

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("friendName").toString()
					.equals(active_user_name)
					&& entity.getProperty("uname").toString()
							.equals(friendName)) {

				// should delete from request table
				datastore.delete(entity.getKey());
				ok = true;
				break;
			}
		}
		if (!ok)
			return false;
		return true;
	}
	
	public void Sort(ArrayList<MyMap> posts){
		
		for(int i = 0; i < posts.size(); i ++){
			for(int j = i+1; j <posts.size();j ++){
				if(posts.get(j).count > posts.get(i).count) 
					Collections.swap(posts, i, j);
			}
		}
	}
	
	@POST
	@Path("/hashTagNameService")
	public String getHash(String hashTag){
		String allPosts = "" ;				
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("PostHashTag");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		for (Entity entity : pq.asIterable()){
			if(entity.getProperty("HashTag").toString().equals(hashTag)){
				allPosts = entity.getProperty("HashTagPost").toString();
				break ;
			}
		}
		System.out.println(allPosts);
		JSONObject obj = new JSONObject() ;
		obj.put("HashTagPost", allPosts) ;
		obj.put("Status", "success") ;
		return obj.toString() ;
	}
	@POST
	@Path("/TopHashTagService")	
	public String TopHashTag(){
		
		String allPosts = "" ;				
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("PostHashTag");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		
		ArrayList<MyMap> posts = new ArrayList<>();
		int hash_count = 0 ; String post = "" ;		
	    
		for (Entity entity : pq.asIterable()){
			 MyMap obj = new MyMap() ;
		     hash_count = Integer.parseInt(entity.getProperty("HashTagCount").toString() );	
		     post = entity.getProperty("HashTagPost").toString();	
		     obj.add(hash_count, post);
		     posts.add(obj) ;
		}
		Sort(posts) ;
		for(int i = 0; i<10 && i<posts.size(); i ++){
			allPosts += posts.get(i).post ;
			allPosts += "/";
		}
		JSONObject object = new JSONObject();
		object.put("TopHashtag", allPosts);
		object.put("Status", "success");
		return object.toString();
		
	}
	
	
	@POST
	@Path("/TimeLineService")
	public String TimeLine(@FormParam("PageName") String PageName, @FormParam("uname") String curr_user){
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("Post");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		String posts = "" , fan;
		for(Entity entity : pq.asIterable()){
			String pg = entity.getProperty("Type").toString() ;
			if(pg.contains("Page")){
				String [] pgName = pg.split("/") ;
				if(pgName[1].equals(PageName)){
					
					Query pgQuery = new Query("Page");
					PreparedQuery pgQ = datastore.prepare(pgQuery);
		
					for(Entity pg_entity : pgQ.asIterable()){
						if(pg_entity.getProperty("PageName").toString().equals(PageName)){								
							fan = pg_entity.getProperty("Fans").toString() ;
							if(entity.getProperty("Privacy").toString().equals("public") || fan.contains(curr_user)){
					    		posts += entity.getProperty("PostID").toString() + "@" + entity.getProperty("Content").toString() + ",";
					    		int see_count  = 0;
					    		see_count = Integer.parseInt(entity.getProperty("Seen").toString()) ;
					    		entity.setUnindexedProperty("Seen", see_count+1);
							}
							break ;
						}
					}	
									
				}
			}
		}
		System.out.println(posts);
		JSONObject obj = new JSONObject() ;
		obj.put("Status", "true") ;
        obj.put("Posts", posts);
		System.out.println(posts);
		return obj.toString() ;
	}
	
	public boolean can_see(String privacy, String UserName, String active_user_name){
		
		
			if(privacy.equals("public")){
				return true ;
			}
			else if(privacy.equals("private")){
				if(!UserEntity.CheckFriend(UserName, active_user_name)){
					return true ;
				}
			}else if(privacy.contains("custom")){				
				String name_custom = privacy.substring(7, privacy.length()) ;
				String [] frnds = name_custom.split(",") ;
				for(int i = 0; i < frnds.length; i ++){
					if(frnds.equals(active_user_name)){
						return true ;
					}
				}
			}else{
				String frnds = privacy.substring(6, privacy.toString().length()) ;
				if(!UserEntity.CheckFriend(frnds, active_user_name)){					
					return true ;
				}
			}
		
		return false ;
	}	
	
	@POST
	@Path("/UserTimeLineService")
	public String  UserTimeLine(@FormParam("UserName") String UserName, @FormParam("uname") String curr_user){
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("Post");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		String posts = "" ; boolean ok = false ;
		
		for(Entity entity : pq.asIterable()){
			if( ( entity.getProperty("Owner").toString().equals(UserName)&&!(entity.getProperty("Type").toString().contains("Friend")) ) 
					|| entity.getProperty("Type").toString().contains(UserName)){
					if( can_see(entity.getProperty("Privacy").toString(), UserName, curr_user) ){
						posts += entity.getProperty("PostID").toString() + "@" + entity.getProperty("Content").toString() + "," ;						
						ok = true ;
					}
			}
		}
		
		System.out.println(posts);
		JSONObject obj = new JSONObject() ;
		obj.put("Status", "true") ;
        obj.put("Posts", posts);
		System.out.println(posts);
		return obj.toString() ;	
	}
	
	@POST
	@Path("/CurrentTimeLineService")
	public String CurrentTimeLine(@FormParam("uname") String curr_user){
                
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("Post");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		String posts = "" ;  JSONObject obj = new JSONObject() ;
		for(Entity entity : pq.asIterable()){
			if(entity.getProperty("Owner").toString().equals(curr_user)&&entity.getProperty("Type").toString().equals("none")
					|| entity.getProperty("Type").toString().contains(curr_user)){			
				posts += entity.getProperty("PostID").toString() + "@" + entity.getProperty("Content").toString() + "," ;
			}
		}
                obj.put("Status", "true") ;
                obj.put("Posts", posts);
		System.out.println(posts);
		return obj.toString() ;
	}
	
	@POST
	@Path("/SignoutService")
	public String SignoutService() {
		JSONObject object = new JSONObject();
		// User ue = new User();
		User.finishSignout();
		object.put("Status", "Signout OK");
		return object.toString();
	}
	
	/*@POST
	@Path("/SendMessageService")
	public String SendMessageService(@FormParam("friendName") String friendName, @FormParam("uname")String current_active_user, @FormParam("message") String message) {
		UserEntity user = new UserEntity();
		JSONObject json = new JSONObject();

		if (user.saveMessage(friendName, current_active_user, message)) {
			json.put("Status", "true");
		} else
			json.put("Status", "Failed");

		return json.toString();
	}*/

}