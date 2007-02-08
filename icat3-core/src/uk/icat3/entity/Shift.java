/*
 * Shift.java
 *
 * Created on 08 February 2007, 09:48
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
 * Entity class Shift
 * 
 * @author gjd37
 */
@Entity
@Table(name = "SHIFT")
@NamedQueries( {
        @NamedQuery(name = "Shift.findByInvestigationId", query = "SELECT s FROM Shift s WHERE s.shiftPK.investigationId = :investigationId"),
        @NamedQuery(name = "Shift.findByStartDate", query = "SELECT s FROM Shift s WHERE s.shiftPK.startDate = :startDate"),
        @NamedQuery(name = "Shift.findByEndDate", query = "SELECT s FROM Shift s WHERE s.shiftPK.endDate = :endDate"),
        @NamedQuery(name = "Shift.findByShiftComment", query = "SELECT s FROM Shift s WHERE s.shiftComment = :shiftComment"),
        @NamedQuery(name = "Shift.findByModId", query = "SELECT s FROM Shift s WHERE s.modId = :modId"),
        @NamedQuery(name = "Shift.findByModTime", query = "SELECT s FROM Shift s WHERE s.modTime = :modTime")
    })
public class Shift extends EntityBaseBean implements Serializable {

    /**
     * EmbeddedId primary key field
     */
    @EmbeddedId
    protected ShiftPK shiftPK;

    @Column(name = "SHIFT_COMMENT")
    private String shiftComment;

    @Column(name = "MOD_ID", nullable = false)
    private String modId;

    @Column(name = "MOD_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modTime;

    @JoinColumn(name = "INVESTIGATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private Investigation investigation;
    
    /** Creates a new instance of Shift */
    public Shift() {
    }

    /**
     * Creates a new instance of Shift with the specified values.
     * @param shiftPK the shiftPK of the Shift
     */
    public Shift(ShiftPK shiftPK) {
        this.shiftPK = shiftPK;
    }

    /**
     * Creates a new instance of Shift with the specified values.
     * @param shiftPK the shiftPK of the Shift
     * @param modId the modId of the Shift
     * @param modTime the modTime of the Shift
     */
    public Shift(ShiftPK shiftPK, String modId, Date modTime) {
        this.shiftPK = shiftPK;
        this.modId = modId;
        this.modTime = modTime;
    }

    /**
     * Creates a new instance of ShiftPK with the specified values.
     * @param endDate the endDate of the ShiftPK
     * @param startDate the startDate of the ShiftPK
     * @param investigationId the investigationId of the ShiftPK
     */
    public Shift(Date endDate, Date startDate, BigInteger investigationId) {
        this.shiftPK = new ShiftPK(endDate, startDate, investigationId);
    }

    /**
     * Gets the shiftPK of this Shift.
     * @return the shiftPK
     */
    public ShiftPK getShiftPK() {
        return this.shiftPK;
    }

    /**
     * Sets the shiftPK of this Shift to the specified value.
     * @param shiftPK the new shiftPK
     */
    public void setShiftPK(ShiftPK shiftPK) {
        this.shiftPK = shiftPK;
    }

    /**
     * Gets the shiftComment of this Shift.
     * @return the shiftComment
     */
    public String getShiftComment() {
        return this.shiftComment;
    }

    /**
     * Sets the shiftComment of this Shift to the specified value.
     * @param shiftComment the new shiftComment
     */
    public void setShiftComment(String shiftComment) {
        this.shiftComment = shiftComment;
    }

    /**
     * Gets the modId of this Shift.
     * @return the modId
     */
    public String getModId() {
        return this.modId;
    }

    /**
     * Sets the modId of this Shift to the specified value.
     * @param modId the new modId
     */
    public void setModId(String modId) {
        this.modId = modId;
    }

    /**
     * Gets the modTime of this Shift.
     * @return the modTime
     */
    public Date getModTime() {
        return this.modTime;
    }

    /**
     * Sets the modTime of this Shift to the specified value.
     * @param modTime the new modTime
     */
    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }

    /**
     * Gets the investigation of this Shift.
     * @return the investigation
     */
    public Investigation getInvestigation() {
        return this.investigation;
    }

    /**
     * Sets the investigation of this Shift to the specified value.
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
        hash += (this.shiftPK != null ? this.shiftPK.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this Shift.  The result is 
     * <code>true</code> if and only if the argument is not null and is a Shift object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Shift)) {
            return false;
        }
        Shift other = (Shift)object;
        if (this.shiftPK != other.shiftPK && (this.shiftPK == null || !this.shiftPK.equals(other.shiftPK))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "uk.icat3.entity.Shift[shiftPK=" + shiftPK + "]";
    }
    
}