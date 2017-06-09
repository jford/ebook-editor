package com.iBook.datastore;

import java.util.*;

import com.iBook.datastore.books.BookProfile;
import com.iBook.datastore.characters.CharacterProfile;
import com.iBook.datastore.locations.GeolocaleProfile;
import com.iBook.datastore.locations.LocationProfile;
import com.iBook.datastore.manuscripts.TemplateProfile;

public class TextFactory
{
	boolean stopForDebugging = false;
	
	BookProfile bookProfile = null;
	PropsManager propsMgr = null;
	TemplateProfile templateProfile = null;
	Vector<CharacterProfile> tChars = null;
	
	public TextFactory(String userId, String templateId)
	{
		propsMgr = new PropsManager(userId);
		templateProfile = propsMgr.getTemplateProfile(templateId); 
		tChars = templateProfile.getCharacters();
	}
	public String purgeTags(String text)
	{
		text = doPronouns(text);
		text = doNames(text);
		text = doTxtSubs(text);
		
		return text;
	}
	
	public String doPronouns(String text)
	{
    	int idx = 0;
    	int end = 0;
    	
    	CharacterProfile charProfile = null;
    	
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
	public String doNames(String text)
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
	public String doTxtSubs(String text)
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
    			if(alias)
    			{
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
//    				descriptorSubEntry = new String[] {tObjId, new Integer(textblocksIdx + 1).toString(), oldText, ""};
//    				if(descriptorIsNeeded(descriptorSubEntry[0], gdescriptor))
//    					manuscript.addDescriptor(descriptorSubEntry);
    			}
    			next = subText.length() > 0 ? insertionPt + subText.length() : textBuff.indexOf(tags[count][1], end);
    			
//    			System.out.println("oldtext = " + oldText + "\nsubText = " + subText);
    		}
    	}
    	return textBuff.toString();
	}
	
    private String getSubId(String objId)
    {
    	// returns the id of the object (char, loc, or geoloc) 
    	// substituted for a template object
    	String subId = "";
    	Vector<String[]> subMatrix = null;
    	
    	// when show display text button clicked in template editor, 
    	// there is no book profile, therefore no substitution text 
    	if(bookProfile != null)
    	{
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
    	
    	// when show display text button clicked in template editor, 
    	// there is no book profile, therefore no substitution text 
    	if(bookProfile != null)
    	{
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
    	}
    	return subText;
    }
    
    private boolean leaveUpper(String text, int idx)
    {
    	/*
    	 * pronoun substitutes default to upper case initial letters, 
    	 * which is not appropriate for mid-sentence pronouns; leaveUpper()
    	 * returns true if the pronoun is determined to be at the start of
    	 * a sentence, false otherwise
    	 */
    	
    	boolean ret = false;
		if(idx == 0|| // first word in textblock?
		   idx == 1)
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
	    	   text.substring(idx - 3, idx).compareTo(".\" ") == 0)
	    	   ret = true;
		}
		if(!ret && idx - 5 > 0)
		{
			if(text.substring(idx - 5, idx).compareTo(". <i>") == 0 || 
 		       text.substring(idx - 5, idx).compareTo("? <i>") == 0 || 
 		       text.substring(idx - 5, idx).compareTo("\"<i>") == 0 || 
 		       text.substring(idx - 5, idx).compareTo("! <i>") == 0 ||
 		       // comes at start of parenthetical sentence?
 		       text.substring(idx - 5, idx).compareTo(". (") == 0 || 		   
 		       text.substring(idx - 5, idx).compareTo("? (") == 0 || 
 		       text.substring(idx - 5, idx).compareTo("! (") == 0 || 
 		       text.substring(idx - 5, idx).compareTo(". [") == 0 || 		   
 		       text.substring(idx - 5, idx).compareTo("? [") == 0 || 		   
 		       text.substring(idx - 5, idx).compareTo("! [") == 0) 
				ret = true;
		}
		return ret;
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
}
