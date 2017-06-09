package com.iBook.datastore.manuscripts;

import java.util.*;
import java.text.Collator;

import com.iBook.datastore.PropsManager;
import com.iBook.datastore.characters.*;
import com.iBook.datastore.locations.*;
import com.iBook.utilities.Utilities;

public class TemplateProfile
{
	private Collator col = Collator.getInstance();
	
	private String author = "";
	private String authorNamePrefix = "";
	private String authorNameFirst = "";
	private String authorNameMiddle = "";
	private String authorNameLast = "";
	private String authorNameSuffix = "";
	private String authorNameShort = "";
	private String filename = "";
	private String id = "";
	private String lastEditedTextblock = "";
	private String name = "";
	private String pubdate = "";
	private String pubtype = "";
	private String title = "";
	
	private Vector<String>           characterRefs = new Vector<String>();
	private Vector<CharacterProfile> characters = new Vector<CharacterProfile>();
	private Vector<String>           geolocRefs = new Vector<String>();
	private Vector<GeolocaleProfile> geolocs = new Vector<GeolocaleProfile>();
	private Vector<String>           locationRefs = new Vector<String>();
	private Vector<LocationProfile>  locations = new Vector<LocationProfile>();
	private Vector<String>           segmentStarts = new Vector<String>();
	private Vector<String>           textblocks = new Vector<String>();
	
	public TemplateProfile()
	{
	}
	
