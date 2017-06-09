package com.iBook.datastore.locations;

import java.io.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import com.iBook.utilities.*;
import com.iBook.datastore.*;
import com.iBook.datastore.characters.CharacterListParser;

public class LocationManager
{
    private Vector<String> locationsList = null;
    private String path_to_resources = "";
    private LocationList locationList = null;
    
    public LocationManager(String path_to_resources)
    {
        this.path_to_resources = path_to_resources;
        
        LocationListParser parser = new LocationListParser();
        String xml = this.path_to_resources + "locationList.xml";
        locationList = parser.parse(xml);
    }
    
    public boolean updateAliases(String loc_id, Vector<String> aliases)
    {
    	return locationList.updateAliases(loc_id, aliases);
    }
    
    public boolean addGeolocaleAttribute(String geolocId, String text)
    {
    	return locationList.addGeolocaleAttribute(geolocId, text);
    }
    
    public boolean addLocationProfile(LocationProfile profile)
    {
    	return locationList.addLocationProfile(profile);
    }
    
    public boolean deleteProfile(PropsManager.ObjType type, String id)
    {
    	boolean ret = false;
    	switch(type)
    	{
    	case LOCATION:
    	case GEOLOCALE:
    		ret = locationList.deleteProfile(type, id);
    		break;
    	case BOOK:
    	case MANUSCRIPT:
    	case TEMPLATE:
    	}
    	return ret;
    }
    
    public boolean addLocationAttribute(String loc_id, String text)
    {
    	return locationList.addLocationAttribute(loc_id, text);
    }
    
    public String getGeolocaleName(String id)
    {
    	return locationList.getGeolocaleName(id);
    }
    
    public String getGeolocaleType(String id)
    {
    	return locationList.getGeolocaleType(id);
    }
    
    public Vector<String> getGeolocaleAttributes(String geolocId)
    {
    	return locationList.getGeolocaleAttributes(geolocId);
    }
    
    public boolean removeGeolocAttribute(String geolocId, String deleteAttrNum)
    {
    	return locationList.removeGeolocAttribute(geolocId, deleteAttrNum);
    }
    
    public boolean deleteAlias(PropsManager.ObjType type, String loc_id, int index)
    {
    	return locationList.deleteAlias(type, loc_id, index);
    }
    
    public boolean removeLocAlias(String loc_id, String deleteAliasNum)
    {
    	return locationList.removeLocAlias(loc_id, deleteAliasNum);
    }
    
    public boolean removeLocAttribute(String loc_id, String deleteAttrNum)
    {
    	return locationList.removeLocAttribute(loc_id, deleteAttrNum);
    }
    
    public Object getProfile(PropsManager.ObjType type, String id)
    {
    	return locationList.getProfile(type, id);
    }
    // returns a list of location ids
    public Vector<String> getLocationIdList()
    {
        return locationList.getLocationIdList();
    }

    public Vector<String> getGeolocaleIdList()
    {
        return locationList.getGeolocaleIdList();
    }

