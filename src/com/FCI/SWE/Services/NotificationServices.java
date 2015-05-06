package com.FCI.SWE.Services;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.FCI.SWE.NotificationsCommands.ICommand;
import com.FCI.SWE.NotificationsCommands.NotificationFactory;
import com.FCI.SWE.ServicesModels.Notification;



@Path("/")
@Produces("text/html")
public class NotificationServices {

	@POST
	@Path("/NotificationService")
	public String handleNotification(@FormParam("id")String id){
		ICommand command = NotificationFactory.createCommand(id);
		return command.execute();
	}
	
	@POST
	@Path("/GetNotificationService")
	public String getAllNotification(@FormParam("user")String user){
		return Notification.getAllNotifications(user);
	}
}
