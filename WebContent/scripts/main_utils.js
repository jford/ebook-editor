function showTitlePage()
{
	document.getElementById("title_page_display").style.display = "inline";
}

function hideTitlePage()
{
	document.getElementById("title_page_display").style.display = "none";
}

function get_doc(doc)
{
   document.location = doc;
}

function c_to_f(centigrade)
{
   fahr = 0;
   fahr = (centigrade.value * 9/5) + 32;
   alert(centigrade.value + " degrees Centigrade = " + fahr + " degrees Fahrenheit");  
}

function f_to_c(fahrenheit)
{
   cent = 0;
   cent = (fahrenheit.value - 32) * 5/9; 
   alert(fahrenheit.value + " degrees Fahrenheit = " + cent.toPrecision(3) + " degrees Centigrade");
}

// target="_blank" substitute; target not allowed in HTML 4.0 Strict
function target_sub()
{
   // dom capable browser?
   if(!document.getElementsByTagName) return;
   
   // yes, get list of anchor tags
   var anchors = document.getElementsByTagName("a");
   for(var i=0; i < anchors.length; ++i)
   {
      var anchor = anchors[i];
      if(anchor.getAttribute("href") && (anchor.getAttribute("rel") == "external"))
      {
         anchor.target = "_blank";
      }
   }

}

function setGender(gender)
{
	var genderMaleButton = document.getElementById("male");
	var genderFemaleButton = document.getElementById("female");
	
	if(gender == "male")
		genderMaleButton.checked = true;
	else if(gender == "female")
		genderFemaleButton.checked = true;
}

function validateMapButton(tObjId, iBookObjId)
{
	var ret = true;
	var objType = (tObjId.indexOf("_char_") != -1) ? "character" : (tObjId.indexOf("_geoloc_") != -1) ? "region" : "location";
	
	if(iBookObjId.length == 0)
	{
		alert("You must assign a substitute " + objType + " profile to this " + 
			  "template " + objType + " before you can map aliases.\n\n" +
			  "Select a " + objType + " from the drop down list and click the Set button.");
		ret = false;
	}
	return ret;
}

function validateNewName()
{
	var newName = document.getElementById("newTemplateName").value;
	var ret = true;
	if(newName == "")
	{
		alert("You must enter a new filename for the template.");
		ret = false;
	}
	return ret;
}

function newUserMsg()
{
	var msg = "ElectraInk's Interactive Book Editor provides the means by which you can edit and modify " + 
	          "published works of literature. The literary works provided by ElectraInk, which serve " +
	          "as templates that you can modify using the eBook Editor application, " +
	          "are either in the public domain, or permission for use has been granted by the copyright " +
	          "holder. Tools are also provided by which you can upload other works, to create your own " +
              "templates.\n\n" +
              "It is ElectraInk's intention that users of these eBook editing tools be restricted to " + 
              "working only on those works for which the user has the legal right to modify. By " + 
              "creating a user account, you are consenting to abide by this intention.\n\n" +
	          "Click OK to accept this condition and proceed to create a user account, or click " +
	          "Cancel to  decline.";

	return confirm(msg);
}

function displayFilebreaks(manuscriptId, pubType)
{
	var pubTypeSelector = document.getElementById("outputFormat");
	var pubType = pubTypeSelector.options[pubTypeSelector.selectedIndex].value;
	
	var strWindowFeatures = "scrollbars=yes,resizable=yes,width=500,height=500,top=10,left=10";
	var filebreakWindow = window.open("showFilebreaks.jsp?manuscriptId=" + manuscriptId + "&pubType=" + pubType, "filebreakWindow", strWindowFeatures);
}

function displayMsg()
{
	if(location.search)
	{
		var show = location.search.substring(1).split("=");
		
		alert("show = " + show.value);
		alert("show[0] = " + show[0] + "\nshow[1] = " + show[1]);
		
		if(show[0] == "msg")
			alert(decodeMsg(show[1]));
	}
}

function displayNames()
{
	// shows names assigned to template character profile selected in drop-down menu
	var doc = document.getElementById("tCharSelector");
	var charId = doc.options[doc.selectedIndex].value;
	var templateId = document.getElementById("templateId");
	var strWindowFeatures = "scrollbars=yes,resizable=yes,width=500,height=500,top=10,left=10";
	var showNamesWindow = window.open("showNames.jsp?charId=" + charId + "&templateId=" + templateId.value, "showNamesWindow", strWindowFeatures);
}

