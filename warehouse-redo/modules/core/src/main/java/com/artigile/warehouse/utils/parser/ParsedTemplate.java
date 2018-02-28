/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.parser;

import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 23.12.2008
 */

/**
 * Helper for evaluating values of string templates.
 * Templates are simular to templates, needed by the format method class String.
 * But except number of the variables there are name of the fields.
 *  Example:
 *   Template "{Model}-{Voltage}{Voltage measure unit} {Year} {Manufactor}", when field values are:
 *     - Model = "K10"
 *     - Voltage = "16"
 *     - Voltage measure unit = "V"
 *     - Year = "2006"
 *     - Manufactor = "Sony"
 *   Gives such field value: "K10-10V 2006 Sony".
 */
public class ParsedTemplate {
    /**
     * Parts of a parsed template.
     */
    private List<TemplatePart> templateParts;

    /**
     * Template part for representing invalid template fields.
     */
    private static final TemplatePart INVALID_FIELD_PART = new PlainTextPart("{" + getErrorMessage() + "}");

    /**
     * Parses given template.
     * @param template - string, representingthe template.
     * @param dataSource - like object model for the template (provides fields for the parser).
     */
    public void parse(String template, ParsedTemplateDataSource dataSource) {
        templateParts = new ArrayList<TemplatePart>();
        doParse(templateParts, template, dataSource);
    }

    /**
     * Recursively parses template string and split it into parts.
     * @param parts
     * @param template
     * @param dataSource
     */
    private void doParse(List<TemplatePart> parts, String template, ParsedTemplateDataSource dataSource) {
        TemplateFieldsIterator it = new TemplateFieldsIterator(template);
        while (it.hasNext()){
            it.moveNext();
            if (it.isSimpleField()){
                //A simple field was found.
                int fieldIndex = dataSource.getFieldIndexByName(it.getValue());
                if (fieldIndex != -1){
                    parts.add(new SimpleFieldPart(fieldIndex));
                }
                else{
                    parts.add(INVALID_FIELD_PART);
                }
            }
            else if (it.isHideFieldGroup()){
                //Parse group of fields, that could be hidden.
                List<TemplatePart> partsInGroup = new ArrayList<TemplatePart>();
                doParse(partsInGroup, it.getValue(), dataSource);
                parts.add(new HiddenFieldGroupPart(partsInGroup));
            }
            else{
                //Simple portiong of the text.
                parts.add(new PlainTextPart(it.getValue()));
            }
        }
    }

    /**
     * Calculate value of the tepmlate.
     * @param dataSource - source of the values for the fields.
     * @return
     */
    public String calculate(ParsedTemplateDataSource dataSource) {
        StringBuilder calculatedTemplate = new StringBuilder();
        for (TemplatePart part : templateParts){
            if (part.isVisible(dataSource)){
                calculatedTemplate.append(part.calculateValue(dataSource));
            }
        }
        return calculatedTemplate.toString();
    }

    public static String getErrorMessage() {
        return I18nSupport.message("detail.type.error");
    }

    /**
     * Helper to parse the string into fields, surrounded by { }.
     */
    private class TemplateFieldsIterator {
        private final char FIELD_BEGIN = '{';
        private final char FIELD_END = '}';

        private final char HIDE_GROUP_BEGIN = '[';
        private final char HIDE_GROUP_END = ']';        

        private String parsingString;
        private int begin_pos;
        private int end_pos;

        public TemplateFieldsIterator(String template){
            parsingString = template;
            begin_pos = -1;
            end_pos = -1;
        }

        /**
         * If true -- value is the field name, if false -- is text between the fields.
         * @return
         */
        public boolean isSimpleField(){
           return parsingString.charAt(begin_pos) == FIELD_BEGIN &&
                  parsingString.charAt(end_pos) == FIELD_END;
        }

        /**
         * If true -- value is a substring with group of fields, that may to be hidden.
         * @return
         */
        public boolean isHideFieldGroup() {
            return parsingString.charAt(begin_pos) == HIDE_GROUP_BEGIN &&
                   parsingString.charAt(end_pos) == HIDE_GROUP_END;
        }

        public String getValue(){
            if (isSimpleField() || isHideFieldGroup()){
                //Field name and hide field group is returned without field begin and end characters.
                return parsingString.substring(begin_pos+1, end_pos);
            }
            else{
                return parsingString.substring(begin_pos, end_pos+1);
            }
        }

        public boolean hasNext() {
            return end_pos < parsingString.length() - 1;
        }

        public void moveNext() {
            //Try to find next field, hide group of fields of plaint text.
            int nextFieldBegin = parsingString.indexOf(FIELD_BEGIN, end_pos);
            int nextFieldEnd = parsingString.indexOf(FIELD_END, nextFieldBegin);

            int nextHideGroupBegin = parsingString.indexOf(HIDE_GROUP_BEGIN, end_pos);
            int nextHideGroupEnd = parsingString.indexOf(HIDE_GROUP_END, nextFieldBegin);

            boolean isHideGroup = false;


            if (nextHideGroupBegin != -1 && nextHideGroupEnd != -1){
                if (nextFieldBegin != -1 && nextFieldEnd != -1 &&
                    (nextFieldBegin < nextHideGroupBegin || nextFieldEnd > nextHideGroupEnd))
                {
                    //This is a field part, but not a hide group part.
                }
                else{
                    //This is a hide group part of the template.
                    isHideGroup = true;
                    if ( nextHideGroupBegin - end_pos > 1){
                        //Next and previous parts has some text between it. We are to process this text.
                        begin_pos = end_pos + 1;
                        end_pos = nextHideGroupBegin - 1;
                    }
                    else{
                        //We are to process found hide group.
                        begin_pos = nextHideGroupBegin;
                        end_pos = nextHideGroupEnd;
                    }
                }
            }

            if (!isHideGroup){
                if (nextFieldBegin != -1 && nextFieldEnd != -1){
                    //We found next field.
                    if ( nextFieldBegin - end_pos > 1){
                        //Next and previous field has some text between it. We are to process this text.
                        begin_pos = end_pos + 1;
                        end_pos = nextFieldBegin - 1;
                    }
                    else{
                        //We are to process field found.
                        begin_pos = nextFieldBegin;
                        end_pos = nextFieldEnd;
                    }
                }
                else{
                    //No correct field specification found. We are to process the rest of the string.
                    begin_pos = end_pos + 1;
                    end_pos = parsingString.length() - 1;
                }
            }
        }
    }
}
