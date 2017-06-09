package com.iBook.jaas;

import com.iBook.datastore.*;
import com.iBook.datastore.users.*;
import com.iBook.utilities.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/*
   
   Create a file called jaas.config containing the following...
   
   IBookLogin
   {
       com.iBook.jaas.IBookLoginModule required application.name=iBook debug=true;
   };
   
   ...and put it in tomcat's conf directory, then edit catalina.sh/catalina.bat to 
   point to it, as follows...
   
   JAVA_OPTS="$JAVA_OPTS -Djava.security.auth.login.config=$CATALINA_BASE/conf/jaas.config"
    
   LoginModule code copied from www.byteslounge.com/subcategory/jaas...
   
 */
public class IBookLoginModule implements LoginModule
{
	private CallbackHandler handler;
	private Subject subject;
	private UserPrincipal userPrincipal;
	private RolePrincipal rolePrincipal;
	private String login;
	private List<String> userGroups;
	
	@Override
	public void initialize(Subject subject,
			               CallbackHandler callbackHandler,
			               Map<String, ?> sharedState,
			               Map<String, ?> options)
	{
		handler = callbackHandler;
		this.subject = subject;
	}
	
	@Override
	public boolean login() throws LoginException
	{
		// PropsManager constructor with no argument has severely limited
		// resources---the user list XML.
		PropsManager propsMgr = new PropsManager();
		Vector<UserAccount> users = propsMgr.getUsers();
		
		Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("login");
		callbacks[1] = new PasswordCallback("password", true);
		
		try
		{
			handler.handle(callbacks);
			String name = ((NameCallback) callbacks[0]).getName();
			String password = String.valueOf(((PasswordCallback) callbacks[1]).getPassword());
			
			Iterator<UserAccount> usersI = users.iterator();
			UserAccount userAccount = null;
			while(usersI.hasNext())
			{
				userAccount = usersI.next();
				if(name != null && 
				   userAccount.getUserId().equals(name) && 
				   password != null &&
				   userAccount.getPassword().equals(password))
				{
					login = name;
					userGroups = new ArrayList<String>();
					userGroups.add("admin");
					String pathMrkr = PropsManager.getPathMarker();
					String path_to_user_dir = PropsManager.getPathToResources() + "users" + pathMrkr + name + pathMrkr + "resources" + pathMrkr; 
					purgeEbooks(path_to_user_dir);
					updateTemplates(path_to_user_dir);
					return true;
				}
			}
			throw new LoginException("Authentication failed");
		}
		catch (IOException e)
		{
			throw new LoginException(e.getMessage());
		}
		catch(UnsupportedCallbackException e)
		{
			throw new LoginException(e.getMessage());
		}
	}
	
	@Override
	public boolean commit() throws LoginException
	{
		userPrincipal = new UserPrincipal(login);
		subject.getPrincipals().add(userPrincipal);
		
		if(userGroups != null && userGroups.size() > 0)
		{
			for(String groupName : userGroups)
			{
				rolePrincipal = new RolePrincipal(groupName);
				subject.getPrincipals().add(rolePrincipal);
			}
		}
		return true;
	}
	
	@Override
	public boolean abort() throws LoginException
	{
		return false;
	}
	
	@Override
	public boolean logout() throws LoginException
	{
		subject.getPrincipals().remove(userPrincipal);
		subject.getPrincipals().remove(rolePrincipal);
		return true;
		
	}
	
	private void purgeEbooks(String path_to_ebooks)
	{
		File ebooksDir = new File(path_to_ebooks);
		String[] ebooks = ebooksDir.list();
		for(int count = 0; count < ebooks.length; count++)
		{
			File epub = new File(path_to_ebooks + ebooks[count]);
			if(epub.getName().endsWith(".epub") || epub.getName().endsWith(".pdf"))
				epub.delete();
		}
	}
	
	private void updateTemplates(String path_to_user_dir)
	{
		boolean addTemplate = false;
		
		String pathMrkr = PropsManager.getPathMarker();
		String path_to_system_templates = path_to_user_dir.substring(0, path_to_user_dir.indexOf("users")) + pathMrkr + "templates" + pathMrkr;

		File systemTemplatesDir = new File(path_to_system_templates);
		File userTemplatesDir = new File(path_to_user_dir + "templates" + pathMrkr);

		String[] sysTemplates = systemTemplatesDir.list();
		String[] usrTemplates = userTemplatesDir.list();
		
		// cycle through all system-defined templates
		for(int sysCount = 0; sysCount < sysTemplates.length; sysCount++)
		{
			// assume each template needs to be added
			addTemplate = true;
			// cycle through all templates in user's templates dir
			for(int usrCount = 0; usrCount < usrTemplates.length; usrCount++)
			{
				// does a template with the system template's name already exist in the user dir? 
				if(sysTemplates[sysCount].equals(usrTemplates[usrCount]))
				{
					// yes, don't add it
					addTemplate = false;
					break;
				}
			}
			// if addTemplate is still true, add the template to the user dir
			if(addTemplate)
			{
				try
				{
					String sysTemplate = Utilities.read_file(path_to_system_templates + sysTemplates[sysCount]);
					Utilities.write_file(sysTemplates[sysCount], sysTemplate, true);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
