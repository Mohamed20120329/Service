package com.FCI.SWE.NotificationsCommands;

public abstract class ICommand {
	protected String params;
	public ICommand(){
		
	}
	
	public void setParams(String params){
		this.params = params;
	}
	
	public String getParams(){
		return params;
	}
	
	public ICommand(String params){
		this.params = params;
	}
	public abstract String execute();
}
