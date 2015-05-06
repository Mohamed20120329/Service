package com.FCI.SWE.Services;

import javax.ws.rs.FormParam;

public interface IPost {

	public boolean LikePostService(@FormParam("uname")String active_user_name ,
			@FormParam("postID") String postID) ;
	 public boolean SharePostService(@FormParam("uname")String active_user_name ,
			 @FormParam("PostID") String postID );
}
