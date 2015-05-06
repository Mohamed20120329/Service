package com.FCI.SWE.ServicesModels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.Models.User;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

/**
 * <h1>User Entity class</h1>
 * <p>
 * This class will act as a model for user, it will holds user data
 * </p>
 *
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 */
public class UserEntity extends IEntity {
	private String name;
	private String email;
	private String password;
	private long id;

	public UserEntity() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor accepts user data
	 * 
	 * @param name
	 *            user name
	 * @param email
	 *            user email
	 * @param password
	 *            user provided password
	 */
	public UserEntity(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

	private void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPass() {
		return password;
	}

	public Boolean notifyUser(String notificationType, String notificationTime,
			String params) {
		// TODO Auto-generated method stub
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		
		Entity entity = new Entity("Notification");
		entity.setProperty("user", getUniqueField());
		entity.setProperty("type", notificationType);
		entity.setProperty("timestamp", notificationTime);
		entity.setProperty("params", params);
		
		if(datastore.put(entity)!=null)
			return true;
		return false;
		
	}
	public static Vector<UserEntity> searchUser(String uname) {
		DatastoreService dataStore = DatastoreServiceFactory
				.getDatastoreService();
		Query gae = new Query("users");
		PreparedQuery preparedQuery = dataStore.prepare(gae);
		Vector<UserEntity> returnedUsers = new Vector<UserEntity>();
		for (Entity entity : preparedQuery.asIterable()) {
			String currentName = entity.getProperty("name").toString();
			if (currentName.contains(uname)) {
				UserEntity user = new UserEntity(entity.getProperty("name").toString(),
						entity.getProperty("email").toString(), entity
						.getProperty("password").toString());
				user.setId(entity.getKey().getId());
				returnedUsers.add(user);
			}
		}
		return returnedUsers;

	}
	public String getUniqueField() {
		// TODO Auto-generated method stub
		
		return name;
	}
	
	public JSONObject toJson(){
		JSONObject object = new JSONObject();
		object.put("name", name);
		object.put("email", email);
		object.put("id", id);
		return object;
	}
	public static UserEntity searchSingleUser(String uname) {
		DatastoreService dataStore = DatastoreServiceFactory
				.getDatastoreService();
		Query gae = new Query("users");
		PreparedQuery preparedQuery = dataStore.prepare(gae);
		for (Entity entity : preparedQuery.asIterable()) {
			entity.getKey().getId();
			String currentName = entity.getProperty("name").toString();
			if (currentName.contains(uname)) {
				UserEntity returnedUser = new UserEntity(entity.getProperty("name").toString(),
						entity.getProperty("email").toString(), entity
								.getProperty("password").toString());
				returnedUser.setId(entity.getKey().getId());
			return returnedUser;
			}
		}
		return null;
	}
	
	public Boolean saveEntity() {
		// TODO Auto-generated method stub
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try {
			Entity user = new Entity("users");

			user.setProperty("name", this.name);
			user.setProperty("email", this.email);
			user.setProperty("password", this.password);

			datastore.put(user);
			txn.commit();
			id = user.getKey().getId();
			
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		return true;

	}


	public IEntity getSingleEntity(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 
	 * This static method will form UserEntity class using user name and
	 * password This method will serach for user in datastore
	 * 
	 * @param name
	 *            user name
	 * @param pass
	 *            user password
	 * @return Constructed user entity
	 */

	public static UserEntity getUser(String name, String pass) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("name").toString().equals(name)
					&& entity.getProperty("password").toString().equals(pass)) {
				UserEntity returnedUser = new UserEntity(entity.getProperty(
						"name").toString(), entity.getProperty("email")
						.toString(), entity.getProperty("password").toString());
				returnedUser.setId(entity.getKey().getId());
				return returnedUser;
			}
		}

		return null;
	}

	public static Boolean CheckFriend(String friendName, String active_user_name) 
	{
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {

			if (entity.getProperty("name").toString().equals(friendName)) {
				Query reqQuery = new Query("Request");

				PreparedQuery preq = datastore.prepare(reqQuery);

				for (Entity Req_entity : preq.asIterable()) {
					if (Req_entity.getProperty("frinedName").toString()
							.equals(friendName)
							&& Req_entity.getProperty("uname").toString().equals(active_user_name)
							&& Req_entity.getProperty("status").toString().equals("Accepted")
							|| Req_entity.getProperty("uname").toString()
									.equals(friendName)
							&& Req_entity.getProperty("frinedName").toString().equals(active_user_name)
							&& Req_entity.getProperty("status").toString().equals("Accepted"))
							 {
						return false ;
					}
				}
				return true ;
			}
		}
		return false;
	}

	public Boolean saveRequest(String friendName, String active_user_name) { 
																				
																				
			if (!CheckFriend(friendName,active_user_name)) return false;
				
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Query gaeQuery = new Query("Request");
			PreparedQuery pq = datastore.prepare(gaeQuery);		

			List<Entity> list2 = pq.asList(FetchOptions.Builder.withDefaults());	

			Entity request = new Entity("Request", list2.size() + 1);
			request.setProperty("uname", active_user_name);
			request.setProperty("frinedName", friendName);
			request.setProperty("status", "pending");
			datastore.put(request);

			return true;
	}

	/**
	 * This method will be used to save user object in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public Boolean saveUser() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		try {
			Entity employee = new Entity("users", list.size() + 2);

			employee.setProperty("name", this.name);
			employee.setProperty("email", this.email);
			employee.setProperty("password", this.password);

			datastore.put(employee);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		return true;

	}
	
	public Boolean saveMessage(String friendName, String active_user_name,String message) 
	{ 
		if (CheckFriend(friendName, active_user_name)) 
		{
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query reqQuery = new Query("Message");
			PreparedQuery preq = datastore.prepare(reqQuery);
			List<Entity> list2 = preq.asList(FetchOptions.Builder.withDefaults());
			Entity messag = new Entity("Message", list2.size() + 1);
			messag.setProperty("uname", active_user_name);
			messag.setProperty("frinedName", friendName);
			messag.setProperty("message", message);
			messag.setProperty("status", "pending");
			datastore.put(messag);
			return true;
		}
		return false;
	}

}
