<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
   
<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*,com.iBook.utilities.*" %>
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		int numLocs = propsMgr.getLocationsCount();
		String loc_id = request.getParameter("loc_id");
		String templateId = request.getParameter("templateId");
		String templateTitle = "";
		LocationProfile profile = null;
		Vector<String> books = null;
		Vector<String> geolocaleIds = null;
		Vector<String> aliases = null;
//		Vector<String> localeDescriptions = null;
		Vector<String> locationDescriptions = null;
		boolean editingTemplate = false;

 		if(templateId == null || templateId.compareTo("null") == 0)
		{
			profile = (LocationProfile)propsMgr.getProfile(PropsManager.ObjType.LOCATION, loc_id);
			books = profile.getBooks();
		}
		else
		{
			editingTemplate = true;
			profile = propsMgr.getTemplateLocation(templateId, loc_id);
			templateTitle = ((TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId)).getTitle();
		}
 		String profileType = editingTemplate ? "<i>" + templateTitle + "</i> template" : "<i>eBook</i>";
		geolocaleIds = profile.getGeolocaleIds();
		
		aliases = profile.getAliases();
		locationDescriptions = profile.getLocationDescriptions();

		String loc_name = profile.getName();
		String loc_type = profile.getType();
		String a_or_an = "";
		String geolocalesQty = "";
		Iterator<String> geoI = null;
		%>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Editing <%= profileType %> location <i><%= loc_name %></i>
		</p>
		
		<!----------------------------------------------------------------------
		                      
		                      Intro Statement 
		                      
		 ---------------------------------------------------------------------->
		<%
		if(profile != null)
		{
			if(loc_type.length() == 0)
				loc_type = "location of unspecified type";
			if(!editingTemplate)
			{
				geolocalesQty = geolocaleIds.size() > 1 ? "in these areas (regions)" : "in";
			}
			a_or_an = Utilities.startsWithVowel(loc_type) ? "an" : "a";
		%>
		<p>
		<%= loc_name %> is <%= a_or_an %> <%= loc_type %> 
		<%
		if(!editingTemplate)
		{
		%>
			 located 
		<%
			if(geolocaleIds.size() > 0)
			{
				%>
				<%= geolocalesQty %>:
	<br/>
	<span class="inline_text_note">(<%= propsMgr.getIBookSystemName() %> ID: <i><%= loc_id %></i>) 
	<input type="button" value="?" id="smallHelpButton" onclick="displayHelp('profileIds.jsp')" /></span>
				</p>
				<%
				geoI = geolocaleIds.iterator();
				GeolocaleProfile geoLoc = null;
				String geolocId = "";
				Vector<String> descs = null;
				%>
				<ul>
				<%
				while(geoI.hasNext())
				{
					geolocId = (String)geoI.next();
					geoLoc = propsMgr.getGeolocale(geolocId);
					descs = geoLoc.getDescriptions();
					String typeText = geoLoc.getType();
					%>
					<li> <a href="editRegion.jsp?geolocId=<%= geolocId %>&loc_id=<%= loc_id %>"><%= geoLoc.getName() %></a> <span class="note_text">(click to edit)</span><% if(typeText.length() > 0){ %>, a <%= typeText %><%}%></li>
					<%
					if(descs.size() > 0)
					{
					%>
						<ul>
					<%
						String descText = "";
						Iterator<String> descsI = descs.iterator();
						while(descsI.hasNext())
						{
							descText = (String)descsI.next();
						%>
						<li><%= descText %></li>	
						<% 	
						}
					%>
					</ul>
					<%
					}
				} // end of while geoI(hasNext)
				geoI = null;
				%>
				</ul>
				<%
				} // end of if(editingTemplate){}else...
				%>
			<%
			} // end of if(geoLocales.size() > 0)
			if(editingTemplate)
			{
			%>.<%
			}
			else
			{
				if(geolocaleIds.size() == 0)
				{
				%>
					an undisclosed location.</p>
				<%
				}
			} 
			%>
				<form name="editLocationnameForm" action="../ManageProfile" method="post">
				<input type="text" name="newLocName" size="30" value="<%= loc_name %>" />
				<input type="submit" name="newLocNameSubmit" value="Save Name" /><br/>
				<span class="inline_text_note"> Enter text and click <i>Save Name</i> to change name: </span>
				<input type="hidden" name="loc_id" value="<%= loc_id %>" />
				<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.LOCATION %>" />
				<input type="hidden" name="templateId" value="<%= templateId %>" />
				<input type="hidden" name="userId" value="<%= userId %>" />
				</form>
				<hr/>
				<p>
				<%
				if(!editingTemplate)
				{
				%>
					Edit type (Residence? Forest? Campsite?):
				<%
				}
				else
				{
				%>
					Significance in source text: 
				<%
				}
				%>
				<form name="newLocationTypeForm" action="../ManageProfile" method="post" >
				<input type="text" name="newLocType" size="30" value="<%= profile.getType() %>" />
				<input type="submit" name="editLocType" value="Save Type"/><br/>
				<span class="inline_text_note">Enter text and click <i>Save Type</i> to change: </span>
				<input type="hidden" name="loc_id" value="<%= loc_id %>" />
				<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.LOCATION %>" />
				<input type="hidden" name="templateId" value="<%= templateId %>" />
				<input type="hidden" name="userId" value="<%= userId %>" />
				</form>
				</p>
			<%
			String localeId = "";
			String localeName = "";
			String geoAvailableId = "";
			boolean listIt = true;
			
			// availableIds will become the display list of locales not curfrently associated 
			// with the location, and therefore available for picking
			Vector<String> availableIds = new Vector<String>();
					
			// geolocalesList contains all geolocales, whether or not associated with the location
			Vector<String> geolocalesList = propsMgr.getIdList(PropsManager.ObjType.GEOLOCALE);
			Iterator<String> iLocales = geolocalesList.iterator();

			boolean idAvailable = true;
			while(iLocales.hasNext())
			{
				// iLocales iterates through the list of all possible locales
				geoAvailableId = (String)iLocales.next();
				
				// match each id with the geolocaleIds list---locales currently associated with the location
				geoI = geolocaleIds.iterator();
				for(int i = 0; i < geolocaleIds.size(); i++)
				{
					localeId = (String)geoI.next();
					if(geoAvailableId.compareTo(localeId) == 0)
					{
						// id from the master list is already used for the location; move on to the next one
						idAvailable = false;
						break;
					}
					else
						// ...otherwise, add it to the display list of available ids
						idAvailable = true;
				}
				if(idAvailable)
					availableIds.add(geoAvailableId);
			}

			if(!editingTemplate)
			{
				String a_another = geolocaleIds.size() == 0 ? "a" : "another";
			%>
			<hr/>
			<p>Associate <%= a_another %> region with <%= loc_name %>:
			<form name="associateRegion" action="../ManageProfile" method="post">
			<%
			
			if(availableIds.size() > 0)
			{
			%>
			
				Choose: 
				<select name="region_id" id="region_id">
				<%
				Iterator<String> availI = availableIds.iterator();
				while(availI.hasNext())
				{
					localeId = (String) availI.next();
					localeName = propsMgr.getGeolocaleName(localeId);
					
				%>
					<option value="<%= localeId %>">
						<%= localeName %>
					</option>
				<%
				}
				%>
				</select>
				 and 
				<input type="submit" name="addExistingLocale" value="Add to Location" onclick="menuSelection();"/>
				<input type="hidden" name="loc_id" value="<%= loc_id %>" />
				<input type="hidden" name="geolocId" value="<%= localeId %>" />
				<input type="hidden" name="profileType" value="geolocale" />
				<input type="hidden" name="templateId" value="<%= templateId %>" />
				<input type="hidden" name="userId" value="<%= userId %>" />
			<p>
			or
			</p>
			<%
			}
			%>
			</form>
			<button name="createLocale" onclick="document.location='createRegion.jsp?loc_id=<%= loc_id %>'">Create Region</button><br/>
			<p>
			<div class="input_text_note">
			(All regions are available to all locations.)
			</div>
			<span class="inline_text_note">Location vs. region? <input type="button" id="smallHelpButton" value="?" onclick="displayHelp('locationVsRegion.jsp')" /></span>
			</p>
			<%
			} // end of if(!editingTemplate))
			%>
				<!----------------------------------------------------------------------
				                      
				                      Attributes (descriptions)
				                      
				 ---------------------------------------------------------------------->
				<%
//				locationDescriptions = propsMgr.getLocationDescriptions(loc_id);
				if(locationDescriptions.size() > 0)
				{
					%>
					<p><hr/></p>
					<p>
					Notable attributes for the <%= loc_type %> of <%= loc_name %> include: 
					</p>
					<ul>
					<%
					String descText = "";
					Iterator<String> descI = locationDescriptions.iterator();
 					while(descI.hasNext())
					{
						descText = (String)descI.next();
						%>
						<li><%= descText %> </li>
						<%
					}
					%>
					</ul>
					<form name="editLocAttributesForm" action="editLocAttributes.jsp" method="post">
					<input type="submit" name="editLocAttributes" value="Edit Attributes" />
					<input type="button" value="?" onclick="displayHelp('attributes.jsp')" />
					<input type="hidden" name="loc_id" value="<%= loc_id %>" />
					<input type="hidden" name="templateId" value="<%= templateId %>" />
					<input type="hidden" name="userId" value="<%= userId %>" />
					</form>
					<%
				}
				else
				{
				%>
				<hr/>
				<p>
				There are no notable attributes listed for <%= loc_name %>.
				</p>
				<form name="addLocAttribute" action="../ManageProfile" method="post">
				New attribute text: <input type="text" name="newAttr" size="50"/>
				<input type="submit" name="submitNewLocAttr" value="Add to Location" />
				<input type="button" value="?" onclick="displayHelp('attributes.jsp')" />
				<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.LOCATION %>" />
				<input type="hidden" name="loc_id" value="<%= loc_id %>" />
				<input type="hidden" name="templateId" value="<%= templateId %>" />
				<input type="hidden" name="userId" value="<%= userId %>" />
				</form>
				<%	
				}
				%>
				<!----------------------------------------------------------------------
				                      
				                      Aliases 
				                      
				 ---------------------------------------------------------------------->
				<%
				if(aliases.size() > 0)
				{
				%>
					<hr/>
					<p>
					<%= loc_name %> is also sometimes called:
					</p>
					<ul>
					<%
					String alias = "";
					Iterator<String> aliasesI = aliases.iterator();
					while(aliasesI.hasNext())
					{
						alias = (String)aliasesI.next();
					%>
					<li><%= alias %></li>
					<%
					} // end of while loop/<ul> list
					%> 
					</ul> 
				<%
				} // end of if aliases.size() > 0
				%>
				<form name="editAliases" action="editAliases.jsp" method="post">
				<%
				if(aliases.size() == 0)
				{
				%>
				<hr/>
				There are no other names listed for <%= loc_name %>. 
				<%
				}
				%>
				<input type="submit" name="editAliaes" value="Edit Alias List" />
				<input type="hidden" name="loc_id" value="<%= loc_id %>" />
				<input type="hidden" name="templateId" value="<%= templateId %>" />
				<input type="hidden" name="userId" value="<%= userId %>" />
				</form>
				<%
		} // end of if(profile != null)
		%>
		
	<div class="send_back">
		<%
		if(templateId != null && templateId.compareTo("null") != 0)
		{
		%>
		<div class="send_back_pair">
			<div class="send_back_label">Go to</div>
				<div class="send_back_target"><a href="editTemplate.jsp?templateId=<%= templateId %>">Template Editor</a></div>
		</div>
		<%
		}
		%>
		<div class="send_back_pair">
			<div class="send_back_label">Return to </div>
			<div class="send_back_target"><a href="editLocations.jsp">Location Editor</a></div>
		</div>
		<div class="send_back_pair">
			<div class="send_back_label"></div>
			<div class="send_back_target"><a href="index.jsp">Start</a></div>
		</div>
	</div>
		</body>
		</html>
		

