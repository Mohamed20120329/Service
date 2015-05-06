package com.FCI.SWE.NotificationsCommands;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.ServicesModels.Message;

public class MessageCommand extends ICommand {

	public MessageCommand() {
		super();
	}

	@Override
	public String execute() {
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject)parser.parse(params);
			String conversation = obj.get("conversation").toString();
			JSONObject returnedObj = new JSONObject();
			returnedObj.put("data", Message.getAllMessages(conversation));
			return returnedObj.toJSONString();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	

}
