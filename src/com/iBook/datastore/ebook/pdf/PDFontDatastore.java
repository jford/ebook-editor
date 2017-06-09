package com.iBook.datastore.ebook.pdf;

import org.apache.pdfbox.pdmodel.font.*;

public class PDFontDatastore
{
	// define fonts---Times
	private PDFont fontBold = PDType1Font.TIMES_BOLD;
	private PDFont fontBoldItal = PDType1Font.TIMES_BOLD_ITALIC;
	private PDFont fontItal = PDType1Font.TIMES_ITALIC;
	private PDFont fontPlain = PDType1Font.TIMES_ROMAN;
	
	public enum Face {TIMES, HELVETICA, COURIER};
	public enum Style {BOLD, ITALIC, BOLD_ITALIC, PLAIN};
	
	private Face defaultFace = Face.TIMES;
	
	public static String fontFamilyTimes = "times";
	public static String fontFamilyHelvetica = "helvetica";
	public static String fontFamilyCourier = "courier";
	
	public static String fontNamePlain = "plain";
	public static String fontNameItal = "italics";
	public static String fontNameBold = "bold";
	public static String fontNameBoldItal = "boldItalics";
	
	public static String defaultFontFace = fontFamilyTimes;
	
//	private final float GLYPH_TO_PDF_SCALE = 1000f;

	public PDFontDatastore()
	{
	}
	
	public float getGlyphScaler(Style fontstyle)
	{
		// 1000 seems to be working now, but if it ever needs 
		// changing, change it here...
		
		float multiplier = 1f;
		
		switch(fontstyle)
		{
		case PLAIN:
		case BOLD:
		case BOLD_ITALIC:
		case ITALIC:
		default:
			break;
		}
		return 1000 * multiplier;
	}
	
	public PDFont getFont(String fontName)
	{
		// if only the font name is specified, as a string,
		// font returned will be in Times family
		Style style = null;
		
		if(fontName.equals(fontNamePlain))
			style = Style.PLAIN;
		if(fontName.equals(fontNameBold))
			style = Style.BOLD;
		if(fontName.equals(fontNameBoldItal))
			style = Style.BOLD_ITALIC;
		if(fontName.equals(fontNameItal))
			style = Style.ITALIC;
			
		return getFont(defaultFace, style);
	}
	
	public PDFont getFont(String face, String style)
	{
		// font specs as strings; pass-through to getFont() with Style enums
		Face fontFace = null;
		Style fontStyle = null;
		
		if(face.equals(fontFamilyTimes))
			fontFace = Face.TIMES;
		if(face.equals(fontFamilyHelvetica))
			fontFace = Face.HELVETICA;
		if(face.equals(fontFamilyCourier))
			fontFace = Face.COURIER;

		if(style.equals(fontNamePlain))
			fontStyle = Style.PLAIN;
		if(style.equals(fontNameBold))
			fontStyle= Style.BOLD;
		if(style.equals(fontNameBoldItal))
			fontStyle = Style.BOLD_ITALIC;
		if(style.equals(fontNameItal))
			fontStyle = Style.ITALIC;
			
		return getFont(fontFace, fontStyle);
	}
	
	public PDFont getFont(Face fontFace, Style style)
	{
		// font face and style specified as enums
		PDFont font = null;
		switch(fontFace)
		{
		case HELVETICA:
			switch(style)
			{
			case BOLD:
				font = PDType1Font.HELVETICA_BOLD;
				break;
			case ITALIC:
				font = PDType1Font.HELVETICA_OBLIQUE;
				break;
			case BOLD_ITALIC:
				font = PDType1Font.HELVETICA_BOLD_OBLIQUE;
				break;
			case PLAIN:
			default:
				font = PDType1Font.HELVETICA;
			}
		case COURIER:
			switch(style)
			{
			case BOLD:
				font = PDType1Font.COURIER_BOLD;
				break;
			case ITALIC:
				font = PDType1Font.COURIER_OBLIQUE;
				break;
			case BOLD_ITALIC:
				font = PDType1Font.COURIER_BOLD_OBLIQUE;
				break;
			case PLAIN:
			default:
				font = PDType1Font.COURIER;
			}
		case TIMES:
			switch(style)
			{
			case BOLD:
				font = PDType1Font.TIMES_BOLD;
				break;
			case ITALIC:
				font = PDType1Font.TIMES_ITALIC;
				break;
			case BOLD_ITALIC:
				font = PDType1Font.TIMES_BOLD_ITALIC;
				break;
			case PLAIN:
			default:
				font = PDType1Font.TIMES_ROMAN;
			}
		}
		return font;
	}
	
	public PDFont initializeTextFont(String textblock)
	{
		// returns default font---TIMES family
		PDFont font = fontPlain;
		
		// font style depends on whether the textblock used for the initialization begins with a tag 
		String[] tags = { "<i>", "<b>", "<h1>", "<h2>", "<h3>" };
		PDFont[] fonts = { fontItal, fontBold, fontBold, fontBoldItal, fontBoldItal};
		for(int count = 0; count < tags.length; count++)
		{
			if(textblock.toLowerCase().startsWith(tags[count]))
				font = fonts[count];
		}
		return font;
	}
	
	public float getTextWidth(TextOutSpecs outSpecs)
	{
		// requires specs to define font, fontsize, and text 
		float width = 0;
		try
		{
			width = (outSpecs.getFont().getStringWidth(outSpecs.getText()) / getGlyphScaler(outSpecs.getFontstyle())) * outSpecs.getFontsize();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return width;
	}
}
