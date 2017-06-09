<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.utilities.*,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>

	<!-- Displays full text of a textblock with descriptor text highlighted; designed for display -->
	<!-- in a popup window called from the ManageDescriptors servlet -->

	<body>
	<%
	String userId = request.getRemoteUser();
 	PropsManager propsMgr = new PropsManager(userId);
			
	String manuscriptId = request.getParameter("manuscriptId");
	String searchText = Utilities.decodeHtmlTags(request.getParameter("searchText"));
	
	Manuscript manuscript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId);
	
	Vector<String> textblocks = manuscript.getTextblocks();
	Vector<String[]> searchHits = new Vector<String[]>();
	
	Iterator<String> textblocksI = textblocks.iterator();
	String context = "";
	String ctxtEnd = "...";
	String ctxtLeadin = "...";
	String highlightEnd = "</span>";
	String highlightStart = "<span class=\"highlight\">";
	String numHits = "";
	String textblock = "";
	
	int textblockNum = 0;
	int hitIdx = 0;
	int hitEnd = 0;
	int tagCheckIdx = 0;
	
	while(textblocksI.hasNext())
	{
		textblockNum++;
		textblock = textblocksI.next();
		while((hitIdx = textblock.indexOf(searchText, hitEnd)) != -1)
		{
			hitEnd = hitIdx + searchText.length();
			for(int spaceCount = 0; spaceCount < 10; spaceCount++)
			{
				hitEnd = textblock.indexOf(" ", hitEnd + 1);
				if(hitEnd == -1)
				{
					ctxtEnd = "";
					hitEnd = textblock.length();
				}
				hitIdx = textblock.lastIndexOf(" ", hitIdx - 1);
				if(hitIdx < 0)
				{
					ctxtLeadin = "";
					hitIdx = 0;
				}
			}
			context = textblock.substring(hitIdx, hitEnd);
			// context string can't start...
			if((tagCheckIdx = context.indexOf(">")) != -1 && context.lastIndexOf("<", tagCheckIdx) == -1)
			{
				context = context.substring(tagCheckIdx + 1);
			}
			// ...or end in the middle of a tag (between < and >)
			if((tagCheckIdx = context.lastIndexOf("<")) != -1 && context.indexOf(">", tagCheckIdx) == -1)
			{
				context = context.substring(0, tagCheckIdx).trim();
			}
			
			context = ctxtLeadin + context + ctxtEnd;
			
			// searchHit = { textblockIdx, "highlighted context string" };
			String[] searchHit = new String[]{"", "" };
			
			searchHit[0] = new Integer(textblockNum).toString();
			searchHit[1] = context;
			searchHits.add(searchHit);
		}
		hitIdx = 0;
		hitEnd = 0;
	}
	numHits = new Integer(searchHits.size()).toString();
	
	if(searchHits.size() == 0)
	{
	%>
		The text <i><%= searchText %></i> was not found.
	<%
	}
	else
	{
	%>
		<p>
		Search for the text <i><%= searchText %></i> produced 
		<%= numHits %> match<%= searchHits.size() == 1 ? ":" : "es:" %>
		</p>
		<p>
		<span class="inline_text_note">(Search is case sensitive. Also 
		note that tags marked by &lt; and &gt;, and therefore rendered invisible 
		by your browser's HTML parser, may block a search match. If a 
		search misses text known to be present in the manuscript, try reducing 
		the search term to as few words as possible, with no punctuation.)</span>
		
		</p>
		<table>
			<tr>
				<th>Context</th><th>Textblock</th>
			</tr>
	<%
		Iterator<String[]> searchHitsI = searchHits.iterator();
		while(searchHitsI.hasNext())
		{
			String[] searchMatch = searchHitsI.next();
			StringBuffer searchMatchText = new StringBuffer(searchMatch[1]);
			hitIdx = searchMatchText.indexOf(searchText);
			searchMatchText.insert(hitIdx + searchText.length(), highlightEnd);
			searchMatchText.insert(hitIdx, highlightStart);
			
	%>
			<tr>
				<td><%= searchMatchText.toString() %></td>
				<td><%= searchMatch[0] %>
			</tr>
	<% 
		}
	%>
		</table>
		
	<%
	}
	%>
	</body>
</html>