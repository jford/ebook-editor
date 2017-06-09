package com.iBook.datastore.characters;

import java.util.*;

public class CharacterProfile
{
	private String nameShort = "";
	private String namePrefix = "";
	private String nameFirst = "";
	private String nameMiddle = "";
	private String nameLast = "";
	private String nameSuffix = "";
	private String gender = "undefined";
	private String age = "not specified";
	private String context = "";
	
	private String id = "";
	
	private Vector<String> attributes = new Vector<String>();
	private Vector<String> books = new Vector<String>();
	private Vector<String> aliases = new Vector<String>();
	
	public CharacterProfile()
	{
	}
	
	public void setContext(String context)
	{
		this.context = context;
	}
	public String getContext()
	{
		// context describes character's role in the narrative
		// only valid if character profile is defined in a template
		return context;
	}
	
	public boolean addAlias(String alias)
	{
		boolean ret = true;
		// check to see if alias already exits
		Iterator<String> aliasesI = aliases.iterator();
		while(aliasesI.hasNext())
		{
			if(aliasesI.next().compareTo(alias) == 0)
			{
				// yes? set ret to false and exit loop
				ret = false;
				break;
			}
		}
		// if ret still true, the alias does not already exist
		if(ret)
			// add it
			aliases.addElement(alias);
		
		// false if the alias already exists and therefore was not added,
		// otherwise ret is still true
		return ret;
	}
	
	public Vector<String> getAliases()
	{
		return aliases;
	}
	
	public boolean updateAliases(Vector<String> aliases)
	{
		// replaces the aliases vector with a new set of aliases
		boolean ret = true;
		this.aliases = aliases;
		return ret;
	}
	
	public boolean deleteAlias(int index)
	{
		boolean ret = false;
		if(aliases.size() >= index)
		{
			// index is position of the alias in an ordered list, beginning at one;
			// elements in the vector or 0-indexed; remove the element at index - 1
			aliases.removeElementAt(index - 1);
			ret = true;
		}
		return ret;
	}
	
	public boolean deleteAttribute(int index)
	{
		boolean ret = false;
		if(attributes.size() >= index)
		{
			// index is position of the attribute in an ordered list, beginning at one;
			// elements in the vector or 0-indexed; remove the element at index - 1
			attributes.removeElementAt(index - 1);
			ret = true;
		}
		return ret;
	}
	
	public boolean addAttribute(String attribute)
	{
		boolean ret = true;
		if(attribute.length() > 0)
			attributes.add(attribute);
		else
			ret = false;
		return ret;
	}

	public Vector<String> getAttributes()
	{
		return attributes;
	}
	
	public void addBook(String bookId)
	{
		books.add(bookId);
	}

	public Vector<String> getBooks()
	{
		return books;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getGender()
	{
		return gender;
	}

	public void setShortName(String nameShort)
	{
		this.nameShort = nameShort;
	}

	public String getShortName()
	{
		String shortName = nameShort;
		
/*		if(shortName.length() == 0)
			shortName = nameFirst;
		if(shortName.length() == 0)
			shortName = namePrefix;
		if(shortName.length() == 0)
			shortName = nameLast;
		if(shortName.length() == 0)
			shortName = nameMiddle;
		if(shortName.length() == 0)
			shortName = nameSuffix;
*/		
		return shortName;
	}
	
	public void setAge(String age)
	{
		this.age = age;
	}
	public String getAge()
	{
		return age;
	}

	public String getName()
	{
		// assume caller wants the character name for a list that should
		// differentiate
		// among similarly named characters. If either nameShort or nameFirst variable
		// has a value, return it and last name, otherwise follow the logic of getShortName()

		String name = "unnamed";
		
		// best choice---short name + last name
		if (nameShort.length() >= 1)
			name = nameShort + " " + nameLast;
		// 2nd best---first name + last name
		else if(nameFirst.length() >= 1)
			name = nameFirst + " " + nameLast;
		// 3rd best---prefix + last name 
		else if(namePrefix.length() >=1)
		{
			name = namePrefix;
			// 4th choice---prefix + first name
			if(nameLast.length() >= 1)
				name += " " + nameLast;
			else
				name += " " + nameFirst;
		}
		// last choice---last name only
		else if(nameLast.length() > 0)
			name = nameLast;
		
		return name.trim();

	}
	public String getNameFormal()
	{
		String formalName = "";
	
		// if there is a namePrefix, use it with the last name...
		if(namePrefix.length() > 0)
		{
			formalName = namePrefix.trim();
			if(nameFirst.length() > 0)
				formalName += " " + nameFirst.trim() + " ";
			if(nameLast.length() > 0)
				formalName += " " + nameLast.trim();
		}
		// if no prefix is there a first nameE?
		else if(nameFirst.length() > 0)
		{
			formalName = nameFirst.trim();
			if(nameLast.length() > 0)
				formalName += " " + nameLast.trim();
			
		}
		// otherwise follow the logic of getName()
		else
			formalName = getName().trim();
			
		return formalName.trim();
	}

	public void setNamePrefix(String namePrefix)
	{
		this.namePrefix = namePrefix;
	}

	public String getNamePrefix()
	{
		return namePrefix;
	}

	public void setNameFirst(String nameFirst)
	{
		this.nameFirst = nameFirst;
	}

	public String getNameFirst()
	{
		return nameFirst;
	}

	public void setNameMiddle(String nameMiddle)
	{
		this.nameMiddle = nameMiddle;
	}

	public String getNameMiddle()
	{
		return nameMiddle;
	}

	public void setNameLast(String nameLast)
	{
		this.nameLast = nameLast;
	}

	public String getNameLast()
	{
		return nameLast;
	}

	public void setNameSuffix(String nameSuffix)
	{
		this.nameSuffix = nameSuffix;
	}

	public String getNameSuffix()
	{
		return nameSuffix;
	}

	public String getId()
	{
		return id;
	}

    public void updateDescriptions(Vector<String> descriptions)
    {
    	attributes = descriptions;
    }
}
