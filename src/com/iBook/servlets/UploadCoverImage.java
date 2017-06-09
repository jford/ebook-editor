package com.iBook.servlets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;

import javax.imageio.ImageIO;
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
public class UploadCoverImage extends HttpServlet 
{
    private static final long serialVersionUID = 7908187011456392847L;
    
    private boolean deleteCoverImage = false;
    private boolean uploadCoverImage = false;
    
    private PropsManager propsMgr = null;
    
    private String bookId = "";
    private String path_to_user_dir = "";
    private String pathMrkr = PropsManager.getPathMarker();
    private String manuscriptId = "";
    private String manuscriptName = "";
    private String redirect_target = "";
    private String userId = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException 
    {
    	/*
    	 * user has clicked upload cover image button on generateEpub.jsp
    	 */
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        
        getParams(request);
        if(uploadCoverImage)
        	redirect_target = processUpload(request);
        if(deleteCoverImage)
        	redirect_target = processDelete();
        String redirect = "<html>\n"
                + "    <body onload=\"document.location = \'" + redirect_target + "\'\" />\n"
                + "</html>";
        response.setContentLength(redirect.length());
        
        ServletOutputStream op = response.getOutputStream();
        op.write(redirect.getBytes(), 0, redirect.length());


    }

    @Override
    public String getServletInfo() 
    {
        return "Servlet that uploads image for cover art";
    }
    
    private String processDelete()
    {
    	File msDir = new File(path_to_user_dir + "manuscripts");
    	String[] filelist = msDir.list();
    	for(int count = 0; count < filelist.length; count++)
    	{
    		if(filelist[count].indexOf(manuscriptName + "_Cover") != -1)
    		{
    			File art = new File(path_to_user_dir + "manuscripts" + pathMrkr + filelist[count]);
    			art.delete();
    		}
    	}
    	return "/ibook/editor/verifyData.jsp?bookId=" + bookId;
    }
    
    protected String processUpload(HttpServletRequest request)
            throws ServletException, IOException 
    {
        // "coverImage" is parameter passed by upload image form
        final Part filePart = request.getPart("coverImage");
        
        // inFilename is file selected by user in file picker
        final String inFilename = getFileName(filePart);
        // need to know type of file---.jpg? .png?
        final String inFileExt = inFilename.substring(inFilename.lastIndexOf('.'));
        
//        final String outFilename = msName + inFileExt;
        
        // need to write the incoming file under a temporary name, so that 
        // the manuscripts directory can be cleaned of any previous cover art
        // files for this manuscript
        String tempOutFilename = "tempImage" + inFileExt;
        
        // get the selected file and write it to the manuscripts folder under the temp name
        String buffer = "";
        String text = "";
        
        InputStream filecontent = null;
        OutputStream out = null;

        try 
        {
        	out = new FileOutputStream(new File(path_to_user_dir + "manuscripts" + pathMrkr+ tempOutFilename));
            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while((read = filecontent.read(bytes)) != -1)
            {
            	out.write(bytes, 0, read);
            }
        }
        catch (FileNotFoundException fne) 
        {
        	System.out.println("error reading input file in UploadCoverArt servlet");
        	fne.printStackTrace();
        }
        finally 
        {
            if (out != null) 
            {
                out.close();
            }
            if(filecontent != null)
            {
            	filecontent.close();
            }
        }
        

        // clean the manuscripts directory of all prior cover art files for this manuscript
        File dir = new File(path_to_user_dir + "manuscripts");
		File file = null;
		
		String filename = "";
		String[] files = dir.list();

		for(int count = 0; count < files.length; count++)
		{
			file = new File(path_to_user_dir + "manuscripts" + pathMrkr + files[count]);
			filename = file.getName();
			if(filename.indexOf(manuscriptName + "_Cover") != -1)
				file.delete();
		}
        
		// replace the temp file name with the manuscript cover art filename
        File coverImage = new File(path_to_user_dir + "manuscripts" + pathMrkr + tempOutFilename);
        coverImage.renameTo(new File(path_to_user_dir + "manuscripts" + pathMrkr + manuscriptName + "_Cover" + inFileExt));

        return "/ibook/editor/verifyData.jsp?bookId=" + bookId;
    }
    
    private String getFileName(final Part part)
    {
    	String name = "";
    	final String partHeader = part.getHeader("content-disposition");
    	for(String content : part.getHeader("content-disposition").split(";"))
    	{
    		if(content.trim().startsWith("filename"))
    		{
    			name = content.substring(content.indexOf('=') + 1).trim().replace("\"",  "");
    		}
    	}
    	return name;
    }
    
    private void getParams(HttpServletRequest request)
    {
    	String key = "";
    	String value = "";
    	
    	deleteCoverImage = false;
    	uploadCoverImage = false;

    	manuscriptId = "";
    	userId = "";
    	
        // Get the value of all request parameters
        Enumeration<?> params = request.getParameterNames();
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
        	value = request.getParameter(key);
        	
        	if(key.equals("bookId"))
        		bookId = value;
        	
        	if(key.equals("manuscriptId"))
        		manuscriptId = value;
        	
        	if(key.equals("userId"))
        		userId = value;
        	if(key.equals("deleteCoverImage"))
        		deleteCoverImage = true;
        	
        	if(key.equals("uploadCoverImage"))
        		uploadCoverImage = true;
        }
        
        propsMgr = new PropsManager(userId);
        path_to_user_dir = propsMgr.getPathToUserDir();
        manuscriptName = ((Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId)).getMsName();
    }
}
