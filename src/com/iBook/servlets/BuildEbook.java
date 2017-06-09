package com.iBook.servlets;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;
import java.util.*;

import com.iBook.datastore.*;
import com.iBook.datastore.books.*;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.ebook.*;
import com.iBook.datastore.ebook.epub.*;
import com.iBook.datastore.ebook.pdf.*;
import com.iBook.datastore.locations.*;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.Utilities;

public class BuildEbook extends HttpServlet
{
	boolean stopForDebugging = false;
	
    private BoilerplateFactory.PubType outputFormat = null;
    private static final long serialVersionUID = 1L;
    private String path_to_user_dir = PropsManager.getPathToUserDir();
    private String pathMrkr = PropsManager.getPathMarker();
    private String userId = "";
    private PropsManager propsMgr = null;
    private BookProfile bookProfile = null;
    private TemplateProfile templateProfile = null;
    private Manuscript manuscript = null;
    private Vector<CharacterProfile> tChars = null;
    private Vector<LocationProfile> tLocs = null;
    private Vector<GeolocaleProfile> tGeolocs = null;
    private Vector<String> tTextblocks = null;
    private int textblocksIdx = 0;
    
    public BuildEbook()
    {
        super();
    }
    
    public String getPathInfo(HttpServletRequest request)
    {
    	return request.getPathInfo();
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        doPost(request, response);
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        ServletOutputStream op = response.getOutputStream();
        
        manuscript = null;
        
        // Get the value of all request parameters
        Enumeration<?> params = request.getParameterNames();

        boolean addFilebreaks = false;
        boolean beginEbook = false;
        boolean deleteEbook = false;
        boolean deleteFilebreaks = false;
        boolean redoDescriptors = false;
        boolean generateEbook = false;
        boolean useCurrentMappings = false;
        
        String bookId = "";
        String ebookName = "";
        String key = "";
        String redirect_target = "/iBook/editor/verifyData.jsp";
        String manuscriptId = "";
        String value = "";
        
        // start fresh each time servlet is called
        textblocksIdx = 0;
        
        
        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
    		value = request.getParameter(key);
    		
    		if(key.compareTo("addFilebreaksSubmit") == 0)
    			addFilebreaks = true;
    		
    		// beginEbook is param of Continue button in generateEbook.jsp
    		// if true, the manuscript text will be retrieved from the template
    		// and all user edits to textblocks will be lost
    		if(key.compareTo("beginEbook") == 0)
    			beginEbook = true;
    		
        	if(key.compareTo("bookId") == 0)
        	{
        		bookId = value;
        		redirect_target += "?bookId=" + bookId;
        	}
        	
        	if(key.compareTo("deleteEbook") == 0)
        		deleteEbook = true;
        	
    		if(key.compareTo("deleteFilebreaksSubmit") == 0)
    			deleteFilebreaks = true;
    		
    		if(key.compareTo("ebookName") == 0)
    			ebookName = value;

    		if(key.compareTo("redoDescriptors") == 0)
        		redoDescriptors = true;
        	
    		// generateEbook is param for Continue button in finalProof.jsp
    		// if true, text in the current manuscript will be used, as opposed
    		// to getting new text from the template
        	if(key.compareTo("generateEbook") == 0)
        		generateEbook = true;
        	
        	if(key.compareTo("manuscriptId") == 0)
        		manuscriptId = value;
        	
        	if(key.compareTo("outputFormat") == 0 || key.compareTo("pubType") == 0)
        	{
        		outputFormat = (value.compareTo(".epub") == 0) ? BoilerplateFactory.PubType.EPUB : BoilerplateFactory.PubType.PDF; 
        	}
        	
        	if(key.compareTo("useCurrentMappings") == 0)
        	{
        		if(value.compareTo("yes") == 0)
        			useCurrentMappings = true;
        	}

        	if(key.compareTo("userId") == 0)
        		userId = value;
        }
        
        propsMgr = new PropsManager(userId);
        
