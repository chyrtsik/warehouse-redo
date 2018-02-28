/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.controller;

import com.artigile.warehouse.gui.core.report.command.ReportCommand;
import com.artigile.warehouse.gui.core.report.view.PredefinedCommand;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Shyrik, 06.07.2009
 */

/**
 * Holder for options of the report.
 */
public class ReportOptions {
    /**
     * Flag, that determines, if dragging of the rows is enabled for the report.
     */
    private boolean dragRowsEnabled = false;

    /**
     * Flag, that determines, if toolbar is shown or hidden.
     */
    private boolean showToolbar = true;

    /**
     * Flag, that determines, is sort ability is available.
     */
    private boolean sortEnabled = true;

    /*
     *  Custom command, that will be shown at the toolbar.
     */
    private ReportCommand customToolbarCommand;

    /**
     * If report is printable contains provider (of helper) for printing of report data.
     */
    private ReportPrintProvider printProvider;

    private Set<PredefinedCommand> predefinedCommands = new HashSet<PredefinedCommand>();

    public ReportOptions(){
        //Initialize default list of report commands.
        predefinedCommands.add(PredefinedCommand.REFRESH);
        predefinedCommands.add(PredefinedCommand.SORT);
        predefinedCommands.add(PredefinedCommand.FILTER);
    }


    //============================ Getters and setters ===================================
    public boolean isDragRowsEnabled() {
        return dragRowsEnabled;
    }

    public void setDragRowsEnabled(boolean dragRowsEnabled) {
        this.dragRowsEnabled = dragRowsEnabled;
    }

    public boolean isShowToolbar() {
        return showToolbar;
    }

    public void setShowToolbar(boolean showToolbar) {
        this.showToolbar = showToolbar;
    }

    public boolean isSortEnabled() {
        return sortEnabled;
    }

    public void setSortEnabled(boolean sortEnabled) {
        this.sortEnabled = sortEnabled;
    }

    public ReportCommand getCustomToolbarCommand() {
        return customToolbarCommand;
    }

    public void setCustomToolbarCommand(ReportCommand customToolbarCommand) {
        this.customToolbarCommand = customToolbarCommand;
    }

    public ReportPrintProvider getPrintProvider() {
        return printProvider;
    }

    public void setPrintProvider(ReportPrintProvider printProvider) {
        this.printProvider = printProvider;
        if (printProvider != null){
            //Report is printable. We may use printing commands.
            predefinedCommands.add(PredefinedCommand.PRINT);
            predefinedCommands.add(PredefinedCommand.PRINT_PREVIEW);
        }
        else {
            //Report is not printable.
            predefinedCommands.remove(PredefinedCommand.PRINT);
            predefinedCommands.remove(PredefinedCommand.PRINT_PREVIEW);
        }
    }

    public Set<PredefinedCommand> getPredefinedCommands(){
        return predefinedCommands;
    }

    public void disablePredefinedCommand(PredefinedCommand command){
        predefinedCommands.remove(command);
    }
}
