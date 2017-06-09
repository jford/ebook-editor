package com.iBook.datastore;

import com.iBook.datastore.characters.*;
import com.iBook.datastore.manuscripts.*;

import java.util.*;

import com.iBook.datastore.characters.*;
import com.iBook.datastore.locations.*;

public class ObjSubstitutionsManager
{
	/*
	 * The ObjSubstitutionsManager object maps user-selected 
	 * template-to-generated-text substitutions. 
	 * 
	 * When a new book profile is created by the user, character, location, 
	 * and geolocale (region) assignments are written to the bookList.xml repository.
	 * 
	 * When this XML file is parsed to create a bookProfile object, the BookListParser 
	 * object creates an ObjSubstitutionsManager object and adds it to the 
	 * BookProfile object. 
	 * 
	 * Substitution mapping data is retrieved through the book profile.
	 * 
	 * This mapping data is stored in a set of Vector<String[]> lists. 
	 * There are two variants of this matrix list: 
	 * 
	 * I. Object substitution 
	 * 
	 * Each entry in the char, loc, and geoloc lists is an array of
	 * two strings:
	 * 
	 *    String[] subMatrixEntry = { tObjId, iBookObjId }
	 *    
	 * ...where tObjId is the template-defined entity and IBookObjId is an 
	 * entity defined by the user.
	 * 
	 * II. Alias substitutions
	 * 
	 * The aliasSubMatrix vector maps char, loc, and geoloc aliases. Each 
	 * Vector entry is an array of three strings:
	 * 
	 *   String[] aliasSubEntry = { tObjId, alias, substitute }
	 *
	 * ...where tObjId is one of:
	 *  
	 *   - tCharId     template-defined character
	 *   - tLocId      template-defined location 
	 *   - tGeolocId   template-defined geolocale
	 *    
	 * Alias is the string used as an alias for the character's name in
	 * the source text.
	 *   
	 * Substitute is string to be substituted when the character's alias is 
	 * used in the source text.
	 * 
	 * Objects can have multiple aliases. That is, an author of an original work
	 * may have referred to a character by several different aliases at various times.
	 * 
	 * The matrix can have multiple aliases for an object, but only one entry where
	 * the tObjId and alias entries match. When those two strings match, the substitute text 
	 * in the array should be used.
	 *  
	 */

	private enum TObjType {CHARACTER, LOCATION, GEOLOCALE};
	
	private String templateId = "";
	
	private Vector<String[]> aliasSubMatrix = new Vector<String[]>();
	private Vector<String[]> attributeSubMatrix = new Vector<String[]>();
	private Vector<String[]> charSubMatrix = new Vector<String[]>();
	private Vector<String[]> locSubMatrix = new Vector<String[]>();
	private Vector<String[]> geolocSubMatrix = new Vector<String[]>();
	
	public ObjSubstitutionsManager()
	{
	}
	
	public boolean addAliasSubstitute(String tObjId, String tObjAlias, String substituteText)
	{
		boolean ret = true;
		String[] aliasSubEntry = {tObjId, tObjAlias, substituteText };
		if(validateEntry(aliasSubEntry, "alias"))
			aliasSubMatrix.add(aliasSubEntry);
		return ret;
	}
	
	public boolean clearAliasSubstitutes(String tObjId)
	{
		boolean ret = true;
		Iterator<String[]> aliasSubsI = aliasSubMatrix.iterator();
		String[] aliasSubEntry = null;
		while(aliasSubsI.hasNext())
		{
			aliasSubEntry = aliasSubsI.next();
			if(aliasSubEntry[0].compareTo(tObjId) == 0)
				aliasSubEntry[2] = "";
		}
		return ret;
	};
	
	public boolean clearAttributeSubstitutes(String tObjId)
	{
		boolean ret = true;
		Iterator<String[]> attributeSubsI = attributeSubMatrix.iterator();
		String[] attributeSubEntry = null;
		while(attributeSubsI.hasNext())
		{
			attributeSubEntry = attributeSubsI.next();
			if(attributeSubEntry[0].compareTo(tObjId) == 0)
				attributeSubEntry[2] = "";
		}
		return ret;
	};
	
	public void setTemplateId(String templateId)
	{
		// setTemplateid() is called by the BookList parser after the book object has been created
		this.templateId = templateId;
	}
	
	public boolean addAliasSubstitute(String[] aliasSubEntry)
	{
		boolean ret = true;
		if(validateEntry(aliasSubEntry, "alias"))
			aliasSubMatrix.add(aliasSubEntry);
		return ret;
	}
	
