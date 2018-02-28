/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto;

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

public class MeasureUnitTO extends EqualsByIdImpl {

    private long id;

    private String uidMeasureUnit;

    private String sign;

    private String name;

    private String code;

    private String notice;
    
    private boolean defaultMeasureUnit;

    public MeasureUnitTO() {
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

    public boolean getDefaultMeasureUnit() {
        return defaultMeasureUnit;
    }

    public void setDefaultMeasureUnit(boolean defaultMeasureUnit) {
        this.defaultMeasureUnit = defaultMeasureUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MeasureUnitTO that = (MeasureUnitTO) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }


}
