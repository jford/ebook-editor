package com.iBook.datastore;

import java.util.Date;

import com.iBook.utilities.Utilities;

public class MetaData
{
	private String nextBookNum = "";
	private String nextCharNum = "";
	private String nextGeolocNum = "";
	private String nextLocNum = "";
	private String nextManuscriptNum = "";
	private String nextTemplateNum = "";

	public MetaData()
	{
	}
	
	public String getId(PropsManager.ObjType type, String name)
	{
		boolean useTimeAsId = true;
		long time = new Date().getTime();

		// remove illegal characters from name
		name = Utilities.normalizeName(name);
		String id = "";
		int nextNum = 0;
		switch(type)
		{
		case BOOK:
			if(!useTimeAsId)
			{
				nextNum = Integer.parseInt(nextBookNum);
				// add one for next time around
				nextBookNum = Integer.toString(nextNum + 1);
				id = name + "_iBook_" + nextBookNum;
			}
			else
				id = name + "_iBook_" + time;
			break;
		case CHARACTER:
			if(!useTimeAsId)
			{
				nextNum = Integer.parseInt(nextCharNum);
				// add one for next time around
				nextCharNum = Integer.toString(nextNum + 1);
				id = name + "_iBookChar_" + nextCharNum;
			}
			else
				id = name + "_iBookChar_" + time;
			break;
		case GEOLOCALE:
			if(!useTimeAsId)
			{
				nextNum = Integer.parseInt(nextGeolocNum);
				// add one for next time around
				nextGeolocNum = Integer.toString(nextNum + 1);
				id = name + "_iBookGeoloc_" + nextGeolocNum;
			}
			else
				id = name + "_iBookGeoloc_" + time;
			break;
		case LOCATION:
			if(!useTimeAsId)
			{
				nextNum = Integer.parseInt(nextLocNum);
				// add one for next time around
				nextLocNum = Integer.toString(nextNum + 1);
				id = name + "_iBookLoc_" + nextLocNum;
			}
			else
				id = name + "_iBookLoc_" + time;
			break;
		case MANUSCRIPT:
			if(!useTimeAsId)
			{
				nextNum = Integer.parseInt(nextManuscriptNum);
				// add one for next time around
				nextManuscriptNum = Integer.toString(nextNum + 1);
				id = name + "_iBookManuscript_" + nextManuscriptNum;
			}
			else
				id = name + "_iBookManuscript_" + time;
			break;
		case TEMPLATE:
			if(!useTimeAsId)
			{
				nextNum = Integer.parseInt(nextTemplateNum);
				// add one for next time around
				nextTemplateNum = Integer.toString(nextNum + 1);
				id = name + "_iBookTemplate_" + nextTemplateNum;
			}
			else
				id = name + "_iBookTemplate_" + time;
			break;
		}
		return id;
	}
	
	public boolean setNextBookNum(String num)
	{ 
		// all setNext???Num calls should only come from the MetaDataParser. All 
		// other meta data id interaction should go through getId()
		boolean ret = true;
		nextBookNum = num;
		return ret;
	}

	public boolean setNextCharNum(String num)
	{ 
		// all setNext???Num calls should only come from the MetaDataParser. All 
		// other meta data id interaction should go through getId()
		boolean ret = true;
		nextCharNum = num;
		return ret;
	}

	public boolean setNextGeolocNum(String num)
	{ 
		// all setNext???Num calls should only come from the MetaDataParser. All 
		// other meta data id interaction should go through getId()
		boolean ret = true;
		nextGeolocNum = num;
		return ret;
	}

	public boolean setNextLocNum(String num)
	{ 
		boolean ret = true;
		nextLocNum = num;
		return ret;
	}

	public boolean setNextManuscriptNum(String num)
	{ 
		// all setNext???Num calls should only come from the MetaDataParser. All 
		// other meta data id interaction should go through getId()
		boolean ret = true;
		nextManuscriptNum = num;
		return ret;
	}

	public boolean setNextTemplateNum(String num)
	{ 
		// all setNext???Num calls should only come from the MetaDataParser. All 
		// other meta data id interaction should go through getId()
		boolean ret = true;
		nextTemplateNum = num;
		return ret;
	}
	public String getNextBookNum()
	{
		return nextBookNum;
	}
	public String getNextCharNum()
	{
		return nextCharNum;
	}
	public String getNextGeolocNum()
	{
		return nextGeolocNum;
	}
	public String getNextLocNum()
	{
		return nextLocNum;
	}
	public String getNextManuscriptNum()
	{
		return nextManuscriptNum;
	}
	public String getNextTemplateNum()
	{
		return nextTemplateNum;
	}
}