	public boolean addAttributeSubstitute(String tObjId, String tObjAttribute, String substituteText)
	{
		boolean ret = true;
		String[] attributeSubEntry = {tObjId, tObjAttribute, substituteText }; 
		if(validateEntry(attributeSubEntry, "attribute"))
			attributeSubMatrix.add(attributeSubEntry);
		return ret;
	}
	
	public boolean addAttributeSubstitute(String[] attributeSubEntry)
	{
		boolean ret = true;
		if(validateEntry(attributeSubEntry, "attribute"))
			attributeSubMatrix.add(attributeSubEntry);
		return ret;
	}
	
	public int getNumCharSubs()
	{
		int num = 0;
		Iterator<String[]> subMatrixI = charSubMatrix.iterator();
		String[] matrixEntry = null;
		while(subMatrixI.hasNext())
		{
			matrixEntry = subMatrixI.next();
			if(matrixEntry[1].length() > 0)
				num++;
		}
		return num;
	}
	
	public int getNumLocSubs()
	{
		int num = 0;
		Iterator<String[]> subMatrixI = locSubMatrix.iterator();
		String[] matrixEntry = null;
		while(subMatrixI.hasNext())
		{
			matrixEntry = subMatrixI.next();
			if(matrixEntry[1].length() > 0)
				num++;
		}
		return num;
	}
	
	public int getNumGeolocSubs()
	{
		int num = 0;
		Iterator<String[]> subMatrixI = geolocSubMatrix.iterator();
		String[] matrixEntry = null;
		while(subMatrixI.hasNext())
		{
			matrixEntry = subMatrixI.next();
			if(matrixEntry[1].length() > 0)
				num++;
		}
		return num;
	}
	
	private boolean validateEntry(String[] subEntry, String type)
	{
		// checks for duplicate entries; removes entry from 
		// subMatrix if the incoming substitute text is an empty string
		
		boolean ret = true;

		Iterator<String[]> matrixI = null;

		// type can be either "alias" or "attribute"
		Vector<String[]> matrix = type.compareTo("alias") == 0 ? aliasSubMatrix : attributeSubMatrix;
		
		matrixI = matrix.iterator();
		String[] matrixEntry = null;
		int index = 0;
		boolean deleteEntry = false;
		
		// if no entries yet, add it (unless substituteText is an empty string)
		if(matrix.size() == 0 && subEntry[2].compareTo("") != 0)
		{
			ret = true;
		}
		else
		{
			while(matrixI.hasNext())
			{
				matrixEntry = matrixI.next();
				// look for entry with matching tObj and tObjAlias
				if(matrixEntry[0].compareTo(subEntry[0]) == 0 && matrixEntry[1].compareTo(subEntry[1]) == 0)
				{
					// if incoming substitute text is an empty string, the entry will 
					// exit the loop so that the index pointer will point to the vector element to be deleted
					if(subEntry[2].compareTo("") == 0)
					{
						deleteEntry = true;
						ret = false;
					}
					// if not marked for deletion, is the incoming substitute text the same as already in the matrix?
					else if(matrixEntry[2].compareTo(subEntry[2]) == 0)
					{
						// yes
						ret = false;
					}
					else
					{
						// not a duplicate, not a "clear" so add the entry...
						ret = true;
						// ...but delete the current entry for this tObjAlias (can't have two 
						// substitutes for the same alias)
						deleteEntry = true;
					}
					// subEntry has been marked as a duplicate or needs to be cleared, so break out of the loop 
					// while index points to the right element in the vector
					break;
				}
				index++;
			}
		}
		if(deleteEntry)
		{
			if(type.compareTo("alias") == 0)
				aliasSubMatrix.remove(index);
			else
				attributeSubMatrix.remove(index);
		}
		
		// ret is true if the subEntry was not found in the matrix and was not 
		// marked for deletion; false if it is a duplicate or was deleted   
		return ret;
	}
	
	public Vector<String[]> getAliasSubMatrix()
	{
		return aliasSubMatrix;
	}

	public Vector<String[]> getAttributeSubMatrix()
	{
		return attributeSubMatrix;
	}

