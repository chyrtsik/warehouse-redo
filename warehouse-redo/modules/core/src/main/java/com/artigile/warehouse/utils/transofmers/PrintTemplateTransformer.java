/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.bl.print.PrintTemplateService;
import com.artigile.warehouse.domain.printing.PrintTemplate;
import com.artigile.warehouse.domain.printing.PrintTemplateFieldMapping;
import com.artigile.warehouse.domain.printing.PrintTemplateInstance;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;
import com.artigile.warehouse.utils.dto.template.PrintTemplateTO;
import com.artigile.warehouse.utils.dto.template.TemplateFieldMappingTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author IoaN, Dec 28, 2008
 */

public final class PrintTemplateTransformer {
    private PrintTemplateTransformer(){
    }

    public static List<TemplateFieldMappingTO> transformTemplateFieldMappingList(List<PrintTemplateFieldMapping> fieldsMapping) {
        List<TemplateFieldMappingTO> newList = new ArrayList<TemplateFieldMappingTO>();
        for (PrintTemplateFieldMapping templateFieldMapping : fieldsMapping) {
            newList.add(transformTemplateFieldMapping(templateFieldMapping));
        }
        return newList;
    }

    public static TemplateFieldMappingTO transformTemplateFieldMapping(PrintTemplateFieldMapping templateFieldMapping) {
        return new TemplateFieldMappingTO(templateFieldMapping.getId(), templateFieldMapping.getReportField(), templateFieldMapping.getObjectField());
    }

    public static List<PrintTemplateInstanceTO> transformTemplateInstanceList(List<PrintTemplateInstance> list) {
        List<PrintTemplateInstanceTO> newList = new ArrayList<PrintTemplateInstanceTO>();
        for (PrintTemplateInstance printTemplateInstance : list) {
            newList.add(transformPrintTemplateInstance(printTemplateInstance));
        }
        return newList;
    }

    public static PrintTemplateInstanceTO transformPrintTemplateInstance(PrintTemplateInstance templateInstance) {
        if (templateInstance == null) {
            return null;
        }
        PrintTemplateInstanceTO templateInstanceTO = new PrintTemplateInstanceTO();
        updatePrintTemplateInstance(templateInstanceTO, templateInstance);
        return templateInstanceTO;
    }

    public static void updatePrintTemplateInstance(PrintTemplateInstanceTO templateInstanceTO, PrintTemplateInstance templateInstance) {
        templateInstanceTO.setId(templateInstance.getId());
        templateInstanceTO.setTemplate(transformPrintTemplate(templateInstance.getPrintTemplate()));
        templateInstanceTO.setDefaultTemplate(templateInstance.isDefaultTemplate());
        templateInstanceTO.setName(templateInstance.getName());
        templateInstanceTO.setDescription(templateInstance.getDescription());
        templateInstanceTO.setTemplateFile(StoredFileTransformer.transform(templateInstance.getTemplateFile()));
    }

    private static PrintTemplateTO transformPrintTemplate(PrintTemplate printTemplate) {
        PrintTemplateTO printTemplateTO = new PrintTemplateTO();
        printTemplateTO.setId(printTemplate.getId());
        printTemplateTO.setName(printTemplate.getName());
        return printTemplateTO;
    }

    public static List<PrintTemplateFieldMapping> fieldMappingList(List<TemplateFieldMappingTO> fieldMappingsList) {
        List<PrintTemplateFieldMapping> newListTemplateFieldMappings = new ArrayList<PrintTemplateFieldMapping>();
        for (TemplateFieldMappingTO templateFieldMappingTO : fieldMappingsList) {
            newListTemplateFieldMappings.add(templateFieldMapping(templateFieldMappingTO));
        }
        return newListTemplateFieldMappings;
    }

    private static PrintTemplateFieldMapping templateFieldMapping(TemplateFieldMappingTO templateFieldMappingTO) {
        return new PrintTemplateFieldMapping(templateFieldMappingTO.getObjectField(), templateFieldMappingTO.getReportField());
    }

    private static PrintTemplateService getPrintTemplateService() {
        return SpringServiceContext.getInstance().getPrintTemplateService();
    }

    public static List<PrintTemplateTO> transformTemplateList(List<PrintTemplate> list) {
        List<PrintTemplateTO> transformedList = new ArrayList<PrintTemplateTO>(list.size());
        for (PrintTemplate template : list){
            transformedList.add(transformPrintTemplate(template));
        }
        return transformedList;
    }
}
