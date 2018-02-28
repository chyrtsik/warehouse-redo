/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.configuration;

import com.artigile.warehouse.adapter.spi.DataAdapterConfigView;
import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import com.artigile.warehouse.adapter.spi.impl.ExcelTableModelWorker;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelReader;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelReaderFactory;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelSheetReader;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.choosedialog.ChooseDialogResult;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.openide.util.UserCancelException;
import org.openide.windows.WindowManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;

/**
 * UI for editing parameters of import from Excel files.
 *
 * @author Valery Barysok, 6/12/11
 */

public class ExcelDataAdapterConfigView implements DataAdapterConfigView {

    private JPanel contentPane;
    private JButton browseFile;
    private JTextField filePath;
    private JButton chooseSheets;
    private JComboBox sheetsToImport;
    private JPanel tableViewPanel;
    private JLabel statusLabel;
    private JLabel sheetsToImportLabel;
    private RelationshipTableView tableView;

    /**
     * List of columns being configured with this instance of config view.
     */
    private List<DomainColumn> domainColumns;

    /**
     * Reader for working with currently selected excel file.
     */
    private ExcelReader excelReader;

    /**
     * List of sheets chosen by the user for import.
     */
    private List<SheetInfo> excelSheetsToImport;

    /**
     * List of all column relationship configurations for each sheet of the document.
     */
    private Map<String, List<ColumnRelationship>> sheetsColumnsConfiguration;

    /**
     * Current sheet selected for columns editing.
     */
    private String currentSheet;

    /**
     * Temporary flag. If true then changes in sheets combo box should not be processed.
     */
    private boolean ignoreSheetsComboEvents;

    private ExcelDataAdapterConfigurationData adapterConfig;

    public ExcelDataAdapterConfigView(List<DomainColumn> domainColumns) {
        this.domainColumns = domainColumns;
        this.tableView = new RelationshipTableView(domainColumns);
        initListeners();
        refreshTableViewPanel();
    }

    @Override
    public JComponent getView() {
        return contentPane;
    }

    private void restoreFileSettings() {
        if (adapterConfig != null) {
            Map<String, SheetInfo> sheetExists = new HashMap<String, SheetInfo>();
            for (SheetInfo sheetInfo : excelSheetsToImport) {
                sheetExists.put(sheetInfo.getListItem().getDisplayName(), sheetInfo);
            }

            for (Map.Entry<String, List<ColumnRelationship>> entry : adapterConfig.getSheetsColumnsConfig().entrySet()) {
                String sheetName = entry.getKey();
                if (sheetExists.containsKey(sheetName)) {
                    List<ColumnRelationship> relationships = entry.getValue();
                    SheetInfo sheetInfo = sheetExists.get(sheetName);
                    if (relationships.isEmpty()) {
                        excelSheetsToImport.remove(sheetInfo);
                    } else if (relationships.size() == sheetInfo.getColumnCount()) {
                        sheetsColumnsConfiguration.get(sheetName).addAll(relationships);
                    }
                }
            }

            refreshTableViewPanel();
        }
    }

    @Override
    public void setConfigurationString(String config) {
        adapterConfig = ExcelDataAdapterConfigurationData.parse(config);
        File file;
        try {
            file = new File(adapterConfig.getFileStreamId());
            if (file.isFile()) {
                excelReader = ExcelReaderFactory.create(file.getAbsolutePath());
                excelSheetsToImport = getAllSheetsInChosenFile(true);
                filePath.setText(file.getAbsolutePath());

                //Create editors for every sheet from file.
                initializeSheetsColumnsConfiguration();

                restoreFileSettings();

                //Refresh sheets combo to reflect a list of sheets in current file.
                refreshSheetsCombo();
            }
        } catch (RuntimeException ex) {
            LoggingFacade.logWarning(this, ex);
            excelReader = null;
            refreshSheetsCombo();
        }
    }

