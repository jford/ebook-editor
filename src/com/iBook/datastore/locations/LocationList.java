package com.iBook.datastore.locations;

import java.text.Collator;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import com.iBook.datastore.*;

/*
 * The LocationList class is the Java object representation of the locationList.xml
 * datas repository for locations and geolocales.
 */
public class LocationList
{
	private Vector<LocationProfile> locationList = new Vector<LocationProfile>(); 
	private Vector<GeolocaleProfile> geolocaleList = new Vector<GeolocaleProfile>(); 
//	private Vector<String> idList = new Vector<String>();
	private Vector<String> aliases = new Vector<String>();
//	private Vector<String> geolocaleIdList = new Vector<String>();
	private Vector<String> books = new Vector<String>();
	
    private Collator col = Collator.getInstance(Locale.US);
	
	public LocationList()
	{
		
	}
	
	public boolean addGeolocaleAttribute(String geolocId, String text)
	{
		boolean ret = false;
		GeolocaleProfile profile = null;
		Iterator<GeolocaleProfile> geolocI = geolocaleList.iterator();
		while(geolocI.hasNext())
		{
			profile = (GeolocaleProfile)geolocI.next();
			if(profile.getId().compareTo(geolocId) == 0)
			{
				profile.addDescription(text);
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public boolean deleteAlias(PropsManager.ObjType type, String loc_id, int index)
	{
		boolean ret = false;
		
		switch(type)
		{
		case LOCATION:
			LocationProfile locProfile = null;
			Iterator<LocationProfile> locsI = locationList.iterator();
			while (locsI.hasNext())
			{
				locProfile = (LocationProfile)locsI.next();
				if(locProfile.getId().compareTo(loc_id) == 0)
				{
					locProfile.deleteAlias(index);
					ret = true;
					break;
				}
			}
			break;
		case GEOLOCALE:
			GeolocaleProfile geolocProfile = null;
			Iterator<GeolocaleProfile> geolocsI = geolocaleList.iterator();
			while (geolocsI.hasNext())
			{
				geolocProfile = (GeolocaleProfile)geolocsI.next();
				if(geolocProfile.getId().compareTo(loc_id) == 0)
				{
					geolocProfile.deleteAlias(index);
					ret = true;
					break;
				}
			}
			break;
		case BOOK:
		case CHARACTER:
		case TEMPLATE:
			break;
		}
		
		return ret;
	}
	
	public boolean updateAliases(String loc_id, Vector<String> aliases)
	{
		boolean ret = false;
		LocationProfile profile = null;
		Iterator<LocationProfile> locsI = locationList.iterator();
		while(locsI.hasNext())
		{
			profile = (LocationProfile) locsI.next();
			if(profile.getId().compareTo(loc_id) == 0)
			{
				profile.updateAliases(aliases);
				ret = true;
				break;
			}
		}
		return ret;
	}
	
    public boolean addLocationAttribute(String loc_id, String text)
    {
    	boolean ret = false;
    	LocationProfile profile = null;
    	Iterator<LocationProfile> locationI = locationList.iterator();
    	while(locationI.hasNext())
    	{
    		profile = (LocationProfile)locationI.next();
    		if(profile.getId().compareTo(loc_id) == 0)
    		{
    			profile.addLocationDesc(text);
    			ret = true;
    			break;
    		}
    	}
    	return ret;
    }
    
	public boolean removeGeolocAttribute(String geolocId, String deleteAttrNum)
	{
		boolean ret = false;
		// need to convert number entered by user to Vector index (users 
		// see attributes listed by numbers beginning with 1; Vector is 0 indexedJ) 
		int index = Integer.parseInt(deleteAttrNum.trim()) - 1;
		
		GeolocaleProfile profile = null;
		Iterator<GeolocaleProfile> geolocI = geolocaleList.iterator();
		while(geolocI.hasNext())
		{
			profile = (GeolocaleProfile)geolocI.next();
			if(profile.getId().compareTo(geolocId) == 0 && index >= 0 && index <= profile.getDescriptions().size())
			{
				profile.getDescriptions().removeElementAt(index);
				ret = true;
			}
		}
		return ret;
	}
	
	public boolean removeLocAlias(String loc_id, String deleteAliasNum)
	{
		boolean ret = false;
		// need to convert number entered by user to Vector index (users 
		// see attributes listed by numbers beginning with 1; Vector is 0 indexedJ) 
		int index = Integer.parseInt(deleteAliasNum.trim()) - 1;
		
		LocationProfile profile = null;
		Iterator<LocationProfile> locI = locationList.iterator();
		while(locI.hasNext())
		{
			profile = (LocationProfile)locI.next();
			if(profile.getId().compareTo(loc_id) == 0 && index >= 0 && index <= profile.getLocationDescriptions().size())
			{
				profile.getAliases().removeElementAt(index);
				ret = true;
			}
		}
		return ret;
	}
	
	public boolean removeLocAttribute(String loc_id, String deleteAttrNum)
	{
		boolean ret = false;
		// need to convert number entered by user to Vector index (users 
		// see attributes listed by numbers beginning with 1; Vector is 0 indexedJ) 
		int index = Integer.parseInt(deleteAttrNum) - 1;
		
		LocationProfile profile = null;
		Iterator<LocationProfile> locI = locationList.iterator();
		while(locI.hasNext())
		{
			profile = (LocationProfile)locI.next();
			if(profile.getId().compareTo(loc_id) == 0 && index >= 0 && index <= profile.getLocationDescriptions().size())
			{
				profile.getLocationDescriptions().removeElementAt(index);
				ret = true;
			}
		}
		return ret;
	}
	
	public boolean addLocationProfile(LocationProfile profile)
	{
		boolean ret = true;
		locationList.add(profile);
		return ret;
	}

	public boolean deleteProfile(PropsManager.ObjType type, String id)
    {
		boolean ret = false;
		
		switch(type)
		{
		case LOCATION:
			Iterator<LocationProfile> locationsI = locationList.iterator();
			LocationProfile profile = null;
			int count = 0;
			while(locationsI.hasNext())
			{
				profile = (LocationProfile)locationsI.next();
				if(profile.getId().compareTo(id) == 0)
				{
					ret = true;
					break;
				}
				count++;
			}
			if(ret)	
				locationList.removeElementAt(count);
			break;
		case GEOLOCALE:
			Iterator<GeolocaleProfile> geolocsI = geolocaleList.iterator();
			GeolocaleProfile geolocProfile = null;
			count = 0;
			while(geolocsI.hasNext())
			{
				geolocProfile = (GeolocaleProfile)geolocsI.next();
				if(geolocProfile.getId().compareTo(id) == 0)
				{
					ret = true;
					break;
				}
				count++;
			}
			if(ret)	
				geolocaleList.removeElementAt(count);
			break;
		case BOOK:
		case CHARACTER:
		case MANUSCRIPT:
		case TEMPLATE:
		}
    	return ret;
    }
    

	public void addGeolocaleProfile(GeolocaleProfile item)
	{
		geolocaleList.add(item);
	}
	
    public void addGeolocaleToLocation(String geolocId, String loc_id)
    {
    	LocationProfile profile = null;
    	Iterator<LocationProfile> profI = locationList.iterator();
    	while(profI.hasNext())
    	{
    		profile = (LocationProfile)profI.next();
    		if(profile.getId().compareTo(loc_id) == 0)
    			break;
    	}
    	profile.addGeolocaleId(geolocId);
    }
    public boolean removeGeolocReference(String geolocId, String loc_id)
    {
    	boolean ret = false;
    	LocationProfile profile =  null;
    	Iterator<LocationProfile> locsI = locationList.iterator();
    	while(locsI.hasNext())
    	{
    		profile = (LocationProfile)locsI.next();
    		if(profile.getId().compareTo(loc_id) == 0)
    			ret = profile.removeGeolocReference(geolocId);
    	}
    	return ret;
    }
	
	public Vector<String> getLocationDescriptions(String id)
	{
		Vector<String> locDescs = null;
		Iterator<LocationProfile> profI = locationList.iterator();
		LocationProfile profile = null;
		while(profI.hasNext())
		{
			profile = (LocationProfile)profI.next();
			if(profile.getId().compareTo(id) == 0)
			{
				locDescs = profile.getLocationDescriptions();
				break;
			}
		}
		return locDescs;
	}
	
	public Vector<String> getGeolocaleAttributes(String geolocId)
	{
		Vector<String> attrs = null;

		GeolocaleProfile profile = null;
		Iterator<GeolocaleProfile> geolocsI = geolocaleList.iterator();
		while(geolocsI.hasNext())
		{
			profile = (GeolocaleProfile)geolocsI.next();
			if(profile.getId().compareTo(geolocId) == 0)
			{
				attrs = profile.getDescriptions();
				break;
			}
		}
		return attrs;
		
	}
	
	public GeolocaleProfile getGeolocale(String geolocaleId)
	{
		GeolocaleProfile geolocaleProfile = null;
		Iterator<GeolocaleProfile> profI = geolocaleList.iterator();
		while(profI.hasNext())
		{
			geolocaleProfile = (GeolocaleProfile)profI.next();
			if(geolocaleProfile.getId().compareTo(geolocaleId) == 0)
				break;
			else
				geolocaleProfile = null;
		}
		return geolocaleProfile;
	}
	
	public String getGeolocaleName(String id)
	{
		String name = "";
		GeolocaleProfile geolocaleProfile = null;
		Iterator<GeolocaleProfile> profI = geolocaleList.iterator();
		while(profI.hasNext())
		{
			geolocaleProfile = (GeolocaleProfile)profI.next();
			if(geolocaleProfile.getId().compareTo(id) == 0)
				break;
			else
				geolocaleProfile = null;
		}
		return geolocaleProfile.getName();
	}
	
	public String getGeolocaleType(String id)
	{
		String type = "";
		GeolocaleProfile geolocaleProfile = null;
		Iterator<GeolocaleProfile> profI = geolocaleList.iterator();
		while(profI.hasNext())
		{
			geolocaleProfile = (GeolocaleProfile)profI.next();
			if(geolocaleProfile.getId().compareTo(id) == 0)
				break;
			else
				geolocaleProfile = null;
		}
		return geolocaleProfile.getType();
	}
	
	public void addAlias(String alias)
	{
		aliases.add(alias);
	}
	
	public void addBook(String bookId)
	{
		books.add(bookId);
	}
	
	public Vector<String> getLocationIdList()
	{
		// returns a list of location ids, sorted by location name
		Vector<String> sortedList = new Vector<String>();
		Vector<String> returnList = new Vector<String>();
		Iterator<LocationProfile> idL = locationList.iterator();
		String item = "";
		LocationProfile location = null;
		String itemSeparator = "###ID###";
		while(idL.hasNext())
		{
			location = (LocationProfile)idL.next();
			item = location.getName() + itemSeparator + location.getId();
			sortedList.add(item);
		}
        Collections.sort(sortedList, col);
        Iterator<String> sortedI = sortedList.iterator();
        Collections.sort(sortedList, col);

        Iterator<String> idSortedI = sortedList.iterator();
        while(idSortedI.hasNext())
        {
        	String[] items = {"",""};
        	item = (String)idSortedI.next();
        	items = item.split(itemSeparator);
        	returnList.add(items[1]);
        }
		return returnList;
	}
/*
	public void addGeolocaleId(String geolocaleId)
	{
		geolocaleIdList.add(geolocaleId);
	}
	*/
	
	public Vector<String> getGeolocaleIdList()
	{
		Vector<String> idList = new Vector<String>();
		Iterator<GeolocaleProfile> geolocsI = geolocaleList.iterator();
		GeolocaleProfile profile = null;
		while(geolocsI.hasNext())
		{
			profile = (GeolocaleProfile)geolocsI.next();
			idList.add(profile.getId());
		}
		return idList;
	}

	public int getLocationCount()
	{
		return locationList.size();
	}

	public int getGeolocsCount()
	{
		return geolocaleList.size();
	}

	public String getName(String id)
	{
		String name = "Unnamed";
		LocationProfile location = null;
		Iterator<LocationProfile> locI = locationList.iterator();
		while(locI.hasNext())
		{
			location = (LocationProfile)locI.next();
			if(location.getId().compareTo(id) == 0)
			{
				name = location.getName();
				break;
			}
		}
		return name;
	}
	
	public Object getProfile(PropsManager.ObjType type, String id)
	{
		Object profile = null;
		LocationProfile locProfile = null;
		GeolocaleProfile geolocProfile = null;
		
		switch(type)
		{
		case LOCATION:
			Iterator<LocationProfile> locsI = locationList.iterator();
			while(locsI.hasNext())
			{
				locProfile = (LocationProfile)locsI.next();
				if(locProfile.getId().compareTo(id) == 0)
					break;
			}
			profile = locProfile;
			break;
		case GEOLOCALE:
			Iterator<GeolocaleProfile> geolocsI = geolocaleList.iterator();
			while(geolocsI.hasNext())
			{
				geolocProfile = (GeolocaleProfile)geolocsI.next();
				if(geolocProfile.getId().compareTo(id) == 0)
					break;
			}
			profile = geolocProfile;
			break;
		}
		return profile;
	}
}
