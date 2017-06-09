package com.iBook.datastore.ebook.epub;

import com.iBook.datastore.PropsManager;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.zip.*;

import static java.nio.file.StandardCopyOption.*;

public class EPubObj
{
	private boolean stopForDebugging = false;
	
	// web app, on tomcat, can be on a Windows or a 'nix system;
	// use the system-defined path marker when navigating the file system
	// (but only use / when packing the zip file---epub mandate)
	private static String outputFilename = "";
	private static String pathMrkr = PropsManager.getPathMarker();
	private static String path_to_resources = PropsManager.getPathToResources();
	private static String path_to_user_dir = PropsManager.getPathToUserDir();
	private static String path_to_epub = path_to_user_dir + "epub" + pathMrkr;
	private static String userId = "";
	
	public EPubObj(Manuscript manuscript, String userId)
	{
		this.userId = userId;
		PropsManager propsMgr = new PropsManager(userId);
		String coverArtFilename = propsMgr.getCoverArtFilename(manuscript.getId());
		
		// META-INF and Style directories don't change from book to book,
		// so they are not built on demand. Instead, they should always 
		// reside in the WebContent/resources/epub folder. Do not delete
		// those two folders when purging the epub directory after a 
		// book has been published. The MetaInf and Style classes are not
		// currently used, thought they remain in the project should they
		// be needed in the future.

		// create contents of the epub's OEBPS directory
		Oebps oebps = new Oebps(manuscript, userId);

		// get content.opf file contents
		String contentOpf = "content.opf";
		String contentOpfText = oebps.getContentOpf();

		// get toc.ncx file contents
		String tocNcx = "toc.ncx";
		String tocNcxText = oebps.getTocNcx();

		// ...and populate the OEBPS/Text directory (note the dependency 
		// on oebps.getXhtmlTitles()---these titles are generated as part of 
		// the toc.ncx generating process, which must have been run prior to 
		// instantiating the Text object)
		Text epubText = new Text(manuscript, oebps.getXhtmlTitles(), userId);

		// write them to staging directory
		Utilities.write_file(path_to_epub + "OEBPS" + pathMrkr + contentOpf, contentOpfText, true);
		Utilities.write_file(path_to_epub + "OEBPS" + pathMrkr + tocNcx, tocNcxText, true);
		
		// if cover art, copy it to the staging directory
		if(coverArtFilename.length() > 0)
		{
			String fileExt = coverArtFilename.substring(coverArtFilename.lastIndexOf("."));
			File imageDir = new File(path_to_epub + "OEBPS" + pathMrkr + "Images");
			if(!imageDir.exists())
				imageDir.mkdir();
			File coverArtSource = new File(path_to_user_dir + "manuscripts" + pathMrkr + coverArtFilename);
			File coverArt  = new File(path_to_epub + "OEBPS" + pathMrkr + "Images" + pathMrkr + "Cover" + coverArtFilename.substring(coverArtFilename.lastIndexOf(".")));

			copyCoverArt(coverArtSource, coverArt);
		}
		
		// put it all together and generate the .epub file
		outputFilename = Utilities.normalizeName(manuscript.getMsName()) + ".epub"; 
		createEpub();
	}
	
