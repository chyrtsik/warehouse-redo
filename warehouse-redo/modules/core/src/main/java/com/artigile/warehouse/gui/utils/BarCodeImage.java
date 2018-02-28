/*
 * Copyright (c) 2007-2012 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.utils;

import com.artigile.warehouse.utils.logging.LoggingFacade;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 *
 * @author Aliaksandr.Chyrtsik, 14.11.12
 */
public class BarCodeImage {
    private String barCode;
    private BufferedImage barCodeImage;

    public BarCodeImage(String barCode) {
        this.barCode = barCode;
    }

    public Object getImageData(){
        if (barCodeImage == null && barCode != null){
            //Generate barcode image.
            AbstractBarcodeBean barCodeGenerator = new Code128Bean();

            final int dpi = 150;
            barCodeGenerator.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar width exactly one pixel
            barCodeGenerator.doQuietZone(false);
            barCodeGenerator.setMsgPosition(HumanReadablePlacement.HRP_NONE);

            BitmapCanvasProvider provider = new BitmapCanvasProvider(
                dpi, BufferedImage.TYPE_BYTE_GRAY, true, 0);
            barCodeGenerator.generateBarcode(provider, barCode);
            try {
                provider.finish();
            } catch (IOException e) {
                LoggingFacade.logError(this, "Error generating barcode.", e);
                throw new RuntimeException(e);
            }
            barCodeImage = provider.getBufferedImage();
        }
        return barCodeImage;
    }
}
