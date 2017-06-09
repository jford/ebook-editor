package com.iBook.datastore.ebook.pdf;

import java.util.Iterator;
import java.util.Vector;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.iBook.datastore.ebook.BoilerplateFactory;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.Utilities;

public class PrefacePage
{
	private BoilerplateFactory boilerplateFactory = null;
	private PDDocument document = null;
	private int pageCount = 0;
	private Manuscript manuscript = null;
	private PdfLine pdfLine = null;
	private String userId;
	private Vector<PdfLine> pdfLines = null;
	private Iterator<PdfLine> pdfLinesI = null;
	
	public PrefacePage(Manuscript manuscript, String userId, PDDocument document, int pageCount)
	{
		this.manuscript = manuscript;
		this.userId = userId;
		this.document = document;
		this.pageCount = pageCount;
		
		boilerplateFactory = new BoilerplateFactory(manuscript, userId);

		TextOutSpecs outSpecs = new TextOutSpecs();
		outSpecs.setPageSpecs(boilerplateFactory.getPageSpecs(BoilerplateFactory.PdfPage.PREFACE));
		getPdfLines(outSpecs);
	}
	
	private void getPdfLines(TextOutSpecs outSpecs)
	{
		// convert each textblock in the manuscript to a collection of PdfLines, and consolidate all of them in a single vector
		Vector<PdfLine> pdfLinesIn = new Vector<PdfLine>();
		Iterator<PdfLine> linesInI = null;
		pdfLines = new Vector<PdfLine>();
		
		String prefaceText = boilerplateFactory.doPreface(BoilerplateFactory.PubType.PDF);
		prefaceText = Utilities.replaceChars(prefaceText, "\n\n", "\n", "all");
		
		outSpecs.setOffset(outSpecs.getLeftMargin() + outSpecs.getParaIndent());
		
		int idx = 0;
		int end = 0;
		while((idx = prefaceText.indexOf("\n", end)) != -1)
		{
			PdfLines pdfLinesFactory = new PdfLines(prefaceText.substring(end, idx), outSpecs);
			pdfLinesIn = pdfLinesFactory.getPdfLines();
			linesInI = pdfLinesIn.iterator();
			while(linesInI.hasNext())
			{
				pdfLines.add(linesInI.next());
			}
			end = idx + 1;
			if(end > prefaceText.length())
				end = prefaceText.length();
		}
		pdfLinesI = pdfLines.iterator();
	}
	
	public PDPage getPage()
	{
		TextOutSpecs outSpecs = new TextOutSpecs();
		
		PDPage prefacePage = null;
		PDPageContentStream cos = null;

		outSpecs.setPageSpecs(boilerplateFactory.getPageSpecs(BoilerplateFactory.PdfPage.PREFACE));

		if(pdfLinesI.hasNext())
		{
			try
			{
				// create the page and its output stream
				prefacePage = new PDPage(outSpecs.getPageRect());
				cos = new PDPageContentStream(document, prefacePage);
				outSpecs.setCos(cos);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		do
		{
			if(pdfLinesI.hasNext())
			{
				// footer...
				String pageNumText = Utilities.convertNumberToRomanNumeral(++pageCount);
				new PageFooter(outSpecs, pageNumText);
				
				// ...text
				while(pdfLinesI.hasNext())
				{
					pdfLine = pdfLinesI.next();
					outSpecs.setOffset(outSpecs.getLeftMargin());
					new ProcessLine(pdfLine, outSpecs);
					if(pdfLine.getText().length() > 0)	
						outSpecs.getPageSpecs().incrementVSpaceUsed(outSpecs.getLineLeading() + outSpecs.getGap());
				}
				
				try
				{
					cos.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		while(!outSpecs.getPageSpecs().pageFull && pdfLinesI.hasNext());
		return prefacePage;
	}
}
