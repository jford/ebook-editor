package com.iBook.datastore.manuscripts;

import com.iBook.utilities.Utilities;

import java.util.*;
import java.text.Collator;

public class Manuscript
{
	boolean stopForDebugging = false;
	
	private String bookId = "";
	private String filename = "";
    private String iBookAuthor = "";
    private String iBookTitle = "";
    private String lastEditedTextblock = "";
	private String manuscriptId = "";
	private String manuscriptName = "";
    private String sourceTitle = "";
    private String sourceAuthor = "";
    private String sourcePubDate = "";
    
    private Vector<Integer> filebreaks = new Vector<Integer>();
//    private Vector<String> tocEntries = new Vector<String>();
    private Vector<String> textblocks = new Vector<String>();
    private Vector<String[]> descriptors = new Vector<String[]>();
    
    public Manuscript()
    {
    	filebreaks.add(new Integer(1));
    }
    
    public Vector<Integer> getFilebreaks()
    {
    	Collator col = Collator.getInstance();
    	Collections.sort(filebreaks);
    	return filebreaks;
    }
    
    public void setBookId(String bookId)
    {
    	this.bookId = bookId;
    }
    
    public String getBookId()
    {
    	return bookId;
    }
    
    public void setSourcePubDate(String pubDate)
    {
    	sourcePubDate = pubDate;
    }
    
    public String getSourcePubDate()
    {
    	return sourcePubDate;
    }
    
    public void addFilebreak(String textblockId)
    {
    	boolean matchfound = false;

    	Integer textblockInteger = new Integer(textblockId);
    	
    	// check list for dupe
    	Iterator<Integer> filebreaksI = filebreaks.iterator();
    	while(filebreaksI.hasNext())
    	{
    		if(textblockInteger.compareTo(filebreaksI.next()) == 0)
    		{
    			matchfound = true;
    			break;
    		}
    	}
    	// if there was a match, do nothing and exit
    	if(!matchfound)
    		filebreaks.add(textblockInteger);
    }
    
    public void deleteFilebreak(String textblockNum)
    {
    	boolean matchfound = false;
    	
    	Integer textblockInteger = new Integer(textblockNum);
    	
    	// filebreaks is a list textblockNums
    	Iterator<Integer> filebreaksI = filebreaks.iterator();
    	// idx is an index into the filebreaks vector
    	int idx = 0;
    	while(filebreaksI.hasNext())
    	{
    		if(textblockInteger.compareTo(filebreaksI.next()) == 0)
    		{
    			matchfound = true;
    			break;
    		}
    		idx++;
    	}
    	if(matchfound)
    		filebreaks.remove(idx);
    }
    
	public boolean updateTextblock(String textblockId, String newTextblock)
	{
		boolean ret = true;
		
		// idx is index into vector, which is 0 based; IDs start numbering at 1
		int idx = Integer.parseInt(textblockId.substring(textblockId.lastIndexOf("_") + 1)) - 1;
		
		textblocks.set(idx, newTextblock);
		lastEditedTextblock = textblockId;
		return ret;
	}
	
	public boolean updateTextblock(int textblockIdx, String newTextblock)
	{
		boolean ret = true;
		
		int idx = 0;
		
		// idx is index into vector
		textblocks.set(textblockIdx, newTextblock);
		
		String textblockId = getMsName() + "_textblock_" + new Integer(textblockIdx + 1).toString();
		lastEditedTextblock = textblockId;

		return ret;
	}
	
    public int getNumTextblocks()
    {
    	return textblocks.size();
    }
    
	public String getLastEditedTextblock()
	{
		// returns the ID of the last edited textblock
		
		if(lastEditedTextblock.compareTo("") == 0)
		{
			// if no textblocks have been edited, lastEditedTextblock should default to 0;
			// textblock IDs are constructed in the XmlFactory according to this...
			//
			//      String idBase = profile.getFilename();
			//      int idx = idBase.indexOf(".xml");
			//      idBase = idBase.substring(0, idx) + "_manuscript_";
			//      id = idBase + "textblock_";
			String idBase = getFilename();
			int idx = idBase.indexOf(".xml");
			lastEditedTextblock = idBase.substring(0, idx) + "_textblock_0";
		}
		return lastEditedTextblock;
	}

