package com.iBook.datastore.ebook.pdf;

import com.iBook.datastore.manuscripts.*;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class CoverPage
{
	public CoverPage(String coverArt, PDDocument document)
	{
		PDPage coverPage = null;
		PDPageContentStream cos = null;
		
		try
		{
			coverPage = new PDPage();
			cos = new PDPageContentStream(document, coverPage);
			PDImageXObject pdImage = PDImageXObject.createFromFile(coverArt, document);
			float scale = .31f;
			// drawImage(pdImage, leftCoord, bottomCoord, imageWidth, imageHeight);
			cos.drawImage(pdImage,  60,  30, pdImage.getWidth()*scale, pdImage.getHeight()*scale);;
			cos.close();
			document.addPage(coverPage);

			// add blank page
			PDPage blankPage = new PDPage();
			document.addPage(blankPage);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
