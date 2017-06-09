package com.iBook.servlets;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;
import java.util.*;

import com.iBook.datastore.*;
import com.iBook.datastore.PropsManager.ObjType;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.books.*;
import com.iBook.datastore.locations.*;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.Utilities;

public class ManageBook extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    // parameter flags
    private boolean clearAliasText;
    private boolean clearAttributeText;
    private boolean mapAliases;
    private boolean mapAttributes;
    private boolean setAliasText;
    private boolean setAttributeText;
	
    // iBook resources
    private String path_to_resources = PropsManager.getPathToResources();
    private String path_to_user_dir = PropsManager.getPathToUserDir();
    private String pathMrkr = PropsManager.getPathMarker();
    private PropsManager propsMgr = null;

    // parameter values
    private String bookId;
    private String tObjId;
    private String redirect_target;
    private String userId;
    private String substituteText;
    private String tObjAlias;
    private String tObjAttribute;
  	
    // parameter xfr values
    private String key;
    private String value;

    public ManageBook()
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
        
        if(clearAliasText || setAliasText)
       		redirect_target = doAliasMapping();

        if(clearAttributeText || setAttributeText)
        	redirect_target = doAttributeMapping();
    
        if(mapAliases)
        	redirect_target = "/iBook/editor/mapAliases.jsp?bookId=" + bookId + "&tObjId=" + tObjId;
        
         if(mapAttributes)
        	redirect_target = "/iBook/editor/mapAttributes.jsp?bookId=" + bookId + "&tObjId=" + tObjId;
        
  		String xml = "";
  		XmlFactory xmlFactory = new XmlFactory();
		xml = xmlFactory.doNewXml(propsMgr, PropsManager.ObjType.BOOK);
  		Utilities.write_file(path_to_user_dir + "bookList.xml", xml, true);
        
        String redirect = "<html>\n"
            + "    <body onload=\"document.location = \'" + redirect_target + "\'\" />\n"
            + "</html>";
        response.setContentLength(redirect.length());
        op.write(redirect.getBytes(), 0, redirect.length());
    }
    
    
    private String doAliasMapping()
    {
        if(clearAliasText)
        	substituteText = "";
        BookProfile bookProfile = propsMgr.getBookProfile(bookId);
       	bookProfile.addTAliasSub(tObjId, tObjAlias, substituteText);
        redirect_target = "/iBook/editor/mapAliases.jsp?bookId=" + bookId + "&tObjId=" + tObjId;    	
    	return redirect_target;
    }
    
    private String doAttributeMapping()
    {
        if(clearAttributeText)
        	substituteText = "";
        BookProfile bookProfile = propsMgr.getBookProfile(bookId);
       	bookProfile.addTAttributeSub(tObjId, tObjAttribute, substituteText);
        redirect_target = "/iBook/editor/mapAttributes.jsp?bookId=" + bookId + "&tObjId=" + tObjId;    	
    	return redirect_target;
    }
    
    private void getParams(HttpServletRequest request, HttpServletResponse response)
    {
    	// reset all parameters to default values
        clearAliasText = false;
        clearAttributeText = false;
        mapAliases = false;
        mapAttributes = false;
        setAliasText = false;
        setAttributeText = false;
    	
        // parameter values
        bookId = "";
        tObjId = "";
        redirect_target = "/iBook/editor/index.jsp";
        userId = "";
        substituteText = "";
        tObjAlias = "";
        tObjAttribute = "";
      	
        // Get current values for all request parameters
        Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
    		value = request.getParameter(key);
    		
//    		System.out.println(key + " = " + value);

          	if(key.compareTo("bookId") == 0)
	        	bookId = value;
	        
          	if(key.compareTo("clearAliasTextSubmit") == 0)
          		clearAliasText = true;

          	if(key.compareTo("clearAttributeTextSubmit") == 0)
          		clearAttributeText = true;

          	if(key.compareTo("mapAliasesSubmit") == 0)
          		mapAliases = true;
          	
          	if(key.compareTo("mapAttributesSubmit") == 0)
          		mapAttributes = true;
          	
          	if(key.compareTo("setAliasTextSubmit") == 0)
          		setAliasText = true;
          	
          	if(key.compareTo("setAttributeTextSubmit") == 0)
          		setAttributeText = true;
          	
	        if(key.compareTo("substituteText") == 0)
	        	substituteText = value;
	        
	        if(key.compareTo("tObjAlias") == 0)
	        	tObjAlias = value;
	        
	        if(key.compareTo("tObjAttribute") == 0)
	        	tObjAttribute = value;
	        
          	if(key.compareTo("tObjId") == 0)
          		tObjId = value;
          	
          	if(key.compareTo("userId") == 0)
          		userId = value;

        }
    }
}
