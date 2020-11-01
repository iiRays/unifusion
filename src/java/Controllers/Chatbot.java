/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Util.Quick;
import Util.Server;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import java.util.regex.*;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

/**
 *
 * @author mast3
 */
@WebServlet(name = "Chatbot", urlPatterns = {"/Chatbot"})
public class Chatbot extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    private UserTransaction utx;

    // Commands
    String retrieveCommand = "(show me|give me|display|show|get)( all)*.*";
    String questions = "(how many|what (time|is the time)).*";
    String create = "(create|make|new|develop)( a| an)? ";
    String createEducation = create + "(class|course|institution|programme).*";
    String createClassObjects = create + "(announcement|session|assignment).*";

    // Util objects
    Util.Servlet servlet;
    Util.DB db = new Util.DB(em, utx);
    Models.Users user;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        servlet = new Util.Servlet(request, response);
        user = Server.getUser(request, response);

        // Redirect guests
        if (user == null) {
            servlet.toServlet("Login");
            return;
        }

        // Read input
        String input = servlet.getQueryStr("query");

        if (input != null && !input.trim().isEmpty()) {
            // Process query

            input = input.replaceAll("\"", "\'");
            input(input);
            servlet.putInJsp("query", input);
            servlet.servletToJsp("chatbot.jsp?refresh=true");
            return;
        }

        replyChat("Hey there! I'm your virtual secretary.");

        // Redirect
        servlet.servletToJsp("chatbot.jsp");

    }

    // Showing stats that can be added to other info
    public String addStats(String heading, String subheading) {
        String output = "<div class='result stat'>\n"
                + "                  <div class='top'>\n"
                + "                    <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/423/423786.svg'>\n"
                + "                    <div class='text'>\n"
                + "                      <a class='stat'>"+heading+"</a>\n"
                + "                      <a class='desc'>"+subheading+"</a>\n"
                + "                    </div>\n"
                + "                  </div>\n"
                + "                </div>";
        
        return output;
    }
    
    // Showing stats only
    public void replyStats(String heading, String subheading) {
        String output = "<div class='result stat'>\n"
                + "                  <div class='top'>\n"
                + "                    <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/423/423786.svg'>\n"
                + "                    <div class='text'>\n"
                + "                      <a class='stat'>"+heading+"</a>\n"
                + "                      <a class='desc'>"+subheading+"</a>\n"
                + "                    </div>\n"
                + "                  </div>\n"
                + "                </div>";
        
        servlet.putInJsp("results",output);
    }
    
    // Get today's classes
    public void getTodayClasses(){
        
    }

    // Get all classes with FROM
    public void showClasses(String from) {
        // Get from all levels
        List<Models.Class> fromInstitution = em.createNativeQuery("select c.* from class c, course cr, programme pg, institution i, institutionparticipant ipa, participant p where c.coursecode = cr.coursecode and cr.programmecode = pg.programmecode and pg.institutioncode = i.institutioncode and ipa.institutioncode = i.institutioncode and ipa.participantid = p.participantid and p.userid = ? and i.institutioncode = ?", Models.Class.class).setParameter(1, user.getUserid()).setParameter(2, from).getResultList();
        List<Models.Class> fromProgramme = em.createNativeQuery("select c.* from class c, course cr, programme pg,programmeparticipant ppa, participant p where c.coursecode = cr.coursecode and cr.programmecode = pg.programmecode and ppa.programmecode = pg.programmecode and ppa.participantid = p.participantid and p.userid = ? and pg.programmecode = ?", Models.Class.class).setParameter(1, user.getUserid()).setParameter(2, from).getResultList();
        List<Models.Class> fromCourse = em.createNativeQuery("select c.* from class c, course cr, courseparticipant cpa, participant p where c.coursecode = cr.coursecode and cpa.coursecode = cr.coursecode and cpa.participantid = p.participantid and p.userid = ? and cpa.coursecode = ?", Models.Class.class).setParameter(1, user.getUserid()).setParameter(2, from).getResultList();

        // Combine the results
        List<Models.Class> results = new ArrayList<Models.Class>();
        results.addAll(fromInstitution);
        results.addAll(fromProgramme);
        results.addAll(fromCourse);

        // Display the output
        String output = "";

        output = addChat(results.size() + " classes were found.");

        for (Models.Class classroom : results) {
            output += "<div class='result display'  onclick=\"window.location.href='Class?id=" + classroom.getClassid() + "'\">\n"
                    + "                  <div class='top'>\n"
                    + "                    <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/717/717874.svg'>\n"
                    + "                    <div class='text'>\n"
                    + "                      <a class='type'>CLASS</a>\n"
                    + "                      <a class='name'>" + classroom.getClasstitle() + "</a>\n"
                    + "                      <a class='subname'>" + classroom.getClassid() + "</a>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>";
        }

        servlet.putInJsp("result", output);
    }

    // Get all classes without FROM
    public void showClasses() {
        // Get from all levels
        List<Models.Class> results = em.createNativeQuery("select c.* from class c, classparticipant cpa, participant p where c.classid = cpa.classid and cpa.participantid = p.participantid and p.userid = ?", Models.Class.class).setParameter(1, user.getUserid()).getResultList();

        // Display the output
        String output = "";

        output = addChat(results.size() + " classes were found.");

        for (Models.Class classroom : results) {
            output += "<div class='result display' onclick=\"window.location.href='Class?id=" + classroom.getClassid() + "'\">\n"
                    + "                  <div class='top'>\n"
                    + "                    <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/717/717874.svg'>\n"
                    + "                    <div class='text'>\n"
                    + "                      <a class='type'>CLASS</a>\n"
                    + "                      <a class='name'>" + classroom.getClasstitle() + "</a>\n"
                    + "                      <a class='subname'>" + classroom.getClassid() + "</a>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>";
        }

        servlet.putInJsp("result", output);
    }

    // Get all courses with FROM
    public void showCourses(String from) {
        // Get from all levels
        List<Models.Course> fromInstitution = em.createNativeQuery("select cr.* from course cr, programme pg, institution i, institutionparticipant ipa, participant p where cr.programmecode = pg.programmecode and pg.institutioncode = i.institutioncode and ipa.institutioncode = i.institutioncode and ipa.participantid = p.participantid and p.userid = ? and i.institutioncode = ?", Models.Course.class).setParameter(1, user.getUserid()).setParameter(2, from).getResultList();
        List<Models.Course> fromProgramme = em.createNativeQuery("select cr.* from  course cr, programme pg,programmeparticipant ppa, participant p where cr.programmecode = pg.programmecode and ppa.programmecode = pg.programmecode and ppa.participantid = p.participantid and p.userid = ? and pg.programmecode = ?", Models.Course.class).setParameter(1, user.getUserid()).setParameter(2, from).getResultList();

        // Combine the results
        List<Models.Course> results = new ArrayList<Models.Course>();
        results.addAll(fromInstitution);
        results.addAll(fromProgramme);

        // Display the output
        String output = "";

        output = addChat(results.size() + " courses were found.");

        for (Models.Course course : results) {
            output += "<div class='result display'  onclick=\"window.location.href='Course?id=" + course.getCoursecode() + "'\">\n"
                    + "                  <div class='top'>\n"
                    + "                    <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/717/717874.svg'>\n"
                    + "                    <div class='text'>\n"
                    + "                      <a class='type'>COURSE</a>\n"
                    + "                      <a class='name'>" + course.getTitle() + "</a>\n"
                    + "                      <a class='subname'>" + course.getCoursecode() + "</a>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>";
        }

        servlet.putInJsp("result", output);
    }

    // Get all courses without FROM
    public void showCourses() {
        List<Models.Course> results = em.createNativeQuery("select cr.* from course cr, courseparticipant cpa, participant p where cr.coursecode = cpa.coursecode and cpa.participantid = p.participantid and p.userid = ?", Models.Course.class).setParameter(1, user.getUserid()).getResultList();

        // Display the output
        String output = "";

        output = addChat(results.size() + " courses were found.");

        for (Models.Course course : results) {
            output += "<div class='result display' onclick=\"window.location.href='Class?id=" + course.getCoursecode() + "'\">\n"
                    + "                  <div class='top'>\n"
                    + "                    <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/717/717874.svg'>\n"
                    + "                    <div class='text'>\n"
                    + "                      <a class='type'>COURSE</a>\n"
                    + "                      <a class='name'>" + course.getTitle() + "</a>\n"
                    + "                      <a class='subname'>" + course.getCoursecode() + "</a>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>";
        }

        servlet.putInJsp("result", output);
    }

    // Get all programmes with FROM
    public void showProgrammes(String from) {
        // Get from all levels
        List<Models.Programme> results = em.createNativeQuery("select pg.* from programme pg, institution i, institutionparticipant ipa, participant p where pg.institutioncode = i.institutioncode and ipa.institutioncode = i.institutioncode and ipa.participantid = p.participantid and p.userid = ? and i.institutioncode = ?", Models.Programme.class).setParameter(1, user.getUserid()).setParameter(2, from).getResultList();

        // Display the output
        String output = "";

        output = addChat(results.size() + " programmes were found.");

        for (Models.Programme programme : results) {
            output += "<div class='result display'  onclick=\"window.location.href='Programme?id=" + programme.getProgrammecode() + "'\">\n"
                    + "                  <div class='top'>\n"
                    + "                    <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/717/717874.svg'>\n"
                    + "                    <div class='text'>\n"
                    + "                      <a class='type'>PROGRAMME</a>\n"
                    + "                      <a class='name'>" + programme.getTitle() + "</a>\n"
                    + "                      <a class='subname'>" + programme.getProgrammecode() + "</a>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>";
        }

        servlet.putInJsp("result", output);
    }

    // Get all programmes  without FROM
    public void showProgrammes() {
        List<Models.Programme> results = em.createNativeQuery("select pg.* from programme pg, programmeparticipant ppa, participant p where pg.programmecode = ppa.programmecode and ppa.participantid = p.participantid and p.userid = ?", Models.Programme.class).setParameter(1, user.getUserid()).getResultList();

        // Display the output
        String output = "";

        output = addChat(results.size() + " programmes were found.");

        for (Models.Programme programme : results) {
            output += "<div class='result display' onclick=\"window.location.href='Programme?id=" + programme.getProgrammecode() + "'\">\n"
                    + "                  <div class='top'>\n"
                    + "                    <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/717/717874.svg'>\n"
                    + "                    <div class='text'>\n"
                    + "                      <a class='type'>PROGRAMME</a>\n"
                    + "                      <a class='name'>" + programme.getTitle() + "</a>\n"
                    + "                      <a class='subname'>" + programme.getProgrammecode() + "</a>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>";
        }

        servlet.putInJsp("result", output);
    }

    // Get all institutions
    public void showInstitutions() {
        List<Models.Institution> results = em.createNativeQuery("select i.* from institution i, institutionparticipant ipa, participant p where i.institutioncode = ipa.institutioncode and ipa.participantid = p.participantid and p.userid = ?", Models.Institution.class).setParameter(1, user.getUserid()).getResultList();

        // Display the output
        String output = "";

        output = addChat(results.size() + " institutions were found.");

        for (Models.Institution institution : results) {
            output += "<div class='result display' onclick=\"window.location.href='Institution?id=" + institution.getInstitutioncode() + "'\">\n"
                    + "                  <div class='top'>\n"
                    + "                    <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/717/717874.svg'>\n"
                    + "                    <div class='text'>\n"
                    + "                      <a class='type'>INSTITUTION</a>\n"
                    + "                      <a class='name'>" + institution.getName() + "</a>\n"
                    + "                      <a class='subname'>" + institution.getInstitutioncode() + "</a>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>";
        }

        servlet.putInJsp("result", output);
    }

    // Getting single data
    public void getSingleClass(String id) {
        // Get from all levels
        List<Models.Class> results = em.createNativeQuery("select c.* from class c, classparticipant cpa, participant p where c.classid = cpa.classid and cpa.participantid = p.participantid and p.userid = ? and c.classid = ?", Models.Class.class).setParameter(1, user.getUserid()).setParameter(2, id).getResultList();

        // Display the output
        String output = "";

        if (results.size() > 0) {
            output = addChat("Here's your class");
        } else {
            output = addChat("Hmm... I couldn't find your class.");
        }

        for (Models.Class classroom : results) {
            output += "<div class='result display' onclick=\"window.location.href='Class?id=" + classroom.getClassid() + "'\">\n"
                    + "                  <div class='top'>\n"
                    + "                    <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/717/717874.svg'>\n"
                    + "                    <div class='text'>\n"
                    + "                      <a class='type'>CLASS</a>\n"
                    + "                      <a class='name'>" + classroom.getClasstitle() + "</a>\n"
                    + "                      <a class='subname'>" + classroom.getClassid() + "</a>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>";
        }

        servlet.putInJsp("result", output);
    }

    public void getSingleCourse(String id) {
        List<Models.Course> results = em.createNativeQuery("select cr.* from course cr, courseparticipant cpa, participant p where cr.coursecode = cpa.coursecode and cpa.participantid = p.participantid and p.userid = ? and cr.coursecode = ?", Models.Course.class).setParameter(1, user.getUserid()).setParameter(2, id).getResultList();

        // Display the output
        String output = "";

        if (results.size() > 0) {
            output = addChat("Here's your course");
        } else {
            output = addChat("Hmm... I couldn't find your course.");
        }

        for (Models.Course course : results) {
            output += "<div class='result display' onclick=\"window.location.href='Class?id=" + course.getCoursecode() + "'\">\n"
                    + "                  <div class='top'>\n"
                    + "                    <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/717/717874.svg'>\n"
                    + "                    <div class='text'>\n"
                    + "                      <a class='type'>COURSE</a>\n"
                    + "                      <a class='name'>" + course.getTitle() + "</a>\n"
                    + "                      <a class='subname'>" + course.getCoursecode() + "</a>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>";
        }

        servlet.putInJsp("result", output);
    }

    public void getSingleProgramme(String id) {
        List<Models.Programme> results = em.createNativeQuery("select pg.* from programme pg, programmeparticipant ppa, participant p where pg.programmecode = ppa.programmecode and ppa.participantid = p.participantid and p.userid = ? and pg.programmecode = ?", Models.Programme.class).setParameter(1, user.getUserid()).setParameter(2, id).getResultList();

        // Display the output
        String output = "";

        if (results.size() > 0) {
            output = addChat("Here's your programme");
        } else {
            output = addChat("Hmm... I couldn't find your programme.");
        }

        for (Models.Programme programme : results) {
            output += "<div class='result display' onclick=\"window.location.href='Programme?id=" + programme.getProgrammecode() + "'\">\n"
                    + "                  <div class='top'>\n"
                    + "                    <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/717/717874.svg'>\n"
                    + "                    <div class='text'>\n"
                    + "                      <a class='type'>PROGRAMME</a>\n"
                    + "                      <a class='name'>" + programme.getTitle() + "</a>\n"
                    + "                      <a class='subname'>" + programme.getProgrammecode() + "</a>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>";
        }

        servlet.putInJsp("result", output);
    }

    public void getSingleInstitution(String id) {
        List<Models.Institution> results = em.createNativeQuery("select i.* from institution i, institutionparticipant ipa, participant p where i.institutioncode = ipa.institutioncode and ipa.participantid = p.participantid and p.userid = ? and i.institutioncode = ?", Models.Institution.class).setParameter(1, user.getUserid()).setParameter(2, id).getResultList();

        // Display the output
        String output = "";

        if (results.size() > 0) {
            output = addChat("Here's your institution");
        } else {
            output = addChat("Hmm... I couldn't find your institution.");
        }

        for (Models.Institution institution : results) {
            output += "<div class='result display' onclick=\"window.location.href='Institution?id=" + institution.getInstitutioncode() + "'\">\n"
                    + "                  <div class='top'>\n"
                    + "                    <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/717/717874.svg'>\n"
                    + "                    <div class='text'>\n"
                    + "                      <a class='type'>INSTITUTION</a>\n"
                    + "                      <a class='name'>" + institution.getName() + "</a>\n"
                    + "                      <a class='subname'>" + institution.getInstitutioncode() + "</a>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>";
        }

        servlet.putInJsp("result", output);
    }

    public String addChat(String text) {
        String output = " <div class='result chat'>\n"
                + "                  <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/1041/1041916.svg'>\n"
                + "                  <a class='text'>" + text + "</a>\n"
                + "                </div>";

        return output;
    }

    public void replyChat(String text) {
        String output = " <div class='result chat'>\n"
                + "                  <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/1041/1041916.svg'>\n"
                + "                  <a class='text'>" + text + "</a>\n"
                + "                </div>";

        servlet.putInJsp("result", output);
    }

    public void addCreateEducationComponent(String type, String id, String name, String addServletName) {
        if (id != null) {
            id = id.replaceAll("\"", "");
            id = id.replaceAll("\'", "");
        }
        String idHref = id == null ? "" : id.trim().isEmpty() ? "" : id;
        String titleHref = name == null ? "" : name.trim().isEmpty() ? "" : name;

        // Replaces apostrophe
        titleHref = titleHref.replaceAll("\'", "%27");

        String output = "  <div class='result action' onclick='window.location.href=\"" + addServletName + "?id=" + idHref + "&title=" + titleHref + "\"'>\n"
                + "            <div class='top'>\n"
                + "              <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/867/867855.svg'>\n"
                + "              <div class='text'>\n"
                + "                <a class='type'>ACTION</a>\n"
                + "                <a class='desc'>Create a <span>" + type + "</span></a>\n"
                + "              </div>\n"
                + "            </div>\n";

        if (id != null || name != null) {
            output += "     <div class='bottom'>\n"
                    + "              <a class='desc'>With the following details:</a>\n";

            if (id != null) {
                output += "      <div class='item'>\n"
                        + "                <a class='label'>ID</a>\n"
                        + "                <a class='value'>" + id + "</a>\n"
                        + "              </div>\n";
            }

            if (name != null) {
                output += "      <div class='item'>\n"
                        + "                <a class='label'>NAME</a>\n"
                        + "                <a class='value'>" + name + "</a>\n"
                        + "              </div>\n";
            }

            output += "          </div>";
        }

        output += "            </div>  \n";

        servlet.putInJsp("result", output);
    }

    public void addCreateAssignment(String classId, String title, String description) {
        // Get class results
        List<Models.Class> classList = em.createNativeQuery("select c.* from class c, classparticipant cpa, participant p where c.classid = ? and c.classid = cpa.classid and cpa.participantid = p.participantid and p.userid = ? and cpa.role='teacher'", Models.Class.class).setParameter(1, classId).setParameter(2, user.getUserid()).getResultList();

        // To hold output
        String output = "";

        // If empty
        if (classList.size() == 0) {
            replyChat("Seems like this class doesn't exist");
            return;
        }

        // A result for each
        for (Models.Class classroom : classList) {
            String titleHref = title == null ? "" : title.trim().isEmpty() ? "" : title;
            String descHref = description == null ? "" : description.trim().isEmpty() ? "" : description;

            output = "  <div class='result action' onclick='window.location.href=\"AddAssignment?title=" + titleHref + "&desc=" + descHref + "&id=" + classId + "\"'>\n"
                    + "            <div class='top'>\n"
                    + "              <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/3324/3324859.svg'>\n"
                    + "              <div class='text'>\n"
                    + "                <a class='type'>ACTION</a>\n"
                    + "                <a class='desc'>Create an assignment for <span>" + classId + "</span></a>\n"
                    + "              </div>\n"
                    + "            </div>\n";

            if (title != null || description != null) {
                output += "     <div class='bottom'>\n"
                        + "              <a class='desc'>With the following details:</a>\n";

                if (title != null) {
                    output += "      <div class='item'>\n"
                            + "                <a class='label'>TITLE</a>\n"
                            + "                <a class='value'>" + title + "</a>\n"
                            + "              </div>\n";
                }

                if (description != null) {
                    output += "      <div class='item'>\n"
                            + "                <a class='label'>DESCRIPTION</a>\n"
                            + "                <a class='value'>" + description + "</a>\n"
                            + "              </div>\n";
                }

                output += "          </div>";
            }

            output += "            </div>  \n";
        }

        servlet.putInJsp("result", output);
    }

    public void addCreateSession(String classId, String date, String time) {
        // Get class results
        List<Models.Class> classList = em.createNativeQuery("select c.* from class c, classparticipant cpa, participant p where c.classid = ? and c.classid = cpa.classid and cpa.participantid = p.participantid and p.userid = ? and cpa.role='teacher'", Models.Class.class).setParameter(1, classId).setParameter(2, user.getUserid()).getResultList();

        // To hold output
        String output = "";

        // If empty
        if (classList.size() == 0) {
            replyChat("Seems like this class doesn't exist");
            return;
        }

        // A result for each
        for (Models.Class classroom : classList) {
            String dateHref = date == null ? "" : date.trim().isEmpty() ? "" : date;
            String timeHref = time == null ? "" : time.trim().isEmpty() ? "" : time;

            // For displaying
            DateTimeFormatter dateFmt = DateTimeFormat.forPattern("dd MMM YYYY");
            String dateDisplay = new DateTime(date).toString(dateFmt);

            output = "  <div class='result action' onclick='window.location.href=\"AddSession?id=" + classId + "&date=" + dateHref + "\"'>\n"
                    + "            <div class='top'>\n"
                    + "              <img class='icon' src='https://www.flaticon.com/svg/static/icons/svg/3324/3324859.svg'>\n"
                    + "              <div class='text'>\n"
                    + "                <a class='type'>ACTION</a>\n"
                    + "                <a class='desc'>Create a session for <span>" + classId + "</span></a>\n"
                    + "              </div>\n"
                    + "            </div>\n";

            if (date != null || time != null) {
                output += "     <div class='bottom'>\n"
                        + "              <a class='desc'>With the following details:</a>\n";

                if (date != null) {
                    output += "      <div class='item'>\n"
                            + "                <a class='label'>DATE</a>\n"
                            + "                <a class='value'>" + dateDisplay + "</a>\n"
                            + "              </div>\n";
                }

                if (time != null) {
                    output += "      <div class='item'>\n"
                            + "                <a class='label'>DESCRIPTION</a>\n"
                            + "                <a class='value'>" + time + "</a>\n"
                            + "              </div>\n";
                }

                output += "          </div>";
            }

            output += "            </div>  \n";
        }

        servlet.putInJsp("result", output);
    }

    // ---- QUERY  PARSING BELOW ----
    public void input(String input) {
        // Lowercase the first letter
        // Thanks to Rekin @
        // https://stackoverflow.com/questions/3904579/how-to-capitalize-the-first-letter-of-a-string-in-java
        input = input.substring(0, 1).toLowerCase() + input.substring(1);

        // Check intention
        // If retrieve
        if (input.matches(retrieveCommand)) {
            retrieve(input);
        } // If asking a question
        else if (input.matches(questions)) {
            questions(input);
        } else if (input.matches(createEducation)) {
            createEducation(input);
        } else if (input.matches(createClassObjects)) {
            createClassObjects(input);
        } else {
            replyChat("Sorry, I don't understand.");
        }
    }

    // Create stuff in class (TEACHER ONLY!)
    public void createClassObjects(String input) {
        // Find target
        if (input.matches(".* (announcement|session|assignment) for.*")) {
            String target = substr(input, "for (\\S*)\\s?");

            // Obtain the class here
            // If no target is found
            if (target == null || target == "") {
                System.out.println("No class found");
                replyChat("This class does not exist.");
                return;
            }

            if (input.matches(".*(announcement).*")) {
                System.out.println("Create an announcement for " + target);
            } else if (input.matches(".*(session).*")) {
                System.out.println("Create a session for " + target);

                // Get optional date
                input = input.trim();
                String dateInput = substr(input, ".*on (.*)");
                dateInput = dateInput != null ? dateInput.trim() : "";
                String date = "";
                boolean processDate = true;
                System.out.println(dateInput + "LOL");
                try {
                    // Convert date to a string that can be read by <input> tags
                    DateTimeFormatter dateFmt = DateTimeFormat.forPattern("YYYY-MM-dd");
                    date = new DateTime(Quick.getDate(dateInput)).toString(dateFmt);
                } catch (Exception e) {
                    System.out.println("Invalid date");
                    processDate = false;
                }

                if (processDate) {
                    addCreateSession(target, date, null);
                } else {
                    // No date
                    addCreateSession(target, null, null);
                }

                // Get optional time (must be a range)
                // if(input.matches(".*(between|from) (.*) (and|to|until) (.*)"))
            } else if (input.matches(".*(assignment).*")) {

                // Detect ID and name
                String title = substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']") != null && !substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']").trim().isEmpty() ? substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']") : substr2(input, ".*(named|titled|name|title) (\\S*)\\s?");
                String description = substr2(input, ".*(message|description) [\"\']([^\']*)[\"\']") != null && !substr2(input, ".*(message|description) [\"\']([^\']*)[\"\']").trim().isEmpty() ? substr2(input, ".*(message|description) [\"\']([^\']*)[\"\']") : substr2(input, ".*(message|description) (\\S*)\\s?");

                System.out.println(title);

                System.out.println(title.trim().isEmpty());
                // If ID provided
                if (title != null && !title.trim().isEmpty()) {

                    // And name as well
                    if (description != null && !description.trim().isEmpty()) {
                        addCreateAssignment(target, title, description);
                    } else {
                        addCreateAssignment(target, title, null);
                    }
                } // If name only
                else if (description != null && !description.trim().isEmpty()) {
                    addCreateAssignment(target, null, description);
                } else {
                    addCreateAssignment(target, null, null);
                }
            } else {
                System.out.println("Sorry, I don't understand");
                replyChat("This class does not exist.");
            }
        }

    }

    // Create class/course/prog/institution
    public void createEducation(String input) {
        if (input.matches(".* (class).*")) {

            // Detect ID and name
            String id = substr2(input, ".*(id|ID) (\\S*)\\s?");
            String name = substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']") != null && !substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']").trim().isEmpty() ? substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']") : substr2(input, ".*(named|titled|name|title) (\\S*)\\s?");

            // If ID provided
            if (id != null && !id.trim().isEmpty()) {

                // And name as well
                if (name != null && !name.trim().isEmpty()) {
                    addCreateEducationComponent("class", id, name, "AddClass");
                } else {
                    addCreateEducationComponent("class", id, null, "AddClass");
                }
            } // If name only
            else if (name != null && !name.trim().isEmpty()) {
                addCreateEducationComponent("class", null, name, "AddClass");
            } else {
                addCreateEducationComponent("class", null, null, "AddClass");
            }
        } else if (input.matches(".* (course).*")) {
            // Detect ID and name
            String id = substr2(input, ".*(id|ID) (\\S*)\\s?");
            String name = substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']") != null && !substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']").trim().isEmpty() ? substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']") : substr2(input, ".*(named|titled|name|title) (\\S*)\\s?");

            // If ID provided
            if (id != null && !id.trim().isEmpty()) {

                // And name as well
                if (name != null && !name.trim().isEmpty()) {
                    addCreateEducationComponent("course", id, name, "AddCourse");
                } else {
                    addCreateEducationComponent("course", id, null, "AddCourse");
                }
            } // If name only
            else if (name != null && !name.trim().isEmpty()) {
                addCreateEducationComponent("course", null, name, "AddCourse");
            } else {
                addCreateEducationComponent("course", null, null, "AddCourse");
            }
        } else if (input.matches(".* (programme).*")) {
            // Detect ID and name
            String id = substr2(input, ".*(id|ID) (\\S*)\\s?");
            String name = substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']") != null && !substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']").trim().isEmpty() ? substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']") : substr2(input, ".*(named|titled|name|title) (\\S*)\\s?");

            // If ID provided
            if (id != null && !id.trim().isEmpty()) {

                // And name as well
                if (name != null && !name.trim().isEmpty()) {
                    addCreateEducationComponent("programme", id, name, "AddProgramme");
                } else {
                    addCreateEducationComponent("programme", id, null, "AddProgramme");
                }
            } // If name only
            else if (name != null && !name.trim().isEmpty()) {
                addCreateEducationComponent("programme", null, name, "AddProgramme");
            } else {
                addCreateEducationComponent("programme", null, null, "AddProgramme");
            }
        } else if (input.matches(".* (institution).*")) {
            // Detect ID and name
            String id = substr2(input, ".*(id|ID) (\\S*)\\s?");
            String name = substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']") != null && !substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']").trim().isEmpty() ? substr2(input, ".*(named|titled|name|title) [\"\']([^\']*)[\"\']") : substr2(input, ".*(named|titled|name|title) (\\S*)\\s?");

            // If ID provided
            if (id != null && !id.trim().isEmpty()) {

                // And name as well
                if (name != null && !name.trim().isEmpty()) {
                    addCreateEducationComponent("institution", id, name, "AddInstitution");
                } else {
                    addCreateEducationComponent("institution", id, null, "AddInstitution");
                }
            } // If name only
            else if (name != null && !name.trim().isEmpty()) {
                addCreateEducationComponent("institution", null, name, "AddInstitution");
            } else {
                addCreateEducationComponent("institution", null, null, "AddInstitution");
            }
        } else {
            System.out.println("Unknown command");
        }
    }

    // Asking questions
    public void questions(String input) {
        // If how many...
        if (input.matches("(how many).*")) {

            // How many classes?
            if (input.matches(".*(classes).*")) {
                if (input.matches(".*today.*")) {
                    System.out.println("5 classes today.");
                } else {
                    System.out.println("You have 50 classes in total");
                }
            } else if (input.matches(".*(courses).*")) {
                System.out.println("You have 10 courses in total");
            } else if (input.matches(".*(programmes).*")) {
                System.out.println("You are active in 2 programmes");
            } else if (input.matches(".*(institutions).*")) {
                System.out.println("You are a student in 3 institutions.");
            } else {
                System.out.println("That is indeed a good question");
            }
        } // What time is it
        else if (input.matches("what (time|is the time|time is it)")) {
            System.out.println("It is currently " + new Date());
        } else {
            System.out.println("That is indeed a good question");
        }
    }

    // Retrieving data
    public void retrieve(String input) {

        // If retrieve all
        if (input.matches(retrieveCommand + " (classes|courses|programmes|institutions).*")) {

            // if classes
            if (input.matches(".*(classes).*")) {

                // If a "from" is provided
                if (input.matches(".*(from|in).*")) {

                    // Check for both FROM xx and IN xx
                    String from = substr(input, "from (.*)") == "" ? substr(input, "in (.*)") : substr(input, "from (.*)");

                    showClasses(from);
                } else {
                    showClasses();
                }

            } // if courses
            else if (input.matches(".*(courses).*")) {
                // If a "from" is provided
                if (input.matches(".*(from|in).*")) {

                    // Check for both FROM xx and IN xx
                    String from = substr(input, "from (.*)") == "" ? substr(input, "in (.*)") : substr(input, "from (.*)");

                    showCourses(from);
                } else {
                    showCourses();
                }

            } // if programmes
            else if (input.matches(".*(programmes).*")) {
                // If a "from" is provided
                if (input.matches(".*(from|in).*")) {

                    // Check for both FROM xx and IN xx
                    String from = substr(input, "from (.*)") == "" ? substr(input, "in (.*)") : substr(input, "from (.*)");
                    showProgrammes(from);
                } else {
                    showProgrammes();
                }

            } // if institutions
            else if (input.matches(".*(institutions).*")) {
                showInstitutions();
            } else {
                System.out.println("error");
            }
        } // If retrieve singular
        else if (input.matches("(show me|give me|display|show|get).*")) {
            // Get singular target
            String target = substr2(input, "(class|course|programme|institution) (\\S*)");

            // If empty, swap position (eg. class LL222 > LL222 class)
            target = substr(input, "(\\S*) (class|course|programme|institution)");

            // target cannot be null
            if (target == "") {
                replyChat("Sorry, I don't understand");
                return;
            }

            // if class
            if (input.matches(".*(class).*")) {
                getSingleClass(target);
            } // if course
            else if (input.matches(".*(course).*")) {
                getSingleCourse(target);
            } // if programme
            else if (input.matches(".*(programme).*")) {
                getSingleProgramme(target);
            } // if institution
            else if (input.matches(".*(institution).*")) {
                getSingleInstitution(target);
            } else {
                replyChat("Sorry, please specify what you want to find");
            }
        } else {
            System.out.println("error");
        }
    }

    // ----- UTIL METHODS BELOW -----
    public String substr(String input, String pattern) {
        Matcher m = Pattern.compile(pattern).matcher(input);
        if (m.find()) {
            return m.group(1);
        } else {
            return "";
        }
    }

    public String substr2(String input, String pattern) {
        Matcher m = Pattern.compile(pattern).matcher(input);
        if (m.find()) {
            return m.group(2);
        } else {
            return "";
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
