package com.iBook.datastore.locations;

import java.util.*;

import com.iBook.utilities.Utilities;

public class LocationProfile
{
    private String name = "";
    private String id = "";
    private String type = "undefined entity";
    private Vector<String> aliases = new Vector<String>();
    private Vector<String> geolocaleIds = new Vector<String>();
    private Vector<String> books = new Vector<String>();
    private Vector<String> locationDescriptions = new Vector<String>();
    
    public LocationProfile()
    {
    }
    public void updateDescriptions(Vector<String> descriptions)
    {
    	locationDescriptions = descriptions;
    }
    
    public void addLocationDesc(String locationDescription)
    {
    	locationDescriptions.add(locationDescription);
    }
    
    public Vector<String> getLocationDescriptions()
    {
    	return locationDescriptions;
    }
    
    public boolean updateAliases(Vector<String> aliases)
    {
    	boolean ret = true;
    	this.aliases = aliases;
    	return ret;
    }
    
    public boolean deleteAlias(int index)
    {
    	boolean ret = false;
    	if(aliases.size() >= index)
    	{
    		aliases.removeElementAt(index);
    		ret = true;
    	}
    	return ret;
    }
    
    public boolean deleteLocationDescription(int index)
    {
    	// location description is what location attributes are called
    	boolean ret = false;
    	if(locationDescriptions.size() >= index)
    	{
    		locationDescriptions.removeElementAt(index);
    		ret = true;
    	}
    	return ret;
    }
    
    public void addBook(String bookId)
    {
    	books.add(bookId);
    }
    public Vector<String> getBooks()
    {
    	return books;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }
    
    public void setId(String id)
    {
    	this.id = Utilities.replaceChars(id, " ", "_", "all").trim().toLowerCase();
    }
    public String getId()
    {
    	return id;
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

    public void addGeolocaleId(String geolocaleId)
    {
    	geolocaleIds.add(geolocaleId);
    }
    
    public Vector<String> getGeolocaleIds()
    {
        return geolocaleIds;
    }
    
    public void setType(String type)
    {
    	this.type = type;
    }
    public String getType()
    {
    	return type;
    }
    
    public boolean removeGeolocReference(String geolocId)
    {
    	boolean ret = false;
    	int index = 0;
    	String refId = "";
    	Iterator<String> geolocRefI = geolocaleIds.iterator();
    	while(geolocRefI.hasNext())
    	{
    		refId = (String)geolocRefI.next();
    		if(refId.compareTo(geolocId) == 0)
    		{
    			break;
    		}
    		else
    			index++;
    	}
    	if(index < geolocaleIds.size())
    	{
    		geolocaleIds.removeElementAt(index);
    		ret = true;
    	}
    	return ret;
    }
    			
}
