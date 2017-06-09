package com.iBook.datastore.books;

import com.iBook.datastore.*;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.*;

import java.util.*;

public class BookProfile
{
	private String authorNamePrefix = "";
	private String authorNameFirst = "";
	private String authorNameMiddle = "";
	private String authorNameLast = "";
	private String authorNameSuffix = "";
	private String id = "";
    private String manuscriptFilename = "";
    private String manuscriptId = "";
	private String sourceTitle = "";
	private String sourceAuthorNamePrefix = "";
	private String sourceAuthorNameFirst = "";
	private String sourceAuthorNameMiddle = "";
	private String sourceAuthorNameLast = "";
	private String sourceAuthorNameSuffix = "";
	private String template = "";
    private String title = "";
	
	private ObjSubstitutionsManager objSubMgr = null;
	
	private Vector<String[]> unmappedAliases = null;
	private Vector<String[]> unmappedAttrs = null;

	public BookProfile()
	{
	}
	
	public String getAliasSubText(String tCharId, String alias)
	{
		String text = "";
		Vector<String[]> aliasSubMatrix = getAliasSubMatrix();
		String[] aliasSubEntry = null;
		Iterator<String[]> aliasSubsI = aliasSubMatrix.iterator();
		while(aliasSubsI.hasNext())
		{
			aliasSubEntry = aliasSubsI.next();
			if(aliasSubEntry[0].compareTo(tCharId) == 0 && aliasSubEntry[1].compareTo(alias) == 0)
			{
				text = aliasSubEntry[2];
				break;
			}
		}
		return text;
	}
	

	public String getAttributeSubText(String tObjId, String attribute)
	{
		String text = "";
		Vector<String[]> attributeSubMatrix = getAttributeSubMatrix();
		String[] attributeSubEntry = null;
		Iterator<String[]> attributeSubsI = attributeSubMatrix.iterator();
		while(attributeSubsI.hasNext())
		{
			attributeSubEntry = attributeSubsI.next();
			if(attributeSubEntry[0].compareTo(tObjId) == 0 && attributeSubEntry[1].compareTo(attribute) == 0)
			{
				text = attributeSubEntry[2];
				break;
			}
		}
		return text;
	}
	
	public Vector<String[]> getUnmappedAliases(TemplateProfile templateProfile)
	{
		return objSubMgr.getUnmappedAliases(templateProfile);
	}
	public Vector<String[]> getUnmappedAttributes(TemplateProfile templateProfile)
	{
		return objSubMgr.getUnmappedAttributes(templateProfile);
	}
    

	public int getNumSubs(PropsManager.ObjType type)
	{
		int num = 0;
		switch(type)
		{
		case CHARACTER:
			num = objSubMgr.getNumCharSubs();
			break;
		case LOCATION:
			num = objSubMgr.getNumLocSubs();
			break;
		case GEOLOCALE:
			num = objSubMgr.getNumGeolocSubs();
			break;
		case BOOK:
		case MANUSCRIPT:
		case TEMPLATE:
			break;
		}
		return num;
	}
	

	public void setObjSubMgr(ObjSubstitutionsManager objSubMgr)
	{
		// The book profile's ObjSubstitutionManager is set by the BookListParser in 
		// processBookList(), after the book Profile has been created and populated
		this.objSubMgr = objSubMgr;
	}
	
	public String getCharStandinId(String tCharId)
	{
		String charStandinId = "";
		Vector<String[]> charSubMatrix = objSubMgr.getMatrix(PropsManager.ObjType.CHARACTER);
		Iterator<String[]> charsI = charSubMatrix.iterator();
		String[] charSubMatrixEntry = null;
		while(charsI.hasNext())
		{
			charSubMatrixEntry = charsI.next();
			if(charSubMatrixEntry[0].compareTo(tCharId) == 0)
			{
				charStandinId = charSubMatrixEntry[1];
				break;
			}
		}
		return charStandinId;
	}

