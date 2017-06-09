<html>
	<head>
		<title>Location Vs. Region</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		<%
		String appNameFull = PropsManager.getIBookSystemName("full");
		String appNameShort = PropsManager.getIBookSystemName("short");
		%>
		
	</head>

	<body>
	<p class="section_head">
	Location Vs. Region
	</p>
	<p>
	Conceptually, the difference between a location and a region is scope. 
	</p>
	<p>
	A region is an area, a place&mdash;a city, a state, a country&mdash;where one or more sets (the specific 
	places where action takes place) are defined.
	</p>
	<p>
	A location is one of those settings&mdash;a hotel, or an estate, or even more narrowly, a bedroom, 
	or a kitchen&mdash;where something happens, or where characters live.
	</p>
	<hr/>
	<p class="inline_text_note" style="margin-top: 3px">
	Locations and regions are not interchangeable.  You cannot assign a location as a standin for what has
	been defined as a region in the template, nor can you use a region as a standin for a location. 
	Locations can only be used for locations, regions for regions.
	</p>
	<hr/>
	<p>
	In the novel <i>Dracula</i>, for example, Carfax&mdash;the estate Dracula purchased 
	to facilitate his relocation to London&mdash;is a location, while both London and England 
	are defined as regions.
	</p>
	<p>
	Functionally, in the <i><%= appNameFull %></i>, a region is defined on the 
	location editor page. That is, you define a location first, then on the page
	that displays your location details, there will be a <b>Create Region</b> button.
	</p>
	<p>
	You then associate the region with your location. For example, you can define a 
	hotel in which a character in the book resides, then you create a region representing 
	the city in which the hotel is located.
	</p>
	<p>
	If you want the action in Dracula to take place in San Francisco, you would start by 
	creating a location that is located in San Francisco. In Bram Stoker's novel, the Dutch 
	doctor Van Helsing stays in the Berkeley Hotel while in London. You could create a 
	hotel location then on the hotel's <i><%= appNameShort %></i> page, create a region called San Francisco, 
	and perhaps another region called California. Once a region has been created it will be 
	available from a drop down menu on any other location's <i><%= appNameShort %></i> page.  
	</p>
	<hr/>
	<p class="inline_text_note" style="margin-top: 3px">
	The association of a location and a region, while necessary structurally (you must go 
	through a location-editor page to create a region), is not used by the 
	<i><%= appNameShort %></i> editor when applying your modifications to a published work. The association
	is an indicator of relationships of places for your convenience while reviewing your
	collection of <i><%= appNameShort %></i> profiles.
	</p>
	<hr/>
	<p>
	If you are creating locations with a specific book project in mind, you should review the 
	locations and regions defined in the template and create your own substitutes accordingly. To 
	see these template-defined places, create a book project and review the location and 
	region assignments pages.  
	</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>