	public String getAlias(String tCharId, String tCharAlias)
	{
		String newAlias = "";
		Iterator<String[]> aliasesI = aliasSubMatrix.iterator();
		String[] aliasSubEntry = null;
		
		while(aliasesI.hasNext())
		{
			aliasSubEntry = aliasesI.next();
			if(aliasSubEntry[0].compareTo(tCharId) == 0 &&
			   aliasSubEntry[1].compareTo(tCharAlias) == 0)
			{
				newAlias = aliasSubEntry[2];
				break;
			}
		}
		return newAlias;
	}
	public String getCurrentSub(PropsManager.ObjType type, String tObjId)
	{
		// return iBook object ID to be used in place of the template object ID (tObjId)
		String currentSub = "";
		Iterator<String[]> matrixI = null;
		
		// first, get the right substitution matrix...
		switch(type)
		{
			case CHARACTER:
				matrixI = charSubMatrix.iterator();
				break;
			case LOCATION:
				matrixI = locSubMatrix.iterator();
				break;
			case GEOLOCALE:
				matrixI = geolocSubMatrix.iterator();
				break;
			case BOOK:
			case MANUSCRIPT:
			case TEMPLATE:
			break;
		}
		
		// extract the iBook ID/template ID array from the matrix
		String[] ids = null;
		while(matrixI.hasNext())
		{
			ids = matrixI.next();
			if(ids[0].compareTo(tObjId) == 0)
			{
				currentSub = ids[1];
				break;
			}
		}
		// and return the iBook object ID to be used in place of the template object ID
		return currentSub;
	}
	
	public boolean addSub(PropsManager.ObjType type, String tObjId, String iBookObjId)
	{
		// matrix should have one entry for every character defined in the template.
		// bookCharId can be empty, to be filled in later by user action
		boolean ret = true;
		
		String[] subMatrixEntry = new String[2];
		subMatrixEntry[0] = tObjId;
		subMatrixEntry[1] = iBookObjId;

		switch(type)
		{
			case CHARACTER:
				charSubMatrix.addElement(subMatrixEntry);
				break;
			case LOCATION:
				locSubMatrix.addElement(subMatrixEntry);
				break;
			case GEOLOCALE:
				geolocSubMatrix.addElement(subMatrixEntry);
				break;
			case BOOK:
			case MANUSCRIPT:
			case TEMPLATE:
			break;
		}
		return ret;
	}
	
	// maps character substitution---when a manuscript needs info re: tCharId, ms should use iBookCharId instead
	public boolean updateSub(PropsManager.ObjType type, 
			                     String tObjId, 
			                     String iBookObjId, 
			                     Vector<?> tObjList)
	{
		boolean ret = false;
		String[] subMatrixEntry = null;
		Vector<String[]> newSubMatrix = new Vector<String[]>();

		Object tObj = null;String objId = "";

		// Step through the template character list input as argument tCharList
		Iterator<?> tObjsI = tObjList.iterator();
		Iterator<String[]> matrixI = null;
		while(tObjsI.hasNext())
		{

			// need to add subMatrixEntry for any objects not currently in the matrix 
			if(type == PropsManager.ObjType.CHARACTER)
			{
				tObj = (CharacterProfile)tObjsI.next();
				objId = ((CharacterProfile)tObj).getId();
				matrixI = charSubMatrix.iterator();
			}
			else if(type == PropsManager.ObjType.LOCATION)
			{
				tObj = (LocationProfile)tObjsI.next();
				objId = ((LocationProfile)tObj).getId();
				matrixI = locSubMatrix.iterator();
			}
			else if(type == PropsManager.ObjType.GEOLOCALE)
			{
				tObj = (GeolocaleProfile)tObjsI.next();
				objId = ((GeolocaleProfile)tObj).getId();
				matrixI = geolocSubMatrix.iterator();
			}
			
			while(matrixI.hasNext())
			{
				subMatrixEntry = matrixI.next();

				// first, retain any matrix entries that refer to characters still defined 
				// in the template xml (and leave out any that have been deleted)
				if(subMatrixEntry[0].compareTo(objId) == 0)
					newSubMatrix.add(subMatrixEntry);
				subMatrixEntry = null;
			}
		}
		// next, add any characters on the template character list that are not currently on the subMatrix list
		boolean matchFound = false;
		tObjsI = tObjList.iterator();
		while(tObjsI.hasNext())
		{
			// reset reach time through the loop
			matchFound = false;

			if(type == PropsManager.ObjType.CHARACTER)
			{
				tObj = (CharacterProfile)tObjsI.next();
				objId = ((CharacterProfile)tObj).getId();
				matrixI = charSubMatrix.iterator();
			}
			if(type == PropsManager.ObjType.LOCATION)
			{
				tObj = (LocationProfile)tObjsI.next();
				objId = ((LocationProfile)tObj).getId();
				matrixI = locSubMatrix.iterator();
			}
			if(type == PropsManager.ObjType.GEOLOCALE)
			{
				tObj = (GeolocaleProfile)tObjsI.next();
				objId = ((GeolocaleProfile)tObj).getId();
				matrixI = geolocSubMatrix.iterator();
			}
			
			// check the entries in the current matrix
			while(matrixI.hasNext())
			{
				subMatrixEntry = matrixI.next();
				// if the tChar ID is already in the matrix, bail out...
				if(objId.compareTo(subMatrixEntry[0]) == 0)
				{
					matchFound = true;
					break;
				}
			}
			// didn't bail, so get the ID of the last-loaded tChar and add it to the xfr list
			if(!matchFound)
			{
				// even though charSubMatrixEntry was pulled from the matrix with the 
				// tChar ID already set, it will be set incorrectly if there was no match; 
				// need to ensure that the tChar id is set correctly before processing the entry array
				String[] xfrSubMatrixEntry = new String[]{objId, ""};
				newSubMatrix.add(xfrSubMatrixEntry);
			}
			subMatrixEntry = null;
		}
				
		// and finally, update the matrix with the new book char id from the method's argument list
		matrixI = newSubMatrix.iterator();
		while(matrixI.hasNext())
		{
			subMatrixEntry = matrixI.next();
			// compare the subMatrixEntry template object id with the tObjId passed in as method argument
			if(subMatrixEntry[0].compareTo(tObjId) == 0)
			{
				// if it's a match, update the entry with the book char ID from the method args
				subMatrixEntry[1] = iBookObjId;
				ret = true;
				break;
			}
			subMatrixEntry = null;
		}
		// finally, replace the subMatrix with the newSubMatrix
		if(type == PropsManager.ObjType.CHARACTER)
			charSubMatrix = newSubMatrix;
		else if(type == PropsManager.ObjType.LOCATION)
			locSubMatrix = newSubMatrix;
		else if(type == PropsManager.ObjType.GEOLOCALE)
			geolocSubMatrix = newSubMatrix;
		else
			ret = false;
		
		return ret;
	}
	
