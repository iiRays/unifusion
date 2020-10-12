/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Institution;
import Models.Programme;
import Models.Users;
import Util.DB;
import Util.Server;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
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
@WebServlet(name = "Institution", urlPatterns = {"/Institution"})
public class Institutions extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    private UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // Redirect
        Util.Servlet servlet = new Util.Servlet(request ,response);
        DB db = new DB(em, utx);
        
          // Get currentUser
        Users currentUser = Server.getUser(request, response);

        // Get course data
        String institutionCode = servlet.getQueryStr("id");
        Query query = em.createNativeQuery("select i.* from institution i, institutionparticipant ipa, participant p where i.institutioncode = ? and ipa.institutioncode = i.institutioncode and ipa.participantid = p.participantid and p.userid = ?", Institution.class).setParameter(1, institutionCode).setParameter(2, currentUser.getUserid());

          // If no results
        if(query.getResultList().isEmpty()){
            servlet.toServlet("Dashboard");
            return;
        }
        
        Institution institution = (Institution) query.getSingleResult();
        
        if (institution == null) {
            // Programme not found
            System.out.println("INSTITUTION NOT FOUND");
            servlet.toServlet("Dashboard");
        } else {
            // Programme is found

            // Get list of tutors
            ArrayList<Users> tutorList = db.getList(Users.class, em.createNativeQuery("select u.* from institutionparticipant ipa, institution i, users u, participant p where i.institutioncode = ? and ipa.role = 'teacher' and u.userid = p.userid and p.participantid = ipa.participantid and i.institutioncode = ipa.institutioncode", Models.Users.class).setParameter(1, institutionCode));

            // Get list of students
            ArrayList<Users> studentList = db.getList(Users.class, em.createNativeQuery("select u.* from institutionparticipant ipa, institution i, users u, participant p where i.institutioncode = ? and ipa.role = 'student' and u.userid = p.userid and p.participantid = ipa.participantid and i.institutioncode = ipa.institutioncode", Models.Users.class).setParameter(1, institutionCode));

            // Get creator
            Users creator = db.getList(Users.class, em.createNativeQuery("select u.* from institutionparticipant ipa, institution i, users u, participant p where i.institutioncode = ? and ipa.role = 'teacher' and u.userid = p.userid and p.participantid = ipa.participantid and ipa.iscreator = true and i.institutioncode = ipa.institutioncode", Models.Users.class).setParameter(1, institutionCode)).get(0);

            

            // Displaying Members box
            String youBox = "", moreStr = "", editBt = "<a class='more' href='#'>Click to view more ></a>";
            int moreCount = tutorList.size() + studentList.size();

            // if current user == creator
            if (currentUser.getUserid().equals(creator.getUserid())) {
                editBt = "<a class='more' href='InstitutionDetails?institution=" + institutionCode + "'>Click to edit ></a>";
                creator.setName("You");
                moreCount -= 1;
            } else {
                youBox = "<div class='box' id='you'>"
                        + "<img class='icon' src='" + ((Models.Users) request.getSession().getAttribute("user")).getImageurl() + "'>"
                        + "<a class='name'>You</a>"
                        + "</div>";
                moreCount -= 2;
            }

            // Displaying "and xx more..."
            if (moreCount > 0) {
                moreStr = "<a id='noOfMembers'>and " + moreCount + " more...</a>";
            }

            // Put data in JSP
            servlet.putInJsp("institution", institution);
            servlet.putInJsp("tutorList", tutorList);
            servlet.putInJsp("studentList", studentList);
            servlet.putInJsp("youBox", youBox);
            servlet.putInJsp("moreStr", moreStr);
            servlet.putInJsp("creator", creator);
            servlet.putInJsp("editBt", editBt);
        }
        
        servlet.servletToJsp("institution.jsp");
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
