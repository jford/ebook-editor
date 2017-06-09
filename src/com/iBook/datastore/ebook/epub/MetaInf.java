package com.iBook.datastore.ebook.epub;

import com.iBook.datastore.*;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.*;

public class MetaInf
{
	private String containerFilename = "container.xml";
	private String containerText = "";
	private String path_to_resources = PropsManager.getPathToResources();
	
	public MetaInf()
	{
		try
		{
			containerText = Utilities.read_file(path_to_resources + "metadata\\" + containerFilename);
		}
		catch(Exception e)
		{
			System.out.println("Can't read " + containerFilename);
			e.printStackTrace();
		}
	}
	public String getContainerText()
	{
		return containerText;
	}
}
