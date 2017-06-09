package com.iBook.datastore.ebook.pdf;

import org.apache.pdfbox.pdmodel.*;

public class TextOut
{
	/*
	 * Create a new TextOut object for each cos.showText() call.
	 */
	public TextOut(TextOutSpecs outSpecs)
	{
		PDPageContentStream cos = outSpecs.getCos();
		float bulletLevel = outSpecs.getBulletLevel();
		float bulletIndent = outSpecs.getBulletIndent() * bulletLevel;
		String bulletChar = "";
		String text = outSpecs.getText();
		
		float offset = outSpecs.getOffset();
		float gap = outSpecs.getGap();

		float textOutVCoord = outSpecs.getTextOutVCoord();
		if(text != null)
		{
			if(bulletLevel > 0)
			{
				offset += bulletIndent;
				try
				{
					bulletChar = bulletLevel == 1 ? "\u2022" : "\u2013";
					cos.beginText();
					cos.newLineAtOffset(offset - 5, textOutVCoord);
					cos.showText(bulletChar);
					cos.endText();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			try
			{
				cos.setFont(outSpecs.getFont(), outSpecs.getFontsize());
				cos.beginText();
				cos.newLineAtOffset(offset, textOutVCoord);
				
				String debugClue = ""; // outSpecs.getPageSpecs().pageFull ? "pageFull flag = true, " : "pageFull flag = false, ";
//				debugClue += new Integer((int)offset).toString();
				
				cos.showText(debugClue + text);
				cos.endText();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
