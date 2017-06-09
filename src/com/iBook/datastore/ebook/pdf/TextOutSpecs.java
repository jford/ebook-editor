package com.iBook.datastore.ebook.pdf;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.common.*;

public class TextOutSpecs
{
	/*
	 * Create one TextOutSpecs object and reuse for
	 * multiple TextOut objects.
	 * 
	 * Once created, individual specs can be updated or not
	 * as needed.
	 */
	
	private PageSpecs pageSpecs = new PageSpecs();

	public boolean centeringFlag = false;
	public boolean followsHead = false;
	public boolean fontItal = false;
	public boolean fontBold = false;
	public boolean fontBoldItal = false;
	public boolean isBodyText = true;
	public boolean isListItem = true;
	public boolean lineStart = false;
	public boolean paraStart = false;

	private PDPageContentStream cos;
	private PDFont font = pageSpecs.getFont();
	
	private float fontsize = pageSpecs.getFontsize();
	private float lineLeading = pageSpecs.getLineLeading();
	private float bulletLevel = 0f;
	private float bulletIndent = 10f;
	
	private float bottomMargin = pageSpecs.getBottomMargin();
	private float leftMargin = pageSpecs.getLeftMargin();

	private float gap = 0;
	// The value of gap represents an arbitrary amount of space to insert 
	// between two lines of text, to allow extra space between a heading and the 
	// following body text.
	//
	// In ProcessLines(), the value of gap is added to the amount of the 
	// PageSpec's verticalSpaceUsed variable (and subtracted from verticalSpaceAvailable) 
	// following a TextOut operation:
	//
	// 		outSpecs.getPageSpecs().incrementVSpaceUsed(outSpecs.getGap() + outSpecs.getLineLeading());
	//
	// The next TextOut operation will use the sum of gap and line leading, as calculated 
	// during processing of the previous line, to determine the vCoord of the next line of text.  

	private float offset = leftMargin;
	private float paraIndent = pageSpecs.getParaIndent();
	private float rectHeight = pageSpecs.getPageHeight();
	private float rightMargin = pageSpecs.getRightMargin();
	private float topMargin = pageSpecs.getTopMargin();
	
	PDRectangle pageRect = pageSpecs.getPageRect();
	PDRectangle displayRect = pageRect;
	
	private String text;

	public TextOutSpecs()
	{
		// font should never be null, initialize to plain style, in system default font
		PDFontDatastore fontStore = new PDFontDatastore();
		font = fontStore.getFont(PDFontDatastore.fontNamePlain);
	}
	
	public float getTextOutVCoord()
	{
		// VCoord defines an imaginary line upon which the text to be output will be placed
		return getPageHeight() - getTopMargin() - getPageSpecs().getVSpaceUsed() - lineLeading;
	}
	
	public void setLineStartFlag(boolean lineStart)
	{
		this.lineStart = lineStart;
	}
	public boolean getLineStartFlag()
	{
		return lineStart;
	}
	
	public void setGap(float gap)
	{
		this.gap = gap;
	}
	
	public float getGap()
	{
		return gap;
	}
	
	public void setFollowsHeadFlag(boolean followsHead)
	{
		this.followsHead = followsHead;
	}
	public boolean getFollowsHeadFlag()
	{
		return followsHead;
	}
	
	public float getGlyphScaler(PDFontDatastore.Style fontstyle)
	{
		float scaler;

		PDFontDatastore fontStore = new PDFontDatastore();

		scaler = fontStore.getGlyphScaler(fontstyle);
		
		return scaler;
	}

	public float getMidpoint()
	{
		return pageRect.getWidth() / 2;
	}
	
	public void setParaIndent(float paraIndent)
	{
		this.paraIndent = paraIndent;
	}
	public float getParaIndent()
	{
		return paraIndent;
	}
	
	public PDRectangle getPageRect()
	{
		return pageRect;
	}
	public PDRectangle getDisplayRect()
	{
		// page rectangle
		return displayRect;
	}
	public void setDisplayRect(PDRectangle displayRect)
	{
		// page's media box rectangle
		this.displayRect = displayRect;
	}
	
