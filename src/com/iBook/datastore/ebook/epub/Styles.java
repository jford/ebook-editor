package com.iBook.datastore.ebook.epub;

import com.iBook.datastore.*;
import com.iBook.utilities.*;

public class Styles
{
	private String pageTemplateText = "";
	private String pageTemplateFilename = "";
	private String pathMrkr = PropsManager.getPathMarker();
	
	public Styles()
	{
		String path_to_resources = PropsManager.getPathToResources();
		pageTemplateFilename = "page-template.xpgt";
		try
		{
			pageTemplateText = Utilities.read_file(path_to_resources + "metadata" + pathMrkr + pageTemplateFilename);
		}
		catch(Exception e)
		{
			System.out.println("Can't read " + pageTemplateFilename);
			e.printStackTrace();
		}
	}
	public String getPageTemplateText()
	{
		return pageTemplateText;
	}
}
