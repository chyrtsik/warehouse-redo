/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.controller;

import com.artigile.warehouse.bl.lock.Utils;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.core.report.model.ReportModel;
import com.artigile.warehouse.gui.core.report.view.ReportView;
import com.artigile.warehouse.gui.utils.TableDoubleClickListener;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * @author Shyrik, 06.12.2008
 */

/**
 * Controller for the any report. This class incapsulates processing user unput,
 * for the report. It delegates all data manipulation actions to the strategy.
 */
public class ReportController implements ReportCommandContext {
    /**
     * Model of the report.
     */
    private ReportModel reportModel;

    /**
     * View of the report.
     */
    private ReportView reportView;

    /**
     * Editing strategy of the report.
     */
    private ReportEditingStrategy editingStrategy;

    /**
     * Constructor.
     *
     * @param reportModel model of the report.
     * @param reportView view of the report.
     * @param editingStrategy editing strategy of the report.
     */
    public ReportController(ReportModel reportModel, ReportView reportView, ReportEditingStrategy editingStrategy) {
        this.reportModel = reportModel;
        this.reportView = reportView;
        this.editingStrategy = (editingStrategy != null) ? editingStrategy : new DefaultEditingStrategy();

        initViewListeners();
    }

    private void initViewListeners() {
        Component viewComponent = reportView.getViewComponent();
        viewComponent.addKeyListener(getKeyListener());
        viewComponent.addMouseListener(getMouseListener());
        viewComponent.addMouseListener(getDoubleClickListener());
    }

    //==================== Helpers =============================
    private KeyListener getKeyListener() {
        return new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                //Mapping key events to editing actions
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_INSERT:
                        onCommand(findCommand(ReportCommandType.CREATE, getReportCommandList()));
                        break;
                    case KeyEvent.VK_DELETE:
                        onCommand(findCommand(ReportCommandType.DELETE, getReportCommandList()));
                        break;
                    case KeyEvent.VK_ENTER:
                        if (onDefaultCommand(false)) {
                            e.consume();
                        }
                        break;
                }
            }
        };
    }

    private MouseListener getMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)){
                    //Right button click - time to show context menu.
                    onContextMenu(e.getX(), e.getY());
                }
            }
        };
    }

    private MouseListener getDoubleClickListener() {
        return new TableDoubleClickListener() {
            @Override
            public void onDoubleClick() {
                onDefaultCommand(true);
            }
        };
    }

    private ReportCommandList getReportCommandList() {
        //At first we are to get available command for the menu.
        ReportCommandList commands = new ReportCommandListImpl();
        if (reportView.getSelectedItems() == null) {
            //Menu for all tableReport.
            editingStrategy.getCommandsForReport(commands, this);
        } else {
            //Menu for the selected item.
            editingStrategy.getCommandsForItem(commands, this);
        }
        return commands;
    }

    private void onContextMenu(int x, int y) {
        ReportCommandList commands = getReportCommandList();
        if (commands.size() == 0) {
            return;
        }

        //Creating and showing a context menu.
        JPopupMenu menu = new JPopupMenu();
        for (ReportCommand command : commands) {
            menu.add(new ReportCommandAction(command, this));
        }
        menu.show(reportView.getViewComponent(), x, y);
    }

    //=============== Manipulation with tableReport's data =================
    private boolean onDefaultCommand(boolean useDefaultCommandForColumn) {
        //Executes default command for the Table Report.
        ReportCommandList commands = getReportCommandList();
        ReportCommand command = useDefaultCommandForColumn ? commands.getDefaultCommandForColumn(reportView.getSelectedColumn()) : commands.getDefaultCommandForRow();
        if (command != null) {
            //Custom default command.
            return onCommand(command);
        } else {
            //Open Table Report item command is considered to be default command, if
            //there is not custom default command.
            return onCommand(findCommand(ReportCommandType.PROPERTIES, getReportCommandList()));
        }
    }

    public boolean onCommand(ReportCommand command) {
        //Performs given command.
        if (command == null) {
            return false;
        }

        try {
            command.execute(this);
        }
        catch (javax.persistence.OptimisticLockException e) {
            LoggingFacade.logError(this, e);
            MessageDialogs.showError(reportView.getViewComponent(), I18nSupport.message("error.message.already.modified"));
            return false;
        }
        catch (org.hibernate.OptimisticLockException e) {
            LoggingFacade.logError(this, e);
            MessageDialogs.showError(reportView.getViewComponent(), I18nSupport.message("error.message.already.modified"));
            return false;
        }
        catch (RuntimeException e) {
            if (Utils.isLockedException(e)) {
                MessageDialogs.showWarning(reportView.getViewComponent(), I18nSupport.message("locking.warning.use.locked.object"));
            } else if (Utils.isUsedException(e)) {
                MessageDialogs.showWarning(reportView.getViewComponent(), I18nSupport.message("error.message.cannot.update.or.delete.parent.row"));
            } else if (Utils.isExistsException(e)) {
                MessageDialogs.showWarning(reportView.getViewComponent(), I18nSupport.message("error.message.cannot.insert"));
            } else {
                MessageDialogs.showError(reportView.getViewComponent(), e);
            }
            return false;
        }
        catch (ReportCommandException e) {
            LoggingFacade.logWarning(this, e);
            MessageDialogs.showWarning(reportView.getViewComponent(), e.getMessage());
            return false;
        }
        catch (Throwable e) {
            LoggingFacade.logError(this, e);
            MessageDialogs.showError(reportView.getViewComponent(), e);
            return false;
        }

        return true;
    }

    private ReportCommand findCommand(ReportCommandType commandType, ReportCommandList commands) {
        for (ReportCommand command : commands) {
            if (command.getType() == commandType) {
                return command;
            }
        }
        return null;
    }

    //======================= Methods of the Report context interface ============================
    @Override
    public Object getCurrentReportItem() {
        List selItems = reportView.getSelectedItems();
        return selItems == null ? null : reportView.getSelectedItems().get(0);
    }

    @Override
    public List getCurrentReportItems() {
        return reportView.getSelectedItems();
    }

    @Override
    public ReportModel getReportModel() {
        return reportModel;
    }

    @Override
    public ReportView getReportView() {
        return reportView;
    }

    @Override
    public Object getCommandParameters() {
        return null;
    }

    private class DefaultEditingStrategy implements ReportEditingStrategy {
        @Override
        public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
            //Do nothing.
        }

        @Override
        public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
            //Do nothing.
        }
    }

    /**
     * Wrapper for the report command to use it as UI actions.
     */
    private class ReportCommandAction extends AbstractAction {

        private ReportCommand command;

        public ReportCommandAction(ReportCommand command, ReportCommandContext context) {
            this.command = command;
            setEnabled(command.isAvailable(context));
            putValue(Action.NAME, command.getName(context));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            onCommand(command);
        }
    }
}
