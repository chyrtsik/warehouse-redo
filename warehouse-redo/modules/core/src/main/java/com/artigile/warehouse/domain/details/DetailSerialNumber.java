/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.details;

import javax.persistence.*;
import java.text.MessageFormat;

/**
 * Serial number to count unique products (from just products issues on different dates and by different people and
 * to products with unique individual serial numbers).
 * @author Aliaksandr Chyrtsik
 * @since 15.06.13
 */
@Entity
public class DetailSerialNumber {
    /**
     * Maximum number of user configured fields.
     */
    public static final int MAX_FIELD_COUNT = 30;

    /**
     * Add user defined field types available in serial numbers.
     */
    public static final DetailFieldType[] AVAILABLE_FIELDS = {
            DetailFieldType.NUMBER,
            DetailFieldType.INTEGER_NUMBER,
            DetailFieldType.BOOLEAN,
            DetailFieldType.TEXT,
            DetailFieldType.DATE,
            DetailFieldType.ENUM,
            DetailFieldType.TEMPLATE_TEXT,
            DetailFieldType.SHELF_LIFE,
            DetailFieldType.COUNT_IN_PACKAGING,
            DetailFieldType.CURRENT_DATE,
            DetailFieldType.CURRENT_TIME,
            DetailFieldType.CURRENT_USER
    };

    /**
     * Retrieve field name by it's index.
     * @param fieldIndex Zero-based index of field.
     * @return field name in database.
     */
    public static String getFieldName(int fieldIndex) {
        if (fieldIndex >= 1 && fieldIndex <= MAX_FIELD_COUNT){
            return "field" + fieldIndex;
        }
        else{
            throw new IllegalArgumentException(MessageFormat.format("DetailSerialNumber.getFieldName. Invalid field index: {0}", fieldIndex));
        }
    }

    @Id
    @GeneratedValue
    private long id;

    /**
     * Product which this serial number is related to.
     */
    @JoinColumn
    @ManyToOne(optional = false)
    private DetailBatch detail;

    /**
     * User configurable fields.
     */
    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String field5;
    private String field6;
    private String field7;
    private String field8;
    private String field9;
    private String field10;
    private String field11;
    private String field12;
    private String field13;
    private String field14;
    private String field15;
    private String field16;
    private String field17;
    private String field18;
    private String field19;
    private String field20;
    private String field21;
    private String field22;
    private String field23;
    private String field24;
    private String field25;
    private String field26;
    private String field27;
    private String field28;
    private String field29;
    private String field30;

    /**
     * Entity version for optimistic locking.
     */
    @Version
    @Column(nullable = false)
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DetailBatch getDetail() {
        return detail;
    }

    public void setDetail(DetailBatch detail) {
        this.detail = detail;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public String getField6() {
        return field6;
    }

    public void setField6(String field6) {
        this.field6 = field6;
    }

    public String getField7() {
        return field7;
    }

    public void setField7(String field7) {
        this.field7 = field7;
    }

    public String getField8() {
        return field8;
    }

    public void setField8(String field8) {
        this.field8 = field8;
    }

    public String getField9() {
        return field9;
    }

    public void setField9(String field9) {
        this.field9 = field9;
    }

    public String getField10() {
        return field10;
    }

    public void setField10(String field10) {
        this.field10 = field10;
    }

    public String getField11() {
        return field11;
    }

    public void setField11(String field11) {
        this.field11 = field11;
    }

    public String getField12() {
        return field12;
    }

    public void setField12(String field12) {
        this.field12 = field12;
    }

    public String getField13() {
        return field13;
    }

    public void setField13(String field13) {
        this.field13 = field13;
    }

    public String getField14() {
        return field14;
    }

    public void setField14(String field14) {
        this.field14 = field14;
    }

    public String getField15() {
        return field15;
    }

    public void setField15(String field15) {
        this.field15 = field15;
    }

    public String getField16() {
        return field16;
    }

    public void setField16(String field16) {
        this.field16 = field16;
    }

    public String getField17() {
        return field17;
    }

    public void setField17(String field17) {
        this.field17 = field17;
    }

    public String getField18() {
        return field18;
    }

    public void setField18(String field18) {
        this.field18 = field18;
    }

    public String getField19() {
        return field19;
    }

    public void setField19(String field19) {
        this.field19 = field19;
    }

    public String getField20() {
        return field20;
    }

    public void setField20(String field20) {
        this.field20 = field20;
    }

    public String getField21() {
        return field21;
    }

    public void setField21(String field21) {
        this.field21 = field21;
    }

    public String getField22() {
        return field22;
    }

    public void setField22(String field22) {
        this.field22 = field22;
    }

    public String getField23() {
        return field23;
    }

    public void setField23(String field23) {
        this.field23 = field23;
    }

    public String getField24() {
        return field24;
    }

    public void setField24(String field24) {
        this.field24 = field24;
    }

    public String getField25() {
        return field25;
    }

    public void setField25(String field25) {
        this.field25 = field25;
    }

    public String getField26() {
        return field26;
    }

    public void setField26(String field26) {
        this.field26 = field26;
    }

    public String getField27() {
        return field27;
    }

    public void setField27(String field27) {
        this.field27 = field27;
    }

    public String getField28() {
        return field28;
    }

    public void setField28(String field28) {
        this.field28 = field28;
    }

    public String getField29() {
        return field29;
    }

    public void setField29(String field29) {
        this.field29 = field29;
    }

    public String getField30() {
        return field30;
    }

    public void setField30(String field30) {
        this.field30 = field30;
    }
}
