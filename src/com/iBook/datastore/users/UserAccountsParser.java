package com.iBook.datastore.users;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.iBook.datastore.DOMParser;
import com.iBook.utilities.Utilities;

public class UserAccountsParser
{
    private String node_name                     = "";
    private String node_val                      = "";
    
    private Vector<UserAccount> users = new Vector<UserAccount>();
    
    public UserAccountsParser()
    {
    }

    public Vector<UserAccount> parse(String xml)
    {
        // Parse the XML and create a DOM node tree
        DOMParser domParser                = new DOMParser();
        Document parsedXML                 = domParser.parse(xml);
        
    	// walk the DOM tree
        processDocument(parsedXML);
        
        return users;
    }

    private void processDocument(Node n)
    {
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                if(node_name.compareTo("ibooks") == 0)
                {
                    processIbook(n);
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
    
    private void processIbook(Node n)
    {
        int type = n.getNodeType();

        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("account") == 0)
                {
                    UserAccount user = new UserAccount();
                	processAccount(n, user);
                	users.add(user);
                }

            }
            default:
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processIbook(child);
        }
    }
    
    private void processAccount(Node n, UserAccount user)
    {
        int type = n.getNodeType();

        switch (type)
        {
	        case Node.ELEMENT_NODE:
	        {
	            NamedNodeMap atts = n.getAttributes();
	            if(node_name.compareTo("account") == 0)
	            {
	                for(int i =0; i < atts.getLength(); i++)
	                {
	                    Node att = atts.item(i);
	                    processAccount(att, user);
	                }
	            }
	        }
	        break;
	        case Node.ATTRIBUTE_NODE:
	        {
	            node_name = n.getNodeName();
	            node_val = n.getNodeValue();
	            if(node_name.compareTo("emailAddr") == 0)
	            	user.setEmailAddr(getText(n));
	            if(node_name.compareTo("userId") == 0)
	                user.setUserId(getText(n));
	            if(node_name.compareTo("password") == 0)
	            	user.setPassword(getText(n));
	            if(node_name.compareTo("role") == 0)
	            	user.setRole(getText(n));
	            if(node_name.compareTo("userdir") == 0)
	            	user.setUserDir(getText(n));
	        }
            break;
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processAccount(child, user);
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
