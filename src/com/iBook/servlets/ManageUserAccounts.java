package com.iBook.servlets;

import com.iBook.datastore.*;
import com.iBook.datastore.users.*;
import com.iBook.utilities.Utilities;

import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManageUserAccounts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private PropsManager propsMgr = null;
	private static String path_to_resources = PropsManager.getPathToResources();
	private static String pathMrkr = PropsManager.getPathMarker();
	
	private Vector<UserAccount> users = null;

    public ManageUserAccounts() 
    {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
        ServletOutputStream op = response.getOutputStream();
        
        // Get the value of all request parameters
        Enumeration<?> params = request.getParameterNames();
        
        boolean forgotPassword = false;
        boolean changeData = false;
        boolean signup = false;
        boolean viewAccountData = false;
        
        String key = "";
        String redirect_target = "login.jsp";
        String userId = "";
        String value = "";

        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
    		value = request.getParameter(key);
    		
    		if(key.compareTo("changeEmailSubmit") == 0)
    		{
    			changeData = true;
    		}
    		if(key.compareTo("changePasswordSubmit") == 0)
    		{
    			changeData = true;
    		}
    		if(key.compareTo("forgotPasswordSubmit") == 0)
    		{
    			forgotPassword = true;
    			changeData = true;
    		}
    		if(key.compareTo("signupSubmit") == 0)
    			signup = true;
    		
    		if(key.compareTo("userId") == 0)
    			userId = value;
    		
    		if(key.compareTo("viewAccountSubmit") == 0)
    			viewAccountData = true;
        }

        if(signup)
        {
        	// if no user id in arguments, propsMgr can only get the user accounts list
        	propsMgr = new PropsManager();
        	redirect_target = addAccount(request, response);
        }
        else if(changeData || viewAccountData)
        {
        	// user has asked to change passowrd or email, or has forgotten password
        	// first, check user's credentials
        	try
            {
//    	        String usersXml = path_to_resources + pathMrkr + "metadata" + pathMrkr + "userAccounts.xml";
        		String usersXml = propsMgr.getPathToUserDB();
    	        UserAccountsParser userAccountsParser = new UserAccountsParser();
    	        users = userAccountsParser.parse(usersXml);
            }
            catch(Exception e)
            {
            	e.printStackTrace();
            }
        	Iterator<UserAccount> usersI = users.iterator();
        	UserAccount userAccount = null;
        	boolean foundMatch = false;
        	while(usersI.hasNext())
        	{
        		userAccount = usersI.next();
        		if(userAccount.getUserId().compareTo(userId) == 0)
        		{
        			// userId provided by user matches a valid user account Id
        			foundMatch = true;
        			break;
        		}
        	}
        	if(foundMatch)
        	{
        		// user Id is valid, so either reset the password and email it to the email address
        		// in the user accounts XML...
        		if(forgotPassword)
        			redirect_target = forgotPassword(request, response);
        		else
        			// ...or proceed to the change/view data handler
        			redirect_target = manageData(request, response);
        	}
        	else
        		redirect_target = "index.html?msg=Invalid user ID.";
        }
        
        redirect_target = Utilities.replaceChars(redirect_target, " ", "%20", "all");
        String redirect = "<"
        		+ "html>\n"
                + "    <body onload=\"document.location = \'" + redirect_target + "\'\" />\n"
                + "</html>";
            response.setContentLength(redirect.length());
            op.write(redirect.getBytes(), 0, redirect.length());
	}
	
	private String manageData(HttpServletRequest request, HttpServletResponse response)
	{
		boolean changeEmail = false;
		boolean changePassword = false;
		boolean passwordValidated = false;
		boolean viewAccountData = false;
		
        // Get the value of all request parameters
        Enumeration<?> params = request.getParameterNames();
        
        String currentPassword = "";
        String newEmail = "";
        String key = "";
        String value = "";
        String newPassword = "";
        String password = "";
        String redirect_target = "/iBook/index.html";
        String msg = "";
        String userId = "";

        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
    		value = request.getParameter(key);
    		
    		if(key.compareTo("changeEmailSubmit") == 0)
    			changeEmail = true;
    		
    		if(key.compareTo("changePasswordSubmit") == 0)
    			changePassword = true;
    		
    		if(key.compareTo("currentPassword") == 0)
    			currentPassword = value;
    		
    		if(key.compareTo("newEmail") == 0)
    			newEmail = value;
    		
    		if(key.compareTo("newPassword") == 0)
    			newPassword = value;
    		
    		if(key.compareTo("password") == 0)
    			password = value;
    		
    		if(key.compareTo("userId") == 0)
    			userId = value;
    		
    		if(key.compareTo("viewAccountSubmit") == 0)
    			viewAccountData = true;
    		
        }
        
        // compensate for overloading this method to handle multiple requests
        if(viewAccountData)
        	currentPassword = password;
        
        // user ID was validated in doPost, so use it to get the users list
		propsMgr = new PropsManager(userId);
		users = propsMgr.getUsers();

		Iterator<UserAccount> usersI = users.iterator();
        UserAccount user = null;
        while(usersI.hasNext())
        {
        	// locate the user account to be changed...
        	user = usersI.next();
        	if(user.getUserId().equals(userId))
        	{
        		// validate the current password entered by the user...
        		if(user.getPassword().equals(currentPassword))
        		{
        			passwordValidated = true;
        			if(viewAccountData)
        			{
        				msg = "?msg=Email: " + user.getEmailAddr();
        				break;
        			}
        			if(changePassword)
        			{
        				// ...and either change the password as requested...
	        			user.setPassword(newPassword);
	                	msg = Utilities.replaceChars("?msg=Your password has been changed.", " ", "%20", "all");
        			}
        			else if(changeEmail)
        			{ 
        				// ...or reset the email address
        				user.setEmailAddr(newEmail);
        	        	msg = Utilities.replaceChars("?msg=Your email address has been changed.", " ", "%20", "all");
        			}
        			String xml = updateXml(users);
//    		        Utilities.write_file(path_to_resources + "metadata" + pathMrkr + "userAccounts.xml", xml, true);
    		        Utilities.write_file(propsMgr.getPathToUserDB(), xml, true);
    		        String data = changePassword? "password" : "email address";
    		        msg = "?msg=Your " + data + " has been changed.";
        		}
        	}
        }
        if(!passwordValidated)
        	msg = "?msg=Invalid user credentials.";
        
		return redirect_target + msg;
	}

	private String forgotPassword(HttpServletRequest request, HttpServletResponse response)
	{
		String key = "";
		String msg = "";
		String emailAddr = "";
		String redirect_target = "/iBook/index.html";
		String userId = "";
		String value = "";
		
        // Get the value of all request parameters
        Enumeration<?> params = request.getParameterNames();
        
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
    		value = request.getParameter(key);
    		
    		if(key.compareTo("userId") == 0)
    			userId = value;
        }    
        // user account was validated in doPost(), so locate the user account for the 
        // request...
        Iterator<UserAccount> usersI = users.iterator();
        UserAccount user = null;
        int userCount = 0;
        while(usersI.hasNext())
        {
        	user = usersI.next();
        	// found it; get the email address
        	if(user.getUserId().equals(userId))
        	{
        		emailAddr = user.getEmailAddr();
        		break;
        	}
        	userCount++;
        }
        // generate a new random password
    	String password = getNewPassword();
    	// mail password to user's email account
    	Mailer mailer = new Mailer(emailAddr, password);
    	// set the new password in the user obj
    	user.setPassword(password);
    	// remove the user account data with the old password from the users list
    	users.remove(userCount);
    	// add the new user account data with new password
    	users.add(user);
    	// write a new users XML
        String xml = updateXml(users);
