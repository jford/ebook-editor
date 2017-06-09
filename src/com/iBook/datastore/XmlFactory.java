package com.iBook.datastore;

import java.util.*;
import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;

import com.iBook.datastore.locations.*;
import com.iBook.datastore.books.*;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.manuscripts.*;
import com.iBook.datastore.users.*;
import com.iBook.utilities.Utilities;

public class XmlFactory
{
    private DOMParser parser = null;
    boolean stopForDebugging = false;
    
	public XmlFactory()
	{
		parser = new DOMParser();
	}
	
	public String doNewXml(PropsManager propsMgr, PropsManager.ObjType objType)
	{
		Document doc = null;
		
		switch(objType)
		{
		case BOOK:
			BookListFactory bookListFactory = new BookListFactory();
			doc = bookListFactory.doNewBookList(propsMgr);
			break;
		case GEOLOCALE:
		case LOCATION:
			LocationListFactory locationListFactory = new LocationListFactory();
			doc = locationListFactory.doNewLocationList(propsMgr);
			break;
		case CHARACTER:
			CharacterListFactory characterListFactory = new CharacterListFactory();
			doc = characterListFactory.doNewCharacterList(propsMgr);
			break;
		case MANUSCRIPT:
			ManuscriptFactory manuscriptFactory = new ManuscriptFactory();
			doc = manuscriptFactory.doNewManuscript(propsMgr);
			break;
		case TEMPLATE:
			TemplateFactory templateFactory = new TemplateFactory();
			doc = templateFactory.doNewTemplate(propsMgr);
			break;
		}
		return generateXml(doc);
	}
	
	public String doNewMetaXml(MetaData metaData)
	{
		Document doc = null;
		MetaXmlFactory metaXmlFactory = new MetaXmlFactory();
		doc = metaXmlFactory.doNewMetaXml(metaData);
		
		// returns contents of meta.xml
		return generateXml(doc);
	}
	
	public String doNewUserAccountsXml(Vector<UserAccount> users)
	{
		Document doc = null;
		UsersXmlFactory usersXmlFactory = new UsersXmlFactory();
		doc = usersXmlFactory.doNewUsersXml(users);
		// returns contents of userAccounts.xml
		return generateXml(doc);
	}
	
