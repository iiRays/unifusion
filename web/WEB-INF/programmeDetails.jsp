<%@page import="Util.Errors"%>
<%@page import="Util.Errors"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UniFusion :: Programme Details</title>
        <link rel="stylesheet" href="CSS/programmeDetails.css">
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
          <a id='heading'>Programme details</a>
          <a id='subheading'>Programme name</a>
        </div>
          
        <a id='error'style='margin-top: 40px;'><%out.print(Errors.requestSimple(session));%></a>
        
         <form action="PerformEditProgramme">

        <!-- section 1: identity -->

        <div class='section'>
          <div class='header'>
            <img src='https://i.postimg.cc/G2K4PkF0/unifusion-create-programme-graphic-1.png'>
          </div>

          <div class='textboxes'>

            <div id='left'>
            <a class='label' id='name'>Programme code</a>
              <input class='textbox' id='programmeCode-input' style='background-color: rgba(223, 224, 242, 0.5); color: white;' type='text' name='programmeCode' placeholder='eg. GG420' value="${programme.getProgrammecode()}" readonly>
            </div>

            <div id='right'>
              <a class='label' id='email'>Programme title</a>
              <input class='textbox' id='programmeName-input' type='text' name='programmeTitle' placeholder='eg. Software Systems Development' value="${programme.getTitle()}">
            </div>

          </div>
        </div>

        <!-- section 2: institution -->

        <div class='section'>
          <div class='header'>
            <img src='https://i.postimg.cc/Y0ShdhRv/unifusion-create-programme-graphic-2.png'>
          </div>

          <div class='textboxes' id='institutionTextboxes'>
            <div id='left'>
              <a id='institutionTextboxesMsg'>The institution details of a programme cannot be edited.</a>
              <a class='label' id='name'>Is this programme part of an institution?</a>
              <input type='checkbox' class='checkbox' id='hasInstitution' name='hasInstitution'  readonly>
              <label class='checkboxLabel' for='hasInstitution' id='hasInstitutionLabel'>
              <div class='slider'></div>
              </label>
              <a class='label' id='institutionCodeLabel'>Institution code</a>
              <input id='institutionCodeTextbox' class='textbox' type='text' name='institutionCode' placeholder='eg. LOL1337' disabled value="${programme.getInstitutioncode()}" ${programme.getInstitutioncode() == null ? "" : "checked"}>
              <input type='hidden' id='institutionCodeEnabled' name='institutionCodeEnabled' value='${programme.getInstitutioncode() == null ? "false" : "true"}' disabled>
            </div>

          </div>
        </div>

        <!-- section 3: details -->

        <div class='section'>
          <div class='header'>
            <img src='https://i.postimg.cc/C56dXSrm/unifusion-create-programme-graphic-3.png'>
          </div>

          <div class='textboxes'>

            <div id='left'>
            <a class='label' id='name'>Description</a>
              <input class='textbox' id='description-input' type='text' name='description' placeholder='eg. This is a CS course.' value="${programme.getDescription()}">
            </div>

            <div id='right'>
              <a class='label' id='name'>Is this class public?</a>
              <input type='checkbox' class='checkbox' id='isPublic' name='isPublic' ${programme.getIspublic() ? "checked" : ""}>
              <label class='checkboxLabel' for='isPublic' id='isPublicLabel'>
                <div class='slider'></div>
              </label>
            </div>

          </div>
        </div>

        <!-- section 4: design -->

        <div class='section'>
          <div class='header'>
            <img src='https://i.postimg.cc/C56dXSrm/unifusion-create-programme-graphic-3.png'>
          </div>

          <div class='textboxes'>

            <div id='left'>
              <a class='label' id='name'>Icon URL</a>
              <input class='textbox' type='text' name='iconURL' placeholder='eg. example.com' value="${programme.getIconurl()}">
              <!--<a class='label' id='name'>Banner URL</a>
              <input class='textbox' type='text' name='bannerURL' placeholder='eg. example.com'>-->
            </div>

            <div id='right'>
              <!--<a class='label'>Colour theme</a>
              <select class='dropdown' name='colourTheme'>
                <option value='lecture'>Default</option>
                <option value='tutorial'>Light</option>
                <option value='practical'>Dark</option>
              </select>
              <a class='dropdownLabel'>Click to view options</a>-->
            </div>

          </div>
        </div>

        <!-- bottom section: save button -->

        <div class='section'>
          <input id='save-button' type='submit' value='Save!'>
        </div>
         </form>
      </div>
    </body>

<script src="JS/validator.js"></script>
<script src="JS/programmeDetails.js"></script>

    <script>
        function hasInstitutionClicked() {
            
            // note: editing institution settings in a programme has been disabled
            
            /*var hasInstitution = document.getElementById("hasInstitution");
            var institutionCodeTextbox = document.getElementById("institutionCodeTextbox");

            document.getElementById("institutionCodeEnabled").value = hasInstitution.checked;

            if (hasInstitution.checked) {
                institutionCodeTextbox.disabled = false;
            } else {
                institutionCodeTextbox.disabled = true;
            }*/
        }
    </script>
</html>
