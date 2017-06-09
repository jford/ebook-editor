package com.iBook.datastore.ebook;

import com.iBook.datastore.ebook.pdf.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import com.iBook.datastore.*;
import com.iBook.datastore.books.*;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.*;

public class BoilerplateFactory
{
	public static enum PubType {EPUB, PDF};
	public static enum PdfPage {COPYRIGHT, PREFACE, TOC, LEGALNOTICE};
	
	public static String fileHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"   <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" " +
            "\"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
			"   <html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
			"      <head>\n" +
			"         <title></title>\n" +
			"         <link rel=\"stylesheet\" href=\"../Styles/epub_styles.css\" type=\"text/css\" />\n" +
			"         <link rel=\"stylesheet\" type=\"application/vnd.adobe-page-template+xml\" " +
			      "href=\"../Styles/page-template.xpgt\" />\n" +
			"      </head>\n" +
			"      <body>\n" +
			"      <div>\n"; 
	
	public static String coverFileHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"   <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" " +
            "\"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
			"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
			"   <head>\n" +
			"      <title>Cover</title>\n" +
			"      <style type=\"text/css\">\n" +
			"          div { text-align: center }\n" +
			"          img { max-width: 100%; } \n" +
			"       </style>\n" +
			"   </head>\n" +
			"   <body>\n" +
			"      <div>\n";
	
	public static String fileTail = "   </div>\n" +
							  "   </body>\n" +
							  "</html>";
	
	public static String coverFileTail = "      </div> \n" +  
			                       "   </body>\n" +
			  					   "</html>";

	private String iBookSystemName = PropsManager.getIBookSystemName();
	private String userId = "";
	private BoilerplateParts textStore = null;
	
	public BoilerplateFactory(Manuscript manuscript, String userId)
	{
		textStore = new BoilerplateParts(manuscript);
		this.userId = userId;
	}
	
	public String doTitle(PubType pubType)
	{
		String titleText = "";
		
		String baseText = textStore.getTitleText();
		
		switch(pubType)
		{
		case EPUB:
			// add html paragraph end/begin tags to all double line returns 
			baseText = Utilities.replaceChars(baseText, "\n\n", "\n</p>\n<p>\n", "all");
			// except the first one, which gets a div tag instead of close paragraph tag
			baseText = Utilities.replaceChars(baseText, "\n</p>\n<p>\n", "\n<div class=\"titleblock\">\n<p>\n", "first");
			
			titleText = getFileHeader("Title");
			titleText += baseText;
			titleText += fileTail;
			break;
		case PDF:
			default:
				// title text as lines of text separated by \n\n
				titleText = baseText;
				break;
		}
		return titleText;
	}

	public String doCopyright(PubType pubType)
	{
		String copyrightText = "";
		
		// get the raw text
		String baseText = textStore.getCopyrightText();
		
		// format it for consumption by epub or pdf generator, as needed
		switch(pubType)
		{
		case EPUB:
			// add html paragraph end/begin tags to all double line returns 
			baseText = Utilities.replaceChars(baseText, "\n\n", "\n</p>\n<p>\n", "all");
			// except the first one, which gets a div tag instead of close paragraph tag
			baseText = Utilities.replaceChars(baseText, "\n</p>\n<p>\n", "\n<div class=\"copyrightblock\">\n<p>\n", "first");
			
			copyrightText = getFileHeader("Copyright");
			copyrightText += baseText;
			copyrightText += fileTail;
			break;
		case PDF:
			// copyright text as lines of text separated by \n\n
			copyrightText = baseText;
			break;
		}
		return copyrightText;
	}
	
	public PageSpecs getPageSpecs(PdfPage page)
	{
		PageSpecs pageSpecs = new PageSpecs();
		pageSpecs.setFontsize(8);
		pageSpecs.setLineLeading(9);
		pageSpecs.setLeftMargin(100);
		pageSpecs.setRightMargin(120);
		
		switch(page)
		{
		case COPYRIGHT:
			break;
		case PREFACE:
			break;
		case TOC:
			break;
		case LEGALNOTICE:
			break;
		}
		return pageSpecs;
	}
	
	public String doLegal(PubType pubType)
	{
		String legalText = "";
		String baseText = textStore.getLegalText();
		
		switch(pubType)
		{
		case EPUB:
			// add html paragraph end/begin tags to all double line returns 
			baseText = Utilities.replaceChars(baseText, "\n\n", "\n</p>\n<p>\n", "all");
			// except the first one, which gets a div tag instead of close paragraph tag
			baseText = Utilities.replaceChars(baseText, "\n</p>\n<p>\n", "\n<div class=\"legalnotice\">\n<p>\n", "first");
			
			legalText = getFileHeader("Legal Disclaimer");
			legalText += baseText;
			legalText += fileTail;
			break;
		case PDF:
			legalText = baseText;
			break;
		}
		
		return legalText;
	}
	
