/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.print.images;

import com.artigile.warehouse.domain.printing.PrintTemplateImage;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 25.01.2009
 */
public class ImageForm implements PropertiesForm {
    private JPanel contentPanel;
    private JTextField imageName;
    private JTextArea imageNotice;
    private JButton saveImage;
    private JButton loadImage;
    private JLabel imageView;
    private JTextField imageFile;

    private PrintTemplateImage image;
    private boolean canEdit;

    public ImageForm(PrintTemplateImage image, boolean canEdit) {
        this.image = image;
        this.canEdit = canEdit;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(imageName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(imageNotice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        initListeners();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("printing.image.properties.title");
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
        imageName.setText(image.getName());
        imageNotice.setText(image.getNotice());
        imageFile.setText(image.getImageFileName());
        imageView.setIcon(getImageForView(image.getImageData()));
        refreshImageButtons();
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(imageName);
        DataValidation.checkCondition(isImageNameUnique(imageName.getText()), imageName, "printing.image.properties.imageNameExists");
        DataValidation.checkCondition(isImageLoaded(), imageView, "printing.image.properties.chooseImage");
    }

    @Override
    public void saveData() {
        image.setName(imageName.getText());
        image.setNotice(imageNotice.getText());
        image.setImageFileName(imageFile.getText());
        image.setImageData(getBytesFromImageView());
    }

    //============================== Helpers and user input processing ======================================
    private Icon getImageForView(byte[] imageData) {
        //Loads image data from the byte array.
        if (imageData == null) {
            return null;
        }
        try {
            return new ImageIcon(ImageIO.read(new ByteArrayInputStream(imageData)));
        }
        catch (IOException ex) {
            MessageDialogs.showWarning(imageView, I18nSupport.message("error.message", ex.getMessage()));
        }
        return null;
    }

    private byte[] getBytesFromImageView() {
        //Stotes image data to the array of bytes.
        ByteArrayOutputStream out = new ByteArrayOutputStream(512);
        BufferedImage imageData = (BufferedImage) ((ImageIcon) imageView.getIcon()).getImage();
        try {
            ImageIO.write(imageData, StringUtils.getFileExtension(imageFile.getText()), out);
        }
        catch (IOException ex) {
            MessageDialogs.showWarning(imageView, I18nSupport.message("error.message", ex.getMessage()));
        }
        return out.toByteArray();
    }

    private void initListeners() {
        saveImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSaveImageToFile();
            }
        });
        loadImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onLoadImageFromFile();
            }
        });
    }

    private void onSaveImageToFile() {
        //Exports image to the file.
        JFileChooser fileChooser = createFileChooser();
        fileChooser.setSelectedFile(new File(imageFile.getText()));
        if (fileChooser.showSaveDialog(contentPanel) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage image = (BufferedImage) ((ImageIcon) imageView.getIcon()).getImage();
                ImageIO.write(image, StringUtils.getFileExtension(file.getName()), file);
            }
            catch (Exception ex) {
                MessageDialogs.showWarning(contentPanel, I18nSupport.message("error.message", ex.getMessage()));
            }
        }
    }

    private void onLoadImageFromFile() {
        //Import printing printing from file.
        JFileChooser fileChooser = createFileChooser();
        if (fileChooser.showOpenDialog(contentPanel) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                imageView.setIcon(new ImageIcon(ImageIO.read(file)));
                imageFile.setText(file.getName());
            }
            catch (Exception ex) {
                MessageDialogs.showWarning(contentPanel, I18nSupport.message("error.message", ex.getMessage()));
            }
        }
        refreshImageButtons();
    }

    private JFileChooser createFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(I18nSupport.message("printing.image.properties.filter.images"), "bmp", "jpg", "gif", "png", "wbmp"));
        return chooser;
    }

    private void refreshImageButtons() {
        saveImage.setEnabled(isImageLoaded());
        loadImage.setEnabled(true);
    }

    private boolean isImageLoaded() {
        return imageView.getIcon() != null;
    }

    private boolean isImageNameUnique(String name) {
        return SpringServiceContext.getInstance().getPrintTemplateService().isUniqueImageName(name, image.getId());
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
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("printing.image.properties.name"));
        contentPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("printing.image.properties.notice"));
        contentPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("printing.image.properties.image")));
        saveImage = new JButton();
        this.$$$loadButtonText$$$(saveImage, ResourceBundle.getBundle("i18n/warehouse").getString("printing.template.properties.export.to.file"));
        panel1.add(saveImage, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadImage = new JButton();
        this.$$$loadButtonText$$$(loadImage, ResourceBundle.getBundle("i18n/warehouse").getString("printing.template.properties.import.from.file"));
        panel1.add(loadImage, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(30);
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(400, 250), null, null, 0, false));
        imageView = new JLabel();
        imageView.setHorizontalAlignment(0);
        imageView.setHorizontalTextPosition(0);
        imageView.setText("");
        scrollPane1.setViewportView(imageView);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("printing.image.properties.imageFile"));
        panel2.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        imageFile = new JTextField();
        imageFile.setEditable(false);
        panel2.add(imageFile, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        imageName = new JTextField();
        contentPanel.add(imageName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        contentPanel.add(scrollPane2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        imageNotice = new JTextArea();
        imageNotice.setRows(0);
        imageNotice.setWrapStyleWord(true);
        scrollPane2.setViewportView(imageNotice);
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
