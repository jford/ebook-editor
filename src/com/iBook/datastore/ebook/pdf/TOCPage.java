package com.iBook.datastore.ebook.pdf;

import com.iBook.datastore.manuscripts.Manuscript;
import com.iBook.utilities.Utilities;

import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class TOCPage
{
	// only need title on first toc page
	private boolean isFirstTocPage = true;
	
	private PDDocument document;
	private PDPage page;
	private PDRectangle pageSize = PdfObj.pageSize;
	
	private Manuscript manuscript;

	private String userId;
	private String[] tocItem = null;
	
	private TextOutSpecs outSpecs;
	
	private Vector<String[]> tocItems = null;
	private Iterator<String[]> tocItemsI = null;
	
	public TOCPage(Manuscript manuscript, String userId, Vector<String[]> tocItems, PDDocument document)
	{
		this.manuscript = manuscript;
		this.userId = userId;
		this.tocItems = tocItems;
		this.document = document;
		
		tocItemsI = tocItems.iterator();
	}
	
	public PDPage getPage(int pageCount)
	{
		float lineLeading;
		float textWidth;
		
		int idx = 0;
		int wordCount = 0;
		
		String text = "";
		
		// create page
		page = null;		
		PDPageContentStream cos = null;
		
		TextOutSpecs outSpecs = new TextOutSpecs();
		outSpecs.setCos(cos);
		
		outSpecs.getPageSpecs().resetVSpaceUsed();
		outSpecs.setGap(0);
		outSpecs.setLeftMargin(outSpecs.getPageSpecs().getLeftMargin() + 50);
		outSpecs.setRightMargin(outSpecs.getPageSpecs().getRightMargin() + 50);

		if(tocItemsI.hasNext())
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
			new PageFooter(outSpecs, Utilities.convertNumberToRomanNumeral(pageCount));
			
			if(isFirstTocPage)
			{
				text = "Contents";
				outSpecs.setText(text);
				outSpecs.setFontsize(outSpecs.getPageSpecs().getBookSpecs().getH1Fontsize());
				outSpecs.setLineLeading(outSpecs.getPageSpecs().getBookSpecs().getH1LineLeading());
				textWidth = getWidth(text, outSpecs);
				outSpecs.setOffset(outSpecs.getMidpoint() - (textWidth/2));
				new TextOut(outSpecs);
				outSpecs.getPageSpecs().incrementVSpaceUsed(outSpecs.getLineLeading() * 2);
				
				// reset for text
				outSpecs.setFontsize(outSpecs.getPageSpecs().getFontsize());
				outSpecs.setLineLeading(outSpecs.getPageSpecs().getLineLeading());
			}
			
			// iterate through tocItems; vector was created in the constructor and remains
			// valid until all items have been put to a page
			do
			{
				tocItem = tocItemsI.next();

				String tocText = tocItem[0];
				while((idx = tocText.indexOf(" ")) != -1)
				{
					if(wordCount++ > 10)
						break;
				}
				if(idx > -1)
					tocText = tocText.substring(0, idx);
				String tocPageRef = tocItem[1];
				outSpecs.setText(tocItem[0]);
				outSpecs.setOffset(outSpecs.getLeftMargin());
				new TextOut(outSpecs);
				
				outSpecs.setText(tocPageRef);
				outSpecs.setOffset(outSpecs.getPageSpecs().getPageWidth() - outSpecs.getRightMargin() - getWidth(tocPageRef, outSpecs));
				new TextOut(outSpecs);
		
				lineLeading = outSpecs.getLineLeading();
				outSpecs.getPageSpecs().incrementVSpaceUsed(lineLeading);
				
			}
			// stop the loop when either page is full or all pdf lines have been used
			while(!outSpecs.getPageSpecs().pageFull && tocItemsI.hasNext());

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
	private float getWidth(String text, TextOutSpecs outSpecs)
	{
		float width = 0;
		try
		{
			width = (outSpecs.getFont().getStringWidth(text) / outSpecs.getGlyphScaler(outSpecs.getFontstyle())) * outSpecs.getFontsize();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return width;
	}
}
