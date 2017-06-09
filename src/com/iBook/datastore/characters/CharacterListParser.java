package com.iBook.datastore.characters;

import org.w3c.dom.*;

import java.util.Vector;

import com.iBook.datastore.*;
import com.iBook.datastore.books.*;
import com.iBook.datastore.locations.*;

public class CharacterListParser
{
    private String node_name                     = "";
    private String node_val                      = "";
//    private NarrativeProperties narrativeProps   = null;
//    private TextProperties textProps             = null;
    private CharacterProfile character           = null;
    private LocationProfile location             = null;
//    private NarrativeProperty narrativeProp      = null;
//    private TextBlock textBlock                  = null;
    
    private CharacterList characterList          = new CharacterList();
    
    public CharacterListParser()
    {
    }

    public CharacterList parse(String xml)
    {
        // Parse the XML and create a DOM node tree
        DOMParser domParser                = new DOMParser();
        Document parsedXML                 = domParser.parse(xml);
        
        // walk the DOM tree
        processDocument(parsedXML);
        
        return characterList;
    }
    
    private void processDocument(Node n)
    {
    	processCharacterList(n);
    }

    private void processCharacterList(Node n)
    {
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                NamedNodeMap atts = n.getAttributes();

                if(node_name.compareTo("character") == 0)
                {
                	CharacterProfile characterProfile = new CharacterProfile();
                    // Add item to list. 
                    processCharacter(n, characterProfile);
                    characterList.addCharacterProfile(characterProfile);
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
    
    private void processCharacter(Node n, CharacterProfile characterProfile)
    {
    	String text = "";
    	
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                
                if(node_name.compareTo("character") == 0)
                {
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processCharacter(att, characterProfile);
                    }
                }
                if(node_name.compareTo("gender") == 0)
                {
                	text = getText(n);
                	characterProfile.setGender(text);
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processCharacter(att, characterProfile);
                    }
                }
                if(node_name.compareTo("age") == 0)
                {
                	text = getText(n);
                	if(text.length() > 0)
                		characterProfile.setAge(text);
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processCharacter(att, characterProfile);
                    }
                }
                if(node_name.compareTo("name") == 0)
                {
                    processName(n, characterProfile);
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processName(att, characterProfile);
                    }
                }
                if(node_name.compareTo("attributes") == 0)
                {
                    processAttributes(n, characterProfile);
                }
                if(node_name.compareTo("books") == 0)
                {
                	processBooks(n, characterProfile);
                }
            }
            break;
            case Node.ATTRIBUTE_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("id") == 0)
                {
                    characterProfile.setId(node_val);
                }
            }
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processCharacter(child, characterProfile);
        }
    }
    private void processName(Node n, CharacterProfile characterProfile)
    {
    	String text = "";
    	
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                if(node_name.compareTo("namePrefix") == 0)
                {
                	text = getText(n);
                    characterProfile.setNamePrefix(text);
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processName(att, characterProfile);
                    }
                }
                if(node_name.compareTo("nameFirst") == 0)
                {
                	text = getText(n);
                	characterProfile.setNameFirst(text);
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processName(att, characterProfile);
                    }
                }
                if(node_name.compareTo("nameMiddle") == 0)
                {
                	text = getText(n);
                	characterProfile.setNameMiddle(text);
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processName(att, characterProfile);
                    }
                }
                if(node_name.compareTo("nameLast") == 0)
                {
                	text = getText(n);
                	characterProfile.setNameLast(text);
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processName(att, characterProfile);
                    }
                }
                if(node_name.compareTo("nameSuffix") == 0)
                {
                	text = getText(n);
                	characterProfile.setNameSuffix(text);
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processName(att, characterProfile);
                    }
                }
                if(node_name.compareTo("nameShort") == 0)
                {
                	text = getText(n);
                	characterProfile.setShortName(text);
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processName(att, characterProfile);
                    }
                }
                if(node_name.compareTo("aliases") == 0)
                {
                	processAliases(n, characterProfile);
                }
            }
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processName(child, characterProfile);
        }
    }
    private void processAliases(Node n, CharacterProfile characterProfile)
    {
    	String text = "";
    	
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("alias") == 0)
                {
                	text = getText(n);
                	if(text.length() > 0)
                		characterProfile.addAlias(text);
                }
            }
        }
        for(Node child = n.getFirstChild();
                child != null;
                child = child.getNextSibling())
       {
           processAliases(child, characterProfile);
       }
    }
    
    private void processAttributes(Node n, CharacterProfile characterProfile)
    {
    	String text = "";
    	
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("attribute") == 0)
                {
                	text = getText(n);
                	if(text.length() > 0)
                		characterProfile.addAttribute(text);
                }
            }
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processAttributes(child, characterProfile);
        }
    }

    private void processBooks(Node n, CharacterProfile characterProfile)
    {
    	String text = "";
    	
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("bookId") == 0)
                {
                	text = getText(n);
                	if(text.length() > 0)
                		characterProfile.addBook(text);
                }
            }
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processBooks(child, characterProfile);
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
