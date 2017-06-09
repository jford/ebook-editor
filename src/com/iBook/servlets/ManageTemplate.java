package com.iBook.servlets;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;
import java.util.*;

import com.iBook.datastore.PropsManager;
import com.iBook.datastore.PropsManager.ObjType;
import com.iBook.datastore.XmlFactory;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.books.*;
import com.iBook.datastore.locations.*;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.Utilities;

public class ManageTemplate extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private String path_to_resources = PropsManager.getPathToResources();
    private String path_to_user_dir = ""; 
    private String pathMrkr = PropsManager.getPathMarker();
    
    private PropsManager propsMgr = null;
    
    public ManageTemplate()
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
        
        boolean createTemplate = false;
        boolean createTemplateCharacter = false;
        boolean createTemplateGeoloc = false;
        boolean createTemplateLocation = false;
        boolean editTemplate = false;
        boolean deleteTemplate = false;
        boolean editTemplateChar = false;
        boolean editTemplateGeoloc = false;
        boolean editTemplateLoc = false;
        boolean editTemplatePubDetails = false;
        boolean renameTemplate = false;
        boolean revertTemplates = false;
        boolean updateTemplates = false;
        
        /*
         * ToDo:  Add edit location and edit geoloc (editTemplateLocSubmit and 
         * editTemplateGeolodSubmit buttons already added to editTemplate.jsp)
         */
        
        // Get the value of all request parameters
        Enumeration<?> params = request.getParameterNames();

        String charId = "";
        String key = "";
        String redirect_target = "/iBook/editor/index.jsp";
