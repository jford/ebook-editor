package com.iBook.datastore.ebook.epub;

import com.iBook.datastore.*;
import com.iBook.datastore.manuscripts.*;
import com.iBook.utilities.*;

import java.util.*;

public class Package
{
	private Manuscript manuscript = null;
	private Manifest manifestObj = null;
	private Metadata metadataObj = null;
	private Spine spineObj = null;

	private String manifestText = "";
	private String metadataText = ""; 
	private String packageText = "";
	private String spineText = "";

	public Package(Manuscript manuscript, String userId)
	{
		this.manuscript = manuscript;
		manifestObj = new Manifest(this.manuscript, userId);
		spineObj = new Spine(this.manifestObj);
		
		metadataObj = new Metadata(manuscript, userId);
		metadataText = metadataObj.getMetadata();
		manifestText = manifestObj.getManifest();
		spineText = spineObj.getSpine();
		
		packageText =
				"   <package xmlns=\"http://www.idpf.org/2007/opf\" " +
	                        "unique-identifier=\"BookID\" " +
	                        "version=\"2.0\">\n";
		packageText += metadataText + manifestText + spineText + "</package>\n";
		
	}
	
	public String getPackageText()
	{
		return packageText;
	}
	
	public String getManifestText()
	{
		return manifestText;
	}
	
	public String getMetadataText()
	{
		return metadataText;
	}
	
	public String getSpineText()
	{
		return spineText;
	}
	
	public Vector<String> getSpineItems()
	{
		return manifestObj.getSpineItems();
	}
	
	public String getUid()
	{
		return metadataObj.getUid();
	}

}