function displayNamesForChar(charId, templateId)
{
	// displays names for template char identified by templateId, or if templateId == "" then char from propsMgr
	var strWindowFeatures = "scrollbars=yes,resizable=yes,width=500,height=500,top=10,left=10";
	var showNamesWindow = window.open("showNames.jsp?charId=" + charId + "&templateId=" + templateId, "", strWindowFeatures);
}

function displayPublishAlert()
{ 
	var selector = document.getElementById("outputFormat");
	var pubType = selector.options[selector.selectedIndex].value;
	
	var readerType = pubType == ".epub" ? "eBook" : "PDF";

	var msg = "Click OK, then wait a moment while the editor app \n" +
		"generates your " + pubType + " file. \n\n" +
		"When the file is ready, you will be offered the opportunity \n" +
		"to open it in your " + readerType + " software or save it to a file on \n" +
		"your local system. If you decline either of these options, the \n" +
		"file will be retained on the Electraink server, with a download \n" +
		"link on the start page, only for the remainder of the current \n" +
		"log-in session. \n\n" +
		"Once you log out, the file will be deleted and you will have to \n" +
		"regenerate it the next time you log in.";
	
	return confirm(msg);
}

function mapNames(templateId, tCharId, standinId)
{
	// matches name assignments of template charapcter profiles with name assignments for standin profiles
	var strWindowFeatures = "scrollbars=yes,resizable=yes,width=900,height=500";
	var showNamesWindow = window.open("mapNames.jsp?tCharId=" + tCharId + "&templateId=" + templateId + "&standinId=" + standinId, "showNamesWindow", strWindowFeatures);
}

function displayAliases(templateId)
{
	var strWindowFeatures = "scrollbars=yes,resizable=yes,width=500,height=500,top=10,left=10";
	var aliasesWindow = window.open("showAliases.jsp?templateId=" + templateId, "aliasesWindow", strWindowFeatures);
}

function displayAttribute(templateId)
{
	var strWindowFeatures = "scrollbars=yes,resizable=yes,width=500,height=500,top=15,left=15";
	var attributesWindow = window.open("showAttributes.jsp?templateId=" + templateId, "attributesWindow", strWindowFeatures);
}

function displayHelp(helpFile)
{
	var strWindowFeatures = "scrollbars=yes,resizable=yes,width=500,height=500,left=20,top=20";
	var textsearchWindow = window.open("help/" + helpFile, "helpWindow", strWindowFeatures);
}

function displaySearchResults(manuscriptId)
{
	var searchText = document.getElementById("searchText");
	var strWindowFeatures = "scrollbars=yes,resizable=yes,width=500,height=500,top=10,left=10";
	var textsearchWindow = window.open("showSearchResults.jsp?manuscriptId=" + manuscriptId + "&searchText=" + searchText.value, "textsearchWindow", strWindowFeatures);
}

function displayTextblock(descriptorCount, manuscriptId)
{
	var strWindowFeatures = "scrollbars=yes,resizable=yes,width=500,height=500,top=10,left=10";
	var textblockWindow = window.open("showFullText.jsp?manuscriptId=" + manuscriptId + "&descriptorCount=" + descriptorCount, "textblockWindow", strWindowFeatures);
}

function displayTaggedText(textblockId, templateId)
{
	var strWindowFeatures = "scrollbars=yes,resizable=yes,width=800,height=280,top=10,left=10";
	var textWindow = window.open("showTags.jsp?templateId=" + templateId + "&textblockId=" + textblockId, "textWindow", strWindowFeatures);
}

function displayDisplayText(textblockId, templateId)
{
	var strWindowFeatures = "scrollbars=yes,resizable=yes,width=800,height=280,top=20,left=20";
	var displayTextWindow = window.open("showDisplayText.jsp?templateId=" + templateId + "&textblockId=" + textblockId, "displayTextWindow", strWindowFeatures);
}

