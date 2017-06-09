package com.iBook.datastore.ebook.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class BlankPage
{
	private PDPage blankPage = null;
	
	public BlankPage(PDDocument document, TextOutSpecs outSpecs, String pageNum)
	{
		try
		{
			// create the page and its output stream
			blankPage = new PDPage(outSpecs.getPageRect());
			PDPageContentStream cos = new PDPageContentStream(document, blankPage);
			outSpecs.setCos(cos);

			new PageFooter(outSpecs, pageNum);
			cos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public PDPage getPage()
	{
		return blankPage;
	}
}
