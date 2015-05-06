package com.FCI.SWE.Services;

import javax.ws.rs.FormParam;

public interface IPage {
	public void CreatePage(@FormParam("PageName") String PageName,
			@FormParam("Category") String Category, @FormParam("uname") String cur_user);
	public boolean LikePage(@FormParam("PageName") String PageName, @FormParam("uname") String fan);

}