function templateEditorConfirmation()
{
	return confirm(
		"The Template Editor is an advanced function that requires in-depth " +
		"knowledge of how the Interactive Book Editor application matches custom text with " +
		"published manuscripts. \r\n\r\n" +
		"Additionally, to create a new template, you must provide the text " +
		"of the document to be modified, in a plain-text file. \r\n\r\n" + 
		"Most interactive book projects won't require the use of the Template Editor.\r\n\r\n" +
		"Continue anyway? \r\n\r\n" +
		"Click OK to continue, or Cancel to return  to the index page.");
}

function validateForgotPasswordUserId()
{
	var ret = true;

	var userId = document.getElementById("userId");

	if(userId.value.localeCompare("") == 0)
	{
		alert("Enter a user name.");
		ret = false;
	}
	return ret;
}

function validateSignupData()
{
	var newPassword = document.getElementById("signupNewPassword").value;
	var confirmPassword = document.getElementById("signupConfirmPassword").value;
	var emailAddr = document.getElementById("signupEmailAddr").value;
	var ret = true;
	var idx = 0;
	
	if(newPassword.localeCompare(confirmPassword) != 0)
	{
		alert("Passwords do not match.");
		ret = false;
	}
	if((idx = emailAddr.indexOf("@")) == -1 ||
	    emailAddr.indexOf(".") == -1)
	{
		alert("You must provide a valid email address.")
		ret = false;
	}
	return ret;
}

function validateNewProject()
{
	var ret = true;
	var index = document.getElementById("templateId").selectedIndex;
	var templateName = arguments[index];
	var projectName = document.getElementById("projectName");
	
	if(projectName.value.localeCompare("") == 0)
	{
		alert("You must enter a title for the new project.\r\n\r\nIf you're not sure what you want to call it, use a temporary title.  It can be changed later, in the book editor.");
		ret = false;
	}
	
	if(ret == true && !templateName)
	{
		alert("You cannot create an eBook project without a template.");
		ret = false;
	}

	if(ret == true)
	{
		ret = confirm("You have chosen to use \r\n\r\n    " + templateName + "\r\n\r\nas your template for the project\r\n\r\n    " + projectName.value + "\r\n\r\nIs that correct?" +
				"\r\n\r\n(OK for yes, Cancel to return to the Create page to make changes.)");
	}
	return ret;
}

function validateNewTemplate()
{
	var numTemplates = arguments.length;
	var count = 0;
	var filename = document.getElementById("templateFilename");
	var sourceTitle = document.getElementById("sourceTitle");
	var namePrefix = document.getElementById("namePrefix");
	var nameFirst = document.getElementById("nameFirst");
	var nameMiddle = document.getElementById("nameMiddle");
	var nameLast = document.getElementById("nameLast");
	var nameSuffix = document.getElementById("nameSuffix");
	var pubdate = document.getElementById("pubdate");
	var pubtype = document.getElementById("pubtype");
	var noName = (namePrefix == null && nameFirst == null && nameMiddle == null && nameLast == null && nameSuffix == null);	
	var ret = true;
	var msg = "";
	
	var illegalFilenameChars = [
            "\\",
            "/",
            "*",
            "?",
            ":",
            "|",
            "<",
            ">",
            "%",
            " "
          ];
	
	if(filename.value.length == 0)
		msg += "- Filename cannot be empty.\n";
	else 
	{
		for(count = 0; count < illegalFilenameChars.length; count++)
		{
			if(filename.value.indexOf(illegalFilenameChars[count]) != -1)
			{
				ret = false;
				msg += "count = " + count + "\n\n- Filename " + filename.value + " contains illegal characters (\\, /, *, ?, :, |, <, >, or space)\n";
				break;
			}
			if(ret == false)
				break;
		}
	}
	if(ret && (!stringEndsWith(filename.value, ".xml") || stringStartsWith(filename.value, ".")))
	{
		msg = "The filename is incorrect:\n\n"
		var newFilename = filename.value;
		if(!stringEndsWith(filename.value, ".xml"))
		{
			msg += "- Template filenames must end with the extension .xml.\n";
			newFilename += ".xml";
		}
		if(stringStartsWith(filename.value, "."))
		{
			msg += "- Template filenames cannot begin with the \"dot\" character.\n";
			newFilename = newFilename.substring(1);
		}
		msg += "\nClick OK to continue saving the template as " + newFilename + ", or Cancel to abort without saving the template.";
		ret = confirm(msg);
	}
	
	if(ret && 
		(sourceTitle.value.length == 0 || 
		 noName ||
		 pubdate.value.length == 0 ||
		 pubtype.value.length == 0)
	  )
	{
		msg = "The profile is incomplete. You must specify:\n\n";
		if(sourceTitle.value.length == 0)
			msg += "- The title of the source document.\n";
		if(noName)
			msg += "- At least one name for author of the source document.\n";
		if(pubdate.value.length == 0)
			msg += "- Date (year) the source document was published.\n";
		if(pubtype.value.length == 0)
			msg += "- Type of publication (novel, short story, poem, play, etc.)\n";
		alert(msg);
		ret = false;
	}
	for(count; count < numTemplates; count++)
	{
		if(filename.value == arguments[count])
		{
			ret = confirm("A template file named " + filename.value + " already exists. Continue anyway? \n\n" + 
					"Click OK to replace the existing template, or Cancel to go back and rename the file.");
			break;
		}
	}
	return ret;
}

