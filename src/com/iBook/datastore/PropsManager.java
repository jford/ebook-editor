package com.iBook.datastore;

import java.io.File;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.web.*;
import org.apache.logging.log4j.web.appender.*;

import com.iBook.datastore.books.*;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.locations.*;
import com.iBook.datastore.manuscripts.*;
import com.iBook.datastore.users.*;
import com.iBook.utilities.Utilities;

public class PropsManager
{
    private BookManager         library            = null;
    private CharacterManager    centralCasting     = null;
    private LocationManager     locationManager    = null;
    private ManuscriptManager   agent              = null;
    private Vector<String>      list               = null;
    private Vector<UserAccount> users              = null;
    
    private static String pathMrkr = System.getProperty("file.separator");
    private static String path_to_resources  = findResources();
    private static String path_to_user_dir ="";
    private static String path_to_user_db = "";
    private static String iBookSystemNameFull = "eBook Editor App";
    private static String iBookSystemNameShort = "eBook App";
    
    // active Manuscript and activeTemplate are transient objects, never 
    // persisted from state to state.  Use setActive...() in the propsMgr that will
    // be passed on as an argument, then use getActive() to retrieve the object. 
    private TemplateProfile   activeTemplate     = null;
    private Manuscript	      activeManuscript   = null;

    public enum ObjType {BOOK, CHARACTER, LOCATION, GEOLOCALE, MANUSCRIPT, TEMPLATE};
    
    static Logger logger = null;
    public enum LogLevel { TRACE, DEBUG, INFO, WARN, ERROR };
    