	public boolean setLastEditedTextblock(String textblockId)
	{
		// sets the lastEdited... attribute in the manuscript .xml
		boolean ret = true;
		lastEditedTextblock = textblockId;
		return ret;
	}
	
    
    public void setId(String manuscriptId)
    {
    	this.manuscriptId = manuscriptId;
    }
    
    public String getId()
    {
    	return manuscriptId;
    }

    public void setFilename(String filename)
    {
    	this.filename = filename;
    }
    
    public String getFilename()
    {
    	return filename;
    }
    
    public String getMsName()
    {
    	return manuscriptName;
    }
    
    public void setManuscriptName(String manuscriptName)
    {
    	this.manuscriptName = manuscriptName;
    	filename = Utilities.replaceChars(manuscriptName, " ", "_", "all");
    	filename = Utilities.replaceChars(filename, ".", "_", "all");
    	filename += ".xml";
    }
    
    public Vector<String[]> getDescriptors()
    {
    	return descriptors;
    }
    
    public void clearDescriptors()
    {
    	descriptors.clear();
    }
    
    public void clearTextblocks()
    {
    	textblocks.clear();
    }
    
    public void addDescriptor(String[] descriptor)
    {
    	boolean alreadyExists = false;
    	/*
    	 * BuildEpub servlet calls addDescriptor() with individual string arguments;
    	 * ManuscriptParser calls addDescriptor() with a String[].
    	 * 
    	 * See addDescriptor(String, String, String, String) for breakdown of 
    	 * array contents.
    	 */

    	// first, check to see if the incoming descriptor is already in the list 
    	Iterator<String[]> descriptorsI = descriptors.iterator();
    	String[] existingDescriptor = null;
    	while(descriptorsI.hasNext())
    	{
    		existingDescriptor = descriptorsI.next();
    		if(existingDescriptor[0].compareTo(descriptor[0]) == 0 && 
    		   existingDescriptor[1].compareTo(descriptor[1]) == 0 &&
    		   existingDescriptor[2].compareTo(descriptor[2]) == 0)
           	 	// don't add the incoming descriptor if the Vector already contains a descriptor 
           	 	// identical to it except for new text
    			alreadyExists = true;
    	}
    	if(!alreadyExists)
    		descriptors.add(descriptor);
    }
    
    public void addDescriptor(String tCharId, String textblocksIdx, String sourceText, String newText)
    {
    	/*
    	 *  a descriptor is an array that matches descriptive text in the template
    	 *  with user-defined replacement text, for a specified tChar:
    	 *  
    	 *  BuildEpub servlet calls addDescriptor() with individual string arguments;
    	 *  ManuscriptParser calls addDescriptor() with a String[]
    	 *  
    	 *  descriptor = { tCharId, textblockId, "text contained in source", "substitute text" }' 
    	 *  
    	 *  Descriptors are added to the array by the BuildEpub servlet. The template has 
    	 *  flagged certain phrases/words that need user attention; the BuildEpub servelet 
    	 *  has entered each of these flagged phrases to the descriptors vector.  
    	 */
    	String[] descriptor = {tCharId, textblocksIdx, sourceText, newText};
    	
    	// first, check to see if the incoming descriptor is already in the list 
    	boolean alreadyExists = false;
    	Iterator<String[]> descriptorsI = descriptors.iterator();
    	String[] existingDescriptor = null;
    	while(descriptorsI.hasNext())
    	{
    		existingDescriptor = descriptorsI.next();
    		if(existingDescriptor[0].compareTo(descriptor[0]) == 0 && 
    		   existingDescriptor[1].compareTo(descriptor[1]) == 0 &&
    		   existingDescriptor[2].compareTo(descriptor[2]) == 0)
           	 	// don't add the incoming descriptor if the Vector already contains a descriptor 
           	 	// identical to it except for new text
    			alreadyExists = true;
    	}
    	if(!alreadyExists)
    		descriptors.add(descriptor);
    }
    