function stringStartsWith(string, prefix)
{
	return string.slice(0, prefix.length) == prefix;
}

function stringEndsWith(string, suffix)
{
	return suffix == '' || string.slice(-suffix.length) == suffix;
}

function validEntry(entryValue, type)
{
	var ret = true;
	
	if(entryValue == null || entryValue.length === 0)
	{
		alert("You must enter something for " + type + ".");
		ret = false;
	}
	return ret;
}

function menuSelection()
{
	var region_id = document.getElementById("region_id");
	var selectedValue = region_id.options[region_id.selectedIndex].value;
}

function getEpubName()
{
	var epubs = document.getElementById("epubs");
	return epubs.options[epubs.selectedIndex].value;
}

function confirmDelete()
{
	var loc = document.getElementById("loc_id");
	var book = document.getElementById("bookId");
	var character = document.getElementById("charId");
	var geoloc = document.getElementById("geolocId");
	var template = document.getElementById("templateId");
	
	var selector = null;

	if(loc != null)
		selector = loc;
	else if(book != null)
		selector = book;
	else if(character != null)
		selector = character;
	else if(geoloc != null)
		selector = geoloc;
	else if(template != null)
		selector = template;
	
	var name = selector.options[selector.selectedIndex].text;
	var msg = " will no longer be available as a resource. There is no " +
			  "recovery from this action. ";
	if(character != null)
		msg += "Any changes to you may have made to character aliases " +
			   "because of this character will remain in place. " +
			   "Delete or edit aliases manually on the book profile's character " +
			   "assignment page.\n\n";
	msg += "Are you sure? (OK for yes, delete " + name + "; Cancel for no.)";
	return confirm(name + msg);
}

function confirmMsReset()
{
	return confirm("If you continue, all custom edits to the story text will be lost, " +
			       "and you will have to resolve all descriptors again. \n\n" +
			       "Are you sure? (OK for yes, continue with the reset; Cancel to abort " +
			       "and keep current mansucript changes.");
}

function validateTemplateProfile()
{
	
}

function whyUserAccounts()
{
	var msg =
		"Why do you need a user account to use a free program?\n\n" +
	    "There's really nothing to hide. There's no credit card information \n" +
	    "involved, no personal data to protect. \n\n" +
	    "So why do you need a password-protected user account to access \n" +
	    "the eBook app?\n\n" +
	    "Convenience.\n\n" +
	    "User accounts provide a convenient way to compartmentalize files so \n" +
	    "that users aren't tripping over other users and stepping on each \n"+
	    "other's literary toes.\n\n" +
	    "Your files are accessible only to your user account so that if you and \n"+
	    "another user create a character named Pollyanna and substitute it for \n" +
	    "Count Dracula, for example, your character profiles won't crash into \n" +
	    "each other as you flesh out the details.";
	
	alert(msg);
}

