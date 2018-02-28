/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.installer.utils;

import org.netbeans.installer.utils.LogManager;

import java.io.*;

/**
 * Common utils to work with files and directories.
 *
 * @author vadim.zverugo (vadim.zverugo@artigile.by)
 */
public class FileUtils {

    /* File utils
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * Loads content of the given file at this computer.
     *
     * @param pathToFile Path to the file at the computer.
     * @return Content of the given file.
     */
    public String loadFileContent(String pathToFile) {
        if (fileExists(pathToFile)) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(pathToFile)));
                StringBuilder contentBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    contentBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return contentBuilder.toString();
            } catch (IOException e) {
                LogManager.log(e);
            }
        }
        return null;
    }

    /**
     * Loads content of the given file at the application classpath.
     *
     * @param pathToFile Path to file at the application classpath.
     * @return Content of the given file.
     */
    public String loadClasspathFileContent(String pathToFile) {
        InputStream inputStream = loadClasspathFileStream(pathToFile);
        if (inputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    contentBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return contentBuilder.toString();
            } catch (IOException e) {
                LogManager.log(e);
            }
        }
        return null;
    }

    /**
     * @param pathToFile Path to file at the application classpath.
     * @return Input stream of the given file.
     */
    public InputStream loadClasspathFileStream(String pathToFile) {
        return CommonUtils.isEmptyString(pathToFile)
                ? null
                : this.getClass().getClassLoader().getResourceAsStream(pathToFile);
    }

    /**
     * Stores file with the given content at this computer.
     * Attention! File will be replaced if it's already existed.
     *
     * @param pathToFile Path to the file with a filename.
     * @param content Content, that is saved to the file.
     */
    public void storeFile(String pathToFile, String content) {
        if (!CommonUtils.isEmptyString(pathToFile)) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathToFile, false));
                bufferedWriter.write(content);
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException e) {
                LogManager.log(e);
            }
        }
    }

    /**
     * Appends text to the already existed file.
     *
     * @param pathToFile Path to the file with a filename.
     * @param appendix Text, that is appended to the file content.
     */
    public void appendToFileContent(String pathToFile, String appendix) {
        if (fileExists(pathToFile)) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathToFile, true));
                bufferedWriter.write(appendix);
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException e) {
                LogManager.log(e);
            }
        }
    }
    
    public boolean fileExists(String pathToFile) {
        return !CommonUtils.isEmptyString(pathToFile) && new File(pathToFile).exists();
    }

    /* Directory utils
    ------------------------------------------------------------------------------------------------------------------*/
    public boolean makeDirectory(String pathToDir) {
        return !CommonUtils.isEmptyString(pathToDir) && new File(pathToDir).mkdir();
    }
}
