<%@page import="Util.Errors"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UniFusion :: Session Details</title>
        <link rel="stylesheet" href="CSS/sessionDetails.css">
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
                <div id='topOverlay'></div>
                <div id='info'>
                    <img id='icon' src='${icon}'>
                    <div id='text'>
                        <a id='subheading'>${subheading}</a>
                        <a id='heading'>Session Details</a>
                    </div>
                </div>
            </div>

            <div id='content'>

                <div id='header'>
                    <a id='announcementId'>${id}</a>
                    <a id='announcementTitle'>${date}</a>
                </div>

                <div id='row'>

                    <div id='left'>

                        <div id='tutor'>
                            <img id='tutorIcon' src='${teacherIcon}'>
                            <div id='tutorDetails'>
                                <a id='tutorRole'>TUTOR</a>
                                <a href='#' id='tutorName'>${teacher.getName()}</a>
                            </div>
                        </div>

                        <div class='detail'>
                            <a class='label'>TIME</a>
                            <a class='value'>${range}</a>
                        </div>

                        <div class='detail'>
                            <a class='label'>VENUE</a>
                            <a class='value'>${venue}</a>
                        </div>

                    </div>

                    <div id='right'>

                        <div id='attendance' ${hideIfTeacher}>
                            <div class='detail' id='status'>
                                <a class='label'>ATTENDANCE</a>
                                <a class='value'>${attendedStr}</a>
                            </div>
                            ${attendedStatus}
                            ${markBt}
                        </div>

                    </div>

                </div>


                <div id='buttons'>
                    <!--<input id='edit-button' class='button' type='submit' value='Edit'>-->
                    <a href='AttendanceList?id=${classroom.getClassid()}&code=${id}' id='attendance-button' class='button' ${hideIfStudent}>View Attendance List</a>
                    <a href='PerformDeleteSession?id=${classroom.getClassid()}&code=${id}' id='remove-button' class='button' ${hideIfStudent}>Delete</a>
                </div>

            </div>

        </div>

    </body>

</html>
