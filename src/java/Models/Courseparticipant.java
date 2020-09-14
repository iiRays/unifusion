/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mast3
 */
@Entity
@Table(name = "COURSEPARTICIPANT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Courseparticipant.findAll", query = "SELECT c FROM Courseparticipant c")
    , @NamedQuery(name = "Courseparticipant.findByCourseparticipantid", query = "SELECT c FROM Courseparticipant c WHERE c.courseparticipantid = :courseparticipantid")
    , @NamedQuery(name = "Courseparticipant.findByCgpa", query = "SELECT c FROM Courseparticipant c WHERE c.cgpa = :cgpa")
    , @NamedQuery(name = "Courseparticipant.findByIscreator", query = "SELECT c FROM Courseparticipant c WHERE c.iscreator = :iscreator")})
public class Courseparticipant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "COURSEPARTICIPANTID")
    private String courseparticipantid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CGPA")
    private BigDecimal cgpa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ISCREATOR")
    private Boolean iscreator;
    @JoinColumn(name = "COURSECODE", referencedColumnName = "COURSECODE")
    @ManyToOne
    private Course coursecode;
    @JoinColumn(name = "PARTICIPANTID", referencedColumnName = "PARTICIPANTID")
    @ManyToOne
    private Participant participantid;

    public Courseparticipant() {
    }

    public Courseparticipant(String courseparticipantid) {
        this.courseparticipantid = courseparticipantid;
    }

    public Courseparticipant(String courseparticipantid, Boolean iscreator) {
        this.courseparticipantid = courseparticipantid;
        this.iscreator = iscreator;
    }

    public String getCourseparticipantid() {
        return courseparticipantid;
    }

    public void setCourseparticipantid(String courseparticipantid) {
        this.courseparticipantid = courseparticipantid;
    }

    public BigDecimal getCgpa() {
        return cgpa;
    }

    public void setCgpa(BigDecimal cgpa) {
        this.cgpa = cgpa;
    }

    public Boolean getIscreator() {
        return iscreator;
    }

    public void setIscreator(Boolean iscreator) {
        this.iscreator = iscreator;
    }

    public Course getCoursecode() {
        return coursecode;
    }

    public void setCoursecode(Course coursecode) {
        this.coursecode = coursecode;
    }

    public Participant getParticipantid() {
        return participantid;
    }

    public void setParticipantid(Participant participantid) {
        this.participantid = participantid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (courseparticipantid != null ? courseparticipantid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Courseparticipant)) {
            return false;
        }
        Courseparticipant other = (Courseparticipant) object;
        if ((this.courseparticipantid == null && other.courseparticipantid != null) || (this.courseparticipantid != null && !this.courseparticipantid.equals(other.courseparticipantid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Models.Courseparticipant[ courseparticipantid=" + courseparticipantid + " ]";
    }
    
}
