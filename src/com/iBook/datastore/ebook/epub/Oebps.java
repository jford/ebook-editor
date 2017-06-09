package com.iBook.datastore.ebook.epub;

import java.util.*;

import com.iBook.datastore.manuscripts.*;

public class Oebps
{
	private Manuscript manuscript = null;
	private Package packageObj = null;
	
	private Vector<String> xhtmlTitles = new Vector<String>();
	
	public Oebps(Manuscript manuscript, String userId)
	{
		this.manuscript = manuscript;
		packageObj = new Package(manuscript, userId);
	}
	
	public String getContentOpf()
	{
		String fileText = "";
		String contentOpfHeader = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		fileText = contentOpfHeader + packageObj.getPackageText();
		return fileText;
	}
	
	public String getTocNcx()
	{
		String fileText = "";
		
		String tocNcxHeader = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" +
	        "   <!DOCTYPE ncx PUBLIC \"-//NISO//DTD ncx 2005-1//EN\"\n " +
	        "            \"http://www.daisy.org/z3986/2005/ncx-2005-1.dtd\">\n";
		
		String ncxTagOpen = "<ncx xmlns=\"http://www.daisy.org/z3986/2005/ncx/\" version=\"2005-1\">\n";
		
		String ncxHeadTag =
			"<head>\n" +
		    "   <meta name=\"dtb:uid\" " + 
			         "content=\"" +
		             packageObj.getUid() + 
		             "\"/>\n" +
		    "   <meta name=\"dtb:depth\" content=\"2\"/>\n" +
		    "   <meta name=\"dtb:totalPageCount\" content=\"0\"/>\n" +
		    "   <meta name=\"dtb:maxPageNumber\" content=\"0\"/>\n" +
		    "</head>\n";
		
		String docTitle = 
			"<docTitle>\n" +
		    "   <text>" + manuscript.getIBookTitle() + "</text>\n" +
			"</docTitle>\n";
		
		String navMap = 
			"<navMap>\n" +
			getNavPoints() +
		    "</navMap>\n";
			
		String fileCloseTag = "</ncx>";
		
		fileText = tocNcxHeader + ncxTagOpen + ncxHeadTag + docTitle + navMap + fileCloseTag;
		
		return fileText;
	}
	
	private String getNavPoints()
	{
		int idx = 0;
		int playOrder = 1;
		// number of navpoints is number of filebreaks in the 
		// manuscript, plus 3 (title, copyright, and legalese pages)
		int numNavPoints = manuscript.getFilebreaks().size() + 3;

		Integer filebreak = null;

		String navPointId = "";
		String navPointText = "";
		String navPointsText = "";
		String navLabelText = "";
		String textblock = "";
		String tocText = "";
		
		// playOrderList is a list of spine item IDs; one spine item was created for each
		// entry in the filebreaks list, by the manifest class, plus title and copyright files
		// with list indices of 0 and 1, and legal verbage as last item on the list
		Vector<String> playOrderList = packageObj.getSpineItems();
		Vector<Integer> filebreaks = manuscript.getFilebreaks();
		
		Iterator<String> playOrderI = playOrderList.iterator();
		Iterator<Integer> filebreaksI = filebreaks.iterator();
		
		// do the navPoints list
		navPointId = playOrderI.next();

		// first, is there a cover?
		if(navPointId.equals("Cover"))
		{
			navPointsText += getNavPointText(navPointId, getNavLabelText(navPointId), playOrder++);
			navPointId = playOrderI.next();
		}
		// next, the title and copyright files...
		String[] tocTextSupplementals = { "Title", "Copyright", "Preface", "Legal Disclaimer" };
		// if the order of file titles in the array changes, update the reference to Legal
		// after the end of the while(filebreaksI.hasNext()) loop
		for(int count = 0; count < 3; count++)
		{
			navPointsText += getNavPointText(navPointId, getNavLabelText(tocTextSupplementals[count]), playOrder++);
			navPointId = playOrderI.next();
		}
		// then the manuscript content files...
		while(filebreaksI.hasNext())
		{
			filebreak = filebreaksI.next();
			
			textblock = manuscript.getTextblock(filebreak);
			
			idx = 0;
			for(int spaceCount = 0; spaceCount < 6; spaceCount++)
			{
				idx = textblock.indexOf(" ", idx + 1);
				if(idx == -1)
				{
					idx = textblock.length();
					break;
				}
			}
			// if label text is excerpt, add "..."
			String textContinues = idx < textblock.length() ? "..." : "";
			tocText = textblock.substring(0, idx) + textContinues;
			xhtmlTitles.add(tocText);
			navLabelText = getNavLabelText(tocText);
			navPointText = getNavPointText(navPointId, navLabelText, playOrder++); 
			navPointsText += navPointText;
			navPointId = playOrderI.next();
		}
		// and finally, the legal stuff...
		// be sure the array reference points to the correct supplemental title
		navPointsText += getNavPointText(navPointId, getNavLabelText(tocTextSupplementals[3]), playOrder);
		

		
		return navPointsText;
	}
	private String getNavLabelText(String tocText)
	{
		return "      <navLabel>\n" +
			   "         <text>" + tocText + "</text>\n" + 
	           "      </navLabel>\n";
	}
	
	private String getNavPointText(String navPointId, String navLabelText, int playOrder)
	{
		String navPointText = "";
		navPointText = 
				"   <navPoint id=\"" + 
		            navPointId + 
		            "\" playOrder=\"" + 
		            new Integer(playOrder).toString() + 
		            "\">\n" +
		            navLabelText +
		         "      <content src=\"Text/" +
		            navPointId + 
		            ".xhtml\"/>\n" +
		         "   </navPoint>\n";
		return navPointText;
	}
	
	public Vector<String> getXhtmlTitles()
	{
		return xhtmlTitles;
	}
	
}
