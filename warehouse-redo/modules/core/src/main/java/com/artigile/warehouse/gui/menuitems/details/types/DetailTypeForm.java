/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.types;

import com.artigile.warehouse.bl.detail.DetailTypeService;
import com.artigile.warehouse.bl.sticker.StickerPrintParamService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.choosedialog.ChooseDialogResult;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.menuitems.details.types.fields.DetailFieldsList;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTO;
import com.artigile.warehouse.utils.dto.sticker.StickerPrintParamTO;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * @author Shyrik, 14.12.2008
 */
public class DetailTypeForm implements PropertiesForm {

    private JPanel contentPanel;

    private JTextField textName;

    private JTextArea textDescription;

    private JPanel fieldsPanel;

    private JTextArea textNameInPrice;

    private JTextArea textMiscInPrice;

    private JTextArea textTypeInPrice;

    private JTabbedPane tabs;

    private JPanel serialNumberFieldsPanel;

    private JPanel propertiesTab;

    private JPanel serialNumbersTab;

    private JPanel stickerPrintParamsTab;

    private JRadioButton warehouseBatch;

    private JRadioButton serialNumbers;

    private JButton selectFields;

    private JList selected;

    private JComboBox templates;

    private boolean canEdit;

    private DetailTypeTO detailType;

    private DetailFieldsList fieldsList;

    private DetailFieldsList serialNumberFieldsList;

    private List<ListItem> selectedItems;

