/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.admin;

/**
 * @author Shyrik, 02.12.2008
 */

/**
 * Enum contains identifiers of user rights
 */
public enum PermissionType {
    //====================== Administration ===========
    VIEW_CHANGE_PASS,

    VIEW_GROUPS,
    EDIT_GROUPS,

    VIEW_USERS,
    EDIT_USERS,

    VIEW_PERMISSIONS,

    //====================== Settings ==================
    VIEW_INVENTORY_CONTROL_SETTINGS,
    EDIT_INVENTORY_CONTROL_SETTINGS,

    VIEW_BARCODE_GENERATION_SETTINGS,
    EDIT_BARCODE_GENERATION_SETTINGS,

    VIEW_SERIAL_NUMBERS_SETTINGS,
    EDIT_SERIAL_NUMBERS_SETTINGS,

    VIEW_STICKER_PRINT_PARAMS,
    EDIT_STICKER_PRINT_PARAMS,

    //======================= Printing ==================
    VIEW_PRINT_TEMPLATES_LIST,
    EDIT_PRINT_TEMPLATES_LIST,

    VIEW_PRINT_TEMPLATE_IMAGES_LIST,
    EDIT_PRINT_TEMPLATE_IMAGES_LIST,

    //======================= Finance ====================
    VIEW_EXCHANGE_RATES,
    EDIT_EXCHANGE_RATES,

    //===================== Contractors ==================
    VIEW_CONTRACTORS_LIST,
    EDIT_CONTRACTORS_LIST,
    EDIT_CONTACTS,
    VIEW_ACCOUNT_OPERATIONS_HISTORY,
    VIEW_CONTRACTOR_BALANCE,
    EDIT_CONTRACTOR_BALANCE,

    EDIT_LOAD_PLACE_LIST,
    VIEW_LOAD_PLACE_LIST,

    //====================== Details =====================
    VIEW_DETAIL_TYPES_LIST,
    EDIT_DETAIL_TYPES_LIST,

    VIEW_DETAIL_MODELS_LIST,
    EDIT_DETAIL_MODELS_LIST,

    VIEW_DETAIL_BATCHES_LIST,
    VIEW_DETAIL_BATCH_HISTORY,
    VIEW_DETAIL_BATCH_RESERVES,
    EDIT_DETAIL_BATCHES_LIST,

    VIEW_DETAIL_BATCHES_IMPORT,
    EDIT_DETAIL_BATCHES_IMPORT,

    VIEW_DETAIL_GROUPS,
    EDIT_DETAIL_GROUPS,

    VIEW_DETAIL_CATALOG,
    EDIT_DETAIL_CATALOG,

    VIEW_SERIAL_NUMBERS,
    EDIT_SERIAL_NUMBERS,

    VIEW_STICKER_PRINT,

    //=================== Measure unit ===================
    VIEW_MEASURE_UNIT_LIST,
    EDIT_MEASURE_UNIT_LIST,

    //==================== Manufacturer ==================
    VIEW_MANUFACTURER_LIST,
    EDIT_MANUFACTURER_LIST,

    //==================== Car ==================
    VIEW_CAR_LIST,
    EDIT_CAR_LIST,

    //===================== Warehouses ====================
    VIEW_WAREHOUSE_LIST,
    EDIT_WAREHOUSE_LIST,
    EDIT_WAREHOUSE_USER_LIST,
    EDIT_WAREHOUSE_OTHER,
    EDIT_STORAGE_PLACES_TREE,

    VIEW_WAREHOUSE_BATCH_LIST,
    EDIT_WAREHOUSE_BATCH_LIST,

    //======================= Operations with ware ========
    VIEW_ORDER_LIST,
    EDIT_ORDER_LIST,

    VIEW_ORDER_ITEMS,
    EDIT_ORDER_ITEMS,

    VIEW_POSTING_LIST,
    EDIT_POSTING_LIST,

    VIEW_POSTING_ITEMS,
    EDIT_POSTING_ITEMS,

    EDIT_POSTING_COMPLETION,

    EDIT_COMPLETED_POSTING_ITEM_QUANTITY,

    MOVE_DETAIL_INSIDE_WAREHOUSE,
    EDIT_WAREHOUSE_BATCH_ITEM_QUANTITY,

    VIEW_WARE_NEEDS,  //Place holder (right permission is not used, but may be become useful soon)
    EDIT_WARE_NEEDS,  //Place holder (right permission is not used, but may be become useful soon)

    VIEW_WARE_NEED_ITEMS,
    EDIT_WARE_NEED_ITEMS,

    VIEW_PURCHASE_LIST,
    EDIT_PURCHASE_LIST,

    VIEW_PURCHASE_ITEMS,
    EDIT_PURCHASE_ITEMS,

    //========================= Complecting tasks ===============================
    VIEW_COMPLECTING_TASKS,
    EDIT_COMPLECTING_TASKS,
    VIEW_ALL_COMPLECTING_TASKS,

    //=========================== Currencies ====================================
    VIEW_CURRENCY_LIST,
    EDIT_CURRENCY_LIST,

    //============================ Inventorizations =================================
    VIEW_INVENTORIZATIONS_LIST,
    EDIT_CREATE_DELETE_INVENTORIZATION,
    EDIT_CLOSE_INVENTORIZATION,
    VIEW_INVENTORIZATION_CONTENT,
    EDIT_INVENTORIZATION_CONTENT,

    //========================= Inventorization tasks ===============================
    VIEW_INVENTORIZATION_TASKS,
    VIEW_ALL_INVENTORIZATION_TASKS,
    EDIT_INVENTORIZATION_TASKS,

    //============================ Charge-offs =====================================
    VIEW_CHARGE_OFF_LIST,
    EDIT_CHARGE_OFF_LIST,  //Place holder. May become useful soon.  
    VIEW_CHARGE_OFF_CONTENT,
    EDIT_CHARGE_OFF_CONTENT, //Place holder. May become useful soon.

    //========================= Delivery notes ======================================
    VIEW_DELIVERY_NOTE_LIST,
    EDIT_DELIVERY_NOTE_LIST,
    VIEW_DELIVERY_NOTE_CONTENT,
    EDIT_CREATE_DELIVERY_NOTE,

    //========================== Wares ready for shipping ===========================
    VIEW_READY_FOR_SHIPPING_FROM_WAREHOUSE_LIST,
    EDIT_SHIP_WARES_FROM_WAREHOUSE,

    //========================== Movement of wares between warehouses ===============
    VIEW_MOVEMENT_LIST,
    EDIT_MOVEMENT_LIST,
    VIEW_MOVEMENT_CONTENT,
    EDIT_MOVEMENT_CONTENT,

    //========================== Contractor product list ===============
    VIEW_CONTRACTOR_PRODUCT_LIST,
    EDIT_CONTRACTOR_PRODUCT_LIST,
    VIEW_PRICE_IMPORT_LIST,
    EDIT_PRICE_IMPORT_LIST,
    EDIT_PRICE_IMPORT_ROLLBACK,
    VIEW_PURCHASES_SETTINGS,
    EDIT_PURCHASES_SETTINGS,
    REQUEST_PRICE_LIST,
    REQUEST_SELECTED_POSITIONS_PURCHASE,
    
    //=================== Operation with items in price list ====================
    EDIT_DETAIL_BATCH_SALE_PRICE,

    //================================ Licences  ================================
    VIEW_LICENSES,
    EDIT_LICENSES,

    VIEW_HARDWARE_ID,
}
