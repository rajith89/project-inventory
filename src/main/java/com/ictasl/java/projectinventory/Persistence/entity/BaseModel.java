package com.ictasl.java.projectinventory.Persistence.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class BaseModel  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "created_date",updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;

    @Column(name = "created_user")
    @CreatedBy
    protected long createdUser;

    @Column(name = "last_update_date")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastUpdateDate;

    @Column(name = "last_update_user")
    @LastModifiedBy
    protected long lastUpdateUser;

    @Column(name = "is_deleted")
    protected boolean isDeleted;


    @PrePersist
    public final void preCreate() {
        createdDate = new Date();
    }

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the createdUser
     */
    public long getCreatedUser() {
        return createdUser;
    }

    /**
     * @param createdUser the createdUser to set
     */
    public void setCreatedUser(long createdUser) {
        this.createdUser = createdUser;
    }

    /**
     * @return the lastUpdateDate
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * @param lastUpdateDate the lastUpdateDate to set
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * @return the lastUpdateUser
     */
    public long getLastUpdateUser() {
        return lastUpdateUser;
    }

    /**
     * @param lastUpdateUser the lastUpdateUser to set
     */
    public void setLastUpdateUser(long lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    /**
     * @return the isDeleted
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted the isDeleted to set
     */
    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
