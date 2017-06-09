package com.iBook.datastore.ebook.pdf;

import java.util.*;

import com.iBook.utilities.Utilities;

public class PdfLine
{
	public boolean pageStart = false;
	
	private Vector<String[]> lineSegments = new Vector<String[]>();
	
	public PdfLine(String lineText, TextOutSpecs outSpecs)
	{
		// takes a line of text premeasured to fit on the PDF page, and returns a 
        // a vector of segments that, together, represent a single line of output
		
		// lineText should be a line of text that fits into a PDF page, not 
		// a manuscript textblock, which is a run of text separated by line endings; 
		// manuscript textblocks may be divided into multiple PDF lines;
		//
		// PDF line will be segmented into string arrays consisting of a run of text 
		// and the font face to be used for the next segment
		//
		// the first segment in the line will use whatever font is valid for the PDPage;
		// 
		// for each manuscript textblock, and at the beginning of each book 
		// unit (copyright page, preface, contents, everything except title page), 
		// PDFontDatabase.intializeTextFont() should be called,
		// in case a font change comes at the beginning of the block. Title page is
		// excepted because it is constructed from textblocks created within 
		// the TitlePage
		
		// center tag
		String centerTag = "<center>";
		String centerCloseTag = "</center>";
		
		// heading style tags
		String h1Tag = "<h1>";
		String h2Tag = "<h2>";
		String h3Tag = "<h3>";
		String closeH1Tag = "</h1>";
		String closeH2Tag = "</h2>";
		String closeH3Tag = "</h3>";
		
		// ital style tags
		String italTag = "<i>";
		String closeItalTag = "</i>";
		
		// bold style tags
		String boldTag = "<b>";
		String closeBoldTag = "</b>";
		
		// list tags
		String listTag = "<ul>";
		String listItemTag = "<li>";
		String listCloseTag = "</ul>";
		String listItemCloseTag = "</li>";
		
		StringBuffer lineTextBuff = new StringBuffer(lineText.trim());
		
		// tag string lengths
		int tagLength = 0; // moves idx past an html tag when setting lineSegment text
		int centerTagLength = centerTag.length();
//		int centerCloseTagLength = centerCloseTag.length();
		// all h tags are same length; use h1 for all
		int hTagLength = h1Tag.length();
//		int closeBoldTagLength = closeBoldTag.length();
//		int closeItalTagLength = closeItalTag.length();
		int listTagLength = listTag.length();
		int listItemTagLength = listItemTag.length();
		int listCloseTagLength = listCloseTag.length();
		int listItemCloseTagLength = listItemCloseTag.length();
		
//		float textWidth = 0; 
				
		boolean italSet = false;
		boolean boldSet = false;
		boolean boldItalSet = false;
		
		// not sure at the time comment was written how many parts to 
		// lineSegment will end up with when code is complete, but when/wherever one gets 
		// created they should all be the same size
		int numSegmentAtts = 4;
		
		//  The lineSegment array contains two strings:
		//    [0] == The text to be output
		//    [1] == Font style for the next segment (change fonts to this 
		//    style after the current segment's text has been output to the PDF)
		//    [2] == heading level, 1-3, in the form of h1, h2, or h3
		//    [3] == para type (start of paragraph? centered text?)
		String[] lineSegment = null;
		
		String remainder = "";
		int idx = 0;
		int end = 0; 
		
		outSpecs.setLineStartFlag(true);
		
		while((idx = lineTextBuff.indexOf("<", end)) != -1)
		{
			lineSegment = new String[numSegmentAtts];
			if(outSpecs.paraStart)
				lineSegment[3] = "paraStart";
			else if(outSpecs.lineStart)
				lineSegment[3] = "lineStart";
			
			outSpecs.setParaStartFlag(false);
			outSpecs.setLineStartFlag(false);

			// center---<center>
			if(lineTextBuff.substring(idx).startsWith(centerTag))
			{
				tagLength = centerTagLength;
				lineSegment[2] = "center";
				lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
				// sanitizeText() will remove any html tags that were allowed in the manuscript 
				// but aren't handled here for the PDF
			}

			// close center---</center>
			if(lineTextBuff.substring(idx).startsWith(centerCloseTag))
			{
				tagLength = centerCloseTag.length();
				if(idx > end) // + closeBoldTagLength)
					lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
				lineSegment[2] = "/center";
			}
			
			// level 1 head---<h1>
			if(lineTextBuff.substring(idx).startsWith(h1Tag))
			{
				tagLength = hTagLength;
				lineSegment[2] = "h1";
				lineSegment[1] = PDFontDatastore.fontNameBold;
				boldSet = true;
				lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
				// sanitizeText() will remove any html tags that were allowed in the manuscript 
				// but aren't handled here for the PDF
			}
			if(lineTextBuff.substring(idx).startsWith(h2Tag))
			{
				tagLength = hTagLength;
				lineSegment[2] = "h2";
				lineSegment[1] = PDFontDatastore.fontNameItal;
				boldSet = false;
				italSet = true;
				lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
				// sanitizeText() will remove any html tags that were allowed in the manuscript 
				// but aren't handled here for the PDF
			}
			if(lineTextBuff.substring(idx).startsWith(h3Tag))
			{
				tagLength = hTagLength;
				lineSegment[2] = "h3";
				lineSegment[1] = PDFontDatastore.fontNameBold;
				boldSet = true;
				italSet = false;
				lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
				// sanitizeText() will remove any html tags that were allowed in the manuscript 
				// but aren't handled here for the PDF
			}
			// close head---</h1..3>
			if(lineTextBuff.substring(idx).startsWith(closeH1Tag) ||
			   lineTextBuff.substring(idx).startsWith(closeH2Tag) ||
			   lineTextBuff.substring(idx).startsWith(closeH3Tag))
			{
				tagLength = hTagLength;
				lineSegment[2] = "/h" + lineTextBuff.substring(idx + 3, idx + 4);
				lineSegment[1] = PDFontDatastore.fontNamePlain;
				boldSet = false;
				italSet = false;
				lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
				// sanitizeText() will remove any html tags that were allowed in the manuscript 
				// but aren't handled here for the PDF
			}
			// ital---<i>
			if(lineTextBuff.substring(idx).startsWith(italTag))
			{
				tagLength = italTag.length();
				
				// ital/bold---<i><b>...</b></i>
				if(lineTextBuff.substring(idx).startsWith(italTag + boldTag))
				{
					lineSegment[1] = PDFontDatastore.fontNameBoldItal;
					boldItalSet = true;
					tagLength += boldTag.length();
				}
				else
				{
					lineSegment[1] = PDFontDatastore.fontNameItal;
					italSet = true;
				}
				lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
			}
			
			// bold---<b>
			if(lineTextBuff.substring(idx).startsWith(boldTag))
			{
				tagLength = boldTag.length();
				
				// bold/ital---<b><i>...</i></b>
				if(lineTextBuff.substring(idx).startsWith(boldTag + italTag))
				{
					lineSegment[1] = PDFontDatastore.fontNameBoldItal;
					boldItalSet = true;
					tagLength += italTag.length();
				}
				else
				{
					lineSegment[1] = PDFontDatastore.fontNameBold;
					boldSet = true;
				}
				lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
			}
			
			// close ital---</i>
			if(lineTextBuff.substring(idx).startsWith(closeItalTag))
			{
				tagLength = closeItalTag.length();
				if(lineTextBuff.substring(idx).startsWith(closeItalTag + closeBoldTag))
				{
					boldItalSet = false;
					boldSet = true;
					tagLength += closeBoldTag.length();
				}
				else
				{
					boldItalSet = false;
					italSet = false;
				}
				if(idx >= end) // + closeItalTagLength)
					lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
				lineSegment[1] = PDFontDatastore.fontNamePlain;
			}
			
			// close bold---</b>
			if(lineTextBuff.substring(idx).startsWith(closeBoldTag))
			{
				tagLength = closeBoldTag.length();
				if(lineTextBuff.substring(idx).startsWith(closeBoldTag + closeItalTag))
				{
					boldItalSet = false;
					italSet = true;
					tagLength += closeItalTag.length();
				}
				else
				{
					boldItalSet = false;
					boldSet = false;
				}
				if(idx > end) // + closeBoldTagLength)
					lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
				lineSegment[1] = PDFontDatastore.fontNamePlain;
			}
			
			// list tag---<ul>
			if(lineTextBuff.substring(idx).startsWith(listTag))
			{
				tagLength = listTagLength;
				lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
				lineSegment[2] = "ul";
			}
			
			// list item---<li>
			if(lineTextBuff.substring(idx).startsWith(listItemTag))
			{
				tagLength = listItemTagLength;
				lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
				lineSegment[2] = "li";
			}
			
			// close list---</ul>
			if(lineTextBuff.substring(idx).startsWith(listCloseTag))
			{
				tagLength = listCloseTagLength;
				lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
				lineSegment[2] = "/ul";
			}
			
			// close list item---</li>
			if(lineTextBuff.substring(idx).startsWith(listItemCloseTag))
			{
				tagLength = listItemCloseTagLength;
				lineSegment[0] = Utilities.sanitizeString(lineTextBuff.substring(end, idx));
				lineSegment[2] = "/li";
			}
			
			
			// end if(lineTextBuff.substring(idx).startsWith( ...tag... )) series
			
			// adjust value of end for next trip through loop; if left alone it points 
			// to the first < in the string, and an infinite loop. If no tag is found, 
			// idx + tagLength could = 0, and again, infinite loop, so set end to idx + 1
			if(tagLength > 0)
				end = idx + tagLength;
			else
				end = idx + 1;
			lineSegments.addElement(lineSegment);
			
			// remainder of input string after all html tags have been processed
			remainder = Utilities.sanitizeString(lineTextBuff.substring(idx));
			
			// only the first segment can be the start of a new line
			outSpecs.setLineStartFlag(false);
		} // end while((idx = lineTextBuff.indexOf("<", end)) != -1)
		
		// remainder is what's left over after processing the input string in the while() loop
		if(tagLength == 0)
		{
			lineSegment = new String[numSegmentAtts];
			// if there were no html tags in the input string, it will not have been processed,
			// so remainder is equal to the entire string
			remainder = lineTextBuff.toString();
			lineSegment[3] = "paraStart";
		}
		if(remainder.length() > 0)
		{
			lineSegment = new String[numSegmentAtts];
			if(outSpecs.paraStart)
				lineSegment[3] = "paraStart";
			else if(outSpecs.lineStart)
				lineSegment[3] = "lineStart";
			lineSegment[0] = remainder;
			// [1], the font type, will be null for the last segment
			lineSegments.addElement(lineSegment);
		}
	}

	public Vector<String[]> getLineSegments()
	{
		return lineSegments;
	}
	
	public String getText()
	{
		String text = "";
		String[] lineSegment = null;
		
		Iterator<String[]> lineSegmentsI = lineSegments.iterator();
		while(lineSegmentsI.hasNext())
		{
			lineSegment = lineSegmentsI.next();
			text += lineSegment[0];
		}
		return text;
	}
}
