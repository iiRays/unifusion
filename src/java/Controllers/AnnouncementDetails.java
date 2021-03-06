/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Course;
import Models.Institution;
import Models.Programme;
import Util.Quick;
import Util.Server;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

/**
 *
 * @author mast3
 */
@WebServlet(name = "AnnouncementDetails", urlPatterns = {"/AnnouncementDetails"})
public class AnnouncementDetails extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    private UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Util objects
        Util.Servlet servlet = new Util.Servlet(request, response);
        Util.DB db = new Util.DB(em, utx);
        Models.Users user = Server.getUser(request, response);

        // Announcement object
        Models.Announcement announcement = new Models.Announcement();

        // Get query strings
        String type = servlet.getQueryStr("type");
        String id = servlet.getQueryStr("id");
        String code = servlet.getQueryStr("code"); // announcement id

        Query query;

        try {
            if ("class".equalsIgnoreCase(type)) {
                // Get the class
                Models.Class classroom = (Models.Class) em.createNativeQuery("select c.* from class c, classparticipant cpa, participant p where c.classid = ? and cpa.classid = c.classid and cpa.participantid = p.participantid and p.userid = ?", Models.Class.class).setParameter(1, id).setParameter(2, user.getUserid()).getSingleResult();

                // Get the announcement
                announcement = (Models.Announcement) em.createNativeQuery("select a.* from announcement a where a.classid = ? and a.announcementid = ?", Models.Announcement.class).setParameter(1, classroom.getClassid()).setParameter(2, code).getSingleResult();

                // If the user is  the poster
                if (user.getUserid().equals(announcement.getPosterid().getUserid().getUserid())) {
                    // Show button
                    String button = "<div id='buttons'>\n"
                            + "                    <input id='edit-button' class='button' type='submit' value='Edit' onclick='window.location.href=\"EditAnnouncement?id=" + id + "&code=" + code + "&type=" + type +"\"'>\n"
                            + "                    <input id='remove-button' class='button' type='submit' value='Delete' onclick='window.location.href=\"PerformDeleteAnnouncement?id=" + id + "&code=" + code + "&type=" + type +"\"'>\n"
                            + "                </div>";
                    
                    servlet.putInJsp("button", button);
                }


                // Put into JSP
                servlet.putInJsp("subheading", classroom.getClassid() + " - " + classroom.getClasstitle() + " (Class)");
                servlet.putInJsp("icon", Quick.getIcon(classroom.getIconurl()));
                servlet.putInJsp("id", classroom.getClassid());
                servlet.putInJsp("type", "class");
                servlet.putInJsp("announcement", announcement);

            } else if ("course".equalsIgnoreCase(type)) {
                // Get the course
                Models.Course course = (Models.Course) em.createNativeQuery("select c.* from course c, courseparticipant cpa, participant p where c.coursecode = ? and cpa.coursecode = c.coursecode and cpa.participantid = p.participantid and p.userid = ?", Course.class).setParameter(1, id).setParameter(2, user.getUserid()).getSingleResult();

                // Get the announcement
                announcement = (Models.Announcement) em.createNativeQuery("select a.* from announcement a where a.coursecode = ? and a.announcementid = ?", Models.Announcement.class).setParameter(1, course.getCoursecode()).setParameter(2, code).getSingleResult();

                 // If the user is  the poster
                if (user.getUserid().equals(announcement.getPosterid().getUserid().getUserid())) {
                    // Show button
                    String button = "<div id='buttons'>\n"
                            + "                    <input id='edit-button' class='button' type='submit' value='Edit' onclick='window.location.href=\"EditAnnouncement?id=" + id + "&code=" + code + "&type=" + type +"\"'>\n"
                            + "                    <input id='remove-button' class='button' type='submit' value='Delete' onclick='window.location.href=\"PerformDeleteAnnouncement?id=" + id + "&code=" + code + "&type=" + type +"\"'>\n"
                            + "                </div>";
                    
                    servlet.putInJsp("button", button);
                }


                // Put into JSP
                servlet.putInJsp("subheading", course.getCoursecode() + " - " + course.getTitle() + " (Course)");
                servlet.putInJsp("icon", Quick.getIcon(course.getIconurl()));
                servlet.putInJsp("id", course.getCoursecode());
                servlet.putInJsp("type", "course");
                servlet.putInJsp("announcement", announcement);

            } else if ("programme".equalsIgnoreCase(type)) {
                // Get the programme
                Models.Programme programme = (Models.Programme) em.createNativeQuery("select pg.* from programme pg, programmeparticipant ppa, participant p where pg.programmecode = ? and ppa.programmecode = pg.programmecode and ppa.participantid = p.participantid and p.userid = ?", Programme.class).setParameter(1, id).setParameter(2, user.getUserid()).getSingleResult();

                // Get the announcement
                announcement = (Models.Announcement) em.createNativeQuery("select a.* from announcement a where a.programmecode = ? and a.announcementid = ?", Models.Announcement.class).setParameter(1, programme.getProgrammecode()).setParameter(2, code).getSingleResult();

                  // If the user is  the poster
                if (user.getUserid().equals(announcement.getPosterid().getUserid().getUserid())) {
                    // Show button
                    String button = "<div id='buttons'>\n"
                            + "                    <input id='edit-button' class='button' type='submit' value='Edit' onclick='window.location.href=\"EditAnnouncement?id=" + id + "&code=" + code + "&type=" + type +"\"'>\n"
                            + "                    <input id='remove-button' class='button' type='submit' value='Delete' onclick='window.location.href=\"PerformDeleteAnnouncement?id=" + id + "&code=" + code + "&type=" + type +"\"'>\n"
                            + "                </div>";
                    
                    servlet.putInJsp("button", button);
                }


                // Put into JSP
                servlet.putInJsp("subheading", programme.getProgrammecode() + " - " + programme.getTitle() + " (Programme)");
                servlet.putInJsp("icon", Quick.getIcon(programme.getIconurl()));
                servlet.putInJsp("id", programme.getProgrammecode());
                servlet.putInJsp("type", "programme");
                servlet.putInJsp("announcement", announcement);

            } else if ("institution".equalsIgnoreCase(type)) {
                // Get the course
                Models.Institution institution = (Models.Institution) em.createNativeQuery("select i.* from institution i, institutionparticipant ipa, participant p where i.institutioncode = ? and ipa.institutioncode = i.institutioncode and ipa.participantid = p.participantid and p.userid = ?", Institution.class).setParameter(1, id).setParameter(2, user.getUserid()).getSingleResult();

                // Get the announcement
                announcement = (Models.Announcement) em.createNativeQuery("select a.* from announcement a where a.institutioncode = ? and a.announcementid = ?", Models.Announcement.class).setParameter(1, institution.getInstitutioncode()).setParameter(2, code).getSingleResult();

                // If the user is  the poster
                if (user.getUserid().equals(announcement.getPosterid().getUserid().getUserid())) {
                    // Show button
                    String button = "<div id='buttons'>\n"
                            + "                    <input id='edit-button' class='button' type='submit' value='Edit' onclick='window.location.href=\"EditAnnouncement?id=" + id + "&code=" + code + "&type=" + type +"\"'>\n"
                            + "                    <input id='remove-button' class='button' type='submit' value='Delete' onclick='window.location.href=\"PerformDeleteAnnouncement?id=" + id + "&code=" + code + "&type=" + type +"\"'>\n"
                            + "                </div>";
                    
                    servlet.putInJsp("button", button);
                }

                // Put into JSP
                servlet.putInJsp("subheading", institution.getInstitutioncode() + " - " + institution.getName() + " (Institution)");
                servlet.putInJsp("icon", Quick.getIcon(institution.getIconurl()));
                servlet.putInJsp("id", institution.getInstitutioncode());
                servlet.putInJsp("type", "institution");
                servlet.putInJsp("announcement", announcement);

            } else {
                // Incorrect type
                System.out.println("Type is incorrect");
                servlet.toServlet("Dashboard");
                return;
            }

            String fileStr = "";

            // Get each file
            if (announcement.getFileurl() != null) {
                String[] files = announcement.getFileurl().split("\\|");

                for (String file : files) {
                    String name = file.substring(file.indexOf("\\") + 1, file.length());

                    fileStr += "<div class='attachment'>\n"
                            + "            <img class='icon' src='https://icons.iconarchive.com/icons/pelfusion/flat-file-type/512/doc-icon.png'>\n"
                            + "            <a href='AnnouncementFile?type=" + type + "&id=" + id + "&code=" + code + "&file=" + file + "' class='name'>" + name + "</a>\n"
                            + "          </div>";
                }

                servlet.putInJsp("fileStr", fileStr);
            }

        } catch (NoResultException e) {
            System.out.println("No data found");
            servlet.toServlet("Dashboard");
            return;
        }
        servlet.servletToJsp("announcementDetails.jsp");
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
