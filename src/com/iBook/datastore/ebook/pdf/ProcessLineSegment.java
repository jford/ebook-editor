package com.iBook.datastore.ebook.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;

public class ProcessLineSegment
{
	private PDFontDatastore fontStore = new PDFontDatastore();
	
	public ProcessLineSegment(String[] lineSegment, TextOutSpecs outSpecs)
	{

		float gap = outSpecs.getGap();
		float textWidth = 0;
		
		String text = lineSegment[0];
		outSpecs.setText(text);
		
		// text marked for centering?
		if(outSpecs.centeringFlag)
		{
			// yes, calculate centering offset
			try
			{
				textWidth = (outSpecs.getFont().getStringWidth(text) / outSpecs.getGlyphScaler(outSpecs.getFontstyle())) * outSpecs.getFontsize();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			} 
			// set the new offset
			outSpecs.setOffset((outSpecs.getDisplayRect().getWidth()/2) - (textWidth/2));
		}
		
		TextOut textOut = null;
		// text should never be null, but just in 
		// case---test it (0 length is a real possibility)
		if(text != null && text.length() > 0)
		{
			// output text from current line segment
			textOut = new TextOut(outSpecs);
		}

        // calculate offset/gap for the next lineSegment
		
		// center it?
		if(lineSegment[2] != null && (lineSegment[2].equals("h1") || lineSegment[2].equals("center")))
		{
			// yes
			outSpecs.setOffset((outSpecs.getDisplayRect().getWidth()/2) - (textWidth/2));
		}
		else
		{
			// no, not being centered; calculate new offset for next segment, if the just-processed segment contained text to be output...
			if(lineSegment[0] != null)
			{
				try
				{
					textWidth = (outSpecs.getFont().getStringWidth(lineSegment[0]) / outSpecs.getGlyphScaler(outSpecs.getFontstyle())) * outSpecs.getFontsize();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			outSpecs.setOffset(outSpecs.getOffset() + textWidth);
		}
		if(lineSegment[2] != null)
		{
			if(lineSegment[2].equals("h1") || lineSegment[2].equals("h2") || lineSegment[2].equals("h3"))
			{
				outSpecs.isBodyText = false;
				if(lineSegment[2].equals("h1"))
				{
					outSpecs.setCenteringFlag(true);
					outSpecs.setFontsize(outSpecs.getPageSpecs().getBookSpecs().getH1Fontsize());
					outSpecs.setLineLeading(outSpecs.getPageSpecs().getBookSpecs().getH1LineLeading());
				}
				else if(lineSegment[2].equals("h2"))
				{
					outSpecs.setFontsize(outSpecs.getPageSpecs().getBookSpecs().getH2Fontsize());
					outSpecs.setLineLeading(outSpecs.getPageSpecs().getBookSpecs().getH2LineLeading());
				}
				else if(lineSegment[2].equals("h3"))
				{
					outSpecs.setFontsize(outSpecs.getPageSpecs().getBookSpecs().getH2Fontsize());
					outSpecs.setLineLeading(outSpecs.getPageSpecs().getBookSpecs().getH2LineLeading());
				}
			}
			else if(lineSegment[2].equals("/h1"))
			{ 
				outSpecs.isBodyText = true;
				outSpecs.setCenteringFlag(false);
				outSpecs.setParaStartFlag(true);
				outSpecs.setGap(outSpecs.getPageSpecs().getBookSpecs().getH1Gap());
			}
			else if(lineSegment[2].equals("/h2"))
			{
				outSpecs.isBodyText = true;
				outSpecs.setCenteringFlag(false);
				outSpecs.setParaStartFlag(true);
				outSpecs.setGap(outSpecs.getPageSpecs().getBookSpecs().getH2Gap());
			}
			else if(lineSegment[2].equals("/h3"))
			{
				outSpecs.isBodyText = true;
				outSpecs.setCenteringFlag(false);
				outSpecs.setParaStartFlag(true);
				outSpecs.setGap(outSpecs.getPageSpecs().getBookSpecs().getH3Gap());
			}
			else if(lineSegment[2].equals("center"))
			{
				outSpecs.setCenteringFlag(true);
//				outSpecs.setGap(outSpecs.getPageSpecs().getBookSpecs().getH2Gap());
			}
			else if(lineSegment[2].equals("/center"))
			{
				outSpecs.setCenteringFlag(false);
//				outSpecs.setGap(outSpecs.getPageSpecs().getBookSpecs().getH3Gap());
			}
			else if(lineSegment[2].equals("ul"))
			{
				outSpecs.incrementBulletLevel();
			}
			else if(lineSegment[2].equals("/ul"))
			{
				outSpecs.decrementBulletLevel();
			}
			else if(lineSegment[2].equals("li"))
				outSpecs.isListItem = true;
			else if(lineSegment[2].equals("/li"))
				outSpecs.isListItem = false;
				
		}
		setFont(lineSegment, outSpecs);
	}
	
	private void setFont(String[] lineSegment, TextOutSpecs outSpecs)
	{
		// html tags were interpreted in PdfLine, while constructing lineSegment[] arrays;
		// lineSegment[1] contains a string identifying which font style to use;
		// font-style strings are defined in PDFontDatastore
		
		PDFont font = null;
		
		// retrieve font for title heading from the font store
		if(lineSegment[2] != null && lineSegment[2].equals("h1"))
			font = fontStore.getFont(PDFontDatastore.fontNamePlain);
		else
		{
			// retrieve font for body-text style from the font store
			if(lineSegment[1] != null)
			{
				if(lineSegment[1].equals(PDFontDatastore.fontNameItal))
					font = outSpecs.fontBold ? fontStore.getFont(PDFontDatastore.fontNameBoldItal) : fontStore.getFont(PDFontDatastore.fontNameItal);
				
				if(lineSegment[1].equals(PDFontDatastore.fontNameBold))
					font = outSpecs.fontItal ? fontStore.getFont(PDFontDatastore.fontNameBoldItal) : fontStore.getFont(PDFontDatastore.fontNameBold);
					
				if(lineSegment[1].equals(PDFontDatastore.fontNameBoldItal))
					font = fontStore.getFont(PDFontDatastore.fontNameBoldItal);
				
				if(lineSegment[1].equals(PDFontDatastore.fontNamePlain))
					font = fontStore.getFont(PDFontDatastore.fontNamePlain);
			}
		}
		// if a font was retrieved from the font store, set the outSpecs font
		if(font != null)
			outSpecs.setFont(font);
	}
}
