package com.iBook.datastore.ebook.pdf;

import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.Utilities;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class PrefacePages
{
	private int pageCount = 0;
	private Manuscript manuscript = null;
	private String userId = "";
	
	public PrefacePages(Manuscript manuscript, String userId, int pageCount)
	{
		this.manuscript = manuscript;
		this.pageCount = pageCount;
		this.userId = userId;
	}
	
	public int addToDocument(PDDocument document)
	{
		PDPage prefacePage = null;
		
		PrefacePage pageFactory = new PrefacePage(manuscript, userId, document, pageCount);
		
		do
		{
			prefacePage = pageFactory.getPage();
			if(prefacePage != null)
			{
				document.addPage(prefacePage);
				pageCount++;
			}
		}
		while(prefacePage != null);
		
		if(pageCount % 2 != 0)
		{
			PDPage blankPage = (new BlankPage(document, new TextOutSpecs(), Utilities.convertNumberToRomanNumeral(++pageCount)).getPage());
			document.addPage(blankPage);
		}
		return pageCount;
	}
}
