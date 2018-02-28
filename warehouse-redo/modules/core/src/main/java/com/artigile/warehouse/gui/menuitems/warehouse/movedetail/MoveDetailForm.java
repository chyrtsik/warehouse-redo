/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.warehouse.movedetail;

import com.artigile.warehouse.bl.warehouse.WarehouseService;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
import com.artigile.warehouse.gui.utils.widget.NumericTextField;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * This class represents GUI and business layer for details movement inside one warehouse.
 * To add new place click add button.
 * To remove entered places click required checkboxes and click delete button.
 *
 * @author IoaN, Feb 15, 2009
 */

public class MoveDetailForm implements PropertiesForm {
    private JPanel panel;
    private JTextField name;
    private JTextField misc;
    private JTextField curPlace;
    private JPanel placesList;
    private JPanel moveToPanel;
    private JButton add;
    private JButton remove;
    private JLabel quantityLabel;
    private JTextField quantity;
    private JLabel edIzm;
    private JTextField availableQuantity;
    private JLabel edIzm2;
    private WarehouseBatchTO warehouseBatchTO;
    List<Long> targetStoragePlaces = new ArrayList<Long>();
    List<Long> amountsToMove = new ArrayList<Long>();

    /**
     * List of view objects to move weteen places inside warehouse.
     */
    private List<NewPlace> newPlaces = new ArrayList<NewPlace>();

    /**
     * List of all places in selected warehouse.
     */
    private Vector<StoragePlaceTO> availablePlaces = new Vector<StoragePlaceTO>();

    /**
     * Warehouse server - used for loading wareshouse content.
     */
    private static final WarehouseService warehouseService = SpringServiceContext.getInstance().getWarehouseService();

    /**
     * The {@link com.artigile.warehouse.gui.menuitems.warehouse.movedetail.MoveDetailForm}  needs {@link com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO}
     * as input parameter to initialize GUI form and to be aware of the storage place <b>from</b> what the detail movement
     * is performing. Also the init place can not be selected in any new place. Combobox will automatically reject the
     * current place from list of available places.
     *
     * @param warehouseBatchTO from what place the detail is moving.
     */
    public MoveDetailForm(WarehouseBatchTO warehouseBatchTO) {
        this.warehouseBatchTO = warehouseBatchTO;
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("warehousebatch.move.details.title");
    }

    @Override
    public JPanel getContentPanel() {
        return panel;
    }

    @Override
    public boolean canSaveData() {
        return true;
    }

