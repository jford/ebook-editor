package com.iBook.servlets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.iBook.datastore.PropsManager;
import com.iBook.datastore.XmlFactory;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.*;

@MultipartConfig
public class UpdateTextblock extends HttpServlet 
{
    private static final long serialVersionUID = 7908187011456392847L;
    
    private PropsManager propsMgr = null;
    
    private String path_to_resources = PropsManager.getPathToResources();
    private String path_to_user_dir = PropsManager.getPathToUserDir();
    private String pathMrkr = PropsManager.getPathMarker();

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        doPost(request, response);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException 
    {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
    	boolean goToBlockNum = false;
    	boolean searchForText = false;
    	boolean updateAndAdvanceTextblock = false;
    	boolean updateAndGoBackOne =false;
    	boolean updateTextblock = false;
    	
        int numTextblocks = 0;
        int curBlockNum = 0;

        ServletOutputStream op = response.getOutputStream();
        
        String blockNum = "";
        String key = "";
        String redirect_target = "";
        String manuscriptId = "";
        String newTextblock = "";
        String redirect = "";
        String searchText = "";
        String templateId = "";
        String textblockId = "";
        String userId = "";
        String value = "";
        
        Manuscript manuscript = null;
        TemplateProfile templateProfile = null;

    	Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
        	if(key.compareTo("blockNum") == 0)
        		blockNum = value.trim();
        	
        	if(key.compareTo("goToBlockNum") == 0 || key.compareTo("updateAndGoToBlockNum") == 0)
        	{
        		// user specified a textblock by number
        		goToBlockNum = true;
        		if(key.compareTo("updateAndGoToBlockNum") == 0)
        			updateTextblock = true;
        	}
        	
        	if(key.compareTo("manuscriptId") == 0)
        	{
        		manuscriptId = value;
        	}
        	
        	if(key.compareTo("searchText") == 0)
        	{
        		searchText = value;
        		searchForText = true;
        	}
        	
        	if(key.compareTo("textblock") == 0)
        		newTextblock = value;
        	
        	if(key.compareTo("templateId") == 0)
        	{
        		templateId = value;
        	}

        	if(key.compareTo("textblockId") == 0)
        		textblockId = value;
        	
        	if(key.compareTo("updateAndAdvanceTextblock") == 0)
        	{
        		// if update and advance button clicked, both flags need to be true
        		updateTextblock = true;
        		updateAndAdvanceTextblock = true;
        	}
        	
        	if(key.compareTo("updateAndGoBackOne") == 0)
        	{
        		updateTextblock = true;
        		updateAndGoBackOne = true;
        	}
        	
        	if(key.compareTo("updateTextblock") == 0)
        		// user did not say advance, so only one flag is true
        		updateTextblock = true;
        	
        	if(key.compareTo("userId") == 0)
        		userId = value;
        }
        
        if(blockNum.length() == 0)
        {
        	curBlockNum = textblockId.length() > 0 ? new Integer(textblockId.substring(textblockId.lastIndexOf("_") + 1)) : 1;
        }

