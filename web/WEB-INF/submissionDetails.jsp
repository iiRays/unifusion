<%@page import="Util.Errors"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UniFusion :: Submit Assignment</title>
        <link rel="stylesheet" href="CSS/submissionDetails.css">
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
                        <a id='heading'>Submission Details</a>
                    </div>
                </div>
            </div>

            <div id='content' action='GradeSubmission'>

                <div id='header'>
                    <a id='assignmentId'>${assignment.getComponentid()}</a>
                    <a id='assignmentTitle'>${assignment.getTitle()}</a>
                    <a id='submissionId'>Submission ID:<br><span>${submission.getSubmissionid()}</span></a>
                </div>

                <div id='details'>

                    <div id='poster'>
                        <img id='posterIcon' src='${studentIcon}'>
                        <div id='posterDetails'>
                            <a id='posterRole'>MEMBER</a>
                            <a href='#' id='posterName'>${student}</a>
                        </div>
                    </div>

                    <div class='detail'>
                        <a class='label'>SUBMITTED</a>
                        <a class='value'>${dateSubmitted}</a>
                        <a class='subvalue'>${timeSubmitted}</a>
                    </div>

                    <div class='detail'>
                        <a class='label'>STATUS</a>
                        <a class='value'>${submission.getMarks() == null? "Unmarked" : "Marked"}</a>
                    </div>

                    <div class='detail'>
                        <a class='label'>MARKS</a>
                        <a class='value'>${submission.getMarks() == null? "?" : submission.getMarks().toString()} / ${assignment.getTotalmarks()}</a>
                    </div>

                </div>
                
                <div class='remarks'>
                    <a class='remarksLabel'>Student's remarks</a>
                    <a class='remarksContent' id='studentRemarks'>${submission.getComment()}</a>
                </div>

                <div class='remarks'>
                    <a class='remarksLabel'>Tutor's remarks</a>
                    <a class='remarksContent' id='tutorRemarks'>${submission.getRemarks() == null ? "No remarks" : submission.getRemarks().trim().isEmpty() ? "No remarks" :  submission.getRemarks()}</a>
                </div>

                <div id='attachments'>
                    
                    ${attachments}
                    
                    <!--            <div class='attachment'>
                                  <img class='icon' src='https://icons.iconarchive.com/icons/pelfusion/flat-file-type/512/doc-icon.png'>
                                  <a href='#' class='name'>sampleFile.doc</a>
                                </div>
                                <div class='attachment'>
                                  <img class='icon' src='https://how-to.aimms.com/_images/exe-file-icon-68130.png'>
                                  <a href='#' class='name'>sampleProgram.exe</a>
                                </div>-->
                </div>

                <div id='buttons'>
                    ${gradeSubmission}
                    <!--            <a href='#' id='edit-button' class='button'>Edit</a>-->
                    ${deleteSubmission}
                </div>

            </div>

        </div>

    </body>

</html>
