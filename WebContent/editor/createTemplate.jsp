<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.manuscripts.*" %>
		
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String msg = request.getParameter("msg");
		%>
	</head>
	<%
	if(msg != null && msg.length() > 0)
	{
	%>
		<body onload="alert(msg);">
	<%
	}
	else
	{
	%>
		<body>
	<%
	}
	%>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Create A New <i>eBook</i> Template
		</p>
		<form name="newTemplate" action="../ManageTemplate" method="post" >
		<p>
		<table border="0">
			<tr><td>Save as:</td><td><input type="text" name="templateFilename" id="templateFilename" value="*.xml" size="30" /></td></tr>
			<tr><td></td><td><div class="input_text_note">(Filename to be used when saving the template.)</div></td></tr>
			<tr><td>Book Title:</td><td><input type="text" name="sourceTitle" id="sourceTitle" size="50" /></td></tr>
			<tr><td>Book Author</td></tr>
			<tr><td colspan="2">
				<table>
					<tr><td style="text-align: right">Title:</td><td><input type="text" name="namePrefix" id="namePrefix" size = 30 /><br/><span class="inline_text_note">(i.e., Mr./Ms./The Honorable/...)</span></td></tr>
					<tr><td style="text-align: right">First name:</td><td><input type="text" name="nameFirst" id="nameFirst" size = 30 /></td></tr>
					<tr><td style="text-align: right">Middle name:</td><td><input type="text" name="nameMiddle" id="nameMiddle" size = 30 /></td></tr>
					<tr><td style="text-align: right">Last name:</td><td><input type="text" name="nameLast" id="nameLast" size = 30 /></td></tr>
					<tr><td style="text-align: right">Suffix:</td><td><input type="text" name="nameSuffix" ="nameSuffix" size = 30 /><br/><span class="inline_text_note">(i.e., Jr./Sr./Esq./...)</span></td></tr>
				</table></td></tr>
			<tr><td>Date of publication:</td><td><input type="text" name="pubdate" id="pubdate" size="15" /></td></tr>
			<tr><td>Type of publication:</td><td><input type="text" name="pubtype" id="pubtype" size="30" /><br/><span class="inline_text_note">(i.e., novel/novella/short story/...)</span></td></tr>
		</table>
		</p>
		<%
		String args = "'";
		Vector<String> templateIdList = propsMgr.getTemplateIdList();
		Iterator<String> tplistI = templateIdList.iterator();
		while(tplistI.hasNext())
		{
			args += (String)tplistI.next() + "','";
		}
		if(args.length() >= 2)
			args = args.substring(0, args.length() - 2);
		%>
		<input type="submit" name="newTemplateSubmit" value="Create Template" onclick="return validateNewTemplate(<%= args %>);" />
		<div class="input_text_note">
		<p>
		(Add characters, locations, and source text in the <i>Edit Template</i> page after the template has been created.)
		</p>
		</div>
		<input type="hidden" name="userId" value="<%= userId %>" />
		
		</form>
			
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Return to </div>
				<div class="send_back_target"><a href="editTemplates.jsp">Template Editor</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label"></div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>
		