//        String filename = "";
        String geolocId = "";
        String locId = "";
        String newTemplateName = "";
        String templateId = "";
        String templateFilename = "";
        String userId = "";
        String value = "";
        
        
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
        	if(key.compareTo("addCharToTemplateSubmit") == 0)
        		redirect_target = addCharToTemplate(request, response);
        	
        	if(key.compareTo("addLocToTemplateSubmit") == 0)
        		redirect_target = addLocToTemplate(request, response);
        	
        	if(key.compareTo("addGeolocToTemplateSubmit") == 0)
        		redirect_target = addGeolocToTemplate(request, response);
        	
        	if(key.compareTo("charId") == 0)
        		charId = value;
        		
        	if(key.compareTo("createTemplateCharacterSubmit") == 0)
        		createTemplateCharacter = true;

        	if(key.compareTo("createTemplateGeolocSubmit") == 0)
        		createTemplateGeoloc = true;
        	
        	if(key.compareTo("createTemplateLocationSubmit") == 0)
        		createTemplateLocation = true;
        	
        	if(key.compareTo("deleteTemplate") == 0)
        		deleteTemplate = true;
        	
        	if(key.compareTo("deleteTemplateCharSubmit") == 0)
        		redirect_target = deleteCharFromTemplate(request, response);
        	
        	if(key.compareTo("deleteTemplateGeolocSubmit") == 0)
        		redirect_target = deleteGeolocFromTemplate(request, response);
        	
        	if(key.compareTo("deleteTemplateLocSubmit") == 0)
        		redirect_target = deleteLocFromTemplate(request, response);
        	
        	if(key.compareTo("editPubDetailsSubmit") == 0)
        		editTemplatePubDetails = true;
        	
        	if(key.compareTo("editTemplate") == 0)
        		editTemplate = true;
        	
        	if(key.compareTo("editTemplateCharSubmit") == 0)
        		editTemplateChar = true;
        	
        	if(key.compareTo("editTemplateGeolocSubmit") == 0)
        		editTemplateGeoloc = true;
        	
        	if(key.compareTo("editTemplateLocSubmit") == 0)
        		editTemplateLoc = true;
        	
        	if(key.compareTo("geolocId") == 0)
        		geolocId = value;
        	
        	if(key.compareTo("locId") == 0)
        		locId = value;
        	
        	if(key.compareTo("newTemplateName") == 0)
        		newTemplateName = value;
        	
        	if(key.compareTo("newTemplateSubmit") == 0)
        		createTemplate = true;
        	
        	if(key.compareTo("renameTemplate") == 0)
        		renameTemplate = true;
        	
        	if(key.compareTo("revertTemplates") == 0)
        		revertTemplates = true;
        	
        	if(key.compareTo("templateFilename") == 0)
        	{
//        		filename = value;
        		templateFilename = value;
        	}
        	
        	if(key.compareTo("templateId") == 0)
        		templateId = value;
        	
        	if(key.compareTo("updateTemplates") == 0)
        		updateTemplates = true;
        	
        	if(key.compareTo("uploadSrcSubmit") == 0)
        		redirect_target = uploadSourceText(request, response);
        	
        	if(key.compareTo("userId") == 0)
        		userId = value;
        		
        }
        
        propsMgr =  new PropsManager(userId);
        path_to_user_dir = propsMgr.getPathToUserDir();

        if(createTemplate)
        {
        	redirect_target = createTemplate(request, response);
        }
        
        if(createTemplateCharacter)
        	redirect_target = "/iBook/editor/createCharacter.jsp?templateId=" + templateId;
        
        if(createTemplateGeoloc)
        	redirect_target = "/iBook/editor/createRegion.jsp?templateId=" + templateId;
        
        if(createTemplateLocation)
        	redirect_target = "/iBook/editor/createLocation.jsp?templateId=" + templateId;

    	if(deleteTemplate)
    	{
    		templateFilename = propsMgr.getTemplateFilename(templateId);
        	File templateFile = new File(path_to_user_dir + "templates" + pathMrkr + templateFilename);
        	templateFile.delete();
        	redirect_target = "/iBook/editor/editTemplates.jsp";
    	}
    	
        if(editTemplate)
        	redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + templateId;

        if(editTemplateChar)
        	redirect_target = "/iBook/editor/editCharacter.jsp?templateId=" + templateId + "&charId=" + charId;
        
        if(editTemplateGeoloc)
        	redirect_target = "/iBook/editor/editRegion.jsp?templateId=" + templateId + "&geolocId=" + geolocId;
        
        if(editTemplateLoc)
        	redirect_target = "/iBook/editor/editLocation.jsp?templateId=" + templateId + "&loc_id=" + locId;
        
        if(editTemplatePubDetails)
        	redirect_target = editPubDetails(request, response);
        
        if(renameTemplate)
        {
        	if(!newTemplateName.endsWith(".xml"))
        		newTemplateName += ".xml";
        	String newId = propsMgr.getId(PropsManager.ObjType.TEMPLATE, newTemplateName.substring(0, newTemplateName.lastIndexOf(".")));
        	String xfr = Utilities.read_file(path_to_user_dir + "templates" + pathMrkr + templateFilename);
        	String idMrkr = "id=\"";
        	int idx = xfr.indexOf(idMrkr);
        	int end = xfr.indexOf("\"", idx + idMrkr.length());
           	xfr = xfr.substring(0, idx + idMrkr.length()) + newId + xfr.substring(end);
           	xfr = Utilities.replaceChars(xfr, templateFilename, newTemplateName, "first");
        	Utilities.write_file(path_to_user_dir + "templates" + pathMrkr + newTemplateName, xfr, true);
        	redirect_target = "/iBook/editor/editTemplates.jsp";
        }
        
        if(revertTemplates || updateTemplates)
        	propsMgr.updateUserTemplateDir(revertTemplates);
        
        String redirect = "<html>\n"
            + "    <body onload=\"document.location = \'" + redirect_target + "\'\" />\n"
            + "</html>";
        response.setContentLength(redirect.length());
        op.write(redirect.getBytes(), 0, redirect.length());
    }
    
    private String deleteCharFromTemplate(HttpServletRequest request, HttpServletResponse response)
    {
    	String charId = "";
    	String key = "";
    	String redirect_target = "";
    	String templateId = "";
    	String value = "";
    	
        Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
        	if(key.compareTo("charId") == 0)
        		charId = value;
        	
        	if(key.compareTo("templateId") == 0)
        		templateId = value;
        }    	
    	TemplateProfile templateProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
    	templateProfile.deleteCharacter(charId);
    	propsMgr.setActiveTemplate(templateProfile);
    	updateXml(propsMgr);

    	redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + templateId;
    	return  redirect_target;
    }
    
    private String addCharToTemplate(HttpServletRequest request, HttpServletResponse response)
    {
    	String charId = "";
    	String key = "";
    	String redirect_target = "";
    	String templateId = "";
    	String value = "";
    	
        Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
        	if(key.compareTo("charId") == 0)
        		charId = value;
        	
        	if(key.compareTo("templateId") == 0)
        		templateId = value;
        }    	
    	TemplateProfile templateProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
    	Vector<CharacterProfile> tChars = templateProfile.getCharacters();
    	CharacterProfile tChar = null;
    	boolean duplicateChar = false;
    	CharacterProfile charProfile = propsMgr.getCharacterProfile(charId);
    	String charName = charProfile.getName();
    	Iterator<CharacterProfile> tCharsI = tChars.iterator();
    	while(tCharsI.hasNext())
    	{
    		tChar = (CharacterProfile)tCharsI.next();
    		if(charName.compareTo(tChar.getName()) == 0)
    		{
    			duplicateChar = true;
    			break;
    		}
    	}
    	if(!duplicateChar)
    	{
	    	templateProfile.addCharacter(charProfile);
	    	propsMgr.setActiveTemplate(templateProfile);
	
	    	updateXml(propsMgr);
    	}    	
    	redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + templateId;
    	return redirect_target;
    }
    
    private String deleteLocFromTemplate(HttpServletRequest request, HttpServletResponse response)
    {
    	String locId = "";
    	String key = "";
    	String redirect_target = "";
    	String templateId = "";
    	String value = "";
    	
        Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
        	if(key.compareTo("locId") == 0)
        		locId = value;
        	
        	if(key.compareTo("templateId") == 0)
        		templateId = value;
        }    	
    	TemplateProfile templateProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
    	templateProfile.deleteLocation(locId);
    	propsMgr.setActiveTemplate(templateProfile);
    	updateXml(propsMgr);

    	redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + templateId;
    	return  redirect_target;
    }
    
    private String addLocToTemplate(HttpServletRequest request, HttpServletResponse response)
    {
    	String locId = "";
    	String key = "";
    	String redirect_target = "";
    	String templateId = "";
    	String value = "";
    	
        Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
        	if(key.compareTo("locId") == 0)
        		locId = value;
        	
        	if(key.compareTo("templateId") == 0)
        		templateId = value;
        }    	
    	TemplateProfile templateProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
    	Vector<LocationProfile> tLocs = templateProfile.getLocations();
    	LocationProfile tLoc = null;
    	boolean duplicateLoc = false;
    	LocationProfile locProfile = (LocationProfile)propsMgr.getProfile(PropsManager.ObjType.LOCATION, locId);
    	String locName = locProfile.getName();
    	Iterator<LocationProfile> tLocsI = tLocs.iterator();
    	while(tLocsI.hasNext())
    	{
    		tLoc = (LocationProfile)tLocsI.next();
    		if(locName.compareTo(tLoc.getName()) == 0)
    		{
    			duplicateLoc = true;
    			break;
    		}
    	}
    	if(!duplicateLoc)
    	{
	    	templateProfile.addLocation(locProfile);
	    	propsMgr.setActiveTemplate(templateProfile);
	
	    	updateXml(propsMgr);
    	}    	
    	redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + templateId;
    	return redirect_target;
    }
    
    private String deleteGeolocFromTemplate(HttpServletRequest request, HttpServletResponse response)
    {
    	String geolocId = "";
    	String key = "";
    	String redirect_target = "";
    	String templateId = "";
    	String value = "";
    	
        Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
        	if(key.compareTo("geolocId") == 0)
        		geolocId = value;
        	
        	if(key.compareTo("templateId") == 0)
        		templateId = value;
        }    	
    	TemplateProfile templateProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
    	templateProfile.deleteGeoloc(geolocId);
    	propsMgr.setActiveTemplate(templateProfile);
    	updateXml(propsMgr);

    	redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + templateId;
    	return  redirect_target;
    }
    
    private String addGeolocToTemplate(HttpServletRequest request, HttpServletResponse response)
    {
    	String geolocId = "";
    	String key = "";
    	String redirect_target = "";
    	String templateId = "";
    	String value = "";
    	
        Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
        	if(key.compareTo("geolocId") == 0)
        		geolocId = value;
        	
        	if(key.compareTo("templateId") == 0)
        		templateId = value;
        }    	
    	TemplateProfile templateProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
    	Vector<GeolocaleProfile> tGeolocs = templateProfile.getGeolocs();
    	GeolocaleProfile tGeoloc = null;
    	boolean duplicateGeoloc = false;
    	GeolocaleProfile geolocProfile = (GeolocaleProfile)propsMgr.getProfile(PropsManager.ObjType.GEOLOCALE, geolocId);
    	String geolocName = geolocProfile.getName();
    	Iterator<GeolocaleProfile> tGeolocsI = tGeolocs.iterator();
    	while(tGeolocsI.hasNext())
    	{
    		tGeoloc = (GeolocaleProfile)tGeolocsI.next();
    		if(geolocName.compareTo(tGeoloc.getName()) == 0)
    		{
    			duplicateGeoloc = true;
    			break;
    		}
    	}
    	if(!duplicateGeoloc)
    	{
	    	templateProfile.addGeoloc(geolocProfile);
	    	propsMgr.setActiveTemplate(templateProfile);
	
	    	updateXml(propsMgr);
    	}    	
    	redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + templateId;
    	return redirect_target;
    }
    
    private String doTemplate(HttpServletRequest request, HttpServletResponse response)
    {
    	String key = "";
    	String redirect_target = "";
    	String value = "";
    	
    	boolean createTemplate = false;
    	boolean editPubDetails = false;
    	
        Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
        	if(key.compareTo("editPubDetailsSubmit") == 0)
        		editPubDetails = true;
        	
        	if(key.compareTo("newTemplateSubmit") == 0)
        		createTemplate = true;;
        }
    	
        if(createTemplate)
        	redirect_target = createTemplate(request, response);
        
        if(editPubDetails)
        	redirect_target = editPubDetails(request, response);
        
    	return redirect_target;
    }
    
    private String editPubDetails(HttpServletRequest request, HttpServletResponse response)
    {
    	String key = "";
    	String editSrcBkTitle = "";
    	String editSrcBkPubdate = "";
    	String editSrcBkPubtype = "";
    	String editSrcAuthorNamePrefix = "";
    	String editSrcAuthorNameFirst = "";
    	String editSrcAuthorNameMiddle = "";
    	String editSrcAuthorNameLast = "";
    	String editSrcAuthorNameSuffix = "";
    	String redirect_target = "";
    	String templateId = "";
    	String value = "";
    	
    	TemplateProfile profile = null;
    	
        Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
        	if(key.compareTo("editSrcBkTitle") == 0)
        		editSrcBkTitle = value;
        	
        	if(key.compareTo("editSrcBkPubdate") == 0)
        		editSrcBkPubdate = value;
        	
     		if(key.compareTo("editSrcBkPubtype") == 0)
     			editSrcBkPubtype = value;
        	
     		if(key.compareTo("editSrcAuthorNamePrefix") == 0)
     			editSrcAuthorNamePrefix = value;
        	
     		if(key.compareTo("editSrcAuthorNameFirst") == 0)
     			editSrcAuthorNameFirst = value;
        	
     		if(key.compareTo("editSrcAuthorNameMiddle") == 0)
     			editSrcAuthorNameMiddle = value;
        	
     		if(key.compareTo("editSrcAuthorNameLast") == 0)
     			editSrcAuthorNameLast = value;
        	
     		if(key.compareTo("editSrcAuthorNameSuffix") == 0)
     			editSrcAuthorNameSuffix = value;
     		
     		if(key.compareTo("templateId") == 0)
     		{
     			templateId = value;
     			profile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
     		}	
        }
        profile.setTitle(editSrcBkTitle);
        profile.setAuthorNameFirst(editSrcAuthorNameFirst);
        profile.setAuthorNameLast(editSrcAuthorNameLast);
        profile.setAuthorNameMiddle(editSrcAuthorNameMiddle);
        profile.setAuthorNamePrefix(editSrcAuthorNamePrefix);
        profile.setAuthorNameSuffix(editSrcAuthorNameSuffix);
        profile.setPubdate(editSrcBkPubdate);
        profile.setPubtype(editSrcBkPubtype);
