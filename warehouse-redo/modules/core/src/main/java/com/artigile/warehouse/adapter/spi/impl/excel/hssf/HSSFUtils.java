/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.excel.hssf;

import com.artigile.warehouse.utils.logging.LoggingFacade;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for HEEF files parsings.
 *
 * @author Aliaksandr.Chyrtsik, 15.08.11
 */
public final class HSSFUtils {
    private HSSFUtils(){
    }

    /**
     * Presses HSSF request to retrieve elements of single type specified by sid.
     * @param docFileSystem document file system.
     * @param sid type of element to be retrieved.
     * @param listener listener for storing of retrieved elements.
     */
    public static void processRequestForRecordsWithSid(POIFSFileSystem docFileSystem, short sid, HSSFListener listener) {
        processRequestForRecordsWithMultipleSid(docFileSystem, new short[]{sid}, listener);
    }

    /**
     * Presses HSSF request to retrieve elements of multiple types specified by array of sids.
     * @param docFileSystem document file system.
     * @param sids types of elements to be retrieved.
     * @param listener listener for storing of retrieved elements.
     */
    public static void processRequestForRecordsWithMultipleSid(POIFSFileSystem docFileSystem, short[] sids, HSSFListener listener) {
        InputStream docInputStream = null;
        try {
            docInputStream = docFileSystem.createDocumentInputStream("Workbook");
            HSSFRequest req = new HSSFRequest();
            for (short sid : sids){
                req.addListener(listener, sid);
            }
            HSSFEventFactory factory = new HSSFEventFactory();
            factory.processEvents(req, docInputStream);
        } catch (IOException e) {
            LoggingFacade.logError(e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (docInputStream != null){
                    docInputStream.close();
                }
            } catch (IOException e) {
                LoggingFacade.logError(e);
            }
        }
    }

    /**
     * Presses HSSF request to retrieve elements of single type specified by sid. Parses only given sheet.
     * @param docFileSystem document file system.
     * @param sheetName sheet to be parsed. Other sheets are ignored.
     * @param sid type of element to be retrieved.
     * @param listener listener for storing of retrieved elements.
     */
    public static void processRequestForSheetRecordsWithSid(POIFSFileSystem docFileSystem, String sheetName, short sid, HSSFListener listener) {
        processRequestForSheetRecordsWithMultipleSid(docFileSystem, sheetName, new short[]{sid}, listener);
    }

    /**
     * Presses HSSF request to retrieve elements of multiple types specified by array of sids. Parses only given sheet.
     * @param docFileSystem document file system.
     * @param sids types of elements to be retrieved.
     * @param sheetName sheet to be parsed. Other sheets are ignored.
     * @param listener listener for storing of retrieved elements.
     */
    public static void processRequestForSheetRecordsWithMultipleSid(POIFSFileSystem docFileSystem, String sheetName, short[] sids, HSSFListener listener) {
        SheetParserListener sheetListener = new SheetParserListener(sheetName, listener);
        processRequestForRecordsWithMultipleSid(docFileSystem, SheetParserListener.makeSidsArray(sids), sheetListener);
    }

    static class SheetParserListener extends AbortableHSSFListener{
        private String targetSheetName;
        private int targetSheetIndex = -1;
        private boolean targetSheetFound;
        private HSSFListener listener;

        private int currentSheetIndex = -1;
        private int currentSheetBOFIndex = -1;

        public SheetParserListener(String targetSheetName, HSSFListener listener){
            this.targetSheetName = targetSheetName;
            this.listener = listener;
        }

        @Override
        public short abortableProcessRecord(Record record) throws HSSFUserException {
            if (record.getSid() == BoundSheetRecord.sid){
                currentSheetIndex++;
                BoundSheetRecord sheetRecord = (BoundSheetRecord)record;
                if (targetSheetName.equals(sheetRecord.getSheetname())){
                    //We found the target sheet. Remember it's index.
                    targetSheetIndex = currentSheetIndex;
                }
            }
            else if (record.getSid() == BOFRecord.sid){
                BOFRecord bofRecord = (BOFRecord)record;
                if (bofRecord.getType() == BOFRecord.TYPE_WORKSHEET){
                    currentSheetBOFIndex++;
                    if (currentSheetBOFIndex == targetSheetIndex){
                        //Starting listening of records for the current sheet.
                        targetSheetFound = true;
                    }
                }
            }
            else if (record.getSid() == EOFRecord.sid){
                if (targetSheetFound){
                    //End of target sheet. Stop further processing.
                    targetSheetFound = false;
                    return -1;
                }
            }
            else if (targetSheetFound){
                if (listener instanceof AbortableHSSFListener){
                    return ((AbortableHSSFListener) listener).abortableProcessRecord(record);
                }
                else{
                    listener.processRecord(record);
                }
            }
            return 0;
        }

        /**
         * Adds specific sids to the list of sids to make this listener working.
         * @param sids initial array of sids.
         * @return merged array of sids.
         */
        public static short[] makeSidsArray(short[] sids) {
            Set<Short> specificSids = new HashSet<Short>();
            Collections.addAll(specificSids, BoundSheetRecord.sid, BOFRecord.sid, EOFRecord.sid);
            for (short sid : sids){
                specificSids.add(sid);
            }
            short[] finalSids = new short[specificSids.size()];
            int i=0;
            for (Short sid : specificSids){
                finalSids[i++] = sid;
            }
            return finalSids;
        }
    }
}
