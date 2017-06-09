package com.iBook.datastore.ebook.epub;

import java.util.*;

import com.iBook.datastore.*;
import com.iBook.datastore.books.*;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.*;

public class Text                              
{
	private Manuscript manuscript = null;
	
	private String fileHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
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
	
	private String coverFileHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
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
	
	private String fileTail = "   </div>\n" +
							  "   </body>\n" +
							  "</html>";
	
	private String coverFileTail = "      </div> \n" +  
			                       "   </body>\n" +
			  					   "</html>";

	PropsManager propsMgr = null;
	
	private String coverArtFilename = "";
	private String iBookSystemName = PropsManager.getIBookSystemName();
	private String pathMrkr = PropsManager.getPathMarker();
	private String path_to_epub = EPubObj.getPathToEpub(); 
	private String path_to_text = path_to_epub + "OEBPS" + pathMrkr + "Text" + pathMrkr;
	private String userId= "";
	
	private boolean stopForDebugging = false;

	public Text(Manuscript manuscript, Vector<String> xhtmlTitles, String userId)
	{
		this.manuscript = manuscript;
		this.userId = userId;
		
		propsMgr = new PropsManager(userId);
		coverArtFilename = propsMgr.getCoverArtFilename(manuscript.getId());
		if(coverArtFilename.length() > 0)
		{
			doCover();
		}
		
		Vector<String> textblocks = manuscript.getTextblocks(); 
		Vector<Integer> filebreaks = manuscript.getFilebreaks();
		Iterator<String> textblocksI = textblocks.iterator();
		Iterator<String>xhtmlTitlesI = xhtmlTitles.iterator();
		Iterator<Integer> filebreaksI = null;
		String fileName = "unit_1.xhtml";
		String fileText = "";
		String textblock = "";
		String titleCloseTag = "</title>";
		String titleTag = "<title>";
		String titleText = "";
		int idx = 0;
		int textblockNum = 1;
		int unitCount = 1;
		Integer filebreak = null;
		
		while(textblocksI.hasNext())
		{
			textblock = purgeIbookTags(textblocksI.next());
			textblock = normalizeForHtml(textblock);
			
			// for each text block, we need to go through the filebreaks and xhtml 
			// titles vectors from the beginning; a fileHeader string will be created 
			// for every textblock, but only used when it's time for a new file to be
			// created. The filebreaks and xhtmlTitles vectors need to be in agreement
			// when that happens
			filebreaksI = filebreaks.iterator();
			xhtmlTitlesI = xhtmlTitles.iterator();
			while(filebreaksI.hasNext())
			{
				filebreak = filebreaksI.next();
				titleText = xhtmlTitlesI.next();
				if(textblockNum == 1)
				{
					// first file needs a title tag...
//					idx = fileText.indexOf(titleTag);
//					fileText.insert(idx + titleTag.length(), titleText);
					// ...then it's on to build up the rest of the file
					fileText = getFileHeader(titleText);
					break;
				}
				else if(filebreak.compareTo(new Integer(textblockNum)) == 0)
				{
					// okay, now it's time to create a new file
					// first, add fileTail to the text...
					fileText += fileTail;
					
					// then write the current fileText to a file...
					writeText(fileText.toString(), "unit_" + new Integer(unitCount++).toString() + ".xhtml");
					
					// and finally, start over with a new file text...
					fileText = getFileHeader(titleText);
					break;
				}
			}
			fileText += "<p>\n" +
			           textblock +
			           "\n</p>\n";

			textblockNum++;
		}
		// ...and write out the last chapter...
		fileText += fileTail;
		writeText(fileText.toString(), "unit_" + new Integer(unitCount++).toString() + ".xhtml");
		
		/*
		 * supplemental files are itemized in the Oebps object, in  getNavPoints() method
		 * and supplementalFiles[] in the Manifest object's constructor where the files are 
		 * inserted into the spineItems vector; the max value of the for loop count variable
		 * should match the number of supplemental files that precede the book contents; currently,
		 * there are four files, one of which comes at the end of the book, so the for loop
		 * sets a max of count < 3    
		 */
		doTitle();
		doCopyright();
		doLegal();
		doPreface();
		
	}
	
	private void doCover()
	{
		String coverArtPath = Utilities.replaceChars(propsMgr.getPathToUserDir() + "manuscripts" + pathMrkr, pathMrkr, "/", "all");
		coverArtPath = coverArtPath.substring(coverArtPath.indexOf("/iBook"));
		String coverFileText = coverFileHeader;
//		coverFileText += "<img media-type=\"image/jpeg\" src=\"../Images/" + coverArtFilename.substring(coverArtFilename.lastIndexOf("Cover")) + "\" alt=\"Cover art\" /> \n";
		coverFileText += "        <img src=\"../Images/" + coverArtFilename.substring(coverArtFilename.lastIndexOf("Cover")) + "\" alt=\"Cover art\" /> \n";
		coverFileText += coverFileTail;
		writeText(coverFileText, "Cover.xhtml");
	}
	