//        Utilities.write_file(path_to_resources + "metadata" + pathMrkr + "userAccounts.xml", xml, true);
        Utilities.write_file(propsMgr.getPathToUserDB(), xml, true);
    	msg = "?msg=A new password has been emailed to you.";

    	return redirect_target + msg;
	}

	private String addAccount(HttpServletRequest request, HttpServletResponse response)
	{
		UserAccount newAccount = new UserAccount();
		Vector<UserAccount> users = propsMgr.getUsers();
		newAccount.setRole("user");
		
        // Get the value of all request parameters
        Enumeration<?> params = request.getParameterNames();
        
        String key = "";
        String msg = "";
        String redirect_target = "/iBook/index.html";
        String userId = "";
        String value = "";

        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
    		value = request.getParameter(key);
    		
    		if(key.compareTo("newPassword") == 0)
    			newAccount.setPassword(value);

    		if(key.compareTo("userId") == 0)
    		{
    			userId = value;
    			newAccount.setUserId(userId);
    		}
    		
    		if(key.compareTo("emailAddr") == 0)
    			newAccount.setEmailAddr(value);
        }
        if(userId.length() == 0)
        {
        	msg = "?msg=Invalid user ID.";
        }
        else
        {
	        Iterator<UserAccount> usersI = users.iterator();
	        UserAccount user = null;
	        boolean accountExists = false;
	        while(usersI.hasNext())
	        {
	        	user = usersI.next();
	        	if(user.getUserId().compareTo(newAccount.getUserId()) == 0)
	        	{
	        		accountExists = true;
	        		msg = "?msg=User ID has already been claimed.";
	        		break;
	        	}
	        }
	        if(!accountExists)
	        {
	        	newAccount.setUserDir(makeUserDir(newAccount.getUserId()));
	//	        propsMgr.addUserAccount(newAccount);
	        	users.add(newAccount);
		        String xml = updateXml(users);
//		        Utilities.write_file(path_to_resources + "metadata" + pathMrkr + "userAccounts.xml", xml, true);
		        Utilities.write_file(propsMgr.getPathToUserDB(), xml, true);
		        msg = "?msg=Account created.";
	        }
        }
		return redirect_target + msg;
	}
	private String updateXml(Vector<UserAccount> users)
	{
		XmlFactory xmlFactory = new XmlFactory();
		return xmlFactory.doNewUserAccountsXml(users);
	}
	private String makeUserDir(String userId)
	{
		/*
		 * Need to create the following directory structure under userId dir:
		 * 
		 * resources\
		 *    epub\
		 *       META-INF\
		 *       OEBPS\
		 *           Styles\
		 *           Text\
		 *    manuscripts\
		 *    templates\
		 */
		String fileXfrText = "";
		String userDir = Utilities.replaceChars(userId, "@", "_", "all");
		String[] dirs = {
				"epub",
				"epub" + pathMrkr + "META-INF",
				"epub" + pathMrkr + "OEBPS",
				"epub" + pathMrkr + "OEBPS" + pathMrkr + "Styles",
				"epub" + pathMrkr + "OEBPS" + pathMrkr + "Text",
				"manuscripts",
				"templates"
		};
		String[] files =  { 
				// resources
				"bookList.xml", "characterList.xml", "locationList.xml",
				// resources/epub/META-INF
				"container.xml",
				// resources/epub/OEBPS/Styles
				"epub_styles.css",
				"page-template.xpgt"
				};
		String[] fileText = {
				"<booklist/>",
				"<characterlist/>",
				"<locationlist/>"
				};
		userDir = path_to_resources + 
				  "users" + 
				  pathMrkr + 
				  Utilities.replaceChars(userDir, ".", "_", "all") + 
				  pathMrkr + 
				  "resources" + 
				  pathMrkr;
		File newDir = new File(userDir);
		
		try
		{
			newDir.mkdirs();
			for(int count = 0; count < dirs.length; count++)
			{
				newDir = new File(userDir + dirs[count]);
				newDir.mkdir();
			}
			for(int count = 0; count < files.length; count++)
			{
				if(count < 3)
				{
					Utilities.write_file(userDir + files[count], fileText[count], true);
				}
				if(count == 3)
				{
					fileXfrText = Utilities.read_file(
							path_to_resources + 
							"epub" + 
							pathMrkr + 
							"META-INF" + 
							pathMrkr + 
							"container.xml");
					Utilities.write_file(
							userDir + 
							dirs[1] + 
							pathMrkr + 
							"container.xml", 
							fileXfrText, true);
				}
				if(count == 4)
				{
					fileXfrText = Utilities.read_file(
							path_to_resources + 
							"epub" + 
							pathMrkr + 
							"OEBPS" + 
							pathMrkr +
							"Styles" +
							pathMrkr +
							"epub_styles.css");
					Utilities.write_file(
							userDir + 
							dirs[3] + 
							pathMrkr + 
							"epub_styles.css", 
							fileXfrText, true);
				}
				if(count == 5)
				{
					fileXfrText = Utilities.read_file(
							path_to_resources + 
							"epub" + 
							pathMrkr + 
							"OEBPS" + 
							pathMrkr +
							"Styles" +
							pathMrkr +
							"page-template.xpgt");
					Utilities.write_file(
							userDir + 
							dirs[3] + 
							pathMrkr + 
							"page-template.xpgt", 
							fileXfrText, true);
				}
			}
			File templateDir = new File(path_to_resources + "templates");
			String[] templates = templateDir.list();
			String path_to_templates = "";
			{
				for(int count = 0; count < templates.length; count++)
				{
					path_to_templates = path_to_resources + "templates" + pathMrkr;
					fileXfrText = Utilities.read_file(path_to_templates + templates[count]);
					Utilities.write_file(userDir + "templates" + pathMrkr + templates[count], fileXfrText, true);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		// newUserDir.renameTo(new File(newDirName)), above, produced a directory of the  correct name,
		// but newUserDir.getName() still produces the old name, so we need to juggle a bit to get the 
		// correct value for the return...
		return userDir;
	}
	
	private String makeNameUnique(String dirname)
	{
		int count = 0;
		int idx = 0;
		// does dirname already end with (n)?
		if(dirname.endsWith(")") && (idx = dirname.indexOf("(")) != -1)
		{
			// find out what n is...
			count = new Integer(dirname.substring(idx + 1, dirname.length() - 1));
			// now strip off the (n), so that it can be rest
			dirname = dirname.substring(0, idx);
		}
		// add (n) to dirname, where n is count + 1
		dirname = dirname + "(" + new Integer(count + 1).toString() + ")";
		File newDir = new File(dirname);
		if(newDir.exists())
			dirname = makeNameUnique(dirname);
		return dirname;
	}

	// new-password generator
    private String getNewPassword()
    {
    	Date date = new Date();
        Random rand = new Random(date.getTime());

        String alphaCased = "";
        String alphas = "";
        String newString = "";
        String numerics = "";
        String specials = "";
        String specialsBase = "!#$%^*()_+-{}[]";
        
        int count = 0;
        int num = 0;
        int alphaCase = 0;
        // length of new password
        int len = 12;
        
        for(count = 0; count < len; count++)
        {    
            num = rand.nextInt(10);
            numerics += Integer.toString(num);
            
            num = rand.nextInt(26);
            alphas += (char) (97 + num);
            
            num = rand.nextInt(specialsBase.length());
            specials += specialsBase.charAt(num);
        }

        count = 0;
        while(newString.length() < len)
        {
            num = rand.nextInt(3);
            switch(num)
            {
            case 0:
                alphaCased += alphas.charAt(count);
                alphaCase = rand.nextInt(2);
                alphaCased = alphaCase > 0 ? alphaCased.toUpperCase() : alphaCased.toLowerCase(); 
                newString += alphaCased;
                alphaCased = "";
                break;
            case 1:
                newString += numerics.charAt(count);
                break;
            default:
                newString += specials.charAt(count);
                break;
            }
            count++;
        }
        
        return newString;
    }
}
