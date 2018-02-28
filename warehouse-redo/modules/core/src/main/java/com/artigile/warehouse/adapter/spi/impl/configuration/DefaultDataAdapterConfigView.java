/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.configuration;

import com.artigile.warehouse.adapter.spi.DataAdapterConfigView;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.EnumConst;

import javax.swing.*;
import java.awt.*;

/**
 * @author Valery Barysok, 6/12/11
 */

public class DefaultDataAdapterConfigView implements DataAdapterConfigView {

    private JScrollPane scrollPane;
    private JTextArea textArea;

    public DefaultDataAdapterConfigView() {
        scrollPane = new JScrollPane();
        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        scrollPane.setMinimumSize(new Dimension(100, 40));
        DataFiltering.setTextLengthLimit(textArea, EnumConst.TEXT_MAX_LENGTH);
    }

    @Override
    public JComponent getView() {
        return scrollPane;
    }

    @Override
    public void setConfigurationString(String conf) {
        textArea.setText(conf);
    }

    @Override
    public String getConfigurationString() {
        return textArea.getText();
    }

    @Override
    public void validateData() throws DataValidationException {
    }
}
