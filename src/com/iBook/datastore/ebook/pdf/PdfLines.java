package com.iBook.datastore.ebook.pdf;

import java.util.*;

import com.iBook.utilities.Utilities;

import org.apache.pdfbox.pdmodel.common.*;
import org.apache.pdfbox.pdmodel.font.*;


public class PdfLines
{
	Vector<PdfLine> pdfLines = null;
	PDFontDatastore fontStore = new PDFontDatastore();
	
// textblock with line breaks accepted
	
	public PdfLines(String textblockIn, TextOutSpecs outSpecs)
	{
		String word = "";
		// words might include html tags---<i>, <b>, etc---which should not 
		// be output into the PDF; but they need to be preserved for later 
		// processing of the string after it has been used, so a sanitized 
		// copy needs to be made for PDF output
		String sanitizedWord = "";
		String lineText = "";
		String textblock = "";
		
		int idx = 0;
		int end = 0;

		pdfLines = new Vector<PdfLine>();
		PDRectangle rect = outSpecs.getPageRect();
		
		PDFont font = outSpecs.getFont();
		
		if(outSpecs.isBodyText)
		{
			outSpecs.setFontsize(outSpecs.getPageSpecs().getFontsize());
		}
		float fontSize = outSpecs.getFontsize();
		
		// edges of text, relative to edges of paper
		float leftMargin = outSpecs.getLeftMargin();
		float rightMargin = outSpecs.getRightMargin();
		
		// number of units first line of each paragraph will be indented from the left margin
		float paraIndent = outSpecs.getParaIndent();
		// current indent value
		float indent = paraIndent;
		
		// width of the current word (plus trailing space)
		float wordWidth = 0;

		// how far from the left margin next text to be shown will be placed 
		float offset = leftMargin + indent;
		outSpecs.setOffset(leftMargin + indent);
		
		int lineReturnIdx = 0;

		do
		{
			lineReturnIdx = textblockIn.indexOf("\n");
			if(lineReturnIdx != -1)
			{
				textblock = Utilities.replaceChars(textblock, "\n\n", "\n", "all");
				textblock = textblockIn.substring(0, lineReturnIdx);
				textblockIn = textblockIn.substring(lineReturnIdx + 1);
			}
			else
				textblock = textblockIn;
			
			// cycle through the words of the paragraph being output, measure word width, 
			// and compare cumulative total to width of the page. Nothing is output here,
			// only determining how many words fit on a line and compiling a list of 
			// lines, divided into lineSegment[] increments so that font changes can
			// be made as indicated by html bold and italic tags in the input text.
			// In actual use, methods for handling centered and headline text will need
			// to be developed, but not here in the testbed code
			
			outSpecs.paraStart = true;
			lineText = "";
			
			while((end = textblock.indexOf(" ", idx + 1)) != -1)
			{
				// get the next word
				word = textblock.substring(idx, end);
				
				// using metrics defined by the current font, get the width of the word being displayed 
				// (sanitizedWord is word minus any html tags) and add it to the offset 
				sanitizedWord = Utilities.sanitizeString(word);
				try
				{
					wordWidth = (font.getStringWidth(sanitizedWord) / outSpecs.getGlyphScaler(outSpecs.getFontstyle())) * fontSize;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				offset += wordWidth;
				
	 			// if new offset exceeds the width of the page, reset offset and start a new line;
				// since output will no longer be to the first line in the paragraph, indent goes to 0
				if(offset > rect.getWidth() - rightMargin)
				{
					// lineText should now contain all the words that will fit onto a line once the html tags are removed;
		 			// compile a list of such lines for later output to the PDF
					// The list contains lineSegment[] arrays, which contain text and font specifications
					pdfLines.add(new PdfLine(lineText, outSpecs));
					
					// reset for the next line
					lineText = "";
					indent = 0;
					outSpecs.paraStart = false;
					offset = leftMargin;
					
					continue;
				}
	 			
	 			// not a new line, so keep adding words to lineText
	 			lineText += word;
	 			idx = end;
			}
			// while() loop stops short of last word of paraText, so add it now, then add the 
			// final line of text to the lines vector
			if(idx < textblock.length())
				lineText += textblock.substring(idx);
			else
				lineText += textblock;
			pdfLines.add(new PdfLine(lineText, outSpecs));
			
		}while(lineReturnIdx != -1);
	}
	
	
	
	
// original
//
//	public PdfLines(String textblock, TextOutSpecs outSpecs)
//	{
//		String word = "";
//		// words might include html tags---<i>, <b>, etc---which should not 
//		// be output into the PDF; but they need to be preserved for later 
//		// processing of the string after it has been used, so a sanitized 
//		// copy needs to be made for PDF output
//		String sanitizedWord = "";
//		String lineText = "";
//		
//		int idx = 0;
//		int end = 0;
//
//		// text should not contain any line breaks (text in lineSegment == text from 
//		// the manuscript textblock, without the closing line feed)
//		if(textblock.indexOf("\n") != -1)
//		{
//			System.out.println("Textblock contains multiple line returns, in PdfLines.java constructor.");
//			
//			// PdfLines() requires a single textblock as input. Cannot process text if it does not
//			// contain exactly one line return, which smust be at the end of the text block.
//		}
//		else
//		{
//			pdfLines = new Vector<PdfLine>();
//			PDRectangle rect = outSpecs.getPageRect();
//			
//			PDFont font = outSpecs.getFont();
//			
//			if(outSpecs.isBodyText)
//			{
//				outSpecs.setFontsize(outSpecs.getPageSpecs().getFontsize());
//			}
//			float fontSize = outSpecs.getFontsize();
//			
//			// edges of text, relative to edges of paper
//			float leftMargin = outSpecs.getLeftMargin();
//			float rightMargin = outSpecs.getRightMargin();
//			
//			// number of units first line of each paragraph will be indented from the left margin
//			float paraIndent = outSpecs.getParaIndent();
//			// current indent value
//			float indent = paraIndent;
//			
//			// width of the current word (plus trailing space)
//			float wordWidth = 0;
//	
//			// how far from the left margin next text to be shown will be placed 
//			float offset = leftMargin + indent;
//			outSpecs.setOffset(leftMargin + indent);
//	
//			// cycle through the words of the paragraph being output, measure word width, 
//			// and compare cumulative total to width of the page. Nothing is output here,
//			// only determining how many words fit on a line and compiling a list of 
//			// lines, divided into lineSegment[] increments so that font changes can
//			// be made as indicated by html bold and italic tags in the input text.
//			// In actual use, methods for handling centered and headline text will need
//			// to be developed, but not here in the testbed code
//			
//			outSpecs.paraStart = true;
//			
//			while((end = textblock.indexOf(" ", idx + 1)) != -1)
//			{
//				// get the next word
//				word = textblock.substring(idx, end);
//				
//				// using metrics defined by the current font, get the width of the word being displayed 
//				// (sanitizedWord is word minus any html tags) and add it to the offset 
//				sanitizedWord = Utilities.sanitizeString(word);
//				try
//				{
//					wordWidth = (font.getStringWidth(sanitizedWord) / outSpecs.getGlyphScaler(outSpecs.getFontstyle())) * fontSize;
//				}
//				catch(Exception e)
//				{
//					e.printStackTrace();
//				}
//				offset += wordWidth;
//				
//	 			// if new offset exceeds the width of the page, reset offset and start a new line;
//				// since output will no longer be to the first line in the paragraph, indent goes to 0
//				if(offset > rect.getWidth() - rightMargin)
//				{
//					// lineText should now contain all the words that will fit onto a line once the html tags are removed;
//		 			// compile a list of such lines for later output to the PDF
//					// The list contains lineSegment[] arrays, which contain text and font specifications
//					pdfLines.add(new PdfLine(lineText, outSpecs));
//					
//					// reset for the next line
//					lineText = "";
//					indent = 0;
//					outSpecs.paraStart = false;
//					offset = leftMargin;
//					
//					continue;
//				}
//	 			
//	 			// not a new line, so keep adding words to lineText
//	 			lineText += word;
//	 			idx = end;
//			}
//			// while() loop stops short of last word of paraText, so add it now, then add the 
//			// final line of text to the lines vector
//			lineText += textblock.substring(idx);
//			pdfLines.add(new PdfLine(lineText, outSpecs));
//		}
//	}

	public Vector<PdfLine> getPdfLines()
	{
		return pdfLines;
	}

}