    // add a new location to the list of available locations
    public boolean addLocation(String location)
    {
    	String encoded_location = Utilities.replaceChars(location, " ", "_", "all");
        // first, check to see if a location of the same name 
        // already exists in the catalog
        boolean exists = locationExists(encoded_location);
        
        // if no, then add it...
        if(!exists)
        {
            // First, a properties filename based on the location'path_to_resources name... 
            String location_profile_name = encoded_location + ".properties";
            
            // create a Properties object and add new key/value pair location name
            Properties props = new Properties();
            props.setProperty("name", location);
            
            // save the Properties object to file
            File propsFile = new File(path_to_resources + location_profile_name);
            try
            {
                OutputStream propOut = new FileOutputStream(propsFile);
                props.store(propOut, location + " profile");
                propOut.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            // ...then add the location to the location catalogue, maintained in 
            // the location_list.properties file 
            addLocationToList(encoded_location);
        }
        return exists;
    }
    
    public void addGeolocaleProfile(GeolocaleProfile geolocale)
    {
    	locationList.addGeolocaleProfile(geolocale);
    }

    /*
    public void addGeolocaleId(String id)
    {
    	locationList.addGeolocaleId(id);
    }
    */
    
    public void addGeolocaleToLocation(String geolocId, String loc_id)
    {
    	locationList.addGeolocaleToLocation(geolocId, loc_id);
    	
    }
    
    public boolean removeGeolocReference(String geolocId, String loc_id)
    {
    	return locationList.removeGeolocReference(geolocId, loc_id);
    }
    
    // add a new location to the location catalogue; user does not do this,
    // it'path_to_resources called from addlocation()
    private void addLocationToList(String encoded_location)
    {
        String character_num = "";
        String new_character_key = "";
        character_num = Integer.toString(getLocationsCount());
        new_character_key = "book_" + character_num;
        

    }
    public String getName(String id)
    { 
    	String name = "";
    	name = locationList.getName(id);
    	return name;
    }
    
    public Vector<String> getLocationDescriptions(String id)
    {
    	return locationList.getLocationDescriptions(id);
    }

    public int getLocationsCount()
    {
        return locationList.getLocationCount();
    }
    
    public int getGeolocsCount()
    {
        return locationList.getGeolocsCount();
    }
    
    public GeolocaleProfile getGeolocale(String geolocaleId)
    {
    	return (GeolocaleProfile)locationList.getProfile(PropsManager.ObjType.GEOLOCALE, geolocaleId);
    }
    
    // does the catalogue already contain a location with the 
    // same first and last names?
    public boolean locationExists(String encoded_location)
    {
        boolean exists = false;
        String location_profile_name = encoded_location + ".properties";
        File propsFile = new File(path_to_resources + location_profile_name);
        exists = propsFile.exists();
        return exists;
    }
    
/*    public String getListFilename()
    {
        return location_list_filename;
    }
*/
    // add a key/value pair to a profile
    public void addProperty(String profile, String key, String value)
    {
        Properties props = getProps(profile);
        if(!value.isEmpty() || value.compareTo(" ") != 0)
            props.setProperty(key, value);
        else
            props.remove(key);
        File propsFile = new File(path_to_resources + profile + ".properties");
        if(propsFile.exists())
        {
            try
            {
                OutputStream propOut = new FileOutputStream(propsFile);
                props.store(propOut, profile + " profile");
                propOut.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    // add multiple key/value pairs to a profile
    public void addProperties(String profile, String[][] data)
    {
        int num = data.length;
        Properties props = getProps(profile);
        String key = "";
        String value = "";
        for(int count = 0; count < num; count++)
        {
            key = data[count][0];
            value = data[count][1];
            if(!value.isEmpty() || value.compareTo(" ") != 0)
                props.setProperty(key, value);
            else
                props.remove(key);
        }
        File propsFile = new File(path_to_resources + profile + ".properties");
        try
        {
            OutputStream propOut = new FileOutputStream(propsFile);
            props.store(propOut, profile + " profile");
            propOut.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    // delete a property from a profile
    public void removeProperty(String profile, String key)
    {
        Properties props = getProps(profile);
        props.remove(key);
        File propsFile = new File(path_to_resources + profile + ".properties");
        try
        {
            OutputStream propOut = new FileOutputStream(propsFile);
            props.store(propOut, profile + " profile");
            propOut.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    // delete multiple properties from a profile
    public void removeProperties(String profile, String[] keys)
    {
        int num = keys.length;
        Properties props = getProps(profile);
        for(int count = 0; count < num; count++)
        {
            props.remove(keys[count]);
        }
        File propsFile = new File(path_to_resources + profile + ".properties");
        try
        {
            OutputStream propOut = new FileOutputStream(propsFile);
            props.store(propOut, profile + " profile");
            propOut.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    // get a Properties object
    public Properties getProps(String profile)
    {
        Properties props = null;
        File propFile = new File(path_to_resources + Utilities.encodeString(profile) + ".properties");
        if(propFile.exists())
        {
            props = new Properties();
            try
            {
                props.load(new FileInputStream(propFile));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return props;
    }
    
    // count the number of properties contained in a profile
    public int getPropertyCount(String profile)
    {
        int count = 0;
        Properties props = getProps(Utilities.encodeString(profile));
        Enumeration<?> list = props.propertyNames();
        while(list.hasMoreElements())
        {
            count++;
            list.nextElement();
        }
        return count;
    }
    
    // remove a location from the location list and delete its profile
    public void removeLocation(String profile)
    {
        profile = Utilities.encodeString(profile);
        // get the current list of locations
        Properties props = new Properties();
        try
        {
            props.load(new FileInputStream(path_to_resources + "location_list.properties"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        // hand the properties off to the generic "remove" method
        props = removeProfile(profile, props);
        
        // write edited list of properties to file
        File propsFile = new File(path_to_resources + "location_list.properties");
        try
        {
            OutputStream propOut = new FileOutputStream(propsFile);
            props.store(propOut, "Location list profile");
            propOut.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        // remove the location-profile properties file
        File locationFile = new File(path_to_resources + profile + ".properties");
        locationFile.delete();
    }
    
    private Properties removeProfile(String profile, Properties props)
    {
        // props is one of book, character, or location_list.properties
        Enumeration<?> dataKeys = props.propertyNames();
        
        // count the number of items in the list
        int num = 0;
        while(dataKeys.hasMoreElements())
        {
            num++;
            dataKeys.nextElement();
        }
        
        // get list of keys from the properties object...
        dataKeys = props.propertyNames();
        
        // ...and build a string array out of the keys and values, minus 
        // the property being deleted
        String[][] data_list = new String [num--][2];
        String key = "";
        String value = "";
        int count = 0;
        while(dataKeys.hasMoreElements())
        {
            key = (String)dataKeys.nextElement();
            value = props.getProperty(key);
            
            // this is the one to skip
            if(value.compareTo(profile) == 0)
                continue;
            
            data_list[count][0] = key;
            data_list[count][1] = value;
            count++;
        }
        
        // rebuild the properties object...
        int idx = key.indexOf("_");
        String profile_type = key.substring(0, idx + 1);
        props.clear();
        for(count = 0; count < num; count++)
        {
            props.setProperty(profile_type + Integer.toString(count), data_list[count][1]);
        }
        // ...and return it to the calling method
        return props;
    }
    
    // retrieve the value of a specified key from a profile
    public String getProperty(String profile, String key)
    {
//        System.out.println("in LocationManager.getProperty(), profile = " + profile + "; key = " + key);
        Properties props = getProps(Utilities.encodeString(profile));
        return props.getProperty(key);
    }
}
