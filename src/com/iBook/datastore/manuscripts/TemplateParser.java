package com.iBook.datastore.manuscripts;

import org.w3c.dom.*;

import java.util.Vector;

import com.iBook.datastore.*;
import com.iBook.datastore.books.*;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.locations.*;
import com.iBook.datastore.manuscripts.*;

public class TemplateParser
{
	private boolean stopForDebugging = false;
	
	// xml needs to store its own filename
	private String filename                      = "";
    private String node_name                     = "";
    private String node_val                      = "";
    private TemplateProfile template             = null;
    private CharacterProfile character			 = null;
    private LocationProfile location             = null;
    private GeolocaleProfile geoloc              = null;
    
    private ManuscriptManager mscriptMgr = new ManuscriptManager();
    
    public TemplateParser()
    {
    }

    public TemplateProfile parse(String templateId)
    {
        // Parse the XML and create a DOM node tree
    	
/*    	// xml needs to store its own filename, without path
    	String marker = System.getProperty("file.separator");
    	int mrkrLoc = templateId.lastIndexOf(marker);
    	filename = templateId.substring(mrkrLoc + 1);
*/    	
        DOMParser domParser                = new DOMParser();
        Document parsedXML                 = domParser.parse(templateId);
        
        // walk the DOM tree
        processDocument(parsedXML);
        
        return template;
    }

