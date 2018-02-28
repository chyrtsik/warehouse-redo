/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.browser;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * @author Shyrik, 03.10.2009
 */

/**
 * Tree table cell renderer, that decorates all tree elements by check boxes.
 */
public class CheckBoxTreeCellRenderer extends JPanel implements TreeCellRenderer {
    final private JLabel label = new JLabel();
    final private JCheckBox check = new JCheckBox();

    private CheckBoxTreeCellModel model;

    public CheckBoxTreeCellRenderer() {
        this(null);
    }

    public CheckBoxTreeCellRenderer(CheckBoxTreeCellModel model) {
        this.model = model;

        final Insets inset0=new Insets(0,0,0,0);
        check.setMargin(inset0);
        setLayout(new BorderLayout());
        add(check, BorderLayout.WEST);
        add(label, BorderLayout.CENTER);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean isSelected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        String stringValue = tree.convertValueToText(value, isSelected, expanded, leaf, row, hasFocus);
        setEnabled(tree.isEnabled());
        check.setSelected(model.isItemChecked(value));
        label.setFont(tree.getFont());
        label.setText(stringValue);
        if (leaf) {
            label.setIcon(UIManager.getIcon("Tree.leafIcon"));
        } else if (expanded) {
            label.setIcon(UIManager.getIcon("Tree.openIcon"));
        } else {
            label.setIcon(UIManager.getIcon("Tree.closedIcon"));
        }

        Color bgColor = tree.getBackground();
        setBackground(bgColor);
        check.setBackground(bgColor);

        return this;
    }

    public void setModel(CheckBoxTreeCellModel model) {
        this.model = model;
    }

    public JCheckBox getCheck() {
        return check;
    }
}