    public PropsManager()
    {
    	// With no user id argument, the only resource available to this constructor is the 
    	// user account list; this version of the PropsManager only creates 
    	// a Vector<UserAccount> users object for use by the login module
    	
        if(logger == null)
        {
	        System.setProperty("log4j.configurationFile", getTomcatHome() + pathMrkr + "conf" + pathMrkr + "electra_ink_log4j2.xml");
	        logger = LogManager.getLogger("electraInk.iBook");
        }
    	try
        {
    		String usersXml = getPathToUserDB();
	        UserAccountsParser userAccountsParser = new UserAccountsParser();
	        
	        // first verify that a user-db file exists  
	        File usersFile = new File(usersXml);
	        if(!usersFile.exists())
	        {
	        	// if not, create one---content will be "<accounts/>" with no
	        	// actual users
	        	Utilities.write_file(usersXml, "<accounts/>", true);
	        }
	        users = userAccountsParser.parse(usersXml);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    	
    }
 
    public PropsManager(String userId)
    {
    	
        if(logger == null)
        {
	        System.setProperty("log4j.configurationFile", getTomcatHome() + pathMrkr + "conf" + pathMrkr + "electra_ink_log4j2.xml");
	        logger = LogManager.getLogger("electraInk.iBook");
        }

        path_to_user_dir = path_to_resources + 
        		           "users" + 
        		           pathMrkr + 
        		           userId + 
        		           pathMrkr + 
        		           "resources" + 
        		           pathMrkr;

        agent           = new ManuscriptManager(path_to_user_dir);
        centralCasting  = new CharacterManager(path_to_user_dir);
        library         = new BookManager(path_to_user_dir);
        locationManager = new LocationManager(path_to_user_dir);
        try
        {
        	String usersXml = getPathToUserDB();
	        UserAccountsParser userAccountsParser = new UserAccountsParser();
	        users = userAccountsParser.parse(usersXml);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }
    

    public static void logMsg(LogLevel level, String msg)
    {
    	// sends ismple message to log (use logError() for stacktrace)
    	switch(level)
    	{
    	case TRACE:
    		logger.trace(msg);
    		break;
    	case DEBUG:
    		logger.debug(msg);
    		break;
    	case INFO:
    		logger.info(msg);
    		break;
    	case WARN:
    		logger.warn(msg);
    		break;
    	case ERROR:
    		logger.error(msg);
    		break;
    	}
    }
    
    public static void logError(String msg, Throwable e)
    {
    	// sends exception e to log
    	logger.error(msg, e);
    }
    
    public static String getErrorFilename()
    {
        String errorFilename = "iBook_login_credentials.txt";
    	
        String path_to_error_file =  
    			System.getProperty("os.name").indexOf("Windows") == -1 ? 
    					"/home/jford/log/" : "e:\\temp\\"; 
    	return path_to_error_file + errorFilename;
    }
    
    public String getCoverArtFilename(String manuscriptId)
    {
    	String msName = ((Manuscript)getProfile(ObjType.MANUSCRIPT, manuscriptId)).getMsName();
    	String coverArtFilename = "";
    	File msScriptDir = new File(path_to_user_dir + "manuscripts");
    	String[] files = msScriptDir.list();
    	int count = 0;
    	for(count = 0; count < files.length; count++)
    	{
    		if(files[count].indexOf(msName + "_Cover") != -1)
    		{
    			coverArtFilename = files[count];
    			break;
    		}
    	}
    	return coverArtFilename;
    }
    
    public Vector<String> getEbookList()
    {
    	File rsrcDir = new File(path_to_user_dir);
    	String[] fileList = rsrcDir.list();
    	Vector<String> epubList = new Vector<String>();
    	for(int count = 0; count < fileList.length; count++)
    	{
    		if(fileList[count].endsWith(".epub") || fileList[count].endsWith(".pdf"))
    			epubList.add(fileList[count]);
    	}
    	return epubList;
    }
    
    public static String getPathToUserDB()
	{
    	// user DB is stored in $TOMCAT_HOME/conf directory 
    	String tomcatHome = getTomcatHome();
		return tomcatHome + pathMrkr + "conf" + pathMrkr + "electraink_users.xml";
	}
    
    private static String findResources()
    {
    	String resourceDir = "";
    	
    	// when run from Eclipse, resources path starts with WebContent
    	//
    	// when run from .war file, current dir is $TOMCAT_HOME/bin 
    	String resourcePath = "";
    	String[] resourcePathArray = {"WebContent" + pathMrkr + "resources" + pathMrkr, 
    			                 "webapps" + pathMrkr + "iBook" + pathMrkr + "resources" + pathMrkr,
    			                 "iBook" + pathMrkr + "resources" + pathMrkr };
    	String errMsg = "";
    	String catalina_home = System.getenv("CATALINA_HOME");
    	String path = "-";
    	String tomcat_home = System.getenv("TOMCAT_HOME");
    	File currDir = null;
    	File resources = null;
    	
    	try
    	{
    		if(catalina_home != null)
    			currDir = new File(catalina_home);
    		else
    			currDir = new File(tomcat_home);
	    	path = currDir.getCanonicalPath();
	    	for(int count = 0; count < resourcePathArray.length; count++)
	    	{
	    		resourceDir = path + pathMrkr + resourcePathArray[count];
	    		resources = new File(resourceDir);
		    	if(resources.exists() && resources.isDirectory())
		    		break;
	    	}
    	}
    	catch(Exception e)
    	{
    		logMsg(LogLevel.INFO, "TOMCAT_HOME = \"" + tomcat_home + "\"");
    		logMsg(LogLevel.INFO, "CATALINA_HOME = \"" + catalina_home + "\"");
    		logMsg(LogLevel.INFO, "Cannot find webapp's resources directory. Set CATALINA_HOME or TOMCAT_HOME in tomcat startup script.");
    		logMsg(LogLevel.ERROR, e.getStackTrace().toString());
    		e.printStackTrace();
    		System.out.println("TOMCAT_HOME = \"" + tomcat_home + "\"");
    		System.out.println("CATALINA_HOME = \"" + catalina_home + "\"");
    		System.out.println("Cannot find webapp's resources directory. Set CATALINA_HOME or TOMCAT_HOME in tomcat startup script.");
    	}
		return resourceDir;
    }
    
    private static String getTomcatHome()
    {
    	String tomcatHome = "";
    	String catalinaHome = System.getenv("CATALINA_HOME");
    	File dir = null;
    	File resources = null;
    	
    	try
    	{
    		if(catalinaHome != null)
    			dir = new File(catalinaHome);
    		else
    			dir = new File(System.getenv("TOMCAT_HOME"));
	    	tomcatHome = dir.getCanonicalPath();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		logMsg(LogLevel.ERROR, e.getStackTrace().toString());
    	}
    	return tomcatHome;
    }
    
    public static String getPathToUserDir()
    {
    	return path_to_user_dir;
    }
    
    public static String getIBookSystemName()
    {
    	return iBookSystemNameFull;
    }
    
    public static String getIBookSystemName(String type)
    {
    	// type == "short" or "full"
    	String name = type.equals("short") ? iBookSystemNameShort : iBookSystemNameFull;
    	return name;
    }
    
    public void setActiveManuscript(Manuscript activeManuscript)
    {
    	this.activeManuscript = activeManuscript;
    }
    
    public void updateUserTemplateDir(boolean revertSystemTemplates)
    {
    	File systemTemplateDir = new File(path_to_resources + "templates");
    	File userTemplateDir = new File(path_to_user_dir + "templates");
    	String[] systemTemplates = systemTemplateDir.list();
    	String[] userTemplates = userTemplateDir.list();
    	String xfrFileContents = "";
    	boolean fileExists = false;
    	
    	for(int sysCount = 0; sysCount < systemTemplates.length; sysCount++)
    	{
    		for(int userCount = 0; userCount < userTemplates.length; userCount++)
    		{
    			if(systemTemplates[sysCount].compareTo(userTemplates[userCount]) == 0)
    			{
    				fileExists = true;
    				break; // break from userCount loop
    			}
    		}
    		if(fileExists && !revertSystemTemplates)
    			fileExists = false; // reset and check next file
    		else
    		{   
    			// copy the system template into the user directory
    			try
    			{
    				xfrFileContents = Utilities.read_file(path_to_resources + "templates" + pathMrkr + systemTemplates[sysCount]);
    				Utilities.write_file(path_to_user_dir + "templates" + pathMrkr + systemTemplates[sysCount], xfrFileContents, true);
    			}
    			catch(Exception e)
    			{
    				System.out.println("failed to read template file in propsMgr.updateUserTemplateDir().");
    				e.printStackTrace();
    				logMsg(LogLevel.WARN, "failed to read template file in propsMgr.updateUserTemplateDir().");
    				logMsg(LogLevel.WARN, e.getStackTrace().toString());
    			}
    		}
    	}
    }
    
    public void addUserAccount(UserAccount user)
    {
    	users.add(user);
    }
    
    public void updateUser(UserAccount user)
    {
    	Iterator<UserAccount> usersI = users.iterator();
    	UserAccount userAccount = null;
    	int idx = 0;
    	while(usersI.hasNext())
    	{
    		userAccount = usersI.next();
    		if(userAccount.getUserId().compareTo(user.getUserId()) == 0)
    		{
    			users.remove(idx);
    			users.add(user);
    			break;
    		}
    	}
    }
    
    public Vector<UserAccount> getUsers()
    {
    	return users;
    }
    
    public Manuscript getActiveManuscript()
    {
    	// getActiveManuscript() is only valid when the PropsManager object has
    	// been passed as an argument after setting the activeManuscript. If the PropsManager
    	// object isn't passed as an argument, use getProfile()
    	return activeManuscript;
    }
    
    public String getId(ObjType type, String objName)
    {
    	// this method should only be called by the ManageProfile servlet, when creating 
    	// a new object.
    	//
    	// To get a new id, a metadata object must be created by parsing the meta.xml file.
    	//
    	// Extracting the new id will automatically increment the "next" attribute of 
    	// the "id" element in the meta.xml, for the designated 
    	// object type. This is the value that will be used the next time an id of that tyupe is created.
    	//
    	// A new xml must be written after each new id is created.
    	
    	// First, parse the xml...
    	String metaFile = path_to_resources + "metadata" + pathMrkr + "meta.xml";
    	MetaDataParser parser = new MetaDataParser();
    	MetaData metaData = parser.parse(metaFile);
    	
    	// ...then get the new id...
    	String id = metaData.getId(type, objName);
    	
    	// ...write a new xml...
    	XmlFactory xmlFactory = new XmlFactory();
    	String xml = xmlFactory.doNewMetaXml(metaData);
    	Utilities.write_file(path_to_resources + "metadata" + pathMrkr + "meta.xml", xml, true);
    	
    	// and return the newly created ID
    	return id;
    }
    
    public MetaData getMetaData()
    {
    	return new MetaData();
    }
    
    public boolean setActiveTemplate(TemplateProfile profile)
    {
    	// templateId is filename in templates directory
    	boolean ret = true;
    	activeTemplate = profile;
    	return ret;
    }
    
    public TemplateProfile getActiveTemplate()
    {
    	return activeTemplate;
    }
    
    public boolean addTemplateCharacter(CharacterProfile character, String templateId)
    {
    	return agent.addTemplateCharacter(character, templateId);
    }
    
    public boolean addLocationProfile(LocationProfile profile)
    {
    	return locationManager.addLocationProfile(profile);
    }
    
    public boolean removeGeolocReference(String geolocId, String loc_id)
    {
    	return locationManager.removeGeolocReference(geolocId, loc_id);
    }
    
    public boolean addProfile(ObjType type, Object profile)
    {
    	boolean ret = false;
    	switch(type)
    	{
	    	case BOOK:
	    		ret = library.addProfile(profile);
	    		break;
	    	case CHARACTER:
	    		ret = centralCasting.addCharacterProfile((CharacterProfile)profile);
	    		break;
	    	case LOCATION:
	    	case GEOLOCALE:
	    		ret = locationManager.addLocationProfile((LocationProfile)profile);
	    		break;
	    	case MANUSCRIPT:
	    		break;
	    	case TEMPLATE:
	    		break;
    	}
    	return ret;
    }
    
    public Object getProfile(ObjType type, String id)
    {
    	Object profile = null;
    	switch(type)
    	{
	    	case BOOK:
	    		profile = library.getProfile(id);
	    		break;
	    	case CHARACTER:
	    		profile = centralCasting.getProfile(id);
	    		break;
	    	case LOCATION:
	    		profile = locationManager.getProfile(PropsManager.ObjType.LOCATION, id);
	    		break;
	    	case GEOLOCALE:
	    		profile = locationManager.getProfile(PropsManager.ObjType.GEOLOCALE, id);
	    		break;
	    	case MANUSCRIPT:
	    	case TEMPLATE:
	    		profile = agent.getProfile(type, id);
	    		break;
    	}
    	return profile;
    }
    
    public TemplateProfile getTemplateProfile(String templateId)
    {
    	return agent.getTemplateProfile(templateId);
    }
    
    public String getName(ObjType type, String id)
    {
    	String name = "";
    	switch(type)
    	{
    	case BOOK:
    		break;
    	case CHARACTER:
    		name = centralCasting.getName(id);
    		break;
    	case LOCATION:
    		name = locationManager.getName(id);
    		break;
    	case GEOLOCALE:
    		name = locationManager.getGeolocaleName(id);
    	case MANUSCRIPT:
    	case TEMPLATE:
    		break;
    	}
    	
    	return name;
    }
    
    public String getGeolocaleName(String id)
    {
    	return locationManager.getGeolocaleName(id);
    }
    
    public String getGeolocaleType(String id)
    {
    	return locationManager.getGeolocaleType(id);
    }
    
    public Vector<String> getAttributes(ObjType type, String id)
    {
    	Vector<String> atts = null;
    	
    	switch(type)
    	{
	    	case BOOK:
	    		break;
	    	case CHARACTER:
	    		atts = centralCasting.getCharacterAttributes(id);
	    		break;
	    	case LOCATION:
	    		atts = locationManager.getLocationDescriptions(id);
	    		break;
	    	case GEOLOCALE:
	    		atts = locationManager.getGeolocaleAttributes(id);
	    		break;
	    	case MANUSCRIPT:
	    	case TEMPLATE:
	    		break;
    	}
    	
    	return atts;
    }
    
    public boolean removeAttribute(ObjType type, String id, String index)
    {
    	boolean ret = true;
    	
    	// id = object's id (loc_id, geolocId, etc.
    	// index = string representation of the integer index into the Vector for the 
    	// item to be deleted ("3" for third item, or Vector element [2])
    	
    	switch(type)
    	{
	    	case BOOK:
	    		break;
	    	case CHARACTER:
	    		break;
	    	case LOCATION:
	    		ret = locationManager.removeLocAttribute(id, index);
	    		break;
	    	case GEOLOCALE:
	    		ret = locationManager.removeGeolocAttribute(id, index);
	    		break;
	    	case MANUSCRIPT:
	    	case TEMPLATE:
	    		break;
    	}
    	
    	return ret;
    }
    
    public Vector<String> getLocationDescriptions(String id)
    {
    	return locationManager.getLocationDescriptions(id);
    }
    
    public static String getPathToResources()
    {
        return path_to_resources;
    }
    
    public static String getPathMarker()
    {
    	return pathMrkr;
    }
    
    public GeolocaleProfile getGeolocale(String geolocaleId)
    {
    	return locationManager.getGeolocale(geolocaleId);
    }
    
    // add a new title to the book catalogue
    public boolean addBook(String title)
    {
        return library.addBook(title.trim());
    }
    
    // add a character to the character catalogue
    public boolean addCharacterProfile(CharacterProfile charProfile)
    {
        return centralCasting.addCharacterProfile(charProfile);
    }
    
    
    
    public void addGeolocaleProfile(GeolocaleProfile geolocale)
    {
    	locationManager.addGeolocaleProfile(geolocale);
    }
    
    public boolean addAttribute(PropsManager.ObjType objType, String id, String text)
    {
    	boolean ret = true;
    	
    	switch(objType)
    	{
	    	case LOCATION:
	    		locationManager.addGeolocaleAttribute(id, text);
	    		break;
	    	case GEOLOCALE:
	    		locationManager.addLocationAttribute(id, text);
	    		break;
	    	case CHARACTER:
	    		centralCasting.addCharacterAttribute(id, text);
	    		break;
	    	case BOOK:
	    	case MANUSCRIPT:
	    	case TEMPLATE:
    	}
    	return ret;
    }
    
    public boolean addGeolocaleAttribute(String geolocId, String text)
    {
    	return locationManager.addGeolocaleAttribute(geolocId, text);
    }
    
    public boolean addLocationAttribute(String loc_id, String text)
    {
    	return locationManager.addLocationAttribute(loc_id, text);
    }
    
    public boolean removeGeolocAttribute(String geolocId, String deleteAttrNum)
    {
    	return locationManager.removeGeolocAttribute(geolocId, deleteAttrNum);
    }
    
    public boolean removeLocAlias(String loc_id, String deleteAliasNum)
    {
    	return locationManager.removeLocAlias(loc_id, deleteAliasNum);
    }
    
    public boolean removeLocAttribute(String loc_id, String deleteAttrNum)
    {
    	return locationManager.removeLocAttribute(loc_id, deleteAttrNum);
    }

    public boolean removeCharAttribute(String charId, String deleteAttrNum)
    {
    	return centralCasting.removeCharAttribute(charId, deleteAttrNum);
    }
    
    public boolean removeCharAttribute(String templateId, String charId, String deleteAttrNum)
    {
    	return agent.removeCharAttribute(templateId, charId, deleteAttrNum);
    }

    public void addGeolocaleToLocation(String geolocId, String loc_id)
    {
    	locationManager.addGeolocaleToLocation(geolocId, loc_id);
    }
 
    public BookProfile getBookProfile(String id)
    {
    	return library.getProfile(id);
    }
    
    public String getBookSourceTitle(String bookId)
    {
    	return library.getBookSourceTitle(bookId);
    }
    
    public String getAuthorName(String bookId)
    {
    	return library.getAuthorName(bookId);
    }
    
    public String getSourceAuthorName(String bookId)
    {
    	return library.getSourceAuthorName(bookId); 
    }
    
    // get a list of keys currently contained in the character catalogue  
    public Vector<String> getCharacterIdList()
    {
        // each key can be used to obtain the first_lastName of a character profile
        list = centralCasting.getCharacterIdList();
        return list;
    }
    
    public String getCharacterName(String characterId)
    {
    	return centralCasting.getName(characterId);
    }
    public String getLocationName(String locationId)
    {
    	return locationManager.getName(locationId);
    }
    
    public int getBookCount()
    {
        return library.getBookCount();
    }
    public String getBookTitle(String bookId)
    {
    	return library.getBookTitle(bookId);
    }
    
    public int getCharacterCount()
    {
        return centralCasting.getCharacterCount();
    }
    
    public int getCharacterCount(String templateId)
    {
    	return agent.getCharacterCount(templateId);
    }
    
    public int getLocationsCount()
    {
        return locationManager.getLocationsCount();
    }
    
    public int getGeolocsCount()
    {
        return locationManager.getGeolocsCount();
    }
    
    // get a list of location IDs, sorted by location name  
    public Vector<String> getLocationIdList()
    {
         list = locationManager.getLocationIdList();
        return list;
    }
    
    public Vector<String> getIdList(PropsManager.ObjType objType)
    {
    	Vector<String> idList = null;
    	switch(objType)
    	{
	    	case BOOK:
	    		idList = library.getBookIdList();
	    		break;
	    	case CHARACTER:
	    		idList = centralCasting.getCharacterIdList();
	    		break;
	    	case GEOLOCALE:
	    		idList = locationManager.getGeolocaleIdList();
	    		break;
	    	case LOCATION:
	    		idList = locationManager.getLocationIdList();
	    		break;
	    	case MANUSCRIPT:
	    	case TEMPLATE:
	    		break;
    	}
    	return idList;
    }
    
    public Vector<String> getTemplateCharacterList(String templateId)
    {
    	return agent.getTemplateCharacterList(templateId);
    }

    public Vector<String> getGeolocaleIdList()
    {
        list = locationManager.getGeolocaleIdList();
        return list;
    }

    public CharacterProfile getCharacterProfile(String id)
    {
    	CharacterProfile profile = null;
    	profile = centralCasting.getProfile(id);
    	return profile;
    }
    
    public void removeBook(String profile)
    {
        library.removeBook(profile);
    }

    public boolean deleteProfile(PropsManager.ObjType type, String id)
    {
    	boolean ret = true;
    	switch(type)
    	{
	    	case BOOK:
	    		library.deleteProfile(id);
	    		break;
	    	case LOCATION:
	    	case GEOLOCALE:
	    		locationManager.deleteProfile(type, id);
	    		break;
	    	case CHARACTER:
	            centralCasting.deleteProfile(id);
	    		break;
	    	case MANUSCRIPT:
	    		break;
	    	case TEMPLATE:
	    		break;
    	}
    	return ret;
    }

    public Vector<String> getTemplateIdList()
    {
    	return agent.getTemplateIdList();
    }
    public Vector<TemplateProfile> getTemplateList()
    {
    	return agent.getTemplateList();
    }
    
    public int getTemplateCount()
    {
    	return agent.getTemplateCount();
    }
    
    public String getTemplateFilename(String templateId)
    {
    	return agent.getTemplateFilename(templateId);
    }
    
    public String getTemplateTitle(String templateId)
    {
    	return agent.getTemplateTitle(templateId);
    }
    
    public CharacterProfile getTemplateCharacter(String templateId, String charId)
    {
    	return agent.getTemplateCharacter(templateId, charId);
    }
    
    public LocationProfile getTemplateLocation(String templateId, String charId)
    {
    	return agent.getTemplateLocation(templateId, charId);
    }
    
    public boolean updateAliases(ObjType type, String objId, Vector<String> aliases)
    {
    	boolean ret = false;
    	switch(type)
    	{
	    	case CHARACTER:
	    		centralCasting.updateAliases(objId, aliases);
	    		break;
	    	case LOCATION:
	    		locationManager.updateAliases(objId, aliases);
	    		break;
	    	case GEOLOCALE:
	    	case MANUSCRIPT:
	    	case BOOK:
    		default:
    			ret = false;
    	}
    	return ret;
    }
    
    public boolean deleteAlias(ObjType type, String objId, int index)
    {
    	boolean ret = false;
    	switch(type)
    	{
	    	case CHARACTER:
	    		centralCasting.deleteAlias(objId, index);
	    		break;
	    	case GEOLOCALE:
	    	case LOCATION:
	    		locationManager.deleteAlias(type, objId, index);
	    		break;
	    	case MANUSCRIPT:
	    	case BOOK:
    		default:
    			ret = false;
    	}
    	return ret;
    }
}