    private void copyCoverArt(File source, File dest)
    {	
    	InputStream inStream = null;
    	OutputStream outStream = null;
    	try
    	{
    	    inStream = new FileInputStream(source);
    	    outStream = new FileOutputStream(dest);
 
    	    byte[] buffer = new byte[1024];
 
    	    int length;
    	    while ((length = inStream.read(buffer)) > 0)
    	    {
    	    	outStream.write(buffer, 0, length);
    	    }
 
    	    if (inStream != null)
    	    	inStream.close();
    	    
    	    if (outStream != null)
    	    	outStream.close();
 
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    }
 
	public static String getPathToResources()
	{
		return path_to_resources;
	}
	
	public static String getPathToUserDir()
	{
		return path_to_user_dir;
	}
	
	public static String getPathToEpub()
	{
		return path_to_epub;
	}
	
	public static String getOutputFilename()
	{
		return outputFilename;
	}
	
	private void createEpub()
	{
		File dir_to_epub = new File(path_to_epub);
		FileOutputStream fileOut = null;
		try
		{
			// create the .epub container
			fileOut = new FileOutputStream(path_to_user_dir + outputFilename);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			// file should be created after exception is thrown, so what's to catch? 
		}
		
		// attach file to a zip output stream
		ZipOutputStream zipOut = new ZipOutputStream(fileOut);
		try
		{
			// ...and write the mimetype file to it
			writeMimeType(zipOut);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		// add the files that should now exist in the 
		// staging directory (created when the Oebps object was instantiated, 
		// in the EpubObj constructor
		addDirsToEpub(dir_to_epub, zipOut);

		// close the streams
		try
		{
			zipOut.close();
			fileOut.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		// clean up when done; delete all generated files except the .epub
		emptyEpubDirs(dir_to_epub);
		
	}
	
	
	private void emptyEpubDirs(File dir)
	{
		File file = null;
		
		String filename = "";
		String path_to_dir = "";
		try
		{
			path_to_dir = dir.getCanonicalPath() + pathMrkr;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		String[] files = dir.list();

		for(int count = 0; count < files.length; count++)
		{
			file = new File(path_to_dir + files[count]);
			filename = file.getName();
			if(filename.compareTo("META-INF") == 0 ||
			   filename.compareTo("Styles") == 0)
				continue;
			if(file.isDirectory())
				emptyEpubDirs(file);
			else
				file.delete();
		}
	}
	
	private void addDirsToEpub(File dir, ZipOutputStream zipOut)
	{
		// starting dir is path_to_epub
		
		String[] files = dir.list();
		String dirname = dir.getName();
		String path_to_dir = "";
		String zipDirname = "";
		try
		{
			// use pathMrkr when navigating the OS file system (but not when packing the Zip stream)
			path_to_dir = dir.getCanonicalPath() + pathMrkr;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		String filename = "";
		File file = null;
		ZipEntry zipEntry = null;
		for(int count = 0; count < files.length; count++)
		{
			file = new File(path_to_dir + files[count]);
			filename = file.getName();
			// already did mimetype, so skip it
			if(filename.compareTo("mimetype") == 0)
				continue;
			FileInputStream fileIn = null;
			if(file.isDirectory())
				addDirsToEpub(file, zipOut);
			else
			{
				try
				{
					fileIn = new FileInputStream(path_to_dir + files[count]);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				if(dirname.indexOf("Text") != -1 || 
				   dirname.indexOf("Styles") != -1 ||
				   dirname.indexOf("Images") != -1)
					zipDirname = "OEBPS" + "/" + dirname;
				else
					zipDirname = dirname;
				
				// epub explicitly requires forward slashes as file separators in paths; 
				// use / instead of \\ when packing the Zip (but get pathMrkr 
				// from the PropsManager when navigating the OS file system)
				zipEntry = new ZipEntry(zipDirname + "/" + filename);
				zipEntry.setMethod(ZipOutputStream.DEFLATED);
				
				try
				{
					zipOut.putNextEntry(zipEntry);
					
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				byte[] buff = new byte[1024];
				int length = 0;
				try
				{
					while((length = fileIn.read(buff)) >= 0)
					{
						zipOut.write(buff,0,length);
					}
					zipOut.closeEntry();
					fileIn.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	private void writeMimeType(ZipOutputStream zip) throws IOException {
	    byte[] content = "application/epub+zip".getBytes("UTF-8");
	    ZipEntry entry = new ZipEntry("mimetype");
	    entry.setMethod(ZipEntry.STORED);
	    entry.setSize(20);
	    entry.setCompressedSize(20);
	    entry.setCrc(0x2CAB616F); // pre-computed
	    zip.putNextEntry(entry);
	    zip.write(content);
	    zip.closeEntry();
	}
}
