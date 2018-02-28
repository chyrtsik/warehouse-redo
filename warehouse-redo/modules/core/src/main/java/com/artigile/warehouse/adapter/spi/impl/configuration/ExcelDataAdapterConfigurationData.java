/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.configuration;

import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.*;

/**
 * Class for storing excel data adapter configuration in usable form.
 * Used in UI editors and in parsers (to access columns configuration).
 *
 * @author Aliaksandr.Chyrtsik, 17.08.11
 */
public class ExcelDataAdapterConfigurationData {
    /**
     * Id of file stream to be used for import.
     */
    private String fileStreamId;

    /**
     * Columns configuration for each sheet from the work book.
     */
    private LinkedHashMap<String, List<ColumnRelationship>> sheetsColumnsConfig = new LinkedHashMap<String, List<ColumnRelationship>>();

    /**
     * List of sheet indexes that are configured for import (only sheets with column relations setup performed).
     */
    private LinkedHashMap<String, Integer> configuredSheets = new LinkedHashMap<String, Integer>();

    /**
     * Parses configuration string and returns
     * @param configurationString string dump of configuration.
     * @return parsed configuration object.
     * @throws AdapterConfigurationFormatException is configuration string has invalid format.
     */
    public static ExcelDataAdapterConfigurationData parse(String configurationString) throws AdapterConfigurationFormatException {
        ExcelDataAdapterConfigurationParser parser = ExcelDataAdapterConfigurationParserFactory.getInstance().createParser(configurationString);
        ExcelDataAdapterConfigurationData parsedConfigData = new ExcelDataAdapterConfigurationData();
        parsedConfigData.setFileStreamId(parser.getFileStreamId());
        parsedConfigData.setSheetsColumnsConfig(parser.getSheetsColumnsConfig());
        return parsedConfigData;
    }

    /**
     * This method writes full configuration dump to a string.
     * @return string representing configuration.
     */
    public String toString(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LoggingFacade.logError(this, "Cannot create DOM document builder", e);
            throw new RuntimeException(e);
        }
        Document document = builder.newDocument();
        Element configuration = document.createElement("excelDataAdapterConfigurationData");
        document.appendChild(configuration);

        //1. Write version of current configuration data format.
        configuration.setAttribute("version", "3");

        //2. Write file stream id;
        configuration.appendChild(createTextElement(document, "fileStreamId", fileStreamId));

        //3. Write domain columns.
        Element domainColumnsElement = document.createElement("domainColumns");
        configuration.appendChild(domainColumnsElement);

        Set<DomainColumn> domainColumns = getAllUsedDomainColumns();
        for(DomainColumn domainColumn : domainColumns){
            Element domainColumnElement = document.createElement("domainColumn");
            domainColumnElement.setAttribute("id", domainColumn.getId());
            domainColumnElement.setAttribute("name", domainColumn.getName());
            domainColumnElement.setAttribute("required", String.valueOf(domainColumn.isRequired()));
            domainColumnElement.setAttribute("multiple", String.valueOf(domainColumn.isMultiple()));
            domainColumnElement.setAttribute("multipleDelimiter", domainColumn.getMultipleDelimiter());
            domainColumnsElement.appendChild(domainColumnElement);
        }

        //4. Write column relations for each sheet.
        Element sheetsElement = document.createElement("sheets");
        configuration.appendChild(sheetsElement);

        int sheetIndex = 0;
        for (Map.Entry<String, List<ColumnRelationship>> sheetConfig : sheetsColumnsConfig.entrySet()) {
            Element sheetElement = document.createElement("sheet");
            sheetsElement.appendChild(sheetElement);

            sheetElement.setAttribute("index", String.valueOf(sheetIndex++));
            sheetElement.setAttribute("name", sheetConfig.getKey());

            for (ColumnRelationship relationship : sheetConfig.getValue()) {
                Element columnElement = document.createElement("column");
                columnElement.setAttribute("index", String.valueOf(relationship.getColumnIndex()));
                columnElement.setAttribute("domainColumn", relationship.getDomainColumn().getId());
                sheetElement.appendChild(columnElement);
            }
        }

        //Write configuration to xml string.
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(new DOMSource(document), result);
            return result.getWriter().toString();
        }
        catch (TransformerException e) {
            LoggingFacade.logError(this, "Cannot create XML string from DOM document", e);
            throw new RuntimeException(e);
        }
    }

    private Node createTextElement(Document document, String name, String value) {
        Element element = document.createElement(name);
        element.appendChild(document.createTextNode(value));
        return element;
    }

    private Set<DomainColumn> getAllUsedDomainColumns() {
        HashSet<DomainColumn> usedDomainColumns = new HashSet<DomainColumn>();
        for (List<ColumnRelationship> sheetColumns : sheetsColumnsConfig.values()){
            for (ColumnRelationship sheetColumn : sheetColumns){
                usedDomainColumns.add(sheetColumn.getDomainColumn());
            }
        }
        return usedDomainColumns;
    }

    public String getFileStreamId() {
        return fileStreamId;
    }

    public void setFileStreamId(String fileStreamId) {
        this.fileStreamId = fileStreamId;
    }

    public LinkedHashMap<String, List<ColumnRelationship>> getSheetsColumnsConfig() {
        return sheetsColumnsConfig;
    }

    public void setSheetsColumnsConfig(LinkedHashMap<String, List<ColumnRelationship>> sheetsColumnsConfig) {
        this.sheetsColumnsConfig = sheetsColumnsConfig;
        //... and calculate list of sheets which have configured columns relationships.
        configuredSheets.clear();
        int sheetIndex = 0;
        for (Map.Entry<String, List<ColumnRelationship>> entry : sheetsColumnsConfig.entrySet()) {
            if (sheetsColumnsConfig.get(entry.getKey()).size() > 0) {
                configuredSheets.put(entry.getKey(), sheetIndex);
            }
            ++sheetIndex;
        }
    }

    public LinkedHashMap<String, Integer> getConfiguredSheets() {
        return configuredSheets;
    }
}
