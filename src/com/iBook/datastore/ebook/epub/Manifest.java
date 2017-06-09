package com.iBook.datastore.ebook.epub;

import com.iBook.datastore.PropsManager;
import com.iBook.datastore.manuscripts.*;

import java.util.*;

public class Manifest
{
	private String coverArtFilename = "";
	private String manifest = "   <manifest>\n";
	private String pathMrkr = PropsManager.getPathMarker();
	
	// list of spine item IDs
	private Vector<String> spineItems = new Vector<String>();
	
	public Manifest(Manuscript manuscript, String userId)
	{
		String manifestItemId = "";
		PropsManager propsMgr = new PropsManager(userId);
		coverArtFilename = propsMgr.getCoverArtFilename(manuscript.getId());

		int num = manuscript.getFilebreaks().size();
		
		// cover
		if(coverArtFilename.length() > 0)
		{
			// cover art file
			manifestItemId = propsMgr.getCoverArtFilename(manuscript.getId());
			manifestItemId = manifestItemId.substring(manifestItemId.lastIndexOf("Cover"));
			manifest += makeItem(manifestItemId);

			// cover Xhtml file
			manifestItemId = "Cover.xhtml";
			manifest += makeItem(manifestItemId);
		}
		
		// toc.ncx
		manifestItemId = "ncx";
		manifest += makeItem(manifestItemId);
		
		// style sheet, will be .css
		manifestItemId = "epub_styles.css";
		manifest += makeItem(manifestItemId);
		
		// adobe layout sheet, will be page-template.xpgt
		manifestItemId = "page-template.xpgt";
		manifest += makeItem(manifestItemId);
		
		// textblock files; will be .xhtml
		for(int count = 1; count <= num; count++)
		{
			manifestItemId = "unit_" + new Integer(count).toString();
			spineItems.add(manifestItemId);
			manifest += makeItem(manifestItemId + ".xhtml");
		}

		// copyright, legal, title page, also .xhtml
		String[] supplementalFiles = { "copyright", "legal", "title", "preface" };
		for(int count = 0; count < supplementalFiles.length; count++)
		{
			manifestItemId = supplementalFiles[count];
			manifest += makeItem(manifestItemId + ".xhtml");
		}
		// Supplemental files are created in the Text object constructor, 
		// and added to the spine; the title file will be first item 
		// in the TOC play order, followed by copyright; followed by preface; legal will be last 
		// in the play order list
		spineItems.insertElementAt(supplementalFiles[2], 0); // title
		spineItems.insertElementAt(supplementalFiles[0], 1); // copyright
		spineItems.insertElementAt(supplementalFiles[3], 2); // preface
		spineItems.add(supplementalFiles[1]); // legal 
		
		// add cover.xhtml to spine, if there is cover art
		if(coverArtFilename.length() > 0)
		{
			spineItems.insertElementAt("Cover", 0);
		}
		
		manifest += "   </manifest>\n";
	}
	private String makeItem(String manifestItemId)
	{
		String manifestItemHref = "";
		String mediaType = "";
		if(manifestItemId.startsWith("Cover."))
		{
			String fileExt = manifestItemId.substring(manifestItemId.lastIndexOf('.'));
			manifestItemHref = "Images/" + manifestItemId;
			if(fileExt.equals(".gif"))
				mediaType = "image/gif";
			else if(fileExt.equals(".jpg") || fileExt.equals(".jpeg"))
				mediaType = "image/jpeg";
			else if(fileExt.equals(".png"))
				mediaType = "image/png";
			else if(fileExt.equals(".svg"))
				mediaType = "image/svg";
			else if(fileExt.equals(".tiff"))
				mediaType = "image/tiff";
		}
		
		if(manifestItemId.endsWith(".xhtml"))
		{
			manifestItemHref = "Text/" + manifestItemId;
			mediaType = "application/xhtml+xml";
		}
		else if(manifestItemId.compareTo("page-template.xpgt") == 0)
		{
			manifestItemHref = "Styles/page-template.xpgt";
			mediaType = "application/vnd.adobe-page-template+xml";
		}
		else if(manifestItemId.endsWith(".css"))
		{
			manifestItemHref = "Styles/" + manifestItemId;
			mediaType = "text/css";
		}
		else if(manifestItemId.compareTo("ncx") == 0)
		{
			manifestItemHref = "toc." + manifestItemId;
			mediaType = "application/x-dtbncx+xml";
		}
		return "      <item id=\"" + 
			                manifestItemId + 
			                "\" href=\"" + 
			                manifestItemHref + 
			               "\" media-type=\"" + 
			                mediaType + 
			                "\" />\n";
	}
	
	public String getManifest()
	{
		return manifest;
	}
	
	public Vector<String> getSpineItems()
	{
		return spineItems;
	}
	
}
