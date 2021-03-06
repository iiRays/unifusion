<%@page import="Util.Server"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Models.Classparticipant"%>
<%@page import="Models.Classparticipant"%>
<%@page import="Models.Users"%>
<%@page import="Util.Quick"%>
<%@page import="Util.Errors"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UniFusion :: Join Institution</title>
        <link rel="stylesheet" href="CSS/joinInstitution.css">
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
          <div class='image'>
            <img src='https://image.flaticon.com/icons/svg/3300/3300028.svg'>
          </div>
          <div class='text'>
            <a id='title'>Join an Institution</a>
            <a id='instruction'>Enter in the code of an existing institution below.</a>
            <a id='error'><%out.print(Errors.requestSimple(session));%></a>
            <form id='form' action="PerformJoinInstitution">
               <div id='right'>
                <div class='input'>
                    <a class='label'>Institution code</a>
                    <input class='textbox' id='institutionCode-input' type='text' name='institutionCode' placeholder='eg. 177013'>
                </div>
                <div class='input' style='margin-left: 30px;'>
                    <a class='label' id='name'>Are you a staff member?</a>
                    <input type='checkbox' class='checkbox' id='isStaff' name='isStaff' onclick='isStaffClicked()'>
                    <label class='checkboxLabel' for='isStaff' id='isStaffLabel'>
                        <div class='slider'></div>
                    </label>
                    <a class='label' id='authorisationCodeLabel' style='margin-top: -10px;'>Authorisation code</a>
                    <input id='authorisationCodeTextbox' class='textbox' type='text' name='authCode' placeholder='eg. LOL1337'>
                </div>
              </div>
              <input type='submit' id='proceed-button' value='Join!'>
            </form>
          </div>
        </div>

      </div>
    </body>
    
<script src="JS/validator.js"></script>
<script src="JS/joinInstitution.js"></script>
    
</html>
