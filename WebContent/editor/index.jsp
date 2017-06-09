<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
      
   <html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,com.iBook.datastore.*,com.iBook.utilities.*" %>
		<%
		String count = "";
		String suffix = "s";
		String objType = "";
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		
		// make sure user has all system-defined templates, in case any 
		// new ones have been added (but don't revert any 
		// changes user may have made to templates already in the user's 
		// template dir; user has to explicitly request that, 
		// in template editor) 
		propsMgr.updateUserTemplateDir(false);

		Vector<String> ebookList = propsMgr.getEbookList();
		int numBooks = propsMgr.getBookCount();
		int numCharacters = propsMgr.getCharacterCount();
		int numLocations = propsMgr.getLocationsCount();
		int numRegions = propsMgr.getGeolocsCount();
		int numTemplates = propsMgr.getTemplateCount();
		%>
	</head>
	<body>
		<%@include file="header.jsp"%>
		<p>
			There are currently profiles for:
		</p>
		<table class="inventory">

<% // get ready to build drop down menus for books, characters, and locations that can be selected for editing
for(int objCount = 0; objCount < 4; objCount++)
{
	// first, books...
	if(objCount == 0)  // books
	{
		objType = "book";
		// with some grammar control...
		if(numBooks == 0)
		{
			count = "No";
			suffix = "s";
		}
		else // grammar control
		{
			count = Integer.toString(numBooks);
			if(numBooks == 1)
				suffix = "";
			else
				suffix = "s";
		} // end of books grammar control
	} // end of if books
	else  if(objCount == 1) // characters
	{
		// ...and characters...
		objType = "character";
		// ...with more grammar control...
		if(numCharacters == 0)
		{
			count = "No";
			suffix = "s";
		}
		else // grammar control
		{
			count = Integer.toString(numCharacters);
			if(numCharacters == 1)
				suffix = "";
			else
				suffix = "s";
		} // end of grammar control
	} // end of else characters
	// then locations...
	else if(objCount == 2) // locations
	{
		objType = "location";
		// ...with grammar control...
		if(numLocations + numRegions == 0)
		{
			count = "No";
			suffix = "s";
		}
		else // grammar control
		{
			count = Integer.toString(numLocations + numRegions);
			if(numLocations + numRegions == 1)
				suffix = "";
			else
				suffix = "s";
		} // end of location grammar control
	} // end of else locations
	else // templates
	{
		// ...and characters...
		objType = "template";
		// ...with more grammar control...
		if(numTemplates == 0)
		{
			count = "No";
			suffix = "s";
		}
		else // grammar control
		{
			count = Integer.toString(numTemplates);
			if(numTemplates == 1)
				suffix = "";
			else
				suffix = "s";
		} // end of grammar control
	} // end of else templates
	// okay ducks are lined up in a row, build the menu...
%>
	<tr class="table_bullet"><td class="inventory_num"><%= count %></td><td class="inventory_type"><%= objType+suffix%> 
	<%
	if(objCount == 2 && numRegions > 0) 
	{
		%>
		(includes regions) <input type="button" value="?" onclick="displayHelp('locationVsRegion.jsp')" />
		<%
	}
	%>
	</td>
	</tr>
<% } // end of for objCount loop %>
		</table>
		<p>
       		What would you like to do?
		</p>
		Edit/Create 
		<input type="button" name="do_Book" value= "Book" onclick="document.location='editBooks.jsp'" />		
		<input type="button" name="do_Character" value= "Character" onclick="document.location='editCharacters.jsp'" />	
		<input type="button" name="do_Location" value= "Location" onclick="document.location='editLocations.jsp'" />
		<input type="button" name="do_Template" value= "Template" onclick="if(templateEditorConfirmation())document.location='editTemplates.jsp'" />
		<input type="button" name="help" value="?" onclick="displayHelp('begin.jsp');" />
		
		<div class="generateEpubButton">
		<input type="button" id="generateEbookButton" value="Generate..." onclick="document.location='generate.jsp'" />
		</div>	
		
		<script type="text/javascript">
		<%
		if(numBooks < 1)
		{
		%>
//			document.getElementById("generateEpubButton").disabled = true;
			document.getElementById("generateEbookButton").style.display = "none";
		<%
		}
		else
		{
		%>
			document.getElementById("generateEpubButton").style.display = "inline";
		<%
		}
		%>
			
		</script>
		
		<%
		if(ebookList.size() > 0)
		{
			int numEbooks = ebookList.size();
			String ebookCount = new Integer(numEbooks).toString();

		%>
			<div class="downloadEpubs">
			<hr/>
			<form name="deleteEbookForm" action="../BuildEbook" method="post">
			<p>
			You have <%= ebookCount %> finished <i>eBook<%= numEbooks > 1 ? "s" : "" %> currently stored on the server:
			</p>
			<select id="ebooks">
			<%
			Iterator<String> ebooksI = ebookList.iterator();
			while(ebooksI.hasNext())
			{
				String ebookName = ebooksI.next();
			%>
				<option value="<%= ebookName %>"><%= ebookName %></option>
			<%
			}
			%>
			</select>
				<input type="button" name="getEpub" id="getEpub" value="Get eBook" onclick="document.location='/iBook/resources/users/<%= userId %>/resources/' + getEpubName()" />
				<input type="button" value="?" onclick="displayHelp('generatedFiles.jsp');" />
				<input type="hidden" name="userId" value="<%= userId %>" />
			</form>
			<span class="inline_text_note">(Finished eBooks cannot be stored on the server beyond the current log-in session.)</span>
			</div>
		<%
		}
		%>
		
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label"></div>
				<div class="send_back_target"><a href="." onclick="displayHelp('usage.jsp');">Usage Tips</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label"></div>
				<div class="send_back_target"><a href="." onclick="displayHelp('helpFilesIndex.jsp');">Help Files Index</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label"></div>
				<div class="send_back_target"><a href="${pageContext.request.contextPath}/logout">Logout</a></div>
			</div>
		</div>
	</body>
</html>

