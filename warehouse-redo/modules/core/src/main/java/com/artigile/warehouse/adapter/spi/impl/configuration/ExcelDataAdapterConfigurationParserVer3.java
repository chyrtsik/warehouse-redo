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
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * Parser for the version 3 of Excel configuration (domain column settings are stored in configuration).
 *
 * @author Aliaksandr.Chyrtsik, 05.11.11
 */
public class ExcelDataAdapterConfigurationParserVer3 implements ExcelDataAdapterConfigurationParser {

    //Raw (not parsed) configuration data.
    private String configString;

    //Parsed configuration data.
    private String fileStreamId;
    private LinkedHashMap<String, List<ColumnRelationship>> sheetsColumnsConfig;

    public ExcelDataAdapterConfigurationParserVer3(String configurationString) {
        this.configString = configurationString;
    }

    @Override
    public String getFileStreamId() throws AdapterConfigurationFormatException {
        parseConfiguration();
        return fileStreamId;
    }

    @Override
    public LinkedHashMap<String, List<ColumnRelationship>> getSheetsColumnsConfig() throws AdapterConfigurationFormatException {
        parseConfiguration();
        return sheetsColumnsConfig;
    }

    private void parseConfiguration() throws AdapterConfigurationFormatException {
        if (fileStreamId != null){
            //Configuration is already parsed.
            return;
        }


        try{
            //Parse configuration with xml parser.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(configString.getBytes("UTF-8")));

            Element configuration = (Element)document.getElementsByTagName("excelDataAdapterConfigurationData").item(0);
            String tempFileStreamId = configuration.getElementsByTagName("fileStreamId").item(0).getTextContent();
            Map<String, DomainColumn> domainColumns = parseDomainColumns((Element)configuration.getElementsByTagName("domainColumns").item(0));
            LinkedHashMap<String, List<ColumnRelationship>> tempSheetsColumnsConfig = parseSheetsColumnsConfig(domainColumns, (Element)configuration.getElementsByTagName("sheets").item(0));

            //Parsing is done. Store temporary data in instance fields.
            fileStreamId = tempFileStreamId;
            sheetsColumnsConfig = tempSheetsColumnsConfig;
        }
        catch (Exception e){
            LoggingFacade.logError(this, "Error when parsing Excel import adapter configuration", e);
            throw new AdapterConfigurationFormatException(e);
        }
    }

    private LinkedHashMap<String, List<ColumnRelationship>> parseSheetsColumnsConfig(Map<String, DomainColumn> domainColumns, Element sheetsElement) {
        LinkedHashMap<String, List<ColumnRelationship>> sheetsConfig = new LinkedHashMap<String, List<ColumnRelationship>>();
        NodeList sheetNodes = sheetsElement.getElementsByTagName("sheet");
        for (int sheetIndex=0; sheetIndex<sheetNodes.getLength(); sheetIndex++){
            Element sheetElement = (Element)sheetNodes.item(sheetIndex);

            List<ColumnRelationship> sheetColumns = new ArrayList<ColumnRelationship>();
            NodeList columnNodes = sheetElement.getElementsByTagName("column");
            for (int columnIndex=0; columnIndex<columnNodes.getLength(); columnIndex++){
                Element columnElement = (Element)columnNodes.item(columnIndex);
                int index = Integer.valueOf(columnElement.getAttribute("index"));
                String domainColumnId = columnElement.getAttribute("domainColumn");
                sheetColumns.add(new ColumnRelationship(domainColumns.get(domainColumnId), index));
            }

            sheetsConfig.put(sheetElement.getAttribute("name"), sheetColumns);
        }
        return sheetsConfig;
    }

    private Map<String, DomainColumn> parseDomainColumns(Element domainColumnsNode) {
        Map<String, DomainColumn> domainColumns = new HashMap<String, DomainColumn>();
        NodeList nodes = domainColumnsNode.getElementsByTagName("domainColumn");
        for (int i=0; i<nodes.getLength(); i++){
            Element node = (Element)nodes.item(i);
            DomainColumn domainColumn;
            if (node.getAttribute("id").equals(DomainColumn.NOT_DEFINED.getId())){
                domainColumn = DomainColumn.NOT_DEFINED;
            }
            else{
                domainColumn = new DomainColumn(
                        node.getAttribute("id"),
                        node.getAttribute("name"),
                        Boolean.valueOf(node.getAttribute("required")),
                        Boolean.valueOf(node.getAttribute("multiple")),
                        node.getAttribute("multipleDelimiter")
                );
            }
            domainColumns.put(domainColumn.getId(), domainColumn);
        }
        return domainColumns;
    }
}