        if(deleteEbook)
        	redirect_target = deleteEbook(ebookName);
        else
        {
	        // bookId not null, not "null", and longer than 0?
	        boolean bookIdIsNull = bookId == null ? true : bookId.compareTo("null") == 0 ? true : false;
	        if(!bookIdIsNull && bookId.length() > 0)
	        {
	        	// bookId not null and length > 0; use it to get the manuscriptId
		        bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
		        // getManuscript() returns the manuscriptId string
		        manuscriptId = bookProfile.getManuscriptId();
	        }
	        // if no bookId, then there should have been a manuscriptId in the parameter list
	        if(manuscriptId.length() > 0)
	        {
	        	// if there's a current manuscript, use it
	        	manuscript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId);
	        	// if manuscript contains textblocks, generate the book without going through the begin steps
	        	if(manuscript.getTextblocks().size() > 0)
	        	{
		        	beginEbook = false;
		        	generateEbook = true;
	        	}
	        }
	        else
	        	// or else the textblocks need to be parsed again to restore descriptors to the manuscript;
	        	// this will erase any user-entered edits to textblocks
	        	redoDescriptors();
	        
	        if(addFilebreaks || deleteFilebreaks)
	        {
	        	redirect_target = editFilebreaks(request, response);
	        }
	        else if(beginEbook || redoDescriptors)
	        {
		        // if beginning a new ebook or user forced a descriptor redo means text needs to be retrieved from the template
		        templateProfile = propsMgr.getTemplateProfile(bookProfile.getTemplateId());
		        tChars = templateProfile.getCharacters();
		        tLocs = templateProfile.getLocations();
		        tGeolocs = templateProfile.getGeolocs();
		        tTextblocks = templateProfile.getTextblocks();
		        
		    	Iterator<String> textblocksI = tTextblocks.iterator();
		    	String text = "";
		    	String textblockId = "";
		    	int textblockIdx = 0;
		    	
		    	if(redoDescriptors)
		    		manuscript.clearDescriptors();
	
		    	boolean newMs = manuscript.getTextblocks().size() == 0 ? true : false;
		    	
		    	while(textblocksI.hasNext())
		    	{
		    		textblockId = manuscript.getMsName() + "_textblock_" + new Integer(++textblockIdx).toString();
		    		
		    		text = processTextblock(textblocksI.next());
					if(newMs)
						manuscript.addTextblock(text);
					else
						manuscript.setTextblock(textblockId, text);
		    		textblocksIdx++;
		    	}
		    	propsMgr.setActiveManuscript(manuscript);
		    	updateXml(PropsManager.ObjType.MANUSCRIPT);
		    	
		    	Vector<String[]> unmappedAliases = bookProfile.getUnmappedAliases(templateProfile);
		    	Vector<String[]> unmappedAttrs = bookProfile.getUnmappedAttributes(templateProfile);
		        if((unmappedAliases != null && unmappedAliases.size() > 0) || 
		           (unmappedAttrs != null && unmappedAttrs.size() > 0) &&  
		           !useCurrentMappings)
		        {
		        	redirect_target = "/iBook/editor/doMappings.jsp?bookId=" + bookId;
		        	beginEbook = false;
		        }
		        else if(manuscript.getNumDescriptorsUnresolved() != 0)
				{
					redirect_target = "/iBook/editor/resolveDescriptors.jsp?manuscriptId=" + manuscriptId + "&templateId=" + templateProfile.getId();
					beginEbook = false;
				}
				else
				{
					redirect_target = deleteIBookTags();
				}
	        }
	        else if(generateEbook)
	        {
	        	// generating from the final proof page means text has already been retrieved from the template
				redirect_target = deleteIBookTags();
	        }
	        if((beginEbook || generateEbook) && (!addFilebreaks && !deleteFilebreaks))
	        {
	        	// one way or another text, from the template is in the manuscript and ready for output
				redirect_target = generateEbook(manuscript, outputFormat);
	        }
        }
        if(redirect_target.length() == 0)
        	redirect_target = "" + bookId;
        String redirect = "<html>\n"
            + "    <body onload=\"document.location = \'" + redirect_target + "\'\" />\n"
            + "</html>";
        response.setContentLength(redirect.length());
        op.write(redirect.getBytes(), 0, redirect.length());
    }
    
    private String deleteEbook(String ebookName)
    {
    	String redirect_target = "editor/";
    	
    	File ebookFile = new File(propsMgr.getPathToUserDir() + "resources" + propsMgr.getPathMarker() + ebookName);  
    	ebookFile.delete();
    	
    	return redirect_target;
    }
    
    private String editFilebreaks(HttpServletRequest request, HttpServletResponse response)
    {
        boolean addFilebreaks = false;
        boolean deleteFilebreaks = false;
    	boolean matchfound = false;
    	
    	int idx = 0;
    	int start = 0;
    	int count = 0;
    	
    	String existingBreak = "";
    	String breakFromList = "";
        String filebreaksList = "";
        String key = "";
        String value = "";

        // Get the value of all request parameters
        Enumeration<?> params = request.getParameterNames();

        while(params.hasMoreElements())
        {
        	key = (String)params.nextElement();
    		value = request.getParameter(key);
    		
    		if(key.compareTo("addFilebreaksSubmit") == 0)
    		{
    			addFilebreaks = true;
    		}
    		
    		if(key.compareTo("deleteFilebreaksSubmit") == 0)
    		{
    			deleteFilebreaks = true;
    		}

        	if(key.compareTo("filebreaksList") == 0)
        	{
        		//trim then add exactly one space so parsing loop will work
        		filebreaksList = value.trim() + " ";
        	}
        }
        	
    	Vector<Integer> filebreaks = manuscript.getFilebreaks();
    	Iterator<Integer> filebreaksI = null;
    	
    	// parse the filebreaksList (space-separated list of textblock numbers)
    	while((idx = filebreaksList.indexOf(" ", start)) != -1)
    	{
    		breakFromList = filebreaksList.substring(start, idx);
    		start = idx + 1;
    		// compare each entry in the list with the manuscript's existing filebreaks 
    		filebreaksI = filebreaks.iterator();
    		while(filebreaksI.hasNext())
    		{
    			// if there's a match... 
    			existingBreak = filebreaksI.next().toString();
    			if(breakFromList.compareTo(existingBreak) == 0)
    			{
    				// and the delete button was clicked...
    				if(deleteFilebreaks)
    					// delete it
    					manuscript.deleteFilebreak(breakFromList);
    				// mark the match in case we're adding
    				matchfound = true;
    				break;
    			}
    		}
    		// if no match was found, and the add button was clicked...
    		if(!matchfound && addFilebreaks)
    			// add it
     			manuscript.addFilebreak(breakFromList);
    		
    		// reset for the next entry on the list
    		matchfound = false;
    		count = 0;
    	}
    	propsMgr.setActiveManuscript(manuscript);
    	updateXml(PropsManager.ObjType.MANUSCRIPT);
    	return "/iBook/editor/showFilebreaks.jsp?manuscriptId=" + manuscript.getId() ;
    }
    
    private String deleteIBookTags()
    {
    	boolean textChanged = false;
    	
    	int idx = 0;
    	int end = 0;
    	int count = 0;
    	
    	StringBuffer textblockBuff = new StringBuffer();
    	String tag = "";
    	
    	Vector<String> textblocks = manuscript.getTextblocks();
    	
    	Iterator<String> textblocksI = textblocks.iterator();
    	while(textblocksI.hasNext())
    	{
    		// reset for new textblock
    		textblockBuff.replace(0,  textblockBuff.length(), "");
    		textblockBuff.append(textblocksI.next());
    		end = 0;
    		while((idx = textblockBuff.indexOf("<", end)) != -1)
    		{
    			if((end = textblockBuff.indexOf(">", idx)) != -1)
    			{
    				tag = textblockBuff.substring(idx, end + 1);
    				if(tag.indexOf("iBook_") != -1 ||
    				   tag.indexOf("_template_") != -1 ||
    				   tag.indexOf("descriptor") != -1 ||
    				   tag.indexOf("alias") != -1 ||
    				   tag.indexOf("attribute") != -1)
    				{
    					textblockBuff.replace(idx, end + 1, "");
    					textChanged = true;
    					// textblockBuff's char count has changed; reset end so next indexOf will 
    					// start just after the previous location if idx
    					end = idx + 1;
    				}
    			}
    			else
    				// there was a < with no >; this probably will never happen---well, 
    				// at least, it shouldn't---but if it does, end the loop
    				end = textblockBuff.length();
    		}
    		if(textChanged)
    			manuscript.updateTextblock(count, textblockBuff.toString());
    		
    		// set
    		count++;
    		textChanged = false;
    	}
    	
    	return "/iBook/editor/finalProof.jsp?manuscriptId=" + manuscript.getId() + "&textblockId=" + manuscript.getMsName() + "_textblock_1";
    }
    
    
    private void deleteManuscript(String bookId)
    {
    	BookProfile bookProfile = propsMgr.getBookProfile(bookId);
    	String manuscriptId = bookProfile.getManuscriptId();
    	Manuscript mScript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId);
    	String manuscriptFilename = mScript.getFilename();
    	File msFile = new File (path_to_user_dir + "manuscripts" + pathMrkr + manuscriptFilename);
    	msFile.delete();
    	bookProfile.setManuscriptId("");
    }
    
    private String generateEbook(Manuscript manuscript, BoilerplateFactory.PubType pubType)
    {
    	String outputFilename = "";
    	String redirect_target = "";
    	switch(pubType)
    	{
    	case EPUB:
        	
        	EPubObj epub = new EPubObj(manuscript, userId);
        	outputFilename = epub.getOutputFilename();
    		break;
    	case PDF:
    		PdfObj pdf = new PdfObj(manuscript, userId);
    		outputFilename = pdf.getOutputFilename();
    		break;
    	}
    	redirect_target = "/iBook/editor/verifyData.jsp?bookId=" + manuscript.getBookId() + "&userId=" + userId + "&outputFile=" + outputFilename;
    	return redirect_target;
    }
    
    private boolean redoDescriptors()
    {
    	boolean ret = true;
    	
    	manuscript.clearTextblocks();
    	
    	propsMgr.setActiveManuscript(manuscript);
 
    	updateXml(PropsManager.ObjType.BOOK);
    	updateXml(PropsManager.ObjType.MANUSCRIPT);
    	
    	return ret;
    }
    
    private String processTextblock(String text)
    {
    	// characters

    	// getHeShe(charId)
    	// getHisHer(charId
    	// getHisHers(charId)
    	// getHimHer(charId)
    	// getHimselfHerself(charId)
    	text = doPronouns(text);
    	
    	// full name
    	// short name
    	// formal name
    	// prefix
    	// first
    	// middle
    	//last
    	// suffix
    	text = doNames(text);
    	
    	// aliases, descriptors
    	text = doTextSubs(text);
    	
    	// locations
    	
    	// regions
    	
    	
    	
    	return text;
    }
    
    private String getSubId(String objId)
    {
    	// returns the id of the object (char, loc, or geoloc) 
    	// substituted for a template object
    	String subId = "";
    	Vector<String[]> subMatrix = null;
    	
    	if(objId.indexOf("template_char") != -1)
    		subMatrix = bookProfile.getCharSubMatrix();
    	else if(objId.indexOf("template_loc") != -1)
    		subMatrix = bookProfile.getLocSubMatrix();
    	else if(objId.indexOf("template_geoloc") != -1)
    		subMatrix = bookProfile.getGeolocSubMatrix();

    	Iterator<String[]> charsI = subMatrix.iterator();
    	String[] subEntry = null;
    	while(charsI.hasNext())
    	{
    		subEntry = charsI.next();
    		if(subEntry[0].compareTo(objId) == 0)
    		{
    			subId = subEntry[1];
    			break;
    		}
    	}
    	return subId;
    }
    
    private String getSubText(String tObjId, String substituteText, String type)
    {
    	/*
    	 * Returns substitute text for aliases or attributes, depending on value of
    	 * type, which must be one of "alias" or "attribute"
    	 */
    	
    	String subText = "";
    	String mrkr = type + ">";
    	String endMrkr = "</";
    	Vector<String[]> subMatrix = null;
    	if(type.compareTo("alias") == 0)
    		subMatrix = bookProfile.getAliasSubMatrix();
    	else if(type.compareTo("attribute") == 0)
    		subMatrix = bookProfile.getAttributeSubMatrix();
    	String[] subEntry = null;
    	Iterator<String[]> subsI = subMatrix.iterator();
    	
    	int idx = substituteText.indexOf(mrkr) + mrkr.length();
    	int end = substituteText.indexOf(endMrkr, idx);

    	if(idx != -1 && end != -1)
    		substituteText = substituteText.substring(idx, end);
    	
    	while(subsI.hasNext())
    	{
    		subEntry = subsI.next();
    		if(subEntry[0].compareTo(tObjId) == 0 && subEntry[1].compareTo(substituteText) == 0)
    		{
    			subText = subEntry[2];
    			break;
    		}
    	}
    	return subText;
    }
    
    private String doPronouns(String text)
    {
    	int idx = 0;
    	int end = 0;
    	
    	CharacterProfile charProfile = null;
    	
    	if(text.indexOf("<getHisHer") != -1)
    		stopForDebugging = true;
    	
    	if(textblocksIdx == 341)
    		stopForDebugging = true;
    	
    	String[][] tagData = { {"<getHeShe(", "He", "She" },
    			                {"<getHimHer(", "Him", "Her" },
    			                {"<getHisHer(", "His", "Her" },
    			                {"<getHisHers(", "His", "Hers" },
    			                {"<getHimselfHerself(", "Himself", "Herself" } };
    	;
    	String tagEnd = ")>";
    	String charId = "";
    	String charSubId = "";
    	String pronoun = "";
    	StringBuffer textBuff = new StringBuffer(text) ;

    	for(int count = 0; count < tagData.length; count++)
    	{
	    	while((idx = textBuff.indexOf(tagData[count][0])) != -1)
	    	{
	    		end = textBuff.indexOf(tagEnd, idx + tagData[count][0].length());
	    		// charId will be the id of the template character contained in the tag
	    		charId = textBuff.substring(idx + tagData[count][0].length(), end);
	    		// need to convert to the substitute character, if one was assigned
	    		charSubId = getSubId(charId);
	    		if(charSubId != null && charSubId.length() > 0)
	    			charId = charSubId;
	    		
	    		// if charSubId is null or empty, there was no substitution for 
	    		// this character, so use the character profile from the template, 
	    		// otherwise get the character profile from the data store
	    		if(charSubId == null || charSubId.length() == 0)
	    		{
	    			Iterator<CharacterProfile> tCharsI = tChars.iterator();
	    			while(tCharsI.hasNext())
	    			{
	    				charProfile = tCharsI.next();
	    				if(charProfile.getId().compareTo(charId) == 0)
	    					break;
	    			}
	    		}
	    		else
	    			charProfile = (CharacterProfile)propsMgr.getProfile(PropsManager.ObjType.CHARACTER, charId);
	    		
	    		if(textblocksIdx == 341 && tagData[count][0].indexOf("HeShe") != -1)
	    			stopForDebugging = true;

				pronoun = charProfile.getGender().compareTo("female") == 0 ? tagData[count][2] : tagData[count][1];
				// leave uppercase?
				if(!leaveUpper(textBuff.toString(), idx))
					pronoun = pronoun.toLowerCase();
				textBuff.replace(idx, end + tagEnd.length(), pronoun);
				// length of text has changed; need to start next indexof search from end of the new pronoun 
				end = idx;
	    	}
    	}
    	return textBuff.toString();
    }
    
    private boolean leaveUpper(String text, int idx)
    {
    	/*
    	 * pronoun substitutes default to upper case initial letters, 
    	 * which is not appropriate for mid-sentence pronouns; leaveUpper()
    	 * returns true if the pronoun is determined to be at the start of
    	 * a sentence, false otherwise
    	 */
    	
    	if(text.indexOf("effect. [") != -1)
    		stopForDebugging = true;
    	
    	boolean ret = false;
		if(idx == 0 || // first word in textblock?
		   idx == 1 ||
		   text.substring(0, idx).toLowerCase().compareTo("<i>") == 0 || // first word in textblock, italicized
		   text.substring(0, idx).toLowerCase().compareTo("<b>") == 0) // first word in textblock, bold
			ret = true;
		if(!ret && idx - 2 > 0)
		{
			if(text.substring(idx - 2, idx).compareTo(". ") == 0 || // follows end of sentence?
		       text.substring(idx - 2, idx).compareTo("! ") == 0 || 
		       text.substring(idx - 2, idx).compareTo("!\" ") == 0 || 
		       text.substring(idx - 2, idx).compareTo("? ") == 0 || 
		       text.substring(idx - 2, idx).compareTo("?\" ") == 0)
				ret = true;
		}
		if(!ret && idx - 3 > 0)
		{
			if(text.substring(idx - 3, idx).compareTo(".\" ")== 0 ||
	    	   text.substring(idx - 3, idx).compareTo(".\" ") == 0 ||
 		       // comes at start of parenthetical sentence?
 		       text.substring(idx - 3, idx).compareTo(". (") == 0 || 		   
 		       text.substring(idx - 3, idx).compareTo("? (") == 0 || 
 		       text.substring(idx - 3, idx).compareTo("! (") == 0 || 
 		       text.substring(idx - 3, idx).compareTo(". [") == 0 || 		   
 		       text.substring(idx - 3, idx).compareTo("? [") == 0 || 		   
 		       text.substring(idx - 3, idx).compareTo("! [") == 0) 
	    	   ret = true;
		}
		if(!ret && idx - 4 > 0)
		{
			if(text.substring(idx - 4, idx).compareTo("\"<i>") == 0)
				ret = true;
		}
		if(!ret && idx - 5 > 0)
		{
			if(text.substring(idx - 5, idx).compareTo(". <i>") == 0 || 
 		       text.substring(idx - 5, idx).compareTo("? <i>") == 0 || 
 		       text.substring(idx - 5, idx).compareTo("! <i>") == 0)
				ret = true;
		}
		return ret;
    }

    private String doNames(String text)
    {
    	int idx = 0;
    	int end = 0;

    	CharacterProfile charProfile = null;
    	GeolocaleProfile geolocProfile = null;
    	LocationProfile locProfile = null;
    	
    	StringBuffer textBuff = new StringBuffer(text);
    	
    	String objId = "";
    	String objSubId = "";
    	String nameText = "";
    	String tagStart = "<";
    	String[] tagEnd = { " namePrefix>",
    			            " nameFirst>",
    			            " nameMiddle>",
    			            " nameLast>",
    			            " nameSuffix>",
    			            " name>",
    			            " shortName>",
    			            " formalName>" };
    	
     	boolean useObjSubId = false;
     	
   	for(int count = 0; count < tagEnd.length; count++)
    	{
    		while((end = textBuff.indexOf(tagEnd[count])) != -1)
    		{
    			idx = textBuff.lastIndexOf(tagStart, end);
    			objId = textBuff.substring(idx + 1, end);
    			
    			// charId points to template character profile in the tag text; get substitute char Id
    			// if one was assigned for this character
    			objSubId = getSubId(objId);

    	     	if(textblocksIdx == 273)
    	    		stopForDebugging = true;
    	     	
    			useObjSubId = (objSubId != null && objSubId.length() > 0) ? true : false;
    			
    			// if the id still contains the word "template" use the profile defined in 
    			// the template, otherwise get the profile from the datastore
    			if(objId.indexOf("_char_") != -1)
    			{
    				if(!useObjSubId)
	    				charProfile = templateProfile.getCharacter(objId);
	    			else
		    			charProfile = (CharacterProfile)propsMgr.getProfile(PropsManager.ObjType.CHARACTER, objSubId);
    			}
    			if(objId.indexOf("_geoloc_") != -1)
    			{
    				if(!useObjSubId)
	    				geolocProfile = templateProfile.getGeoloc(objId);
	    			else
		    			geolocProfile = (GeolocaleProfile)propsMgr.getProfile(PropsManager.ObjType.GEOLOCALE, objSubId);
    			}
    			if(objId.indexOf("_loc_") != -1)
    			{
    				if(!useObjSubId)
	    				locProfile = templateProfile.getLoc(objId);
	    			else
		    			locProfile = (LocationProfile)propsMgr.getProfile(PropsManager.ObjType.LOCATION, objSubId);
    			}
 
    			// if all profiles are null, bail
	    		if(charProfile == null && geolocProfile == null && locProfile == null)
	    			continue;
	    		
	    		if(tagEnd[count].indexOf("namePrefix") != -1)
	    		{
	    			nameText = charProfile.getNamePrefix();
	    			if(nameText.length() == 0)
	    				nameText = templateProfile.getCharacter(objId).getNamePrefix();
	    		}
	    		else if(tagEnd[count].indexOf("nameFirst") != -1)
	    		{
	    			nameText = charProfile.getNameFirst();
	    			if(nameText.length() == 0)
	    				nameText = templateProfile.getCharacter(objId).getNameFirst();
	    		}
	    		else if(tagEnd[count].indexOf("nameMiddle") != -1)
	    		{
	    			nameText = charProfile.getNameMiddle();
	    			if(nameText.length() == 0)
	    				nameText = templateProfile.getCharacter(objId).getNameMiddle();
	    		}
	    		else if(tagEnd[count].indexOf("nameLast") != -1)
	    		{
	    			nameText = charProfile.getNameLast();
	    			if(nameText.length() == 0)
	    				nameText = templateProfile.getCharacter(objId).getNameLast();
	    		}
	    		else if(tagEnd[count].indexOf("nameSuffix") != -1)
	    		{
	    			nameText = charProfile.getNameSuffix();
	    			if(nameText.length() == 0)
	    				nameText = templateProfile.getCharacter(objId).getNameSuffix();
	    		}
	    		else if(tagEnd[count].indexOf("shortName") != -1)
	    		{
	    			nameText = charProfile.getShortName();
	    			if(nameText.length() == 0)
	    				nameText = templateProfile.getCharacter(objId).getShortName();
	    		}
	    		else if(tagEnd[count].indexOf(" formalName>") != -1)
	    		{
	    			nameText = charProfile.getName();
	    			if(nameText.length() == 0)
	    				nameText = templateProfile.getCharacter(objId).getName();
	    		}
	    		else if(tagEnd[count].indexOf(" name>") != -1)
	    		{
	    			if(objId.indexOf("_geoloc_") != -1)
	    			{
		    			nameText = geolocProfile.getName();
		    			if(nameText.length() == 0)
		    				nameText = templateProfile.getGeoloc(objId).getName();
	    			}
	    			else if(objId.indexOf("_loc_") != -1)
	    			{
		    			nameText = locProfile.getName();
		    			if(nameText.length() == 0)
		    				nameText = templateProfile.getLoc(objId).getName();
	    			}
	    			else if(objId.indexOf("_char_") != -1)
	    			{
		    			nameText = charProfile.getName();
		    			if(nameText.length() == 0)
		    				nameText = templateProfile.getCharacter(objId).getName();
	    			}
	    		}
	    		
	    		textBuff.replace(idx,  end + tagEnd[count].length(), nameText);
	    		
    		}
    	}
    	return textBuff.toString();
    }
    
    private String doTextSubs(String text)
    {
    	/*
    	 * Exchanges substitute text for attributes, aliases, and descriptors.
    	 * 
    	 * Aliases are mapped in the aliasSubCharMatrix and can be processed without
    	 * additional user interaction. 
    	 * 
    	 * Descriptors are words/phrases in the original text that need end-user attention.
    	 * Manuscript will record them in a String[] array, then stop the epub generating process. User
    	 * interface needs to obtain end user resolution of each entry in the array. When
    	 * the build epub servelet is able to finish processing text with no descriptors, 
    	 * then the epub can be finished.
    	 */
    	
    	StringBuffer textBuff = new StringBuffer(text);
    	String tObjId = "";
    	String oldText = "";
    	String subText = "";
    	
    	int idx = 0;
    	int end = 0;
    	int insertionPt = 0;
    	
    	String[][] tags = {
    			            {" alias>", "</alias>"},
    			            {" attribute>", "</attribute>"},
    			            {" descriptor>", "</descriptor>"},
    			            {" gdescriptor>", "</gdescriptor>"}
    	                  };
    	
    	String[] descriptorSubEntry = null; 
    	
    	for(int count = 0; count < tags.length; count++)
    	{
    		boolean alias = count == 0 ? true : false;
    		boolean attribute = count == 1 ? true : false;
    		boolean descriptor = count == 2 ? true : false;
    		boolean gdescriptor = count == 3 ? true : false;
    		
        	/*
        	 * starting position of indexOf in the while() loop; 
        	 * indexOf search needs to leap over unremoved tags for subsequent searches
        	 */
        	int next = 0;
    		while((end = textBuff.indexOf(tags[count][0], next)) != -1)
    		{
    			idx = textBuff.lastIndexOf("<", end);
    			tObjId = textBuff.substring(idx + 1, end);
    			
    			// oldText is the text that is to be replaced, and should include  
    			// the start and end tags; end is the index of the last portion 
    			// of the start tag.
    			//
    			// for oldText, need to back up to the start of the tag, and then
    			// go through the end of the close tag

    			insertionPt = textBuff.lastIndexOf("<", end);
    			oldText = textBuff.substring(textBuff.lastIndexOf("<", end), 
    					                     textBuff.indexOf(tags[count][1], end) + tags[count][1].length());
    			if(oldText.indexOf("old lady") != -1)
    				stopForDebugging = true;
    			if(alias)
    			{
    				if(textBuff.indexOf("manly little fellow") != -1)
    					stopForDebugging = true;
	    			subText = getSubText(tObjId, oldText, "alias");
	    			if(subText.length() > 1)
	    				textBuff.replace(idx, textBuff.indexOf(tags[count][1], idx) + tags[count][1].length(), subText);
    			}
    			else if(attribute)
    			{
	    			subText = getSubText(tObjId, oldText, "attribute");
	    			if(subText.length() > 1)
	    				 textBuff.replace(idx, textBuff.indexOf(tags[count][1], idx), subText);
    			}
    			else if(descriptor || gdescriptor)
    			{
    				// subText valid for aliases and attributes only; need to clear it
    				subText = "";
    				/*
    				 *  descriptor[] = {tCharId, textblockIdx, sourceText, newText}
    				 *  
    				 *  tCharId       id of the template character referred to by the descriptor text
    				 *  textblockIdx  index into the texblock vector (0-based, unlike textblockIds, which start at 1)
    				 *  sourceText    txt used in the original text
    				 *  newText       text to be used in the output text
    				 */
    				descriptorSubEntry = new String[] {tObjId, new Integer(textblocksIdx + 1).toString(), oldText, ""};
    				if(descriptorIsNeeded(descriptorSubEntry[0], gdescriptor))
    					manuscript.addDescriptor(descriptorSubEntry);
    			}
    			next = subText.length() > 0 ? insertionPt + subText.length() : textBuff.indexOf(tags[count][1], end);
    			
//    			System.out.println("oldtext = " + oldText + "\nsubText = " + subText);
    		}
    	}
    	return textBuff.toString();
    }
    
    private boolean descriptorIsNeeded(String tObjId, boolean gdescriptor)
    {
    	// returns true if the descriptor will require user-action to resolve
   
    	boolean ret = true;
    	
    	Vector<String[]> subMatrix = null;
    	if(tObjId.indexOf("_char_") != -1)
    		subMatrix = bookProfile.getCharSubMatrix();
    	else if(tObjId.indexOf("_loc_") != -1)
    		subMatrix = bookProfile.getLocSubMatrix();
    	else if(tObjId.indexOf("_geoloc_") != -1)
    		subMatrix = bookProfile.getGeolocSubMatrix();
    	
    	String tObjGender = templateProfile.getCharacter(tObjId).getGender();
    	String iBookObjId = "";
    	CharacterProfile iBookChar = null;
    	String iBookObjGender = "";
    	
    	Iterator<String[]> matrixI = subMatrix.iterator();
    	
  		// subMatrixEntry = { tObjId, iBookObjId }
    	String[] subMatrixEntry = null;
    	while(matrixI.hasNext())
    	{
    		subMatrixEntry = matrixI.next();
    		iBookObjId = subMatrixEntry[1];
    		
    		if(tObjId.compareTo(subMatrixEntry[0]) == 0)
    		{
    			// object IDs match...
        		if(gdescriptor)
        		{
        			// descriptor is gender based---template text refers to specific gender;
        			// does it match the substitution character's gender?
        			if(iBookObjId != null && iBookObjId.length() > 0)
        			{
	    	    		iBookChar = (CharacterProfile)propsMgr.getProfile(PropsManager.ObjType.CHARACTER, iBookObjId);
	    	    		iBookObjGender = iBookChar.getGender();
        			}
    	    		if(iBookObjGender.compareTo(tObjGender) == 0)
    	    		{
    	    			// template character and substitute character genders match, no substitution required,
    	    			// so no descriptor resolution required
    	    			ret = false;
    	    		}
        		}
        		// ...break out of loop; ret remains true---a descriptor will have to be resolved by 
        		// the user---unless descriptor is gender based and character genders match
    			break;
    		}
    	}
    	
    	if(subMatrixEntry[1].length() == 0)
    		ret = false;
    	
    	return ret;
    }
    
  	private boolean updateXml(PropsManager.ObjType type)
  	{
  		boolean ret = true;
  		String xml = "";
  		XmlFactory xmlFactory = new XmlFactory();
  		String outputFilename = "";
  		
  		switch(type)
  		{
	  		case MANUSCRIPT:
	  			outputFilename = "manuscripts" + pathMrkr + propsMgr.getActiveManuscript().getFilename();
	  			break;
	  		case BOOK:
	  			outputFilename = "bookList.xml";
	  			break;
	  		case CHARACTER:
	  		case LOCATION:
	  		case GEOLOCALE:
	  		case TEMPLATE:
	  			break;
  		}
		xml = xmlFactory.doNewXml(propsMgr, type);
  		Utilities.write_file(path_to_user_dir + outputFilename, xml, true);
  		return ret;
  	}
}
