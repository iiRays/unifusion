<%@page import="Util.Errors"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UniFusion :: Take Attendance</title>
        <link rel="stylesheet" href="CSS/takeAttendance.css">
        <link rel="stylesheet" href="CSS/all.css">
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
              <a id='title'>Take attendance</a>
              <div class='desc'>
                <a class='label' style='margin-right: 24px;'>CLASS</a>
                <a class='value'>Computer Science</a>
              </div>
              <div class='desc'>
                <a class='label' style='margin-right: 10px;'>SESSION</a>
                <a class='value'>Thursday 3-5pm</a>
              </div>

              <form id='form' action="PerformTakeAttendance">
                 <div class='input' id='right'>
                  <a class='label'>Class code</a>
                  <input class='textbox' type='text' name='code' placeholder='eg. 177013'>
                </div>
                <input type='submit' id='proceed-button' value='Proceed!'>
              </form>
              <a id='error'><%out.print(Errors.requestSimple(session));%></a>
            </div>
          </div>

        </div>
      </body>
</html>
