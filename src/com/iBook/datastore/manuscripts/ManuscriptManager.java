package com.iBook.datastore.manuscripts;

import java.io.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.stream.Collectors;

import com.iBook.utilities.*;
import com.iBook.datastore.*;
import com.iBook.datastore.books.*;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.locations.*;

public class ManuscriptManager
{
    private String path_to_resources = "";
    private String pathMrkr = "";
    private String manuscript_list_filename = "manuscript_list.properties";
    private Properties manuscript_list_props = null;
    private Vector<String> manuscript_list = null;
    
    private Vector<Manuscript> manuscripts = new Vector<Manuscript>();
    private Vector<CharacterProfile> templateCharacters = new Vector<CharacterProfile>();
    private Vector<LocationProfile> templateLocations = new Vector<LocationProfile>();
    private Vector<GeolocaleProfile> templateGeolocales = new Vector<GeolocaleProfile>();
    
    // ManuscriptManagere manages both manuscripts (books produced as iBook projects) and templates
    // (source texts)
    public ManuscriptManager()
    {
    	// constructor for templates---don't need path to resources
    }

    public ManuscriptManager(String path_to_resources)
    {
    	// constructor for manuscripts, which need a path to resource files;
    	// PropsManager constructor sends in path to userId/resources
        this.path_to_resources = path_to_resources;
        pathMrkr = PropsManager.getPathMarker();
    }

    public void addManuscript(Manuscript ms)
    {
    	manuscripts.add(ms);
    }
    
    public Manuscript getManuscript(String manuscriptId)
    {
    	Manuscript ms = null;
    	
    	Iterator<Manuscript> msI = manuscripts.iterator();
    	while(msI.hasNext())
    	{
    		ms = (Manuscript)msI.next();
    		if(ms.getId().compareTo(manuscriptId) == 0)
    			break;
    	}
    	return ms;
    }
    // returns a list of keys in the manuscript catalog
    public Vector<String> getList()
    {
        return manuscript_list;
    }
    
    public boolean addTemplateCharacter(CharacterProfile character, String templateId)
    {
    	boolean ret = true;
    	Vector<TemplateProfile> templates = getTemplateList();
    	Iterator<TemplateProfile> templatesI = templates.iterator();
    	TemplateProfile profile = null;
    	while(templatesI.hasNext())
    	{
    		profile = (TemplateProfile)templatesI.next();
    		if(profile.getId().compareTo(templateId) == 0)
    			profile.addCharacter(character);
    	}
    	return ret;
    }
    
    public Vector<CharacterProfile> getTemplateCharacters()
    {
    	return templateCharacters;
    }
    
    public CharacterProfile getTemplateCharacter(String templateId, String charId)
    {
    	CharacterProfile charProfile = null;
    	TemplateProfile templateProfile = null;
    	Vector<TemplateProfile> templates = getTemplateList();
    	Iterator<TemplateProfile> templatesI = templates.iterator();
    	while(templatesI.hasNext())
    	{
    		templateProfile = (TemplateProfile)templatesI.next();
    		if(templateProfile.getId().compareTo(templateId) == 0)
    			break;
    	}
    	Vector<CharacterProfile> chars = templateProfile.getCharacters();
    	Iterator<CharacterProfile> charsI = chars.iterator();
    	while(charsI.hasNext())
    	{
    		charProfile = (CharacterProfile)charsI.next();
    		if(charProfile.getId().compareTo(charId) == 0)
    			break;
    	}
    	return charProfile;
    }
    
    public LocationProfile getTemplateLocation(String templateId, String loc_id)
    {
    	LocationProfile locProfile = null;
    	TemplateProfile templateProfile = null;
    	Vector<TemplateProfile> templates = getTemplateList();
    	Iterator<TemplateProfile> templatesI = templates.iterator();
    	while(templatesI.hasNext())
    	{
    		templateProfile = (TemplateProfile)templatesI.next();
    		if(templateProfile.getId().compareTo(templateId) == 0)
    			break;
    	}
    	Vector<LocationProfile> locs = templateProfile.getLocations();
    	Iterator<LocationProfile> locsI = locs.iterator();
    	while(locsI.hasNext())
    	{
    		locProfile = (LocationProfile)locsI.next();
    		if(locProfile.getId().compareTo(loc_id) == 0)
    			break;
    	}
    	return locProfile;
    }
    
