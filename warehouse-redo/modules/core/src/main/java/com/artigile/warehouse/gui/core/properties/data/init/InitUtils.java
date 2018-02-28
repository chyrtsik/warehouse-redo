/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.data.init;

import com.artigile.warehouse.adapter.spi.DataAdapterFactory;
import com.artigile.warehouse.adapter.spi.DataAdapterInfo;
import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.bl.directory.ManufacturerService;
import com.artigile.warehouse.bl.directory.MeasureUnitService;
import com.artigile.warehouse.bl.finance.CurrencyService;
import com.artigile.warehouse.bl.print.PrintTemplateService;
import com.artigile.warehouse.bl.warehouse.StoragePlaceFilter;
import com.artigile.warehouse.bl.warehouse.StoragePlaceService;
import com.artigile.warehouse.bl.warehouse.WarehouseFilter;
import com.artigile.warehouse.bl.warehouse.WarehouseService;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.*;
import com.artigile.warehouse.utils.dto.details.DetailTypeTOForReport;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import javax.print.PrintService;
import javax.swing.*;
import java.awt.print.PrinterJob;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

/**
 * @author Shyrik, 07.01.2009
 */

/**
 * Utility class for common initialization of UI components.
 */
public final class InitUtils {
    private InitUtils(){}

    /**
     * Fill combo box with list af all load places.
     * @param loadPlacesCombo
     * @param options
     */
    public static void initLoadPlacesCombo(JComboBox loadPlacesCombo, ComboBoxFillOptions options) {
        ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();
        List<LoadPlaceTO> loadPlaces = contractorService.getAllLoadPlaces();
        loadPlacesCombo.removeAllItems();
        for (LoadPlaceTO place : loadPlaces){
            loadPlacesCombo.addItem(new ListItem(place.getName(), place));
        }
        processComboBoxFillOptions(loadPlacesCombo, options);
    }

    /**
     * Fills combo box with list of all manufacturers.
     * @param manufacturersCombo
     * @param options
     */
    public static void initManufacturersCombo(JComboBox manufacturersCombo, ComboBoxFillOptions options) {
        ManufacturerService manufacturersService = SpringServiceContext.getInstance().getManufacturerService();
        List<ManufacturerTO> manufacturers = manufacturersService.getAll();
        for (ManufacturerTO manufacturer : manufacturers) {
            manufacturersCombo.addItem(new ListItem(manufacturer.getName(), manufacturer));
        }
        processComboBoxFillOptions(manufacturersCombo, options);
    }

    /**
     * Fill combo box with list of all currencies.
     * @param currencyCombo
     * @param options
     */
    public static void initCurrenciesCombo(JComboBox currencyCombo, ComboBoxFillOptions options) {
        CurrencyService currencyService = SpringServiceContext.getInstance().getCurrencyService();
        List<CurrencyTO> currencies = currencyService.getAll();
        currencyCombo.removeAllItems();
        for (CurrencyTO currency : currencies){
            ListItem currencyItem = new ListItem(currency.getSign(), currency);
            currencyCombo.addItem(currencyItem);
            if ( currency.getDefaultCurrency() ){
                //Default currency is to be selected in list by default.
                currencyCombo.setSelectedItem(currencyItem);
            }
        }
        processComboBoxFillOptions(currencyCombo, options);
    }

    /**
     * Fill combo box with list of all measure units.
     * @param measuresCombo
     * @param options
     */
    public static void initMeasuresCombo(JComboBox measuresCombo, ComboBoxFillOptions options) {
        MeasureUnitService measureUnitService = SpringServiceContext.getInstance().getMeasureUnitService();
        List<MeasureUnitTO> measures = measureUnitService.getAll();
        measuresCombo.removeAllItems();
        for (MeasureUnitTO measure : measures){
            ListItem measureItem = new ListItem(measure.getSign(), measure);
            measuresCombo.addItem(measureItem);
            if ( measure.getDefaultMeasureUnit() ){
                //Default measure unit is to be selected in list by default.
                measuresCombo.setSelectedItem(measureItem);
            }
        }
        processComboBoxFillOptions(measuresCombo, options);
    }

