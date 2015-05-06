package com.FCI.SWE.Services;

public interface MassageInterface {
	
	public void notifyObservers();
	public String getUpdate(Users observ);
	public String sendMessageService(String from_name,String to_names,String messg_text);
	public void replyMessageService();
	
}
