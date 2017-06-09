package com.iBook.datastore.ebook.pdf;

import com.iBook.datastore.PropsManager;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.*;

import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class PdfObj
{
	private String outputFilename = "";
	
	private Vector<String[]> tocItems = new Vector<String[]>();
	
	public static PDRectangle pageSize = PDRectangle.LETTER;

	public PdfObj(Manuscript manuscript, String userId)
	{
		PropsManager propsMgr = new PropsManager(userId);
		String path_to_user_dir = PropsManager.getPathToUserDir();
		String pathMrkr = PropsManager.getPathMarker();
		String coverArtFilename = propsMgr.getCoverArtFilename(manuscript.getId());

		String[] tocItem = null;
		
		// number of pages added to doc
		int pageCount = 0;
		
		String bookTitle = manuscript.getIBookTitle(); 
		outputFilename = Utilities.replaceChars(bookTitle, " ", "_", "all") + ".pdf";
		
		PDDocument document = new PDDocument();

		// page number starts at title page; cover page and backside page are unnumbered
//		CoverPage coverPage = null;
		if(coverArtFilename.length() > 0)
		{
//			coverPage = new CoverPage(path_to_user_dir + "manuscripts" + pathMrkr + coverArtFilename, document);
			new CoverPage(path_to_user_dir + "manuscripts" + pathMrkr + coverArtFilename, document);
		}
		
		// title, copyright, and preface pagenumbering is cumulative
		// across all three files, beginning with i for title page
		// so number of pages consumed is passed into each constructor
 		TitlePage titlePage = new TitlePage(manuscript, pageCount);
 		// titlePage.addToDocument() returns number of pages used so far
		pageCount = titlePage.addToDocument(document);
		
		CopyrightPage copyrightPage = new CopyrightPage(manuscript, userId, pageCount);
		// copyrightPage.addToDocument() increments pageCount for each page added and returns the total number of pages used so far
		pageCount = copyrightPage.addToDocument(document);
		tocItem = new String[]{"Copyright", Utilities.convertNumberToRomanNumeral(pageCount)};
		tocItems.add(tocItem);

		tocItem = new String[]{"Preface", Utilities.convertNumberToRomanNumeral(pageCount + 2)};
		tocItems.add(tocItem);
		PrefacePages prefacePages = new PrefacePages(manuscript, userId, pageCount);
		// prefacePages.addToDocument() increments pageCount for each page added and returns the total number of pages used so far
		pageCount = prefacePages.addToDocument(document);
		
		// toc constructor needs pageCount + 1 as marker where to insert toc pages when generated, after content pages processed
		TOCPages toc = new TOCPages(manuscript, userId, pageCount + 1);
		
		// content pages start at one; page count is reset to 0...
		pageCount = 0;

		// ...and not passed into ContentPages constructor
		ContentPages contents = new ContentPages(manuscript, userId);
		// contents.addToDocument() returns number of pages added + 1, not total number of pages
		pageCount += contents.addToDocument(document);
		
		// pageCount is used for page numbering as each page is output to the PDF, then it is incremented
		// result is 1 count more than actual pages; need to adjust before pageCount gets used again
		pageCount--;
		
		Vector<String[]> contentsTocItems = contents.getTocItems();
		Iterator<String[]> contentsTocItemsI = contentsTocItems.iterator();
		tocItem = new String[2];

		while(contentsTocItemsI.hasNext())
		{
			tocItem = contentsTocItemsI.next();
			tocItems.add(tocItem);
		}
		
		// legal notice page numbering follows content pages, so pass in pageCount 
		LegalNotice legalNotice = new LegalNotice(manuscript, userId, pageCount);
		// ...get total number of pages used in the content section
		pageCount = legalNotice.addToDocument(document);
		tocItem = new String[]{"Legal Disclaimer", new Integer(pageCount).toString()};
		tocItems.add(tocItem);
		
		// toc.addToDocument() returns number of pages used prior to first content page
		// after toc has been generated and inserted into the document
		pageCount = toc.addToDocument(document, tocItems);
		
		try
		{
			document.save(path_to_user_dir + outputFilename);
			document.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getOutputFilename()
	{
		// name of the file to be written, without path
		return outputFilename;
	}

}
