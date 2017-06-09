package com.iBook.datastore.ebook.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class PageFooter
{
	public PageFooter(TextOutSpecs outSpecs, String text)
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
		
		float offset = (outSpecs.getPageRect().getWidth() - textWidth) / 2;
		float newLineVCoord = outSpecs.getTopMargin() / 2; 

		PDPageContentStream cos = outSpecs.getCos();
		
		try
		{
			cos.setFont(outSpecs.getFont(), outSpecs.getFontsize());
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