	private void doCopyright()
	{
		Calendar calendar = new GregorianCalendar();
		
		String copyrightText = getFileHeader("Copyright");
		
		copyrightText += "<h1>Copyright</h1>\n" +
						 "<div class=\"copyrightblock\">\n" +
		                  "<p>\n" +
                           "<i>" + 
                           manuscript.getIBookTitle() + 
                           "</i> is a derivative work, based on " + 
                           manuscript.getSourceAuthor() + 
                           "'s original manuscript <i>" + 
                           manuscript.getSourceTitle() + 
                           "</i>, published in " + 
                           manuscript.getSourcePubDate() +
                           "." +
                           "</p>\n";
		
		copyrightText += "<p>\n" +
		                 "All rights which pertain to that original work remain " +
				         "in place.\n" +
		                 "</p>\n";
		
		copyrightText += "<p>\n" + 
		                 "No claim of copyright is made for this derived work.\n" +
		                 "</p>\n";
		
		copyrightText += "<p>\n" + 
		                 "<i>" + 
		                 manuscript.getIBookTitle() + 
		                 "</i> was created and published in <i>epub</i> format for " +
		                 manuscript.getIBookAuthor() + 
		                 " in " +
		                 new Integer(calendar.get(Calendar.YEAR)).toString() +
		                 ", by the computer-automated <i>" + 
		                 iBookSystemName + 
		                 "</i> Interactive Book Publishing System hosted at <i>www.electraink.com</i>. \n" +
		                 "</p>\n" +
                         "</div>";
		
		copyrightText += fileTail;
		writeText(copyrightText, "copyright.xhtml");
	}
	
	private void doTitle()
	{
		String titleFileText = getFileHeader("Title");

		if(coverArtFilename.length() > 0)
		{
			String coverArtType = "";
			String coverLink = "";
			String imageType = "";

			int idx = coverArtFilename.lastIndexOf(".");
			
			if(idx != -1)
				coverArtType = coverArtFilename.substring(coverArtFilename.lastIndexOf("."));
			if(coverArtType.equals(".jpg"))
				imageType = "jpeg";
			else if(coverArtType.equals(".svg"))
				imageType = "svg+xml";
			else
				imageType = coverArtType.substring(1);
			
			coverLink = "\n         <link rel=\"coverpage\" href=\"../Images/Cover" + coverArtType + "\" type=\"image/" + imageType + "\" />";
			
			idx = titleFileText.indexOf("<link");
			idx = titleFileText.lastIndexOf("\n", idx);
			titleFileText = titleFileText.substring(0, idx) + coverLink + titleFileText.substring(idx);
		}
		
		titleFileText += "<div class=\"titlepage\">\n" +
				         "         <p class=\"booktitle\">\n" + manuscript.getIBookTitle() + "\n</p>\n" +
		                 "         <p class=\"bookauthor\">\nBy " + manuscript.getIBookAuthor() + "\n</p>\n" +
				         "         <p class=\"booksubtitle\">\nA retelling of " + manuscript.getSourceAuthor() +
				                        "'s " + manuscript.getSourcePubDate() + " manuscript <i>" + 
				                        manuscript.getSourceTitle() + "</i>.\n</p>\n" +
				         "          <p class=\"ibookslug\">\nGenerated by <i>" + 
				                        iBookSystemName + 
				                        "</i>, a computer application for retelling old tales in modern times.</p>\n" +
				                        "</div>\n";
		
		titleFileText += fileTail;
		
		writeText(titleFileText, "title.xhtml");
				
	}
	
