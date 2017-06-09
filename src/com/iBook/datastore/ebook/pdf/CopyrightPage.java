package com.iBook.datastore.ebook.pdf;

import com.iBook.datastore.ebook.BoilerplateFactory;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import com.iBook.datastore.manuscripts.Manuscript;
import com.iBook.utilities.Utilities;

public class CopyrightPage
{
	private int pageCount = 0;
	
	private Manuscript manuscript = null;

	// define fonts---Times
	PDFontDatastore fontStore = new PDFontDatastore();
	
	private String userId;
	
	public CopyrightPage(Manuscript manuscript, String userId, int pageCount)
	{
		this.manuscript = manuscript;
		this.userId = userId;
		this.pageCount = pageCount;
	}
	
	public int addToDocument(PDDocument document)
	{
		BoilerplateFactory boilerplateFactory = new BoilerplateFactory(manuscript, userId);
		
		String copyrightText = boilerplateFactory.doCopyright(BoilerplateFactory.PubType.PDF);

		TextOutSpecs outSpecs = new TextOutSpecs();
		
		PDPage copyrightPage = null;
		PDPageContentStream cos = null;
		PDRectangle displayRect = outSpecs.getPageSpecs().getPageRect();
		
		outSpecs.setDisplayRect(displayRect);
		outSpecs.setPageSpecs(boilerplateFactory.getPageSpecs(BoilerplateFactory.PdfPage.COPYRIGHT));
		
		try
		{
			// create the page and its output stream
			copyrightPage = new PDPage(outSpecs.getPageRect());
			cos = new PDPageContentStream(document, copyrightPage);
			outSpecs.setCos(cos);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		// footer...
		String pageNumText = Utilities.convertNumberToRomanNumeral(++pageCount);
		new PageFooter(outSpecs, pageNumText);
		
		// ...text
		new ProcessLines(copyrightText, outSpecs);

		// add the page to the document
		document.addPage(copyrightPage);
		
		BlankPage blankPage = new BlankPage(document, outSpecs, Utilities.convertNumberToRomanNumeral(++pageCount));
		document.addPage(blankPage.getPage());
		
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
