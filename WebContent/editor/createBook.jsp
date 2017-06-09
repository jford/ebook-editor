<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.manuscripts.*,com.iBook.datastore.books.*" %>
		
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		%>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Create A New <i>eBook</i> Project
		</p>
		<form name="templateBinding" action="../ManageProfile" method="post" >
		<p>
		<table>
		<tr><td>Title: </td><td><input type="text" name="projectName" id="projectName" size="50" /></td></tr>
	    <tr><td></td><td><div class="input_text_note">(A title is required to create a new project. You can change this title later in the <i>Book Editor</i> page.)</div></td></tr>
		<tr><td>Template:</td><td><select name="templateId" id="templateId" onchange="templateId.options[selectedIndex].value">
			
<% 
	Vector<TemplateProfile> templateList = propsMgr.getTemplateList();
	Iterator<TemplateProfile> templatesI = templateList.iterator();
	TemplateProfile template = null;
	
	String xml = "";
	String templateFilename = "";
	String templateName ="";
	String templateAuthor = "";
	String validationArrayText = "";
	String idValue = "";
	
	while(templatesI.hasNext())
	{
		template = (TemplateProfile)templatesI.next();
		idValue = template.getId();
		templateFilename = template.getFilename();
		idValue = template.getId();
		templateName = template.getTitle() + " by " + template.getAuthor();
		validationArrayText += "\'" + templateName + "\',";
%>
				<option value="<%= /* templateFilename */ idValue %>">
					<%= templateName %>
				</option>
<% 
	}

	
	validationArrayText = validationArrayText.length() > 1 ? validationArrayText.substring(0, validationArrayText.length() - 1) : "empty";
	// end of while{}
%>
			</select> 
			<input type="button" value="?" onclick="displayHelp('templates.jsp')" /></td></tr>
			<tr><td></td><td><div class="input_text_note">(Template selection is final. It cannot be changed once the project is created. Create a new project to use a different template.)</div></td></tr>
			<tr><td colspan="2">Author:</td><td></td></tr>
			<tr><td colspan="2"><div class="input_text_note">(Project author, <i>not</i> author of the source text; if all fields are blank, author will be shown as <i>Anonymous</i></i>)</div></td><td></td></tr>
			<tr><td>Prefix:</td><td><input type="text" name="namePrefix" size="50" /></td></tr>
			<tr><td>First:</td><td><input type="text" name="nameFirst" size="50" /></td></tr>
			<tr><td>Middle:</td><td><input type="text" name="nameMiddle" size="50" /></td></tr>
			<tr><td>Last:</td><td><input type="text" name="nameLast" size="50" /></td></tr>
			<tr><td>Suffix:</td><td><input type="text" name="nameSuffix" size="50" /></td></tr>
		</table>
		</p>
		<input type="submit" name="bindTemplate" value="Create Book Profile" onclick="return validateNewProject(<%= validationArrayText %>);" />
		<input type="button" value="?" onclick="displayHelp('createBookHelp.jsp')" />
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.BOOK %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
		<div class="send_back"> 
			<div class="send_back_pair">
				<div class="send_back_label">Return to</div>
				<div class="send_back_target"><a href="editBooks.jsp">Book Editor</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label"></div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>
		