    private void processDocument(Node n)
    {
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                if(node_name.compareTo("template") == 0)
                {
                    // Create a new Manuscript object, then populate it. 
                    template = new TemplateProfile();
                    processTemplate(n, template);
                }
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processDocument(child);
        }
    }
    
    private void processTemplate(Node n, TemplateProfile template)
    {
        int type = n.getNodeType();
        switch (type)
        {
	        case Node.ATTRIBUTE_NODE:
	        {
	        	node_name = n.getNodeName();
	        	node_val = n.getNodeValue();
	        	if(node_name.compareTo("id") == 0)
	        		template.setId(getText(n));
	        	if(node_name.compareTo("filename") == 0)
	        		template.setFilename(getText(n));
//	        		template.setFilename(filename);
	        	if(node_name.compareTo("lastEditedTextblock") == 0)
	        		template.setLastEditedTextblock(getText(n));
	        }
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                if(node_name.compareTo("template") == 0)
                {
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processTemplate(att, template);
                    }
                }
                if(node_name.compareTo("author") == 0)
                	processAuthor(n, template);
                if(node_name.compareTo("title") == 0)
                	template.setTitle(getText(n));
                if(node_name.compareTo("pubdate") == 0)
                	template.setPubdate(getText(n));
                if(node_name.compareTo("pubtype") == 0)
                	template.setPubtype(getText(n));
                if(node_name.compareTo("characters") == 0)
                	processCharacters(n, template);
                if(node_name.compareTo("locations") == 0)
                	processLocations(n, template);
                if(node_name.compareTo("geolocales") == 0)
                	processGeolocales(n, template);
                if(node_name.compareTo("textblock") == 0)
                	processTextblock(n, template);
                break;
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processTemplate(child, template);
        }
    }
    
    private void processAuthor(Node n, TemplateProfile template)
    {
    	int type = n.getNodeType();
    	switch(type)
    	{
	    	case Node.ELEMENT_NODE:
	    	{
	    		node_name = n.getNodeName();
	    		node_val = n.getNodeValue();
	    		if(node_name.compareTo("namePrefix") == 0)
	    			template.setAuthorNamePrefix(getText(n));
	    		if(node_name.compareTo("nameFirst") == 0)
	    			template.setAuthorNameFirst(getText(n));
	    		if(node_name.compareTo("nameMiddle") == 0)
	    			template.setAuthorNameMiddle(getText(n));
	    		if(node_name.compareTo("nameLast") == 0)
	    			template.setAuthorNameLast(getText(n));
	    		if(node_name.compareTo("nameSuffix") == 0)
	    			template.setAuthorNameSuffix(getText(n));
	    		break;
	    	}
    	}
        for(Node child = n.getFirstChild();
                child != null;
                child = child.getNextSibling())
       {
           processAuthor(child, template);
       }

    }
    
    private void processCharacters(Node n, TemplateProfile template)
    {
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("character") == 0)
                { 
                	character = new CharacterProfile();
                	processCharacter(n, character);
                	template.addCharacter(character);
//                	mscriptMgr.addTemplateCharacter(character);
                }
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processCharacters(child, template);
        }
    }
    
    private void processCharacter(Node n, CharacterProfile character)
    {
        int type = n.getNodeType();
        switch (type)
        {
	        case Node.ATTRIBUTE_NODE:
	        {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("id") == 0)
                {
                    character.setId(node_val);
                }
	        	break;
	        }
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                
                for(int i =0; i < atts.getLength(); i++)
                {
                    Node att = atts.item(i);
                    processCharacter(att, character);
                }
                if(node_name.compareTo("namePrefix") == 0)
                	character.setNamePrefix(getText(n));
                
                if(node_name.compareTo("nameFirst") == 0)
                	character.setNameFirst(getText(n));
                
                if(node_name.compareTo("nameMiddle") == 0)
                	character.setNameMiddle(getText(n));
                
                if(node_name.compareTo("nameLast") == 0)
                	character.setNameLast(getText(n));
                
                if(node_name.compareTo("nameSuffix") == 0)
                	character.setNameSuffix(getText(n));
                
                if(node_name.compareTo("nameShort") == 0)
                	character.setShortName(getText(n));
                
                if(node_name.compareTo("alias") == 0)
                	character.addAlias(getText(n));
                
                if(node_name.compareTo("gender") == 0)
                	character.setGender(getText(n));
                
                if(node_name.compareTo("age") == 0)
                	character.setAge(getText(n));
                
                if(node_name.compareTo("attribute") == 0)
                	character.addAttribute(getText(n));
                
                if(node_name.compareTo("context") == 0)
                	character.setContext(getText(n));
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processCharacter(child, character);
        }
    }
    
    private void processLocations(Node n, TemplateProfile template)
    {
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("location") == 0)
                {
                	location = new LocationProfile();
                	processLocation(n, location);
                	template.addLocation(location);
//                	mscriptMgr.addTemplateLocation(location);
                }
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processLocations(child, template);
        }
    }
    
    private void processLocation(Node n, LocationProfile location)
    {
        int type = n.getNodeType();
        switch (type)
        {
	        case Node.ATTRIBUTE_NODE:
	        {
	            node_name = n.getNodeName();
	            node_val = n.getNodeValue();
	            if(node_name.compareTo("id") == 0)
	            {
	                location.setId(node_val);
	            }
	                   	
	        	break;
	        }
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                
                for(int i =0; i < atts.getLength(); i++)
                {
                    Node att = atts.item(i);
                    processLocation(att, location);
                }
                
                if(node_name.compareTo("full") == 0)
                	location.setName(getText(n));
                
                if(node_name.compareTo("alias") == 0)
                	location.addAlias(getText(n));
                
                if(node_name.compareTo("description") == 0)
                	location.addLocationDesc(getText(n));
                
                if(node_name.compareTo("type") == 0)
                	location.setType(getText(n));
                
                if(node_name.compareTo("geolocale") == 0)
                {
/*                	geoloc = new GeolocaleProfile();
                	processGeolocale(n, geoloc);
                	template.addGeoloc(geoloc);
//                	mscriptMgr.addTemplateGeolocale(geoloc);
*/                }
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processLocation(child, location);
        }
    }
    
    private void processGeolocales(Node n, TemplateProfile template)
    {
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                
                if(node_name.compareTo("geolocale") == 0)
                {
                	GeolocaleProfile geoloc = new GeolocaleProfile();
                	processGeolocale(n, geoloc);
                	template.addGeoloc(geoloc);
                }
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processGeolocales(child, template);
        }
    }
    
    private void processGeolocale(Node n, GeolocaleProfile geoloc)
    {
        int type = n.getNodeType();
        switch (type)
        {
	        case Node.ATTRIBUTE_NODE:
	        {
	            node_name = n.getNodeName();
	            node_val = n.getNodeValue();
	            if(node_name.compareTo("id") == 0)
	            {
	                geoloc.setId(getText(n));
	            }
	                   	
	        	break;
	        }
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                
                for(int i =0; i < atts.getLength(); i++)
                {
                    Node att = atts.item(i);
                    processGeolocale(att, geoloc);
                }
                
                if(node_name.compareTo("name") == 0)
                	geoloc.setName(getText(n));
                
                if(node_name.compareTo("description") == 0)
                	geoloc.addDescription(getText(n));

                if(node_name.compareTo("type") == 0)
                	geoloc.setType(getText(n));
                
                if(node_name.compareTo("alias") == 0)
                	geoloc.addAlias(getText(n));
            
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processGeolocale(child, geoloc);
        }
    }
    
   private void processTextblock(Node n, TemplateProfile template)
    {
        int type = n.getNodeType();
        String textblockId = "";
        
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                if(node_name.compareTo("textblock") == 0)
                {
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processTextblock(att, template);
                    }
                }
                template.addTextblock(getText(n));
            }
            break;
            case Node.ATTRIBUTE_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("id") == 0)
               		textblockId = getText(n);
                if(textblockId.indexOf("273") != -1)
                	stopForDebugging = true;
            }
            break;

        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processTextblock(child, template);
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
