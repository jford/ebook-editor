<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.utilities.*,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>

	<body>
	<%
	String pubType = request.getParameter("pubType");
	
	String userId = request.getRemoteUser();
 	PropsManager propsMgr = new PropsManager(userId);
			
	String manuscriptId = request.getParameter("manuscriptId");
	Manuscript manuscript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId);
	
	Vector<Integer> filebreaks = manuscript.getFilebreaks();
	Vector<String> textblocks = manuscript.getTextblocks();
	
	Iterator<Integer> filebreaksI = filebreaks.iterator();
	Iterator<String> textblocksI = null;
	
	int numFilebreaks = filebreaks.size();
	
	String context = "";
	String strNumFilebreaks = new Integer(numFilebreaks).toString();
	String textblockId = "";
	String textblockNum = "";
	
	int idx = 0;
	
	if(numFilebreaks == 0)
	{
	%>
		<p>
		No textblocks have been identified as break points for new files within the <i>ePub</i> file system. 
		</p>
		<p>
		The entire manuscript will be contained in a single file.
		</p>


	<%
	} // end of if(numFilebreaks < 0)
	%>
	
	<form name="editFilebreaksForm" action="../BuildEbook" method="post">
		<span class="inline_text_note">(List one or more text block numbers to add/delete filebreaks; 
		if multiple, separate entries by spaces)</span><br/><br/>
		<textarea name="filebreaksList" id="filebreaksList" rows="4" cols="30"></textarea><br/><br/>
		<input type="submit" name="addFilebreaksSubmit" value="Add" onclick="return validateFilebreakInput(<%= Integer.toString(textblocks.size()) %>)" />
		<input type="submit" name="deleteFilebreaksSubmit" value= "Delete" onclick="return validateFilebreakInput(<%= Integer.toString(textblocks.size()) %>)" />
		<input type="hidden" name="manuscriptId" value="<%= manuscriptId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		<input type="hidden" name="pubType" id="pubType" value="<%= pubType %>" />
	</form>
	
	<form name="search" >
		<textarea name="searchText" id="searchText" cols="30" rows="1"></textarea> 
		<input type="button" name="searchButton" value="Search" onclick="displaySearchResults('<%= Utilities.encodeHtmlTags(manuscriptId) %>');" /> <br/>
	</form>
	
	<%
	if(numFilebreaks > 0)
	{
	%>
		<p>
		The following textblocks have been identified as the 
		start of a new file within the <i>ePub's</i> internal file system:
		</p>
		<table>
			<tr>
				<th>Textblock</th><th>Context</th>
			</tr>
		
		<%
		String contextTail = "";
		while(filebreaksI.hasNext())
		{
			textblockId = filebreaksI.next().toString();
			textblockNum = textblockId.substring(textblockId.lastIndexOf("_") + 1);
	
			// show first 10 words of the textblock
			context = manuscript.getTextblock(textblockId);
			for(int count = 0; count < 10; count++)
			{
				// except if there are less than 10 words...
				if((idx = context.indexOf(" ", idx + 1)) == -1)
				{
					// ...show the entire block 
					idx = context.length();
					contextTail = "";
					break;
				}
				else
					contextTail = "...";
			}
			context = context.substring(0, idx) + contextTail;
			%>
			
			<tr>
				<td valign="top"><%= textblockNum %></td><td valign="top"><%= context %></td>
			</tr>
			
			<%
		}
		%>
		</table>
	<%
	} // end of if numfilebreaks > 0)
	%>
	
	</body>
</html>