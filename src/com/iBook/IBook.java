package com.iBook;

import com.iBook.datastore.PropsManager;

import com.iBook.datastore.books.*;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.Utilities;
import com.iBook.datastore.*;

import java.util.*;

// please can we be source controlled now?

public class IBook 
{
    private static boolean dev_code = true;
    private static String path_to_resources = "src\\resources\\";
    private static PropsManager dataStore = new PropsManager();

    public static void main(String[] args)
    {
        parserTest();
        
        System.out.println("Done!");
        System.exit(0);
    }
    private static void parserTest()
    {
        BookList bookList = null;
        BookListParser parser = new BookListParser();
        String xml = path_to_resources + "bookList.xml";
        bookList = parser.parse(xml);
        int bookCount = bookList.getBookCount();
        
        
    }
}