	private void doLegal()
	{
		String legalText = getFileHeader("Legal Disclaimer");
		
		legalText += "<h1>Legal Disclaimer</h1>\n" +
		             "<div class=\"legalnotice\">\n" +
				     "<p>\n" + 
		             "The derived work <i>" + 
		             manuscript.getIBookTitle() + 
		             "</i> was produced by <i>" + 
		             iBookSystemName + 
		             "</i>, a computer program which combines " +
		             "names of people and locations that have been input by visitors to " +
		             "the website <i>www.electraink.com</i> with published works that " +
		             "are either in the public domain or for which permission to use " +
		             "has been explicitly granted by the copyright holder. \n" +
		             "</p>\n" +
		             "<p>\n" + 
		             "While it is the policy of <i>www.electraink.com</i> to discourage any usage " +
		             "of the software that may infringe on the rights of authors, publishers, " +
		             "or other copyright holders, the <i>" + 
		             iBookSystemName + 
		             "</i> software includes tools by which a site visitor can create his or " +
		             "her own templates based on text he or she uploads into the system. " +
		             "</p>\n" +
		             "<p>\n" +
		             "The <i>www.electraink.com</i> administrator has no ability to monitor " +
		             "or control such usage, and it is the responsibility of the end user who " +
		             "uploads such materials into the system to obtain any required " +
		             "permissions, or to verify that the work is in fact in the public domain." +
		             "</p>\n" + 
		             "<p>\n" +
		             "Acknowledgement of these rights and responsibilities is a condition of use at the <i>" +
		             iBookSystemName +
		             "</i> website.\n" +
		             "</p>\n" +
		             "</div>";
		
		legalText += fileTail;
		writeText(legalText, "legal.xhtml");
	}
	
	private void doPreface()
	{
		int subCount = 0;
		String preface = getFileHeader("Preface");
		
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
		
		preface += "<div class=\"preface\">\n" +
		           "   <h1>Preface</h1>\n";
		if(bookProfile.getNumSubs(PropsManager.ObjType.CHARACTER) > 0 || 
				bookProfile.getNumSubs(PropsManager.ObjType.LOCATION) > 0 || 
				bookProfile.getNumSubs(PropsManager.ObjType.GEOLOCALE) > 0)
		{
			preface += "   <p>\n" +
			           "   <i>" +
			           manuscript.getIBookTitle() +
			           "</i> is a modern version of " + 
			           manuscript.getSourceAuthor() + 
			           "'s <i>" + 
			           manuscript.getSourceTitle() + 
			           "</i>.\n" +
			           "</p>\n" +
			           "<p>\n" +
			           "This retelling of the story first told in " + 
			           manuscript.getSourcePubDate() + 
			           " has been updated as follows:\n" +
			           "</p>\n";
				if(bookProfile.getNumSubs(PropsManager.ObjType.CHARACTER) > 0)
				{
					preface += "<ul>\n" +
				               "   <li>The cast of characters has been modified:\n" +
							   "      <ul>\n";
					while(charSubMatrixI.hasNext())
					{
						matrixEntry = charSubMatrixI.next();
						tObjId = matrixEntry[0];
						iBookObjId = matrixEntry[1];
						if(iBookObjId.length() > 0)
							preface += "         <li>" + propsMgr.getCharacterName(iBookObjId) + " takes the role of the character " + tProfile.getCharacter(tObjId).getName() + "</li>\n";
					}
					preface += "      </ul>\n" +
					           "   </li>\n" +
					           "</ul>\n";
					
				}
				if(bookProfile.getNumSubs(PropsManager.ObjType.GEOLOCALE) > 0)
				{
					preface += "<ul>\n" +
				               "   <li>The action takes place in:\n" +
							   "      <ul>\n";
					while(geolocSubMatrixI.hasNext())
					{
						matrixEntry = geolocSubMatrixI.next();
						tObjId = matrixEntry[0];
						iBookObjId = matrixEntry[1];
						
						if(iBookObjId.length() > 0)
							preface += "      <li>" + propsMgr.getGeolocaleName(iBookObjId) + " instead of " + tProfile.getGeoloc(tObjId).getName() + "</li>\n";
					}
					preface += "      </ul>\n" +
					           "   </li>\n" +
					           "</ul>\n";
				}
				if(bookProfile.getNumSubs(PropsManager.ObjType.LOCATION) > 0)
				{
					preface += "<ul>\n" +
				               "   <li>The scenery has changed:\n" +
							   "      <ul>\n";
				while(locSubMatrixI.hasNext())
				{
					matrixEntry = locSubMatrixI.next();
					tObjId = matrixEntry[0];
					iBookObjId = matrixEntry[1];
					if(iBookObjId.length() > 0)
						preface += "         <li>" + manuscript.getSourceAuthor() + "'s " + tProfile.getLoc(tObjId).getName() + " becomes the modern tale's " + propsMgr.getLocationName(iBookObjId) + "</li>\n";
				}
				preface += "      </ul>\n" +
				           "   </li>\n" +
				           "</ul>\n";
			}
			preface += "</div>\n";
		}
		else
		{
			preface += "<p>\n" +
		               "None of the characters or locations in the " +
						manuscript.getSourcePubDate() +
						"<i>" +
						manuscript.getIBookTitle() +
						"</i> manuscript by " +
						manuscript.getSourceAuthor() + " have been changed in this <i>" +
						iBookSystemName +
						"</i> retelling.\n" +
						"</p>\n";
		}
		preface += fileTail;
		writeText(preface, "preface.xhtml");
	}
	
