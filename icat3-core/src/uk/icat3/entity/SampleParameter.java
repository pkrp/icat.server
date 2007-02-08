/*
 * SampleParameter.java
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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class SampleParameter
 * 
 * @author gjd37
 */
@Entity
@Table(name = "SAMPLE_PARAMETER")
@NamedQueries( {
        @NamedQuery(name = "SampleParameter.findBySampleId", query = "SELECT s FROM SampleParameter s WHERE s.sampleParameterPK.sampleId = :sampleId"),
        @NamedQuery(name = "SampleParameter.findByName", query = "SELECT s FROM SampleParameter s WHERE s.sampleParameterPK.name = :name"),
        @NamedQuery(name = "SampleParameter.findByUnits", query = "SELECT s FROM SampleParameter s WHERE s.sampleParameterPK.units = :units"),
        @NamedQuery(name = "SampleParameter.findByStringValue", query = "SELECT s FROM SampleParameter s WHERE s.stringValue = :stringValue"),
        @NamedQuery(name = "SampleParameter.findByNumericValue", query = "SELECT s FROM SampleParameter s WHERE s.numericValue = :numericValue"),
        @NamedQuery(name = "SampleParameter.findByError", query = "SELECT s FROM SampleParameter s WHERE s.error = :error"),
        @NamedQuery(name = "SampleParameter.findByRangeTop", query = "SELECT s FROM SampleParameter s WHERE s.rangeTop = :rangeTop"),
        @NamedQuery(name = "SampleParameter.findByRangeBottom", query = "SELECT s FROM SampleParameter s WHERE s.rangeBottom = :rangeBottom"),
        @NamedQuery(name = "SampleParameter.findByModTime", query = "SELECT s FROM SampleParameter s WHERE s.modTime = :modTime"),
        @NamedQuery(name = "SampleParameter.findByModId", query = "SELECT s FROM SampleParameter s WHERE s.modId = :modId")
    })
public class SampleParameter implements Serializable {

    /**
     * EmbeddedId primary key field
     */
    @EmbeddedId
    protected SampleParameterPK sampleParameterPK;

    @Column(name = "STRING_VALUE")
    private String stringValue;

    @Column(name = "NUMERIC_VALUE")
    private Double numericValue;

    @Column(name = "ERROR")
    private String error;

    @Column(name = "RANGE_TOP")
    private String rangeTop;

    @Column(name = "RANGE_BOTTOM")
    private String rangeBottom;

