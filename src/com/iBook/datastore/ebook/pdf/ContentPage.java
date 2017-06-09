package com.iBook.datastore.ebook.pdf;

import com.iBook.datastore.manuscripts.Manuscript;

import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

public class ContentPage
{
	public boolean needToAddBlank = false;
	
	// page counter, for page footers in content (excludes prefaratory pages)
	int pageCount = 0;
	
	private PDDocument document;
	private PDPage page;
	private PDRectangle pageSize = PdfObj.pageSize;
	
	private PdfLine pdfLine = null;
	
	private Manuscript manuscript;

	private String userId;
	
	private TextOutSpecs outSpecs;
	
	private Vector<Integer> filebreaks;
	private Vector<String> textblocks;
	private Vector<PdfLine> pdfLines;
	private Vector<String[]> tocItems = new Vector<String[]>();
	private String[] tocItem = null;
	
	private Iterator<PdfLine> pdfLinesI;
	
	public ContentPage(Manuscript manuscript, String userId, PDDocument document)
	{
		this.manuscript = manuscript;
		this.userId = userId;
		this.document = document;
		

		textblocks = manuscript.getTextblocks();
		filebreaks = manuscript.getFilebreaks();
		outSpecs = new TextOutSpecs();

		PageSpecs pageSpecs = outSpecs.getPageSpecs();
		
		outSpecs.setCenteringFlag(false);
		outSpecs.setFontsize(10);
		outSpecs.setLineLeading(12);
		outSpecs.setLeftMargin((int) pageSpecs.getLeftMargin());
		outSpecs.setRightMargin(90);
//		outSpecs.setLine(1);
		outSpecs.setLineLeading(pageSpecs.getLineLeading());
		outSpecs.setOffset(outSpecs.getLeftMargin());
		outSpecs.setPageSpecs(pageSpecs);
		outSpecs.setParaIndent(pageSpecs.getParaIndent());
		outSpecs.setRightMargin((int)pageSpecs.getRightMargin());
		outSpecs.setTopMargin(pageSpecs.getTopMargin());

		// pdfLines vector will be populated in getPdfLines()
		getPdfLines(outSpecs);
		
		// vector populated, get an iterator for use in getPage(); each call to getPage will
		// advance deeper into the list; each call to getPage() ends when page is full, 
		// iterator stays pointing at next line for next call to getPage()
		pdfLinesI = pdfLines.iterator();
		pdfLine = pdfLinesI.next();
		tocItem = new String[]{ pdfLine.getText(), new Integer(pageCount + 1).toString() };
		tocItems.add(tocItem);
	}
	
	private void getPdfLines(TextOutSpecs outSpecs)
	{
		Vector<Integer> filebreaks = manuscript.getFilebreaks();
		Iterator<Integer> filebreaksI = filebreaks.iterator();
		Integer filebreak = filebreaksI.next();
		
		int textblockCount = 0;
		
		// convert each textblock in the manuscript to a collection of PdfLines, and consolidate all of them in a single vector
		Vector<PdfLine> pdfLinesIn;
		Iterator<String> textblocksI = textblocks.iterator();
		Iterator<PdfLine> linesInI = null;
		pdfLines = new Vector<PdfLine>();
		do
		{
			String textblock = textblocksI.next();
			textblockCount++;
			
			outSpecs.setOffset(outSpecs.getLeftMargin() + outSpecs.getParaIndent());
			PdfLines pdfLinesFactory = new PdfLines(textblock, outSpecs);
//			PdfLines pdfLinesFactory = new PdfLines(textblocksI.next(), outSpecs);
			pdfLinesIn = pdfLinesFactory.getPdfLines();
			linesInI = pdfLinesIn.iterator();
			do
			{
				pdfLine = linesInI.next();
				if(filebreak.compareTo(new Integer(textblockCount)) == 0)
				{
					pdfLine.pageStart = true;
					if(filebreaksI.hasNext())
						filebreak = filebreaksI.next();
				}
				
				pdfLines.add(pdfLine);
			}
			while(linesInI.hasNext());
		}
		while(textblocksI.hasNext());
		
	}
	
	public PDPage getPage()
	{
		// getPage() is called repeatedly in ContentPages.addToDocument() 
		// until getPage() returns null
		page = null;
		
		PDPageContentStream cos = null;
		outSpecs.getPageSpecs().resetVSpaceUsed();
		outSpecs.setGap(0);

		// line number, for page placement
		int bufferLines = 0;
		
		Vector<String[]> lineSegments = null;
		Iterator<String[]> lineSegmentsI = null;
		String[] lineSegment = null;
		while(pdfLinesI.hasNext() && !outSpecs.getPageSpecs().pageFull)
		{
			page = new PDPage(pageSize);
			try
			{
				cos = new PDPageContentStream(document, page);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			outSpecs.setCos(cos);
			
			// first, the footer (page number)
			String pageNumText = new Integer(++pageCount).toString();
			new PageFooter(outSpecs, pageNumText);
			
			// next, the header (book title)
			new PageHeader(outSpecs, manuscript.getIBookTitle());
			
			// iterate through pdfLines; vector was created in the constructor and remains
			// valid until all lines have been put to a page
			do
			{
				// does the line start a new paragraph?
				lineSegments = pdfLine.getLineSegments();
				lineSegmentsI = lineSegments.iterator();
				while(lineSegmentsI.hasNext())
				{
					lineSegment = lineSegmentsI.next();

					if(lineSegment[3] != null)
					{
						if(lineSegment[3].equals("paraStart"))
						{
							outSpecs.paraStart = true;
							outSpecs.setOffset(outSpecs.getLeftMargin() + outSpecs.getParaIndent());
						}
						else if(lineSegment[3].equals("lineStart"))
						{
							outSpecs.lineStart = true;
						}
					}
				}
				
				if(outSpecs.getText() != null && outSpecs.getText().length() > 0)
				{
					// for paragraph and line starts, increment the amount of vertical space used  
					// so that outSpecs.getTextOutVCoord() will return the correct vertical 
					// coordinate in the next textOut operation 
					outSpecs.getPageSpecs().incrementVSpaceUsed(outSpecs.getLineLeading() + outSpecs.getGap());
				}
				// process the line
				new ProcessLine(pdfLine, outSpecs);
				
				outSpecs.setOffset(outSpecs.getPageSpecs().getLeftMargin());

				pdfLine = pdfLinesI.next();
				if(pdfLine.pageStart)
				{
					outSpecs.getPageSpecs().pageFull = true;
					
					if(pageCount % 2 != 0)
						needToAddBlank = true;
					else
						needToAddBlank = false;
					
					tocItem = new String[]{ pdfLine.getText(), new Integer(pageCount + 1).toString() };
					tocItems.add(tocItem);
				}
			}
			// stop the loop when either page is full or all pdf lines have been used
			while(!outSpecs.getPageSpecs().pageFull && pdfLinesI.hasNext());
			try
			{
				cos.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return page;
	}
	
	public Vector<String[]> getTocItems()
	{
		return tocItems;
	}
}
