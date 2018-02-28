/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.directory;

import com.artigile.warehouse.utils.ModelFieldsLengths;

import javax.persistence.*;

@Entity
public class MeasureUnit {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Unique identification number of measure unit in global directory.
     */
    @Column(unique = true, updatable = false, length = ModelFieldsLengths.UID_LENGTH)
    private String uidMeasureUnit;

    /**
     * Short name of the measurement unit.
     */
    @Column(nullable = false, unique = true)
    private String sign;

    /**
     * Full name of the measure unit.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * code of the measure unit.
     */
    @Column(unique = true)
    private String code;

    /**
     * Notice about measure unit.
     */
    private String notice;

    /**
     * If true, measure unit is default (and should me selected by default in lists of measures).
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean defaultMeasureUnit;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //========================================= Getters and setters =====================================
    public MeasureUnit() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUidMeasureUnit() {
        return uidMeasureUnit;
    }

    public void setUidMeasureUnit(String uidMeasureUnit) {
        this.uidMeasureUnit = uidMeasureUnit;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public boolean isDefaultMeasureUnit() {
        return defaultMeasureUnit;
    }

    public void setDefaultMeasureUnit(boolean defaultMeasureUnit) {
        this.defaultMeasureUnit = defaultMeasureUnit;
    }

    public long getVersion() {
        return version;
    }
}
