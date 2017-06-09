<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.utilities.*,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>

	<!-- Displays text of a textblock with all <...> tags supporessed; designed to be called from UpdateTextblock servlet -->

	<body>
	<%
	String userId = request.getRemoteUser();
 	PropsManager propsMgr = new PropsManager(userId);
	String templateId = request.getParameter("templateId");
	String manuscriptId = request.getParameter("manuscriptId");
	// if this page is being displayed from the template editor, there will be no 
	// manuscript, and therefore no character/location/region substitutions, which 
	// in turn means no alias or descriptor text substitutions
	boolean havemanuscript = manuscriptId != null ? true : false;
	
	TemplateProfile templateProfile = propsMgr.getTemplateProfile(templateId);
	String textblockId = request.getParameter("textblockId");
	String textblockNum = textblockId.substring(textblockId.lastIndexOf("_") + 1);
	String textblock = templateProfile.getTextblock(textblockId);
	
	TextFactory txtFactory = new TextFactory(userId, templateId);
	
	Vector<CharacterProfile> tChars = templateProfile.getCharacters();
	Vector<GeolocaleProfile> tGeolocs = templateProfile.getGeolocs();
	Vector<LocationProfile> tLocs = templateProfile.getLocations();

 	int idx = 0;
	int end = 0;
	%>
	
	<p>
 	<span class="inline_text_note">Textblock <%= textblockNum %> (Text as it would 
 	appear in the finished <i>.epub</i> with all <i>iBook</i> tags removed.)</span>
	</p>
	<p>
	<%= txtFactory.purgeTags(textblock) %>
	</p>
	</body>
</html>