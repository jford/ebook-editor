package com.iBook.datastore.ebook.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class PageHeader
{
	public PageHeader(TextOutSpecs outSpecs, String text)
	{
		float textWidth = 0;
		try
		{
			textWidth = (outSpecs.getFont().getStringWidth(text) / outSpecs.getGlyphScaler(outSpecs.getFontstyle())) * outSpecs.getFontsize();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		} 
		
		float offset = outSpecs.getPageRect().getWidth() - textWidth;
		float newLineVCoord = outSpecs.getPageHeight()
            	- (outSpecs.getTopMargin() / 2); 

		PDFontDatastore fontStore = new PDFontDatastore();
		PDFont font = fontStore.getFont(PDFontDatastore.fontNameItal);
		int fontsize = 8;
		PDPageContentStream cos = outSpecs.getCos();
		
		try
		{
			cos.setFont(font, fontsize);
			cos.beginText();
			cos.newLineAtOffset(offset, newLineVCoord);
			
			String debugClue = ""; // outSpecs.getPageSpecs().pageFull ? "pageFull flag = true, " : "pageFull flag = false, ";
//			debugClue += new Integer((int)offset).toString();
			
			cos.showText(debugClue + text);
			cos.endText();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}