//        profile.setFilename(templateId);
        
        propsMgr.setActiveTemplate(profile);
        updateXml(propsMgr);
        
        redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + templateId;
		return redirect_target;
    }
    
    private String createTemplate(HttpServletRequest request, HttpServletResponse response)
    {
    	String key = "";
//    	String filename = "";
    	String msg = "";
    	String namePrefix= "";
    	String nameFirst = "";
    	String nameMiddle = "";
    	String nameLast = "";
    	String nameSuffix = "";
    	String pubdate = "";
    	String pubtype = "";
    	String redirect_target = "/iBook/editor/editTemplates.jsp";
    	String templateFilename = "";
    	String templateId = "";
    	String title = "";
    	String value = "";
    	
    	boolean uploadFile = false;

        TemplateProfile profile = new TemplateProfile();

        XmlFactory xmlFactory = new XmlFactory();

        Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
        	if(key.compareTo("filename") == 0)
        		profile.setFilename(value);
        	
        	if(key.compareTo("namePrefix") == 0)
        		profile.setAuthorNamePrefix(value);
        	
        	if(key.compareTo("nameFirst") == 0)
        		profile.setAuthorNameFirst(value);
        	
        	if(key.compareTo("nameMiddle") == 0)
        		profile.setAuthorNameMiddle(value);
        	
        	if(key.compareTo("nameLast") == 0)
        		profile.setAuthorNameLast(value);
        	
        	if(key.compareTo("nameSuffix") == 0)
        		profile.setAuthorNameSuffix(value);
        	
        	if(key.compareTo("pubdate") == 0)
        		profile.setPubdate(value);
        	
        	if(key.compareTo("pubtype") == 0)
        		profile.setPubtype(value);
        	
        	if(key.compareTo("sourceTitle") == 0)
        		profile.setTitle(value);

        	if(key.compareTo("templateFilename") == 0)
        	{
        		templateFilename = validateFilename(value);
        		templateId = propsMgr.getId(PropsManager.ObjType.TEMPLATE, templateFilename.substring(0, templateFilename.lastIndexOf(".")));
        		profile.setFilename(templateFilename);
        		profile.setId(templateId);
//        		redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + value; 
        	}
        	
        	if(key.compareTo("textSource") == 0)
        		uploadFile = true;
        	
        }
        
        if(uploadFile)
        	uploadSourceText(request, response);
        if(templateFilename.length() > 0)
        {
        	propsMgr.setActiveTemplate(profile);
	        updateXml(propsMgr);
	        redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + profile.getId();
        }
        else
        {
        	msg = Utilities.encodeHtmlParam("Unable to create file named " + templateFilename);
        	redirect_target = "/iBook/editor/createTemplate.jsp?msg=" + msg;
        }
        return redirect_target;
    }
    
    private String validateFilename(String filename)
    {
    	if(!filename.endsWith(".xml"))
    		filename += ".xml";
    	if(filename.startsWith("."))
    		filename = filename.replaceFirst(".", "");
    	return filename;
    }
    
    private String uploadSourceText(HttpServletRequest request, HttpServletResponse response)
    {
    	String key = "";
    	String redirect_target = "";
    	String templateId = "";
    	String value = "";
    	
        Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
        	if(key.compareTo("templateId") == 0)
        		templateId = value;
        }
    	try
    	{
    		processRequest(request, response);
    	}
    	catch(Exception e)
    	{
    		System.out.println("error uploading file in ManageTemplate");
    		e.printStackTrace();
    	}
    	return redirect_target;
   }
   
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Create path components to save the file
        final String path = request.getParameter("destination");
        final Part filePart = request.getPart("file");
        final String fileName = getFileName(filePart);

        OutputStream out = null;
        InputStream filecontent = null;
        final PrintWriter writer = response.getWriter();

        try {
            out = new FileOutputStream(new File(path + File.separator
                    + fileName));
            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];
            
            while(read != -1)
            	read = filecontent.read(bytes);

/*            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            writer.println("New file " + fileName + " created at " + path);
            LOGGER.log(Level.INFO, "File{0}being uploaded to {1}", 
                    new Object[]{fileName, path});
*/        } catch (FileNotFoundException fne) {
            writer.println("You either did not specify a file to upload or are "
                    + "trying to upload a file to a protected or nonexistent "
                    + "location.");
            writer.println("<br/> ERROR: " + fne.getMessage());

/*            LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}", 
                    new Object[]{fne.getMessage()});
*/        } finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
    
  	private boolean updateXml(PropsManager propsMgr)
  	{
  		boolean ret = true;
  		String xml = "";
  		XmlFactory xmlFactory = new XmlFactory();
  		String outputFilename = propsMgr.getActiveTemplate().getFilename();
  		
 		xml = xmlFactory.doNewXml(propsMgr, PropsManager.ObjType.TEMPLATE);
  		Utilities.write_file(path_to_user_dir + "templates" + pathMrkr + outputFilename, xml, true);
  		return ret;
  	}
    
}
