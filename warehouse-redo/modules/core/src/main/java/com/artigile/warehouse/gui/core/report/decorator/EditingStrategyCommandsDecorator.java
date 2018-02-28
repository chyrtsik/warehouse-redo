/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.decorator;

import com.artigile.warehouse.gui.core.report.command.ReportCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.ReportCommandListImpl;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;

/**
 * @author Shyrik, 08.01.2009
 */

/**
 * Decorator, that allows to process additional specific default command, or list of additional commands, when
 * user works with report.
 */
class EditingStrategyCommandsDecorator implements ReportEditingStrategy {
    /**
     * Strategy, which commands are beeing decorated.
     */
    private ReportEditingStrategy decoratedStrategy;

    /**
     * List of commands, used for decoration.
     */
    private ReportCommandList decorationCommands = new ReportCommandListImpl();

    /**
     * Constructs decorator and replaces default command of the report.
     * @param decoratedStrategy - strategy to be decorated.
     * @param newDefaultCommand - command, to be added to the command list. It will become the detail command of the report.
     */
    public EditingStrategyCommandsDecorator(ReportEditingStrategy decoratedStrategy, ReportCommand newDefaultCommand) {
        this.decoratedStrategy = decoratedStrategy;
        this.decorationCommands.add(newDefaultCommand);
        this.decorationCommands.setDefaultCommandForRow(newDefaultCommand);
    }

    /**
     * Constructs decorator, that decorates report with given list of command. If one of the command in list
     * is marked as default, it becomes default command of the report.
     * @param decoratedStrategy - strategy to be decorated.
     * @param decorationCommands - commands to be added to the report.
     */
    public EditingStrategyCommandsDecorator(ReportEditingStrategy decoratedStrategy, ReportCommandList decorationCommands){
        this.decoratedStrategy = decoratedStrategy;
        this.decorationCommands = decorationCommands;
    }


    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        decoratedStrategy.getCommandsForReport(commands, context);
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        decoratedStrategy.getCommandsForItem(commands, context);
        commands.addAll(0, decorationCommands);
        if (decorationCommands.getDefaultCommandForRow() != null){
            commands.setDefaultCommandForRow(decorationCommands.getDefaultCommandForRow());
        }
    }
}
