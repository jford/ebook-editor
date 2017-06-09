<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>

		</script>

		<style>
			p.textblock
			{
				background-color: #eeeeff;
			}
		</style>
	</head>

	<!-- Displays full text of a textblock with descriptor text highlighted; designed for display -->
	<!-- in a popup window called from the ManageDescriptors servlet -->

	<%
	String userId = request.getRemoteUser();
 	PropsManager propsMgr = new PropsManager(userId);
	String manuscriptId = request.getParameter("manuscriptId");
	String descriptorCount = request.getParameter("descriptorCount");
	Manuscript manuscript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId);
	Vector<String[]> descriptors = manuscript.getDescriptors();
	String[] descriptor = descriptors.get(new Integer(descriptorCount));
	String textblockNum = descriptor[1];
	String descriptorText = descriptor[2];
	String msName = manuscript.getMsName();
	String msg = "";
	String textblock = manuscript.getTextblock(msName + "_" + textblockNum);
	StringBuffer textblockBuff = new StringBuffer(textblock);
	int idx = textblock.indexOf(descriptorText);
	int end = idx + descriptorText.length();
	String divStart = "<span class=\"highlight\">";
	String divEnd = "</span>";
	if(idx != -1)
	{
		textblockBuff.insert(end, divEnd);
		textblockBuff.insert(idx, divStart);
	}
	else
	{
		msg = divStart +
			  "No descriptor tag found. If you have edited the template, " +
			  "you must reset the manuscript on the <i>Build Epub</i> page. " +
			  "<b>Note:</b> this will reset all descriptors, and remove all customizations " +
			  "you may have made to the template text." +
			  divEnd;
	}
	%>
	<body>
	
	<%
	if(msg.length() > 0)
	{
	%>
	<%= msg %>
	<%
	}
	%>

	<p class="section_head">
	Textblock <%= textblockNum %>
	</p>
	<p class="inline_text_note" style="margin-top: 3px">
	Only the descriptor text, highlighted in yellow here, can be changed
	in the <i>Descriptor Resolution Editor</i>. To make other changes in this block of text,
	make a note of the textblock number and search for it in the <i>Final Proof Editor</i>.  
	</p>
	<hr/>
	<p class="textblock">
	<%= textblockBuff.toString() %>
	</p>
	<span class="inline_text_note">(Only one descriptor can be edited at a time, even if a textblock contains multiple descriptors. Each will be dealt with individually.)</span>
	</body>
</html>