package com.iBook.datastore.characters;

import java.text.Collator;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import com.iBook.datastore.PropsManager;
import com.iBook.datastore.locations.LocationProfile;
import com.iBook.utilities.Utilities;

public class CharacterList
{
	/*
	 * The CharacterList object representation of the data 
	 * contained in the characterList.xml file is built by the 
	 * CharacterListParser, which reads the existing xml file,
	 * or by user action through .jsp files that pass data 
	 * through the Props/Character manager objects to the CharacterList 
	 * object created by the parser. The user-edited
	 * object is then converted to xml (which gets written to disk for
	 * use by the CharacterListParser) in the XmlFactory class.
	 * 
	 */
	private String path_to_user_dir = PropsManager.getPathToUserDir();
	
	private Vector<CharacterProfile> characterList = new Vector<CharacterProfile>(); 
	private Vector<String> idList = new Vector<String>();
	private Vector<String> attributes = new Vector<String>();
	private Vector<String> books = new Vector<String>();
	
    private Collator col = Collator.getInstance(Locale.US);
	
	public CharacterList()
	{
	}
	public boolean addCharacterProfile(CharacterProfile profile)
	{
		idList.addElement(profile.getId());
		return characterList.add(profile);
	}
	public void addId(String id)
	{
		idList.add(id);
	}
	
	public Vector<String> getCharacterAttributes(String id)
	{
			Vector<String> attributes = null;
			Iterator<CharacterProfile> profI = characterList.iterator();
			CharacterProfile profile = null;
			while(profI.hasNext())
			{
				profile = (CharacterProfile)profI.next();
				if(profile.getId().compareTo(id) == 0)
				{
					attributes = profile.getAttributes();
					break;
				}
			}
			return attributes;
	}
	
	public boolean deleteAlias(String charId, int index)
	{
		boolean ret = false;
		CharacterProfile profile = null;
		Iterator<CharacterProfile> charsI = characterList.iterator();
		while(charsI.hasNext())
		{
			profile = (CharacterProfile)charsI.next();
			if(profile.getId().compareTo(charId) == 0)
			{
				profile.deleteAlias(index);
				ret = true;
			}
		}
		return ret;
	}
	
	public boolean updateAliases(String charId, Vector<String> aliases)
	{
		boolean ret = false;
		Iterator<CharacterProfile> charsI = characterList.iterator();
		CharacterProfile profile = null;
		while(charsI.hasNext())
		{
			profile = (CharacterProfile)charsI.next();
			if(profile.getId().compareTo(charId) == 0)
			{
				profile.updateAliases(aliases);
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public boolean deleteProfile(String id)
	{
		boolean ret = false;
		CharacterProfile profile = null;
		int count = 0;

		// delete from profile list (represented by characterList.xml) 
		Iterator<CharacterProfile> charsI = characterList.iterator();
		while(charsI.hasNext())
		{
			profile = (CharacterProfile)charsI.next();
			if(profile.getId().compareTo(id) == 0)
			{
				ret = true;
				break;
			}
			count++;
		}
		characterList.removeElementAt(count);
		
		count = 0;
		String idFromList = "";

		// remove from id list
		Iterator<String> charIdsI = idList.iterator();
		while(charIdsI.hasNext())
		{
			idFromList = (String)charIdsI.next();
			if(idFromList.compareTo(id) == 0)
			{
				ret = true;
				break;
			}
			count++;
		}
		idList.removeElementAt(count);
		
		// remove all references to character from bookList.xml
		try
		{
			StringBuffer xml = new StringBuffer(Utilities.read_file(path_to_user_dir + "bookList.xml"));
			String xfr = "";
			String tagMrkr = "iBookCharId=\"";
			xfr = Utilities.replaceChars(xml.toString(), tagMrkr + id, tagMrkr, "all");
			xml.replace(0,  xml.length(), "");
			xml.append(xfr);
			Utilities.write_file(path_to_user_dir + "bookList.xml", xml.toString(), true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	public String getCharacterFirstLastNames(String id)
	{
		String nameFirst = "";
		String nameLast = "";
		String names = "no first/last names";
		
		CharacterProfile profile = null;
		Iterator<CharacterProfile> charsI = characterList.iterator();
		while(charsI.hasNext())
		{
			profile = (CharacterProfile)charsI.next();
			if(profile.getId().compareTo(id) == 0)
			{
				nameFirst = profile.getNameFirst();
				nameLast = profile.getNameLast();
				if(nameFirst.length() > 0)
					names = nameFirst;
				if(nameLast.length() > 0)
					names += " " + nameLast;
			}
		}
		return names.trim();
	}
	
	public boolean addCharacterAttribute(String id, String text)
	{
		boolean ret = false;
		
		CharacterProfile profile = null;
		Iterator<CharacterProfile> charsI = characterList.iterator();
		while(charsI.hasNext())
		{
			profile = (CharacterProfile)charsI.next();
			if(profile.getId().compareTo(id) == 0)
			{
				profile.addAttribute(text);
				break;
			}
		}
		return ret;
	}
	
	public Vector<String> getIdList()
	{
		return idList;
	}
	
    public int getCharacterCount()
    {
        return characterList.size();
    }
    
	public boolean addAttribute(String charId, String text)
	{
		boolean ret = true;
		
		Iterator<CharacterProfile> charsI = characterList.iterator();
		CharacterProfile profile = null;
		while(charsI.hasNext())
		{
			profile = (CharacterProfile)charsI.next();
			if(profile.getId().compareTo(charId) == 0)
			{
				profile.addAttribute(text);
				break;
			}
		}
		return ret;
	}
	
	public void addBook(String bookId)
	{
		books.add(bookId);
	}
	
	public String getName(String id)
	{
		String name = "Unnamed";
		CharacterProfile character = null;
		Iterator<CharacterProfile> characterI = characterList.iterator();
		while(characterI.hasNext())
		{
			character = (CharacterProfile)characterI.next();
			if(character.getId().compareTo(id) == 0)
			{
				name = character.getName();
				break;
			}
		}
		return name;
	}
	
	public CharacterProfile getProfile(String id)
	{
		CharacterProfile profile = null;
		
		Iterator<CharacterProfile> charI = characterList.iterator();
		while(charI.hasNext())
		{
			profile = (CharacterProfile)charI.next();
			if(profile != null && profile.getId().compareTo(id) == 0)
				break;
			else
				profile = null;
		}
		return profile;
	}

	public int getNumCharacters()
	{
		return characterList.size();
	}
	public boolean removeCharAttribute(String charId, String deleteAttrNum)
	{
		boolean ret = false;
		// need to convert number entered by user to Vector index (users 
		// see attributes listed by numbers beginning with 1; Vector is 0 indexedJ) 
		int index = Integer.parseInt(deleteAttrNum) - 1;
		
		CharacterProfile profile = null;
		Iterator<CharacterProfile> charI = characterList.iterator();
		while(charI.hasNext())
		{
			profile = (CharacterProfile)charI.next();
			if(profile.getId().compareTo(charId) == 0 && index >= 0 && index <= profile.getAttributes().size())
			{
				profile.getAttributes().removeElementAt(index);
				ret = true;
			}
		}
		return ret;
	}
}
