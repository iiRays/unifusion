/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mast3
 */
@Entity
@Table(name = "GRADEDCOMPONENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gradedcomponent.findAll", query = "SELECT g FROM Gradedcomponent g")
    , @NamedQuery(name = "Gradedcomponent.findByComponentid", query = "SELECT g FROM Gradedcomponent g WHERE g.componentid = :componentid")
    , @NamedQuery(name = "Gradedcomponent.findByTitle", query = "SELECT g FROM Gradedcomponent g WHERE g.title = :title")
    , @NamedQuery(name = "Gradedcomponent.findByDetails", query = "SELECT g FROM Gradedcomponent g WHERE g.details = :details")
    , @NamedQuery(name = "Gradedcomponent.findByTotalmarks", query = "SELECT g FROM Gradedcomponent g WHERE g.totalmarks = :totalmarks")
    , @NamedQuery(name = "Gradedcomponent.findByIssueddate", query = "SELECT g FROM Gradedcomponent g WHERE g.issueddate = :issueddate")
    , @NamedQuery(name = "Gradedcomponent.findByDeadline", query = "SELECT g FROM Gradedcomponent g WHERE g.deadline = :deadline")
    , @NamedQuery(name = "Gradedcomponent.findByIstoshowmarksonly", query = "SELECT g FROM Gradedcomponent g WHERE g.istoshowmarksonly = :istoshowmarksonly")
    , @NamedQuery(name = "Gradedcomponent.findByFileurl", query = "SELECT g FROM Gradedcomponent g WHERE g.fileurl = :fileurl")})
public class Gradedcomponent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "COMPONENTID")
    private String componentid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TITLE")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "DETAILS")
    private String details;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTALMARKS")
    private double totalmarks;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ISSUEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueddate;
    @Column(name = "DEADLINE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ISTOSHOWMARKSONLY")
    private Boolean istoshowmarksonly;
    @Size(max = 500)
    @Column(name = "FILEURL")
    private String fileurl;
    @OneToMany(mappedBy = "componentid")
    private Collection<Submission> submissionCollection;
    @JoinColumn(name = "CLASSID", referencedColumnName = "CLASSID")
    @ManyToOne
    private Class classid;

    public Gradedcomponent() {
    }

    public Gradedcomponent(String componentid) {
        this.componentid = componentid;
    }

    public Gradedcomponent(String componentid, String title, String details, double totalmarks, Date issueddate, Boolean istoshowmarksonly) {
        this.componentid = componentid;
        this.title = title;
        this.details = details;
        this.totalmarks = totalmarks;
        this.issueddate = issueddate;
        this.istoshowmarksonly = istoshowmarksonly;
    }

    public String getComponentid() {
        return componentid;
    }

    public void setComponentid(String componentid) {
        this.componentid = componentid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getTotalmarks() {
        return totalmarks;
    }

    public void setTotalmarks(double totalmarks) {
        this.totalmarks = totalmarks;
    }

    public Date getIssueddate() {
        return issueddate;
    }

    public void setIssueddate(Date issueddate) {
        this.issueddate = issueddate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Boolean getIstoshowmarksonly() {
        return istoshowmarksonly;
    }

    public void setIstoshowmarksonly(Boolean istoshowmarksonly) {
        this.istoshowmarksonly = istoshowmarksonly;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    @XmlTransient
    public Collection<Submission> getSubmissionCollection() {
        return submissionCollection;
    }

    public void setSubmissionCollection(Collection<Submission> submissionCollection) {
        this.submissionCollection = submissionCollection;
    }

    public Class getClassid() {
        return classid;
    }

    public void setClassid(Class classid) {
        this.classid = classid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (componentid != null ? componentid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gradedcomponent)) {
            return false;
        }
        Gradedcomponent other = (Gradedcomponent) object;
        if ((this.componentid == null && other.componentid != null) || (this.componentid != null && !this.componentid.equals(other.componentid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Models.Gradedcomponent[ componentid=" + componentid + " ]";
    }
    
}
