/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting.complectingTasks;

import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;

import java.awt.*;

/**
 * @author Shyrik, 20.05.2009
 */

/**
 * Factory of styles for decorating complecting tasks.
 */
public class ComplectingTaskStyleFactory implements StyleFactory {
    private Style notInYetInProcessTaskStyle = new Style(new Color(200, 200, 200));
    private Style processingTaskStyle = new Style(new Color(200, 200, 255));
    private Style problemTaskStyle = new Style(new Color(255, 200, 200));
    private Style completedTaskStyle = new Style(new Color(200, 255, 200));

    @Override
    public Style getStyle(Object rowData) {
        ComplectingTaskTO task = (ComplectingTaskTO) rowData;
        if (task.getState() == ComplectingTaskState.NOT_PROCESSED){
           return notInYetInProcessTaskStyle; 
        }
        else if (task.isProblem()){
            return problemTaskStyle;
        }
        else if (task.isInProcess()){
            return processingTaskStyle;
        }
        else if (task.isCompleted()){
            return completedTaskStyle;
        }
        else{
            return null;
        }
    }
}