    /**
     * Fill combo box with list of all legacy warehouses.
     * @param warehouseCombo
     * @param options
     */
    public static void initWarehousesCombo(JComboBox warehouseCombo, ComboBoxFillOptions options) {
        initWarehousesCombo(warehouseCombo, null, options);
    }

    /**
     * Fill combo box with list of all warheouses, that matches the given filter.
     * @param warehouseCombo
     * @param filter
     * @param options
     */
    public static void initWarehousesCombo(JComboBox warehouseCombo, WarehouseFilter filter, ComboBoxFillOptions options) {
        WarehouseService warehouseService = SpringServiceContext.getInstance().getWarehouseService();
        List<WarehouseTOForReport> warehouses = filter == null ? warehouseService.getAllForReport() : warehouseService.getWarehousesByFilter(filter);
        warehouseCombo.removeAllItems();
        for (WarehouseTOForReport warehouse : warehouses){
            warehouseCombo.addItem(new ListItem(warehouse.getName(), warehouse));
        }
        processComboBoxFillOptions(warehouseCombo, options);
    }

    /**
     * Fill combo box with list of all storage splaces, that matches the given filter.
     * @param storagePlaceCombo
     * @param filter
     * @param options
     */
    public static void initStoragePlacesCombo(JComboBox storagePlaceCombo, StoragePlaceFilter filter, ComboBoxFillOptions options) {
        StoragePlaceService storagePlaceService = SpringServiceContext.getInstance().getStoragePlaceService();
        List<StoragePlaceTOForReport> storagePlaces = storagePlaceService.getStoragePlacesByFilter(filter);
        storagePlaceCombo.removeAllItems();
        for (StoragePlaceTOForReport storagePlace : storagePlaces){
            storagePlaceCombo.addItem(new ListItem(storagePlace.getSign(), storagePlace));
        }
        processComboBoxFillOptions(storagePlaceCombo, options);
    }

    /**
     * Fills combo box with list of printers, installed in te system.
     * @param printersCombo
     * @param options
     */
    public static void initPrintersCombo(JComboBox printersCombo, ComboBoxFillOptions options) {
        PrintService[] services = PrinterJob.lookupPrintServices();
        printersCombo.removeAllItems();
        for (PrintService service : services){
            printersCombo.addItem(new ListItem(service.getName(), service.getName()));
        }
        processComboBoxFillOptions(printersCombo, options);
    }

    /**
     * Fills combo box with list of all coutries.
     * @param countriesCombo
     * @param options
     */
    public static void initCountriesCombo(JComboBox countriesCombo, ComboBoxFillOptions options) {
        List<String> countries = SpringServiceContext.getInstance().getContractorService().getAllCountries();
        countriesCombo.removeAllItems();
        for (String country : countries){
            countriesCombo.addItem(new ListItem(country, country));
        }
        processComboBoxFillOptions(countriesCombo, options);
    }