    public Vector<String> getTemplateCharacterList(String templateId)
    {
    	TemplateProfile templateProfile = getTemplateProfile(templateId);
    	Vector<String> list = new Vector<String>();
    	Iterator<CharacterProfile> charsI = templateCharacters.iterator();
    	while(charsI.hasNext())
    	{
    		list.addElement((String)charsI.next().getId());
    	}
    	return list;
    }
    
    public int getCharacterCount(String templateId)
    {
    	TemplateProfile profile = getTemplateProfile(templateId);
    	return profile.getCharacterCount();
    }
    
    public boolean addTemplateLocation(LocationProfile location)
    {
    	boolean ret = true;
    	templateLocations.addElement(location);
    	return ret;
    }
    
    public Vector<LocationProfile> getTemplateLocations()
    {
    	return templateLocations;
    }
    
    public Vector<String> getTemplateLocationList()
    {
    	Vector<String> list = new Vector<String>();
    	Iterator<LocationProfile> charsI = templateLocations.iterator();
    	while(charsI.hasNext())
    	{
    		list.addElement((String)charsI.next().getId());
    	}
    	return list;
    }
    
    public boolean addTemplateGeolocale(GeolocaleProfile geolocale)
    {
    	boolean ret = true;
    	templateGeolocales.addElement(geolocale);
    	return ret;
    }
    
    public Vector<GeolocaleProfile> getTemplateGeolocales()
    {
    	return templateGeolocales;
    }
    
    public Vector<String> getTemplateGeolocaleList()
    {
    	Vector<String> list = new Vector<String>();
    	Iterator<GeolocaleProfile> charsI = templateGeolocales.iterator();
    	while(charsI.hasNext())
    	{
    		list.addElement((String)charsI.next().getId());
    	}
    	return list;
    }
    
    public Object getProfile(PropsManager.ObjType type, String id)
    {
    	Object profile = null;
    	switch(type)
    	{
    	case MANUSCRIPT:
    		ManuscriptParser mParser = new ManuscriptParser();
    		profile = mParser.parse(path_to_resources + "manuscripts" + pathMrkr + getFilename(PropsManager.ObjType.MANUSCRIPT, id));
    		break;
    	case TEMPLATE:
            TemplateParser tParser = new TemplateParser();
            profile = tParser.parse(path_to_resources + "templates" + pathMrkr + getTemplateFilename(id));
    		break;
    	}
    	return profile;
    }
    
    public TemplateProfile getTemplateProfile(String templateId)
    {
    	TemplateProfile profile = new TemplateProfile();
    	Vector<TemplateProfile> profileList = getTemplateList();
    	Iterator<TemplateProfile> listI = profileList.iterator();
    	while(listI.hasNext())
    	{
    		profile = (TemplateProfile)listI.next();
    		if(profile.getId().compareTo(templateId) == 0)
    			break;
    	}
    	return profile;
    }
    
    public String getTemplateTitle(String templateId)
    {
    	String templateTitle = "";
    	Vector<TemplateProfile> templateList = getTemplateList();
    	Iterator<TemplateProfile> templatesI = templateList.iterator();
    	
    	TemplateProfile profile = null;
    	
    	while(templatesI.hasNext())
    	{
    		profile = (TemplateProfile)templatesI.next();
    		if(profile.getId().compareTo(templateId) == 0)
    		{
    			templateTitle = profile.getTitle();
    			break;
    		}
    	}
    	return templateTitle;
    }
    public Vector<String> getTemplateIdList()
    {
    	Vector<String> templateIdList = new Vector<String>();
    	File dir = new File(path_to_resources + "templates");
        String[] dirList = dir.list();
        String path = path_to_resources + "templates" + pathMrkr;
        String id = "";
        String idMrkr = "id=\"";
        
        String templateText = "";
        int idx = 0;
        int end = 0;
        boolean fileOkay = true;
        
        for(int count = 0; count < dirList.length; count++)
        {
        	try
        	{
        		templateText = Utilities.read_file(path + dirList[count]);
        	}
        	catch(Exception e)
        	{
        		System.out.println("Error reading template in ManuscriptManager.getTemplateIdList()");
        		e.printStackTrace();
        		fileOkay = false;
        	}
        	if(fileOkay)
        	{
        		idx = templateText.indexOf(idMrkr);
        		end = templateText.indexOf("\"", idx + idMrkr.length());
        		id = templateText.substring(idx + idMrkr.length(), end);
        		templateIdList.addElement(id);
        	}
        }

        return templateIdList;
    }
    
