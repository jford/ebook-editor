package com.iBook.datastore.ebook.pdf;

import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;

public class TitlePage
{
	private int  pageCount = 0;
			
	private Manuscript manuscript = null;

	// define fonts---Times
	PDFontDatastore fontStore = new PDFontDatastore();
	
	public TitlePage(Manuscript manuscript, int pageCount)
	{
		this.manuscript = manuscript;
		this.pageCount = pageCount;
	}
	
	public int addToDocument(PDDocument document)
	{
		int numTitlePages = 2;
		
		String titleText = manuscript.getIBookTitle();
		String authorText = manuscript.getIBookAuthor();

		TextOutSpecs outSpecs = new TextOutSpecs();
		
		PDFontDatastore fontStore = new PDFontDatastore();
		PDPage titlePage = null;
		PDPageContentStream cos = null;
		PDRectangle displayRect = outSpecs.getPageSpecs().getPageRect();
		
		outSpecs.setDisplayRect(displayRect);
		// set margins for title page at 150% of default margins
		outSpecs.setLeftMargin(outSpecs.getLeftMargin() + 37f);
		outSpecs.setRightMargin(outSpecs.getRightMargin() + 45f);
		
		for(int count = 0; count < numTitlePages; count++)
		{
			Utilities.convertNumberToRomanNumeral(++pageCount);
			try
			{
				// create the page and its output stream
				titlePage = new PDPage(outSpecs.getPageRect());
				cos = new PDPageContentStream(document, titlePage);
				outSpecs.setCos(cos);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			if(pageCount == 1)
			{
				// title
				outSpecs.setFontsize(36);
				outSpecs.setLineLeading(38);
				outSpecs.isBodyText = false;
				new ProcessLines("<h1>" + titleText + "</h1>\n", outSpecs);
				
				// author
				outSpecs.getPageSpecs().incrementVSpaceUsed(100);
				new ProcessLines("<h2><center> By " + authorText + "</center></h2>\n", outSpecs);
				
				outSpecs.setFontsize(18);
				outSpecs.setFont(fontStore.getFont(PDFontDatastore.fontNameItal));
				new PageFooter(outSpecs, "An ElectraInk Book");
			}
			
			// add the page to the document
			document.addPage(titlePage);
			try
			{
				cos.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			titleText = "";
		}
		return numTitlePages;
	}
}
