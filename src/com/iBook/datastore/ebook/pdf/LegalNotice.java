package com.iBook.datastore.ebook.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

import com.iBook.datastore.ebook.BoilerplateFactory;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.Utilities;

public class LegalNotice
{
	private int pageCount = 0;
	
	private Manuscript manuscript = null;

	// define fonts---Times
	PDFontDatastore fontStore = new PDFontDatastore();
	
	private PDRectangle pageSize = PDRectangle.LETTER;
	
	private String userId;
	
	public LegalNotice(Manuscript manuscript, String userId, int pageCount)
	{
		this.manuscript = manuscript;
		this.userId = userId;
		
		// pageCount is number of pages already used; need to increment by one
		// when the first LegalNotice page outputs it to the pdf
		this.pageCount = pageCount;
	}
	public int addToDocument(PDDocument document)
	{
		BoilerplateFactory boilerplateFactory = new BoilerplateFactory(manuscript, userId);
		
		// boilerplate copyright text use \n\n as paragraph markers; replace with \n
		String legalNoticeText = Utilities.replaceChars(boilerplateFactory.doLegal(BoilerplateFactory.PubType.PDF), "\n\n", "\n", "all");
		PDFontDatastore fontStore = new PDFontDatastore();
		
		// font must be reset for each text block in order to get accurate offset/lineheight specs;
		
		// get the eBook default font; if the string sent as argument begins with 
		// an html tag, font style will be set accordingly, otherwise it will be plain. Copyright
		// text starts with <h1>, so initial font is bold
		PDFont font = fontStore.initializeTextFont(legalNoticeText);
		
		
		// create a new TextOut object for every line of text to be placed on the page
		TextOut textOut = null;
		
		// create page
		PDPage legalNoticePage = new PDPage(pageSize /* defaults to PDRectangle.LETTER */);
		PDPage blankPage = new PDPage(pageSize);
		
		// create area within page for data display
		PDRectangle displayRect = legalNoticePage.getMediaBox();
		
		// create one specs obj; use setters to update specs as needed for different fonts/font sizes/margins/etc.
		TextOutSpecs outSpecs = new TextOutSpecs();
		outSpecs.setPageSpecs(boilerplateFactory.getPageSpecs(BoilerplateFactory.PdfPage.LEGALNOTICE));
		
		outSpecs.setDisplayRect(displayRect);
	
		// add the title page to the document
		document.addPage(legalNoticePage);
		// add blank second page
		document.addPage(blankPage);
		
		PDPageContentStream cos = null;
		try
		{
			// create the title page's output stream
			cos = new PDPageContentStream(document, legalNoticePage);
			outSpecs.setCos(cos);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		// first, the page number
		String pageNumText = new Integer(++pageCount).toString();
		new PageFooter(outSpecs, pageNumText);

		// then the text
		new ProcessLines(legalNoticeText, outSpecs);

		try
		{
			cos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return pageCount;
	}
}
