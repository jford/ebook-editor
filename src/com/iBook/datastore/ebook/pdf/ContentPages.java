package com.iBook.datastore.ebook.pdf;

import com.iBook.datastore.manuscripts.Manuscript;
import com.iBook.utilities.Utilities;

import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class ContentPages
{
	private Manuscript manuscript = null;
	private String userId = "";
	private Vector<String[]> tocItems = null;

	public ContentPages(Manuscript manuscript, String userId)
	{
		this.manuscript = manuscript;
		this.userId = userId;
	}
	public int addToDocument(PDDocument document)
	{
		PDPage contentPage = null;
		int pageCount = 0;
		ContentPage pageFactory = new ContentPage(manuscript, userId, document);
		do
		{
			contentPage = pageFactory.getPage();
			if(contentPage != null)
			{
				document.addPage(contentPage);
				pageCount++;
				
				// chapter end on an odd page?
				if(pageFactory.needToAddBlank)
				{
					PDPage blankPage = (new BlankPage(document, new TextOutSpecs(), new Integer(++pageCount).toString()).getPage());
					document.addPage(blankPage);
					pageFactory.needToAddBlank = false;
				}
			}
		}
		while(contentPage != null);
		
		// last page odd or even? (pageCount was incremented after the last page was added, so need to check module for pageCount - 1
		if((pageCount - 1) % 2 != 0)
		{
			PDPage blankPage = (new BlankPage(document, new TextOutSpecs(), new Integer(pageCount++).toString()).getPage());
			document.addPage(blankPage);
			pageCount++;
		}
		tocItems = pageFactory.getTocItems();
		return pageCount;
	}
	public Vector<String[]> getTocItems()
	{
		return tocItems;
	}
}