	public String doPreface(PubType pubType)
	{
		String baseText = textStore.getPrefaceText();
		String prefaceText = "";
		
		switch(pubType)
		{
		case EPUB:
			// add html paragraph end/begin tags to all double line returns 
			baseText = Utilities.replaceChars(baseText, "\n\n", "\n</p>\n<p>\n", "all");
			// except the first one, which gets a div tag instead of close paragraph tag
			baseText = Utilities.replaceChars(baseText, "\n</p>\n<p>\n", "\n<div class=\"preface\">\n<p>\n", "first");
			
			prefaceText = getFileHeader("Preface");
			prefaceText += baseText;
			prefaceText += fileTail;
			break;
		case PDF:
			prefaceText = baseText;
			break;
		}
		
		return prefaceText;
	}
	
	private String getFileHeader(String title)
	{
		// title is the text to be inserted into the .xhtml's <title>...</title> tag
		int idx = 0;
		String titleTag = "<title>";
		StringBuffer fileHeaderBuff = new StringBuffer(fileHeader);

		// file needs a title tag...
		idx = fileHeaderBuff.indexOf(titleTag);
		fileHeaderBuff.insert(idx + titleTag.length(), title);

		return fileHeaderBuff.toString();
	}
	
	public class BoilerplateParts
	{
		Manuscript manuscript = null;
		public BoilerplateParts(Manuscript manuscript)
		{
			this.manuscript = manuscript;
		}
		
		public String getTitleText()
		{
			String baseText = 
					"<h1>" + manuscript.getIBookTitle() + "</h1>\n\n" +
					"<h2>" + manuscript.getIBookAuthor() + "</h2>\n\n";
			return baseText;
		}
		
		public String getCopyrightText()
		{
			Calendar calendar = new GregorianCalendar();

			String baseText = 
					"<h1>Copyright</h1>\n\n" +
			
	                "<i>" + manuscript.getIBookTitle() + "</i> is a derivative work, based " +
					"on " + manuscript.getSourceAuthor() + "'s original manuscript " + 
	                "<i>" + manuscript.getSourceTitle() + "</i>, published in " +
					manuscript.getSourcePubDate() + ".\n\n" +
	
	                "All rights which pertain to that original work remain in place.\n\n" +
	
	                "No claim of copyright is made for this derived work.\n\n" +
	
	                "<i>" + manuscript.getIBookTitle() + "</i> was created and published " +
	                "in <i>epub</i> format for " + manuscript.getIBookAuthor() + " in " +
	                new Integer(calendar.get(Calendar.YEAR)).toString() + ", by the " +
	                "computer-automated <i>" + iBookSystemName + "</i>, an interactive " +
	                "book-publishing system hosted at <i>www.electraink.com</i>.\n\n";
				
			return baseText;
		}
		
		public String getLegalText()
		{
			String baseText = "<h1>Legal Disclaimer</h1>\n\n" +

		             "The derived work <i>" + 
		             manuscript.getIBookTitle() + 
		             "</i> was produced by <i>" + 
		             iBookSystemName + 
		             "</i>, a computer program which combines " +
		             "names of people and locations that have been input by visitors to " +
		             "the website <i>www.electraink.com</i> with published works that " +
		             "are either in the public domain or for which permission to use " +
		             "has been explicitly granted by the copyright holder. \n\n" +

		             "While it is the policy of <i>www.electraink.com</i> to discourage any usage " +
		             "of the software that may infringe on the rights of authors, publishers, " +
		             "or other copyright holders, the <i>" + 
		             iBookSystemName + 
		             "</i> software includes tools by which a site visitor can create his or " +
		             "her own templates based on text he or she uploads into the system. \n\n" +

		             "The <i>www.electraink.com</i> administrator has no ability to monitor " +
		             "or control such usage, and it is the responsibility of the end user who " +
		             "uploads such materials into the system to obtain any required " +
		             "permissions, or to verify that the work is in fact in the public domain. \n\n" +

		             "Acknowledgement of these rights and responsibilities is a condition of use at the <i>" +
		             iBookSystemName +
		             "</i> website.\n\n";
			return baseText;
		}
		
