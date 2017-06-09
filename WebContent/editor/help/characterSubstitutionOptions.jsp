<html>
	<head>
		<title>Character Substitution Options</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		<%
		String appName = PropsManager.getIBookSystemName();
		String appNameShort = PropsManager.getIBookSystemName("short");
		%>
		
	</head>

	<body>
	<p class="section_head">
	Character Substitution Options
	</p>
	<ul>
	<li><a href="characterSubstitutionOptions.jsp#names">Names and Pronouns</a></li>
	<li><a href="characterSubstitutionOptions.jsp#tags">Tag Substitution</a></li>
	<li><a href="characterSubstitutionOptions.jsp#aliases">Aliases</a></li>
	<li><a href="characterSubstitutionOptions.jsp#attributes">Attributes</a></li>
	<li><a href="characterSubstitutionOptions.jsp#gender">Gender Associations and Descriptions</a></li>
	<li><a href="characterSubstitutionOptions.jsp#aliasVsDescriptor">Alias vs. Attribute vs. Descriptor</a>
	</ul>
	<a name="names"></a>
	<p class="section_subhead">Names and Pronouns</p>
	<p>
	Characters in a source text can be exchanged for user-defined characters only if character 
	substitutions have been enabled in the template.  
	</p>
	<p>
	Every instance of the character's name&mdash;in all of its various parts and forms&mdash;must
	be tagged, by selecting the name in the text block panel and clicking the appropriate button in the 
	<i>Characters</i> column of the <i>Enable Substitutions</i> page in the <i>Template Editor</i>.
	<p>
	Every pronoun that refers to the character also needs to be tagged so that the <%= appName %> can 
	keep pronouns consistent with character genders when substitutions cross gender lines.
	</p>
	<a name="tags"></a><p class="section_subhead">Tag Substitution</p>
	<p>
	When the <i>.epub</i> is generated at the end of the <%= appNameShort %> process, each tagged word or phrase will be 
	exchanged with the corresponding word/phrase associated with the user-defined <%= appNameShort %> character profile. 
	</p>
	<p>
	If a word or phrase has been flagged as an alias or attribute in the template, that same word or phrase 
	must be also defined as an alias or attribute in the template profile for the character, location, or 
	region associated with the alias or attribute in the template text. 
	</p>
	<p>
	If the word or phrase is also defined as an alias or attribute in the profile being substituted for 
	the template profile, that definition can be used. The user will also be given the opportunity to 
	define a new substitute word or phrase during the final proofing process. 
	</p>
	<p>
	Alias and attribute definitions are case sensitive. If the template identifies <i>man</i> and <i>Man</i> 
	as aliases for a character, then the corresponding template character profile must define both 
	<i>man</i> and <i>Man</i> as aliases.  
	</p>
	<a name="aliases"></a>
	<p class="section_subhead">Aliases</p>
	<p>
	Aliases are alternate forms of address used in the source text, for which there won't be a pre-defined
	substitute in the user's character profile (though there may be a list of aliases defined). The user 
	will have to resolve these references individually during the final processing phase. 
	</p>
	<p>
	For example, in the novel <i>Tom Sawyer</i>, the title character's first name in the template profile 
	is Thomas, and the short name is Tom. Occasionally, the character is referred to as <i>TOM</i>, when, 
	for instance, Aunt Polly calls him with emphasis. 
	</p>
	<p>
	If all instances of <i>TOM</i> were flagged as the character profile's short name, the final <i>.epub</i> 
	would lose the the emphasis associated with all upper caps when it is generated.
	</p>
	<p>
	But since <i>TOM</i> is flagged as an alias, a user generating a book based on the <i>Tom Sawyer</i> template
	has the option of associating the alias with an all-upper-case version of the substitute character's short 
	name (or anything else, for that matter).
	</p>
	<a name="attributes"></a>
	<p class="section_subhead">Attributes</p>
	<p>
	Attributes are words or phrases other than a name or alias that identify the 
	character&mdash;husband, or wife, for example.
	</p>
	<p>
	Aliases and attributes are very similar, and are somewhat interchangeable. An alias 
	could just as easily have been defined as an attribute, and vice versa. In fact, they \
	are processed in the same way by the <%= PropsManager.getIBookSystemName("short") %> software. 
	</p>
	<p>
	It's just a bit easier to remember an alias as an alternate name and an attribute as bit 
	of descriptive data.
	</p>
	<a name="gender">
	</a><p class="section_subhead">Gender and Description References</p>
	<p>
	The <i>Gender Association</i> and <i>Description</i> tags indicate words and phrases in the source text that 
	are either gender specific or reference something non-gender-related about a character that may need some 
	special attention by the user creating the <i>.epub</i>.  
	</p>
	<p>
	If a user-defined character is a different gender than the source-text character, these 
	gender-specific references may produce some awkward passages in the generated <i>.epub</i> text. 
	</p>
	<p>
	For example, in the book <i>Dracula</i>, references are made to the Count's moustache. If a 
	female character is substituted for Count Dracula, references to the moustache would most likely be
	considered out of place and are flagged as gender-associated descriptors.
	</p>
	<p>
	Elsewhere in <i>Dracula</i>, the character Jonathan Harker is described as an Englishman. If the 
	user relocates the action to America, references to Englishmen&mdash;and solicitors instead of 
	lawyers, or pounds instad of dollars&mdash;may also be considered out of place and so are flagged 
	with the <b>Description</b> button.
	</p>
	<a name="aliasVsDescriptor"></a>
	<p class="section_subhead">Alias vs. Attribute vs. Descriptor</p>
	<p>
	Sometimes, an alias or attribute designation may be the better choice even though the logical tag 
	might seem to be a description or gender association.
	</p>
	<p>
	In <i>Tom Sawyer</i>, for example, Tom is sometimes referred to in the text as <i>the boy</i>. It 
	might be tempting to flag each instance of <i>boy</i> as a gender association so that if a female 
	profile is substituted for the title character, the user can change each instance of <i>boy</i> to 
	<i>girl</i>.
	</p>
	<p>
	But there are 286 instances of <i>boy</i> in the text&mdash;most (though not all) in reference to 
	Tom.
	</p>
	<p>
	That means the user would have to enter the word <i>girl</i> in the <i>descriptor resolution</i>i> page 
	more than 200 times.
	</p>
	<p>
	Instead, the template flags each instance of the word <i>boy</i> when used as a reference to Tom as an alias
	of the Tom Sawyer profile. The user can simply define <i>girl</i> as an alias of the character being 
	substituted for Tom, then map that alias to the alias <i>boy</i> in the character assignment page of 
	the <%= appNameShort %>. 
	</p>
	<hr/>
	<p class="inline_text_note" style="margin-top: 3px">
	Only text that has been flagged will be eligible for substitution when the <i>.epub</i> file is 
	generated, and the substitution will be made only if the template character profile has been replaced by a 
	user-defined character profile.   
	<br/><br/>
	In the novel <i>Tom Sawyer</i>, for example, there are two characters named Joe&mdash;Injun Joe and Joe Harper. 
	The name <i>Joe</i> is flagged for both characters, but Joe Harper's name will only be changed 
	if a character profile has been substituted for the Joe Harper profile in the template. 
	<br/><br/>
	Similarly, in the same novel, Aunt Polly is sometimes referred to as <i>the old lady</i>. Other 
	characters in the novel are also referred to as <i>lady</i> but substitutions for lady will be 
	made only in those references to the Aunt Polly character that are tagged as an alias.
	<br/><br/>
	Each time you click a navigation button (<b>Stay</b>, <b>Advance</b>, or <b>Go Back One</b>) the current 
	editor page will be reloaded into the browser. This will reset all selection menus. You will have 
	to re-select a character, location or region in order to add a new tag.
	</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>