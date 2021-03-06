/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Gradedcomponent;
import Models.Submission;
import Util.Quick;
import Util.Server;
import Util.Servlet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author mast3
 */
@WebServlet(name = "GradeSubmission", urlPatterns = {"/GradeSubmission"})
public class GradeSubmission extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    private UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Utility objects
        Servlet servlet = new Servlet(request, response);
        Util.DB db = new Util.DB(em, utx);

        // Get class code and assignment id
        String classId = servlet.getQueryStr("code");
        String id = servlet.getQueryStr("id");
        String submissionId = servlet.getQueryStr("submission");
        Models.Users user = Server.getUser(request, response);

        // Objects
        Models.Class classroom = new Models.Class();
        Models.Classparticipant cpa = new Models.Classparticipant();
        Models.Gradedcomponent assignment = new Models.Gradedcomponent();
        Submission submission = new Submission();

        try {
            // Get class participant
            cpa = (Models.Classparticipant) em.createNativeQuery("select cpa.* from classparticipant cpa, participant p where p.userid = ? and p.participantid = cpa.participantid and cpa.classid = ? and cpa.role='teacher'", Models.Classparticipant.class).setParameter(1, user.getUserid()).setParameter(2, classId).getSingleResult();
            classroom = cpa.getClassid();

            // Get assignment
            assignment = (Gradedcomponent) em.createNativeQuery("select * from gradedcomponent where componentid = ? and classid = ?", Models.Gradedcomponent.class).setParameter(1, id).setParameter(2, classId).getSingleResult();

        } catch (Exception ex) {
            ex.printStackTrace();
            servlet.toServlet("Dashboard");
            return;
        }

        // Get assignment
        try {
            // Get the submission 
            submission = (Submission) em.createNativeQuery("select * from submission where componentid = ? and submissionid= ?", Submission.class).setParameter(1, id).setParameter(2, submissionId).getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("No submission found");
            servlet.toServlet("AssignmentDetails?id=" + id + "&code=" + classId);
            return;
        }
        
         // Get time submitted
        DateTimeFormatter dateFmt = DateTimeFormat.forPattern("d MMM YYYY");
        DateTimeFormatter timeFmt = DateTimeFormat.forPattern("h'.'mma");
        String dateSubmitted = new DateTime(submission.getDatesubmitted()).toString(dateFmt);
        String timeSubmitted = new DateTime(submission.getDatesubmitted()).toString(timeFmt);
        
        // Put in JSP
        servlet.putInJsp("id", assignment.getComponentid()); // Component id
        servlet.putInJsp("code", classroom.getClassid()); // Class code
        servlet.putInJsp("submissionId", submission.getSubmissionid()); // Submission ID
        servlet.putInJsp("submission", submission);
        servlet.putInJsp("alreadyGraded", submission.getMarks() != null);
        servlet.putInJsp("assignment", assignment);
        servlet.putInJsp("dateSubmitted", dateSubmitted);
        servlet.putInJsp("timeSubmitted", timeSubmitted);
        servlet.putInJsp("student", submission.getClassparticipantid().getParticipantid().getUserid());
        servlet.putInJsp("studentIcon", Quick.getIcon(submission.getClassparticipantid().getParticipantid().getUserid().getImageurl()));
        servlet.putInJsp("subheading", classroom.getClassid() + " - " + classroom.getClasstitle() + " (Class)");
        servlet.putInJsp("icon", Quick.getIcon(classroom.getIconurl()));
        
        // Redirect
        servlet.servletToJsp("gradeSubmission.jsp");

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