    /**
     * Loads all nessesary dataincode GUI form:<br>
     * 1) Detail name<br>
     * 2) Misc<br>
     * 3) Quantity<br>
     * 4) Curent place.<br>
     */
    @Override
    public void loadData() {
        fillStoragePlasesList(warehouseService.getWarehouseFull(warehouseBatchTO.getWarehouse().getId()).getStoragePlaces());
        name.setText(warehouseBatchTO.getDetailBatch().getName());
        misc.setText(warehouseBatchTO.getDetailBatch().getMisc());
        curPlace.setText(warehouseBatchTO.getStoragePlace().getSign());
        quantity.setText(String.valueOf(warehouseBatchTO.getCount()));
        edIzm.setText(warehouseBatchTO.getDetailBatch().getCountMeas().getSign());
        availableQuantity.setText(String.valueOf(warehouseBatchTO.getAvailableCount()));
        edIzm2.setText(warehouseBatchTO.getDetailBatch().getCountMeas().getSign());


        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewPlaceViewItem();
            }
        });
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveToPanel.removeAll();
                Vector<NewPlace> newList = new Vector<NewPlace>();
                for (NewPlace place : newPlaces) {
                    if (!place.isChecked()) {
                        newList.add(place);
                    }
                }
                newPlaces = newList;
                remove.setEnabled(newList.size() > 0);
                for (NewPlace place : newPlaces) {
                    moveToPanel.add(place);
                }
                moveToPanel.revalidate();
                moveToPanel.repaint();
                checkQuantities();
            }
        });
        addNewPlaceViewItem();
    }

    /**
     * Check the entered data before saving.
     *
     * @throws com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException
     *          can be thrown if the old storage place contains negaivve details number.
     */
    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkValueMinLong(Long.valueOf(availableQuantity.getText()), availableQuantity, 0);
    }

    /**
     * Saves the data.<hr>
     * More  detailed:<br>
     * - quantity of details for old place(WarehouseBatch) updated.<br>
     * - new {@link com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO} objects saved into WareHouseBatch table
     * with proper details quantity  <br>
     * - if old WarehouseBatch contains 0 details it will be removed from DB.<br>
     * - if storage place from what the details were moved has 0 details at all, it's fill storage persent will be set to 0;<br>
     */
    @Override
    public void saveData() {
        for (NewPlace newPlace : newPlaces) {
            if (newPlace.getQuantity() > 0) {
                targetStoragePlaces.add(newPlace.getStoragePlace().getId());
                amountsToMove.add((long) newPlace.getQuantity());
            }
        }
    }

    public List<Long> getTargetStoragePlaces() {
        return targetStoragePlaces;
    }

    public List<Long> getAmountsToMove() {
        return amountsToMove;
    }

    /**
     * Adds new place to which details are moved in two objects:
     * 1) JPanel - GUI representation.
     * 2) list of data representation
     */
    private void addNewPlaceViewItem() {
        NewPlace place = new NewPlace();
        newPlaces.add(place);
        moveToPanel.add(place);
        moveToPanel.revalidate();
        remove.setEnabled(true);
    }

    /**
     * Scans all the places in warehouse and builds one single list from all of them.
     * Called right after the data will be initialized.
     * Used when creating combobox for selecting new storage place.
     *
     * @param storagePlace - root storage place.
     */
    private void fillStoragePlasesList(List<StoragePlaceTO> storagePlace) {
        for (StoragePlaceTO place : storagePlace) {
            if (place.getId() != warehouseBatchTO.getStoragePlace().getId()) {
                availablePlaces.add(place);
            }
            fillStoragePlasesList(place.getStoragePlaces());
        }
    }

    /**
     * The GUI objects that need custom initialization.
     */
    private void createUIComponents() {
        moveToPanel = new JPanel(new VerticalLayout());
    }

    /**
     * Dynamically changes the old place details quantity and shows error ballon message if old warehouse Batch has
     * negative quantity.
     */
    private void checkQuantities() {
        int sum = 0;
        for (NewPlace place : newPlaces) {
            sum += place.getQuantity();
        }
        availableQuantity.setText(String.valueOf(warehouseBatchTO.getAvailableCount() - sum));
        quantity.setText(String.valueOf(warehouseBatchTO.getCount() - sum));
        try {
            DataValidation.checkValueMinLong(warehouseBatchTO.getAvailableCount() - sum, availableQuantity, 0);
        } catch (DataValidationException e) {
            UIComponentUtils.showCommentOnComponent(availableQuantity, e.getMessage());
        }
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
        createUIComponents();
        panel = new JPanel();
        panel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:180px:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:150px:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.move.details.name"));
        CellConstraints cc = new CellConstraints();
        panel.add(label1, cc.xy(1, 1));
        name = new JTextField();
        name.setEditable(false);
        panel.add(name, cc.xyw(3, 1, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.move.details.detailMisc"));
        panel.add(label2, cc.xy(1, 3));
        misc = new JTextField();
        misc.setEditable(false);
        panel.add(misc, cc.xyw(3, 3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.move.details.currentPlace"));
        panel.add(label3, cc.xy(1, 9));
        curPlace = new JTextField();
        curPlace.setEditable(false);
        panel.add(curPlace, cc.xyw(3, 9, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        placesList = new JPanel();
        placesList.setLayout(new FormLayout("", ""));
        panel.add(placesList, cc.xyw(1, 15, 7));
        quantity = new JTextField();
        quantity.setEditable(false);
        panel.add(quantity, cc.xy(3, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        quantityLabel = new JLabel();
        this.$$$loadLabelText$$$(quantityLabel, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.move.details.count"));
        panel.add(quantityLabel, cc.xy(1, 5));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel.add(scrollPane1, cc.xyw(1, 13, 7, CellConstraints.FILL, CellConstraints.FILL));
        scrollPane1.setViewportView(moveToPanel);
        edIzm = new JLabel();
        edIzm.setText("<Meas>");
        panel.add(edIzm, cc.xy(5, 5));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.move.details.availableForMove"));
        panel.add(label4, cc.xy(1, 7));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, cc.xyw(1, 11, 5));
        add = new JButton();
        this.$$$loadButtonText$$$(add, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.move.details.addNewPlace"));
        panel1.add(add, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        remove = new JButton();
        this.$$$loadButtonText$$$(remove, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.move.details.removePlace"));
        panel1.add(remove, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        availableQuantity = new JTextField();
        availableQuantity.setEditable(false);
        panel.add(availableQuantity, cc.xy(3, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        edIzm2 = new JLabel();
        edIzm2.setText("<Meas>");
        panel.add(edIzm2, cc.xy(5, 7));
        label1.setLabelFor(name);
        label2.setLabelFor(misc);
        label3.setLabelFor(curPlace);
        quantityLabel.setLabelFor(quantity);
        label4.setLabelFor(quantity);
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
        return panel;
    }

    private class NewPlace extends JPanel {
        JComboBox combo = new JComboBox();
        NumericTextField field = new NumericTextField(true, true);
        JCheckBox checkBox = new JCheckBox();

        public NewPlace() {
            initModel();
            buildLayout();
            initListeners();
        }

        public StoragePlaceTO getStoragePlace() {
            return (StoragePlaceTO) combo.getSelectedItem();
        }

        public int getQuantity() {
            return (Integer) field.getNumber();
        }

        public boolean isChecked() {
            return checkBox.isSelected();
        }


        private void initListeners() {
            field.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    checkQuantities();
                }
            });
        }

        private void initModel() {
            ComboBoxModel model = new DefaultComboBoxModel(availablePlaces);
            combo.setModel(model);
        }

        private void buildLayout() {
            setLayout(new HorizontalLayout());
            combo.setPreferredSize(new Dimension(163, 20));
            field.setPreferredSize(new Dimension(130, 20));
            add(checkBox, BorderLayout.EAST);
            add(combo, BorderLayout.EAST);
            add(field, BorderLayout.EAST);
        }
    }
}
