<html>
	<head>
		<title>Index to Help Files</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.io.File,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*,com.iBook.utilities.*" %>
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String pathMrkr = propsMgr.getPathMarker();
		String path_to_resources = propsMgr.getPathToResources();
		int idx = path_to_resources.indexOf("resources");
		String path_to_help_files = path_to_resources.substring(0, idx) + "editor" + pathMrkr + "help" + pathMrkr;
		File helpDir = new File(path_to_help_files);
		// list of help file filenames
		String[] helpFiles = helpDir.list();
		int fileCount = 0;
		String sepMrkr = "###";
		String title = "";
		for(fileCount = 0; fileCount < helpFiles.length; fileCount++)
		{
			String titleMrkr = "<title>";
			String helpText = Utilities.read_file(path_to_help_files + helpFiles[fileCount]);
			idx = helpText.indexOf(titleMrkr);
			title = helpText.substring(idx + titleMrkr.length(), helpText.indexOf("</", idx));
			helpFiles[fileCount] = title + sepMrkr + helpFiles[fileCount];
		}
		Arrays.sort(helpFiles);
		%>
	</head>

	<body>
	<p class="section_head">
	Index to Help Files
	</p>
	<p>
	The <%= PropsManager.getIBookSystemName() %> contains the following help files:
	</p>
	<ul>
	<%
	for(fileCount = 0; fileCount < helpFiles.length; fileCount++)
	{
		idx = helpFiles[fileCount].indexOf(sepMrkr);
		title = helpFiles[fileCount].substring(0, idx);
		if(title.indexOf("Index to Help Files") != -1)
			continue;
	%>
		<li><a href="/iBook/editor/help/<%= helpFiles[fileCount].substring(idx + sepMrkr.length()) %>"><%= title %></a></li>
	<%
	}
	%>
	</ul>
	</body>
</html>