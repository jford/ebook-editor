package com.iBook.datastore.characters;

import java.io.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import com.iBook.datastore.books.BookListParser;
import com.iBook.utilities.*;

public class CharacterManager
{
    private String character_list_filename = "character_list.properties";
    private String path_to_resources = "";
    private Properties charListProps = null;
    private CharacterList characterList = null;
    
    public CharacterManager(String path_to_resources)
    {
        this.path_to_resources = path_to_resources;
        String xml = path_to_resources + "characterList.xml";
        
        /*
         * The CharacterList object representation of the characterList.xml
         * file is created by the CharacterListParser.
         */
        CharacterListParser parser = new CharacterListParser();
        characterList = parser.parse(xml);
    }
    
    public boolean addAttribute(String charId, String text)
    {
    	return characterList.addAttribute(charId, text);
    }
    
    public boolean updateAliases(String charId, Vector<String> aliases)
    {
    	return characterList.updateAliases(charId, aliases);
    }
    
    public boolean deleteAlias(String charId, int index)
    {
    	return characterList.deleteAlias(charId, index);
    }
    
    public CharacterProfile getProfile(String id)
    {
    	return characterList.getProfile(id);
    }
    
    public Vector<String> getCharacterAttributes(String id)
    {
    	return characterList.getCharacterAttributes(id);
    }
    
    public Vector<String> getCharacterIdList()
    {
    	return characterList.getIdList();
    }
    
    public boolean addCharacterAttribute(String id, String text)
    {
    	return characterList.addCharacterAttribute(id, text);
    }
    
    public String getCharacterFirstLastNames(String id)
    {
    	return characterList.getCharacterFirstLastNames(id);
    }
    
    public boolean deleteProfile(String id)
    {
    	boolean ret = true;
    	characterList.deleteProfile(id);
    	return ret;
    }
 
    public String getName(String id)
    {
    	String name = "";
    	name = characterList.getName(id);
    	return name;
    }
    // returns a list of keys contained in a character profile
    public Enumeration<?> getCharacterDataList(String first_lastNames)
    {
        Properties character_props = new Properties();
        String character_data_filename = first_lastNames + ".properties";
        
        try
        {
            character_props.load(new FileInputStream(path_to_resources + character_data_filename));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return character_props.propertyNames();
    }
    
     // add a new character to the list of available characters
    public boolean addCharacterProfile(CharacterProfile charProfile)
    {
    	return characterList.addCharacterProfile(charProfile);
    }
    
    // add a new character to the character catalogue; user does not do this,
    // it's called from addCharacter()
    private void addCharacterToList(String firstLastNames)
    {
        // Read the list of characters in the current catalogue
        Properties character_list_props = new Properties();
        try
        {
            character_list_props.load(new FileInputStream(path_to_resources + character_list_filename));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        // build a key in the form "character_N" based on the number of 
        // characters in the list... 
        String character_num = "";
        String new_character_key = "";
        character_num = Integer.toString(getCharacterCount());
        new_character_key = "character_" + character_num;
        
        // ...and store it in the catalog
        character_list_props.setProperty(new_character_key, firstLastNames);
        try
        {
            OutputStream propOut = new FileOutputStream(new File(path_to_resources + character_list_filename));
            character_list_props.store(propOut, "Character list");
            propOut.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public String getListFilename()
    {
        return character_list_filename;
    }
    
    public int getCharacterCount()
    {
        return characterList.getCharacterCount();
    }
    public boolean removeCharAttribute(String charId, String deleteAttrNum)
    {
    	return characterList.removeCharAttribute(charId, deleteAttrNum);
    }

}
