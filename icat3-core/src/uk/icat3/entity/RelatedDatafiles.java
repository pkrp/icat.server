/*
 * RelatedDatafiles.java
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
 * Entity class RelatedDatafiles
 * 
 * @author gjd37
 */
@Entity
@Table(name = "RELATED_DATAFILES")
@NamedQueries( {
        @NamedQuery(name = "RelatedDatafiles.findBySourceDatafileId", query = "SELECT r FROM RelatedDatafiles r WHERE r.relatedDatafilesPK.sourceDatafileId = :sourceDatafileId"),
        @NamedQuery(name = "RelatedDatafiles.findByDestDatafileId", query = "SELECT r FROM RelatedDatafiles r WHERE r.relatedDatafilesPK.destDatafileId = :destDatafileId"),
        @NamedQuery(name = "RelatedDatafiles.findByRelation", query = "SELECT r FROM RelatedDatafiles r WHERE r.relation = :relation"),
        @NamedQuery(name = "RelatedDatafiles.findByModTime", query = "SELECT r FROM RelatedDatafiles r WHERE r.modTime = :modTime"),
        @NamedQuery(name = "RelatedDatafiles.findByModId", query = "SELECT r FROM RelatedDatafiles r WHERE r.modId = :modId")
    })
public class RelatedDatafiles implements Serializable {

    /**
     * EmbeddedId primary key field
     */
    @EmbeddedId
    protected RelatedDatafilesPK relatedDatafilesPK;

    @Column(name = "RELATION", nullable = false)
    private String relation;

    @Column(name = "MOD_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modTime;

    @Column(name = "MOD_ID", nullable = false)
    private String modId;

    @JoinColumn(name = "DEST_DATAFILE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private Datafile datafile;

    @JoinColumn(name = "SOURCE_DATAFILE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private Datafile datafile1;
    
    /** Creates a new instance of RelatedDatafiles */
    public RelatedDatafiles() {
    }

    /**
     * Creates a new instance of RelatedDatafiles with the specified values.
     * @param relatedDatafilesPK the relatedDatafilesPK of the RelatedDatafiles
     */
    public RelatedDatafiles(RelatedDatafilesPK relatedDatafilesPK) {
        this.relatedDatafilesPK = relatedDatafilesPK;
    }

    /**
     * Creates a new instance of RelatedDatafiles with the specified values.
     * @param relatedDatafilesPK the relatedDatafilesPK of the RelatedDatafiles
     * @param relation the relation of the RelatedDatafiles
     * @param modTime the modTime of the RelatedDatafiles
     * @param modId the modId of the RelatedDatafiles
     */
    public RelatedDatafiles(RelatedDatafilesPK relatedDatafilesPK, String relation, Date modTime, String modId) {
        this.relatedDatafilesPK = relatedDatafilesPK;
        this.relation = relation;
        this.modTime = modTime;
        this.modId = modId;
    }

    /**
     * Creates a new instance of RelatedDatafilesPK with the specified values.
     * @param destDatafileId the destDatafileId of the RelatedDatafilesPK
     * @param sourceDatafileId the sourceDatafileId of the RelatedDatafilesPK
     */
    public RelatedDatafiles(BigInteger destDatafileId, BigInteger sourceDatafileId) {
        this.relatedDatafilesPK = new RelatedDatafilesPK(destDatafileId, sourceDatafileId);
    }

    /**
     * Gets the relatedDatafilesPK of this RelatedDatafiles.
     * @return the relatedDatafilesPK
     */
    public RelatedDatafilesPK getRelatedDatafilesPK() {
        return this.relatedDatafilesPK;
    }

    /**
     * Sets the relatedDatafilesPK of this RelatedDatafiles to the specified value.
     * @param relatedDatafilesPK the new relatedDatafilesPK
     */
    public void setRelatedDatafilesPK(RelatedDatafilesPK relatedDatafilesPK) {
        this.relatedDatafilesPK = relatedDatafilesPK;
    }

    /**
     * Gets the relation of this RelatedDatafiles.
     * @return the relation
     */
    public String getRelation() {
        return this.relation;
    }

    /**
     * Sets the relation of this RelatedDatafiles to the specified value.
     * @param relation the new relation
     */
    public void setRelation(String relation) {
        this.relation = relation;
    }

    /**
     * Gets the modTime of this RelatedDatafiles.
     * @return the modTime
     */
    public Date getModTime() {
        return this.modTime;
    }

    /**
     * Sets the modTime of this RelatedDatafiles to the specified value.
     * @param modTime the new modTime
     */
    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }

    /**
     * Gets the modId of this RelatedDatafiles.
     * @return the modId
     */
    public String getModId() {
        return this.modId;
    }

    /**
     * Sets the modId of this RelatedDatafiles to the specified value.
     * @param modId the new modId
     */
    public void setModId(String modId) {
        this.modId = modId;
    }

    /**
     * Gets the datafile of this RelatedDatafiles.
     * @return the datafile
     */
    public Datafile getDatafile() {
        return this.datafile;
    }

    /**
     * Sets the datafile of this RelatedDatafiles to the specified value.
     * @param datafile the new datafile
     */
    public void setDatafile(Datafile datafile) {
        this.datafile = datafile;
    }

    /**
     * Gets the datafile1 of this RelatedDatafiles.
     * @return the datafile1
     */
    public Datafile getDatafile1() {
        return this.datafile1;
    }

    /**
     * Sets the datafile1 of this RelatedDatafiles to the specified value.
     * @param datafile1 the new datafile1
     */
    public void setDatafile1(Datafile datafile1) {
        this.datafile1 = datafile1;
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.relatedDatafilesPK != null ? this.relatedDatafilesPK.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this RelatedDatafiles.  The result is 
     * <code>true</code> if and only if the argument is not null and is a RelatedDatafiles object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RelatedDatafiles)) {
            return false;
        }
        RelatedDatafiles other = (RelatedDatafiles)object;
        if (this.relatedDatafilesPK != other.relatedDatafilesPK && (this.relatedDatafilesPK == null || !this.relatedDatafilesPK.equals(other.relatedDatafilesPK))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "uk.icat3.entity.RelatedDatafiles[relatedDatafilesPK=" + relatedDatafilesPK + "]";
    }
    
}