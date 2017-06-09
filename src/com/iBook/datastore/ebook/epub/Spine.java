package com.iBook.datastore.ebook.epub;

import java.util.*;

public class Spine
{
	private Vector<String> itemrefs = null;
	private String spine = "   <spine toc=\"ncx\">\n";
	
	public Spine(Manifest manifest)
	{
		itemrefs = manifest.getSpineItems();
		
		String spineItem = "";
		String itemText = "";
		Iterator<String> itemrefsI = itemrefs.iterator();
		while(itemrefsI.hasNext())
		{
			itemText = itemrefsI.next() + ".xhtml";
			if(itemText.equals("Cover.xhtml"))
				itemText += "\" linear=\"yes\"";
			else
				itemText += "\" linear=\"yes\"";
			spineItem = "      <itemref idref=\"" + itemText + " />\n";
			spine += spineItem;
		}
		spine += "   </spine>\n";
	}
	public String getSpine()
	{
		return spine;
	}
}
