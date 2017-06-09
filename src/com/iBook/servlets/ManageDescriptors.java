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

public class ManageDescriptors extends HttpServlet
{
	boolean stopForDebugging = false;
	
    private static final long serialVersionUID = 1L;
    private String path_to_user_dir = PropsManager.getPathToUserDir();
    private String pathMrkr = PropsManager.getPathMarker();
    private PropsManager propsMgr = null;
    BookProfile bookProfile = null;
    TemplateProfile templateProfile = null;
    Manuscript manuscript = null;
    Vector<CharacterProfile> tChars = null;
    Vector<LocationProfile> tLocs = null;
    Vector<GeolocaleProfile> tGeolocs = null;
    Vector<String> tTextblocks = null;
    int textblocksIdx = 0;
    
    public ManageDescriptors()
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
        ServletOutputStream op = null; 
        
        // Get the value of all request parameters
        Enumeration<?> params = request.getParameterNames();
        
        boolean setDescriptorText = false;
        boolean useOriginalDescriptorText = false;
        
        int idx = 0;
        int end = 0;

        Iterator<String[]> descriptorsI = null;
        
        Manuscript manuscript = null;
        
        String[] descriptor = {"","","",""};
        String descriptorTag = "descriptor>";
        String descriptorTagClose = "</descriptor>";
        String descriptorText = "";
        String descriptorTextEdit = "";
        String key = "";
        String redirect = "";
        String redirect_target = "";
        String manuscriptId = "";
        String templateId = "";
        String textblock = "";
        String textblockNum = "";
        String userId = "";
        String value = "";
        
        Vector<String[]> descriptors = null;
        
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
    		value = request.getParameter(key);
        	
        	if(key.compareTo("descriptorText") == 0)
        		descriptorText = Utilities.decodeHtmlTags(value);
        	
        	if(key.compareTo("descriptorTextEdit") == 0)
        		descriptorTextEdit = value;
        	
        	if(key.compareTo("manuscriptId") == 0)
        	{
        		manuscriptId = value;
        	}
        	
        	if(key.compareTo("setDescriptorText") == 0)
        	{
        		setDescriptorText = true;
        	}

        	if(key.compareTo("templateId") == 0)
        		templateId = value;

        	if(key.compareTo("textblockNum") == 0)
        		textblockNum = value;

            if(key.compareTo("useOriginalDescriptorText") == 0)
            	useOriginalDescriptorText = true;
            
            if(key.compareTo("userId") == 0)
            	userId = value;
        }
        propsMgr =  new PropsManager(userId);
		manuscript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId);
		descriptors = manuscript.getDescriptors();
		descriptorsI = descriptors.iterator();
        
       	redirect_target = "/iBook/editor/resolveDescriptors.jsp?templateId=" + templateId + "&manuscriptId="+  manuscriptId;

       	if(setDescriptorText || useOriginalDescriptorText)
        {
        	while(descriptorsI.hasNext())
        	{
        		descriptor = descriptorsI.next();
        		if(descriptor[1].compareTo(textblockNum) == 0 &&
        		   descriptor[2].compareTo(descriptorText) == 0)
        		{
    				// descriptor or gdescriptor?
    				if(descriptor[2].indexOf("gdescriptor>") != -1)
    				{
    					descriptorTag = "gdescriptor>";
    					descriptorTagClose = "</gdescriptor>";
    				}
        			if(setDescriptorText)
        			{
        				// change text and remove tags
        				idx = descriptor[2].indexOf(descriptorTagClose);
        				
        				// if original text enclosed bys the tags ends in a space, make sure 
        				// new text also ends in a space
        				if(descriptor[2].charAt(idx - 1) == 32 && !descriptorTextEdit.endsWith(" "))
        					descriptorTextEdit += " ";
        				descriptor[3] = descriptorTextEdit;
        			}
        			else
        			{
        				// keep text and remove tags
        				idx = descriptor[2].indexOf(descriptorTag);
        				end = descriptor[2].indexOf(descriptorTagClose, idx);
        				descriptorText = descriptor[2].substring(idx + descriptorTag.length(), end);
        				descriptor[3] = descriptorText;
        			}
        			break;
        		}
        	}
        	textblock = updateTextblock(descriptor, manuscript.getTextblock("textblock_" + textblockNum));
        	manuscript.setTextblock("textblock_" + descriptor[1], textblock);
        	manuscript.updateDescriptor(descriptor);
        	textblockNum = descriptor[1];
        	propsMgr.setActiveManuscript(manuscript);
        	updateXml();
        	if(manuscript.getNumDescriptorsUnresolved() > 0)
        		redirect_target = "/iBook/editor/resolveDescriptors.jsp?manuscriptId=" + manuscriptId + "&templateId=" + templateId;
        	else
        		redirect_target = "/iBook/editor/finalProof.jsp?manuscriptId=" + manuscriptId + "&textblockId=" + manuscript.getMsName() + "_1";
        }
        
        redirect = "<html>\n" +
                   "    <body onload=\"document.location = \'" + redirect_target + "\'\" />\n" +
                   "</html>";
        response.setContentLength(redirect.length());
        op = response.getOutputStream();
        op.write(redirect.getBytes(), 0, redirect.length());
 	}
    
    private String updateTextblock(String[] descriptor, String textblock)
    { 
        int idx = 0;
        int end = 0;
        
        String text = "";
        
    	StringBuffer textBuff = new StringBuffer(textblock);
    	// find descriptor text in the textblock
    	idx = textBuff.indexOf(descriptor[2]);
    	end = idx + descriptor[2].length();
    	// substitute the text (as long as idx and end are valid for the stringBuff)
    	if(idx != -1 && end < textBuff.length())
    		text = textBuff.replace(idx,  end,  descriptor[3]).toString();
    	
    	return text;
    }
    
  	private boolean updateXml()
  	{
  		boolean ret = true;
  		String xml = "";
  		XmlFactory xmlFactory = new XmlFactory();
  		String outputFilename = "";
  		
		outputFilename = "manuscripts" + pathMrkr + propsMgr.getActiveManuscript().getMsName() + ".xml";
		xml = xmlFactory.doNewXml(propsMgr, PropsManager.ObjType.MANUSCRIPT);
  		Utilities.write_file(path_to_user_dir + outputFilename, xml, true);
  		return ret;
  	}
}