        propsMgr =  new PropsManager(userId);
        if(manuscriptId.length() > 0)
        {
			manuscript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId);
			numTextblocks = manuscript.getNumTextblocks();
        }
        if(templateId.length() > 0)
        {
    		templateProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
    		numTextblocks = templateProfile.getNumTextBlocks();
        }

        if(updateTextblock)
        {
        	// purge any unnecessary characters added by the browser
        	newTextblock = normalizeNewTextblock(newTextblock);

        	// update the textblock in the profile, then return to the browser with same textblock displayed,
        	// in case there are more edits to be made
        	updateTextblock(templateId, textblockId, newTextblock, manuscriptId);

        	// if coming from template editor, no manuscript ID param
        	if(manuscriptId.length() == 0)
        		redirect_target = "/iBook/editor/enableSubstitutions.jsp?templateId=" + templateId + "&textblockId=" + textblockId;
        	else
        		redirect_target="/iBook/editor/finalProof.jsp?manuscriptId=" + manuscriptId + "&textblockId=" + textblockId;

        	if(updateAndAdvanceTextblock || updateAndGoBackOne)
        	{
        		// user said update and advance, so return to the browser with the next block of text after the current textblockId
        		String newTextblockId = textblockId.substring(0, textblockId.lastIndexOf("_") + 1);
        		int num = Integer.parseInt(textblockId.substring(textblockId.lastIndexOf("_") + 1));
        		
        		// if num less than total number of text blocks, increment by one, otherwise go back to beginning
        		if(updateAndAdvanceTextblock)
        			num = num < numTextblocks ? num + 1 : 1;
        		
        		// if num higher than 1, go back one, otherwise go to last textblock
        		else if(updateAndGoBackOne)
        			num = num > 1 ? num - 1 : numTextblocks;
        			
        		newTextblockId += new Integer(num).toString();

        		// if coming from template editor, no manuscript ID param
            	if(manuscriptId.length() == 0)
        			redirect_target = "/iBook/editor/enableSubstitutions.jsp?templateId=" + templateId + "&textblockId=" + newTextblockId;
        		else
        			redirect_target = "/iBook/editor/finalProof.jsp?manuscriptId=" + manuscriptId +"&textblockId=" + newTextblockId;
        	}
        }
        if(goToBlockNum)
        {
        	// user specified a text block by number
        	String newTextblockId = textblockId.substring(0, textblockId.lastIndexOf("_") + 1);
        	newTextblockId += blockNum;
        	
        	// if coming from template editor, no manuscript ID param
        	if(manuscriptId.length() == 0)
        		redirect_target = "/iBook/editor/enableSubstitutions.jsp?&templateId=" + templateId + "&textblockId=" + newTextblockId;
        	else
        		redirect_target = "/iBook/editor/finalProof.jsp?manuscriptId=" + manuscriptId + "&textblockId=" + newTextblockId;
        }
        
        redirect = "<html>\n"
                + "    <body onload=\"document.location = \'" + redirect_target + "\'\" />\n"
                + "</html>";
        response.setContentLength(redirect.length());
        op.write(redirect.getBytes(), 0, redirect.length());
    }
    
    private String normalizeNewTextblock(String newText)
    {
    	// first, remove any \r chars (Firefox inserts both forms of line 
    	// endings---\n and \r\n---when text is updated in the text input form on finalProof.jsp
    	newText = Utilities.replaceChars(newText, "\r", "", "all");
    	
    	// Next, keep the new lines at 2 for each paragraph marker. 
    	// Firefox doubles every new line, every time the text pane in the 
    	// Final Proof Editor is updated. Need to search continually for \n\n\n in  
    	// newText and replace with \n\n. (The Text object's normalizeForHtml(String) 
    	// will look for \n\n and replace each with <p></p> when processing text for output. ) 
    	StringBuffer textBuff = new StringBuffer(newText);

    	int idx = 0;
    	
    	String oldLineMrkr = "\n\n\n";
    	String newLineMrkr = "\n\n";
    	while((idx = textBuff.indexOf(oldLineMrkr)) != -1)
    	{
    		textBuff.replace(idx, idx + oldLineMrkr.length(), newLineMrkr);
    	}
    	return textBuff.toString();
    }
    
    private boolean updateTextblock(String objId, String textblockId, String newTextblock, String manuscriptId)
    {
    	boolean ret = false;

    	// if coming from template editor, no manuscript ID param
    	if(manuscriptId.length() == 0)
    	{
	    	TemplateProfile tProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, objId);
	    	ret = tProfile.updateTextblock(textblockId, newTextblock);
	    	propsMgr.setActiveTemplate(tProfile);
	    	updateXml(PropsManager.ObjType.TEMPLATE, propsMgr);
    	}
    	else
    	{
	    	Manuscript mScript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId);
	    	ret = mScript.updateTextblock(textblockId, newTextblock);
	    	propsMgr.setActiveManuscript(mScript);
	    	updateXml(PropsManager.ObjType.MANUSCRIPT, propsMgr);
    	}

    	return ret;
    }

    @Override
    public String getServletInfo() 
    {
        return "Servlet that enables substitutions in template textblocks";
    }
    
  	private boolean updateXml(PropsManager.ObjType type, PropsManager propsMgr)
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
  		Utilities.write_file(path_to_user_dir + outputFilename, xml, true);
  		return ret;
  	}
}
