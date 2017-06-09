package com.iBook.datastore.books;

import org.w3c.dom.*;
import com.iBook.datastore.*;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.locations.*;
// import com.iBook.datastore.manuscripts.*;

// import java.util.Vector;

public class BookListParser
{
    private String node_name                     = "";
    private String node_val                      = "";
    private String tObjId                       = "";
    
    private BookList bookList                    = new BookList();
    
    private ObjSubstitutionsManager objSubMgr = null;
    
    public BookListParser()
    {
    }

    public BookList parse(String xml)
    {
        // Parse the XML and create a DOM node tree
        DOMParser domParser                = new DOMParser();
        Document parsedXML                 = domParser.parse(xml);
        
        // walk the DOM tree
        processDocument(parsedXML);
        
        return bookList;
    }
    
    private void processDocument(Node n)
    {
    	processBookList(n);
    }

    private void processBookList(Node n)
    {
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                if(node_name.compareTo("book") == 0)
                {
                	BookProfile bookProfile = new BookProfile();
                	// a new objSubMgr (manager of book/template object substitutions) 
                	// is created for each book 
                	objSubMgr = new ObjSubstitutionsManager();
                    processBook(n, bookProfile);
                    // okay, book is done, objSubMgr needs to be added to the book profile
                    objSubMgr.setTemplateId(bookProfile.getTemplateId());
                    bookProfile.setObjSubMgr(objSubMgr);
                    bookList.addItem(bookProfile);
                }
            }
            break;

        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processDocument(child);
        }
    }
    
    private void processBook(Node n, BookProfile bookProfile)
    {
    	String text = "";
    	
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();

                if(node_name.compareTo("book") == 0)
                {
	                for(int i =0; i < atts.getLength(); i++)
	                {
	                    Node att = atts.item(i);
	                    processBook(att, bookProfile);
	                }
                }
                if(node_name.compareTo("newtitle") == 0)
                {
                	text = getText(n);
                    bookProfile.setTitle(text);
                }
                if(node_name.compareTo("author") == 0)
                {
                    processAuthor(n, bookProfile);
                }
                if(node_name.compareTo("source") == 0)
                {
                    processSource(n, bookProfile);
                }
                if(node_name.compareTo("characters") == 0)
                {
                	processCharacters(n, objSubMgr);
                }
                if(node_name.compareTo("locations") == 0)
                {
                	processLocations(n, objSubMgr);
                }
                if(node_name.compareTo("geolocales") == 0)
                {
                	processGeolocales(n, objSubMgr);
                }
                break;
            case Node.ATTRIBUTE_NODE:
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("id") == 0)
                {
                    bookProfile.setId(node_val);
                    bookList.addId(node_val);
                }
                if(node_name.compareTo("template") == 0)
                {
                    bookProfile.setTemplate(node_val);
                }
                if(node_name.compareTo("manuscriptId") == 0)
                {
                    bookProfile.setManuscriptId(node_val);
                }
                break;
        }

        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processBook(child, bookProfile);
        }
    }
    private void processCharacters(Node n, ObjSubstitutionsManager objSubMgr)
    {
    	int type = n.getNodeType();
    	switch(type)
    	{
	    	case Node.ELEMENT_NODE:
	    	{
	    		node_name = n.getNodeName();
	    		node_val = n.getNodeValue();
	    		
                NamedNodeMap atts = n.getAttributes();

	    		if(node_name.compareTo("character") == 0)
	    		{
	    			tObjId = "";
	    			String[] charSubPair = new String[]{"",""};
                    for(int i =0; i < atts.getLength(); i++)
                    {
                    	String[] ids = new String[]{"",""};
                        Node att = atts.item(i);
                        processCharacter(att, ids);
                        if(ids[0].length() > 0)
                        	charSubPair[0] = ids[0];
                        else if(ids[1].length() > 0)
                        	charSubPair[1] = ids[1];
                    }
                    objSubMgr.addSub(PropsManager.ObjType.CHARACTER, charSubPair[0], charSubPair[1]);
	    		}
	    		if(node_name.compareTo("tCharAlias") == 0 ||
	    		   node_name.compareTo("tCharAttribute") == 0)
	    		{
	    			String[] ids = {"",""};
	    			processCharacter(n, ids);
	    		}
	    	}
	    	break;
    	}
        for(Node child = n.getFirstChild();
                child != null;
                child = child.getNextSibling())
       {
           processCharacters(child, objSubMgr);
       }
    }

    private void processCharacter(Node n, String[] ids)
    {
    	int type = n.getNodeType();
    	switch(type)
    	{
			case Node.ATTRIBUTE_NODE:
			    node_name = n.getNodeName();
			    node_val = n.getNodeValue();
			    
			    if(node_name.compareTo("tCharId") == 0)
			    {
			    	tObjId = node_val;
			    	ids[0] = node_val;
			    }
			    if(node_name.compareTo("iBookCharId") == 0)
			    {
			    	 ids[1] = node_val;
			    }
				break;
	    	case Node.ELEMENT_NODE:
	    	{
	    		node_name = n.getNodeName();
	    		node_val = n.getNodeValue();
	    		
                NamedNodeMap atts = n.getAttributes();

	    		if(node_name.compareTo("tCharAlias") == 0)
	    		{
	    			String[] aliasEntry = {tObjId, "", ""};
                	String[] aliasText = new String[]{"",""};
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i); 
                        processAliases(att, aliasText);
                    }
                    if(aliasText[0].length() > 0)
                    	aliasEntry[1] = aliasText[0];
                    if(aliasText[1].length() > 0)
                    	aliasEntry[2] = aliasText[1];
                    objSubMgr.addAliasSubstitute(aliasEntry);
	    		}
	    		if(node_name.compareTo("tCharAttribute") == 0)
	    		{
	    			String[] attributeEntry = {tObjId, "", ""};
                	String[] attributeText = new String[]{"",""};
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i); 
                        processAttributes(att, attributeText);
                    }
                    if(attributeText[0].length() > 0)
                    	attributeEntry[1] = attributeText[0];
                    if(attributeText[1].length() > 0)
                    	attributeEntry[2] = attributeText[1];
                    objSubMgr.addAttributeSubstitute(attributeEntry);
	    		}
	    	}
	    	break;
    	}
        for(Node child = n.getFirstChild();
                child != null;
                child = child.getNextSibling())
       {
           processCharacter(child, ids);
       }
    }
    
    private void processAliases(Node n, String[] aliasText)
    {
    	int type = n.getNodeType();
    	switch (type)
    	{
	    	case Node.ATTRIBUTE_NODE:
	    	{
			    node_name = n.getNodeName();
			    node_val = n.getNodeValue();
			    if(node_name.compareTo("alias") == 0)
			    {
			    	aliasText[0] = node_val;
			    }
			    if(node_name.compareTo("substituteText") == 0)
			    {
			    	aliasText[1] = node_val;
			    }
	    	}
	    	break;
    	}
        for(Node child = n.getFirstChild();
                child != null;
                child = child.getNextSibling())
       {
           processAliases(child, aliasText);
       }
    }
    
    private void processAttributes(Node n, String[] attributeText)
    {
    	int type = n.getNodeType();
    	switch (type)
    	{
	    	case Node.ATTRIBUTE_NODE:
	    	{
			    node_name = n.getNodeName();
			    node_val = n.getNodeValue();
			    if(node_name.compareTo("attribute") == 0)
			    {
			    	attributeText[0] = node_val;
			    }
			    if(node_name.compareTo("substituteText") == 0)
			    {
			    	attributeText[1] = node_val;
			    }
	    	}
	    	break;
    	}
        for(Node child = n.getFirstChild();
                child != null;
                child = child.getNextSibling())
       {
           processAttributes(child, attributeText);
       }
    }
    
    private void processLocations(Node n, ObjSubstitutionsManager objSubMgr)
    {
    	int type = n.getNodeType();
    	switch(type)
    	{
	    	case Node.ELEMENT_NODE:
	    	{
	    		node_name = n.getNodeName();
	    		node_val = n.getNodeValue();
	    		
                NamedNodeMap atts = n.getAttributes();

	    		if(node_name.compareTo("location") == 0)
	    		{
	    			tObjId = "";
	    			String[] locSubPair = new String[]{"",""};
                    for(int i =0; i < atts.getLength(); i++)
                    {
                    	String[] ids = new String[]{"",""};
                        Node att = atts.item(i);
                        processLocation(att, ids);
                        if(ids[0].length() > 0)
                        	locSubPair[0] = ids[0];
                        else if(ids[1].length() > 0)
                        	locSubPair[1] = ids[1];
                    }
                    objSubMgr.addSub(PropsManager.ObjType.LOCATION, locSubPair[0], locSubPair[1]);
	    		}
	    		if(node_name.compareTo("tLocAlias") == 0)
	    		{
	    			String[] ids = {"",""};
	    			processLocation(n, ids);
	    		}
	    		if(node_name.compareTo("tLocAttribute") == 0)
	    		{
	    			String[] ids = {"",""};
	    			processLocation(n, ids);
	    		}
	    	}
	    	break;
    	}
        for(Node child = n.getFirstChild();
                child != null;
                child = child.getNextSibling())
       {
           processLocations(child, objSubMgr);
       }
    }

    private void processLocation(Node n, String[] ids)
    {
    	int type = n.getNodeType();
    	switch(type)
    	{
			case Node.ATTRIBUTE_NODE:
			    node_name = n.getNodeName();
			    node_val = n.getNodeValue();
			    if(node_name.compareTo("tLocId") == 0)
			    {
			    	ids[0] = node_val;
			    	tObjId = node_val;
			    }
			    if(node_name.compareTo("iBookLocId") == 0)
			    {
			    	 ids[1] = node_val;
			    }
				break;
	    	case Node.ELEMENT_NODE:
	    	{
	    		node_name = n.getNodeName();
	    		node_val = n.getNodeValue();
	    		
                NamedNodeMap atts = n.getAttributes();

	    		if(node_name.compareTo("tLocAlias") == 0)
	    		{
	    			String[] aliasEntry = {tObjId, "", ""};
                	String[] aliasText = new String[]{"",""};
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i); 
                        processAliases(att, aliasText);
                    }
                    if(aliasText[0].length() > 0)
                    	aliasEntry[1] = aliasText[0];
                    if(aliasText[1].length() > 0)
                    	aliasEntry[2] = aliasText[1];
                    objSubMgr.addAliasSubstitute(aliasEntry);
	    		}
	    		if(node_name.compareTo("tLocAttribute") == 0)
	    		{
	    			String[] attributeEntry = {tObjId, "", ""};
                	String[] attributeText = new String[]{"",""};
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i); 
                        processAttributes(att, attributeText);
                    }
                    if(attributeText[0].length() > 0)
                    	attributeEntry[1] = attributeText[0];
                    if(attributeText[1].length() > 0)
                    	attributeEntry[2] = attributeText[1];
                    objSubMgr.addAttributeSubstitute(attributeEntry);
	    		}
	    	}
	    	break;
    	}
        for(Node child = n.getFirstChild();
                child != null;
                child = child.getNextSibling())
       {
           processLocation(child, ids);
       }
    }
    
    private void processGeolocales(Node n, ObjSubstitutionsManager objSubMgr)
    {
    	int type = n.getNodeType();
    	switch(type)
    	{
	    	case Node.ELEMENT_NODE:
	    	{
	    		node_name = n.getNodeName();
	    		node_val = n.getNodeValue();
	    		
                NamedNodeMap atts = n.getAttributes();

	    		if(node_name.compareTo("geolocale") == 0)
	    		{
	    			tObjId = "";
	    			String[] geolocSubPair = new String[]{"",""};
                    for(int i =0; i < atts.getLength(); i++)
                    {
                    	String[] ids = new String[]{"",""};
                        Node att = atts.item(i);
                        processGeolocale(att, ids);
                        if(ids[0].length() > 0)
                        	geolocSubPair[0] = ids[0];
                        else if(ids[1].length() > 0)
                        	geolocSubPair[1] = ids[1];
                    }
                    objSubMgr.addSub(PropsManager.ObjType.GEOLOCALE, geolocSubPair[0], geolocSubPair[1]);
	    		}
	    		if(node_name.compareTo("tGeolocAlias") == 0)
	    		{
	    			String[] ids = {"",""};
	    			processGeolocale(n, ids);
	    		}
	    		if(node_name.compareTo("tGeolocAttribute") == 0)
	    		{
	    			String[] ids = {"",""};
	    			processGeolocale(n, ids);
	    		}
	    	}
	    	break;
    	}
        for(Node child = n.getFirstChild();
                child != null;
                child = child.getNextSibling())
       {
           processGeolocales(child, objSubMgr);
       }
    }

    private void processGeolocale(Node n, String[] ids)
    {
    	int type = n.getNodeType();
    	switch(type)
    	{
			case Node.ATTRIBUTE_NODE:
			    node_name = n.getNodeName();
			    node_val = n.getNodeValue();
			    if(node_name.compareTo("tGeolocId") == 0)
			    {
			    	ids[0] = node_val;
			    	tObjId = node_val;
			    }
			    if(node_name.compareTo("iBookGeolocId") == 0)
			    {
			    	 ids[1] = node_val;
			    }
				break;
			case Node.ELEMENT_NODE:
			{
				node_name = n.getNodeName();
				node_val = n.getNodeValue();
				
		        NamedNodeMap atts = n.getAttributes();
		
				if(node_name.compareTo("tGeolocAlias") == 0)
				{
					String[] aliasEntry = {tObjId, "", ""};
		        	String[] aliasText = new String[]{"",""};
		            for(int i =0; i < atts.getLength(); i++)
		            {
		                Node att = atts.item(i); 
		                processAliases(att, aliasText);
		            }
		            if(aliasText[0].length() > 0)
		            	aliasEntry[1] = aliasText[0];
		            if(aliasText[1].length() > 0)
		            	aliasEntry[2] = aliasText[1];
		            objSubMgr.addAliasSubstitute(aliasEntry);
				}
	    		if(node_name.compareTo("tGeolocAttribute") == 0)
	    		{
	    			String[] attributeEntry = {tObjId, "", ""};
                	String[] attributeText = new String[]{"",""};
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i); 
                        processAttributes(att, attributeText);
                    }
                    if(attributeText[0].length() > 0)
                    	attributeEntry[1] = attributeText[0];
                    if(attributeText[1].length() > 0)
                    	attributeEntry[2] = attributeText[1];
                    objSubMgr.addAttributeSubstitute(attributeEntry);
	    		}
			}
			break;
    	}
        for(Node child = n.getFirstChild();
                child != null;
                child = child.getNextSibling())
       {
           processGeolocale(child, ids);
       }
    }
    
    private void processAuthor(Node n, BookProfile bookProfile)
    {
    	int type = n.getNodeType();
    	switch(type)
    	{
	    	case Node.ELEMENT_NODE:
	    	{
	    		node_name = n.getNodeName();
	    		node_val = n.getNodeValue();
	    		if(node_name.compareTo("name") == 0)
	    		{
	    			processName(n, bookProfile);
	    		}
	    	}
    	}
        for(Node child = n.getFirstChild();
                child != null;
                child = child.getNextSibling())
       {
           processAuthor(child, bookProfile);
       }
    }
    
    private void processName(Node n, BookProfile bookProfile)
    {
    	String text = "";
    	
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("prefix") == 0)
                {
                	text = getText(n);
                    bookProfile.setNamePrefix(text);
                }
                if(node_name.compareTo("first") == 0)
                {
                	text = getText(n);
                    bookProfile.setNameFirst(text);
                }
                if(node_name.compareTo("middle") == 0)
                {
                	text = getText(n);
                    bookProfile.setNameMiddle(text);
                }
                if(node_name.compareTo("last") == 0)
                {
                	text = getText(n);
                    bookProfile.setNameLast(text);
                }
                if(node_name.compareTo("suffix") == 0)
                {
                	text = getText(n);
                    bookProfile.setNameSuffix(text);
                }
            }
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processName(child, bookProfile);
        }
    }
    
    private void processSource(Node n, BookProfile bookProfile)
    {
    	int type = n.getNodeType();
    	switch(type)
    	{
	    	case Node.ELEMENT_NODE:
	    	{
	    		node_name = n.getNodeName();
	    		node_val = n.getNodeValue();
	    		if(node_name.compareTo("author") == 0)
	    		{
	    			processSourceAuthorName(n, bookProfile);
	    		}
	    		if(node_name.compareTo("title") == 0)
	    		{   
	    			String text = getText(n);
                    bookProfile.setSourceTitle(text);
	    		}
	    	}
    	}
        for(Node child = n.getFirstChild();
                child != null;
                child = child.getNextSibling())
       {
           processSource(child, bookProfile);
       }
    }
    
    private void processSourceAuthorName(Node n, BookProfile bookProfile)
    {
    	String text = "";
    	
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("prefix") == 0)
                {
                	text = getText(n);
                    bookProfile.setSourceAuthorNamePrefix(text);
                }
                if(node_name.compareTo("first") == 0)
                {
                	text = getText(n);
                    bookProfile.setSourceAuthorNameFirst(text);
                }
                if(node_name.compareTo("middle") == 0)
                {
                	text = getText(n);
                    bookProfile.setSourceAuthorNameMiddle(text);
                }
                if(node_name.compareTo("last") == 0)
                {
                	text = getText(n);
                    bookProfile.setSourceAuthorNameLast(text);
                }
                if(node_name.compareTo("suffix") == 0)
                {
                	text = getText(n);
                    bookProfile.setSourceAuthorNameSuffix(text);
                }
            }
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processSourceAuthorName(child, bookProfile);
        }
    }

    private String getText(Node n)
    {
    	String text = "";
        for(Node child = n.getFirstChild();
                child != null;
                child = child.getNextSibling())
       {
            int type = child.getNodeType();
            if(type == Node.TEXT_NODE)
        		text = child.getNodeValue();
        }
    	return text;
    }
}