	public String getStandinId(String tObjId)
	{
		String standinId = "";
		Vector<String[]> subMatrix = null;
		if(tObjId.indexOf("_char_") != -1)
			subMatrix = objSubMgr.getMatrix(PropsManager.ObjType.CHARACTER);
		else if(tObjId.indexOf("_geoloc_") != -1)
			subMatrix = objSubMgr.getMatrix(PropsManager.ObjType.GEOLOCALE);
		else if(tObjId.indexOf("_loc_") != -1)
			subMatrix = objSubMgr.getMatrix(PropsManager.ObjType.LOCATION);
		Iterator<String[]> objsI = subMatrix.iterator();
		String[] subMatrixEntry = null;
		while(objsI.hasNext())
		{
			subMatrixEntry = objsI.next();
			if(subMatrixEntry[0].compareTo(tObjId) == 0)
			{
				standinId = subMatrixEntry[1];
				break;
			}
		}
		return standinId;
	}

	public String getCurrentSub(PropsManager.ObjType type, String tObjId)
	{
		return objSubMgr.getCurrentSub(type,  tObjId);
	}
	
	public boolean updateSubPair(PropsManager.ObjType type, String tObjId, String iBookObjId, Vector<?> tObjList)
	{
		boolean ret = false;
		if(objSubMgr != null)
		{
			objSubMgr.updateSub(type, tObjId, iBookObjId, tObjList);
			ret = true;
		}
		return ret;
	}
	
	public boolean addTAliasSub(String tObjId, String tObjAlias, String substituteText)
	{
		// tObjId is the template object; tObjAlias is an alternate name used in 
		// the original book as an alias for the
		// object. substituteText is the user defined alias to use instead.  This mapping
		// of alias texts is done by the user in the Book Editor.
		
		boolean ret = true;
		objSubMgr.addAliasSubstitute(tObjId, tObjAlias, substituteText);
		return ret;
	} 
	
	public boolean clearTAliasSubs(String tObjId)
	{
		boolean ret = true;
		objSubMgr.clearAliasSubstitutes(tObjId);
		return ret;
	}
	
	public boolean clearTAttributeSubs(String tObjId)
	{
		boolean ret = true;
		objSubMgr.clearAttributeSubstitutes(tObjId);
		return ret;
	}
	
	public boolean addTAttributeSub(String tObjId, String tObjAttribute, String substituteText)
	{
		// tObjId is the template object; tObjAttribute is a regionalism name used in 
		// the original book. substituteText is the user defined value to use instead.  This mapping
		// of texts is done by the user in the Book Editor.
		
		boolean ret = true;
		objSubMgr.addAttributeSubstitute(tObjId, tObjAttribute, substituteText);
		return ret;
	} 
	
	public String getTcharAliasSub(String tCharId, String tCharAlias)
	{
		String newAliasText = "";
		newAliasText = objSubMgr.getAlias(tCharId, tCharAlias);
		return newAliasText;
	}
	
	public Vector<String[]> getAliasSubMatrix()
	{
		return objSubMgr.getAliasSubMatrix();
	}
	
	public Vector<String[]> getAttributeSubMatrix()
	{
		return objSubMgr.getAttributeSubMatrix();
	}
	
	public Vector<String[]> getCharSubMatrix()
	{
		return objSubMgr.getMatrix(PropsManager.ObjType.CHARACTER);
	}
	
	public Vector<String[]> getLocSubMatrix()
	{
		return objSubMgr.getMatrix(PropsManager.ObjType.LOCATION);
	}