    private boolean containsSheet(String name) {
        for (SheetInfo sheetInfo : excelSheetsToImport) {
            if (sheetInfo.getListItem().getDisplayName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getConfigurationString() {
        ExcelDataAdapterConfigurationData parsedConfig = new ExcelDataAdapterConfigurationData();
        parsedConfig.setFileStreamId(filePath.getText());

        List<SheetInfo> allSheetsInFile = getAllSheetsInChosenFile(false);
        for (SheetInfo sheetInfo : allSheetsInFile) {
            ListItem listItem = sheetInfo.getListItem();
            if (containsSheet(listItem.getDisplayName())) {
                List<ColumnRelationship> columnRelationships = sheetsColumnsConfiguration.get(listItem.getDisplayName());
                parsedConfig.getSheetsColumnsConfig().put(listItem.getDisplayName(), columnRelationships);
            } else {
                parsedConfig.getSheetsColumnsConfig().put(listItem.getDisplayName(), new ArrayList<ColumnRelationship>());
            }
        }

        return parsedConfig.toString();
    }

    private Map<DomainColumn, Integer> getColumnCount(List<ColumnRelationship> columnsConfig) {
        //Calculates how much time each domain column is specified for excel table column.
        Map<DomainColumn, Integer> columnCount = new HashMap<DomainColumn, Integer>();
        for (DomainColumn domainColumn : domainColumns) {
            columnCount.put(domainColumn, 0);
        }

        for (ColumnRelationship columnConfig : columnsConfig) {
            DomainColumn domainColumn = columnConfig.getDomainColumn();
            if (!domainColumn.equals(DomainColumn.NOT_DEFINED)) {
                Integer oldColumnCount = columnCount.get(domainColumn);
                if (oldColumnCount != null) {
                    columnCount.put(domainColumn, oldColumnCount + 1);
                } else {
                    //Not supported column. Just log this to notice if this is a bug.
                    LoggingFacade.logWarning(this, MessageFormat.format("Column {0} not found in domain columns list.", domainColumn.getId()));
                }
            }
        }

        return columnCount;
    }

    @Override
    public void validateData() throws DataValidationException {
        saveCurrentSheetColumnsConfiguration();

        if (excelSheetsToImport.isEmpty()) {
            DataValidation.failRes(sheetsToImport, "data.import.relationship.validation.sheets.must.be.set");
        }

        //Check that all mandatory domain columns are set for every sheet selected for import.
        for (SheetInfo sheetInfo : excelSheetsToImport) {
            ListItem sheet = sheetInfo.getListItem();
            List<ColumnRelationship> sheetColumnsConfig = sheetsColumnsConfiguration.get(sheet.getDisplayName());
            Map<DomainColumn, Integer> columnCount = getColumnCount(sheetColumnsConfig);
            for (Map.Entry<DomainColumn, Integer> entry : columnCount.entrySet()) {
                DomainColumn domainColumn = entry.getKey();
                if (domainColumn.equals(DomainColumn.NOT_DEFINED)) {
                    continue;
                }
                Integer valueMappingCount = entry.getValue();
                if (domainColumn.isRequired() && valueMappingCount == 0) {
                    //This column is required.
                    DataValidation.failRes("data.import.relationship.validation.required.column.must.be.defined", domainColumn.getName(), sheet.getDisplayName());
                }
                if (!domainColumn.isMultiple() && valueMappingCount > 1) {
                    //This column is not multiple.
                    DataValidation.failRes("data.import.relationship.validation.column.is.not.multiple", domainColumn.getName(), sheet.getDisplayName());
                }
            }
        }
    }

    private void saveCurrentSheetColumnsConfiguration() {
        if (currentSheet != null) {
            sheetsColumnsConfiguration.put(currentSheet, TableHeaderRelationshipUtil.getColumnRelationships(tableView));
        }
    }

    private void initListeners() {
        browseFile.addActionListener(new ChooseFileAction());
        chooseSheets.addActionListener(new ChooseSheetsAction());
        sheetsToImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ignoreSheetsComboEvents) {
                    refreshTableViewPanel();
                    ListItem sheet = (ListItem) sheetsToImport.getSelectedItem();
                    currentSheet = sheet.getDisplayName();
                }
            }
        });
    }

    private void refreshTableViewPanel() {
        if (filePath.getText().isEmpty()) {
            showStatus(I18nSupport.message("excel.import.data.adapter.status.choose.file"));
        } else if (sheetsToImport.getSelectedIndex() == -1) {
            showStatus(I18nSupport.message("excel.import.data.adapter.status.choose.sheet"));
        } else {
            showRelationsTable();
        }
    }

    private void showStatus(String message) {
        //Display status message instead of table.
        statusLabel.setText(message);
        tableViewPanel.removeAll();
        tableViewPanel.add(statusLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tableViewPanel.revalidate();
    }

    private void initializeSheetsColumnsConfiguration() {
        //Creates storage for each sheet columns configuration.
        sheetsColumnsConfiguration = new LinkedHashMap<String, List<ColumnRelationship>>();
        List<SheetInfo> allSheetsInFile = getAllSheetsInChosenFile(false);
        for (SheetInfo sheetInfo : allSheetsInFile) {
            //Load contents of selected sheet.
            sheetsColumnsConfiguration.put(sheetInfo.getListItem().getDisplayName(), new ArrayList<ColumnRelationship>());
        }

        currentSheet = null;
    }

    private void showRelationsTable() {
        saveCurrentSheetColumnsConfiguration();

        //Display table for relations configuration.
        tableViewPanel.removeAll();

        //Integer sheetIndex = (Integer) DataExchange.getComboSelection(sheetsToImport);
        ListItem selectedItem = (ListItem) sheetsToImport.getSelectedItem();

        ExcelSheetReader selectedSheetReader = excelReader.getSheetReader((Integer) selectedItem.getValue());
        ExcelTableModelWorker tableLoadingWorker = new ExcelTableModelWorker(selectedSheetReader, tableView, sheetsColumnsConfiguration.get(selectedItem.getDisplayName()));
        tableLoadingWorker.execute();

        tableViewPanel.add(tableView.getViewComponent(), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));

        tableViewPanel.revalidate();
    }

    private void refreshSheetsCombo() {
        saveCurrentSheetColumnsConfiguration();

        boolean enableSheetsCombo = false;

        ignoreSheetsComboEvents = true;
        sheetsToImport.removeAllItems();
        if (excelReader != null) {
            //Loads list of sheets into combo box.
            for (SheetInfo item : excelSheetsToImport) {
                sheetsToImport.addItem(item.getListItem());
            }
            enableSheetsCombo = true;
        }
        ignoreSheetsComboEvents = false;

        if (sheetsToImport.getItemCount() > 0) {
            sheetsToImport.setSelectedIndex(0);
        }

        chooseSheets.setEnabled(enableSheetsCombo);
        sheetsToImport.setEnabled(enableSheetsCombo);
        sheetsToImportLabel.setEnabled(enableSheetsCombo);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("excel.import.data.adapter.select.file"));
        contentPane.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filePath = new JTextField();
        filePath.setEditable(false);
        contentPane.add(filePath, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        browseFile = new JButton();
        browseFile.setText("...");
        browseFile.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("excel.import.data.adapter.select.file.tooltip"));
        contentPane.add(browseFile, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sheetsToImportLabel = new JLabel();
        sheetsToImportLabel.setEnabled(false);
        this.$$$loadLabelText$$$(sheetsToImportLabel, ResourceBundle.getBundle("i18n/warehouse").getString("excel.import.data.adapter.sheets"));
        contentPane.add(sheetsToImportLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chooseSheets = new JButton();
        chooseSheets.setEnabled(false);
        chooseSheets.setText("...");
        chooseSheets.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("excel.import.data.adapter.sheets.tooltip"));
        contentPane.add(chooseSheets, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sheetsToImport = new JComboBox();
        sheetsToImport.setEnabled(false);
        contentPane.add(sheetsToImport, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tableViewPanel = new JPanel();
        tableViewPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(tableViewPanel, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(450, 200), null, null, 0, false));
        tableViewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        statusLabel = new JLabel();
        statusLabel.setText("<Status messages go here>");
        tableViewPanel.add(statusLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        label1.setLabelFor(filePath);
        sheetsToImportLabel.setLabelFor(sheetsToImport);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private class ChooseFileAction implements ActionListener {

        /**
         * stores the last current directory of the file chooser
         */
        private File currentDirectory = null;

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = prepareFileChooser();
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("Microsoft Excel Files", "xls", "xlsx"));
            File file;
            try {
                file = chooseFilesToOpen(chooser);
                currentDirectory = chooser.getCurrentDirectory();
                excelReader = ExcelReaderFactory.create(file.getAbsolutePath());
                excelSheetsToImport = getAllSheetsInChosenFile(true);
                filePath.setText(file.getAbsolutePath());

                //Create editors for every sheet from file.
                initializeSheetsColumnsConfiguration();

                restoreFileSettings();

                //Refresh sheets combo to reflect a list of sheets in current file.
                refreshSheetsCombo();
            } catch (UserCancelException e1) {
                //This is normal. No not change anything.
            } catch (RuntimeException ex) {
                LoggingFacade.logWarning(this, ex);
                MessageDialogs.showWarning(I18nSupport.message("excel.import.data.adapter.error.opening.file"));
                excelReader = null;
                refreshSheetsCombo();
            }
        }

        private JFileChooser prepareFileChooser() {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(getCurrentDirectory());
            return chooser;
        }

        private File getCurrentDirectory() {
            if (currentDirectory != null && currentDirectory.exists()) {
                return currentDirectory;
            }
            currentDirectory = FileSystemView.getFileSystemView().getDefaultDirectory();
            return currentDirectory;
        }

        public File chooseFilesToOpen(JFileChooser chooser) throws UserCancelException {
            File file;
            do {
                int selectedOption = chooser.showOpenDialog(WindowManager.getDefault().getMainWindow());

                if (selectedOption != JFileChooser.APPROVE_OPTION) {
                    throw new UserCancelException();
                }
                file = chooser.getSelectedFile();
            } while (file == null);
            return file;
        }
    }

    private class ChooseSheetsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<SheetInfo> allSheetsList = getAllSheetsInChosenFile(false);
            Map<String, SheetInfo> sheetExists = new HashMap<String, SheetInfo>();
            List<ListItem> allSheets = new ArrayList<ListItem>(allSheetsList.size());
            for (SheetInfo sheetInfo : allSheetsList) {
                sheetExists.put(sheetInfo.getListItem().getDisplayName(), sheetInfo);
                allSheets.add(sheetInfo.getListItem());
            }

            List<ListItem> sheets = new ArrayList<ListItem>();
            for (SheetInfo sheetInfo : excelSheetsToImport) {
                sheets.add(sheetInfo.getListItem());
            }

            String title = I18nSupport.message("excel.import.data.adapter.sheets.dialog.title");
            ChooseDialogResult result = Dialogs.runChooseListDialog(title, allSheets, sheets);
            if (result.isOk()) {
                List<ListItem> selectedItems = result.getSelectedItems();
                excelSheetsToImport.clear();
                for (ListItem item : selectedItems) {
                    excelSheetsToImport.add(sheetExists.get(item.getDisplayName()));
                }
                refreshSheetsCombo();
            }
        }
    }

    private List<SheetInfo> getAllSheetsInChosenFile(boolean ignoreEmptySheets) {
        List<SheetInfo> allSheets = new ArrayList<SheetInfo>();
        for (int i = 0; i < excelReader.getSheetCount(); i++) {
            ExcelSheetReader sheetReader = excelReader.getSheetReader(i);
            int columnCount = sheetReader.getColumnCount();
            if (ignoreEmptySheets && sheetReader.getRowCount() < 2 && columnCount < 2) {
                //Do not include sheets without data into result list.
                continue;
            }
            allSheets.add(new SheetInfo(new ListItem(sheetReader.getSheetName(), i), columnCount));
        }
        return allSheets;
    }

    private static class SheetInfo {

        private ListItem listItem;
        private int columnCount;

        private SheetInfo(ListItem listItem, int columnCount) {
            this.listItem = listItem;
            this.columnCount = columnCount;
        }

        public ListItem getListItem() {
            return listItem;
        }

        public int getColumnCount() {
            return columnCount;
        }
    }
}
