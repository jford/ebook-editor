package com.iBook.datastore.books;

import java.io.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import com.iBook.utilities.*;

public class BookManager
{
    private Enumeration<?> book_list = null;
    private int bookCount = 0;
    private static String path_to_resources = null;
    private String book_list_filename = "bookList.xml";
    private BookList bookList = null;
    
    public BookManager(String path_to_resources)
    {
        this.path_to_resources = path_to_resources;

        BookListParser parser = new BookListParser();
        String xml = this.path_to_resources + "bookList.xml";
         bookList = parser.parse(xml);
    }
    
    public boolean addProfile(Object profile)
    {
    	return bookList.addProfile(profile);
    }
    
    public boolean deleteProfile(String id)
    {
    	return bookList.deleteProfile(id);
    }
    
    public String getAuthorName(String bookId)
    {
    	return bookList.getAuthorName(bookId);
    }
    
    public String getBookSourceTitle(String bookId)
    {
    	return bookList.getBookSourceTitle(bookId);
    }
    
    public String getSourceAuthorName(String bookId)
    {
    	return bookList.getSourceAuthorName(bookId);
    }

    // returns a list of keys in the book catalog
    public Vector<String> getBookIdList()
    {
    	return bookList.getIdList();
    }
    
    // returns collection of book profiles
    /*
    public BookProfile getBookProfile(String id)
    {
    	BookProfile bookProfile = null;
    	
    	return bookList.getBookProfile(id);
    }
    */
    
    public BookProfile getProfile(String id)
    {
    	return bookList.getProfile(id);
    }
    
/*    public Vector<String> getCharactersForBook(String bookId)
    {
    	return bookList.getCharactersForBook(bookId);
    }
    public Vector<String> getLocationsForBook(String bookId)
    {
    	return bookList.getLocationsForBook(bookId);
    }
    
*/    // add a new character to the list of available characters
    public boolean addBook(String title)
    {
        // first, check to see if a character of the same name 
        // already exists in the catalog
        boolean exists = bookExists(title);
        
        // if no, then add it...
        if(!exists)
        {
            // First, a properties filename based on the book's name... 
            String book_profile_name = title + ".properties";
            book_profile_name = Utilities.replaceChars(book_profile_name, " ", "_", "all");
            
            // create a Properties object and add new key/value pairs for first
            // and last names
            Properties book_props = new Properties();
            book_props.setProperty("title", title);
            
            // save the Properties object to file
            File propsFile = new File(path_to_resources + "\\" + book_profile_name);
            try
            {
                OutputStream propOut = new FileOutputStream(propsFile);
                book_props.store(propOut, title + " profile");
                propOut.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            // ...then add the book to the book catalogue, maintained in 
            // the book_list.properties file 
            addBookToList(title);
        }
        return exists;
    }
    
    // does the catalogue already contain a character with the 
    // same first and last names?
    public boolean bookExists(String title)
    {
        title = Utilities.replaceChars(title, " ", "_", "all");
        boolean exists = false;
        String book_profile_name = title + ".properties";
        File propsFile = new File(path_to_resources + book_profile_name);
        exists = propsFile.exists();
        return exists;
    }
    
    // add a new book title to the book catalogue; user does not do this,
    // it's called from addBook()
    private void addBookToList(String title)
    {
        title = Utilities.replaceChars(title, " ", "_", "all");
/*        // Read the list of titles in the current catalogue
        try
        {
//            book_list_props.load(new FileInputStream(path_to_resources + book_list_filename));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
*/        
        // build a key in the form "character_N" based on the number of 
        // characters in the list... 
        String character_num = "";
        String new_character_key = "";
        character_num = Integer.toString(getBookCount());
        new_character_key = "book_" + character_num;
        
/*        // ...and store it in the catalogue
//        book_list_props.setProperty(new_character_key, title);
        try
        {
            OutputStream propOut = new FileOutputStream(new File(path_to_resources + book_list_filename));
//            book_list_props.store(propOut, "Character list");
            propOut.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
*/    }
    
    public int getBookCount()
    {
        return bookList.getBookCount();
    }
    
    // get encoded_title value specified by key from the book catalogue
    public String getEncodedTitle(String key)
    {
        // book catalog entries are in the form:
        //
        //     book_N=encoded_title
        //
        // the method will return the encoded_title string associated 
        // with the given key
/*        try
        {
            book_list_props.load(new FileInputStream(path_to_resources + book_list_filename));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return book_list_props.getProperty(key); 
*/    
    	return null;
    	}
    
    // returns a list of keys contained in a book profile
    public Enumeration<?> getBookDataList(String encoded_title)
    {
        Properties book_props = new Properties();
        String book_data_filename = encoded_title + ".properties";
        
        try
        {
            book_props.load(new FileInputStream(path_to_resources + book_data_filename));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return book_props.propertyNames();
    }

    public String getListFilename()
    {
        return book_list_filename;
    }
    
    // get book title without underscores for the given key
    public String getUnencodedTitle(String key)
    {
        return Utilities.decodeString(getEncodedTitle(key));
    }
    
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
        Properties props = null;
        try
        {
            props = getProps(profile);
            props.remove(key);
        }
        catch(Exception e)
        {
        }
        
        File propsFile = new File(path_to_resources + profile + ".properties");
        
        if(propsFile != null)
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
        profile = path_to_resources + Utilities.encodeString(profile) + ".properties";
        File propFile = new File(profile);
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
    
    // remove a book from the book list and delete its profile
    public void removeBook(String profile)
    {
        profile = Utilities.encodeString(profile);
        // get the current list of characters
        Properties props = new Properties();
        try
        {
            props.load(new FileInputStream(path_to_resources + "book_list.properties"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        // hand properties off to the generic "remove" method
        props = removeProfile(profile, props);
        
        // write the edited properties to file
        File propsFile = new File(path_to_resources + "book_list.properties");
        try
        {
            OutputStream propOut = new FileOutputStream(propsFile);
            props.store(propOut, "Book list profile");
            propOut.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        // remove the book-profile properties file
        File locationFile = new File(path_to_resources + profile + ".properties");
        locationFile.delete();
    }

    // generic "remove" method to delete a book, character, or location; 
    // method is called by one of:
    //
    //     removeBook()
    //     removeCharacter()
    //     removeLocation()
    //
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
/*        if(dev_code)
        {
//            System.out.println("in BookManager.getProperty(), profile = " + profile + "; key = " + key);
        }
        Properties props = getProps(Utilities.encodeString(profile));
        return props.getProperty(key);
*/
    	return null;	
    }
    
    public String getBookTitle(String id)
    {
    	String title = "";
    	title = bookList.getBookTitle(id);
    	return title;
    }
}
