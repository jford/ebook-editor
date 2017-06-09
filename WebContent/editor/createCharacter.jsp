<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.locations.*" %>
		
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String templateId = request.getParameter("templateId");
		String templateName = "";
		if(templateId != null && templateId.compareTo("null") != 0)
			templateName = propsMgr.getTemplateTitle(templateId);
		%>
	</head>
	<body onload="document.getElementById('namePrefix').focus()">
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Create <i>eBook</i> Character 
	<%
		if(templateName.length() > 0)
		{
	%>
		for <i><%= templateName %></i> template
	<%
		}
	%>
		</p>
		<form name="createCharacter" action="../ManageProfile" method="post">
		<p>
		Name: <input type="button" id="smallHelpButton" value="?" onclick="displayHelp('names.jsp')" />
		</p>
		<ul>
		<li>Prefix: <span class="inline_text_note">(i.e., Mr./Ms./The Right Honorable Grand Poobah)</span> <br/>
		<input type="text" id="namePrefix" name="namePrefix" size="30" /></li>
		<li>First: <br/><input type="text" id="nameFirst" name="nameFirst" size="30" /></li>
		<li>Middle: <br/><input type="text" id="nameMiddle" name="nameMiddle" size="30" /></li>
		<li>Last: <br/><input type="text" id="nameLast" name="nameLast" size="30" /></li>
		<li>Suffix: <span class="inline_text_note"> (i.e., Jr./Sr./the Impaler)</span> <br/>
		<input type="text" id="nameSuffix" name="nameSuffix" size="30" /></li>
		<li>Familiar: <span class="inline_text_note"> (what close friends/relatives use)</span> <br/>
		<input type="text" id="nameShort" name="nameShort" size="30" /></li>
		</ul>
		<hr/>
		<p>
		Age: <input type="text" id="age" name="age" size="3" />
		</p>
		<hr/>
		
		<p>
		Gender: <input type="radio" name="gender" id="gender" value="male" />Male 
		        <input type="radio" name="gender" id="gender" value="female" />Female
		</p>
		<hr/>
		<p>
		Description: <br/>
		<input type="text" id="description" name="description" size="50" />
		<input type="button" value="?" onclick="displayHelp('charDescs.jsp')" /><br/>
		
		<span class="inline_text_note">(Add additional descriptive attributes and aliases on the <i>Character Editor</i> page.)</span>
		</p>
		<hr/>
		<input type="submit" name="submitNewCharacter" id="submitNewCharacter" value="Done" onclick="return validateCharacterData();" />
			<input type="hidden" name="profileType" value="character"/>
			<input type="hidden" name="templateId" value="<%= templateId %>" />
			<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
		
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Return to </div>
				<div class="send_back_target"><a href="editCharacters.jsp">Character Editor</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label"></div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>
		