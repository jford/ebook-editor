package com.iBook.datastore.locations;

import org.w3c.dom.*;


import java.util.Vector;

import com.iBook.datastore.*;
import com.iBook.datastore.books.*;
import com.iBook.datastore.locations.*;

public class LocationListParser
{
    private String node_name                     = "";
    private String node_val                      = "";
//    private LocationProfile location             = null;
    
    private LocationList locationList          = new LocationList();
    
    public LocationListParser()
    {
    	/* 
    	 * The LocationListParser class reads the locationList.xml file and
    	 * constructions a LocationList object for use by the PropsManager
    	 * and LocationManager objects.
    	 * 
    	 * Data is stored in the LocationList object directly from the 
    	 * DOM parser (which reads the existing locationList.xml file) or
    	 * from user action through the PropsManager/LocationManager classes.
    	 */
    }

    public LocationList parse(String xml)
    {
        // Parse the XML and create a DOM node tree
        DOMParser domParser                = new DOMParser();
        Document parsedXML                 = domParser.parse(xml);
        
        // walk the DOM tree
        processDocument(parsedXML);
        
        return locationList;
    }
    
    private void processDocument(Node n)
    {
    	processLocationList(n);
    }

    private void processLocationList(Node n)
    {
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
//                NamedNodeMap atts = n.getAttributes();

                if(node_name.compareTo("location") == 0)
                {
                	LocationProfile locationProfile = new LocationProfile();
                    // Add item to list. 
                    processLocation(n, locationProfile);
                    locationList.addLocationProfile(locationProfile);
                }
                if(node_name.compareTo("geolocale") == 0)
                {
                	GeolocaleProfile geolocaleProfile = new GeolocaleProfile();
                	processGeolocale(n, geolocaleProfile);
                	locationList.addGeolocaleProfile(geolocaleProfile);
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
    
    private void processLocation(Node n, LocationProfile locationProfile)
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
                if(node_name.compareTo("location") == 0)
                {
                    for(int i = 0; i< atts.getLength(); i++)
                    {
                    	Node att = atts.item(i);
                    	processLocation(att, locationProfile);
                    }
                }
                if(node_name.compareTo("name") == 0)
                {
                    processLocationName(n, locationProfile);
                }
                
                if(node_name.compareTo("type") == 0)
                {
                    processLocationName(n, locationProfile);
                    text = getText(n);
                    locationProfile.setType(text);
                }
                if(node_name.compareTo("context") == 0)
                {
                	processContext(n, locationProfile);
                }
                if(node_name.compareTo("books") == 0)
                {
                	processBooks(n, locationProfile);
                }
                if(node_name.compareTo("description") == 0)
                {
                	text = getText(n);
                	if(text.length() > 0)
                		locationProfile.addLocationDesc(text);
                }
            }
            break;
            case Node.ATTRIBUTE_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("id") == 0)
                {
                    locationProfile.setId(node_val);
                }
                       	
            }
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processLocation(child, locationProfile);
        }
    }
    private void processContext(Node n, LocationProfile locationProfile)
    {
        int type = n.getNodeType();
        String text = "";
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
				if(node_name.compareTo("geolocaleId") == 0)
				{
					text = getText(n);
				    locationProfile.addGeolocaleId(text);
				}
           }
            break;
       }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processContext(child, locationProfile);
        }
    }
    
    private void processGeolocale(Node n, GeolocaleProfile geolocaleProfile)
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
                if(node_name.compareTo("geolocale") == 0)
                {
                    for(int i = 0; i< atts.getLength(); i++)
                    {
                    	Node att = atts.item(i);
                    	processGeolocale(att, geolocaleProfile);
                    }
                }
				if(node_name.compareTo("name") == 0)
				{
					text = getText(n);
					geolocaleProfile.setName(text);
				}
				if(node_name.compareTo("description") == 0)
				{
					text = getText(n);
					if(text.length() > 0)
						geolocaleProfile.addDescription(text);
				}
				
				if(node_name.compareTo("type") == 0)
				{
					text = getText(n);
					if(text.length() > 0)
						geolocaleProfile.setType(text);
				}
				
                if(node_name.compareTo("alias") == 0)
                	geolocaleProfile.addAlias(getText(n));
            
           }
             break;
            case Node.ATTRIBUTE_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("id") == 0)
                {
                    geolocaleProfile.setId(node_val);
                }
            }
       }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processGeolocale(child, geolocaleProfile);
        }
    }
    private void processBooks(Node n, LocationProfile locationProfile)
    {
        int type = n.getNodeType();
        String text = "";
        
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
				if(node_name.compareTo("bookId") == 0)
				{
					text = getText(n);
					locationProfile.addBook(text);
				}
           }
            break;
       }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processBooks(child, locationProfile);
        }
    }

    private void processLocationName(Node n, LocationProfile locationProfile)
    {
    	String text = "";
    	
        int type = n.getNodeType();
        switch (type)
        {
            case Node.ELEMENT_NODE:
            {
                node_name = n.getNodeName();
                node_val = n.getNodeValue();
                if(node_name.compareTo("full") == 0)
                {
                	text = getText(n);
                    locationProfile.setName(text);
                }
                if(node_name.compareTo("alias") == 0)
                {
                	text = getText(n);
                	locationProfile.addAlias(text);
                }
            }
        }
        for(Node child = n.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
        {
            processLocationName(child, locationProfile);
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
