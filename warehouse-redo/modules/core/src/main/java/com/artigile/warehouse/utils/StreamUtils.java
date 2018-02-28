/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils;

import java.io.*;

/**
 * Utility class for working with streams.
 *
 * @author Aliaksandr.Chyrtsik, 15.07.11
 */
public final class StreamUtils {
    private StreamUtils(){
    }

    /**
     * Creates temporary file on disk and writes stream content into this file.
     * @param inputStream stream to be written into file.
     * @return file object holding information about temp file. No need to delete this file -- it will
     * be deleted automatically when application exit.
     * @throws java.io.IOException if file cannot be created.
     */
    public static File createTemporaryFileFromStream(InputStream inputStream) throws IOException {
        File tempFile = File.createTempFile("whclient", null);
        tempFile.deleteOnExit();
        createFileFromStream(tempFile, inputStream);
        return tempFile;
    }

    /**
     * Write content of stream to the the file with specified name.
     * @param outFile file to write content to.
     * @param inputStream stread to get data from.
     */
    public static void createFileFromStream(File outFile, InputStream inputStream) throws IOException {
        InputStream in = new BufferedInputStream(inputStream);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
        int b;
        while ((b = in.read()) != -1){
            out.write(b);
        }
        out.close();
    }

    /**
     * Read input stream into array of bytes.
     * @param inputStream stream to be read.
     * @return bytes read.
     * @throws IOException
     */
    public static byte[] streamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        int b;
        while ((b = inputStream.read()) != -1){
            out.write(b);
        }
        out.close();
        return out.toByteArray();
    }
}
