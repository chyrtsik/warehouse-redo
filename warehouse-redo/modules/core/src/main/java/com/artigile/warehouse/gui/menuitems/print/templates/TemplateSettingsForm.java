/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.print.templates;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.print.PrintTemplatePlugin;
import com.artigile.warehouse.bl.print.PrintTemplatePluginFactory;
import com.artigile.warehouse.bl.print.PrintTemplateService;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StreamUtils;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;
import com.artigile.warehouse.utils.dto.template.PrintTemplateTO;
import com.artigile.warehouse.utils.dto.template.TemplateFieldMappingTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 03.12.2008
 */
public class TemplateSettingsForm implements PropertiesForm {

    PrintTemplateService templateService = SpringServiceContext.getInstance().getPrintTemplateService();


    private JPanel contentPane;

    private JTextField textName;

    private JTextField textDescription;

    private JList listFields;

    private JButton buttonLoad;

    private JButton buttonSave;

    private JComboBox fieldTemplateType;

    private JCheckBox fieldDefaultTemplate;

    private JTextField fieldTemplateFileName;

    private PrintTemplateInstanceTO template;

    private File newTemplateFile;

    boolean canEdit = false;

    private PrintTemplatePluginFactory printTemplatePluginFactory = SpringServiceContext.getInstance().getPrintTemplatePluginFactory();

    private static File currentDir;

