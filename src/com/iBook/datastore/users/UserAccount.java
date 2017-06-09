package com.iBook.datastore.users;

public class UserAccount
{
	private String emailAddr = "";
	private String password = "";
	private String role = "";
	private String userDir = "";
	private String userId = "";
	
	public UserAccount()
	{
	}
	
	public String getEmailAddr()
	{
		return emailAddr;
	}
	
	public void setEmailAddr(String emailAddr)
	{
		this.emailAddr = emailAddr;
	}
	
	public String getUserId()
	{
		return userId;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public String getRole()
	{
		return role;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setRole(String role)
	{
		this.role = role;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public void setUserDir(String userDir)
	{
		this.userDir = userDir;
	}
	public String getUserDir()
	{
		return userDir;
	}
}
