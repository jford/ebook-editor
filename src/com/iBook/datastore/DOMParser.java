package com.iBook.datastore;

import java.io.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.w3c.dom.*;

import com.iBook.utilities.Utilities;

public class DOMParser
{
    public DOMParser()
    {
    }

    // DOMParser only parses from a file URL or a byte stream. String argument 
    // to parse() is an URL pointing to an xml file on disc. To parse xml contained 
    // in a string, use parseFromString()
    public Document parse(String xml)
    {
/*
        ByteArrayInputStream iStream = null;
        try
        {
            iStream = new ByteArrayInputStream(xml.getBytes());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
*/
        // Build the DOM parser
        DocumentBuilderFactory documentFactory;
        documentFactory = DocumentBuilderFactory.newInstance();
                                                                                
        documentFactory.setCoalescing(true);
        documentFactory.setIgnoringComments(true);
        documentFactory.setNamespaceAware(true);
        documentFactory.setIgnoringElementContentWhitespace(true);
        documentFactory.setExpandEntityReferences(true);
                                                                                
        DocumentBuilder myDocumentBuilder = null;
        try 
        {
            myDocumentBuilder = documentFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException pce) 
        {
            System.out.println(pce);
            System.exit(1);
        }
                                                                                
        myDocumentBuilder.setErrorHandler(new MyErrorHandler());
                                                                                
        Document parsedXML = null;
        try 
        {
 //            parsedXML = myDocumentBuilder.parse(iStream);
            parsedXML = myDocumentBuilder.parse(xml);
        } 
        catch (SAXException se) 
        {
     //   	String errMsg = "in DOMParser.parse(xml) xml = " + xml;
     //   	Utilities.write_file(PropsManager.getErrorFilename(), errMsg, true);
            System.out.println("DOMParser encountered a SAX Error in parse() while attempting to parse XML:\n" + xml); 
            System.out.println(se.getMessage());
            System.exit(1);
        } 
        catch (IOException ioe) 
        {
/*        	String errMsg = "";
        	try
        	{
    	    	File currDir = new File(".");
    	    	errMsg += "in findResources(): \n" +
    	    	                "   \".\" = " + currDir.getCanonicalPath() +
    	    	                "\n";
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
        	Utilities.write_file(PropsManager.getErrorFilename(), errMsg, true);
*/        	

        	System.out.println("DOMParser encountered an IO Error while attempting to parse XML:\n" + xml);
            System.out.println(ioe);
            System.exit(1);
        }

        return parsedXML;
    }
    
    // DOMParser only parses from files or byte streams. When dealing with exisitng resources, there is a 
    // file that can be read in. No xml file available in XmlFactory, only a string---parseFromStream() 
    // converts the string into a byte stream
    public Document parseFromStream(String xml)
    {
        ByteArrayInputStream iStream = null;
        try
        {
            iStream = new ByteArrayInputStream(xml.getBytes());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        // Build the DOM parser
        DocumentBuilderFactory documentFactory;
        documentFactory = DocumentBuilderFactory.newInstance();
                                                                                
        documentFactory.setCoalescing(true);
        documentFactory.setIgnoringComments(true);
        documentFactory.setNamespaceAware(true);
        documentFactory.setIgnoringElementContentWhitespace(true);
        documentFactory.setExpandEntityReferences(true);
                                                                                
        DocumentBuilder myDocumentBuilder = null;
        try 
        {
            myDocumentBuilder = documentFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException pce) 
        {
            System.out.println(pce);
            System.exit(1);
        }
                                                                                
        myDocumentBuilder.setErrorHandler(new MyErrorHandler());
                                                                                
        Document parsedXML = null;
        try 
        {
            parsedXML = myDocumentBuilder.parse(iStream);
        } 
        catch (SAXException se) 
        {
            System.out.println("DOMParser encountered a SAX Error in parse() while attempting to parse XML:\n" + xml); 
            System.out.println(se.getMessage());
            System.exit(1);
        } 
        catch (IOException ioe) 
        {
            System.out.println("DOMParser encountered an IO Error while attempting to parse XML:\n" + xml);
            System.out.println(ioe);
            System.exit(1);
        }
        return parsedXML;
    }
    private static class MyErrorHandler implements ErrorHandler
    {
        private String getParseExceptionInfo(SAXParseException spe)
         {
            String systemid = spe.getSystemId();
            if(systemid == null)
            {
                systemid = "null";
            }
            String info = "URI=" + systemid +
                          " Line=" + spe.getLineNumber() +
                          ": " + spe.getMessage();
            return info;
        }
                                                                                
        public void warning(SAXParseException spe) throws SAXException
        {
            System.out.println("Warning: " + getParseExceptionInfo(spe));
        }
                                                                                
        public void error(SAXParseException spe) throws SAXException
        {
            String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
                                                                                
        public void fatalError(SAXParseException spe) throws SAXException
        {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }
}
