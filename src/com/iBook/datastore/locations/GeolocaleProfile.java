package com.iBook.datastore.locations;

import java.util.*;

public class GeolocaleProfile
{
	private String name = "";
	private String id = "";
	private String type = "";
	private Vector<String> descriptions = new Vector<String>();
	private Vector<String> aliases = new Vector<String>();

	public GeolocaleProfile()
	{
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

	public boolean deleteAlias(int index)
	{
		boolean ret = false;
		if(aliases.size() >= index)
		{
			// index into alias vector (0-based)
			if(aliases.size() > index)
				// if index is 0, size() must be at least 1
				aliases.removeElementAt(index);
			ret = true;
		}
		return ret;
	}
	
	public boolean deleteAlias(String deleteAliasNum)
	{
		boolean ret = false;
		// deleteAliasNum is number of the alias to be deleted in a 
		// numbered list, beginning with 1
		int deleteNum = new Integer(deleteAliasNum);
		if(aliases.size() >= deleteNum)
		{
			// index is position of the alias in an ordered list, beginning at one;
			// elements in the vector or 0-indexed; remove the element at index - 1
			aliases.removeElementAt(deleteNum - 1);
			ret = true;
		}
		return ret;
	}
	
	public boolean deleteAttribute(int index)
	{
		boolean ret = false;
		if(descriptions.size() >= index)
		{
			// index into alias vector (0-based)
			if(descriptions.size() > index)
				// if index is 0, size() must be at least 1
				descriptions.removeElementAt(index);
			ret = true;
		}
		return ret;
	}
	
	public boolean setType(String type)
	{
		boolean ret = true;
		this.type = type;
		return ret;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	public String getId()
	{
		return id;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return name;
	}
	public void addDescription(String desc)
	{
		descriptions.add(desc);
	}
	public Vector<String> getDescriptions()
	{
		return descriptions;
	}
	public void updateDescriptions(Vector<String> descriptions)
	{
		this.descriptions = descriptions;
	}
	public void updateAliases(Vector<String> aliases)
	{
		this.aliases = aliases;
	}
}
