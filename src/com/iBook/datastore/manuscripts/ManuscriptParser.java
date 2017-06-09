package com.iBook.datastore.manuscripts;

import org.w3c.dom.*;

import java.util.Vector;

import com.iBook.datastore.*;
import com.iBook.datastore.books.*;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.locations.*;

public class ManuscriptParser
{
	boolean stopForDebugging = false;
	
	private String filename = "";
    private String node_name                     = "";
    private String node_val                      = "";
    
    private Manuscript mScript                   = null;
    
    private Vector<CharacterProfile> characterProfiles  = new Vector<CharacterProfile>();;
    private Vector<LocationProfile> locationProfiles    = new Vector<LocationProfile>();

    
    public ManuscriptParser()
    {
    }

    public Manuscript parse(String xml)
    {
        // Parse the XML and create a DOM node tree
        DOMParser domParser                = new DOMParser();
        Document parsedXML                 = domParser.parse(xml);
        
    	// xml needs to store its own filename, without path
    	String marker = System.getProperty("file.separator");
    	int mrkrLoc = xml.lastIndexOf(marker);
    	filename = xml.substring(mrkrLoc + 1);

    	// walk the DOM tree
        processDocument(parsedXML);
        
        return mScript;
    }

    private void processDocument(Node n)
    {
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                if(node_name.compareTo("manuscript") == 0)
                {
                    // Create a new Manuscript object, then populate it. 
                    mScript = new Manuscript();
                    processMs(n, mScript);
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
    
    private void processMs(Node n, Manuscript mScript)
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
                    mScript.setId(node_val);
                }
                if(node_name.compareTo("name") == 0)
                	// setManuscriptName() also sets the manuscript filename
                	mScript.setManuscriptName(node_val);
	        	if(node_name.compareTo("lastEditedTextblock") == 0)
	        		mScript.setLastEditedTextblock(getText(n));
	        	if(node_name.compareTo("bookId") == 0)
	        		mScript.setBookId(node_val);
            }
            break;

            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                NamedNodeMap atts = n.getAttributes();
                if(node_name.compareTo("manuscript") == 0)
                {
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processMs(att, mScript);
                    }
                }

                if(node_name.compareTo("sourceauthor") == 0)
                {
                	mScript.setSourceAuthor(getText(n));
                }
                
                if(node_name.compareTo("sourcepubdate") == 0)
                {
                	mScript.setSourcePubDate(getText(n));
                }
                
                if(node_name.compareTo("sourcetitle") == 0)
                {
                	mScript.setSourceTitle(getText(n));
                }
                
                if(node_name.compareTo("ibookauthor") == 0)
                {
                	mScript.setIBookAuthor(getText(n));
                }
                
                if(node_name.compareTo("ibooktitle") == 0)
                {
                	mScript.setIBookTitle(getText(n));
                }
                
                if(node_name.compareTo("descriptors") == 0)
                {
                	processDescriptors(n, mScript);
                }
                if(node_name.compareTo("textblock") == 0)
                {
                	processTextblock(n, mScript);
                }
                if(node_name.compareTo("filebreak") == 0)
                {
                	processFilebreak(n, mScript);
                }
                
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processMs(child, mScript);
        }
    }
    
    private void processDescriptors(Node n, Manuscript mScript)
    {
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("descriptor") == 0)
                {
                	String[] descriptor = new String[]{"","","",""};
                    processDescriptor(n, descriptor);
                    mScript.addDescriptor(descriptor);
                }
            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processDescriptors(child, mScript);
        }
    }
    
    private void processDescriptor(Node n, String[] descriptor)
    {
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("descriptor") == 0)
                {
                    NamedNodeMap atts = n.getAttributes();
                    for(int i =0; i < atts.getLength(); i++)
                    {
                        Node att = atts.item(i);
                        processDescriptor(att, descriptor);
                    }
                }
            }
            break;
            case Node.ATTRIBUTE_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("tCharId") == 0)
                    descriptor[0] = getText(n);
                if(node_name.compareTo("textblocksIdx") == 0)
                	descriptor[1] = getText(n);
                if(node_name.compareTo("sourceText") == 0)
                	descriptor[2] = getText(n);
                if(node_name.compareTo("newText") == 0)
                	descriptor[3]  = getText(n);
            }
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processDescriptor(child, descriptor);
        }
    }
    
    private void processTextblock(Node n, Manuscript mScript)
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
                        processTextblock(att, mScript);
                    }
                }
                String check = getText(n);
                mScript.addTextblock(getText(n));
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
            processTextblock(child, mScript);
        }
    }

private void processFilebreak(Node n, Manuscript mScript)
{
    int type = n.getNodeType();
    
    switch (type)
    {
        case Node.ELEMENT_NODE:
        {
            node_name = n.getNodeName();
            node_val = n.getNodeValue();
            NamedNodeMap atts = n.getAttributes();
            if(node_name.compareTo("filebreak") == 0)
            {
                for(int i =0; i < atts.getLength(); i++)
                {
                    Node att = atts.item(i);
                    processFilebreak(att, mScript);
                }
            }
        }
        break;
        case Node.ATTRIBUTE_NODE:
        {
            node_name = n.getNodeName();
            node_val = n.getNodeValue();
            if(node_name.compareTo("id") == 0)
           		mScript.addFilebreak(getText(n));
        }
        break;

    }
    for(Node child = n.getFirstChild();
             child != null;
             child = child.getNextSibling())
    {
        processFilebreak(child, mScript);
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
