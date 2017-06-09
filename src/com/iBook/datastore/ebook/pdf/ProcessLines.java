package com.iBook.datastore.ebook.pdf;

import java.util.*;

import com.iBook.datastore.ebook.BoilerplateFactory;
import com.iBook.utilities.Utilities;

public class ProcessLines
{

	public boolean textRemaining = true;
	
	public ProcessLines(String text, TextOutSpecs outSpecs)
	{
		// text is a collection of textblocks; each textblock consist of a 
		// single logical line---all the text between line ends
		
		// texts retrieved from the boilerplate factory use \n\n as paragraph markers; replace with \n
		text = Utilities.replaceChars(text, "\n\n", "\n", "all");

		String textblock = "";
		int idx = 0;
		int end = 0;
		
		float gap = outSpecs.getGap();
		
		// process textblocks one at a time 
		while((idx = text.indexOf("\n", end)) != -1)
		{
			textblock = text.substring(end, idx);
			end = idx + 1;
			
			// divide textblock into a vector of lines of text that fit the PDF page
			// copyrightTextLines is a vector containing vectors of lineSegment arrays;
			// each lineSegment array represents a line of text, divided into segments
			// based on which font is used; this reduces the number of PdfBox showText() calls,
			// reducing in turn the number of drawing operations in the PDF 
 			Vector<PdfLine> pdfLines = (new PdfLines(textblock, outSpecs)).getPdfLines();

			// process the lines of text that represent a textblock;
			// processing the line consists of dividing the textblock 
			// into segments, then outputting those segments to the PDF being
			// created. PDPageContentStream is contained in the outSpecs 
			// object

			Iterator<PdfLine> textLinesI = pdfLines.iterator();
			do // line
			{
				// process the next line of text 
				new ProcessLine(textLinesI.next(), outSpecs);
				
				// update the PageSpec's space-used counter
				if(outSpecs.getText() != null && outSpecs.getText().length() > 0)
				{
					outSpecs.getPageSpecs().incrementVSpaceUsed((float)outSpecs.getLineLeading() + (float)outSpecs.getGap());
				}
				outSpecs.setOffset(outSpecs.getLeftMargin());
			}
			while(textLinesI.hasNext());
		}
	}
}