	public float getPageHeight()
	{
		return rectHeight;
	}
	
	public void setPageSpecs(PageSpecs pageSpecs)
	{
		this.pageSpecs = pageSpecs;
	}
	public PageSpecs getPageSpecs()
	{
		return pageSpecs;
	}
	
	public void setParaStartFlag(boolean value)
	{
		paraStart = value;
	}
	public boolean getParaStartFlag()
	{
		return paraStart;
	}
	
	public PDFontDatastore.Style getFontstyle()
	{
		PDFontDatastore.Style fontstyle = null;
		String name = font.getName();
		if(name.toLowerCase().indexOf("bold") != -1 && 
			(name.toLowerCase().indexOf("ital") != -1 || 
			 name.toLowerCase().indexOf("oblique") != -1))
				fontstyle = PDFontDatastore.Style.BOLD_ITALIC;
		else if(name.toLowerCase().indexOf("bold") != -1)
			fontstyle = PDFontDatastore.Style.BOLD;
		else if(name.toLowerCase().indexOf("ital") != -1)
			fontstyle = PDFontDatastore.Style.ITALIC;
		else
			fontstyle = PDFontDatastore.Style.PLAIN;
		return fontstyle;
	}
	
	public void incrementBulletLevel()
	{
		// increment bullet-lest nesting level
		bulletLevel++;
		offset += bulletIndent;
	}
	public void decrementBulletLevel()
	{
		// decrement bullet-lest nesting level
		bulletLevel--;
		offset -= bulletIndent;
	}
	public float getBulletLevel()
	{
		return bulletLevel;
	}
	
	public void setBulletIndent(float bulletIndent)
	{
		// amount of space to add to offset for each bullete-list nesting level
		this.bulletIndent = bulletIndent;
	}
	public float getBulletIndent()
	{
		return bulletIndent;
	}
	
	public void setCenteringFlag(boolean value)
	{
		centeringFlag = value;
	}
	
	public void setTopMargin(float topMargin)
	{
		this.topMargin = topMargin;
	}
	public float getTopMargin()
	{
		return topMargin;
	}
	
	public void setBottomMargin(float bottomMargin)
	{
		this.bottomMargin = bottomMargin;
	}
	public float getBottomMargin()
	{
		return bottomMargin;
	}
	
	public void setLeftMargin(float leftMargin)
	{
		this.leftMargin = leftMargin;
	}
	public float getLeftMargin()
	{
		return leftMargin;
	}
	
	public void setRightMargin(float rightMargin)
	{
		this.rightMargin = rightMargin;
	}
	public float getRightMargin()
	{
		return rightMargin;
	}
	
	public void setFontsize(float fontsize)
	{
		this.fontsize = fontsize;
	}
	public float getFontsize()
	{
		return fontsize;
	}
	
	public void setOffset(float offset)
	{
		this.offset = offset;
	}
	public float getOffset()
	{
		return offset;
	}
	
	public void setLineLeading(float lineLeading)
	{
		this.lineLeading = lineLeading;
	}
	public float getLineLeading()
	{
		return lineLeading;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	public String getText()
	{
		return text;
	}
	
	public void setCos(PDPageContentStream cos)
	{
		this.cos = cos;
	}
	public PDPageContentStream getCos()
	{
		return cos;
	}
	
	public void setFont(PDFont font)
	{
		this.font = font;
		
		String fontname = font.getName();
		if(fontname.toLowerCase().indexOf("bold") != -1 && fontname.toLowerCase().indexOf("ital") != -1)
		{
			fontBoldItal = true;
			fontItal = false;
			fontBold = false;
		}
		else if(fontname.toLowerCase().indexOf("bold") != -1)
		{
			fontBold = true;
			fontItal = false;
			fontBoldItal = false;
		}
		else if(fontname.toLowerCase().indexOf("ital") != -1|| fontname.toLowerCase().indexOf("oblique") != -1)
		{
			fontItal = true;
			fontBold = false;
			fontBoldItal = false;
		}
		else
		{
			fontItal = false;
			fontBold = false;
			fontBoldItal = false;
		}
	}

	public PDFont getFont()
	{
		return font;
	}
}