function validateCharacterData()
{
	var ret = true;
	var namePrefix = document.getElementById("namePrefix").value;
	var nameFirst = document.getElementById("nameFirst").value;
	var nameMiddle = document.getElementById("nameMiddle").value;
	var nameLast = document.getElementById("nameLast").value;
	var nameSuffix = document.getElementById("nameSuffix").value;
	var nameShort = document.getElementById("nameShort").value;
	
	var genderGroup = document.forms[0].elements["gender"];
	var gender = "";
	
	var msg = "";
	
	for(var count = 0; count < genderGroup.length; count++)
	{
		if(genderGroup[count].checked)
			gender = genderGroup[count].value;
	}
	
	// must have data for at least one name field
	if(namePrefix.length == 0 &&
		nameFirst.length == 0 &&
		nameMiddle.length == 0 &&
		nameLast.length == 0 &&
		nameSuffix.length == 0 &&
		nameShort.length == 0)
		{
			ret = false;
			msg += "You must enter text in at least one name field. " +
					"If you don't designate a familiar name, one will be derived " +
					"from the other entries. Among other uses, this name will be used " +
					"to construct an internal ID for this character.";
		}
	// must have gender selection
	if(gender.length == 0)
	{
		if(msg.length > 0)
			msg += "\n\n";
		msg += "You must select a value for gender.";
		ret = false;
	}
	if(ret == false)
		alert(msg);
	return ret;
}

function numInRange(totalNum, deleteNum)
{
	var ret = true;
	deleteNum = deleteNum.trim();
	
	if(deleteNum % 1 != 0 || 
	   parseInt(deleteNum, 10) > totalNum ||
	   deleteNum.indexOf(",") != -1 ||
	   deleteNum.indexOf(" ") != -1 ||
	   deleteNum.indexOf(".") != -1 || 
	   deleteNum.indexOf("-") != -1)
	{
		alert("Invalid entry (whole numbers---integers---only, no commas, no periods, must be in valid range).");
		ret = false;
	}
	else if(parseInt(deleteNum, 10) == 0 || 
		isNaN(parseInt(deleteNum)))
	{	
		alert("No number entered, nothing to do.")
		ret = false;
	}
	return ret;
}

function getSelection()
{
	var selection;
	var id = document.getElementById("id");
	var templateId = document.getElementById("templateId");
	
	if(id == null && templateId != null)
		id = templateId;
	
	return id.options[id.selectedIndex].value;
}

function insertMetachars(sStartTag, sEndTag) 
{
	// The function insertMetaChars() places tags (such as HTML markup) around the selected 
	// text in a textarea input.
	// Input arguments are:
	//    sStartTag   the string to place in front of the selected text,
	//    sEndTag     the string to place after the selected text.
	
	var selector = null;
	var selection = null;
	var stringParts = null;
	
	// first, determine selection type---character or location
	
	if(sStartTag.indexOf("character") != -1)
	{
		selector = document.getElementById("tCharSelector");
		stringParts = sStartTag.split("character")
	}
	else if(sStartTag.indexOf("location") != -1)
	{
		selector = document.getElementById("tLocSelector");
		stringParts = sStartTag.split("location");
	}
	else if(sStartTag.indexOf("region") != -1)
	{
		selector = document.getElementById("tGeolocSelector");
		stringParts = sStartTag.split("region");
	}
		
	selection = selector.options[selector.selectedIndex].value;
	sStartTag = stringParts[0] + selection + stringParts[1];
		
	// and proceed with tag output (function derived from 
	// https://developer.mozilla.org/en-US/docs/Web/API/HTMLTextAreaElement)
	
	// bDouble is true if both start and end tags are specified, false if there is only one
	var bDouble = arguments.length > 1; 
	
	// the textarea object
	var oMsgInput = document.enableCharSubForm.textblock;
	
	// position of the first selected character (0 indexed)
	var nSelStart = oMsgInput.selectionStart; 
	
	// position of the character *following* the last selected character (0 indexed)
	var nSelEnd = oMsgInput.selectionEnd; 
	
	// text contained in the textarea
	var sOldText = oMsgInput.value;
	
	// if selected text includes trailing space, output must add trailing space after 
	// the input selection has been replaced; resolution of trailing spaces is on a 
	// 'best ecffort' basis---ManageDescriptors servlet will add spaces to substitute text
	// as needed
	var trailingChar = oMsgInput.value.charAt(nSelEnd - 1) === " " ? " " : "";
	
	// if selected text ends in 's, keep the possessive (soffset is the position of the apostrophe from the 
	// end of the selected text; sOffset needs to account for a possible trailing space) 
	var sOffset = 2;
	if(trailingChar === " ")
		sOffset = 3;
	var trailingApostropheS = (oMsgInput.value.charAt(nSelEnd - sOffset) === "\'" && oMsgInput.value.charAt(nSelEnd - sOffset + 1) === "s") ? "'s" : "";

	// don't do possessive if alias tag
	if(sStartTag.indexOf("alias") !== -1)
		trailingApostropheS = "";
	
//	alert("sOldText.substring(nSelStart, nSelEnd) = \"" + sOldText.substring(nSelStart, nSelEnd) + "\"\r\n" +
//			"trailingChar = \"" + trailingChar + "\"");
	
	// do the substitution
	oMsgInput.value = sOldText.substring(0, nSelStart) + 
	                  (bDouble ? sStartTag + sOldText.substring(nSelStart, nSelEnd) + trailingApostropheS + 
	                  sEndTag : sStartTag + trailingApostropheS + trailingChar) + 
	                  sOldText.substring(nSelEnd);
	
		
	// return with newly inserted text selected 
	oMsgInput.setSelectionRange(bDouble || nSelStart === nSelEnd ? nSelStart + 
			sStartTag.length : nSelStart, (bDouble ? nSelEnd : nSelStart) + 
			sStartTag.length);
	
	// return with the textarea the active object on the page
	oMsgInput.focus();

}