	private String getFileHeader(String title)
	{
		// title is the text to be inserted into the .xhtml's <title>...</title> tag
		int idx = 0;
		String titleTag = "<title>";
		StringBuffer fileHeaderBuff = new StringBuffer(fileHeader);

		// first file needs a title tag...
		idx = fileHeaderBuff.indexOf(titleTag);
		fileHeaderBuff.insert(idx + titleTag.length(), title);

		return fileHeaderBuff.toString();
	}
	
	private void writeText(String fileText, String fileName)
	{
		Utilities.write_file(path_to_text + fileName, fileText, true);
	}
	
	private String normalizeForHtml(String textblock)
	{
		int count = 0;
		int idx = 0;
		String[][] allowed = {
				                { "<i>", "HTML_ITAL_OPEN" },
				                { "</i>", "HTML_ITAL_CLOSE" },
				                { "<b>", "HTML_BOLD_OPEN" },
				                { "</b>", "HTML_BOLD_CLOSE" },
				                { "<center>", "HTML_CENTER_OPEN" },
				                { "</center>", "HTML_CENTER_CLOSE" },
				                { "<h1>", "HTML_HEAD_ONE_OPEN" },
				                { "</h1>", "HTML_HEAD_ONE_CLOSE" },
				                { "<h2>", "HTML_HEAD_TWO_OPEN" },
				                { "</h2>", "HTML_HEAD_TWO_CLOSE" },
				                { "<h3>", "HTML_HEAD_THREE_OPEN" },
				                { "</h3>", "HTML_HEAD_THREE_CLOSE" },
				                { "<h4>", "HTML_HEAD_FOUR_OPEN" },
				                { "</h4>", "HTML_HEAD_FOUR_CLOSE" },
				                { "<ul>", "HTML_LIST_OPEN" },
				                { "</ul>", "HTML_LIST_CLOSE" },
				                { "<li>", "HTML_LIST_ITEM_OPEN" },
				                { "</li>", "HTML_LIST_ITEM_CLOSE" }
		                     };
		String[][] disallowed = {
				                    {"&", "&amp;"},
				                    {"<", "&lt;"},
				                    {">", "&gt;"}
		                        };
		
		// change all "allowed" html tags to preservation markers
		for(count = 0; count < allowed.length; count++)
		{
			textblock = Utilities.replaceChars(textblock, allowed[count][0], allowed[count][1], "all");
		}
		
		// change all disallowed tags to encoded equivalents
		for(count = 0; count < disallowed.length; count++)
		{
			textblock = Utilities.replaceChars(textblock, disallowed[count][0], disallowed[count][1], "all");
		}
		
		// restore allowed tags
		for(count = 0; count < allowed.length; count++)
		{
			textblock = Utilities.replaceChars(textblock, allowed[count][1], allowed[count][0], "all");
		}
		
		// need to look for double line separators. Can't use System.getProperty(). Firefox has
		// been observed to use both \n and \r\n in the Final Proof pane on separate occassions.
		// Need to look for both.
		String[] doubleSpace = { "\n\n", "\r\n\r\n" };
		StringBuffer textblockBuff = new StringBuffer(textblock.trim());
		for(count = 0; count < doubleSpace.length; count++)
		{
			while((idx = textblockBuff.indexOf(doubleSpace[count])) != -1)
			{
				textblockBuff.replace(idx, idx + doubleSpace[count].length(), "</p><p>");
			}
		}
		return textblockBuff.toString();
	}
	
	private String purgeIbookTags(String textblock)
	{
		StringBuffer textblockBuff = new StringBuffer(textblock);
		String[] tagMrkrs = { "descriptor>", "gdescriptor>", "alias>", "attribute>" };
		int idx = 0;
		int end = 0;
		for(int count = 0; count < tagMrkrs.length; count++)
		{
			// point idx at the beginning of the xxx> tag...
			while((idx = textblockBuff.indexOf(tagMrkrs[count])) != -1)
			{
				// point end to the char after the xxx> tag...
				end = idx + tagMrkrs[count].length();
				// and reset idx to the < char, counting backwards from end
				idx = textblockBuff.lastIndexOf("<", end - 1);
				// (end - 1, because sometimes an iBook tag butts up to a valid 
				// HTML tag---descriptor><i>---and if end points to the < of 
				// the <i> tag, idx gets reset to that same 
				// char. If end and idx are the same, nothing gets deleted
				textblockBuff.delete(idx, end);
			}
		}
		return textblockBuff.toString();
	}
}
