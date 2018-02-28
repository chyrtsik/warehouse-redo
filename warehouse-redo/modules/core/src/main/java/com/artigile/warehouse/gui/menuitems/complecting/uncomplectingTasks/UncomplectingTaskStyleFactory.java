/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting.uncomplectingTasks;

import com.artigile.warehouse.domain.complecting.UncomplectingTaskState;
import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.menuitems.complecting.complectingTasks.ComplectingTaskStyleFactory;
import com.artigile.warehouse.utils.dto.complecting.UncomplectingTaskTO;

import java.awt.*;

/**
 * @author Shyrik, 15.06.2009
 */
public class UncomplectingTaskStyleFactory extends ComplectingTaskStyleFactory {
    private Style notProcessedTaskStyle = new Style(new Color(255, 100, 100));
    private Style processedTaskStyle = new Style(new Color(200, 255, 200));

    @Override
    public Style getStyle(Object rowData) {
        UncomplectingTaskTO task = (UncomplectingTaskTO) rowData;
        if (task.getState() == UncomplectingTaskState.NOT_PROCESSED){
           return notProcessedTaskStyle;
        }
        else if (task.getState() == UncomplectingTaskState.PROCESSED){
            return processedTaskStyle;
        }
        else {
            return null;
        }
    }

}
