package com.iBook.utilities;

import java.io.*;
import java.util.Properties;

import com.iBook.datastore.*;

public class Utilities
{
   private static PropsManager propsMgr = new PropsManager();
   
   public static String read_file(String filename)
   throws FileNotFoundException, IOException
   {
      File fin = new File(filename);
      FileInputStream fread = new FileInputStream(fin);
      int len = (int)fin.length();
      byte incoming[] = new byte[len];
      String content = "";

      try
      {
         if(fread != null)
         {
            fread.read(incoming, 0, len);
            content = new String(incoming);
         }
      }
      catch(FileNotFoundException e)
      {
         System.out.println("Unable to open file " + filename);
         e.printStackTrace();
      }
      catch(IOException e)
      {
         System.out.println("Unable to read " + filename);
         e.printStackTrace();
      }
      finally
      {
         if(fread != null)
            fread.close();
      }
      return content;
   }
   
   public static String convertNumberToRomanNumeral(int number)
   {
	   String romanNumeral = "";
	   int multiplier = 0;
	   
	   String[] zeroToTen = {"","i","ii","iii","iv","v","vi","vii","viii","ix","x"};
	   if(number >= 10)
		   multiplier = 1;
	   if(number >= 20)
		   multiplier = 2;
	   if(number >= 30)
		   multiplier = 3;
	   if(number >= 40)
		   multiplier = 4;
	   if(number >= 50)
		   multiplier =5;
	   int remainder = number % 10;
	   
	   for(int count = 0; count < multiplier; count++)
	   {
		   romanNumeral += zeroToTen[10];
	   }
	   
	   if(remainder > 0)
		   romanNumeral += zeroToTen[remainder];
	   
	   return romanNumeral;
   }

   public static void write_file(String filename, String content, boolean verbose)
   {
      FileWriter fwrite;
      try
      {
    	 fwrite = new FileWriter(filename);
         fwrite.write(content, 0, content.length());
         fwrite.flush();
         fwrite.close();
         
         propsMgr.logMsg(PropsManager.LogLevel.INFO, "new " + filename + " written");
      }
      catch(IOException e)
      {
    	  propsMgr.logError("Error writing " + filename + " file\n", e);
      }
   }
  // convenience method, allows char replace in Strings w/o using regex,  
  // which String class uses (this function uses StringBuffer instead);
  // regex replace doesn't appear able to handle escape char as a char
  public static String replaceChars(String base, 
                                    String target, 
                                    String substitute, 
                                    String range)
  {
     StringBuffer base_buf = new StringBuffer(base);
     int idx = 0;
     int end = 0;

     if(range.compareTo("all") == 0)
     {
        while((idx = base_buf.indexOf(target, end)) != -1)
        {
           base_buf.replace(idx, idx + target.length(), substitute);
           // start next search after the end of the new text, in case substitute contains an instance of target (i.e., replace & with &amp;)
           end = idx + substitute.length();
        }
     }
     else if (range.compareTo("first") == 0)
     {
        if((idx = base.toString().indexOf(target)) != -1)
       base_buf.replace(idx, idx + target.length(), substitute);
     }
     else
        System.out.println("invalid range in Utilities.replaceChars: " + range);

     return base_buf.toString();
  }
  
  public static String encodeHtmlParam(String string)
  {
	  return replaceChars(string, " ", "%20", "all");
  }

  // change spaces to underscores 
  public static String encodeString(String string)
  {
      return replaceChars(string, " ", "_", "all");
  }
  
  // change underscores to spaces
  public static String decodeString(String string)
  {
      string = replaceChars(string, "_", " ", "all");
      return string;
  }
  
  // change <, >, and " to HTML codes
  public static String encodeHtmlTags(String string)
  {
	  string = replaceChars(string, "<", "&lt;", "all");
	  string = replaceChars(string, ">", "&gt;", "all");
	  string = replaceChars(string, "\"", "&quot;", "all");
	  return string;
  }
  
  // change html codes for <, >, and " back to characters
  public static String decodeHtmlTags(String string)
  {
	  string = replaceChars(string, "&lt;", "<", "all");
	  string = replaceChars(string, "&gt;", ">", "all");
	  string = replaceChars(string, "&quot;", "\"", "all");
	  return string;
  }
  
  public static Properties getProps(String props_filename)
  {
      Properties props = new Properties();
      try
      {
          props.load(new FileInputStream(props_filename));
      }
      catch(Exception e)
      {
          e.printStackTrace();
      }
      return props;
  }
  
  public static boolean startsWithVowel(String text)
  {
	  boolean ret = false;
	  String[] vowels = {"a", "e", "i", "o", "u"};
	  for(int count = 0; count < vowels.length; count++)
	  {
		  if(text.substring(0,1).compareTo(vowels[count]) == 0)
		  {
			  ret = true;
			  break;
		  }
	  }
	  return ret;
  }

	public static String normalizeName(String name)
	{
		String[] illegalChars = {
				                  "*", // asterisk
				                  "&", // ampersand
				                  "'", // apostrophe
				                  "@", // at
				                  "\\", // back slash
				                  "^", // carat
				                  ",", // comma
				                  "/", // forward slash
				                  ">", // greater than (>)
				                  "<", // less than (<)
				                  ".", // period
				                  "?", // question mark
				                  " ", // space
				                };
		
		for(int count = 0; count < illegalChars.length; count++)
		{
			// replace all instances of illegalChars with underscore
			name = Utilities.replaceChars(name, illegalChars[count], "_", "all");
		}
		return name;
	}
	public static String sanitizeString(String string)
	{
		// return string with the html tags contained in the tags array removed
		String[] tags = {
			"<i>",
			"</i>",
			
			"<b>",
			"</b>",
			
			"<center>",
			"</center>",
			
			"<h1>",
			"</h1>",
			
			"<h2>",
			"</h2>",
			
			"<h3>",
			"</h3>",
			
			"<h4>",
			"</h4>",
			
			"<ul>",
			"</ul>",
			
			"<li>",
			"</li>"
		};
		// remove html tags for PDF output
 		String sanitizedString = string;
		for(int count = 0; count < tags.length; count++)
		{
			if(string.indexOf(tags[count]) != -1)
			{
				sanitizedString = sanitizedString.replaceAll(tags[count], "");
			}
		}
		// return the string minus html tags
		return sanitizedString;
	}

  
}