    @Column(name = "MOD_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modTime;

    @Column(name = "MOD_ID", nullable = false)
    private String modId;

    @JoinColumns(value =  {
            @JoinColumn(name = "NAME", referencedColumnName = "NAME", insertable = false, updatable = false),
            @JoinColumn(name = "UNITS", referencedColumnName = "UNITS", insertable = false, updatable = false)
        })
    @ManyToOne
    private Parameter parameter;

    @JoinColumn(name = "SAMPLE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private Sample sample;
    
    /** Creates a new instance of SampleParameter */
    public SampleParameter() {
    }

    /**
     * Creates a new instance of SampleParameter with the specified values.
     * @param sampleParameterPK the sampleParameterPK of the SampleParameter
     */
    public SampleParameter(SampleParameterPK sampleParameterPK) {
        this.sampleParameterPK = sampleParameterPK;
    }

    /**
     * Creates a new instance of SampleParameter with the specified values.
     * @param sampleParameterPK the sampleParameterPK of the SampleParameter
     * @param modTime the modTime of the SampleParameter
     * @param modId the modId of the SampleParameter
     */
    public SampleParameter(SampleParameterPK sampleParameterPK, Date modTime, String modId) {
        this.sampleParameterPK = sampleParameterPK;
        this.modTime = modTime;
        this.modId = modId;
    }

    /**
     * Creates a new instance of SampleParameterPK with the specified values.
     * @param units the units of the SampleParameterPK
     * @param name the name of the SampleParameterPK
     * @param sampleId the sampleId of the SampleParameterPK
     */
    public SampleParameter(String units, String name, BigInteger sampleId) {
        this.sampleParameterPK = new SampleParameterPK(units, name, sampleId);
    }

    /**
     * Gets the sampleParameterPK of this SampleParameter.
     * @return the sampleParameterPK
     */
    public SampleParameterPK getSampleParameterPK() {
        return this.sampleParameterPK;
    }

    /**
     * Sets the sampleParameterPK of this SampleParameter to the specified value.
     * @param sampleParameterPK the new sampleParameterPK
     */
    public void setSampleParameterPK(SampleParameterPK sampleParameterPK) {
        this.sampleParameterPK = sampleParameterPK;
    }

    /**
     * Gets the stringValue of this SampleParameter.
     * @return the stringValue
     */
    public String getStringValue() {
        return this.stringValue;
    }

    /**
     * Sets the stringValue of this SampleParameter to the specified value.
     * @param stringValue the new stringValue
     */
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * Gets the numericValue of this SampleParameter.
     * @return the numericValue
     */
    public Double getNumericValue() {
        return this.numericValue;
    }

    /**
     * Sets the numericValue of this SampleParameter to the specified value.
     * @param numericValue the new numericValue
     */
    public void setNumericValue(Double numericValue) {
        this.numericValue = numericValue;
    }

    /**
     * Gets the error of this SampleParameter.
     * @return the error
     */
    public String getError() {
        return this.error;
    }

    /**
     * Sets the error of this SampleParameter to the specified value.
     * @param error the new error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Gets the rangeTop of this SampleParameter.
     * @return the rangeTop
     */
    public String getRangeTop() {
        return this.rangeTop;
    }

    /**
     * Sets the rangeTop of this SampleParameter to the specified value.
     * @param rangeTop the new rangeTop
     */
    public void setRangeTop(String rangeTop) {
        this.rangeTop = rangeTop;
    }

    /**
     * Gets the rangeBottom of this SampleParameter.
     * @return the rangeBottom
     */
    public String getRangeBottom() {
        return this.rangeBottom;
    }

    /**
     * Sets the rangeBottom of this SampleParameter to the specified value.
     * @param rangeBottom the new rangeBottom
     */
    public void setRangeBottom(String rangeBottom) {
        this.rangeBottom = rangeBottom;
    }

    /**
     * Gets the modTime of this SampleParameter.
     * @return the modTime
     */
    public Date getModTime() {
        return this.modTime;
    }

    /**
     * Sets the modTime of this SampleParameter to the specified value.
     * @param modTime the new modTime
     */
    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }

    /**
     * Gets the modId of this SampleParameter.
     * @return the modId
     */
    public String getModId() {
        return this.modId;
    }

    /**
     * Sets the modId of this SampleParameter to the specified value.
     * @param modId the new modId
     */
    public void setModId(String modId) {
        this.modId = modId;
    }

    /**
     * Gets the parameter of this SampleParameter.
     * @return the parameter
     */
    public Parameter getParameter() {
        return this.parameter;
    }

    /**
     * Sets the parameter of this SampleParameter to the specified value.
     * @param parameter the new parameter
     */
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    /**
     * Gets the sample of this SampleParameter.
     * @return the sample
     */
    public Sample getSample() {
        return this.sample;
    }

    /**
     * Sets the sample of this SampleParameter to the specified value.
     * @param sample the new sample
     */
    public void setSample(Sample sample) {
        this.sample = sample;
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.sampleParameterPK != null ? this.sampleParameterPK.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this SampleParameter.  The result is 
     * <code>true</code> if and only if the argument is not null and is a SampleParameter object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SampleParameter)) {
            return false;
        }
        SampleParameter other = (SampleParameter)object;
        if (this.sampleParameterPK != other.sampleParameterPK && (this.sampleParameterPK == null || !this.sampleParameterPK.equals(other.sampleParameterPK))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "uk.icat3.entity.SampleParameter[sampleParameterPK=" + sampleParameterPK + "]";
    }
    
}