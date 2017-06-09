package com.iBook.datastore.ebook.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class PageSpecs
{
	private BookSpecs bookSpecs = new BookSpecs();

	public boolean pageFull = false;
	
	// margins define the amount of space between the paper edge and the text area, in dpi (~points)
	// dots per inch; roughly equivalent to a points/inch
	private float bottomMargin = bookSpecs.getBottomMargin();
	private float dpi = bookSpecs.getDpi();
	private float footerHCoord = bookSpecs.getFooterHCoord();
	private float footerVCoord = bookSpecs.getFooterVCoord();
	private float headerHCoord = bookSpecs.getHeaderHCoord();
	private float headerVCoord = bookSpecs.getHeaderVCoord();
	private float leftMargin = bookSpecs.getLeftMargin();
	private float paraIndent = bookSpecs.getParaIndent();
	private float rectHeight = bookSpecs.getRectHeight();
	private float rightMargin = dpi;
	private float topMargin = dpi;
	// veticalSpaceAvailable: number of points between bottom margin 
	// and last line of text on the page, not counting page number or other footer text if any
	private float verticalSpaceAvailable = rectHeight - topMargin - bottomMargin;
	private float verticalSpaceUsed = 0;
	
	private float fontsize = bookSpecs.getFontsize();
	private float lineLeading = bookSpecs.getLineLeading();
	
	private PDFont font = bookSpecs.getDefaultFont(BookSpecs.TextType.BODY);
	private PDRectangle rect = bookSpecs.getPageRect();

	public PageSpecs()
	{
	}
	
	public BookSpecs getBookSpecs()
	{
		return bookSpecs;
	}
	
	public void setHeaderVCoord(float headerVCoord)
	{
		this.headerVCoord = headerVCoord;
	}
	
	public float getHeaderVCoord()
	{
		return headerVCoord;
	}
	
	public float getHeaderHCoord()
	{
		return headerHCoord;
	}
	
	public void setHeaderHCoord(float headerHCoord)
	{
		this.headerHCoord = headerHCoord;
	}
	
	public float getFooterVCoord()
	{
		return footerVCoord;
	}
	
	public void setFooterVCoord(float footerVCoord)
	{
		this.footerVCoord = footerVCoord;
	}
	
	public float getFooterHCoord()
	{
		return footerHCoord;
	}
	
	public void setFooterHCoord(float footerHCoord)
	{
		this.footerHCoord = footerHCoord;
	}
	
	public float getPageHeight()
	{
		return rect.getHeight();
	}
	public float getPageWidth()
	{
		return rect.getWidth();
	}
	
	public void setPageRect(PDRectangle rect)
	{
		this.rect = rect;
	}
	public PDRectangle getPageRect()
	{
		return rect;
	}
	
	public void setFont(PDFont font)
	{
		this.font = font;
	}
	public PDFont getFont()
	{
		return font;
	}
	
	public void setFontsize(int fontsize)
	{
		this.fontsize = fontsize;
	}
	public float  getFontsize()
	{
		return fontsize;
	}

	public void setLineLeading(int lineLeading)
	{
		this.lineLeading = lineLeading;
	}
	public float getLineLeading()
	{
		return lineLeading;
	}

	public void setParaIndent(float paraIndent)
	{
		this.paraIndent = paraIndent;
	}
	public float getParaIndent()
	{
		return paraIndent;
	}
	
	public float getVSpaceAvailable()
	{
		return verticalSpaceAvailable;
	}
	
	public float getVSpaceUsed()
	{
		return verticalSpaceUsed;
	}
	
	public void incrementVSpaceUsed(float spaceUsed)
	{
		// space used should be one line of text in the current font:
		//
		//     spaceUsed = (PDFont.getHeight(...))
		//
		// ProcessLine() increments the spaceUsed counter by the value of outSpecs.getLineLeading()
		// for each line of text 
		
		verticalSpaceAvailable -= spaceUsed;
		verticalSpaceUsed += spaceUsed;
		
		// if not enough space available at current line leading, pageFull is true, otherwise false
		pageFull = verticalSpaceAvailable < lineLeading ? true : false;
		
		// if page is full, reset vSpace counter for next page 
		if(pageFull)
			verticalSpaceAvailable = rect.getHeight() - topMargin - bottomMargin;
	}
	
	public void resetVSpaceUsed()
	{
		verticalSpaceUsed = 0;
		verticalSpaceAvailable = rectHeight - topMargin - bottomMargin;
		pageFull = false;
	}
	
	public void setTopMargin(float marginSize)
	{
		// margins defined in dpi
		topMargin = marginSize;
	}
	public float getTopMargin()
	{
		// margins defined in dpi
		return topMargin;
	}

	public void setBottomMargin(float marginSize)
	{
		// margins defined in dpi
		bottomMargin = marginSize;
	}
	public float getBottomMargin()
	{
		// margins defined in dpi
		return bottomMargin;
	}

	public void setLeftMargin(float marginSize)
	{
		// margins defined in dpi
		leftMargin = marginSize;
	}
	public float getLeftMargin()
	{
		// margins defined in dpi
		return leftMargin;
	}

	public void setRightMargin(float marginSize)
	{
		// margins defined in dpi
		rightMargin = marginSize;
	}
	public float getRightMargin()
	{
		// margins defined in dpi
		return rightMargin;
	}
}
