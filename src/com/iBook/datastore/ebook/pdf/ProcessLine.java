package com.iBook.datastore.ebook.pdf;

import java.util.Iterator;
import java.util.Vector;

public class ProcessLine
{
	
	// Caller must set leftMargin and line values and the para start flag in 
	// TextOutSpecs object before creating a ProcessLine object. After 
	// processing a line, and before doing the next one, update the line count 
	// and return the offset to the left margin. Example:
	//  
	// 		int leftMargin = outSpecs.getLeftMargin();
	//		int line = outSpecs.getLine();
	//		outSpecs.setParaStartFlag(true);
	//			
	//		// get the next line in the list
	//		new ProcessLine(textLinesI.next(), outSpecs);
	//
	//		outSpecs.setLine(++line);
	//		outSpecs.setOffset(leftMargin + paraIndent);

	 
	public ProcessLine(PdfLine pdfLine, TextOutSpecs outSpecs)
	{
		if(outSpecs.isBodyText)
		{
			outSpecs.setFontsize(outSpecs.getPageSpecs().getFontsize());
			outSpecs.setLineLeading(outSpecs.getPageSpecs().getLineLeading());
			outSpecs.setGap(0);
		}

		PDFontDatastore fontStore = new PDFontDatastore();
		
		Vector<String[]> lineSegments = pdfLine.getLineSegments();
  		Iterator<String[]> lineSegmentsI = lineSegments.iterator();
  		String[] lineSegment;
  		String fontType = "";
  		
		do // line segment
		{
			// when text contains user-added paragraph breaks, lineSegments may not have a next element;
			// if that's the case, bail out...
			if(!lineSegmentsI.hasNext())
				break;
			
			// If there is a next segment, get it...
			lineSegment = lineSegmentsI.next();
			
			// process it and send text to TextOut class
			new ProcessLineSegment(lineSegment, outSpecs);
			
			// only first segment of a line can be the line start
			outSpecs.setLineStartFlag(false);
			
			if(lineSegment[1] != null)
			{
				// set font in outSpecs for next time through the process segment loop
				fontType = lineSegment[1];
				if(fontType.equals(PDFontDatastore.fontNameBold))
					outSpecs.setFont(fontStore.getFont(PDFontDatastore.fontNameBold));
				if(fontType.equals(PDFontDatastore.fontNameItal))
					outSpecs.setFont(fontStore.getFont(PDFontDatastore.fontNameItal));
				if(fontType.equals(PDFontDatastore.fontNameBoldItal))
					outSpecs.setFont(fontStore.getFont(PDFontDatastore.fontNameBoldItal));
			}
		}
		while(lineSegmentsI.hasNext());
	}
}
