/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.lock;

import com.artigile.warehouse.dao.generic.lock.LockType;
import com.artigile.warehouse.domain.admin.User;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Valery Barysok, 09.11.2009
 */

/**
 * Represents a group of simultaneously locked objects. For example,
 * a set of of wares at the warehouse.
 * <p> LockGroup hasn't direct link to a list of concrete entities because
 * locking may occur with entity of any type. See triggers and procedures in database
 * to find out, where data about concrete locking are stored.
 * <p>For example, Inventorization during it's processing locks a set of WarehouseBatch entities.
 * To store such information there are a number of tables and triggers are created in the database:
 * <ul>
 *   <li>LockGroup_WarehouseBatch - table with one record per one locked warehouse batch.
 *   <li>LockGroupItem - table with a list of entities, that are available for locking.
 *   <li>LockGroup_Owner_Inventorization - table with lock groups, that are owned by inventorizations.
 *   <li>LockGroupOwner - table with a list of possible lock group owners.
 *   <li>LockGroup -- base table of all lock groups. This table is referenced by concrete locked items
 * (by table LockGroup_WarehouseBatch for example).
 *   <li>tbd_lockable_WarehouseBatch - trigger, that prevents locked warehouse batch from deletion.
 *   <li>tbu_lockable_WarehouseBatch - trigger, that prevents locked warehouse batch from update.
 * </ul>
 */
@Entity
public class LockGroup {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Date and time, when locking has been created.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    /**
     * User, who created locking.
     */
    @ManyToOne(optional = false)
    private User createUser;

    /**
     * Type of the lock established by this lock group.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private LockType lockType;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public LockType getLockType() {
        return lockType;
    }

    public void setLockType(LockType lockType) {
        this.lockType = lockType;
    }

    public long getVersion() {
        return version;
    }
}
