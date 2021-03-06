/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Institution;
import Models.Programme;
import Util.DB;
import Util.Server;
import Util.Servlet;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "InstitutionDetails", urlPatterns = {"/InstitutionDetails"})
public class InstitutionDetails extends HttpServlet {

    @PersistenceContext
    EntityManager em;
    
    @Resource
    private UserTransaction utx;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
       // Important utility classes
        Servlet servlet = new Servlet(request, response);
        DB db = new DB(em, utx);
        
        // Get the programme from DB
        String institutionCode = servlet.getQueryStr("institution");
        
        // Get the programme from DB where this person is participating inside it and is a programme leader
        Query query = em.createNativeQuery("select i.* from institution i, institutionparticipant ipa, participant p where p.userid = ? and i.institutioncode = ? and i.institutioncode = ipa.institutioncode and ipa.participantid = p.participantid and p.educatorrole = 'institutionAdmin'", Models.Institution.class);
        query.setParameter(1, Server.getUser(request, response).getUserid());
        query.setParameter(2, institutionCode);
        
        
        // If invalid Institution code
        if(query.getResultList().size() == 0) { 
            System.out.println("Invalid institution code");
           servlet.toServlet("Dashboard");
            return;
        } else{
            Models.Institution institution = (Institution) query.getSingleResult();
            
            // Institution is valid, display data
            servlet.putInJsp("institution", institution);
            servlet.servletToJsp("institutionDetails.jsp");
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