    public String getTemplateFilename(String id)
    {
    	String filename = "";
    	File dir = new File(path_to_resources + "templates");
        String[] dirList = dir.list();
        String path = path_to_resources + "templates" + pathMrkr;
        String idMrkr = "id=\"";
        
        String templateText = "";
        int idx = 0;
        int end = 0;
        boolean fileOkay = true;
        
        for(int count = 0; count < dirList.length; count++)
        {
        	filename = dirList[count];
        	// read in the template text
        	try
        	{
        		templateText = Utilities.read_file(path + filename);
        	}
        	catch(Exception e)
        	{
        		System.out.println("Error reading template in ManuscriptManager.getTemplateIdList()");
        		e.printStackTrace();
        		fileOkay = false;
        	}
        	if(fileOkay)
        	{
        		// get the value of the id attribute
        		idx = templateText.indexOf(idMrkr)  + idMrkr.length();
        		end = templateText.indexOf("\"", idx + 1);
        		
        		// if it's a match, break out of the for loop
        		if(templateText.substring(idx, end).compareTo(id) == 0)
        			break;
        	}
        }
        return filename;
    }
    
    public String getFilename(PropsManager.ObjType type, String id)
    {
    	String pathSegment = "";
    	switch(type)
    	{
    	case TEMPLATE:
    		pathSegment = pathMrkr + "templates" + pathMrkr;
    		break;
    	case MANUSCRIPT:
    		pathSegment = pathMrkr + "manuscripts" + pathMrkr;
    		break;
    	}
    	String filename = "";
    	File dir = new File(path_to_resources + pathSegment);
        String[] dirList = dir.list();
        String path = path_to_resources + pathSegment;
        String idMrkr = "id=\"";
        
        String templateText = "";
        int idx = 0;
        int end = 0;
        boolean fileOkay = true;
        
        for(int count = 0; count < dirList.length; count++)
        {
        	filename = dirList[count];
        	// read in the template text
        	try
        	{
        		templateText = Utilities.read_file(path + filename);
        	}
        	catch(Exception e)
        	{
        		System.out.println("Error reading template in ManuscriptManager.getFilename()");
        		e.printStackTrace();
        		fileOkay = false;
        	}
        	if(fileOkay)
        	{
        		// get the value of the id attribute
        		idx = templateText.indexOf(idMrkr)  + idMrkr.length();
        		end = templateText.indexOf("\"", idx + 1);
        		
        		// if it's a match, break out of the for loop
        		if(templateText.substring(idx, end).compareTo(id) == 0)
        			break;
        	}
        }
        return filename;
    }
    
    public Vector<TemplateProfile> getTemplateList()
    {
    	Vector<TemplateProfile> templateList = new Vector<TemplateProfile>();
    	TemplateParser parser = new TemplateParser();
    	
    	File dir = new File(path_to_resources + "templates");

    	String xml = "";
        String[] dirList = dir.list();
        String path = path_to_resources + "templates" + pathMrkr;
        
        for(int count = 0; count < dirList.length; count++)
        {
            templateList.add(parser.parse(path + dirList[count]));
        }
    	return templateList;
    }
    
    public int getTemplateCount()
    {
    	Vector<TemplateProfile> tList = getTemplateList();
    	return tList.size();
    }
    
    // returns an html-formatted list of manuscript titles
    public String getListTitles()
    {
        reLoadProps();
        
        String html_list = "<ul>\n";
        String item = "";
        String title = "";
        
        Vector<String> list = getList();
        
/*        while(list.hasMoreElements())
        {
            item = (String)list.nextElement();
            if(item.indexOf("title") != -1)
            {
                title = manuscript_list_props.getProperty(item);
                html_list += "    <li>"+ title + "</li>\n";
            }
        }
        html_list += "</ul>\n";
        
*/
        return html_list;
	}
    
    // returns an html menu of titles 
    public String getMenuTitles(String drop_down_selector)
    {
        reLoadProps();
        
        String item = "";
        String title = "";
        String option_tag = "<option name=\"";
        String option_end_tag = "</option>\n";
        String html_menu = option_tag + "\">" + drop_down_selector + option_end_tag;
        
       Vector<String> msList = getList();
        
/*        while(msList.hasMoreElements())
        {
            item = (String)msList.nextElement();
            if(item.indexOf("title") != -1)
            {
                title = manuscript_list_props.getProperty(item);
                html_menu += option_tag + title + "\">"+ title + "</option>\n";
            }
        }
*/        return html_menu;
    }
    
