<%@page import="Util.Errors"%>
<%@page import="Models.Announcement"%>
<%@page import="Models.Users"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Util.Quick"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UniFusion :: Announcement Details</title>
        <link rel="stylesheet" href="CSS/announcementDetails.css">
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
                        <a id='heading'>Announcement Details</a>
                    </div>
                </div>
            </div>

            <div id='content' action=''>

                <div id='header'>
                    <a id='announcementId'>Announcement</a>
                    <a id='announcementTitle'>${announcement.getTitle()}</a>
                </div>
                    <a id='error' style='margin-bottom: 20px;'><%out.print(Errors.requestSimple(session));%></a>

                <div id='details'>

                    <div id='dateDetails'>
                        <a id='ago'><% out.print(Quick.timeSince(((Announcement) request.getAttribute("announcement")).getDateannounced())); %></a>
                        <a id='date'><% out.print(Quick.formatDate(((Announcement) request.getAttribute("announcement")).getDateannounced())); %></a>
                    </div>

                    <div id='poster'>
                        <img id='posterIcon' src='<% out.print(Quick.getIcon(((Announcement) request.getAttribute("announcement")).getPosterid().getUserid().getImageurl()));%>'>
                        <div id='posterDetails'>
                            <a id='posterRole'><% out.print(Quick.getRole(((Announcement) request.getAttribute("announcement")).getPosterid().getEducatorrole()));%></a>
                            <a href='#' id='posterName'>${announcement.getPosterid().getUserid().getName()}</a>
                        </div>
                    </div>

                </div>

                <a id='message'>${announcement.getMessage()}</a>

                <div id='attachments'>
                    ${fileStr}
                    
                    <!--          <div class='attachment'>
                                <img class='icon' src='https://icons.iconarchive.com/icons/pelfusion/flat-file-type/512/doc-icon.png'>
                                <a href='#' class='name'>sampleFile.doc</a>
                              </div>
                              <div class='attachment'>
                                <img class='icon' src='https://how-to.aimms.com/_images/exe-file-icon-68130.png'>
                                <a href='#' class='name'>sampleProgram.exe</a>
                              </div>-->
                </div>

                ${button}
                
                

            </div>

        </div>

    </body>

</html>