    /**
     * Initializes combo box with items, provided by standard Java enumeration.
     * Enumeration class must provide method "String getName()" for obtaining display names
     * of enumeration values.
     * @param values - array of values of enumeration.
     * @param options
     */
    @SuppressWarnings("unchecked")
    public static void initComboFromEnumeration(JComboBox combo, Object[] values, ComboBoxFillOptions options) {
        try {
            Class valueClass = values.getClass().getComponentType();
            Method nameGetter = valueClass.getMethod("getName");
            nameGetter.setAccessible(true);
            if(!nameGetter.getReturnType().equals(String.class)){
                throw new RuntimeException(MessageFormat.format("Enumeration {0} must have getName method with no arguments and returning String result.", valueClass.getCanonicalName()));
            }

            for (Object value : values){
                combo.addItem(new ListItem((String)nameGetter.invoke(value), value));
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        processComboBoxFillOptions(combo, options);
    }

    /**
     * Initialize combo box from given array of values.
     * @param combo combo box to initialize.
     * @param values values to place into combo box.
     */
    public static <T> void initComboFromArray(JComboBox combo, T[] values) {
        initComboFromArray(combo, values,  null);
    }

    /**
     * Initialize combo box from given array of values.
     * @param combo combo box to initialize.
     * @param values values to place into combo box.
     * @param options addition options to apply during combo filling.
     */
    public static <T> void initComboFromArray(JComboBox combo, T[] values, ComboBoxFillOptions options) {
        for (T value : values){
            combo.addItem(new ListItem(value.toString(), value));
        }
        processComboBoxFillOptions(combo, options);
    }

    /**
     * Applies filling options to the combo box.
     * @param comboBox
     * @param options
     */
    private static void processComboBoxFillOptions(JComboBox comboBox, ComboBoxFillOptions options) {
        if (options == null){
            return;
        }
        if (options.getAddNotSelectedItem()){
            comboBox.insertItemAt(new ListItem(I18nSupport.message("common.not.selected"), null), 0);

            if (options.isSelectNotSelectedByDefault()){
                comboBox.setSelectedIndex(0);
            }
        }
    }

    /**
     * Fill combo box with list of import data adapters.
     * @param adapterCombo
     * @param options
     */
    public static void initAdapterCombo(JComboBox adapterCombo, ComboBoxFillOptions options) {
        Collection<DataAdapterFactory> factories = SpringServiceContext.getInstance().getDataImportService().getAvailableDataAdapterFactories();
        for (DataAdapterFactory factory : factories) {
            DataAdapterInfo dataAdapterInfo = factory.getDataAdapterInfo();
            ListItem adapterItem = new ListItem(dataAdapterInfo.getAdapterName(), dataAdapterInfo.getAdapterUid());
            adapterCombo.addItem(adapterItem);
        }
        adapterCombo.setSelectedItem(null);
        processComboBoxFillOptions(adapterCombo, options);
    }

    /**
     * Fill combo box with list of contactor contacts.
     * @param combo
     * @param contractor
     */
    public static void initContractorContactsCombo(JComboBox combo, ContractorTO contractor, ComboBoxFillOptions options) {
        combo.removeAllItems();
        if (contractor != null){
            ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();
            List<ContactTO> contacts = contractorService.getContactsByContractorId(contractor.getId());
            for (ContactTO contact : contacts){
                combo.addItem(new ListItem(contact.getFullName(), contact));
            }
        }
        processComboBoxFillOptions(combo, options);
    }

    /**
     * Fill combo box with all available product types.
     * @param combo combo box to fill.
     */
    public static void initProductTypesCombo(JComboBox combo) {
        //Initialising list of the detail types.
        List<DetailTypeTOForReport> detailTypes = SpringServiceContext.getInstance().getDetailTypesService().getAllDetailTypes();
        for (DetailTypeTOForReport type : detailTypes) {
            combo.addItem(new ListItem(type.getName(), type));
        }
    }

    public static void initPrintTemplatesCombo(JComboBox combo, PrintTemplateInstanceTO selectedPrintTemplate, PrintTemplateType ...printTemplateTypes) {
        if (printTemplateTypes.length > 0) {
            PrintTemplateService printTemplateService = SpringServiceContext.getInstance().getPrintTemplateService();
            List<PrintTemplateInstanceTO> printTemplates = printTemplateService.getTemplateInstancesByTemplateTypes(printTemplateTypes);
            combo.removeAllItems();
            ListItem cur = null;
            for (PrintTemplateInstanceTO template : printTemplates) {
                ListItem item = new ListItem(template.getName(), template);
                if (selectedPrintTemplate != null && selectedPrintTemplate.getId() == template.getId()) {
                    cur = item;
                }
                combo.addItem(item);
            }
            combo.setSelectedItem(cur);
        }
    }
}