		public String getPrefaceText()
		{
			String baseText = "";
			
//			int subCount = 0;
			
			String bookId = manuscript.getBookId();
			PropsManager propsMgr = new PropsManager(userId);
			BookProfile bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
			String templateId = bookProfile.getTemplateId();
			TemplateProfile tProfile = propsMgr.getTemplateProfile(templateId);
			Vector<String[]> charSubMatrix = bookProfile.getCharSubMatrix();
			Vector<String[]> geolocSubMatrix = bookProfile.getGeolocSubMatrix();
			Vector<String[]> locSubMatrix = bookProfile.getLocSubMatrix();
			Iterator<String[]> charSubMatrixI = charSubMatrix.iterator();
			Iterator<String[]> geolocSubMatrixI = geolocSubMatrix.iterator();
			Iterator<String[]> locSubMatrixI = locSubMatrix.iterator();
			String[] matrixEntry = null;
			String tObjId = "";
			String iBookObjId = "";
			
			baseText = "<h1>Preface</h1>\n\n";
			
			if(bookProfile.getNumSubs(PropsManager.ObjType.CHARACTER) > 0 || 
					bookProfile.getNumSubs(PropsManager.ObjType.LOCATION) > 0 || 
					bookProfile.getNumSubs(PropsManager.ObjType.GEOLOCALE) > 0)
			{
				baseText += "<i>" +
				           manuscript.getIBookTitle() +
				           "</i> is a modern version of " + 
				           manuscript.getSourceAuthor() + 
				           "'s <i>" + 
				           manuscript.getSourceTitle() + 
				           "</i>.\n\n" +
				           "This retelling of the story first told in " + 
				           manuscript.getSourcePubDate() + 
				           " has been updated as follows:\n\n";
				
					if(bookProfile.getNumSubs(PropsManager.ObjType.CHARACTER) > 0)
					{
						baseText += "<ul>\n" +
					               "   <li>The cast of characters has been modified:\n" +
								   "      <ul>\n";
						while(charSubMatrixI.hasNext())
						{
							matrixEntry = charSubMatrixI.next();
							tObjId = matrixEntry[0];
							iBookObjId = matrixEntry[1];
							if(iBookObjId.length() > 0)
								baseText += "         <li>" + propsMgr.getCharacterName(iBookObjId) + " takes the role of the character " + tProfile.getCharacter(tObjId).getName() + "</li>\n";
						}
						baseText += "      </ul>\n" +
						           "   </li>\n" +
						           "</ul>\n";
						
					}
					if(bookProfile.getNumSubs(PropsManager.ObjType.GEOLOCALE) > 0)
					{
						baseText += "<ul>\n" +
					               "   <li>The story's geography has been changed:\n" +
								   "      <ul>\n";
						while(geolocSubMatrixI.hasNext())
						{
							matrixEntry = geolocSubMatrixI.next();
							tObjId = matrixEntry[0];
							iBookObjId = matrixEntry[1];
							
							if(iBookObjId.length() > 0)
								baseText += "      <li>" + propsMgr.getGeolocaleName(iBookObjId) + " stands in for " + tProfile.getGeoloc(tObjId).getName() + "</li>\n";
						}
						baseText += "      </ul>\n" +
						           "   </li>\n" +
						           "</ul>\n";
					}
					if(bookProfile.getNumSubs(PropsManager.ObjType.LOCATION) > 0)
					{
						baseText += "<ul>\n" +
					               "   <li>The scenery has changed:\n" +
								   "      <ul>\n";
					while(locSubMatrixI.hasNext())
					{
						matrixEntry = locSubMatrixI.next();
						tObjId = matrixEntry[0];
						iBookObjId = matrixEntry[1];
						if(iBookObjId.length() > 0)
							baseText += "         <li>" + manuscript.getSourceAuthor() + "'s " + tProfile.getLoc(tObjId).getName() + " becomes the modern tale's " + propsMgr.getLocationName(iBookObjId) + "</li>\n";
					}
					baseText += "      </ul>\n" +
					           "   </li>\n" +
					           "</ul>\n";
				}
			}
			else
			{
				baseText += "\n\n" +
			               "None of the characters or locations in the " +
							manuscript.getSourcePubDate() +
							" <i>" +
							manuscript.getSourceTitle() +
							"</i> manuscript by " +
							manuscript.getSourceAuthor() + " have been changed in this <i>" +
							iBookSystemName +
							"</i> retelling, <i>" + manuscript.getIBookTitle() + "</i>\n\n";
			}
			
			return baseText;
		}
	}
}
