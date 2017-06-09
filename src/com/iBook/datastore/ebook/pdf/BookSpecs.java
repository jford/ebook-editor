package com.iBook.datastore.ebook.pdf;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

public class BookSpecs
{
	private PDFontDatastore fontStore = new PDFontDatastore();

	// dots per inch; roughly equivalent to a points/inch
	private float dpi = 72;

	// margins define the amount of space between the paper edge and the text area, in dpi (~points)
	private float bottomMargin = dpi * 1.5f;
	private float leftMargin = dpi;
	private float rightMargin = dpi;
	private float paraIndent = 15;
	private float topMargin = dpi;

	private float footerHCoord = topMargin - 30f;
	private float footerVCoord = bottomMargin + 30f;
	private float headerHCoord = topMargin - (dpi * 2);
	private float headerVCoord = rightMargin - 30f;

	private float fontsize = 10f;
	private float h1Fontsize = 36f;
	private float h2Fontsize = 22f;
	private float h3Fontsize = 14f;
	private float lineLeading = 11f;
	private float h1LineLeading = 38f;
	private float h2LineLeading = 24f;
	private float h3LineLeading = 16f;
	
	public enum TextType { H1, H2, H3, BODY };
	
	public BookSpecs()
	{
		
	}
	
	public float  getH1Fontsize()
	{
		return h1Fontsize;
	}
	
	public float getH2Fontsize()
	{
		return h2Fontsize;
	}
	
	public float getH3Fontsize()
	{
		return h3Fontsize;
	}
	
	public float getH1LineLeading()
	{
		return h1LineLeading;
	}
	
	public float getH2LineLeading()
	{
		return h2LineLeading;
	}
	
	public float getH3LineLeading()
	{
		return h3LineLeading;
	}
	
	public float getH1Gap()
	{
		return h1LineLeading * 2;
	}
	
	public float getH2Gap()
	{
		return h2LineLeading * 2;
	}
	
	public float getH3Gap()
	{
		return h3LineLeading * 2;
	}
	
	public float getGlyphToPdfScaler(PDFontDatastore.Style fontstyle)
	{
		return fontStore.getGlyphScaler(fontstyle);
	}
	
	public float getHeaderVCoord()
	{
		return headerVCoord;
	}
	
	public float getHeaderHCoord()
	{
		return headerHCoord;
	}
	
	public float getFooterVCoord()
	{
		return footerVCoord;
	}
	
	public float getFooterHCoord()
	{
		return footerHCoord;
	}
	
	public float getDpi()
	{
		return dpi;
	}
	
	public float getTopMargin()
	{
		return topMargin;
	}
	
	public float getParaIndent()
	{
		return paraIndent;
	}
	
	public PDFont getDefaultFont(TextType textType)
	{
		PDFont font;
		switch(textType)
		{
		case H1:
		case H2:
		case H3:
			font = fontStore.getFont(PDFontDatastore.defaultFontFace, PDFontDatastore.fontNameBold);
			break;
		case BODY:
		default:
			font = fontStore.getFont(PDFontDatastore.defaultFontFace, PDFontDatastore.fontNamePlain);
			break;
		}
		return font;
	}
	
	public float getLineLeading()
	{
		return lineLeading;
	}
	
	public float getRectHeight()
	{
		return getPageRect().getHeight();
	}
	
	public float getRectWidth()
	{
		return getPageRect().getWidth();
	}

	public PDRectangle getPageRect()
	{
		return PDRectangle.LETTER;
	}
	
	public float getBottomMargin()
	{
		return bottomMargin;
	}
	public float getFontsize()
	{
		return fontsize;
	}
	public float getLeftMargin()
	{
		return leftMargin;
	}
	public float getRightMargin()
	{
		return rightMargin;
	}
	
	

}
