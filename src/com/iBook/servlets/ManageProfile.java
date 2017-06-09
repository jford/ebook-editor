package com.iBook.servlets;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;
import java.util.*;

import com.iBook.datastore.*;
import com.iBook.datastore.PropsManager;
import com.iBook.datastore.PropsManager.ObjType;
import com.iBook.datastore.XmlFactory;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.books.*;
import com.iBook.datastore.locations.*;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.Utilities;

public class ManageProfile extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private String path_to_resources = PropsManager.getPathToResources();
    private String path_to_user_dir = null;
    private String pathMrkr = PropsManager.getPathMarker();
    
    private boolean addAlias;
    private boolean addAttribute;
    private boolean addExistingLocale;
    private boolean addLocAttr; 
    private boolean aliasIndexHasValue;
    private boolean attributeIndexHasValue;
	private boolean createCharacter;
    private boolean clearCharSub;
    private boolean clearGeolocSub;
    private boolean createLocale;
    private boolean clearLocSub;
    private boolean createBook;
    private boolean createLocation;
    private boolean deleteAlias;
//    private boolean deleteAttr;
	private boolean deleteAttribute;
    private boolean deleteBook;
	private boolean deleteCharacter;
    private boolean deleteGeoloc;
    private boolean deleteLocation;
	private boolean editAliases;
	private boolean editAttribute;
	private boolean editBook;
	private boolean editCharacter;
	private boolean editNames;
	private boolean editAlias;
	private boolean editGeoloc;
	private boolean editGeolocName;
	private boolean editGeolocType;
	private boolean editLocation;
	private boolean editLocationType;
	private boolean editLocName;
	private boolean editTitle;
	private boolean isTemplateGeoloc;
	private boolean isTemplateLoc;
	private boolean mScriptChanged;
	private boolean removeGeolocFromLocation;
	private boolean resetAuthorName;
	private boolean setCharContext;
	private boolean setCharSub;
	private boolean setGeolocSub;
	private boolean setLocSub;
	private boolean updateAge;
	private boolean updateGender;

	private String age;
	private String akaName;
	private String alias;
	private String aliasIndexValue;
	private String attributeText;
	private String attributeIndexValue;
	private String bookId;
	private String bookTitle;
	private String charContext;
	private String charId;
	private String deleteAliasNum;
	private String deleteAttrNum;
	private String description;
	private String gender;
	private String geolocId;
	private String key;
	private String iBookCharId;
	private String iBookGeolocId;
	private String iBookLocId;
	private String loc_id;
	private String locationName;
	private String locationType;
	private String locType;
	private String name;
	private String namePrefix;
	private String nameFirst;
	private String nameMiddle;
	private String nameLast;
	private String nameShort;
	private String nameSuffix;
	private String newGeolocName;
	private String newGeolocType;
	private String projectName;
	private String redirect_target = "index.jsp";
	private String region_id;
	private String tCharId;
	private String templateId;
	private String text;
	private String tGeolocId;
	private String tLocId;
	private String type;
	private String userId;
	private String value;

    private PropsManager propsMgr = null;
    
    private Vector<String> aliases = null;
    private Vector<String> attributes = null;
    
    public ManageProfile()
    {
        super();
    }
    
    public String getPathInfo(HttpServletRequest request)
    {
    	return request.getPathInfo();
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        doPost(request, response);
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        ServletOutputStream op = response.getOutputStream();
        
        getParams(request, response);
        
        propsMgr =  new PropsManager(userId);
        path_to_user_dir = propsMgr.getPathToUserDir();
        
        if(type.toLowerCase().compareTo("book") == 0)
        	redirect_target = doBook();
        else if(type.toLowerCase().compareTo("character") == 0)
        	redirect_target = doCharacter();
        else if(type.toLowerCase().compareTo("location") == 0)
        	redirect_target = doLocation();
        else if(type.toLowerCase().compareTo("geolocale") == 0)
        	redirect_target = doGeolocale();

        String redirect = "<html>\n"
            + "    <body onload=\"document.location = \'" + redirect_target + "\'\" />\n"
            + "</html>";
        response.setContentLength(redirect.length());
        op.write(redirect.getBytes(), 0, redirect.length());
    }

    private String doBook()
    { 
		Manuscript mScript = null;

		if(clearCharSub)
    	{
    		redirect_target = "/iBook/editor/doCharacterAssignments.jsp?bookId=" + bookId;
    		mScriptChanged = editCharSub();
    	}
    	
    	if(clearGeolocSub)
    	{
    		redirect_target = "/iBook/editor/doGeolocaleAssignments.jsp?bookId=" + bookId;
    		mScriptChanged = editGeolocSub();
    	}
    	
    	if(clearLocSub)
    	{
    		redirect_target = "/iBook/editor/doLocationAssignments.jsp?bookId=" + bookId;
    		mScriptChanged = editLocSub();
    	}
    	
    	// create book
    	if(createBook)
    	{
    		BookProfile profile = createBook();
    		propsMgr.addProfile(PropsManager.ObjType.BOOK, profile);
    		redirect_target = "/iBook/editor/editBook.jsp?bookId=" + profile.getId();
    		mScriptChanged = true;
    	}
    	else if(deleteBook)
    	{
    		redirect_target = "/iBook/editor/editBooks.jsp";
    		String mScriptId = ((BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId)).getManuscriptId();
    		String mScriptFilename = ((Manuscript)propsMgr.getProfile(ObjType.MANUSCRIPT, mScriptId)).getMsName();
    		File mScriptFile = new File(path_to_user_dir + "manuscripts" + pathMrkr + mScriptFilename);
    		if(mScriptFile.exists())
    			mScriptFile.delete();
    		propsMgr.deleteProfile(PropsManager.ObjType.BOOK, bookId);
    	}
    	
    	// edit book
    	if(editBook)
    	{
    		redirect_target = "/iBook/editor/editBook.jsp?bookId=" + bookId;
    	}
    	
    	// edit title
    	if(editTitle)
    	{
    		BookProfile profile = propsMgr.getBookProfile(bookId);
    		profile.setTitle(bookTitle);
    		String mScriptId = profile.getManuscriptId();
    		if(mScriptId.length() > 0)
    		{
	    		mScript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, mScriptId);
	    		mScript.setIBookTitle(bookTitle);
	    		mScriptChanged = true;
    			propsMgr.setActiveManuscript(mScript);
    		}
    		redirect_target = "/iBook/editor/editBook.jsp?bookId=" + bookId;
    	}
    	
    	// reset author name
    	if(resetAuthorName)
    	{
    		BookProfile profile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
    		profile.setNamePrefix(namePrefix);
    		profile.setNameFirst(nameFirst);
    		profile.setNameLast(nameLast);
    		profile.setNameMiddle(nameMiddle);
    		profile.setNameSuffix(nameSuffix);
    		String iBookAuthor = profile.getAuthorName();

    		String mScriptId = profile.getManuscriptId();
    		if(mScriptId.length() > 0)
    		{
    			mScript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, mScriptId);
    			mScript.setIBookAuthor(iBookAuthor);
    			mScriptChanged = true;
    			propsMgr.setActiveManuscript(mScript);
    		}
    		redirect_target = "/iBook/editor/editBook.jsp?bookId=" + bookId;
    	}
    	
    	if(setCharSub)
    	{
    		redirect_target = "/iBook/editor/doCharacterAssignments.jsp?bookId=" + bookId;
    		editCharSub();
    	}
    	
    	if(setGeolocSub)
    	{
    		redirect_target = "/iBook/editor/doGeolocaleAssignments.jsp?bookId=" + bookId;
    		editGeolocSub();
    	}
    	
    	if(setLocSub)
    	{
    		redirect_target = "/iBook/editor/doLocationAssignments.jsp?bookId=" + bookId;
    		editLocSub();
    	}
    	
    	propsMgr.logMsg(PropsManager.LogLevel.DEBUG, "ManageProfile.doBook() handing off to updateXml()"); 
    	updateXml(PropsManager.ObjType.BOOK);
    	if(mScriptChanged)
        	updateXml(PropsManager.ObjType.MANUSCRIPT);

    	return redirect_target;
    }
    
    private boolean editCharSub()
    {
    	BookProfile bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
    	String templateId = bookProfile.getTemplateId();
    	TemplateProfile tProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
    	Vector<CharacterProfile> tCharList = tProfile.getCharacters();

    	if(clearCharSub)
    	{
    		iBookCharId = "";
    		// when a char sub is cleared and no longer used, need to remove the tChar 
    		// references from the descriptors list in the manuscript
    		String mScriptId = bookProfile.getManuscriptId();
    		Manuscript mScript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, mScriptId);
    		Vector<String[]> descriptors = mScript.getDescriptors();
    		Iterator<String[]> descriptorsI = descriptors.iterator();
    		String[] descriptor = null;
    		int descriptorIdx = 0;
    		Integer dontDelete = new Integer(-1);
    		Integer[] markedForDelete = new Integer[descriptors.size()];
    		while(descriptorsI.hasNext())
    		{
    			descriptor = descriptorsI.next();
    			if(tCharId.equals(descriptor[0]))
    			{
    				markedForDelete[descriptorIdx] = new Integer(descriptorIdx);
    			}
    			else
    				markedForDelete[descriptorIdx] = dontDelete; 
    			descriptorIdx++;
    		}
    		
    		// delete items marked, starting at the end and working backwards so that
    		// the vector never shrinks smaller than the descriptorIdx
    		for(int count = markedForDelete.length - 1; count >= 0; count--)
			{
    			Integer descNum = markedForDelete[count];
    			if(descNum != -1)
    				descriptors.removeElementAt(markedForDelete[count]);
			}
    		
    		// clear all alias and attribute substitutions
			bookProfile.clearTAliasSubs(tCharId);
			bookProfile.clearTAttributeSubs(tCharId);

    		propsMgr.setActiveManuscript(mScript);
    		clearCharSub = true;
    	}
    	
   		bookProfile.updateSubPair(PropsManager.ObjType.CHARACTER, tCharId, iBookCharId, tCharList);
   		return clearCharSub;
    }
    
    private boolean editLocSub()
    {
    	if(clearLocSub)
    		iBookLocId = "";
    	
    	BookProfile bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
    	String templateId = bookProfile.getTemplateId();
    	TemplateProfile tProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
    	Vector<LocationProfile> tLocList = tProfile.getLocations();
    	
    	if(clearLocSub)
    	{
    		iBookLocId = "";
    		// when a char sub is cleared and no longer used, need to remove the tChar 
    		// references from the descriptors list in the manuscript
    		String mScriptId = bookProfile.getManuscriptId();
    		Manuscript mScript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, mScriptId);
    		Vector<String[]> descriptors = mScript.getDescriptors();
    		Iterator<String[]> descriptorsI = descriptors.iterator();
    		String[] descriptor = null;
    		int descriptorIdx = 0;
    		Integer dontDelete = new Integer(-1);
    		Integer[] markedForDelete = new Integer[descriptors.size()];
    		while(descriptorsI.hasNext())
    		{
    			descriptor = descriptorsI.next();
    			if(tLocId.equals(descriptor[0]))
    			{
    				markedForDelete[descriptorIdx] = new Integer(descriptorIdx);
    			}
    			else
    				markedForDelete[descriptorIdx] = dontDelete; 
    			descriptorIdx++;
    		}
    		
    		// delete items marked, starting at the end and working backwards so that
    		// the vector never shrinks smaller than the descriptorIdx
    		for(int count = markedForDelete.length - 1; count >= 0; count--)
			{
    			Integer descNum = markedForDelete[count];
    			if(descNum != -1)
    				descriptors.removeElementAt(markedForDelete[count]);
			}
    		
    		// clear all alias and attribute substitutions
			bookProfile.clearTAliasSubs(tLocId);
			bookProfile.clearTAttributeSubs(tLocId);

    		propsMgr.setActiveManuscript(mScript);
    		clearLocSub = true;
    	}
    	
   		bookProfile.updateSubPair(PropsManager.ObjType.LOCATION, tLocId, iBookLocId, tLocList);
   		return clearLocSub;
    }
    
    private boolean editGeolocSub()
    {
    	if(clearGeolocSub)
    		iBookGeolocId = "";
    	
    	BookProfile bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
    	String templateId = bookProfile.getTemplateId();
    	TemplateProfile tProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
    	Vector<GeolocaleProfile> tGeolocList = tProfile.getGeolocs();

    	if(clearGeolocSub)
    	{
    		iBookGeolocId = "";
    		// when a char sub is cleared and no longer used, need to remove the tChar 
    		// references from the descriptors list in the manuscript
    		String mScriptId = bookProfile.getManuscriptId();
    		Manuscript mScript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, mScriptId);
    		Vector<String[]> descriptors = mScript.getDescriptors();
    		Iterator<String[]> descriptorsI = descriptors.iterator();
    		String[] descriptor = null;
    		int descriptorIdx = 0;
    		Integer dontDelete = new Integer(-1);
    		Integer[] markedForDelete = new Integer[descriptors.size()];
    		while(descriptorsI.hasNext())
    		{
    			descriptor = descriptorsI.next();
    			if(tGeolocId.equals(descriptor[0]))
    			{
    				markedForDelete[descriptorIdx] = new Integer(descriptorIdx);
    				bookProfile.clearTAliasSubs(tGeolocId);
    				bookProfile.clearTAttributeSubs(tGeolocId);
    			}
    			else
    				markedForDelete[descriptorIdx] = dontDelete; 
    			descriptorIdx++;
    		}
    		
    		// delete items marked, starting at the end and working backwards so that
    		// the vector never shrinks smaller than the descriptorIdx
    		for(int count = markedForDelete.length - 1; count >= 0; count--)
			{
    			Integer descNum = markedForDelete[count];
    			if(descNum != -1)
    				descriptors.removeElementAt(markedForDelete[count]);
			}
    		
    		// clear all alias and attribute substitutions
			bookProfile.clearTAliasSubs(tGeolocId);
			bookProfile.clearTAttributeSubs(tGeolocId);


    		propsMgr.setActiveManuscript(mScript);
    		clearGeolocSub = true;
    	}
    	
   		bookProfile.updateSubPair(PropsManager.ObjType.GEOLOCALE, tGeolocId, iBookGeolocId, tGeolocList);
   		return clearGeolocSub;
    }
    
    private BookProfile createBook()
    {
    	BookProfile profile = new BookProfile();
    	
    	TemplateProfile templateProfile = propsMgr.getTemplateProfile(templateId);
    	profile.setSourceAuthorNamePrefix(templateProfile.getAuthorNamePrefix());
    	profile.setSourceAuthorNameFirst(templateProfile.getAuthorNameFirst());
    	profile.setSourceAuthorNameMiddle(templateProfile.getAuthorNameMiddle());
    	profile.setSourceAuthorNameLast(templateProfile.getAuthorNameLast());
    	profile.setSourceAuthorNameSuffix(templateProfile.getAuthorNameSuffix());
    	profile.setNamePrefix(namePrefix);
    	profile.setNameFirst(nameFirst);
    	profile.setNameMiddle(nameMiddle);
    	profile.setNameLast(nameLast);
    	profile.setNameSuffix(nameSuffix);
    	profile.setTitle(projectName);
    	profile.setSourceTitle(templateProfile.getTitle());
//    	profile.setTemplate(templateId);
    	profile.setTemplate(templateProfile.getFilename());
    	
    	// charSub matrix
    	Vector<CharacterProfile> tChars = templateProfile.getCharacters();
    	
//    	CharacterSub charSubMatrix = new CharacterSub();
    	ObjSubstitutionsManager objSubMgr = new ObjSubstitutionsManager();
    	
    	Iterator<CharacterProfile> tCharsI = tChars.iterator();
    	while(tCharsI.hasNext())
    	{
    		// initialize the matrix with list of characters in template
//    		charSubMatrix.addCharSub(((CharacterProfile)tCharsI.next()).getId(), "");
    		objSubMgr.addSub(PropsManager.ObjType.CHARACTER, ((CharacterProfile)tCharsI.next()).getId(), "");
    	}
    	
    	// geolocSub matrix
    	Vector<GeolocaleProfile> tGeolocs = templateProfile.getGeolocs();
    	Iterator<GeolocaleProfile> tGeolocsI = tGeolocs.iterator();
    	while(tGeolocsI.hasNext())
    	{
    		// initialize the matrix with list of geolocales in template
    		objSubMgr.addSub(PropsManager.ObjType.GEOLOCALE, ((GeolocaleProfile)tGeolocsI.next()).getId(), "");
    	}
    	
    	// locSub matrix
    	Vector<LocationProfile> tLocs = templateProfile.getLocations();
    	Iterator<LocationProfile> tLocsI = tLocs.iterator();
    	while(tLocsI.hasNext())
    	{
    		// initialize the matrix with list of locations in template
    		objSubMgr.addSub(PropsManager.ObjType.LOCATION, ((LocationProfile)tLocsI.next()).getId(), "");
    	}
    	
    	profile.setObjSubMgr(objSubMgr);
    	
    	// get new book id from the propsMgr
    	String bookId = "";
    	// get the objName portion of the to-be-created book id from the template id
    	String objName = templateId.substring(0, templateId.lastIndexOf("_iBookTemplate"));

    	bookId = propsMgr.getId(PropsManager.ObjType.BOOK, objName);
    	
    	profile.setId(bookId); 
    	
    	// create manuscript obj
    	Manuscript mScript = new Manuscript();
    	mScript.setBookId(bookId);
    	mScript.setIBookAuthor(profile.getAuthorName());
    	mScript.setIBookTitle(profile.getTitle());
    	mScript.setManuscriptName(Utilities.normalizeName(profile.getTitle()));
    	mScript.setId(propsMgr.getId(PropsManager.ObjType.MANUSCRIPT, mScript.getMsName()));
    	mScript.setSourceAuthor(profile.getSourceAuthorName());
    	mScript.setSourcePubDate(templateProfile.getPubdate());
    	mScript.setSourceTitle(profile.getSourceTitle());
    	
    	profile.setManuscriptId(mScript.getId());
    	
    	propsMgr.setActiveManuscript(mScript);
    	
    	propsMgr.logMsg(PropsManager.LogLevel.INFO, "user " + userId + " created new book in ManageProfile.createBook()");

    	return profile;
    }
    
    private String doLocation()
    { 
    	Vector<String> descriptions = new Vector<String>();
//    	Vector<String> aliases = new Vector<String>();
    	
    	LocationProfile locProfile = null;
     	TemplateProfile templateProfile = null;
    	
		if(!isTemplateLoc)
			locProfile = (LocationProfile)propsMgr.getProfile(PropsManager.ObjType.LOCATION, loc_id);
		else
    	{
    		templateProfile = propsMgr.getTemplateProfile(templateId);
			locProfile = propsMgr.getTemplateLocation(templateId, loc_id);
			Vector<LocationProfile> locProfiles = templateProfile.getLocations();
			Iterator<LocationProfile> locsI = locProfiles.iterator();
			while(locsI.hasNext())
			{
				locProfile = (LocationProfile)locsI.next();
				if(locProfile.getId().compareTo(loc_id) == 0)
					break;
			}
    	}
		
        if(aliasIndexHasValue)
        	aliases.add(aliasIndexValue);
        
        if(attributeIndexHasValue)
        	descriptions.add(attributeIndexValue);
        
    	if(addAlias)
    	{
    		locProfile.addAlias(alias);
    		redirect_target = "/iBook/editor/editAliases.jsp?templateId=" + templateId + "&loc_id=" + loc_id;
    	}
    	else if(addAttribute)
    	{
    		redirect_target = "/iBook/editor/editLocation.jsp?templateId=" + templateId + "&loc_id=" + loc_id;
    		if(text.length() > 0)
    			propsMgr.addGeolocaleAttribute(loc_id, text);
    	}
    	else if(addLocAttr)
    	{
    		redirect_target = "/iBook/editor/editLocation.jsp?templateId=" + templateId + "&loc_id=" + loc_id;
    		locProfile.addLocationDesc(text);
    	}
    	else if(createLocation)
    	{
    		locProfile = createLocation();
    		
        	if(templateId.compareTo("null") == 0)
        	{
        		propsMgr.addLocationProfile(locProfile);
        		redirect_target = "/iBook/editor/editLocation.jsp?templateId=" + templateId + "&loc_id=" + locProfile.getId();
        	}
        	else
        	{
        		templateProfile.addLocation(locProfile);
        		propsMgr.setActiveTemplate(templateProfile);
        		redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + templateId;
        	}
    	}
    	else if(deleteAlias)
    	{
    		redirect_target = "/iBook/editor/editAliases.jsp?templateId=" + templateId + "&loc_id=" + loc_id;
    		if(isTemplateLoc)
    		{
    			// deleteAliasNum is a numbered line beginning at 1; 
    			// index is a 0-indexed position in a Vector
    			int index = new Integer(deleteAliasNum) - 1;
    			locProfile.deleteAlias(index);
    		}
    		else
    			propsMgr.removeLocAlias(loc_id, deleteAliasNum);
    	}
    	else if (deleteAttribute)
    	{
			// if no geolocId, we must be deleting a location attribute
    		redirect_target = "/iBook/editor/editLocation.jsp?templateId=" + templateId + "&loc_id=" + loc_id;
    		if(isTemplateLoc)
    		{
    			// deleteAttrNum is a numbered line beginning at 1; 
    			// index is a 0-indexed position in a Vector
    			int index = new Integer(deleteAttrNum) - 1;
    			locProfile.deleteLocationDescription(index);
    		}
    		else
    			propsMgr.removeLocAttribute(loc_id, deleteAttrNum);
    	}
    	else if(deleteLocation)
    	{
    		redirect_target = "/iBook/editor/editLocations.jsp";
    		propsMgr.deleteProfile(PropsManager.ObjType.LOCATION, loc_id);
    	}
    	else if(editAlias)
    	{
    		locProfile = (LocationProfile)propsMgr.getProfile(PropsManager.ObjType.LOCATION, loc_id);
    		locProfile.updateAliases(aliases);
    		redirect_target = "/iBook/editor/editLocation.jsp?templateId=" + templateId + "&loc_id=" + loc_id;
    	}
    	else if(editAttribute)
    	{
    		locProfile = (LocationProfile)propsMgr.getProfile(PropsManager.ObjType.LOCATION, loc_id);
    		locProfile.updateDescriptions(descriptions);
    		redirect_target = "/iBook/editor/editLocation.jsp?templateId=" + templateId + "&loc_id=" + loc_id;
    	}
    	else if(editLocation)
    	{
    		redirect_target = "/iBook/editor/editLocation.jsp?templateId=" + templateId + "&loc_id=" + loc_id;
    	}
    	else if(editLocName)
    	{
    		if(isTemplateLoc)
    			templateProfile.editLocName(loc_id, locationName);
    		else
    			locProfile.setName(locationName);
    		redirect_target = "/iBook/editor/editLocation.jsp?templateId=" + templateId + "&loc_id=" + loc_id;
    	}
    	else if(editLocationType)
    	{
    		redirect_target = "/iBook/editor/editLocation.jsp?templateId=" + templateId + "&loc_id=" + loc_id;
    		if(isTemplateLoc)
    		{
    			Vector<LocationProfile> locs = templateProfile.getLocations();
    			locProfile = null;
    			Iterator<LocationProfile> locsI = locs.iterator();
    			while(locsI.hasNext())
    			{
    				locProfile = (LocationProfile)locsI.next();
    				if(loc_id.compareTo(locProfile.getId())== 0)
    				{
    					locProfile.setType(locationType);
    					break;
    				}
    			}
    		}
    		else
    			((LocationProfile)propsMgr.getProfile(PropsManager.ObjType.LOCATION, loc_id)).setType(locationType);
    	}
    	else
    		redirect_target = "/iBook/editor/editLocations.jsp";
    	
        if(templateId.compareTo("null") == 0)
        	updateXml(PropsManager.ObjType.LOCATION);
        else
        {
        	propsMgr.setActiveTemplate(templateProfile);
        	
        	Vector<LocationProfile> locsChk = templateProfile.getLocations();
        	Iterator<LocationProfile> locsChkI = locsChk.iterator();
        	LocationProfile locChk = null;
        	while(locsChkI.hasNext())
        	{
        		locChk = (LocationProfile)locsChkI.next();
        		if(locChk.getId().compareTo("dracula_template_loc_30") == 0)
        			break;
        	}
        	Vector<String> aliasesChk = locChk.getAliases();
        	int numAliases = aliasesChk.size();
        	String qty = numAliases == 1 ? " alias" : " aliases";
        	updateXml(PropsManager.ObjType.TEMPLATE);
        }
    	return redirect_target;
    }

    private String doGeolocale()
    { 
    	GeolocaleProfile geolocProfile = null;
    	
    	Vector<String> descriptions = new Vector<String>();
//    	Vector<String> aliases = new Vector<String>();
    	
    	
    	TemplateProfile templateProfile = null;
    	if(isTemplateGeoloc)
    	{
    		templateProfile = propsMgr.getTemplateProfile(templateId);
    	} 
    	
    	if(addAlias)
    	{
     		if(isTemplateGeoloc)
    			geolocProfile = templateProfile.getGeoloc(geolocId);
    		else
    			geolocProfile = (GeolocaleProfile)propsMgr.getProfile(PropsManager.ObjType.GEOLOCALE, geolocId);
    		geolocProfile.addAlias(text);
    		redirect_target = "/iBook/editor/editRegion.jsp?templateId=" + templateId + "&geolocId=" + geolocId;
    	}
    	
    	if(addAttribute)
    	{
    		if(text.length() > 0)
    		{
    			if(!isTemplateGeoloc)
    				propsMgr.addGeolocaleAttribute(geolocId, text);
    			else
    			{
    				templateProfile.addGeolocAttribute(geolocId, text);
    			}
    		}
    		redirect_target = "/iBook/editor/editLocations.jsp";
    	}
    	
        if(aliasIndexHasValue)
        	aliases.add(aliasIndexValue);
        
        if(attributeIndexHasValue)
        	descriptions.add(attributeIndexValue);
        
    	if(addExistingLocale)
    	{
    		// in locale selection form (editLocation.jsp) select menu is "region_id"
    		geolocId = region_id;
    		propsMgr.addGeolocaleToLocation(geolocId, loc_id);
    		redirect_target = "/iBook/editor/editLocation.jsp?templateId=" + templateId + "&loc_id=" + loc_id;
    	}
    	else if(createLocale)
    	{
    		geolocProfile = createGeolocale();
        	if(templateId != null && templateId.compareTo("null") != 0)
        	{
        		templateProfile.addGeoloc(geolocProfile);
        		propsMgr.setActiveTemplate(templateProfile);
        		redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + templateId;
        	}
        	else
        	{
        		propsMgr.addGeolocaleProfile(geolocProfile);
        		if(loc_id != null && loc_id.compareTo("null") != 0)
        			propsMgr.addGeolocaleToLocation(geolocProfile.getId(), loc_id);
        		redirect_target = "/iBook/editor/editLocations.jsp";
        	}
    	}
    	else if(deleteAlias)
    	{
    		// index = deleteAliasNum; index is 0-based position in Vector, deleteAliasNum 
    		// is numbered position in a list starting at 1. Need to compensate before
    		// calling deleteAlias()
    		int index = new Integer(deleteAliasNum) - 1;
    		if(isTemplateGeoloc)
    		{
    			geolocProfile = templateProfile.getGeoloc(geolocId);
    			geolocProfile.deleteAlias(index);
    		}
    		else
    		{
    			propsMgr.deleteAlias(PropsManager.ObjType.GEOLOCALE, geolocId, index);
    		}
    		redirect_target = "/iBook/editor/editRegionAliases.jsp?templateId=" + templateId + "&geolocId=" + geolocId;
    	}
    	else if(deleteAttribute)
    	{
    		// removeGeolocAttribute() will normalize deleteAttrNum (a line number in a 
    		// numbered list, beginning with 1) and the 0-based index in the attributes 
    		// vector, so no adjustment is made (cf. deleteAlias)
    		int index = new Integer(deleteAttrNum) - 1;
    		if(isTemplateGeoloc)
    		{
    			geolocProfile = templateProfile.getGeoloc(geolocId);
    			geolocProfile.deleteAttribute(index);
    		}
    		else
    		{
        		propsMgr.removeGeolocAttribute(geolocId, deleteAttrNum);
    		}
    		redirect_target = "/iBook/editor/editRegion.jsp?templateId=" + templateId + "&geolocId=" + geolocId;
    	}
    	else if(deleteGeoloc)
    	{
    		propsMgr.deleteProfile(PropsManager.ObjType.GEOLOCALE, geolocId);
    		if(propsMgr.getGeolocsCount() > 0)
    			redirect_target = "/iBook/editor/editRegion.jsp?templateId=" + templateId + "&loc_id=" + loc_id + "&geolocId=" + geolocId;
    		else
    			redirect_target = "/iBook/editor/editLocations.jsp";
    	}
    	else if(editAlias)
    	{
    		if(isTemplateGeoloc)
    		{
    			Vector<GeolocaleProfile> geolocs = templateProfile.getGeolocs();
    			Iterator<GeolocaleProfile> geolocsI = geolocs.iterator();
    			GeolocaleProfile geoloc = null;
    			while(geolocsI.hasNext())
    			{
    				geoloc = geolocsI.next();
    				if(geoloc.getId().compareTo(geolocId) == 0)
    				{
    					geoloc.updateAliases(aliases);
    					break;
    				}
    			}
    			templateProfile.updateGeolocs(geolocs);
    		}
    		else
    		{
	    		geolocProfile = propsMgr.getGeolocale(geolocId);
	    		geolocProfile.updateAliases(aliases);
    		}
    		redirect_target = "/iBook/editor/editRegionAliases.jsp?templateId=" + templateId + "&geolocId=" + geolocId;
    	}    	
    	else if(editAttribute)
    	{
    		if(isTemplateGeoloc)
    		{
    			Vector<GeolocaleProfile> geolocs = templateProfile.getGeolocs();
    			Iterator<GeolocaleProfile> geolocsI = geolocs.iterator();
    			GeolocaleProfile geoloc = null;
    			while(geolocsI.hasNext())
    			{
    				geoloc = geolocsI.next();
    				if(geoloc.getId().compareTo(geolocId) == 0)
    				{
    					geoloc.updateDescriptions(descriptions);
    					break;
    				}
    			}
    			templateProfile.updateGeolocs(geolocs);
    		}
    		else
    		{
    			geolocProfile = propsMgr.getGeolocale(geolocId);
    			geolocProfile.updateDescriptions(descriptions);
    		}
    		redirect_target = "/iBook/editor/editRegionAliases.jsp?templateId=" + templateId + "&geolocId=" + geolocId;
    	}
    	else if(editGeoloc)
    		redirect_target="/iBook/editor/editRegion.jsp?templateId=" + templateId + "&geolocId=" + geolocId;
    	else if(editGeolocName)
    	{
    		if(isTemplateGeoloc)
    			templateProfile.editGeolocName(geolocId, newGeolocName);
    		else
    		{
        		geolocProfile = propsMgr.getGeolocale(geolocId);
    			geolocProfile.setName(newGeolocName);
    		}
    		redirect_target = "/iBook/editor/editRegion.jsp?templateId=" + templateId + "&geolocId=" + geolocId;
    	}
    	else if(editGeolocType)
    	{
    		if(isTemplateGeoloc)
    			templateProfile.editGeolocType(geolocId, newGeolocType);
    		else
    		{
        		geolocProfile = propsMgr.getGeolocale(geolocId);
    			geolocProfile.setType(newGeolocType);
    		}
    		redirect_target = "/iBook/editor/editRegion.jsp?templateId=" + templateId + "&geolocId=" + geolocId;
    	}
    	else if(removeGeolocFromLocation)
    	{
    		propsMgr.removeGeolocReference(geolocId, loc_id);
    		redirect_target = "/iBook/editor/editLocation.jsp?templateId=" + templateId + "&loc_id=" + loc_id;
    	}

    	boolean haveTemplateId = (templateId != null && templateId.compareTo("null") != 00);
    	ObjType type = haveTemplateId ? ObjType.TEMPLATE : ObjType.GEOLOCALE;
    	if(!editGeoloc)
    	{
    		propsMgr.setActiveTemplate(templateProfile);
    		updateXml(type);
    	}
    	
    	return redirect_target;
    }
    
    private GeolocaleProfile createGeolocale()
    {
    	GeolocaleProfile profile = new GeolocaleProfile();
    	profile.setName(name);
    	if(description.length() > 0)
    		profile.addDescription(description);
    	Vector<String> geolocales = propsMgr.getGeolocaleIdList();
    	Iterator<String> geolocalesI = geolocales.iterator();
    	
    	// to do:  generate template ID if template geoloc
    	// and make sure profile goes to template profile, not the location list

    	String id = "";
    	String objName = profile.getName();
    	objName = Utilities.replaceChars(objName, " ", "_", "all");
    	id = propsMgr.getId(PropsManager.ObjType.GEOLOCALE, objName);
		profile.setId(id.toLowerCase());
    		
    	return profile;
    }
    private boolean editCharNames(CharacterProfile profile)
    {
    	boolean ret = true;

    	profile.setNamePrefix(namePrefix);
    	profile.setNameFirst(nameFirst);
    	profile.setNameMiddle(nameMiddle);
    	profile.setNameLast(nameLast);
    	profile.setNameSuffix(nameSuffix);
    	profile.setShortName(nameShort);
    	
    	return ret;
    }
    
    private CharacterProfile createCharacter()
    {
    	CharacterProfile profile = new CharacterProfile();
    	profile.setShortName(nameShort);
		profile.setNamePrefix(namePrefix);
		profile.setNameFirst(nameFirst);
		profile.setNameMiddle(nameMiddle);
		profile.setNameLast(nameLast);
		profile.setNameSuffix(nameSuffix);
		
		profile.setAge(age);
		profile.setGender(gender);
		
		profile.addAttribute(description);
    	
    	Vector<String> characters = templateId.compareTo("null") == 0 ? propsMgr.getCharacterIdList() : propsMgr.getTemplateCharacterList(templateId);

    	String id = "";
    	String objName = profile.getShortName();
    	// no short name? cycle through them all until something is found;
    	// can't have gotten this far without at least one name specified
    	int count = 0;
    	while(objName.length() == 0)
    	{
    		if(count == 0)
    			objName = nameFirst;
    		if(count == 1)
    			objName = nameLast;
    		if(count == 2)
    			objName = namePrefix;
    		if(count == 3)
    			objName = nameMiddle;
    		if(count == 4)
    			objName = nameSuffix;
    		count++;
    	}

    	objName = Utilities.replaceChars(objName, " ", "_", "all");
    	id = propsMgr.getId(PropsManager.ObjType.CHARACTER, objName);
		profile.setId(id.toLowerCase());
    		
    	return profile;
    }

    private LocationProfile createLocation()
    {
    	/*
    	 * locationName, description and locType were set in getParams()
    	 */
    	LocationProfile profile = new LocationProfile();
    	profile.setName(locationName);
    	if(description.length() > 0)
    		profile.addLocationDesc(description);
    	profile.setType(locType);
    	
    	String objName = profile.getName();
    	objName = Utilities.replaceChars(objName, " ", "_", "all");
    	String id = propsMgr.getId(PropsManager.ObjType.LOCATION, objName);
		profile.setId(id.toLowerCase());
    		
    	return profile;
    }

  	private String doCharacter()
    { 
        Vector<String> descriptions = new Vector<String>();
//        Vector<String> aliases = new Vector<String>();

    	CharacterProfile charProfile = null;
    	
    	TemplateProfile templateProfile = null;
    	boolean editingTemplate = false;
    	if(templateId.compareTo("null") != 0)
    	{
    		editingTemplate = true;
    		templateProfile = propsMgr.getTemplateProfile(templateId);
    	}
    	
        if(addAlias)
        {
        	charProfile = templateId.compareTo("null") == 0 ? propsMgr.getCharacterProfile(charId) : templateProfile.getCharacter(charId);
        	charProfile.addAlias(akaName);
        	redirect_target = "/iBook/editor/editCharacter.jsp?templateId=" + templateId + "&charId=" + charProfile.getId();
        }
        
        if(addAttribute)
        {
        	// adds a single attribute to the profile, from editCharacter.jsp
        	charProfile = templateId.compareTo("null") == 0? propsMgr.getCharacterProfile(charId) : templateProfile.getCharacter(charId);
        	redirect_target = "/iBook/editor/editCharacter.jsp?templateId=" + templateId + "&charId=" + charProfile.getId();
        	charProfile.addAttribute(attributeText);
//        	propsMgr.addAttribute(PropsManager.ObjType.CHARACTER, charId, attributeText);
        }
        
//        if(aliasIndexHasValue)
//        	aliases.add(aliasIndexValue);
        
//        if(attributeIndexHasValue)
//        	descriptions.add(attributeIndexValue);
        
        if(createCharacter)
        {
        	charProfile = createCharacter();
        	if(templateId.compareTo("null") == 0)
        	{
        		propsMgr.addCharacterProfile(charProfile);
        		redirect_target = "/iBook/editor/editCharacter.jsp?charId=" + charProfile.getId();
        	}
        	else
        	{
        		templateProfile.addCharacter(charProfile);
        		propsMgr.setActiveTemplate(templateProfile);
        		redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + templateId;
        	}
        }
        
        if(deleteAlias)
        {
        	int index = Integer.parseInt(deleteAliasNum);
        	if(templateId.compareTo("null") == 0)
        	{
        		propsMgr.deleteAlias(PropsManager.ObjType.CHARACTER, charId, index);
        	}
        	else
        	{
        		charProfile = templateProfile.getCharacter(charId);
        		charProfile.deleteAlias(index);
        	}
    		redirect_target = "/iBook/editor/editCharacter.jsp?templateId=" + templateId + "&charId=" + charId;
        }
        if(deleteAttribute)
        {
    		redirect_target = "/iBook/editor/editCharacter.jsp?templateId=" + templateId + "&charId=" + charId;
       		
    		if(templateId.compareTo("null") == 0)
    			propsMgr.removeCharAttribute(charId, deleteAttrNum);
    		else
    		{
    			int index = Integer.parseInt(deleteAttrNum);
    			charProfile = templateProfile.getCharacter(charId);
    			charProfile.deleteAttribute(index);
    		}
        }
    	if(deleteCharacter)
    	{
    		redirect_target = "/iBook/editor/editCharacters.jsp";
    		propsMgr.deleteProfile(PropsManager.ObjType.CHARACTER, charId);
    	}
    	if(editAliases)
    	{
    		// updates all character aliases
        	charProfile = templateId.compareTo("null") == 0 ? propsMgr.getCharacterProfile(charId) : templateProfile.getCharacter(charId);
        	charProfile.updateAliases(aliases);
    		redirect_target = "/iBook/editor/editCharacterAliases.jsp?templateId=" + templateId + "&charId=" + charId;
    	}
        if(editAttribute)
        {
        	// updates all attributes associated with the profile from editCharAttributes.jsp
    		charProfile = templateId.compareTo("null") == 0 ? propsMgr.getCharacterProfile(charId) : templateProfile.getCharacter(charId);
    		charProfile.updateDescriptions(attributes);
    		redirect_target = "/iBook/editor/editCharAttributes.jsp?templateId=" + templateId + "&charId=" + charId;
         }
        if(editCharacter)
    		redirect_target = "/iBook/editor/editCharacter.jsp?templateId=" + templateId + "&charId=" + charId;
        
        if(editNames)
        {
        	CharacterProfile profile = null;
        	templateProfile = null;
        	if(templateId.compareTo("null") == 0)
        		profile = propsMgr.getCharacterProfile(charId);
        	else
        	{
        		templateProfile = propsMgr.getTemplateProfile(templateId);
        		profile = templateProfile.getCharacter(charId);
        	}

        	editCharNames(profile);
        	redirect_target = "/iBook/editor/editCharacter.jsp?templateId=" + templateId + "&charId=" + charId;
        }
        if(updateAge)
        {
        	if(templateId.compareTo("null") == 0)
        	{
        		charProfile = propsMgr.getCharacterProfile(charId);
        	}
        	else
        	{
        		templateProfile = propsMgr.getTemplateProfile(templateId);
        		charProfile = templateProfile.getCharacter(charId);
        	}
        	charProfile.setAge(age);
    		redirect_target = "/iBook/editor/editCharacter.jsp?templateId=" + templateId + "&charId=" + charId;
        }
        if(updateGender)
        {
        	if(templateId.compareTo("null") == 0)
        		charProfile = propsMgr.getCharacterProfile(charId);
        	else
        	{
        		templateProfile = propsMgr.getTemplateProfile(templateId);
        		charProfile = templateProfile.getCharacter(charId);
        	}
        	charProfile.setGender(gender);
    		redirect_target = "/iBook/editor/editCharacter.jsp?templateId=" + templateId + "&charId=" + charId;
        }
        if(setCharContext)
        {
        	// charContext only valid for template character profile
    		templateProfile = propsMgr.getTemplateProfile(templateId);
    		charProfile = templateProfile.getCharacter(charId);
        	charProfile.setContext(charContext);
        	redirect_target = "/iBook/editor/editCharacter.jsp?templateId=" + templateId + "&charId=" + charId;
        }

        if(templateId.compareTo("null") == 0)
        	updateXml(PropsManager.ObjType.CHARACTER);
        else
        {
        	propsMgr.setActiveTemplate(templateProfile);
        	updateXml(PropsManager.ObjType.TEMPLATE);
        }
    	return redirect_target;
    }
  	
  	private boolean updateXml(PropsManager.ObjType type)
  	{
  		boolean ret = true;
  		String xml = "";
  		XmlFactory xmlFactory = new XmlFactory();
  		String outputFilename = "";
  		
  		switch(type)
  		{
	  		case BOOK:
	  			outputFilename = "bookList.xml";
	  			break;
	  		case CHARACTER:
	  			outputFilename = "characterList.xml";
	  			break;
	  		case LOCATION:
	  		case GEOLOCALE:
	  			outputFilename = "locationList.xml";
	  			break;
	  		case MANUSCRIPT:
	  			outputFilename = "manuscripts" + pathMrkr + propsMgr.getActiveManuscript().getFilename();
	  			break;
	  		case TEMPLATE:
	  			outputFilename = "templates" + pathMrkr + propsMgr.getActiveTemplate().getFilename();
	  			break;
  		}
		xml = xmlFactory.doNewXml(propsMgr, type);
		propsMgr.logMsg(PropsManager.LogLevel.DEBUG, "ManageProfile.updateXml() ready to write new " + path_to_user_dir + outputFilename);
  		Utilities.write_file(path_to_user_dir + outputFilename, xml, true);
  		propsMgr.logMsg(PropsManager.LogLevel.DEBUG, "back in ManageProfile.updateXml() after Utilities.write_file()");
  		
  		return ret;
  	}
  	
  	private void getParams(HttpServletRequest request, HttpServletResponse response)
  	{
  		addAlias = false;
  		addAttribute = false;
  		addExistingLocale = false;
  		addLocAttr = false;
  		aliasIndexHasValue = false;
  		attributeIndexHasValue = false;
  		clearCharSub = false;
  		clearGeolocSub = false;
  		createCharacter = false;
  		createLocale = false;
  		clearLocSub = false;
  		createBook = false;
  		createLocation = false;
  		deleteAlias = false;
  		deleteAttribute = false;
  		deleteBook = false;
  		deleteCharacter = false;
  		deleteGeoloc = false;
  		deleteLocation = false;
  		editAlias = false;
  		editAliases = false;
  		editAttribute = false;
  		editBook = false;
  		editCharacter = false;
  		editNames = false;
  		editGeoloc = false;
  		editGeolocName = false;
  		editGeolocType = false;
  		editLocation = false;
  		editLocationType = false;
  		editLocName = false;
  		editTitle = false;
  		isTemplateGeoloc = false;
  		isTemplateLoc = false;
  		mScriptChanged = false;
  		removeGeolocFromLocation = false;
  		resetAuthorName = false;
  		setCharContext = false;
  		setCharSub = false;
  		setGeolocSub = false;
  		setLocSub = false;
  		updateAge = false;
  		updateGender = false;

  	    age = "";
  		akaName = "";
  		alias = "";
  		aliasIndexValue = "";
  		attributeText = "";
  		attributeIndexValue = "";
  		bookId = "";
  		bookTitle = "";
  		charContext = "";
  		charId = "";
  		deleteAliasNum = "";
  		deleteAttrNum = "";
  		description = "";
  		gender = "";
  		geolocId = "";
  		key = "";
  		iBookCharId = "";
  		iBookGeolocId = "";
  		iBookLocId = "";
  		loc_id = "";
  		locationName = "";
  		locationType = "";
  		locType = "";
  		name = "";
  		namePrefix = "";
  		nameFirst = "";
  		nameMiddle = "";
  		nameLast = "";
  		nameShort = "";
  		nameSuffix = "";
  		newGeolocName = "";
  		newGeolocType = "";
  		projectName = "";
  		redirect_target = "/iBook/editor/index.jsp";
  		region_id = "";
  		tCharId = "";
  		templateId = "";
  		text = "";
  		tGeolocId = "";
  		tLocId = "";
  		type = "";
  		userId = "";
  		value = "";
  		
  		aliases = new Vector<String>();
  		attributes = new Vector<String>();
  		
        // Get the value of all request parameters
        Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
//        	if(value.length() > 0) System.out.println(key + " = " + value);
        	
    		if(key.compareTo("addAlias") == 0)
    			addAlias = true;
    		
    		if(key.compareTo("addExistingLocale") == 0)
    			addExistingLocale = true;
    		
    		if(key.compareTo("addGeolocAliasSubmit") == 0)
    			addAlias = true;

    		if(key.compareTo("age") == 0)
    			age = value;
    		
            if(key.compareTo("akaName") == 0)
            	akaName = value;
            
    		if(key.compareTo("alias") == 0)
    			text = value;
    		
    		if(key.indexOf("aliasIndex_") != -1)
    		{
//    			aliasIndexHasValue = true;
//    			aliasIndexValue = value;
   				aliases.add(value);
    		}
        	
            if(key.compareTo("attributeText") == 0)
            {
            	attributeText = value;
            }
            
    		if(key.indexOf("attrIndex_") != -1)
    		{
//    			attributeIndexHasValue = true;
//    			attributeIndexValue = value;
    			attributes.add(value);
    		}

        	if(key.compareTo("bindTemplate") == 0)
    			createBook = true;
    		
    		if(key.compareTo("bookId") == 0)
    			bookId = value;
    		
    		if(key.compareTo("bookTitle") == 0)
    			bookTitle = value;
    		
            if(key.compareTo("charId") == 0)
            	charId = value;
            
    		if(key.compareTo("clearCharSubSubmit") == 0)
    			clearCharSub = true;
    		
    		if(key.compareTo("clearGeolocSubSubmit") == 0)
    			clearGeolocSub = true;
    		
    		if(key.compareTo("clearLocSubSubmit") == 0)
    			clearLocSub = true;
    		
    		if(key.compareTo("context") == 0)
    			charContext = value;
    		
    		if(key.compareTo("createLocale") == 0)
    			createLocale = true;
    		
    		if(key.compareTo("createLocation") == 0)
    			createLocation = true;

    		if(key.compareTo("description") == 0)
    			description = value;
    		
    		if(key.compareTo("deleteAliasButton") == 0)
    			deleteAlias = true;
    		
    		if(key.compareTo("deleteAliasNum") == 0)
    			deleteAliasNum = value.trim();
    		
            if(key.compareTo("deleteAliasSubmit") == 0)
            	deleteAlias = true;
            
            if(key.compareTo("deleteAttrButton") == 0)
            	deleteAttribute = true;

    		if(key.compareTo("deleteAttrNum") == 0)
    			deleteAttrNum = value.trim();
    		
    		if(key.compareTo("deleteBook") == 0)
    			deleteBook = true;
    		
            if(key.compareTo("deleteCharacter") == 0)
            	deleteCharacter = true;
            
    		if(key.compareTo("deleteGeoloc") == 0)
    			deleteGeoloc = true;
    		
    		if(key.compareTo("deleteLocation") == 0)
    			deleteLocation = true;

            if(key.compareTo("editAliasSubmit") == 0)
            	editAliases = true;
            
    		if(key.compareTo("editAttrSubmit") == 0)
    			editAttribute = true;
    		
            if(key.compareTo("editCharacter") == 0)
            	editCharacter = true;
            
    		if(key.compareTo("editBook") == 0)
    			editBook = true;
    		
    		if(key.compareTo("editLocation") == 0)
    			editLocation = true;
    		
    		if(key.compareTo("editLocType") == 0)
    			editLocationType = true;
    		
    		if(key.compareTo("editTitleSubmit") == 0)
    			editTitle = true;
    		
    		if(key.compareTo("editAliasSubmit") == 0)
    			editAlias = true;

    		if(key.compareTo("editAttrSubmit") == 0)
    			editAttribute = true;
    		
    		if(key.compareTo("editGeoloc") == 0)
    			editGeoloc = true;
    		
    		if(key.compareTo("gender") == 0)
    			gender = value;

    		if(key.compareTo("geolocId") == 0)
    			geolocId = value;
    		
    		if(key.compareTo("loc_id") == 0)
    			loc_id = value;
    		
    		if(key.compareTo("iBookCharId") == 0)
    			iBookCharId = value;
    		
    		if(key.compareTo("iBookGeolocId") == 0)
    			iBookGeolocId = value;
    		
    		if(key.compareTo("iBookLocId") == 0)
    			iBookLocId = value;
    		
    		if(key.compareTo("loc_id") == 0)
    			loc_id = value;
    		
    		if(key.compareTo("nameFirst") == 0)
    			nameFirst = value;
    		
    		if(key.compareTo("nameLast") == 0)
    			nameLast = value;
    		
    		if(key.compareTo("nameMiddle") == 0)
    			nameMiddle = value;
    		
    		if(key.compareTo("namePrefix") == 0)
    			namePrefix = value;
    		
    		if(key.compareTo("nameSuffix") == 0)
    			nameSuffix = value;
    		
    		if(key.compareTo("nameShort") == 0)
    			nameShort = value;
    		
    		if(key.compareTo("newAlias") == 0)
    			alias = value;

    		if(key.compareTo("newAttr") == 0)
    			text = value;
    		
    		if(key.compareTo("newLocationName") == 0)
    		{
    			locationName = value;
    		}

    		if(key.compareTo("newLocationDescription") == 0)
    		{
    			description = value;
    		}

    		if(key.compareTo("newLocationType") == 0)
    		{
    			locType = value;
    		}

    		if(key.compareTo("newLocName") == 0)
    		{
    			locationName = value;
    			editLocName = true;
    		}
    		
    		if(key.compareTo("newLocType") == 0)
    			locationType = value;
    		
    		if(key.compareTo("newRegionDescription") == 0)
    		{
    			description = value;
    		}
    		
    		if(key.compareTo("newRegionName") == 0)
    		{
    			name = value;
    		}
    		
    		if(key.compareTo("newRegionName") == 0)
    		{
    			newGeolocName = value;
    			editGeolocName = true;
    		}
    		
    		if(key.compareTo("newRegionType") == 0)
    		{
    			newGeolocType = value;
    			editGeolocType = true;
    		}
    		
    		if(key.compareTo("profileType") == 0)
        		type = request.getParameter(key);
        	
    		if(key.compareTo("projectName") == 0)
    			projectName = value;
    		
    		if(key.compareTo("region_id") == 0)
    			region_id = value;
    		
    		if(key.compareTo("removeGeolocFromLocation") == 0)
    			removeGeolocFromLocation = true;
    		
    		if(key.compareTo("resetAuthorNameSubmit") == 0)
    			resetAuthorName = true;
    		
    		if(key.compareTo("setCharSubSubmit") == 0)
    			setCharSub = true;
    		
    		if(key.compareTo("setContextSubmit") == 0)
    			setCharContext = true;
    		
    		if(key.compareTo("setGeolocSubSubmit") == 0)
    			setGeolocSub = true;
    		
    		if(key.compareTo("setLocSubSubmit") == 0)
    			setLocSub = true;

            if(key.compareTo("setNamesSubmit") == 0)
            	editNames = true;
            
    		if(key.compareTo("submitAttrChanges") == 0)
    			addAttribute = true;
    		
            if(key.compareTo("submitNewCharacter") == 0)
            	createCharacter = true;
            
    		if(key.compareTo("submitNewLocAttr") == 0)
    			addLocAttr = true;

    		if(key.compareTo("tCharId") == 0)
    			tCharId = value;

    		if(key.compareTo("templateId") == 0)
    			templateId = value;

    		if(key.compareTo("templateId") == 0)
    		{
    			templateId = value;
    			if(templateId.compareTo("null") != 0)
    				isTemplateLoc = true;
    		}
    		
    		if(key.compareTo("templateId") == 0)
    		{
    			templateId = value;
    			if(templateId != null && templateId.compareTo("null") != 0)
    				isTemplateGeoloc = true;
    		}

    		if(key.compareTo("templateId") == 0)
    			templateId = value;

    		if(key.compareTo("tGeolocId") == 0)
    			tGeolocId = value;

    		if(key.compareTo("tLocId") == 0)
    			tLocId = value;

            if(key.compareTo("updateAddAttribute") == 0)
            	addAttribute = true;
            
            if(key.compareTo("updateAge") == 0)
            	updateAge = true;
            
            if(key.compareTo("updateGender") == 0)
            	updateGender = true;
        	
    		if(key.compareTo("userId") == 0)
        		userId = value;
 
        }
  	}
}
