package com.iBook.datastore.ebook.pdf;

import com.iBook.datastore.ebook.BoilerplateFactory;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.Utilities;

import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

public class TOCPages
{
	private int pageCount = 0;
	private Manuscript manuscript = null;
	private PDRectangle pageSize = PDRectangle.LETTER;
	private String userId;
	private Vector<String[]> tocItems = null;
	
	public TOCPages(Manuscript manuscript, String userId, int pageCount)
	{
		this.manuscript = manuscript;
		this.pageCount = pageCount;
		this.userId = userId;
		this.pageCount = pageCount;
	}
	
	public int addToDocument(PDDocument document, Vector<String[]> tocItems)
	{
		this.tocItems = tocItems;
		PDPage tocPage = null;
		
		TOCPage pageFactory = new TOCPage(manuscript, userId, tocItems, document);
		PDPageTree pageList = document.getDocumentCatalog().getPages();
		PDPage contentPage = pageList.get(pageCount + 1);
		
		do
		{
			tocPage = pageFactory.getPage(pageCount);
			if(tocPage != null)
			{
				pageList.insertBefore(tocPage, contentPage);
				pageCount++;
			}
		}
		while(tocPage != null);
		
		// need to know if page just put to the document is odd or even; pageCount was incremented after the page was written,
		// so check modulo for pageCount - 1
		if((pageCount - 1) % 2 != 0)
		{
			PDPage blankPage = (new BlankPage(document, new TextOutSpecs(), Utilities.convertNumberToRomanNumeral(++pageCount)).getPage());
			pageList.insertBefore(blankPage, contentPage);
		}
		return pageCount;
	}
}
