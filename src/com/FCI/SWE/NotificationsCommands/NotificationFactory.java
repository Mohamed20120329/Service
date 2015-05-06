package com.FCI.SWE.NotificationsCommands;

import com.FCI.SWE.ServicesModels.Notification;

public class NotificationFactory {
	public static ICommand createCommand(String id){
		Notification notification = Notification.getNotification(id);
		String className = Notification.getClassName(notification.getType());
		try {
			ICommand command = (ICommand)Class.forName("com.FCI.SWE.NotificationsCommands."+className).newInstance();
			command.setParams(notification.getParams());
			return command;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