	public Vector<String[]> getGeolocSubMatrix()
	{
		return objSubMgr.getMatrix(PropsManager.ObjType.GEOLOCALE);
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setTemplate(String template)
	{
		this.template = template;
	}
	
	public void setTitle(String title)
	{
		this.title = title.trim();
	}

	public void setNamePrefix(String namePrefix)
	{
		this.authorNamePrefix = namePrefix.trim();
	}

	public void setNameFirst(String nameFirst)
	{
		this.authorNameFirst = nameFirst.trim();
	}

	public void setNameMiddle(String nameMiddle)
	{
		this.authorNameMiddle = nameMiddle.trim();
	}

	public void setNameLast(String nameLast)
	{
		this.authorNameLast = nameLast.trim();
	}

	public void setNameSuffix(String nameSuffix)
	{
		this.authorNameSuffix = nameSuffix.trim();
	}

	public void setManuscriptId(String manuscriptId)
	{
		this.manuscriptId = manuscriptId;
	}
	
	public String getManuscriptId()
	{
		return manuscriptId;
	}

	public void setSourceTitle(String sourceTitle)
	{
		this.sourceTitle = sourceTitle;
	}

	public void setSourceAuthorNamePrefix(String sourceAuthorNamePrefix)
	{
		this.sourceAuthorNamePrefix = sourceAuthorNamePrefix.trim();
	}

	public void setSourceAuthorNameFirst(String sourceAuthorNameFirst)
	{
		this.sourceAuthorNameFirst = sourceAuthorNameFirst.trim();
	}

	public void setSourceAuthorNameMiddle(String sourceAuthorNameMiddle)
	{
		this.sourceAuthorNameMiddle = sourceAuthorNameMiddle.trim();
	}

	public void setSourceAuthorNameLast(String sourceAuthorNameLast)
	{
		this.sourceAuthorNameLast = sourceAuthorNameLast.trim();
	}

	public void setSourceAuthorNameSuffix(String sourceAuthorNameSuffix)
	{
		this.sourceAuthorNameSuffix = sourceAuthorNameSuffix.trim();
	}

	public String getId()
	{
		return id;
	}

	public String getTemplate()
	{
		// returns filename of template
		return template;
	}
	
	public String getTemplateId()
	{
		String templateId = "";
		String path_to_user_dir = PropsManager.getPathToUserDir();
		String pathMrkr = PropsManager.getPathMarker();
		String templateText = "";
		try
		{
			templateText = Utilities.read_file(path_to_user_dir + "templates" + pathMrkr + template);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		String idMrkr = "id=\"";
		int idx = templateText.indexOf(idMrkr);
		templateId = templateText.substring(idx + idMrkr.length(), templateText.indexOf("\"", idx + idMrkr.length()));
		return templateId; 
	}

	public String getSourceTitle()
	{
		return sourceTitle;
	}

	public String getSourceAuthorName()
	{
		String sourceAuthorName = sourceAuthorNamePrefix + " " + 
				                  sourceAuthorNameFirst + " " + 
				                  sourceAuthorNameMiddle + " " + 
				                  sourceAuthorNameLast + " " + 
				                  sourceAuthorNameSuffix;
		sourceAuthorName = Utilities.replaceChars(sourceAuthorName, "  ", " ", "all");
		if(sourceAuthorName.compareTo("    ") == 0)
			sourceAuthorName = "undefined";
		return sourceAuthorName.trim();
	}

	public String getNamePrefix()
	{
		return authorNamePrefix;
	}

	public String getNameFirst()
	{
		return authorNameFirst;
	}

	public String getNameMiddle()
	{
		return authorNameMiddle;
	}

	public String getNameLast()
	{
		return authorNameLast;
	}

	public String getNameSuffix()
	{
		return authorNameSuffix;
	}
	public String getTitle()
	{
		return title;
	}

	public String getAuthorName()
	{
		String name = this.authorNamePrefix + " " + 
	                  this.authorNameFirst + " " +
	                  this.authorNameMiddle + " " +
	                  this.authorNameLast + " " + 
	                  this.authorNameSuffix;
		if(name.compareTo("    ") == 0)
			name = "anonymous";
		name = Utilities.replaceChars(name, "  ", " ", "all");
		return name.trim();
	}

	public String getSourceAuthorNamePrefix()
	{
		return sourceAuthorNamePrefix;
	}

	public String getSourceAuthorNameFirst()
	{
		return sourceAuthorNameFirst;
	}

	public String getSourceAuthorNameMiddle()
	{
		return sourceAuthorNameMiddle;
	}

	public String getSourceAuthorNameLast()
	{
		return sourceAuthorNameLast;
	}

	public String getSourceAuthorNameSuffix()
	{
		return sourceAuthorNameSuffix;
	}
}
