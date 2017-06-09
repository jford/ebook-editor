<html>
	<head>
		<title>Cover Art</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>
	
	<body>
	<p class="section_head">
	Cover Art
	</p>
	<p>
	Cover art is optional. If you don't upload one, the eBook 
	will be built correctly, it just won't have a cover image.
	</p>
	<p>
	If you do upload a properly formatted cover image and generate the eBook in 
	<i>.epub</i> format, the image should be used by 
	most <i>.epub</i> readers as a visual representation of the book.
	</p>
	<p>
	However, the <i>.epub</i> protocol is evolving, and some reader software may 
	not display the image correctly.
	</p>
	<p>
	You may have to experiment with size and file format to get acceptable results, 
	but an internet search for the topic <i>ePub Cover Art</i> suggests the 
	following specifications:
	</p>
	<table class="inline_text_note">
		<tr>
			<td width="25">Format:</td><td><i>.jpg</i></td>
		</tr>
		<tr>
			<td>Dimensions:</td><td>1600 pixels wide by 2400 pixels tall<br/>(height = 1.5 x width)</td>
		</tr>
		<tr>
			<td>Resolution:</td><td>72 dpi</td>
		</tr>
	</table>
	<p>An image that conforms to these specifications will also work in an eBook generated
	in <i>.pdf</i> format.
	<p>
	You can also try <i>.png</i> or for </i> <i>.svg</i> images (though an <i>.sfg</i> image 
	will not work in a <i>.pdf</i> file). 
	</p>
	<p>
	The <i><%= PropsManager.getIBookSystemName("full") %></i> allows you to upload 
	any format, but your eBook reader may not be able to display all formats, and 
	the <i>.pdf</i> format is more restrictive.  
	</p>
	Resolutions higher than 72 dpi produce better images when printed but 
	don't do much for online viewing and result in a larger file size. 
	<hr/>
	<p class="inline_text_note" style="margin-top: 3px">
	If you have already uploaded a cover image for this manuscript, uploading another 
	image will replace the first image. The <b>Delete Cover Art</b> button will 
	remove the cover image for this manuscript, leaving the book with no cover art.
	</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>