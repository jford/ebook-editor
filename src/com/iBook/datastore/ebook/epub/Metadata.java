package com.iBook.datastore.ebook.epub;

import com.iBook.datastore.PropsManager;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.*;

import java.util.*;

public class Metadata
{
	private Manuscript manuscript = null;

	private String dcCreator = "      <dc:creator opf:role=\"aut\"></dc:creator>\n";
	private String dcIdentifier = "";
	private String dcLanguage = "      <dc:language>en-US</dc:language>\n";
	private String dcPublisher = "      <dc:publisher>iBook</dc:publisher>\n";
	private String dcRights = "      <dc:rights>Public Domain</dc:rights>\n";
	private String dcTitle = "";
	private String metaData = "   <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\" " +
			                               "xmlns:opf=\"http://www.idpf.org/2007/opf\">\n";
	private String uid = "";
	private String userId = "";
	private String coverArtFilename = "";
	
	public Metadata(Manuscript manuscript, String userId)
	{
		this.manuscript = manuscript;
		this.userId = userId;
		
		PropsManager propsMgr = new PropsManager(userId);
		coverArtFilename = propsMgr.getCoverArtFilename(manuscript.getId());
		
		dcTitle = "      <dc:title>" + manuscript.getIBookTitle() + "</dc:title>\n";
		long time = new Date().getTime();
		uid = manuscript.getMsName() + 
                "_" + 
                time;
		dcIdentifier = "      <dc:identifier id=\"BookID\" opf:scheme=\"UUID\">" + 
					   uid +
		                "</dc:identifier>\n";
	}
	public String getMetadata()
	{
		return metaData + 
				dcTitle + 
				dcLanguage + 
				dcRights + 
				getDcCreator() + 
				dcIdentifier + 
				dcPublisher + 
				getCover() +
				"   </metadata>\n";
	}
	private String getCover()
	{
		String coverItem = "";
		if(coverArtFilename.length() > 0)
			coverItem = "      <meta name=\"cover\" content=\"" + coverArtFilename.substring(coverArtFilename.lastIndexOf("Cover")) + "\" />\n";
		return coverItem;
	}
	private String getDcCreator()
	{
		int idx = 0;
		
		StringBuffer creator = new StringBuffer(dcCreator);
		String iBookSystemName = PropsManager.getIBookSystemName();
		String iBookAuthor = manuscript.getIBookAuthor();
		
		String creatorName = iBookSystemName + " for " + iBookAuthor;
		idx = creator.indexOf("</");
		creator.insert(idx, creatorName);
		return creator.toString();
	}
	
	public String getUid()
	{
		return uid;
	}
}