	public Vector<String[]> getUnmappedAliases(TemplateProfile tProfile)
	{
		Iterator<?> tObjsI = null;
		Iterator<String> tAliasesI = null;
		
		Object tObj = null;
		
		String tAlias = "";
		String tObjId = "";
		String standinId = "";
		
		String[] unmappedAlias = null;
		
		Vector<CharacterProfile> tChars = tProfile.getCharacters();
		Vector<GeolocaleProfile> tGeolocs = tProfile.getGeolocs();
		Vector<LocationProfile> tLocs = tProfile.getLocations();
		
		Vector<String[]> unmappedAliases = new Vector<String[]>();
		Vector<String> tObjAliases = null;
		
		for(TObjType type : TObjType.values())
		{
			switch(type)
			{
			case CHARACTER:
				tObjsI = tChars.iterator();
				break;
			case GEOLOCALE:
				tObjsI = tGeolocs.iterator();
				break;
			case LOCATION:
				tObjsI = tLocs.iterator();
				break;
			}
			
			while(tObjsI.hasNext())
			{
				tObj = tObjsI.next();
				switch(type)
				{
				case CHARACTER:
					tObjId = ((CharacterProfile)tObj).getId();
					tObjAliases = ((CharacterProfile)tObj).getAliases();
					break;
				case GEOLOCALE:
					tObjId = ((GeolocaleProfile)tObj).getId();
					tObjAliases = ((GeolocaleProfile)tObj).getAliases();
					break;
				case LOCATION:
					tObjId = ((LocationProfile)tObj).getId();
					tObjAliases = ((LocationProfile)tObj).getAliases();
					break;
				}
				standinId = getStandinId(tObjId, type);
				if(standinId.length() > 0)
				{
					tAliasesI = tObjAliases.iterator();
					while(tAliasesI.hasNext())
					{
						tAlias = tAliasesI.next();
						if(!aliasMapped(tAlias, tObjId))
						{
							unmappedAlias = new String[] { standinId, tObjId, tAlias };
							unmappedAliases.add(unmappedAlias);
						}
					}
				}
			}
		}
		return unmappedAliases;
	}
	