    @Override
    public String getTitle() {
        return I18nSupport.message("printing.template.settings.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPane;
    }

    @Override
    public boolean canSaveData() {
        return canEdit;
    }

    @Override
    public void loadData() {
        newTemplateFile = null;

        DataExchange.selectComboItem(fieldTemplateType, template.getTemplate());
        textName.setText(template.getName());
        fieldDefaultTemplate.setSelected(template.getDefaultTemplate());
        textDescription.setText(template.getDescription());
        fieldTemplateFileName.setText(template.getTemplateFile() != null ? template.getTemplateFile().getName() : I18nSupport.message("printing.template.file.not.selected"));

        enableControls();
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkSelected(fieldTemplateType);
        DataValidation.checkNotEmpty(textName);
        PrintTemplateService printTemplateService = SpringServiceContext.getInstance().getPrintTemplateService();
        if (!printTemplateService.isUniqueTemplateInstanceName(template.getId(), textName.getText())) {
            DataValidation.failRes(textName, "printing.template.name.not.unique");
        }
        if (template.getTemplateFile() == null && newTemplateFile == null) {
            DataValidation.failRes(fieldTemplateFileName, "printing.template.validation.file.not.selected");
        }
    }

    @Override
    public void saveData() {
        template.setTemplate((PrintTemplateTO) DataExchange.getComboSelection(fieldTemplateType));
        template.setDefaultTemplate(fieldDefaultTemplate.isSelected());
        template.setName(textName.getText());
        template.setDescription(textDescription.getText());
        template.setNewTemplateFile(newTemplateFile);
    }

    /**
     * Constructor.
     */
    public TemplateSettingsForm(PrintTemplateInstanceTO template, boolean canEdit) {
        //Common initialization
        this.template = template;
        this.canEdit = canEdit;

        //Initialize list of print template types.
        initPrintTemplateTypesCombo();

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(textName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(textDescription, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        //Initializing the pane.
        buttonSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onSaveToFile();
            }
        });

        buttonLoad.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onLoadFromFile();
            }
        });

        fieldTemplateType.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onSelectPrintTemplate();
            }
        });
    }

    private void onSelectPrintTemplate() {
        listFields.setModel(getFieldsListModel((PrintTemplateTO) DataExchange.getComboSelection(fieldTemplateType)));
    }

    private void initPrintTemplateTypesCombo() {
        PrintTemplateService printTemplateService = SpringServiceContext.getInstance().getPrintTemplateService();
        List<PrintTemplateTO> printTemplates = printTemplateService.getAllTemplates();
        fieldTemplateType.removeAllItems();
        for (PrintTemplateTO template : printTemplates) {
            fieldTemplateType.addItem(new ListItem(template.getName(), template));
        }
        fieldTemplateType.setSelectedItem(null);
    }

    private void onSaveToFile() {
        //Exports printing printing to the file.
        JFileChooser fileChooser = createSaveFileChooser(template.getTemplateFile().getName());
        if (fileChooser.showSaveDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
            currentDir = fileChooser.getCurrentDirectory();
            InputStream stream = null;
            try {
                stream = SpringServiceContext.getInstance().getStoredFileService().getStoredFileDataStream(template.getTemplateFile().getId());
                StreamUtils.createFileFromStream(fileChooser.getSelectedFile(), stream);
            } catch (BusinessException e) {
                LoggingFacade.logError(this, e);
                MessageDialogs.showWarning(contentPane, e.getLocalizedMessage());
            } catch (Exception ex) {
                LoggingFacade.logError(this, ex);
                MessageDialogs.showWarning(contentPane, I18nSupport.message("error.message", ex.getMessage()));
            } finally {
                if (stream != null) {
                    try {

                        stream.close();
                    } catch (IOException e) {
                        LoggingFacade.logError(this, e);
                    }
                }
            }
        }
    }

    private void onLoadFromFile() {
        //Import printing printing from file.
        JFileChooser fileChooser = createOpenFileChooser();
        if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
            newTemplateFile = fileChooser.getSelectedFile();
            fieldTemplateFileName.setText(newTemplateFile.getName());
            currentDir = fileChooser.getCurrentDirectory();
        }
        enableControls();
    }

    private JFileChooser createOpenFileChooser() {
        JFileChooser fileChooser = new JFileChooser(currentDir);
        fileChooser.setAcceptAllFileFilterUsed(false);

        PrintTemplatePlugin[] plugins = printTemplatePluginFactory.enumeratePlugins();
        for (PrintTemplatePlugin plugin : plugins) {
            fileChooser.setFileFilter(new FileNameExtensionFilter(
                    plugin.getTemplateFileTypeDescription(),
                    plugin.getTemplateFileExtension()
            ));
        }

        return fileChooser;
    }

    private JFileChooser createSaveFileChooser(String fileName) {
        JFileChooser fileChooser = new JFileChooser(currentDir);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setSelectedFile(new File(fileName));

        PrintTemplatePlugin plugin = printTemplatePluginFactory.getPluginForFileName(fileName);
        if (plugin == null) {
            //Default behavior in case some one forgot to include check for new extension here.
            LoggingFacade.logWarning(this, "Invalid extension when trying to save file: " + fileName);
            fileChooser.setAcceptAllFileFilterUsed(true);
        } else {
            fileChooser.setFileFilter(new FileNameExtensionFilter(
                    plugin.getTemplateFileTypeDescription(),
                    plugin.getTemplateFileExtension()
            ));
        }

        return fileChooser;
    }

    private void enableControls() {
        buttonLoad.setEnabled(canEdit);
        buttonSave.setEnabled(template.getTemplateFile() != null && newTemplateFile == null);
    }

    private ListModel getFieldsListModel(PrintTemplateTO template) {
        DefaultListModel model = new DefaultListModel();
        if (template != null) {
            List<TemplateFieldMappingTO> fields = templateService.getTemplatedFieldMappingByTemplateId(template.getId());
            for (TemplateFieldMappingTO field : fields) {
                model.addElement(field);
            }
        }
        return model;
    }

    public List<TemplateFieldMappingTO> getFieldMappingsList() {
        List<TemplateFieldMappingTO> newList = new ArrayList<TemplateFieldMappingTO>();

        for (int i = 0; i < listFields.getModel().getSize(); i++) {
            newList.add((TemplateFieldMappingTO) listFields.getModel().getElementAt(i));
        }
        return newList;
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
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("printing.template.properties.name"));
        panel1.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("printing.template.properties.description"));
        panel1.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textDescription = new JTextField();
        panel1.add(textDescription, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonLoad = new JButton();
        this.$$$loadButtonText$$$(buttonLoad, ResourceBundle.getBundle("i18n/warehouse").getString("printing.template.properties.import.from.file"));
        panel2.add(buttonLoad, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSave = new JButton();
        this.$$$loadButtonText$$$(buttonSave, ResourceBundle.getBundle("i18n/warehouse").getString("printing.template.properties.export.to.file"));
        panel2.add(buttonSave, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldTemplateFileName = new JTextField();
        fieldTemplateFileName.setEditable(false);
        panel2.add(fieldTemplateFileName, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), new Dimension(200, -1), 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("printing.template.properties.template"));
        panel1.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("printing.template.properties.templateType"));
        panel1.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldTemplateType = new JComboBox();
        panel1.add(fieldTemplateType, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textName = new JTextField();
        panel3.add(textName, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(250, -1), null, 0, false));
        fieldDefaultTemplate = new JCheckBox();
        this.$$$loadButtonText$$$(fieldDefaultTemplate, ResourceBundle.getBundle("i18n/warehouse").getString("printing.template.properties.defaultTemplate"));
        panel3.add(fieldDefaultTemplate, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("printing.template.properties.fiels"));
        panel4.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listFields = new JList();
        scrollPane1.setViewportView(listFields);
        label2.setLabelFor(textDescription);
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
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
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
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