    public void updateDescriptor(String[] newDescriptor)
    {
    	/*
    	 * If the textblockNum and source descriptor text match in both 
    	 * incoming and existing descriptors, replace the existing descriptor 
    	 * with the incoming array.
    	 */
    	Iterator<String[]> descriptorsI = descriptors.iterator();
    	String[] oldDescriptor = null;
    	int idx = 0;
    	while(descriptorsI.hasNext())
    	{
    		oldDescriptor = descriptorsI.next();
    		if(oldDescriptor[1].compareTo(newDescriptor[1]) == 0 &&
    		   oldDescriptor[2].compareTo(newDescriptor[2]) == 0)
    		{
    			break;
    		}
    		idx++;
    	}
    	descriptors.remove(idx);
    	descriptors.add(newDescriptor);
    }
    
    public boolean replaceDescriptorList(Vector<String[]> descriptors)
    {
    	boolean ret = true;
    	this.descriptors = descriptors;
    	return ret;
    }
    
    public int getNumDescriptors()
    {
    	return descriptors.size();
    }
    
    public int getNumDescriptorsUnresolved()
    {
    	int num = 0;

    	// count number of descriptors in which [3] (substitute text) is empty
    	Iterator<String[]> descriptorsI = descriptors.iterator();
    	while(descriptorsI.hasNext())
    	{
    		if(descriptorsI.next()[3].length() == 0)
    			num ++;
    	}
    	return num;
    }
    
    public void setSourceTitle(String sourceTitle)
    {
    	this.sourceTitle = sourceTitle;
    }
    
    public String getSourceTitle()
    {
    	return sourceTitle;
    }
    
    public void setSourceAuthor(String sourceAuthor)
    {
        this.sourceAuthor = sourceAuthor.trim();
    }
    
    public String getSourceAuthor()
    {
        return sourceAuthor;
    }

    public void setIBookAuthor(String iBookAuthor)
    {
        this.iBookAuthor = iBookAuthor.trim();
    }
    
    public String getIBookAuthor()
    {
        return iBookAuthor;
    }

	public void setIBookTitle(String iBookTitle)
	{
		this.iBookTitle = iBookTitle;
	}
	
	public String getIBookTitle()
	{
		return iBookTitle;
	}
	
	public Vector<String> getTextblocks()
	{
		return textblocks;
	}
	
	public void setTextblock(String textblockId, String text)
	{
		// changes text of a specified textblock; to add a new text block use addTextblock()
		// set idx to the number portion of the textblockId (index of last underscore + 1) then 
		// subtract 1 to adjust for vector number (textblockId 1 is at vector index 0)
		int idx = new Integer(textblockId.substring(textblockId.lastIndexOf("_") + 1)) - 1;

		textblocks.setElementAt(text, idx);
	}
	
	public void addTextblock(String text)
	{
		// adds a new textblock to the vector (to edit a specific textblock, use setTextblock()
		textblocks.add(text);
	}
	
	public String getTextblock(String textblockId)
	{
		int idx = new Integer(textblockId.substring(textblockId.lastIndexOf("_") + 1));
		// textblockIds start at 1; vector index is 0-based, so the first textblock,
		// textblockId_1, will be at vector index 0, which is idx - 1
		return textblocks.elementAt(idx - 1);
	}
	
	public String getTextblock(Integer textblockIdx)
	{
		// textblockIdx from filebreaks vector is based on textblock ID, which starts 
		// at 1; need to adjust for vector index
		return textblocks.elementAt(textblockIdx.intValue() - 1);
	}
}