	public Vector<String[]> getUnmappedAttributes(TemplateProfile tProfile)
	{
		Iterator<?> tObjsI = null;
		Iterator<String> tAttrsI = null;
		
		Object tObj = null;
		
		String tAttr = "";
		String tObjId = "";
		String standinId = "";
		
		String[] unmappedAttr = null;
		
		Vector<CharacterProfile> tChars = tProfile.getCharacters();
		Vector<GeolocaleProfile> tGeolocs = tProfile.getGeolocs();
		Vector<LocationProfile> tLocs = tProfile.getLocations();
		
		Vector<String[]> unmappedAttrs = new Vector<String[]>();
		Vector<String> tObjAttrs = null;
		
		for(TObjType type : TObjType.values())
		{
			switch(type)
			{
			case CHARACTER:
				tObjsI = tChars.iterator();
				break;
			case GEOLOCALE:
				tObjsI = tGeolocs.iterator();
				break;
			case LOCATION:
				tObjsI = tLocs.iterator();
				break;
			}
			
			while(tObjsI.hasNext())
			{
				tObj = tObjsI.next();
				switch(type)
				{
				case CHARACTER:
					tObjId = ((CharacterProfile)tObj).getId();
					tObjAttrs = ((CharacterProfile)tObj).getAttributes();
					break;
				case GEOLOCALE:
					tObjId = ((GeolocaleProfile)tObj).getId();
					// in GeolocaleProfile, attributes are called descriptions
					tObjAttrs = ((GeolocaleProfile)tObj).getDescriptions();
					break;
				case LOCATION:
					tObjId = ((LocationProfile)tObj).getId();
					// in LocationProfile, attributes are called locationDescriptions
					tObjAttrs = ((LocationProfile)tObj).getLocationDescriptions();
					break;
				}
				standinId = getStandinId(tObjId, type);
				if(standinId.length() > 0)
				{
					tAttrsI = tObjAttrs.iterator();
					while(tAttrsI.hasNext())
					{
						tAttr = tAttrsI.next();
						if(!attributeMapped(tAttr, tObjId))
						{
							unmappedAttr = new String[] { standinId, tObjId, tAttr };
							unmappedAttrs.add(unmappedAttr);
						}
					}
				}
			}
		}
		return unmappedAttrs;
	}
	
	private boolean aliasMapped(String alias, String tObjId)
	{
		boolean ret = false;
		
		String[] matrixEntry = null;
		
		Iterator<String[]> aliasMatrixI = aliasSubMatrix.iterator();
		while(aliasMatrixI.hasNext())
		{
			matrixEntry = aliasMatrixI.next();
			if(matrixEntry[0].compareTo(tObjId) == 0 &&
			   matrixEntry[1].compareTo(alias) == 0)
			{
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	private boolean attributeMapped(String attribute, String tObjId)
	{
		boolean ret = false;
		
		String[] matrixEntry = null;
		
		Iterator<String[]> attributeMatrixI = attributeSubMatrix.iterator();
		while(attributeMatrixI.hasNext())
		{
			matrixEntry = attributeMatrixI.next();
			if(matrixEntry[0].compareTo(tObjId) == 0 &&
			   matrixEntry[1].compareTo(attribute) == 0)
			{
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	private String getStandinId(String tObjId, TObjType type)
	{
		String standinId = "";
		
		String[] subMatrixEntry = null;
		
		Iterator<String[]> subMatrixI = null;
		
		switch(type)
		{
		case CHARACTER:
			subMatrixI = charSubMatrix.iterator();
			break;
		case GEOLOCALE:
			subMatrixI = geolocSubMatrix.iterator();
			break;
		case LOCATION:
			subMatrixI = locSubMatrix.iterator();
			break;
		}
		
		while(subMatrixI.hasNext())
		{
			subMatrixEntry = subMatrixI.next();
			if(subMatrixEntry[0].compareTo(tObjId) == 0)
			{
				standinId = subMatrixEntry[1];
				break;
			}
		}
		return standinId;
	}

	// pass the template character ID in, get the character list ID back
	public String getSubCharId(PropsManager.ObjType type, String tObjId)
	{
		String iBookObjId = null;
		String[] subMatrixEntry = new String[2];
		Iterator<String[]> subMatrixI = null;
		
		if(type == PropsManager.ObjType.CHARACTER)
			subMatrixI = charSubMatrix.iterator();
		else if(type == PropsManager.ObjType.LOCATION)
			subMatrixI = locSubMatrix.iterator();
		else if(type == PropsManager.ObjType.GEOLOCALE)
			subMatrixI = geolocSubMatrix.iterator();
			
		while(subMatrixI.hasNext())
		{
			subMatrixEntry = (String[])subMatrixI.next();
			if(subMatrixEntry[0].compareTo(tObjId) == 0)
			{
				iBookObjId = subMatrixEntry[1];
				break;
			}
		}
		// returns null if tCharId not found
		return iBookObjId;
	}
	
	public Vector<String[]> getMatrix(PropsManager.ObjType type)
	{
		Vector<String[]> ret = null;
		
		switch(type)
		{
			case CHARACTER:
				ret = charSubMatrix;
				break;
			case LOCATION:
				ret = locSubMatrix;
				break;
			case GEOLOCALE:
				ret = geolocSubMatrix;
				break;
			case BOOK:
			case TEMPLATE:
			case MANUSCRIPT:
				break;
		}
		return ret;
	}
}
