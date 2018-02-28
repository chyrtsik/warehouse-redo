/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.movement;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 21.11.2009
 */

/**
 * All possible results of processing movement item.
 */
public enum MovementItemProcessingResult {
    SUCCESS(I18nSupport.message("movement.item.result.name.success")),

    PROBLEM(I18nSupport.message("movement.item.result.name.problem"));

    //====================== Naming support ========================
    private String name;

    private MovementItemProcessingResult(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
