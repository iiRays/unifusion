<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UniFusion :: Create a Programme</title>
        <link rel="stylesheet" href="CSS/addProgramme.css">
        <link rel="stylesheet" href="CSS/all.css">
    </head>
    <body>
      <div id='container'>

        <div id='navbar'>
            <a href='Dashboard' id='back'>&lt; <span>Back</span></a>
            <a href='Home' id='scaffold'>Scaffold</a>
            <a href='Dashboard' class='link'>Dashboard</a>
            <a href='AccountDetails' class='link'>Account</a>
        </div>

        <div id='top'>
          <div class='image'>
            <img src='https://image.flaticon.com/icons/svg/2991/2991548.svg'>
          </div>
          <div class='text'>
            <a id='title'>Create a programme</a>
            <a id='desc'>You're now creating a programme, let's go!</a>
            <div id='buttons'>
              <a id='proceed-button' href='#form'>Proceed!</a>
              <a id='learn-button' href='#'>Learn more</a>
            </div>
          </div>
        </div>
          
        <a id='error'>Error message</a>

        <form id='form' action="PerformAddProgramme">

          <div class='section'>
            <div class='image' id='left'>
              <img src='https://i.postimg.cc/kgFkP6cK/unifusion-create-class-graphic-1.png'>
            </div>
            <div class='text' id='right'>
              <a class='label'>Programme  code</a>
              <input class='textbox' type='text' name='programmeCode' placeholder='eg. GG420' required>
              <a class='label'>Programme name</a>
              <input class='textbox' type='text' name='programmeTitle' placeholder='eg. Computer Science' required>
            </div>
          </div>

          <div class='section'>
            <div class='text' id='left'>
              <a class='label' id='name'>Is this programme part of an institution?</a>
              <input type='checkbox' class='checkbox' id='hasInstitution' name='hasInstitution' onclick='hasInstitutionClicked()'>
              <label class='checkboxLabel' for='hasInstitution' id='hasInstitutionLabel'>
                <div class='slider'></div>
              </label>
              <a class='label' id='institutionCodeLabel'>Institution code</a>
              <input id='institutionCodeTextbox' class='textbox' type='text' name='institutionCode' placeholder='eg. LOL1337'>
              <input type='hidden' id='institutionCodeEnabled' name='institutionCodeEnabled' value='false'>
            </div>
            <div class='image' id='right'>
              <img src='https://i.postimg.cc/wBD3pkrF/unifusion-create-class-graphic-1-2.png'>
            </div>
          </div>
            
          <div class='section'>
            <div class='image' id='left'>
              <img src='https://i.postimg.cc/wBD3pkrF/unifusion-create-class-graphic-1-2.png'>
            </div>
            <div class='text' id='right'>
              <a class='label'>Description</a>
              <input class='textbox' type='text' name='description' placeholder='eg. This is a CS programme.' required>
              <a class='label' id='name'>Is this programme public?</a>
              <input type='checkbox' class='checkbox' id='isPublic' name='isPublic'>
              <label class='checkboxLabel' for='isPublic' id='isPublicLabel'>
                <div class='slider'></div>
              </label>
            </div>
          </div>

          <input id='create-button' type='submit' value='Create programme!'>

       </form>

      </div>
    </body>
    
    <script>
        function hasInstitutionClicked() {
            var hasInstitution = document.getElementById("hasInstitution");
            var institutionCodeTextbox = document.getElementById("institutionCodeTextbox");

            document.getElementById("institutionCodeEnabled").value = hasInstitution.checked;

            if (hasInstitution.checked) {
                institutionCodeTextbox.disabled = false;
            } else {
                institutionCodeTextbox.disabled = true;
            }
        }
    </script>
</html>