    public String getMSTitle(String item)
    {
        String title = "";
        title = manuscript_list_props.getProperty(item);
        return title;
    }
    
    public boolean hasManuscriptByFilename(String filename)
    {
        String filename_with_path = path_to_resources + filename;
        boolean exists = false;
        File ms = new File(filename_with_path);
        if(ms.exists())
        {
            exists = true;
        }
        return exists;
    }
    
    // does the catalogue already contain a manuscript whose title matches the new title?
    public boolean hasManuscriptByTitle(String title)
    {
        boolean exists = false;
        Properties msProps = new Properties();
        String item = "";
        String existingTitle = "";
        try
        {
            msProps.load(new FileInputStream(path_to_resources + manuscript_list_filename));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Enumeration<?> msList = msProps.propertyNames();
        while(msList.hasMoreElements())
        {
            item = (String)msList.nextElement();
            if(item.indexOf("title") != -1)
            {
                existingTitle = msProps.getProperty(item);
                if(existingTitle.compareTo(title) == 0)
                {
                    exists = true;
                }
            }
        }
        return exists;
    }
    
    // add a new manuscript title to the manuscript catalogue; user does not do this,
    // it's called from addManuscript()
    private void addManuscriptToList(String title, String filename)
    {
        // Read the list of titles in the current catalogue
        try
        {
            manuscript_list_props.load(new FileInputStream(path_to_resources + manuscript_list_filename));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        // build a key in the form "title_N" based on the number of 
        // titles in the list... 
        String manuscript_num = "";
        String new_manuscript_key = "";
        manuscript_num = Integer.toString(getManuscriptCount());
        new_manuscript_key = "title_" + manuscript_num;
        // ...and store the manuscript in the catalog by title 
        manuscript_list_props.setProperty(new_manuscript_key, title);
        // ...and build a key in the form of "filename_N"...
        new_manuscript_key = "filename_" + manuscript_num;
        // ...and store the manuscript in the catalog again, by filename...
        manuscript_list_props.setProperty(new_manuscript_key, filename);
      
        try
        {
            OutputStream propOut = new FileOutputStream(new File(path_to_resources + manuscript_list_filename));
            manuscript_list_props.store(propOut, "Character list");
            propOut.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public int getManuscriptCount()
    {
        reLoadProps();
        
        String item = "";
        
        Enumeration<?> manuscripts = manuscript_list_props.propertyNames();
        int num = 0;
        while(manuscripts.hasMoreElements())
        {
            item = (String)manuscripts.nextElement();
            if(item.indexOf("title") != -1)
                num++;
        }
        return num;
    }
    
    // get encoded_title value specified by key from the manuscript catalogue
    public String getEncodedTitle(String key)
    {
        // manuscript catalog entries are in the form:
        //
        //     manuscript_N=encoded_title
        //
        // the method will return the encoded_title string associated 
        // with the given key
        try
        {
            manuscript_list_props.load(new FileInputStream(path_to_resources + manuscript_list_filename));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return manuscript_list_props.getProperty(key); 
    }
    
    public String getListFilename()
    {
        return manuscript_list_filename;
    }
    
    // get manuscript title without underscores for the given key
    public String getUnencodedTitle(String key)
    {
        return Utilities.decodeString(getEncodedTitle(key));
    }
    
    private void reLoadProps()
    {
        // Read the list of titles in the current catalogue
        try
        {
            manuscript_list_props.load(new FileInputStream(path_to_resources + manuscript_list_filename));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void removeManuscript(String title)
    {
        reLoadProps();
        String item = "";
        String existingTitle = "";
        String filename_key = "";
        String filename = "";
        String num = "";
        int idx = 0;
        
        Enumeration<?> list = manuscript_list_props.propertyNames();
        while(list.hasMoreElements())
        {
            item = (String)list.nextElement();
            if(item.indexOf("title") == 0)
            {
                existingTitle = manuscript_list_props.getProperty(item);
                if(existingTitle.compareTo(title) == 0)
                {
                    idx = item.indexOf("_");
                    num = item.substring(idx);
                    filename_key = "filename" + num;
                    filename = manuscript_list_props.getProperty(filename_key);
                    manuscript_list_props.remove(item);
                    manuscript_list_props.remove(filename_key);
                }
            }
        }
        File propsFile = new File(path_to_resources + manuscript_list_filename);
        try
        {
            OutputStream propOut = new FileOutputStream(propsFile);
            manuscript_list_props.store(propOut, "Manuscript list");
            propOut.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        File ms = new File(path_to_resources + filename);
        ms.delete();
        
        File dir = new File(path_to_resources + filename.replace(".xml", ""));
        File[] dirList = dir.listFiles();
        int numFiles = dirList.length;
        for(int c = 0; c < numFiles; c++)
        {
            File dirFile = dirList[c];
            boolean success = dirFile.delete();
        }
        dir.delete();
    }
    
    public boolean removeCharAttribute (String templateId, String charId, String deleteAttrNum)
    {
    	boolean ret = false;
    	
    	TemplateProfile templateProfile = getTemplateProfile(templateId);
    	Vector<CharacterProfile> tChars = templateProfile.getCharacters();
    	CharacterProfile tCharProfile = null;
    	Iterator<CharacterProfile> tCharsI = tChars.iterator();
    	while(tCharsI.hasNext())
    	{
    		tCharProfile = (CharacterProfile)tCharsI.next();
    		if(tCharProfile.getId().compareTo(charId) == 0)
    		{
 				tCharProfile.getAttributes().removeElementAt(Integer.parseInt(deleteAttrNum));
 				ret = true;
				break;
    		}
    	}
    	return ret;
    }
    // add a key/value pair to a profile
    public void addProperty(String profile, String key, String value)
    {
        Properties props = getProps(profile);
        if(!value.isEmpty() || value.compareTo(" ") != 0)
            props.setProperty(key, value);
        else
            props.remove(key);
        File propsFile = new File(path_to_resources + profile + ".properties");
        if(propsFile.exists())
        {
            try
            {
                OutputStream propOut = new FileOutputStream(propsFile);
                props.store(propOut, profile + " profile");
                propOut.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    // add multiple key/value pairs to a profile
    public void addProperties(String profile, String[][] data)
    {
        int num = data.length;
        Properties props = getProps(profile);
        String key = "";
        String value = "";
        for(int count = 0; count < num; count++)
        {
            key = data[count][0];
            value = data[count][1];
            if(!value.isEmpty() || value.compareTo(" ") != 0)
                props.setProperty(key, value);
            else
                props.remove(key);
        }
        File propsFile = new File(path_to_resources + profile + ".properties");
        try
        {
            OutputStream propOut = new FileOutputStream(propsFile);
            props.store(propOut, profile + " profile");
            propOut.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    // delete a property from a profile
    public void removeProperty(String profile, String key)
    {
        Properties props = getProps(profile);
        props.remove(key);
        File propsFile = new File(path_to_resources + profile + ".properties");
        try
        {
            OutputStream propOut = new FileOutputStream(propsFile);
            props.store(propOut, profile + " profile");
            propOut.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    // delete multiple properties from a profile
    public void removeProperties(String profile, String[] keys)
    {
        int num = keys.length;
        Properties props = getProps(profile);
        for(int count = 0; count < num; count++)
        {
            props.remove(keys[count]);
        }
        File propsFile = new File(path_to_resources + profile + ".properties");
        try
        {
            OutputStream propOut = new FileOutputStream(propsFile);
            props.store(propOut, profile + " profile");
            propOut.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    // get a Properties object
    public Properties getProps(String profile)
    {
        Properties props = null;
        File propFile = new File(path_to_resources + Utilities.encodeString(profile) + ".properties");
        if(propFile.exists())
        {
            props = new Properties();
            try
            {
                props.load(new FileInputStream(propFile));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return props;
    }

    public String getType(String msFilename, String key)
    {
        String type = "";
        Properties props = getProps("\\" + msFilename + "\\" + key);
        if(props != null)
            type = props.getProperty("type");
        return type;
    }
    
    public void addManuscript(String filename)
    {
        // called by ImportManuscript servlet after uploading a file
        String title = "";
        String manuscript = null;
        try
        {
            manuscript = Utilities.read_file(path_to_resources 
                                           + "\\" 
                                           + filename);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        String marker = "<manuscript title=\"";
        int idx = manuscript.indexOf(marker) + marker.length();
        int end = manuscript.indexOf("\"", idx);
        title = manuscript.substring(idx, end);

        // check to see if a manuscript of the same title 
        // already exists in the catalog
        boolean titleExists = hasManuscriptByTitle(title);
        // if no, then add it...
        if(!titleExists)
        {
            addManuscriptToList(title, filename);
        }
    }
}
