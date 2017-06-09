package com.iBook.datastore.books;

import java.text.Collator;
import java.util.Locale;
import java.util.Vector;
import java.util.Iterator;
import java.util.Collections;

public class BookList
{
	private Vector<BookProfile> list = new Vector<BookProfile>();
	private Vector<String> idList = new Vector<String>();
    private Collator col = Collator.getInstance(Locale.US);
	
	public BookList()
	{
		/*
		 * The BookList object representation of the data 
		 * contained in the bookList.xml file is built by the 
		 * BookListParser, which reads the existing xml file,
		 * or by user action through .jsp files that pass data 
		 * through the props/book manager objects. The user-edited
		 * object is then converted to xml (which gets written to disk for
		 * use by the BookListParser) in the XmlUpdate class.
		 */
	}
	
	public void addItem(BookProfile item)
	{
		list.add(item);
	}
	
	public boolean addProfile(Object profile)
	{
		boolean ret = true;
		list.add((BookProfile)profile);
		return ret;
	}
	
	/*
	public BookProfile getBookProfile(String id)
	{
		BookProfile profile = null;
		Iterator<BookProfile> profileI = list.iterator();
		while(profileI.hasNext())
		{
			profile = (BookProfile)profileI.next();
			if(profile.getId().compareTo(id) == 0)
				break;
			else
				profile = null;
		}
		return profile;
	}
	*/
	
	public BookProfile getProfile(String id)
	{
		BookProfile profile = null;
		Iterator<BookProfile> profileI = list.iterator();
		while(profileI.hasNext())
		{
			profile = (BookProfile)profileI.next();
			if(profile.getId().compareTo(id) == 0)
				break;
			else
				profile = null;
		}
		return profile;
	}
	
	public boolean deleteProfile(String id)
	{
		boolean ret = false;
		Iterator<BookProfile> booksI = list.iterator();
		BookProfile profile = null;
		int count = 0;
		while(booksI.hasNext())
		{
			profile = (BookProfile)booksI.next();
			if(profile.getId().compareTo(id) == 0)
			{
				list.removeElementAt(count);
				ret = true;
				break;
			}
			count++;
		}
		return ret;
	}
	
	public int getBookCount()
	{
		return list.size();
	}
	public void addId(String id)
	{
		idList.add(id);
	}
	public Vector<String> getIdList()
	{
		// returns a list of book ids, sorted by book title
		Vector<String> sortedList = new Vector<String>();
		Vector<String> returnList = new Vector<String>();
		Iterator<BookProfile> idI = list.iterator();
		String item = "";
		BookProfile book = null;
		String itemSeparator = "###ID###";
		while(idI.hasNext())
		{
			book = (BookProfile)idI.next();
			item = book.getTitle() + itemSeparator + book.getId();
			sortedList.add(item);
		}
        Collections.sort(sortedList, col);
        Iterator sortedI = sortedList.iterator();
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
	public String getBookTitle(String bookId)
	{
		String title = "";
		Iterator<BookProfile> bookI = list.iterator();
		while(bookI.hasNext())
		{
			BookProfile book = (BookProfile)bookI.next();
			String check = book.getId();
			if(book.getId().compareTo(bookId) == 0)
			{
				title = book.getTitle();
				break;
			}
		}
		if(title.length() == 0)
			title = "Untitled";
		return title.trim();
	}
	
	public String getSourceAuthorName(String bookId)
	{
		String sourceAuthorName = "";
		
		BookProfile profile = null;
		Iterator<BookProfile> booksI = list.iterator();
		while(booksI.hasNext())
		{
			profile = booksI.next();
			if(profile.getId().compareTo(bookId) == 0)
			{
				sourceAuthorName = profile.getSourceAuthorName();
				break;
			}
		}
		return sourceAuthorName;
	}
	public String getAuthorName(String bookId)
	{
		String authorName = "";
		
		BookProfile profile = null;
		Iterator<BookProfile> booksI = list.iterator();
		while(booksI.hasNext())
		{
			profile = booksI.next();
			if(profile.getId().compareTo(bookId) == 0)
			{
				authorName = profile.getAuthorName();
				break;
			}
		}
		return authorName.trim();
	}
	
	public String getBookSourceTitle(String bookId)
	{
		String bookSourceTitle = "";
		BookProfile profile = null;
		Iterator<BookProfile> booksI = list.iterator();
		while(booksI.hasNext())
		{
			profile = booksI.next();
			if(profile.getId().compareTo(bookId) == 0)
			{
				bookSourceTitle = profile.getSourceTitle();
				break;
			}
		}
		if(bookSourceTitle.length() == 0)
			bookSourceTitle = "undefined";
		
		return bookSourceTitle.trim();
	}
	
/*	public Vector<String> getCharactersForBook(String bookId)
	{
		Vector<String> charList = null;
		
		BookProfile book = null;
		Iterator<BookProfile> booksI = list.iterator();
		while(booksI.hasNext())
		{
			book = (BookProfile)booksI.next();
			if(book.getId().compareTo(bookId) == 0)
			{
				charList = book.getCharacterCitations();
				break;
			}
		}
		return charList;
	}

	public Vector<String> getLocationsForBook(String bookId)
	{
		Vector<String> locList = null;
		
		BookProfile book = null;
		Iterator<BookProfile> booksI = list.iterator();
		while(booksI.hasNext())
		{
			book = (BookProfile)booksI.next();
			if(book.getId().compareTo(bookId) == 0)
			{
				locList = book.getLocationsUsed();
				break;
			}
		}
		return locList;
	}
	*/
}