    private String generateXml(Document doc)
    {
        String newXml = "";
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = null;
        try
        {
            trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            // INDENT, sadly, doesn't indent, but it does add line breaks
            // to the XML, making it easier to read than it would otherwise be.
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            newXml = sw.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // filename set by caller when string is saved as file 
        return newXml;
    }
    
    private class UsersXmlFactory
    {
    	private Document doc = null;
    	
    	private Element accountNode = null;
    	private Element ibookNode = null;
    	private Element root = null;
    	
    	private Document doNewUsersXml(Vector<UserAccount> users)
    	{
    		String usersXml = "<accounts/>";
    		doc = parser.parseFromStream(usersXml);
    		
    		root = doc.getDocumentElement();
    		ibookNode = doc.createElement("ibooks");
    		UserAccount userAccount = null;
    		
    		Iterator<UserAccount> usersI = users.iterator();
    		while(usersI.hasNext())
    		{
    			userAccount = usersI.next();
	    		accountNode = doc.createElement("account");
	    		accountNode.setAttribute("emailAddr", userAccount.getEmailAddr());
	    		accountNode.setAttribute("userId", userAccount.getUserId());
	    		accountNode.setAttribute("password", userAccount.getPassword());
	    		accountNode.setAttribute("role", userAccount.getRole());
	    		accountNode.setAttribute("userdir", userAccount.getUserDir());
	    		ibookNode.appendChild(accountNode);
    		}
    		root.appendChild(ibookNode);
    		
    		return doc;
    	}
    }
	
    private class MetaXmlFactory
    {
    	private Document doc = null;
    	
    	private Element booksNode = null;
    	private Element charsNode = null;
    	private Element geolocsNode = null;
    	private Element idNode = null;
    	private Element locsNode = null;
    	private Element manuscriptsNode = null;
    	private Element root = null;
    	private Element templatesNode = null;
    	
    	private Document doNewMetaXml(MetaData metaData)
    	{
    		String metaXml = "<metadata/>";
    		doc = parser.parseFromStream(metaXml);
    		
    		root = doc.getDocumentElement();
    		
    		booksNode = doc.createElement("books");
    		
    		idNode = doc.createElement("id");
    		idNode.setAttribute("next", metaData.getNextBookNum());
    		booksNode.appendChild(idNode);
    		root.appendChild(booksNode);
    		
    		idNode = doc.createElement("id");
    		charsNode = doc.createElement("characters");
    		idNode.setAttribute("next", metaData.getNextCharNum());
    		charsNode.appendChild(idNode);
    		root.appendChild(charsNode);
    		
    		idNode = doc.createElement("id");
    		geolocsNode = doc.createElement("geolocales");
    		idNode.setAttribute("next", metaData.getNextGeolocNum());
    		geolocsNode.appendChild(idNode);
    		root.appendChild(geolocsNode);
    		
    		idNode = doc.createElement("id");
    		locsNode = doc.createElement("locs");
    		idNode.setAttribute("next", metaData.getNextLocNum());
    		locsNode.appendChild(idNode);
    		root.appendChild(locsNode);
    		
    		idNode = doc.createElement("id");
    		templatesNode = doc.createElement("templates");
    		idNode.setAttribute("next", metaData.getNextTemplateNum());
    		templatesNode.appendChild(idNode);
    		root.appendChild(templatesNode);
    		
    		idNode = doc.createElement("id");
    		manuscriptsNode = doc.createElement("manuscripts");
    		idNode.setAttribute("next", metaData.getNextManuscriptNum());
    		manuscriptsNode.appendChild(idNode);
    		root.appendChild(manuscriptsNode);
    		
    		return doc;
    	}
    }
	
	private class BookListFactory
	{
		private PropsManager propsMgr = null;
		private Document doc = null;
		private Element root = null;
		
		// book nodes
		private Element authorNode = null;
		private Element bookNode = null;
		private Element characterNode = null;
		private Element charactersNode = null;
//		private Element ibookNode = null;
		private Element geolocaleNode = null;
		private Element geolocalesNode = null;
		private Element locationNode = null;
		private Element locationsNode = null;
		private Element nameNode = null;
		private Element newtitleNode = null;
		private Element projectAuthorFirstNode = null;
		private Element projectAuthorLastNode = null;
		private Element projectAuthorMiddleNode = null;
		private Element projectAuthorPrefixNode = null;
		private Element projectAuthorSuffixNode = null;
		private Element sourceNode = null;
		private Element sourceAuthorFirstNode = null;
		private Element sourceAuthorLastNode = null;
		private Element sourceAuthorMiddleNode = null;
		private Element sourceAuthorPrefixNode = null;
		private Element sourceAuthorSuffixNode = null;
		private Element tCharAliasNode  = null;
		private Element tCharAttributeNode = null;
		private Element tGeolocAliasNode = null;
		private Element tGeolocAttributeNode = null;
		private Element tLocAliasNode = null;
		private Element tLocAttributeNode = null;
		private Element titleNode = null;
		
		protected Document doNewBookList(PropsManager propsMgr)
		{
			this.propsMgr = propsMgr;
			
			// create the DOM doc object...
			String bookListXml = "<booklist/>";
			doc = parser.parseFromStream(bookListXml);

			// Get the root node of the new document object
			root = doc.getDocumentElement();
			
			// create book nodes
			doBooks(root);

			return doc;
		}
		
		private boolean doBooks(Element root)
		{
			boolean ret = true;
			
			String bookId = "";
			
			BookProfile bookProfile = null;
			
			Vector<String> bookList = propsMgr.getIdList(PropsManager.ObjType.BOOK);
			Iterator<String> bookListI = bookList.iterator();
			while(bookListI.hasNext())
			{
				bookId = bookListI.next();
				bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
//				templateProfile = propsMgr.getTemplateProfile(bookProfile.getTemplate());
				
				// new book item
				bookNode = doc.createElement("book");
				bookNode.setAttribute("id", bookId);
				bookNode.setAttribute("template", bookProfile.getTemplate());
				bookNode.setAttribute("manuscriptId", bookProfile.getManuscriptId());
				
				// project's iBook title
				newtitleNode = doc.createElement("newtitle");
				newtitleNode.appendChild(getTextNode(bookProfile.getTitle()));
				bookNode.appendChild(newtitleNode);
				
				// project author
				authorNode = doc.createElement("author");
				nameNode = doc.createElement("name");
				
				// namePrefix
				projectAuthorPrefixNode = doc.createElement("prefix");
				projectAuthorPrefixNode.appendChild(getTextNode(bookProfile.getNamePrefix()));
				nameNode.appendChild(projectAuthorPrefixNode);
				
				// nameFirst
				projectAuthorFirstNode = doc.createElement("first");
				projectAuthorFirstNode.appendChild(getTextNode(bookProfile.getNameFirst()));
				nameNode.appendChild(projectAuthorFirstNode);

				// nameMiddle
				projectAuthorMiddleNode = doc.createElement("middle");
				projectAuthorMiddleNode.appendChild(getTextNode(bookProfile.getNameMiddle()));
				nameNode.appendChild(projectAuthorMiddleNode);

				// nameLast
				projectAuthorLastNode = doc.createElement("last");
				projectAuthorLastNode.appendChild(getTextNode(bookProfile.getNameLast()));
				nameNode.appendChild(projectAuthorLastNode);

				// nameSuffix
				projectAuthorSuffixNode = doc.createElement("suffix");
				projectAuthorSuffixNode.appendChild(getTextNode(bookProfile.getNameSuffix()));
				nameNode.appendChild(projectAuthorSuffixNode);

				// append name node to author node, author node to book node
				authorNode.appendChild(nameNode);
				bookNode.appendChild(authorNode);
				
				sourceNode = doc.createElement("source");
				titleNode = doc.createElement("title");
				titleNode.appendChild(getTextNode(bookProfile.getSourceTitle()));
				sourceNode.appendChild(titleNode);
				
				// source author
				authorNode = doc.createElement("author");
				
				// source author prefix
				sourceAuthorPrefixNode = doc.createElement("prefix");
				sourceAuthorPrefixNode.appendChild(getTextNode(bookProfile.getSourceAuthorNamePrefix()));
				authorNode.appendChild(sourceAuthorPrefixNode);
				
				// source author first
				sourceAuthorFirstNode = doc.createElement("first");
				sourceAuthorFirstNode.appendChild(getTextNode(bookProfile.getSourceAuthorNameFirst()));
				authorNode.appendChild(sourceAuthorFirstNode);
				
				// source author middle
				sourceAuthorMiddleNode = doc.createElement("middle");
				sourceAuthorMiddleNode.appendChild(getTextNode(bookProfile.getSourceAuthorNameMiddle()));
				authorNode.appendChild(sourceAuthorMiddleNode);
				
				// source author last
				sourceAuthorLastNode = doc.createElement("last");
				sourceAuthorLastNode.appendChild(getTextNode(bookProfile.getSourceAuthorNameLast()));
				authorNode.appendChild(sourceAuthorLastNode);
				
				// source author suffix
				sourceAuthorSuffixNode = doc.createElement("suffix");
				sourceAuthorSuffixNode.appendChild(getTextNode(bookProfile.getSourceAuthorNameSuffix()));
				authorNode.appendChild(sourceAuthorSuffixNode);
				
				// append author node to source node, source node to book node
				sourceNode.appendChild(authorNode);
				bookNode.appendChild(sourceNode);
				
				// characters
				charactersNode = doc.createElement("characters");
				Vector<String[]> subMatrix = bookProfile.getCharSubMatrix();
				Vector<String[]> aliasSubMatrix = bookProfile.getAliasSubMatrix();
				Vector<String[]> attributeSubMatrix = bookProfile.getAttributeSubMatrix();
				Iterator<String[]> attributesI = null;
				
				// character aliases
				Iterator<String[]> aliasesI = null;
				Iterator<String[]> matrixI = subMatrix.iterator();
				String tObjId = "";
				String[] matrixEntry = null;
				while(matrixI.hasNext())
				{
					matrixEntry = (String[])matrixI.next();
					characterNode = doc.createElement("character");
					tObjId = matrixEntry[0];
					characterNode.setAttribute("tCharId", matrixEntry[0]);
					characterNode.setAttribute("iBookCharId", matrixEntry[1]);
					
					if(aliasSubMatrix.size() > 0)
					{
						aliasesI = aliasSubMatrix.iterator();
						String[] aliasSubEntry = null;
						while(aliasesI.hasNext())
						{
							aliasSubEntry = aliasesI.next();
							if(aliasSubEntry[0].compareTo(tObjId) == 0)
							{
								tCharAliasNode = doc.createElement("tCharAlias");
								tCharAliasNode.setAttribute("alias", aliasSubEntry[1]);
								tCharAliasNode.setAttribute("substituteText", aliasSubEntry[2]);
								characterNode.appendChild(tCharAliasNode);
							}
						}
					}
					// character attributes
					if(attributeSubMatrix.size() > 0)
					{
						attributesI = attributeSubMatrix.iterator();
						String[] attributeSubEntry = null;
						while(attributesI.hasNext())
						{
							attributeSubEntry = attributesI.next();
							if(attributeSubEntry[0].compareTo(tObjId) == 0)
							{
								tCharAttributeNode = doc.createElement("tCharAttribute");
								tCharAttributeNode.setAttribute("attribute", attributeSubEntry[1]);
								tCharAttributeNode.setAttribute("substituteText", attributeSubEntry[2]);
								characterNode.appendChild(tCharAttributeNode);
							}
						}
					}
					charactersNode.appendChild(characterNode);
				}
				bookNode.appendChild(charactersNode);
				
				// locations
				locationsNode = doc.createElement("locations");
				subMatrix = bookProfile.getLocSubMatrix();
				aliasesI = aliasSubMatrix.iterator();
				matrixI = subMatrix.iterator();
				matrixEntry = null;
				while(matrixI.hasNext())
				{
					matrixEntry = (String[])matrixI.next();
					tObjId = matrixEntry[0];
					locationNode = doc.createElement("location");
					locationNode.setAttribute("tLocId", matrixEntry[0]);
					locationNode.setAttribute("iBookLocId", matrixEntry[1]);
					locationsNode.appendChild(locationNode);

					// location aliases
					if(aliasSubMatrix.size() > 0)
					{
						aliasesI = aliasSubMatrix.iterator();
						String[] aliasSubEntry = null;
						while(aliasesI.hasNext())
						{
							aliasSubEntry = aliasesI.next();
							if(aliasSubEntry[0].compareTo(tObjId) == 0)
							{
								tLocAliasNode = doc.createElement("tLocAlias");
								tLocAliasNode.setAttribute("alias", aliasSubEntry[1]);
								tLocAliasNode.setAttribute("substituteText", aliasSubEntry[2]);
								locationNode.appendChild(tLocAliasNode);
							}
						}
					}
					// location attributes
					if(attributeSubMatrix.size() > 0)
					{
						attributesI = attributeSubMatrix.iterator();
						String[] attributeSubEntry = null;
						while(attributesI.hasNext())
						{
							attributeSubEntry = attributesI.next();
							if(attributeSubEntry[0].compareTo(tObjId) == 0)
							{
								tLocAttributeNode = doc.createElement("tLocAttribute");
								tLocAttributeNode.setAttribute("attribute", attributeSubEntry[1]);
								tLocAttributeNode.setAttribute("substituteText", attributeSubEntry[2]);
								locationNode.appendChild(tLocAttributeNode);
							}
						}
					}
					locationsNode.appendChild(locationNode);
				}
				bookNode.appendChild(locationsNode);
				
				// geolocales
				geolocalesNode = doc.createElement("geolocales");
				subMatrix = bookProfile.getGeolocSubMatrix();
				matrixI = subMatrix.iterator();
				matrixEntry = null;
				while(matrixI.hasNext())
				{
					matrixEntry = (String[])matrixI.next(); 
					tObjId = matrixEntry[0];
					geolocaleNode = doc.createElement("geolocale");
					geolocaleNode.setAttribute("tGeolocId", matrixEntry[0]);
					geolocaleNode.setAttribute("iBookGeolocId", matrixEntry[1]);
					geolocalesNode.appendChild(geolocaleNode);
					
					// geoloc aliases
					if(aliasSubMatrix.size() > 0)
					{
						aliasesI = aliasSubMatrix.iterator();
						String[] aliasSubEntry = null;
						while(aliasesI.hasNext())
						{
							aliasSubEntry = aliasesI.next();
							if(aliasSubEntry[0].compareTo(tObjId) == 0)
							{
								tLocAliasNode = doc.createElement("tGeolocAlias");
								tLocAliasNode.setAttribute("alias", aliasSubEntry[1]);
								tLocAliasNode.setAttribute("substituteText", aliasSubEntry[2]);
								geolocaleNode.appendChild(tLocAliasNode);
							}
						}
					}
					
					// geoloc attributes
					if(attributeSubMatrix.size() > 0)
					{
						attributesI = attributeSubMatrix.iterator();
						String[] attributeSubEntry = null;
						while(attributesI.hasNext())
						{
							attributeSubEntry = attributesI.next();
							if(attributeSubEntry[0].compareTo(tObjId) == 0)
							{
								tGeolocAttributeNode = doc.createElement("tGeolocAttribute");
								tGeolocAttributeNode.setAttribute("attribute", attributeSubEntry[1]);
								tGeolocAttributeNode.setAttribute("substituteText", attributeSubEntry[2]);
								geolocaleNode.appendChild(tGeolocAttributeNode);
							}
						}
					}
					geolocalesNode.appendChild(geolocaleNode);
				}
				bookNode.appendChild(geolocalesNode);
				
				root.appendChild(bookNode);
			}
			return ret;
		}
		private Text getTextNode(String text)
		{
			return doc.createTextNode(text);
		}
	}
	
	private class CharacterListFactory
	{
		private PropsManager propsMgr = null;
	    private Document doc = null;
	    private Element root = null;
	    
	    // character nodes
	    private Element ageNode = null;
	    private Element aliasesNode = null;
	    private Element aliasNode = null;
	    private Element attributeNode = null;
	    private Element attributesNode = null;
	    private Element booksNode = null;
	    private Element bookNode = null;
	    private Element characterNode = null;
	    private Element genderNode = null;
	    private Element nameNode = null;
	    private Element namePrefixNode = null;
	    private Element nameFirstNode = null;
	    private Element nameMiddleNode = null;
	    private Element nameLastNode = null;
	    private Element nameSuffixNode = null;
	    private Element shortNameNode = null;

		protected Document doNewCharacterList(PropsManager propsMgr)
		{
			// create a new characterList.xml based on updated data contained in the new propsMgr
			this.propsMgr = propsMgr;
			
			// create the DOM doc object...
			String characterListXml = "<characterlist/>";
			doc = parser.parseFromStream(characterListXml);

			// Get the root node of the new document object
			root = doc.getDocumentElement();
			
			// create location nodes
			doCharacters(root);
			
			return doc;
		}
		private Document doCharacters(Element root)
		{
//			String characterListXml = "";
			
			String shortName = "";
			String namePrefix = "";
			String nameFirst = "";
			String nameMiddle = "";
			String nameLast = "";
			String nameSuffix = "";
			String gender = "";
			String age = "";
			String attribute = "";
			String bookId = "";
			
			Vector<String> aliases = null;
			Vector<String> attributes = null;
			Vector<String> bookIds = null;
			String id = "";
			
			CharacterProfile profile = null;
			
			Vector<String> characterIdList = propsMgr.getCharacterIdList();
			Iterator<String> charIdsI = characterIdList.iterator();
			
			while(charIdsI.hasNext())
			{
				id=(String)charIdsI.next();
				profile = propsMgr.getCharacterProfile(id);

				characterNode = doc.createElement("character");
				characterNode.setAttribute("id",  id);
				
				// start with names
				namePrefix = profile.getNamePrefix();
				nameFirst = profile.getNameFirst();
				nameMiddle = profile.getNameMiddle();
				nameLast = profile.getNameLast();
				nameSuffix = profile.getNameSuffix();
				shortName = profile.getShortName();
				
				aliases = profile.getAliases();
				
				// add character's names
				nameNode = doc.createElement("name");
				
				// suffix
				namePrefixNode = doc.createElement("namePrefix");
				if(namePrefix.length() > 0)
					namePrefixNode.appendChild(getTextNode(namePrefix));
				nameNode.appendChild(namePrefixNode);

				// first
				nameFirstNode = doc.createElement("nameFirst");
				if(nameFirst.length() > 0)
					nameFirstNode.appendChild(getTextNode(nameFirst));
				nameNode.appendChild(nameFirstNode);

				// middle
				nameMiddleNode = doc.createElement("nameMiddle");
				if(nameMiddle.length() > 0)
					nameMiddleNode.appendChild(getTextNode(nameMiddle));
				nameNode.appendChild(nameMiddleNode);

				// last
				nameLastNode = doc.createElement("nameLast");
				if(nameLast.length() > 0)
					nameLastNode.appendChild(getTextNode(nameLast));
				nameNode.appendChild(nameLastNode);
				
				nameNode.appendChild(nameLastNode);

				// suffix
				nameSuffixNode = doc.createElement("nameSuffix");
				if(nameSuffix.length() > 0)
					nameSuffixNode.appendChild(getTextNode(nameSuffix));
				nameNode.appendChild(nameSuffixNode);
				
				// short
				shortNameNode = doc.createElement("nameShort");
				if(shortName.length() > 0)
					shortNameNode.appendChild(getTextNode(shortName));
				nameNode.appendChild(shortNameNode);
				
				// aliases?
				if(aliases.size() > 0)
				{
					aliasesNode = doc.createElement("aliases");
					Iterator<String> aliasesI = aliases.iterator();
					while(aliasesI.hasNext())
					{
						aliasNode = doc.createElement("alias");
						aliasNode.appendChild(getTextNode((String)aliasesI.next()));
						aliasesNode.appendChild(aliasNode);
					}
					nameNode.appendChild(aliasesNode);
				}
				
				// name node finished, add it to character node
				characterNode.appendChild(nameNode);
				
				// add gender
				gender = profile.getGender();
				genderNode = doc.createElement("gender");
				if(gender.length() > 0)
					genderNode.appendChild(getTextNode(gender));
				characterNode.appendChild(genderNode);
				
				// add age
				age = profile.getAge();
				ageNode = doc.createElement("age");
				if(age.length() > 0)
					ageNode.appendChild(getTextNode(age));
				characterNode.appendChild(ageNode);
				
				// add attributes
				attributesNode = doc.createElement("attributes");
				
				attributes = propsMgr.getAttributes(PropsManager.ObjType.CHARACTER, id);
				Iterator<String> attsI = attributes.iterator();
				
				while(attsI.hasNext())
				{
					attribute = (String)attsI.next();
					if(attribute.length() > 0)
					{
						attributeNode = doc.createElement("attribute");
						attributeNode.appendChild(getTextNode(attribute));
						attributesNode.appendChild(attributeNode);
					}
				}
				characterNode.appendChild(attributesNode);
					
				// add bookIds
				booksNode = doc.createElement("books");
				
				bookIds = propsMgr.getIdList(PropsManager.ObjType.BOOK);
				Iterator<String>booksI = bookIds.iterator();
				
				while(booksI.hasNext())
				{
					bookId = (String)booksI.next();
					if(bookId.length() > 0)
					{
						bookNode = doc.createElement("book");
						bookNode.appendChild(getTextNode(bookId));
						booksNode.appendChild(bookNode);
					}
				}
				characterNode.appendChild(booksNode);
				
				root.appendChild(characterNode);
			}
			return doc;
		}
		
		private Text getTextNode(String text)
		{
			return doc.createTextNode(text);
		}
	}	
	
	private class ManuscriptFactory
	{
		private Document doc = null;

		private Element descriptorNode = null;
		private Element descriptorsNode = null;
		private Element filebreakNode = null;
		private Element iBookAuthorNode = null;
		private Element iBookTitleNode = null;
		private Element root  = null;
		private Element sourceAuthorNode = null;
		private Element sourcePubDateNode = null;
		private Element sourceTitleNode = null;
		private Element textBlockNode = null;
		
		protected Document doNewManuscript(PropsManager propsMgr)
		{
			Manuscript manuscript = propsMgr.getActiveManuscript();
			
			doc = parser.parseFromStream("<manuscript/>");
			
			root = doc.getDocumentElement();
			root.setAttribute("id", manuscript.getId());
			root.setAttribute("name", manuscript.getMsName());
			root.setAttribute("filename", manuscript.getFilename());
			root.setAttribute("lastEditedTextblock", manuscript.getLastEditedTextblock());
			root.setAttribute("bookId", manuscript.getBookId());
					
			// source title
			sourceTitleNode = doc.createElement("sourcetitle");
			sourceTitleNode.appendChild(getTextNode(manuscript.getSourceTitle()));
			root.appendChild(sourceTitleNode);
			
			// source author name
			sourceAuthorNode = doc.createElement("sourceauthor");
			sourceAuthorNode.appendChild(getTextNode(manuscript.getSourceAuthor()));
			root.appendChild(sourceAuthorNode);
			
			// source pub date
			sourcePubDateNode = doc.createElement("sourcepubdate");
			sourcePubDateNode.appendChild(getTextNode(manuscript.getSourcePubDate()));
			root.appendChild(sourcePubDateNode);
			
			// iBook title
			iBookTitleNode = doc.createElement("ibooktitle");
			iBookTitleNode.appendChild(getTextNode(manuscript.getIBookTitle()));
			root.appendChild(iBookTitleNode);
			
			// iBook author name
			iBookAuthorNode = doc.createElement("ibookauthor");
			iBookAuthorNode.appendChild(getTextNode(manuscript.getIBookAuthor()));
			root.appendChild(iBookAuthorNode);
			
			descriptorsNode = doc.createElement("descriptors");
			
			Vector<String[]> descriptors = manuscript.getDescriptors();
			String[] descriptor = null;
			
			Iterator<String[]> descriptorsI = descriptors.iterator();
			/*
			 *  descriptor[] = {tCharId, textblockIdx, sourceText, newText}
			 *  
			 *  tCharId       id of the template character referred to by the descriptor text
			 *  textblockIdx  index into the texblock vector (0-based, unlike textblockIds, which start at 1)
			 *  sourceText    txt used in the original text
			 *  newText       text to be used in the output text
			 */
			while(descriptorsI.hasNext())
			{
				descriptor = descriptorsI.next();
				descriptorNode = doc.createElement("descriptor");
				descriptorNode.setAttribute("tCharId", descriptor[0]);
				descriptorNode.setAttribute("textblocksIdx", descriptor[1]);
				descriptorNode.setAttribute("sourceText", descriptor[2]);
				descriptorNode.setAttribute("newText", descriptor[3]);
				descriptorsNode.appendChild(descriptorNode);
			}
			root.appendChild(descriptorsNode);
			
			Vector<String> textBlocks = manuscript.getTextblocks();
			Iterator<String> textBlocksI = textBlocks.iterator();
			int textBlockCount = 0;
			String text;
			String id = "";
			while(textBlocksI.hasNext())
			{
				id = manuscript.getMsName() + "_textblock_" + Integer.toString(++textBlockCount);
				
				if(id.indexOf("273") != -1)
					stopForDebugging = true;
				
				text = (String)textBlocksI.next();
				textBlockNode = doc.createElement("textblock");
				textBlockNode.setAttribute("id",  id);
				textBlockNode.appendChild(getCDataNode(text));
				root.appendChild(textBlockNode);
			}
			
			Vector<Integer> filebreaks = manuscript.getFilebreaks();
			Iterator<Integer> filebreaksI = filebreaks.iterator();
			String filebreak = "";
			while(filebreaksI.hasNext())
			{
				filebreak = filebreaksI.next().toString();
				filebreakNode = doc.createElement("filebreak");
				filebreakNode.setAttribute("id", filebreak);
				root.appendChild(filebreakNode);
			}
			
			return doc;
		}
		
		private Text getTextNode(String text)
		{
			return doc.createTextNode(text);
		}
		
		private CDATASection getCDataNode(String text)
		{
			return doc.createCDATASection(text);
		}
	}
	
	private class TemplateFactory
	{
		private Document doc = null;

		private Element ageNode = null;
		private Element akaNode = null;
		private Element aliasesNode = null;
		private Element aliasNode = null;
		private Element attributeNode = null;
		private Element attributesNode = null;
		private Element authorNode = null;
		private Element characterNode = null;
		private Element charactersNode = null;
		private Element contextNode = null;
		private Element descriptionNode = null;
		private Element fullNode = null;
		private Element genderNode = null;
		private Element geolocaleNode = null;
		private Element geolocalesNode = null;
		private Element locationNode = null;
		private Element locationsNode = null;
		private Element nameNode = null;
		private Element nameContainerNode = null;
		private Element namePrefixNode = null;
		private Element nameFirstNode = null;
		private Element nameMiddleNode = null;
		private Element nameLastNode = null;
		private Element nameSuffixNode = null;
		private Element nameShortNode = null;
		private Element pubdateNode = null;
		private Element pubtypeNode = null;
		private Element root = null;
		private Element textBlockNode = null;
		private Element titleNode = null;
		private Element typeNode = null;
		
		protected Document doNewTemplate(PropsManager propsMgr)
		{
			String template = "<template/>";
			TemplateProfile profile = propsMgr.getActiveTemplate();
			String id = profile.getId();
			// filename is name of the xml file
			String filename = profile.getFilename();
			// objname is filename minus the .xml, for use in creating the template id
			String objName = filename.substring(0, filename.indexOf("."));
			if(id.length() == 0)
				id = propsMgr.getId(PropsManager.ObjType.TEMPLATE, objName);
			
			doc = parser.parseFromStream(template);
			
			root = doc.getDocumentElement();
//			root.setAttribute("id", profile.getId());
			root.setAttribute("id", id);
			root.setAttribute("filename", filename);
			root.setAttribute("lastEditedTextblock", profile.getLastEditedTextblock());
					
			// title
			titleNode = doc.createElement("title");
			titleNode.appendChild(getTextNode(profile.getTitle()));
			root.appendChild(titleNode);
			
			// author name
			authorNode = doc.createElement("author");

			namePrefixNode = doc.createElement("namePrefix");
			namePrefixNode.appendChild(getTextNode(profile.getAuthorNamePrefix()));
			authorNode.appendChild(namePrefixNode);

			nameFirstNode = doc.createElement("nameFirst");
			nameFirstNode.appendChild(getTextNode(profile.getAuthorNameFirst()));
			authorNode.appendChild(nameFirstNode);

			nameMiddleNode = doc.createElement("nameMiddle");
			nameMiddleNode.appendChild(getTextNode(profile.getAuthorNameMiddle()));
			authorNode.appendChild(nameMiddleNode);

			nameLastNode = doc.createElement("nameLast");
			nameLastNode.appendChild(getTextNode(profile.getAuthorNameLast()));
			authorNode.appendChild(nameLastNode);

			nameSuffixNode = doc.createElement("nameSuffix");
			nameSuffixNode.appendChild(getTextNode(profile.getAuthorNameSuffix()));
			authorNode.appendChild(nameSuffixNode);

			nameShortNode = doc.createElement("nameShort");
			nameShortNode.appendChild(getTextNode(profile.getAuthorNameShort()));
			authorNode.appendChild(nameShortNode);
			
			root.appendChild(authorNode);
			
			// publication date
			pubdateNode = doc.createElement("pubdate");
			pubdateNode.appendChild(getTextNode(profile.getPubdate()));
			root.appendChild(pubdateNode);
			
			// publication type
			pubtypeNode = doc.createElement("pubtype");
			pubtypeNode.appendChild(getTextNode(profile.getPubtype()));
			root.appendChild(pubtypeNode);
			
// characters
			
			charactersNode = doc.createElement("characters");
			Vector<CharacterProfile> tChars = profile.getCharacters();
			Vector<String> tCharAliases = null;
			String alias = "";
			Vector<String> tCharAtts = null;
			String attribute = "";
			Iterator<CharacterProfile> tCharsI = tChars.iterator();
			CharacterProfile tChar = null;
			
			// initialize character id attribute string
			String idBase = profile.getFilename();
			int idx = idBase.indexOf(".xml");
			idBase = idBase.substring(0, idx) + "_template_";
			id = idBase + "char_";

			int charCount = 0;
			while(tCharsI.hasNext())
			{
				tChar = (CharacterProfile)tCharsI.next();
				characterNode = doc.createElement("character");
				// all characters/locations are created in ManageProfile, and they all get an id
				// that takes the form of ???_iBookChar/Loc_nnn, where nnn is generated by the metaData
				// object. The id for template objects will take a different form, using 
				// the id string created here but preserving the metaData-generated number.
				// this id is created by extracting the id from the profile, droping the text portion
				// and replacing it with the value of id, but keeping the num portion of 
				// whatever was stored in the profile. This will change the initial
				// portion the first time it comes through this section of code, from then on
				// the id will be constructed fresh each time but it should be the same id, unchanged from
				// run to run of the program, no matter how many template objects get created of deleted.
				characterNode.setAttribute("id", id + tChar.getId().substring(tChar.getId().lastIndexOf("_") + 1));
			
				// name
				nameNode = doc.createElement("name");
				
				namePrefixNode = doc.createElement("namePrefix");
				namePrefixNode.appendChild(getTextNode(tChar.getNamePrefix()));
				nameNode.appendChild(namePrefixNode);
				
				nameFirstNode = doc.createElement("nameFirst");
				nameFirstNode.appendChild(getTextNode(tChar.getNameFirst()));
				nameNode.appendChild(nameFirstNode);
				
				nameMiddleNode = doc.createElement("nameMiddle");
				nameMiddleNode.appendChild(getTextNode(tChar.getNameMiddle()));
				nameNode.appendChild(nameMiddleNode);
				
				nameLastNode = doc.createElement("nameLast");
				nameLastNode.appendChild(getTextNode(tChar.getNameLast()));
				nameNode.appendChild(nameLastNode);
				
				nameSuffixNode = doc.createElement("nameSuffix");
				nameSuffixNode.appendChild(getTextNode(tChar.getNameSuffix()));
				nameNode.appendChild(nameSuffixNode);
				
				nameShortNode = doc.createElement("nameShort");
				nameShortNode.appendChild(getTextNode(tChar.getShortName()));
				nameNode.appendChild(nameShortNode);
				
				characterNode.appendChild(nameNode);
				
				contextNode = doc.createElement("context");
				contextNode.appendChild(getTextNode(tChar.getContext()));
				characterNode.appendChild(contextNode);

				aliasesNode = doc.createElement("aliases");
				tCharAliases = tChar.getAliases();
				Iterator<String> tCharAliasesI = tCharAliases.iterator();
				while(tCharAliasesI.hasNext())
				{
					alias = (String)tCharAliasesI.next();
					aliasNode = doc.createElement("alias");
					aliasNode.appendChild(getTextNode(alias));
					aliasesNode.appendChild(aliasNode);
				}
				characterNode.appendChild(aliasesNode);
				
				attributesNode = doc.createElement("attributes");
						
				tCharAtts = tChar.getAttributes();
				Iterator<String> tCharAttsI = tCharAtts.iterator();
				while(tCharAttsI.hasNext())
				{
					attribute = (String)tCharAttsI.next();
					attributeNode = doc.createElement("attribute");
					attributeNode.appendChild(getTextNode(attribute));
					attributesNode.appendChild(attributeNode);
				}
				characterNode.appendChild(attributesNode);
				
				ageNode = doc.createElement("age");
				ageNode.appendChild(getTextNode(tChar.getAge()));
				characterNode.appendChild(ageNode);
				
				genderNode = doc.createElement("gender");
				genderNode.appendChild(getTextNode(tChar.getGender()));
				characterNode.appendChild(genderNode);
				
				charactersNode.appendChild(characterNode);
			}
			root.appendChild(charactersNode);
			
// locations
			
			locationsNode = doc.createElement("locations");
			Vector<LocationProfile> tLocs = profile.getLocations();
			alias = "";
			String description = "";
			Iterator<LocationProfile> tLocsI = tLocs.iterator();
			LocationProfile tLoc = null;
			
			charCount = 0;
			while(tLocsI.hasNext())
			{
				tLoc = (LocationProfile)tLocsI.next();
				// initialize location id attribute string
				id = idBase + "loc_";

				locationNode = doc.createElement("location");
				// all characters/locations are created in ManageProfile, and they all get an id
				// that takes the form of ???_iBookChar/Loc_nnn, where nnn is generated by the metaData
				// object. The id for template objects will take a different form, using 
				// the id string created here but preserving the metaData-generated number.
				// this id is created by extracting the id from the profile, droping the text portion
				// and replacing it with the value of id, but keeping the num portion of 
				// whatever was stored in the profile. This will change the initial
				// portion the first time it comes through this section of code, from then on
				// the id will be constructed fresh each time but it should be the same id, unchanged from
				// run to run of the program, no matter how many template objects get created of deleted.
				locationNode.setAttribute("id", id + tLoc.getId().substring(tLoc.getId().lastIndexOf("_") + 1));
			
				// name
				nameNode = doc.createElement("name");
				fullNode = doc.createElement("full");
				fullNode.appendChild(getTextNode(tLoc.getName()));
				nameNode.appendChild(fullNode);
				
				akaNode = doc.createElement("aka");
				Vector<String> aliases = tLoc.getAliases();
				Iterator<String> aliasesI = aliases.iterator();
				while(aliasesI.hasNext())
				{
					alias = (String)aliasesI.next();
					aliasNode = doc.createElement("alias");
					aliasNode.appendChild(getTextNode(alias));
					akaNode.appendChild(aliasNode);
				}
				nameNode.appendChild(akaNode);
				locationNode.appendChild(nameNode);
				
				Vector<String> descriptions = tLoc.getLocationDescriptions();
				Iterator<String> descI = descriptions.iterator();
				while(descI.hasNext())
				{
					description = (String)descI.next();
					if(description.length() > 0)
					{
						descriptionNode = doc.createElement("description");
						descriptionNode.appendChild(getTextNode(description));
						locationNode.appendChild(descriptionNode);
					}
				}
				
				typeNode = doc.createElement("type");
				typeNode.appendChild(getTextNode(tLoc.getType()));
				locationNode.appendChild(typeNode);
				
			    locationsNode.appendChild(locationNode);
			}
			root.appendChild(locationsNode);
				
// geolocs
			
			geolocalesNode = doc.createElement("geolocales");
			
			geolocaleNode = doc.createElement("geolocale");
			Vector<GeolocaleProfile> tGeolocs = profile.getGeolocs();
			description = "";
			Iterator<GeolocaleProfile> tGeolocsI = tGeolocs.iterator();
			GeolocaleProfile tGeoloc = null;
			
			charCount = 0;
			while(tGeolocsI.hasNext())
			{
				tGeoloc = (GeolocaleProfile)tGeolocsI.next();
				// initialize location id attribute string
				id = idBase + "geoloc_";

				geolocaleNode = doc.createElement("geolocale");
				// set location attribute
//				geolocaleNode.setAttribute("id", id + Integer.toString(++charCount));
				geolocaleNode.setAttribute("id", id + tGeoloc.getId().substring(tGeoloc.getId().lastIndexOf("_") + 1));
				
				nameContainerNode = doc.createElement("name");
				nameNode = doc.createElement("name");
				nameNode.appendChild(getTextNode(tGeoloc.getName()));
				nameContainerNode.appendChild(nameNode);
				
				akaNode = doc.createElement("aka");
				Vector<String> aliases = tGeoloc.getAliases();
				Iterator<String> aliasesI = aliases.iterator();
				while(aliasesI.hasNext())
				{
					alias = (String)aliasesI.next();
					aliasNode = doc.createElement("alias");
					aliasNode.appendChild(getTextNode(alias));
					akaNode.appendChild(aliasNode);
				}
				nameContainerNode.appendChild(akaNode); 		 
				
				geolocaleNode.appendChild(nameContainerNode);
				
				typeNode = doc.createElement("type");
				typeNode.appendChild(getTextNode(tGeoloc.getType()));
				geolocaleNode.appendChild(typeNode);
				
				Vector<String> descriptions = tGeoloc.getDescriptions();
				Iterator<String> descI = descriptions.iterator();
				while(descI.hasNext())
				{
					description = (String)descI.next();
					if(description.length() > 0)
					{
						descriptionNode = doc.createElement("description");
						descriptionNode.appendChild(getTextNode(description));
						geolocaleNode.appendChild(descriptionNode);
					}
				}
				geolocalesNode.appendChild(geolocaleNode);
			}
			root.appendChild(geolocalesNode);
			
			Vector<String> textBlocks = profile.getTextblocks();
			Iterator<String> textBlocksI = textBlocks.iterator();
			int textBlockCount = 0;
			String text;
			while(textBlocksI.hasNext())
			{
				id = idBase + "textblock_" + Integer.toString(++textBlockCount);
				text = (String)textBlocksI.next();
				textBlockNode = doc.createElement("textblock");
				textBlockNode.setAttribute("id",  id);
				textBlockNode.appendChild(getCDataNode(text));
				root.appendChild(textBlockNode);
			}
			return doc;
		}
		
		private Text getTextNode(String text)
		{
			return doc.createTextNode(text);
		}
		
		private CDATASection getCDataNode(String text)
		{
			return doc.createCDATASection(text);
		}
	}
	
	// begin LocationListFactory class
	private class LocationListFactory
	{
		PropsManager propsMgr = null;
	    private Document doc = null;
	    private Element root = null;
		
		// location nodes
		private Element locationNode = null;
		private Element fullNameNode = null;
		private Element akaNode = null;
		private Element aliasListNode = null;
		private Element aliasNode = null;
		private Element typeNode = null;
		private Element contextNode = null;
		private Element geolocaleIdNode = null;
		private Element booksNode = null;
		private Element bookIdNode = null;
//		private Element locIdNode = null;
//		private Element textNode = null;
		
		// geolocale nodes
		private Element geolocaleNode = null;

		// location and geolocale shared nodes
		private Element descriptionNode = null;
		private Element nameNode = null;
		private Element nameContainerNode = null;

		protected Document doNewLocationList(PropsManager propsMgr)
		{
			// create a new locationList.xml based on updated data contained in the new propsMgr
			this.propsMgr = propsMgr;
			
			// create the DOM doc object...
			String locationListXml = "<locationlist/>";
			doc = parser.parseFromStream(locationListXml);

			// Get the root node of the new document object
			root = doc.getDocumentElement();
			
			// create location nodes
			doLocations(root);
			
			// create geolocale nodes
			doGeolocales(root);
			
			locationListXml = generateXml(doc);
	
			return doc;
		}

		private Document doLocations(Element root)
		{
			LocationProfile profile = null;
			String id = "";
			String geolocaleId = "";
			String name = "";
			
			Vector<String>locationIdList = propsMgr.getLocationIdList();
			Iterator<String> locIdI = locationIdList.iterator();
			while(locIdI.hasNext())
			{
				id = (String)locIdI.next();
				profile = (LocationProfile)propsMgr.getProfile(PropsManager.ObjType.LOCATION, id);
				name = profile.getName();
				
	            locationNode = doc.createElement("location");
	            locationNode.setAttribute("id", id);

				// name
				nameNode = doc.createElement("name");
				
				// full name
				fullNameNode = doc.createElement("full");
				fullNameNode.appendChild(getTextNode(name));
				nameNode.appendChild(fullNameNode);
				
				// aka list
				aliasListNode = doc.createElement("aka");
				Vector<String> aliases = profile.getAliases();
				Iterator<String> aliasI = aliases.iterator();
				while(aliasI.hasNext())
				{
					aliasNode = doc.createElement("alias");
					aliasNode.appendChild(getTextNode((String)aliasI.next()));
					aliasListNode.appendChild(aliasNode);
				}
				nameNode.appendChild(aliasListNode);
				
				// full name and aka list have been attached, now add name node to location node
				locationNode.appendChild(nameNode);
	
				// descriptions (can be multiple)
				Vector<String> descriptions = profile.getLocationDescriptions();
				Iterator<String> descI = descriptions.iterator();
				while(descI.hasNext())
				{
					descriptionNode = doc.createElement("description");
					descriptionNode.appendChild(getTextNode((String)descI.next()));
					// attach each description node to the location node
					locationNode.appendChild(descriptionNode);
				}
				
				// type
				typeNode = doc.createElement("type");
				typeNode.appendChild(getTextNode(profile.getType()));
				locationNode.appendChild(typeNode);
				
				// context
				contextNode = doc.createElement("context");
				Vector<String> contexts = profile.getGeolocaleIds();
				Iterator<String> contextI = contexts.iterator();
				// add each geolocale node to the context node...
				while(contextI.hasNext())
				{
					geolocaleIdNode = doc.createElement("geolocaleId");
					geolocaleId = (String)contextI.next();
					geolocaleIdNode.appendChild(getTextNode(geolocaleId));
					contextNode.appendChild(geolocaleIdNode);
				}
				// add the context node to the location node
				locationNode.appendChild(contextNode);
				
				// books
				booksNode = doc.createElement("books");
				Vector<String> books = profile.getBooks();
				Iterator<String> booksI = books.iterator();
				// add each bookId node to the books node...
				while(booksI.hasNext())
				{
					bookIdNode = doc.createElement("bookId");
					bookIdNode.appendChild(getTextNode((String)booksI.next()));
					booksNode.appendChild(bookIdNode);
				}
				// add the books node to the location node
				locationNode.appendChild(booksNode);
				
				// done with this location, add it to the root (locationList) node
				root.appendChild(locationNode);
			}
			return doc;
		}
		
		private void doGeolocales(Element root)
		{
			GeolocaleProfile profile = null;
			String alias = "";
			String id = "";
			String name = "";
			String type = "";
			String description = "";
			
			Vector<String> geolocaleIdList = propsMgr.getGeolocaleIdList();
			Iterator<String> idI = geolocaleIdList.iterator();
			while(idI.hasNext())
			{
				id = (String)idI.next();
				profile = propsMgr.getGeolocale(id);
				name = propsMgr.getGeolocaleName(id);
				type = propsMgr.getGeolocaleType(id);
				
				// create the geolocale node...
				geolocaleNode = doc.createElement("geolocale");
				// ..and set its id attribute
	            geolocaleNode.setAttribute("id", id);

				// name
/*				nameNode = doc.createElement("name");
				nameNode.appendChild(getTextNode(name));
				geolocaleNode.appendChild(nameNode);
*/				
				///////////////////////////
				
				nameContainerNode = doc.createElement("name");
				nameNode = doc.createElement("name");
				nameNode.appendChild(getTextNode(profile.getName()));
				nameContainerNode.appendChild(nameNode);
				
				akaNode = doc.createElement("aka");
				Vector<String> aliases = profile.getAliases();
				Iterator<String> aliasesI = aliases.iterator();
				while(aliasesI.hasNext())
				{
					alias = (String)aliasesI.next();
					aliasNode = doc.createElement("alias");
					aliasNode.appendChild(getTextNode(alias));
					akaNode.appendChild(aliasNode);
				}
				nameContainerNode.appendChild(akaNode);
				
				geolocaleNode.appendChild(nameContainerNode);
				
				////////////////////////////
				
				typeNode = doc.createElement("type");
				typeNode.appendChild(getTextNode(type));
				geolocaleNode.appendChild(typeNode);
				
				// description (can be multiple)
				Vector<String> descriptions = profile.getDescriptions();
				Iterator<String> descI = descriptions.iterator();
				while(descI.hasNext())
				{
					// to do...geolocale description nodes
					descriptionNode = doc.createElement("description");
					description = (String)descI.next();
					descriptionNode.appendChild(getTextNode(description));
					geolocaleNode.appendChild(descriptionNode);
				}
				root.appendChild(geolocaleNode);
			}
		}
		
		private Text getTextNode(String text)
		{
			return doc.createTextNode(text);
		}
	} // end LocationListFactory class
}
