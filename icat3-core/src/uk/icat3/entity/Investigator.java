/*
 * Investigator.java
 *
 * Created on 08 February 2007, 10:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.icat3.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class Investigator
 * 
 * @author gjd37
 */
@Entity
@Table(name = "INVESTIGATOR")
@NamedQueries( {
        @NamedQuery(name = "Investigator.findByInvestigationId", query = "SELECT i FROM Investigator i WHERE i.investigatorPK.investigationId = :investigationId"),
        @NamedQuery(name = "Investigator.findByFacilityUserId", query = "SELECT i FROM Investigator i WHERE i.investigatorPK.facilityUserId = :facilityUserId"),
        @NamedQuery(name = "Investigator.findByModTime", query = "SELECT i FROM Investigator i WHERE i.modTime = :modTime"),
        @NamedQuery(name = "Investigator.findByModId", query = "SELECT i FROM Investigator i WHERE i.modId = :modId")
    })
public class Investigator implements Serializable {

    /**
     * EmbeddedId primary key field
     */
    @EmbeddedId
    protected InvestigatorPK investigatorPK;

    @Column(name = "MOD_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modTime;

    @Column(name = "MOD_ID", nullable = false)
    private String modId;

    @JoinColumn(name = "FACILITY_USER_ID", referencedColumnName = "FACILITY_USER_ID", insertable = false, updatable = false)
    @ManyToOne
    private FacilityUser facilityUser;

    @JoinColumn(name = "INVESTIGATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private Investigation investigation;
    
    /** Creates a new instance of Investigator */
    public Investigator() {
    }

    /**
     * Creates a new instance of Investigator with the specified values.
     * @param investigatorPK the investigatorPK of the Investigator
     */
    public Investigator(InvestigatorPK investigatorPK) {
        this.investigatorPK = investigatorPK;
    }

    /**
     * Creates a new instance of Investigator with the specified values.
     * @param investigatorPK the investigatorPK of the Investigator
     * @param modTime the modTime of the Investigator
     * @param modId the modId of the Investigator
     */
    public Investigator(InvestigatorPK investigatorPK, Date modTime, String modId) {
        this.investigatorPK = investigatorPK;
        this.modTime = modTime;
        this.modId = modId;
    }

    /**
     * Creates a new instance of InvestigatorPK with the specified values.
     * @param facilityUserId the facilityUserId of the InvestigatorPK
     * @param investigationId the investigationId of the InvestigatorPK
     */
    public Investigator(String facilityUserId, BigInteger investigationId) {
        this.investigatorPK = new InvestigatorPK(facilityUserId, investigationId);
    }

    /**
     * Gets the investigatorPK of this Investigator.
     * @return the investigatorPK
     */
    public InvestigatorPK getInvestigatorPK() {
        return this.investigatorPK;
    }

    /**
     * Sets the investigatorPK of this Investigator to the specified value.
     * @param investigatorPK the new investigatorPK
     */
    public void setInvestigatorPK(InvestigatorPK investigatorPK) {
        this.investigatorPK = investigatorPK;
    }

    /**
     * Gets the modTime of this Investigator.
     * @return the modTime
     */
    public Date getModTime() {
        return this.modTime;
    }

    /**
     * Sets the modTime of this Investigator to the specified value.
     * @param modTime the new modTime
     */
    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }

    /**
     * Gets the modId of this Investigator.
     * @return the modId
     */
    public String getModId() {
        return this.modId;
    }

    /**
     * Sets the modId of this Investigator to the specified value.
     * @param modId the new modId
     */
    public void setModId(String modId) {
        this.modId = modId;
    }

    /**
     * Gets the facilityUser of this Investigator.
     * @return the facilityUser
     */
    public FacilityUser getFacilityUser() {
        return this.facilityUser;
    }

    /**
     * Sets the facilityUser of this Investigator to the specified value.
     * @param facilityUser the new facilityUser
     */
    public void setFacilityUser(FacilityUser facilityUser) {
        this.facilityUser = facilityUser;
    }

    /**
     * Gets the investigation of this Investigator.
     * @return the investigation
     */
    public Investigation getInvestigation() {
        return this.investigation;
    }

    /**
     * Sets the investigation of this Investigator to the specified value.
     * @param investigation the new investigation
     */
    public void setInvestigation(Investigation investigation) {
        this.investigation = investigation;
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.investigatorPK != null ? this.investigatorPK.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this Investigator.  The result is 
     * <code>true</code> if and only if the argument is not null and is a Investigator object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Investigator)) {
            return false;
        }
        Investigator other = (Investigator)object;
        if (this.investigatorPK != other.investigatorPK && (this.investigatorPK == null || !this.investigatorPK.equals(other.investigatorPK))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "uk.icat3.entity.Investigator[investigatorPK=" + investigatorPK + "]";
    }
    
}