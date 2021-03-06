<%@page import="Models.Institution"%>
<%@page import="Util.Quick"%>
<%@page import="Util.Errors"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UniFusion :: Edit a Venue</title>
        <link rel="stylesheet" href="CSS/editVenue.css">
        <link rel="stylesheet" href="CSS/all.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    </head>
    
    <body>
        <div id='bot' onclick='location.href="Chatbot"'>
            <img id='eugeo' src='https://www.flaticon.com/svg/static/icons/svg/3398/3398640.svg'>
            <a id='desc'>Need help?<br><span>Eugeo's here!</span></a>
        </div>
        
      <div id='container'>

        <div id='navbar'>
            <a href='Dashboard' id='back'>&lt; <span>Back</span></a>
            <a href='Home' id='scaffold'>Scaffold</a>
            <a href='Dashboard' class='link'>Dashboard</a>
            <a href='AccountDetails' class='link'>Account</a>
            <a href='Chatbot' class='link'>Chatbot</a>
        </div>

        <div id='top'>
          <div id='topOverlay'></div>
          <div id='info'>
            <img id='icon' src='<% out.print(Quick.getIcon(((Institution) request.getAttribute("institution")).getIconurl()));%>'>
            <div id='text'>
              <a id='subheading'>${subheading}</a>
              <a id='heading'>Edit Venue</a>
            </div>
          </div>
        </div>

        <form id='form' action='PerformEditVenue'>
            
            <!-- Hidden fields -->
            <input type="hidden" name="id" value="${id}"/>
            <input type="hidden" name="code" value="${code}"/>
            
          <div id='header'>
            <a id='venueId'>${venue.getVenueid()}</a>
            <a id='venueTitle'>${venue.getTitle()}</a>
          </div>

          <a id='error'><%out.print(Errors.requestSimple(session));%></a>

          <div id='input'>

            <a class='label'>Name</a>
            <input class='textbox' id='name-input' type='text' name='name' placeholder='eg. Class C-15' value="${venue.getTitle()}">

            <a class='label'>Location</a>
            <textarea class="textarea" id='location-input' name='location' placeholder='eg. Alien Insitute Main Campus, Area 51, Nevada'>${venue.getLocation()}</textarea>

            <div id='bottom'>
              <div id='left'>
                <a class='label'>Capacity</a>
                <input class='number' id='capacity-input' type='number' name='capacity' placeholder='eg. 150' value="${venue.getCapacity()}">
              </div>
              <div id='right'>
                <a class='label' id='name'>Is this venue currently active?</a>
                <input type='checkbox' class='checkbox' id='isActive' name='isActive' ${ venue.getIsactive() ? "checked" : "" }>
                <label class='checkboxLabel' for='isActive' id='isActiveLabel'>
                  <div class='slider'></div>
                </label>
              </div>
            </div>

          </div>

          <input id='create-button' type='submit' value='Save!'>

        </form>

      </div>

    </body>

<script src="JS/validator.js"></script>
<script src="JS/editVenue.js"></script>  

</html>