// login.html functions

function confirmEmailFormat(emailAddr)
{
	var ret = true;
	var idx = 0;
	
	if(idx = emailAddr.value.indexOf("@") == -1)
		ret = false;
	else if(emailAddr.value.indexOf(".", idx) == -1)
		ret = false;
	if(ret == false)
		alert("An e-mail address is required.");
	return ret;
}

function confirmJumpToProfileEditor(type)
{
	var typeUpper = "";
	
	if(type.indexOf("character") != -1)
		typeUpper = "Character";
	if(type.indexOf("location") != -1)
		typeUpper = "Location";
	if(type.indexOf("region") != -1)
		typeUpper = "Region";
		
	var msg = "Going to the Create " + typeUpper + " page from this link will add a \n" + type + 
	          " profile to the current template.\n\n" +
	          
	          "If that's your intention, click OK to continue.\n\n" +
	          
	          "If you want to create an eBook " + type + " profile that can be used \n" + 
	          "as a substitute for a template " + type + ", click Cancel and return \n" +
	          "to the start page.";
	
	return confirm(msg);
}

function confirmPasswordEntry(pword, cword)
{
	var yesno = "";
	var ret = true;

	if(pword.value == "" || cword.value == "")
	{
		alert("Please enter password twice to confirm spelling.");
		ret = false
	}
	else
	{
		if(pword.value.localeCompare(cword.value) != 0)
		{
			alert("Password entries do not match.");
			ret = false;
		}
	}
	return ret;
}

function validateFilebreakInput(numTextblocks)
{
	var ret = true;
	var input = document.getElementById("filebreaksList").value;
	var inputElements = input.split(" ");
	var msg = ""; 
	var filebreak = "";
	
	for(count = 0; count < inputElements.length; count++)
	{
		filebreak = inputElements[count];
		if(isNaN(parseInt(filebreak)) ||
		   parseInt(filebreak, 10) > numTextblocks ||
		   filebreak % 1 != 0 || 
		   filebreak.indexOf(",") != -1 ||
		   filebreak.indexOf(" ") != -1 ||
		   filebreak.indexOf(".") != -1 || 
		   filebreak.indexOf("-") != -1)
		{
			msg += filebreak + " is not a valid textblock number\n";
		}
	}
	if(msg.length > 0)
	{
		alert(msg);
		ret = false;
	}
	return ret;
}

function  validateSourceUpload(numTextblocks)
{
	var ret = true;
	if(numTextblocks == "0")
	{
		alert("Template contains no text. Upload a source file and try again.");
		ret = false;
	}
	return ret;
}

function aboutBookEditor()
{
	// 1.0---first release.
	// 1.1---Tom Sawyer template updated
	// 1.2---versioning (this JavaScript alert) added
	
	var version   = "1.3";
	var buildDate = "June 2, 2016";
	
	var msg = "Interactive Book Editor version " + version + "\n" +
			"Build date: " + buildDate;
	
	alert(msg);
}


// end login.html functions
window.onload = target_sub;
