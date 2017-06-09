package com.iBook.servlets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
public class UploadBookSource extends HttpServlet 
{
    private static final long serialVersionUID = 7908187011456392847L;
    
    private String pathMrkr = PropsManager.getPathMarker();
    

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
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() 
    {
        return "Servlet that uploads template source file (ascii txt file)";
    }
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");

        final Part filePart = request.getPart("textSource");
        
        String buffer = "";
        String templateId = request.getParameter("templateId");
        String text = "";
        String userId = request.getRemoteUser();
        
        InputStream filecontent = null;

        try 
        {
            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while(read != -1)
            {
            	read = filecontent.read(bytes);
            	if(read != -1)
            	{
	            	for(int count = 0; count < read; count++)
	            		buffer += (char)bytes[count];
	            	text += buffer;
	            	buffer = "";
            	}
            }
        }
        catch (FileNotFoundException fne) 
        {
        	System.out.println("error reading input file in UploadBookSource servlet");
        	fne.printStackTrace();
        }
        finally 
        { 
            if (filecontent != null) 
            {
                filecontent.close();
            }
        }
        PropsManager propsMgr = new PropsManager(userId);
        String path_to_user_dir = propsMgr.getPathToUserDir();
        TemplateProfile profile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
        String outputFilename = profile.getFilename();
        
        // clear all existing textblocks
        profile.deleteTextBlock(-1);
        
        int idx = 0;
        
        // if incoming text contains \r\n, use that as the lineBreak; if not, the file
        // was either created on a Unix system or processed by the iBookTxtPrep utility, which
        // converts all instances of \r\n to \n
        String lineBreak = text.indexOf("\r\n") != -1 ? "\r\n" : "\n"; 
        while((idx = text.indexOf(lineBreak)) != -1)
        {
        	profile.addTextblock(text.substring(0, idx));
        	text = text.substring(idx + lineBreak.length());
        }
        
        propsMgr.setActiveTemplate(profile);
        XmlFactory xmlFactory = new XmlFactory();
        text = xmlFactory.doNewXml(propsMgr, PropsManager.ObjType.TEMPLATE);
        Utilities.write_file(path_to_user_dir + "templates" + pathMrkr + outputFilename, text, true);

        String redirect_target = "/iBook/editor/editTemplate.jsp?templateId=" + templateId;
        String redirect = "<html>\n"
                + "    <body onload=\"document.location = \'" + redirect_target + "\'\" />\n"
                + "</html>";
            response.setContentLength(redirect.length());
            
            ServletOutputStream op = response.getOutputStream();
            op.write(redirect.getBytes(), 0, redirect.length());
    }
}
