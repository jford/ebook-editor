<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*" %>
		
<%
		String loc_id = request.getParameter("loc_id");
		PropsManager propsMgr = new PropsManager(request.getRemoteUser());
		String location_name = propsMgr.getLocationName(loc_id);
%>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Add Region to <i>iBook</i> Location Profile for <%= location_name %>
		</p>
		<form name="addRegionToLocation" action="" method="post">
		Region name:<br/>
		<input type="text" name="newRegionName" size="50"/><br/>
		Description:<br/>
		<textarea name="newRegionDescription" rows="4" cols="50"></textarea>
		<p>
		<input type="submit" name="submit" value="Add Region to Profile"/>
		</p>
		</form>
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Return to </div>
				<div class="send_back_target"><a href="index.jsp">start</a></div>
			</div>
		</div>
	</body>
</html>
		