    public DetailTypeForm(DetailTypeTO detailTypeFull, boolean canEdit) {
        this.canEdit = canEdit;
        this.detailType = detailTypeFull;
        this.selectedItems = getSelectedItems(detailTypeFull.getStickerPrintParams());

        // Limitation of text length in the fields
        $$$setupUI$$$();
        DataFiltering.setTextLengthLimit(textName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(textDescription, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(textMiscInPrice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(textNameInPrice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(textTypeInPrice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        // Hide tabs depending on user permission.
        if (!new PermissionCommandAvailability(PermissionType.VIEW_SERIAL_NUMBERS_SETTINGS).isAvailable(null)) {
            tabs.remove(serialNumbersTab);
        }
        if (!new PermissionCommandAvailability(PermissionType.VIEW_STICKER_PRINT_PARAMS).isAvailable(null)) {
            tabs.remove(stickerPrintParamsTab);
        }

        initListeners();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("detail.type.properties.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public boolean canSaveData() {
        return canEdit;
    }

    @Override
    public void loadData() {
        textName.setText(detailType.getName());
        textDescription.setText(detailType.getDescription());
        textNameInPrice.setText(detailType.getDetailBatchNameField().getTemplate());
        textMiscInPrice.setText(detailType.getDetailBatchMiscField().getTemplate());
        textTypeInPrice.setText(detailType.getDetailBatchTypeField().getTemplate());

        //Initialize list of custom product fields.
        fieldsList = new DetailFieldsList("0", detailType.getFieldsInDisplayOrder(), canEdit, true,
                getDetailTypesService().getMaxDetailFieldsCount(), getDetailTypesService().getAvailableDetailFields());
        fieldsPanel.add(new TableReport(fieldsList).getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());

        //Initialize list of serial number fields (please note that separate permission is needed or editing).
        serialNumberFieldsList = new DetailFieldsList("1", detailType.getSerialNumberFieldsInDisplayOrder(),
                new PermissionCommandAvailability(PermissionType.EDIT_SERIAL_NUMBERS_SETTINGS).isAvailable(null), false,
                getDetailTypesService().getMaxSerialNumberFieldsCount(), getDetailTypesService().getAvailableSerialNumberFields());
        serialNumberFieldsPanel.add(new TableReport(serialNumberFieldsList).getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());

        if (detailType.getPrintSerialNumbers()) {
            serialNumbers.setSelected(true);
        } else {
            warehouseBatch.setSelected(true);
        }
        refreshPrintTemplates(detailType.getPrintSerialNumbers(), detailType.getPrintTemplateInstance());
    }

    private void refreshPrintTemplates(boolean printSerialNumbers) {
        ListItem selectedItem = (ListItem) templates.getSelectedItem();
        refreshPrintTemplates(printSerialNumbers, selectedItem != null ? (PrintTemplateInstanceTO) selectedItem.getValue() : null);
        UIComponentUtils.packDialog(templates);
    }

    private void refreshPrintTemplates(boolean printSerialNumbers, PrintTemplateInstanceTO selectedPrintTemplateInstance) {
        if (printSerialNumbers) {
            InitUtils.initPrintTemplatesCombo(templates, selectedPrintTemplateInstance, PrintTemplateType.TEMPLATE_DETAIL_BATCHES_LIST, PrintTemplateType.TEMPLATE_SERIAL_NUMBER_LIST);
        } else {
            InitUtils.initPrintTemplatesCombo(templates, selectedPrintTemplateInstance, PrintTemplateType.TEMPLATE_DETAIL_BATCHES_LIST);
        }
    }

    private List<ListItem> getSelectedItems(List<StickerPrintParamTO> stickerPrintParams) {
        List<ListItem> result = new ArrayList<ListItem>();
        for (StickerPrintParamTO stickerPrintParam : stickerPrintParams) {
            DetailFieldTO detailField = stickerPrintParam.getDetailField() != null ? stickerPrintParam.getDetailField() : stickerPrintParam.getSerialDetailField();
            result.add(new ListItem(detailField.getName(), detailField));
        }
        return result;
    }

    public List<DetailFieldTO> getEditFields(boolean serialNumbers) {
        List<DetailFieldTO> result = new ArrayList<DetailFieldTO>();
        result.addAll(fieldsList.getEditedFields());
        if (serialNumbers) {
            result.addAll(serialNumberFieldsList.getEditedFields());
        }
        return result;
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(textName);
        //Group name must be unique
        if (!getDetailTypesService().isUniqueDetailTypeName(textName.getText(), detailType.getId())) {
            DataValidation.failRes(textName, "detail.type.properties.type.already.exists");
        }

        DataValidation.checkNotEmpty(textNameInPrice);
    }

    @Override
    public void saveData() {
        detailType.setName(textName.getText());
        detailType.setDescription(textDescription.getText());
        detailType.getDetailBatchNameField().setTemplate(textNameInPrice.getText());
        detailType.getDetailBatchMiscField().setTemplate(textMiscInPrice.getText());
        detailType.getDetailBatchTypeField().setTemplate(textTypeInPrice.getText());
        detailType.setFields(fieldsList.getEditedFields());
        detailType.setSerialNumberFields(serialNumberFieldsList.getEditedFields());
        detailType.setPrintSerialNumbers(serialNumbers.isSelected());
        detailType.setStickerPrintParams(getStickerPrintParams());
        ListItem selectedItem = (ListItem) templates.getSelectedItem();
        detailType.setPrintTemplateInstance(selectedItem != null ? (PrintTemplateInstanceTO) selectedItem.getValue() : null);
    }

    private List<StickerPrintParamTO> getStickerPrintParams() {
        List<StickerPrintParamTO> result = new ArrayList<StickerPrintParamTO>();
        List<ListItem> listItems = getSelectedItems();
        Map<Long, StickerPrintParamTO> fieldToParam = getFieldToParam(detailType.getStickerPrintParams());
        for (int i = 0; i < listItems.size(); ++i) {
            ListItem listItem = listItems.get(i);
            DetailFieldTO field = (DetailFieldTO) listItem.getValue();
            StickerPrintParamTO stickerPrintParamTO = fieldToParam.get(field.getId());
            if (stickerPrintParamTO == null) {
                stickerPrintParamTO = new StickerPrintParamTO();
                stickerPrintParamTO.setDetailTypeId(detailType.getId());
                if (isSerialField(field)) {
                    stickerPrintParamTO.setSerialDetailField(field);
                } else {
                    stickerPrintParamTO.setDetailField(field);
                }
            }
            stickerPrintParamTO.setOrderNum(i);
            result.add(stickerPrintParamTO);
        }
        return result;
    }

    private boolean isSerialField(DetailFieldTO field) {
        return serialNumberFieldsList.getEditedFields().contains(field);
    }

    private Map<Long, StickerPrintParamTO> getFieldToParam(List<StickerPrintParamTO> stickerPrintParams) {
        Map<Long, StickerPrintParamTO> result = new HashMap<Long, StickerPrintParamTO>();
        for (StickerPrintParamTO param : stickerPrintParams) {
            DetailFieldTO detailFieldTO = param.getDetailField() != null ? param.getDetailField() : param.getSerialDetailField();
            result.put(detailFieldTO.getId(), param);
        }
        return result;
    }

    private void initListeners() {
        selectFields.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                List<DetailFieldTO> editFields = getEditFields(serialNumbers.isSelected());
                ChooseDialogResult result = Dialogs.runChooseListDialog(I18nSupport.message("sticker.print.params.fields.selection"), toListItems(editFields), getSelectedItems());
                if (result.isOk()) {
                    selectedItems = result.getSelectedItems();
                    refreshSelectedList();
                }
            }
        });

        tabs.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
                if (tabbedPane.getSelectedComponent() == stickerPrintParamsTab) {
                    refreshSelectedList();
                }
            }
        });

        ActionListener selectedListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                refreshSelectedList();
                refreshPrintTemplates(serialNumbers.isSelected());
            }
        };
        warehouseBatch.addActionListener(selectedListener);
        serialNumbers.addActionListener(selectedListener);
    }

    private void refreshSelectedList() {
        setListItems(selected, getSelectedItems());
    }

    private void setListItems(JList list, List<ListItem> items) {
        DefaultListModel defaultListModel = new DefaultListModel();
        Object objs[] = items.toArray();
        for (Object obj : objs) {
            defaultListModel.addElement(obj);
        }
        list.setModel(defaultListModel);
    }

    private List<ListItem> getSelectedItems() {
        List<DetailFieldTO> editFields = getEditFields(serialNumbers.isSelected());
        HashSet<DetailFieldTO> exists = new HashSet<DetailFieldTO>(editFields);
        List<ListItem> result = new ArrayList<ListItem>();
        for (Iterator<ListItem> it = selectedItems.iterator(); it.hasNext(); ) {
            ListItem listItem = it.next();
            if (exists.contains(listItem.getValue())) {
                result.add(listItem);
            }
        }
        return result;
    }

    private List<ListItem> toListItems(List<DetailFieldTO> fields) {
        List<ListItem> result = new ArrayList<ListItem>();
        for (DetailFieldTO field : fields) {
            result.add(new ListItem(field.getName(), field));
        }
        return result;
    }

    private DetailTypeService getDetailTypesService() {
        return SpringServiceContext.getInstance().getDetailTypesService();
    }

    private StickerPrintParamService getStickerPrintParamService() {
        return SpringServiceContext.getInstance().getStickerPrintParamService();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabs = new JTabbedPane();
        tabs.setEnabled(true);
        contentPanel.add(tabs, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        propertiesTab = new JPanel();
        propertiesTab.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        tabs.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("detail.type.properties.tab"), propertiesTab);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        propertiesTab.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("detail.type.properties.name"));
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("detail.type.properties.description"));
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textName = new JTextField();
        panel1.add(textName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        textDescription = new JTextArea();
        textDescription.setLineWrap(true);
        textDescription.setWrapStyleWord(true);
        scrollPane1.setViewportView(textDescription);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        propertiesTab.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("detail.type.properties.fieldsInPrice")));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("detail.type.properties.nameInPrice"));
        panel2.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        textNameInPrice = new JTextArea();
        textNameInPrice.setLineWrap(true);
        scrollPane2.setViewportView(textNameInPrice);
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("detail.type.properties.miscInPrice"));
        panel2.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("detail.type.properties.typeInPrice"));
        panel2.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane3 = new JScrollPane();
        panel2.add(scrollPane3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        textMiscInPrice = new JTextArea();
        textMiscInPrice.setLineWrap(true);
        scrollPane3.setViewportView(textMiscInPrice);
        final JScrollPane scrollPane4 = new JScrollPane();
        panel2.add(scrollPane4, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        textTypeInPrice = new JTextArea();
        textTypeInPrice.setLineWrap(true);
        scrollPane4.setViewportView(textTypeInPrice);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        propertiesTab.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("detail.type.properties.fields")));
        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(fieldsPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, 300), null, null, 0, false));
        serialNumbersTab = new JPanel();
        serialNumbersTab.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        serialNumbersTab.setEnabled(true);
        tabs.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("detail.type.serial.numbers.tab"), serialNumbersTab);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        serialNumbersTab.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("detail.type.serial.numbers.fields")));
        serialNumberFieldsPanel = new JPanel();
        serialNumberFieldsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(serialNumberFieldsPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, 300), null, null, 0, false));
        stickerPrintParamsTab = new JPanel();
        stickerPrintParamsTab.setLayout(new GridLayoutManager(5, 1, new Insets(5, 5, 5, 5), -1, -1));
        tabs.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("sticker.print.params.tab"), stickerPrintParamsTab);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        stickerPrintParamsTab.add(panel5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel5.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        selectFields = new JButton();
        this.$$$loadButtonText$$$(selectFields, ResourceBundle.getBundle("i18n/warehouse").getString("sticker.print.params.select.fields.change"));
        panel5.add(selectFields, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("sticker.print.params.select.fields"));
        panel5.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane5 = new JScrollPane();
        panel5.add(scrollPane5, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        selected = new JList();
        scrollPane5.setViewportView(selected);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        stickerPrintParamsTab.add(panel6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        serialNumbers = new JRadioButton();
        this.$$$loadButtonText$$$(serialNumbers, ResourceBundle.getBundle("i18n/warehouse").getString("sticker.print.params.serial.numbers"));
        panel6.add(serialNumbers, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(164, 22), null, 0, false));
        warehouseBatch = new JRadioButton();
        this.$$$loadButtonText$$$(warehouseBatch, ResourceBundle.getBundle("i18n/warehouse").getString("sticker.print.params.detail.batch"));
        panel6.add(warehouseBatch, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(164, 22), null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("sticker.print.params.what.to.print"));
        panel6.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("sticker.print.params.print.template"));
        panel6.add(label8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        templates = new JComboBox();
        panel6.add(templates, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(warehouseBatch);
        buttonGroup.add(serialNumbers);
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
        return contentPanel;
    }
}
