/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs;

import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.ReadOnlyPropertiesDialog;
import com.artigile.warehouse.gui.core.properties.ReadOnlyPropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.dialogs.barcode.BarCodeInputForm;
import com.artigile.warehouse.gui.core.properties.dialogs.barcode.BarCodeListener;
import com.artigile.warehouse.gui.core.properties.dialogs.browser.BrowseResult;
import com.artigile.warehouse.gui.core.properties.dialogs.browser.BrowserForm;
import com.artigile.warehouse.gui.core.properties.dialogs.browser.TreeBrowserForm;
import com.artigile.warehouse.gui.core.properties.dialogs.choosedialog.ChooseDialogResult;
import com.artigile.warehouse.gui.core.properties.dialogs.choosedialog.ChooseProperties;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.gui.core.report.controller.TreeReport;

import java.awt.*;
import java.util.List;

/**
 * @author Shyrik, 05.01.2009
 */

/**
 * Utilites class for showing standard application dialogs.
 */
final public class Dialogs {
    private Dialogs(){
    }

    public static ChooseDialogResult runChooseListDialog(String dialogTitle, List<ListItem> allAvailableItems, List<ListItem> selectedItems){
        return runChooseListDialog(dialogTitle, allAvailableItems, selectedItems, null, true);
    }

    /**
     * Runs choose dialog.
     * @param dialogTitle title of dialog to be displayed.
     * @param allAvailableItems all items, that are available to be chosen.
     * @param selectedItems list of items, that are already selected. may be null.
     * @param ownerComponent component which owns this dialog.
     * @param enableRestore is true then dialog position if size is restored from database.
     * @return - result of showing the dialog.
     */
    public static ChooseDialogResult runChooseListDialog(String dialogTitle, List<ListItem> allAvailableItems, List<ListItem> selectedItems, Component ownerComponent, boolean enableRestore){
        ChooseProperties prop = new ChooseProperties(dialogTitle, allAvailableItems, selectedItems);
        if (runProperties(prop, ownerComponent, enableRestore)){
            return new ChooseDialogResult(true, prop.getSelectedItems());
        }
        return new ChooseDialogResult(false, null);
    }

    /**
     * Runs browser, that lets user to select item of the given report.
     * @param dataSource - report data source
     * @return
     */
    public static BrowseResult runBrowser(ReportDataSource dataSource){
        BrowserForm prop = new BrowserForm(dataSource);
        if (runProperties(prop)){
            return new BrowseResult(true, prop.getSelectedItem());
        }
        return new BrowseResult(false, null);
    }

    /**
     * Run browser for the given tree report.
     * @param treeReport tree report data source.
     * @return
     */
    public static BrowseResult runTreeBrowser(TreeReport treeReport, String browserTitle, boolean allowMultipleSelection, boolean recursivelySelectItems) {
        TreeBrowserForm prop = new TreeBrowserForm(treeReport, browserTitle, allowMultipleSelection, recursivelySelectItems);
        if (runProperties(prop)){
            return new BrowseResult(true, prop.getSelectedItems());
        }
        return new BrowseResult(false, null);
    }

    public static boolean runProperties(PropertiesForm prop) {
        return runProperties(prop, null, true);
    }

    /**
     * Properties window run wrapper.
     * @param prop properties form implementation.
     * @param ownerComponent component which owns this properties dialog.
     * @param enableRestore is true the dislog position and size is restored from database.
     * @return
     */
    public static boolean runProperties(PropertiesForm prop, Component ownerComponent, boolean enableRestore) {
        return new PropertiesDialog(prop, ownerComponent, enableRestore).run();
    }

    /**
     * Runs properties dialog to show readonly data.
     * @param prop properties form to be shown in dialog.
     */
    public static void runReadOnlyProperties(ReadOnlyPropertiesForm prop) {
        new ReadOnlyPropertiesDialog(prop).run();
    }

    /**
     * Run dialog for input of single bar code.
     * @return bar code entered or null if input was cancelled.
     */
    public static String runSingleBarCodeInput() {
        BarCodeInputForm barCodeInputForm = new BarCodeInputForm();
        if (runProperties(barCodeInputForm)){
            return barCodeInputForm.getBarCode();
        }
        return null;
    }

    /**
     * Run dialog for input of multiple bar codes.
     * @param barCodeListener listener to be called per each scanned bar code.
     */
    public static void runMultipleBarCodeInput(BarCodeListener barCodeListener) {
        String barCode = runSingleBarCodeInput();
        while (barCode != null){
            barCodeListener.onBarCodeReceived(barCode);
            barCode = runSingleBarCodeInput();
        }
    }
}
