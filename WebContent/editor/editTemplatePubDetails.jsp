<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>
	<body>
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String templateId = request.getParameter("templateId");
		TemplateProfile profile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
		String sourceBookTitle = profile.getTitle();
		String namePrefix = profile.getAuthorNamePrefix();
		String nameFirst = profile.getAuthorNameFirst();
		String nameMiddle = profile.getAuthorNameMiddle();
		String nameLast = profile.getAuthorNameLast();
		String nameSuffix = profile.getAuthorNameSuffix();
		String pubdate = profile.getPubdate();
		String pubtype = profile.getPubtype();
		%>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Edit <i>eBook</i> Publication Details for <i><%= sourceBookTitle %></i>.
		</p>
		<p>
		<form name="editPubDetailsForm" action="../ManageTemplate" method="post">
		<table>
			<tr>
				<td>Title:</td>
				<td><input type="text" name="editSrcBkTitle" value="<%= sourceBookTitle %>" size="50"/></td>
			</tr>
			<tr>
				<td>Publication date:</td>
				<td><input type="text" name="editSrcBkPubdate" value="<%= pubdate %>" size="10" /></td>
			</tr>
			<tr>
				<td>Publication type:</td>
				<td><input type="text" name="editSrcBkPubtype" value="<%= pubtype %>" size="10" /></td>
			</tr>
			<tr><td>Author:</td></tr>
			<tr>
				<td colspan="2">
				<table>
				<tr>
					<td>Title (Mr./Ms):</td>
					<td><input type="text" name="editSrcAuthorNamePrevix" value="<%= namePrefix %>" size = 50" /></td>
				</tr>
				<tr>
					<td>First:</td>
					<td><input type="text" name="editSrcAuthorNameFirst" value="<%= nameFirst %>" size = 50" /></td>
				</tr>
				<tr>
					<td>Middle:</td>
					<td><input type="text" name="editSrcAuthorNameMiddle" value="<%= nameMiddle %>" size = 50" /></td>
				</tr>
				<tr>
					<td>Last:</td>
					<td><input type="text" name="editSrcAuthorNameLast" value="<%= nameLast %>" size = 50" /></td>
				</tr>
				<tr>
					<td>Suffix (Jr./Sr.):</td>
					<td><input type="text" name="editSrcAuthorNameSuffix" value="<%= nameSuffix %>" size = 50" /></td>
				</tr>
				</table>
				</td>
			</tr>
		</table>
		<input type="submit" name="editPubDetailsSubmit" value="Submit changes" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
		</p>
		<div class="send_back">
	          Return to <a href="index.jsp">start</a>
		</div>
	</body>
</html>
