
package com.FCI.SWE.Services;

import javax.ws.rs.FormParam;


public interface ICreatePost {
	public boolean UserPost(@FormParam("Owner") String owner, @FormParam("Content") String content,
			@FormParam("Feeling") String feeling, @FormParam("Privacy") String privacy);
	public void SearchHashTag(String Content);
	public void SaveHashTagPost(String Content, String hashTag);
	public boolean FriendPost(@FormParam("Owner") String owner, @FormParam("Friend") String friend ,
			@FormParam("Content") String content, @FormParam("Feeling") String feeling,
			@FormParam("Privacy") String privacy);

}