	public boolean editLocName(String locId, String newName)
	{
		boolean ret = false;
		LocationProfile loc = null;
		Iterator<LocationProfile> locsI = locations.iterator();
		while(locsI.hasNext())
		{
			loc = (LocationProfile)locsI.next();
			if(loc.getId().compareTo(locId) == 0)
			{
				loc.setName(newName);
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public boolean editGeolocType(String geolocId, String newGeolocType)
	{
		boolean ret = false;
		GeolocaleProfile geoloc = null;
		Iterator<GeolocaleProfile> geolocsI = geolocs.iterator();
		while(geolocsI.hasNext())
		{
			geoloc = (GeolocaleProfile)geolocsI.next();
			if(geoloc.getId().compareTo(geolocId) == 0)
			{
				geoloc.setType(newGeolocType);
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public boolean editGeolocName(String geolocId, String newName)
	{
		boolean ret = false;
		GeolocaleProfile geoloc = null;
		Iterator<GeolocaleProfile> geolocsI = geolocs.iterator();
		while(geolocsI.hasNext())
		{
			geoloc = (GeolocaleProfile)geolocsI.next();
			if(geoloc.getId().compareTo(geolocId) == 0)
			{
				geoloc.setName(newName);
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public GeolocaleProfile getGeoloc(String geolocId)
	{
		GeolocaleProfile geoloc = null;
		Iterator<GeolocaleProfile> geolocsI = geolocs.iterator();
		while(geolocsI.hasNext())
		{
			geoloc = (GeolocaleProfile)geolocsI.next();
			if(geoloc.getId().compareTo(geolocId) == 0)
				break;
		}
		return geoloc;
	}
	
	public LocationProfile getLoc(String locId)
	{
		LocationProfile loc = null;
		Iterator<LocationProfile> locsI = locations.iterator();
		while(locsI.hasNext())
		{
			loc = (LocationProfile)locsI.next();
			if(loc.getId().compareTo(locId) == 0)
				break;
		}
		return loc;
	}
	
	public boolean setLastEditedTextblock(String textblockId)
	{
		// sets the lastEdited... attribute in the template .xml
		boolean ret = true;
		lastEditedTextblock = textblockId;
		return ret;
	}
	
	public String getLastEditedTextblock()
	{
		// returns the ID of the last edited textblock
		
		if(lastEditedTextblock.compareTo("") == 0)
		{
			// if no textblocks have been edited, lastEditedTextblock will be = should default to 0;
			// textblock IDs are constructed in the XmlFactory according to this...
			//
			//      String idBase = profile.getFilename();
			//      int idx = idBase.indexOf(".xml");
			//      idBase = idBase.substring(0, idx) + "_template_";
			//      id = idBase + "textblock_";
			String idBase = getFilename();
			int idx = idBase.indexOf(".xml");
			idBase = idBase.substring(0, idx) + "_template_";
			String textblockId = idBase + "textblock_";
			
			lastEditedTextblock = textblockId + "0";
		}
		return lastEditedTextblock;
	}
	
	public String getTextblock(String textblockId)
	{
		// need to derive index into vector, 0 based, from the textblockId, which starts numbering at 1
		int idx = Integer.parseInt(textblockId.substring(textblockId.lastIndexOf("_") + 1)) - 1;

		// returns the text of the specified block
		return textblocks.get(idx);
	}
	
	public String getNextTextblock(String textblockId)
	{
		// idx is the index to the vector, which is one less than the textblockId
		// vectors are 0 index3ed; textblockIds start at 1
		int idx = Integer.parseInt(textblockId.substring(textblockId.lastIndexOf("_") + 1)) - 1;
		if(textblocks.size() < idx + 1)
			idx = 0;
		
		// update the lastEdited value
		setLastEditedTextblock(textblockId);
		
		// return the next block 
		return textblocks.get(idx);
		
	}
	
	public String getNextTextblockId(String textblockId)
	{
		// returns the next Id following the argument Id
		String nextId = "";
		int idx = textblockId.lastIndexOf("_") + 1;
		int num = new Integer(textblockId.substring(idx));
		
		// is there a next block? if no, go to 1
		num = num < textblocks.size() ? num++ : 1;
		
		nextId = textblockId.substring(0, idx) + new Integer(num).toString();
		
		return nextId;
	}
	
	public String getSpecifiedTextblockId(String textblockId, String num)
	{
		String newId = textblockId.substring(0, textblockId.lastIndexOf("_") + 1) + num;
		return newId;
	}
	
	public int getTextblockNum(String textblockId)
	{
		// returns the number portion of the ID string, as an int
		return Integer.parseInt(textblockId.substring(textblockId.lastIndexOf("_") + 1));
	}
	
	public boolean updateGeolocs(Vector<GeolocaleProfile> geolocs)
	{
		boolean ret = true;
		this.geolocs = geolocs;
		return ret;
	}
	
	public boolean updateTextblock(String textblockId, String newTextblock)
	{
		boolean ret = true;
		
		// idx is index into vector, which is 0 based; IDs start numbering at 1
		int idx = Integer.parseInt(textblockId.substring(textblockId.lastIndexOf("_") + 1)) - 1;
		
		textblocks.set(idx, newTextblock);
		lastEditedTextblock = textblockId;
		return ret;
	}
	
	public int getCharacterCount()
	{
		return characters.size();
	}
	public int getLocationsCount()
	{
		return locations.size();
	}
	public int getGeolocsCount()
	{
		return geolocs.size();
	}
	
	public CharacterProfile getCharacter(String charId)
	{
		Iterator<CharacterProfile> charsI = characters.iterator();
		CharacterProfile profile = null;
		while(charsI.hasNext())
		{
			profile = (CharacterProfile)charsI.next();
			if(profile.getId().compareTo(charId) == 0)
				break;
		}
		return profile;
	}
	
	public int getNumTextBlocks()
	{
		return textblocks.size();
	}
	
	public Vector<CharacterProfile> getCharacters()
	{
		// return a list of characters sorted alphabetically by profile.getName()
		Vector<String> names = new Vector<String>();
		Vector<CharacterProfile> sortedList = new Vector<CharacterProfile>();
		CharacterProfile profile = null;
		String name = "";
		
		// first, cyle through the characters list and compile a list of names
		Iterator<CharacterProfile> charsI = characters.iterator();
		while(charsI.hasNext())
		{
			names.add(charsI.next().getName());
		}
		// sort the collected names
		Collections.sort(names, col);
		
		// and cycle through the sorted list
		Iterator<String> namesI = names.iterator();
		while(namesI.hasNext())
		{
			name = namesI.next();
			
			// for each name on the list, sift through the characters vector looking for the matching profile
			charsI = characters.iterator();
			while(charsI.hasNext())
			{
				profile = charsI.next();
				if(name.compareTo(profile.getName()) == 0)
				{
					// add it to the list, and break out to get the next name
					sortedList.add(profile);
					break;
				}
			}
		}
		return sortedList;
	}
	
	public void addSegmentStartId(String textblockId)
	{
		segmentStarts.addElement(textblockId);
	}
	public boolean isSegmentStart(String textblockId)
	{
		boolean ret = false;
		Iterator<String> segStartsI = segmentStarts.iterator();
		String segStartId = "";
		while(segStartsI.hasNext())
		{
			segStartId = (String)segStartsI.next();
			if(segStartId.compareTo(textblockId) == 0)
			{
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public String getId()
	{
		return id;
	}
	
	public boolean setId(String id)
	{
		boolean ret = false;
		this.id = id;
		return ret;
	}
	
	public boolean addGeolocAttribute(String geolocId, String text)
	{
		boolean ret= true;
		Iterator<GeolocaleProfile> geolocsI = geolocs.iterator();
		GeolocaleProfile geoloc = null;
		while(geolocsI.hasNext())
		{
			geoloc = (GeolocaleProfile)geolocsI.next();
			if(geoloc.getId().compareTo(geolocId) == 0)
			{
				geoloc.addDescription(text);
				break;
			}
		}
		return ret;
	}
	
	public boolean addCharacter(CharacterProfile character)
	{
		boolean ret = true;
		characters.addElement(character);
		return ret;
	}
	
	public boolean deleteCharacter(String charId)
	{
		Iterator<CharacterProfile> tCharsI = characters.iterator();
		CharacterProfile profile = null;
		int count = 0;
		boolean matchFound = false;
		while(tCharsI.hasNext())
		{
			profile = (CharacterProfile)tCharsI.next();
			if(charId.compareTo(profile.getId()) == 0)
			{
				matchFound = true;
				break;
			}
			count++;
		}
		if(matchFound)
			characters.removeElementAt(count);
		return matchFound;
	}

	public boolean addLocation(LocationProfile location)
	{
		boolean ret = true;
		locations.addElement(location);
		return ret;
	}
	
	public boolean deleteLocation(String locId)
	{
		Iterator<LocationProfile> tLocsI = locations.iterator();
		LocationProfile profile = null;
		int count = 0;
		boolean matchFound = false;
		while(tLocsI.hasNext())
		{
			profile = (LocationProfile)tLocsI.next();
			if(locId.compareTo(profile.getId()) == 0)
			{
				matchFound = true;
				break;
			}
			count++;
		}
		if(matchFound)
			locations.removeElementAt(count);
		return matchFound;
	}

	public Vector<String> getTextblocks()
	{
		return textblocks;
	}
	
	public boolean addTextblock(String textblock)
	{
		boolean ret = true;
		textblocks.addElement(textblock);
		return ret;
	}
	public void deleteTextBlock(int idx)
	{
		// set idx to -1 to clear all textblocks
		if(idx == -1)
			textblocks.removeAllElements();
		else
			textblocks.removeElementAt(idx);
	}
	
	public Vector<String> getCharacterRefs()
	{
		return characterRefs;
	}
	
	public boolean addCharacterRef(String textblockId)
	{
		boolean ret = true;
		characterRefs.addElement(textblockId);
		return ret;
	}
	
	public Vector<String> getGeolocRefs()
	{
		return geolocRefs;
	}
	
	public boolean addGeolocRef(String textblockId)
	{
		boolean ret = true;
		geolocRefs.addElement(textblockId);
		return ret;
	}
	
	public Vector<String> getLocationRefs()
	{
		return locationRefs;
	}
	
	public boolean addLocationRef(String textblockId)
	{
		boolean ret = true;
		locationRefs.addElement(textblockId);
		return ret;
	}
	
	public Vector<LocationProfile> getLocations()
	{
		// return a list of locations sorted alphabetically by profile.getName()
		Vector<String> names = new Vector<String>();
		Vector<LocationProfile> sortedList = new Vector<LocationProfile>();
		LocationProfile profile = null;
		String name = "";
		
		// first, cyle through the locations list and compile a list of names
		Iterator<LocationProfile> locsI = locations.iterator();
		while(locsI.hasNext())
		{
			names.add(locsI.next().getName());
		}
		// sort the collected names
		Collections.sort(names, col);
		
		// and cycle through the sorted list
		Iterator<String> namesI = names.iterator();
		while(namesI.hasNext())
		{
			name = namesI.next();
			
			// for each name on the list, sift through the locations vector looking for the matching profile
			locsI = locations.iterator();
			while(locsI.hasNext())
			{
				profile = locsI.next();
				if(name.compareTo(profile.getName()) == 0)
				{
					// add it to the list, and break out to get the next name
					sortedList.add(profile);
					break;
				}
			}
		}
		return sortedList;
	}
	
	public Vector<GeolocaleProfile> getGeolocs()
	{
		// return a list of regfions sorted alphabetically by profile.getName()
		Vector<String> names = new Vector<String>();
		Vector<GeolocaleProfile> sortedList = new Vector<GeolocaleProfile>();
		GeolocaleProfile profile = null;
		String name = "";
		
		// first, cyle through the characters list and compile a list of names
		Iterator<GeolocaleProfile> geolocsI = geolocs.iterator();
		while(geolocsI.hasNext())
		{
			names.add(geolocsI.next().getName());
		}
		// sort the collected names
		Collections.sort(names, col);
		
		// and cycle through the sorted list
		Iterator<String> namesI = names.iterator();
		while(namesI.hasNext())
		{
			name = namesI.next();
			
			// for each name on the list, sift through the geolocs vector looking for the matching profile
			geolocsI = geolocs.iterator();
			while(geolocsI.hasNext())
			{
				profile = geolocsI.next();
				if(name.compareTo(profile.getName()) == 0)
				{
					// add it to the list, and break out to get the next name
					sortedList.add(profile);
					break;
				}
			}
		}
		return sortedList;
	}
	
	public boolean addGeoloc(GeolocaleProfile geoloc)
	{
		boolean ret = true;
		geolocs.addElement(geoloc);
		return ret;
	}

	public boolean deleteGeoloc(String geolocId)
	{
		Iterator<GeolocaleProfile> tGeolocsI = geolocs.iterator();
		GeolocaleProfile profile = null;
		int count = 0;
		boolean matchFound = false;
		while(tGeolocsI.hasNext())
		{
			profile = (GeolocaleProfile)tGeolocsI.next();
			if(geolocId.compareTo(profile.getId()) == 0)
			{
				matchFound = true;
				break;
			}
			count++;
		}
		if(matchFound)
			geolocs.removeElementAt(count);
		return matchFound;
	}

	public boolean setAuthorNamePrefix(String authorNamePrefix)
	{
		boolean ret = true;
		this.authorNamePrefix = authorNamePrefix;
		return ret;
	}
	public String getAuthorNamePrefix()
	{
		return authorNamePrefix;
	}
	
	public boolean setAuthorNameFirst(String authorNameFirst)
	{
		boolean ret = true;
		this.authorNameFirst= authorNameFirst;
		return ret;
	}
	public String getAuthorNameFirst()
	{
		return authorNameFirst;
	}
	
	public boolean setAuthorNameMiddle(String authorNameMiddle)
	{
		boolean ret = true;
		this.authorNameMiddle = authorNameMiddle;
		return ret;
	}
	public String getAuthorNameMiddle()
	{
		return authorNameMiddle;
	}
	
	public boolean setAuthorNameLast(String authorNameLast)
	{
		boolean ret = true;
		this.authorNameLast = authorNameLast;
		return ret;
	}
	public String getAuthorNameLast()
	{
		return authorNameLast;
	}
	
	public boolean setAuthorNameSuffix(String authorNameSuffix)
	{
		boolean ret = true;
		this.authorNameSuffix = authorNameSuffix;
		return ret;
	}
	public String getAuthorNameSuffix()
	{
		return authorNameSuffix;
	}
	
	public boolean setAuthorNameShort(String authorNameShort)
	{
		boolean ret = true;
		this.authorNameShort= authorNameShort;
		return ret;
	}
	public String getAuthorNameShort()
	{
		return authorNameShort;
	}
	
	public boolean setPubtype(String pubtype)
	{
		boolean ret = true;
		this.pubtype = pubtype;
		return ret;
	}
	public String getPubtype()
	{
		return pubtype;
	}
	
	public boolean setTitle(String title)
	{
		boolean ret = true;
		this.title = title;
		return ret;
	}
	public String getTitle()
	{
		return title;
	}
	
	public boolean setPubdate(String pubdate)
	{
		boolean ret = true;
		this.pubdate = pubdate;
		return ret;
	}
	
	public String getPubdate()
	{
		return pubdate;
	}
	
	public boolean setName(String name)
	{
		boolean ret = true;
		this.name = name;
		return ret;
	}
	public String getName()
	{
		return name;
	}
	public String getAuthor()
	{
		author = authorNamePrefix + " " +
				 authorNameFirst + " " +
				 authorNameMiddle + " " +
				 authorNameLast + " " +
				 authorNameSuffix;
		return Utilities.replaceChars(author, "  ", " ", "all").trim();
	}
	public boolean setFilename(String filename)
	{
		boolean ret = true;
		this.filename = filename;
		return ret;
	}
	public String getFilename()
	{
		return filename;
	}
}
