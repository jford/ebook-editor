package com.iBook.datastore;

import org.w3c.dom.*;

import java.util.Vector;

import com.iBook.datastore.*;
import com.iBook.datastore.books.*;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.locations.*;
import com.iBook.datastore.manuscripts.*;

public class MetaDataParser
{
    private String node_name                     = "";
    private String node_val                      = "";
    private MetaData metaData                    = null;
    
    public MetaDataParser()
    {
    }

    public MetaData parse(String metaFile)
    {
    	// Parse the XML and create a DOM node tree
    	
    	// meta file is the path/filename of the meta.xml
    	DOMParser domParser                = new DOMParser();
        Document parsedXML                 = domParser.parse(metaFile);
        
        // walk the DOM tree
        processDocument(parsedXML);
        
        return metaData;
    }

    private void processDocument(Node n)
    {
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                if(node_name.compareTo("metadata") == 0)
                {
                    // Create a new MetaData object, then populate it. 
                    metaData = new MetaData();
                    processMetaData(n, metaData);
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

    private void processMetaData(Node n, MetaData metaData)
    {
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                if(node_name.compareTo("books") == 0)
                {
                	processBooks(n, metaData);
                }
                if(node_name.compareTo("characters") == 0)
                {
                	processChars(n, metaData);
                }
                if(node_name.compareTo("geolocales") == 0)
                {
                	processGeolocs(n, metaData);
                }
                if(node_name.compareTo("locs") == 0)
                {
                	processLocs(n, metaData);
                }
                if(node_name.compareTo("manuscripts") == 0)
                {
                	processManuscripts(n, metaData);
                }
                if(node_name.compareTo("templates") == 0)
                {
                	processTemplates(n, metaData);
                }
            }
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processMetaData(child, metaData);
        }
    }
    
    private void processBooks(Node n, MetaData metaData)
    {
        int type = n.getNodeType();
        switch (type)
        {
	        case Node.ATTRIBUTE_NODE:
	        {
	        	node_name = n.getNodeName();
	        	node_val = n.getNodeValue();
	        	if(node_name.compareTo("next") == 0)
	        		metaData.setNextBookNum(node_val);
	        }
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                if(node_name.compareTo("id") == 0)
                {
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processBooks(att, metaData);
                    }
                }
                break;
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processBooks(child, metaData);
        }
    }
    
    private void processChars(Node n, MetaData metaData)
    {
        int type = n.getNodeType();
        switch (type)
        {
	        case Node.ATTRIBUTE_NODE:
	        {
	        	node_name = n.getNodeName();
	        	node_val = n.getNodeValue();
	        	if(node_name.compareTo("next") == 0)
	        		metaData.setNextCharNum(node_val);
	        }
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                if(node_name.compareTo("id") == 0)
                {
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processChars(att, metaData);
                    }
                }
                break;
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processChars(child, metaData);
        }
    }
    
    private void processGeolocs(Node n, MetaData metaData)
    {
        int type = n.getNodeType();
        switch (type)
        {
	        case Node.ATTRIBUTE_NODE:
	        {
	        	node_name = n.getNodeName();
	        	node_val = n.getNodeValue();
	        	if(node_name.compareTo("next") == 0)
	        		metaData.setNextGeolocNum(node_val);
	        }
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                if(node_name.compareTo("id") == 0)
                {
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processGeolocs(att, metaData);
                    }
                }
                break;
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processGeolocs(child, metaData);
        }
    }
    
    private void processLocs(Node n, MetaData metaData)
    {
        int type = n.getNodeType();
        switch (type)
        {
	        case Node.ATTRIBUTE_NODE:
	        {
	        	node_name = n.getNodeName();
	        	node_val = n.getNodeValue();
	        	if(node_name.compareTo("next") == 0)
	        		metaData.setNextLocNum(node_val);
	        }
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                if(node_name.compareTo("id") == 0)
                {
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processLocs(att, metaData);
                    }
                }
                break;
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processLocs(child, metaData);
        }
    }
    
    private void processManuscripts(Node n, MetaData metaData)
    {
        int type = n.getNodeType();
        switch (type)
        {
	        case Node.ATTRIBUTE_NODE:
	        {
	        	node_name = n.getNodeName();
	        	node_val = n.getNodeValue();
	        	if(node_name.compareTo("next") == 0)
	        		metaData.setNextManuscriptNum(node_val);
	        }
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                if(node_name.compareTo("id") == 0)
                {
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processManuscripts(att, metaData);
                    }
                }
                break;
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processManuscripts(child, metaData);
        }
    }
    
    private void processTemplates(Node n, MetaData metaData)
    {
        int type = n.getNodeType();
        switch (type)
        {
	        case Node.ATTRIBUTE_NODE:
	        {
	        	node_name = n.getNodeName();
	        	node_val = n.getNodeValue();
	        	if(node_name.compareTo("next") == 0)
	        		metaData.setNextTemplateNum(node_val);
	        }
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                if(node_name.compareTo("id") == 0)
                {
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processTemplates(att, metaData);
                    }
                }
                break;
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processTemplates(child, metaData);
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
    