<html>
	<head>
		<title>Places (Locations/Regions) Substitution Options</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		<%
		String appName = PropsManager.getIBookSystemName();
		String appNameShort = PropsManager.getIBookSystemName("short");
		%>
		
	</head>

	<body>
	<p class="section_head">
	Places (Locations/Regions) Substitution Options
	</p>
	<ul>
	<li><a href="placesSubstitutionOptions.jsp#names">Names</a></li>
	<li><a href="placesSubstitutionOptions.jsp#tags">Tag Substitution</a></li>
	<li><a href="placesSubstitutionOptions.jsp#descriptions">Descriptions</a></li>
	<li><a href="placesSubstitutionOptions.jsp#aliases">Aliases</a></li>
	<li><a href="placesSubstitutionOptions.jsp#attributes">Attributes</a></li>
	<li><a href="placesSubstitutionOptions.jsp#aliasVsDescriptor">Alias vs. Attribute vs. Descriptor</a>
	</ul>
	<a name="names"></a>
	<p class="section_subhead">Names and Pronouns</p>
	<p>
	Places&mdash;locations and regions&mdash;in a source text can be exchanged for user-defined places only if  
	place-name substitutions have been enabled in the template.  
	</p>
	<p>
	Every instance of the place's name must
	be tagged, by selecting the name in the text block panel and clicking the <b>Name</b> button in the 
	<i>Places</i> column of the <i>Enable Substitutions</i> page in the <i>Template Editor</i>.
	<p>
	<a name="tags"></a><p class="section_subhead">Tag Substitution</p>
	<p>
	When the <i>.epub</i> is generated at the end of the <%= appNameShort %>process, each tagged placename will be 
	exchanged with the corresponding placename associated with the user-defined <%= appNameShort %> location or region profile. 
	</p>
	<a name="descriptions"></a>
	<p class="section_subhead">Descriptions</p>
	<p>
	The <b>Descriptions</b> button is used to flag passages that provide specific references to 
	places. These descriptions will need individual attention during the final proofing phase of the <i>.epub</i>
	generating process.
	</p>
	<a name="aliases"></a>
	<p class="section_subhead">Aliases</p>
	<p>
	Aliases are alternate names used in the source text, for which there won't be a pre-defined
	substitute in the placename's profile (though there may be a list of aliases defined). The user 
	will have to resolve these references during the final processing phase. 
	</p>
	<a name="attributes"></a>
	<p class="section_subhead">Attributes</p>
	<p>
	Attributes are things or descriptive phrases that are specific to the place with which they are 
	associated. In the novel <i>Dracula</i>, for example, Jonathan finds a copy of the <i>English Bradshaw's 
	Guide</i>&mdash;a tourist's guide to the United Kingdom&mdash;in Dracula's Transylvania castle. In the 
	<i>Dracula</i> template, the guide book is defined as an attribute of England. If you change the locale 
	of the novel to, say, California, you might want to change the guide book to something more appropriate 
	to the American West, such as <i>Sunset Magazine</i>.  
	</p>
	<a name="aliasVsDescriptor"></a>
	<p class="section_subhead">Alias vs. Attribute vs. Descriptor</p>
	<p>
	Descriptions are "one-off" flags&mdash;each instance will require some individual resolution. Aliases and 
	attributes can be mapped to alternate text so that all occurrences of the alias or attribute will be 
	automatically resolved by the <%= appNameShort %> process.
	</p>
	<p>
	See the discussion of aliases vs. attributes vs. descriptors in the 
	<a href="characterSubstitutionOptions.jsp">Character Substitution Options</a> help file. The process is the 
	same for places.
	</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>