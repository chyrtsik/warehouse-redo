/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.database.update.java;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.plugin.PluginType;
import com.artigile.warehouse.gui.menuitems.admin.password.PasswordChangePlugin;
import com.artigile.warehouse.gui.menuitems.chargeoff.list.ChargeOffList;
import com.artigile.warehouse.gui.menuitems.complecting.complectingTasks.ComplectingTaskList;
import com.artigile.warehouse.gui.menuitems.complecting.readyForShipping.ReadyForShippingFromWarehouseList;
import com.artigile.warehouse.gui.menuitems.deliveryNote.DeliveryNoteList;
import com.artigile.warehouse.gui.menuitems.deliveryNote.WaitingDeliveryNoteList;
import com.artigile.warehouse.gui.menuitems.details.catalog.DetailCatalog;
import com.artigile.warehouse.gui.menuitems.inventorization.InventorizationList;
import com.artigile.warehouse.gui.menuitems.inventorization.task.InventorizationTaskList;
import com.artigile.warehouse.gui.menuitems.inventorization.task.InventorizationTaskWorkerList;
import com.artigile.warehouse.gui.menuitems.movement.MovementList;
import com.artigile.warehouse.gui.menuitems.priceimport.FilteredContractorProductList;
import com.artigile.warehouse.gui.menuitems.priceimport.importing.PriceImportList;
import com.artigile.warehouse.gui.menuitems.warehouse.warehouselist.WarehouseList;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.jetbrains.annotations.PropertyKey;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Valery.Barysok
 */
public class V1_0_2__InitData implements JavaMigration {

    private static final String sqlUserPermissionInsert = "insert into UserPermission (rightType, name) values (?, ?);";
    private static final String sqlMenuItemInsert = "insert into MenuItem (name, pluginType, pluginClassName, viewPermission_id) values (?, ?, ?, (select id from UserPermission where rightType=?));";
    private static final String sqlWarehouseInsert = "insert into Warehouse (name) values (?);";
    private static final String sqlUserGroupInsert = "insert into UserGroup (name, description, predefined) values (?, ?, ?);";
    private static final String sqlAdminUserGroupPermissionsInsert = "insert into UserGroup_UserPermission (userGroup_Id, UserPermission_Id) select (select id from UserGroup where name=?), id from UserPermission;";
    private static final String sqlUserInsert = "insert into User (login, password, firstName, predefined) values (?, ?, ?, ?);";
    private static final String sqlAdminUserGroupInsert = "insert into User_UserGroup (user_Id, userGroup_Id) select (select id from User where login=?), (select id from UserGroup where name=?) from dual;";
    private static final String sqlCurrencyInsert = "insert into Currency (sign, name) values (?, ?);";
    private static final String sqlWareNeedInsert = "insert into WareNeed (id) values (?);";
    //UserGroup

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jt) throws Exception {
        this.jdbcTemplate = jt;

        initUserPermissions();
        initMenuTree();
        initWarehouse();
        initAdministrator();
        initCurrencies();
        initWareNeeds();
    }

    private void initUserPermissions() {
        //Initialization of view permissions
        initPermission(PermissionType.VIEW_CHANGE_PASS, "permission.viewChangePass");
        initPermission(PermissionType.VIEW_GROUPS, "permission.viewGroups");
        initPermission(PermissionType.VIEW_USERS, "permission.viewUsers");
        initPermission(PermissionType.VIEW_PERMISSIONS, "permission.viewPermissions");
        initPermission(PermissionType.VIEW_PRINT_TEMPLATES_LIST, "permission.viewPrintTemplatesList");
        initPermission(PermissionType.VIEW_PRINT_TEMPLATE_IMAGES_LIST, "permission.viewPrintTemplateImagesList");
        initPermission(PermissionType.VIEW_EXCHANGE_RATES, "permission.viewExchangeRates");
        initPermission(PermissionType.VIEW_CONTRACTORS_LIST, "permission.viewContractorsList");
        initPermission(PermissionType.VIEW_DETAIL_TYPES_LIST, "permission.viewDetailTypesList");
        initPermission(PermissionType.VIEW_DETAIL_MODELS_LIST, "permission.viewDetailModelsList");
        initPermission(PermissionType.VIEW_DETAIL_BATCHES_LIST, "permission.viewDetailBatchesList");
        initPermission(PermissionType.VIEW_DETAIL_GROUPS, "permission.viewDetailGroups");
        initPermission(PermissionType.VIEW_MEASURE_UNIT_LIST, "permission.viewMeasureUnitList");
        initPermission(PermissionType.VIEW_MANUFACTURER_LIST, "permission.viewManufacturerList");
        initPermission(PermissionType.VIEW_WAREHOUSE_LIST, "permission.viewWarehouseList");
        initPermission(PermissionType.VIEW_DETAIL_CATALOG, "permission.viewDetailCatalog");
        initPermission(PermissionType.VIEW_LOAD_PLACE_LIST, "permission.viewLoadPlaceList");
        initPermission(PermissionType.VIEW_WAREHOUSE_BATCH_LIST, "permission.viewWarehouseBatchList");
        initPermission(PermissionType.VIEW_ORDER_LIST, "permission.viewOrderList");
        initPermission(PermissionType.VIEW_ORDER_ITEMS, "permission.viewOrderItems");
        initPermission(PermissionType.VIEW_POSTING_LIST, "permission.viewPostingList");
        initPermission(PermissionType.VIEW_POSTING_ITEMS, "permission.viewPostingItems");
        initPermission(PermissionType.VIEW_WARE_NEED_ITEMS, "permission.viewWareNeedItems");
        initPermission(PermissionType.VIEW_PURCHASE_LIST, "permission.viewPurchaseList");
        initPermission(PermissionType.VIEW_PURCHASE_ITEMS, "permission.viewPurchaseItems");
        initPermission(PermissionType.VIEW_COMPLECTING_TASKS, "permission.viewComplectingTasks");
        initPermission(PermissionType.VIEW_ALL_COMPLECTING_TASKS, "permission.viewAllComplectingTasks");
        initPermission(PermissionType.VIEW_CURRENCY_LIST, "permission.viewCurrencyList");
        initPermission(PermissionType.VIEW_INVENTORIZATIONS_LIST, "permission.viewInventorizationList");
        initPermission(PermissionType.VIEW_INVENTORIZATION_CONTENT, "permission.viewInventorizationContent");
        initPermission(PermissionType.VIEW_INVENTORIZATION_TASKS, "permission.viewInventorizationTasks");
        initPermission(PermissionType.VIEW_ALL_INVENTORIZATION_TASKS, "permission.viewAllInventorizationTasks");
        initPermission(PermissionType.VIEW_CHARGE_OFF_LIST, "permission.viewChargeOffList");
        initPermission(PermissionType.VIEW_CHARGE_OFF_CONTENT, "permission.viewChargeOffContent");
        initPermission(PermissionType.VIEW_DELIVERY_NOTE_LIST, "permission.viewDeliveryNoteList");
        initPermission(PermissionType.VIEW_DELIVERY_NOTE_CONTENT, "permission.viewDeliveryNoteContent");
        initPermission(PermissionType.VIEW_READY_FOR_SHIPPING_FROM_WAREHOUSE_LIST, "permission.viewReadyForShippingFromWarehouseList");
        initPermission(PermissionType.VIEW_MOVEMENT_LIST, "permission.viewMovementList");
        initPermission(PermissionType.VIEW_MOVEMENT_CONTENT, "permission.viewMovementContent");
        initPermission(PermissionType.VIEW_ACCOUNT_OPERATIONS_HISTORY, "permission.viewAccountOperationsHistory");
        initPermission(PermissionType.VIEW_CONTRACTOR_PRODUCT_LIST, "permission.viewContractorProductList");
        initPermission(PermissionType.VIEW_PRICE_IMPORT_LIST, "permission.viewPriceImportList");

        //Initialization of edit permissions
        initPermission(PermissionType.EDIT_USERS, "permission.editUsers");
        initPermission(PermissionType.EDIT_PRINT_TEMPLATES_LIST, "permission.editPrintTemplatesList");
        initPermission(PermissionType.EDIT_PRINT_TEMPLATE_IMAGES_LIST, "permission.editPrintTemplateImagesList");
        initPermission(PermissionType.EDIT_EXCHANGE_RATES, "permission.editCurrencyExchange");
        initPermission(PermissionType.EDIT_CONTRACTORS_LIST, "permission.editContractors");
        initPermission(PermissionType.EDIT_GROUPS, "permission.editGroups");
        initPermission(PermissionType.EDIT_DETAIL_TYPES_LIST, "permission.editDetailTypesList");
        initPermission(PermissionType.EDIT_DETAIL_MODELS_LIST, "permission.editDetailModelsList");
        initPermission(PermissionType.EDIT_DETAIL_BATCHES_LIST, "permission.editDetailBatchesList");
        initPermission(PermissionType.EDIT_DETAIL_GROUPS, "permission.editDetailGroups");
        initPermission(PermissionType.EDIT_MEASURE_UNIT_LIST, "permission.editMeasureUnitList");
        initPermission(PermissionType.EDIT_MANUFACTURER_LIST, "permission.editManufacturerList");
        initPermission(PermissionType.EDIT_WAREHOUSE_LIST, "permission.editWarehouseList");
        initPermission(PermissionType.EDIT_WAREHOUSE_USER_LIST, "permission.editWarehouseUserList");
        initPermission(PermissionType.EDIT_WAREHOUSE_OTHER, "permission.editWarehouseOther");
        initPermission(PermissionType.EDIT_DETAIL_CATALOG, "permission.editDetailCatalog");
        initPermission(PermissionType.EDIT_STORAGE_PLACES_TREE, "permission.editStoragePlacesTree");
        initPermission(PermissionType.EDIT_LOAD_PLACE_LIST, "permission.editLoadPlaceList");
        initPermission(PermissionType.EDIT_WAREHOUSE_BATCH_LIST, "permission.editWarehouseBatchList");
        initPermission(PermissionType.EDIT_ORDER_LIST, "permission.editOrderList");
        initPermission(PermissionType.EDIT_ORDER_ITEMS, "permission.editOrderItems");
        initPermission(PermissionType.EDIT_POSTING_LIST, "permission.editPostingList");
        initPermission(PermissionType.EDIT_POSTING_ITEMS, "permission.editPostingItems");
        initPermission(PermissionType.EDIT_POSTING_COMPLETION, "permission.editCompletionPosting");
        initPermission(PermissionType.EDIT_WARE_NEED_ITEMS, "permission.editWareNeedItems");
        initPermission(PermissionType.EDIT_PURCHASE_LIST, "permission.editPurchaseList");
        initPermission(PermissionType.EDIT_PURCHASE_ITEMS, "permission.editPurchaseItems");
        initPermission(PermissionType.EDIT_COMPLECTING_TASKS, "permission.editComplectingTasks");
        initPermission(PermissionType.EDIT_CURRENCY_LIST, "permission.editCurrencyList");
        initPermission(PermissionType.MOVE_DETAIL_INSIDE_WAREHOUSE, "permission.move.detail.inside.warehouse");
        initPermission(PermissionType.EDIT_CREATE_DELETE_INVENTORIZATION, "permission.editCreateDeleteInventorization");
        initPermission(PermissionType.EDIT_CLOSE_INVENTORIZATION, "permission.editCloseInventorization");
        initPermission(PermissionType.EDIT_INVENTORIZATION_CONTENT, "permission.editInventorizationContent");
        initPermission(PermissionType.EDIT_INVENTORIZATION_TASKS, "permission.editInventorizationTasks");
        initPermission(PermissionType.EDIT_DELIVERY_NOTE_LIST, "permission.editDeliveryNoteList");
        initPermission(PermissionType.EDIT_CREATE_DELIVERY_NOTE, "permission.editCreateDeliveryNote");
        initPermission(PermissionType.EDIT_SHIP_WARES_FROM_WAREHOUSE, "permission.editShipWaresFromWarehouse");
        initPermission(PermissionType.EDIT_MOVEMENT_LIST, "permission.editMovementList");
        initPermission(PermissionType.EDIT_MOVEMENT_CONTENT, "permission.editMovementContent");
        initPermission(PermissionType.EDIT_DETAIL_BATCH_SALE_PRICE, "permission.editItemSalePrice");
        initPermission(PermissionType.EDIT_PRICE_IMPORT_LIST, "permission.editPriceImportList");
    }

    private void initPermission(PermissionType permissionType, @PropertyKey(resourceBundle = "i18n.warehouse") String permissionNameRes) {
        jdbcTemplate.update(sqlUserPermissionInsert, permissionType.name(), I18nSupport.message(permissionNameRes));
    }

    private void initMenuTree() {
        initMenuItem("menutree.admin.users.groups", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.admin.groups.UserGroupsList.class, PermissionType.VIEW_GROUPS);
        initMenuItem("menutree.admin.users.users", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.admin.users.UsersList.class, PermissionType.VIEW_USERS);
        initMenuItem("menutree.admin.users.permissions", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.admin.permissions.UserPermissionList.class, PermissionType.VIEW_PERMISSIONS);
        initMenuItem("menutree.admin.changePassword", PluginType.CUSTOM, PasswordChangePlugin.class, PermissionType.VIEW_CHANGE_PASS);
        initMenuItem("menutree.printing.templates", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.print.templates.TemplateList.class, PermissionType.VIEW_PRINT_TEMPLATES_LIST);
        initMenuItem("menutree.printing.templateImages", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.print.images.ImagesList.class, PermissionType.VIEW_PRINT_TEMPLATE_IMAGES_LIST);
        initMenuItem("menutree.finance.exchange.rates", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.finance.exchangeRate.ExchangeRatesList.class, PermissionType.VIEW_EXCHANGE_RATES);
        initMenuItem("menutree.finance.currency.list", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.finance.currency.CurrencyList.class, PermissionType.VIEW_CURRENCY_LIST);
        initMenuItem("menutree.contr.agents.list", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.contractors.ContractorsList.class, PermissionType.VIEW_CONTRACTORS_LIST);
        initMenuItem("menutree.contr.loadPlace.list", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.contractors.loadplace.LoadPlaceList.class, PermissionType.VIEW_LOAD_PLACE_LIST);
        initMenuItem("menutree.details.types.list", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.details.types.DetailTypesList.class, PermissionType.VIEW_DETAIL_TYPES_LIST);
        initMenuItem("menutree.details.models.list", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.details.models.DetailModelsList.class, PermissionType.VIEW_DETAIL_MODELS_LIST);
        initMenuItem("menutree.details.groups.editor", PluginType.CUSTOM, com.artigile.warehouse.gui.menuitems.details.catalog.DetailCatalogStructureEditor.class, PermissionType.VIEW_DETAIL_GROUPS);
        initMenuItem("menutree.basedirectory.measureunit.list", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.basedirectory.measureunit.MeasureUnitList.class, PermissionType.VIEW_MEASURE_UNIT_LIST);
        initMenuItem("menutree.basedirectory.manufacturer.list", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.basedirectory.manufacturer.ManufacturerList.class, PermissionType.VIEW_MANUFACTURER_LIST);
        initMenuItem("menutree.warehouse.warehouse.list", PluginType.TABLE_REPORT, WarehouseList.class, PermissionType.VIEW_WAREHOUSE_LIST);
        initMenuItem("menutree.warehouse.batches.list", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.details.batches.DetailBatchesList.class, PermissionType.VIEW_DETAIL_BATCHES_LIST);
        initMenuItem("menutree.warehouse.batchesext.list", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.details.batchesext.DetailBatchesExtList.class, PermissionType.VIEW_DETAIL_BATCHES_LIST);
        initMenuItem("menutree.warehouse.detail.catalog", PluginType.CUSTOM, DetailCatalog.class, PermissionType.VIEW_DETAIL_CATALOG);
        initMenuItem("menutree.warehouse.warehousebatches.list", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.warehouse.warehousebatch.WarehouseBatchList.class, PermissionType.VIEW_WAREHOUSE_BATCH_LIST);
        initMenuItem("menutree.ware.orders.list", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.orders.OrdersList.class, PermissionType.VIEW_ORDER_LIST);
        initMenuItem("menutree.ware.postings.list", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.postings.PostingsList.class, PermissionType.VIEW_POSTING_LIST);
        initMenuItem("menutree.ware.needs.list", PluginType.CUSTOM, com.artigile.warehouse.gui.menuitems.needs.WareNeedItemsEditor.class, PermissionType.VIEW_WARE_NEED_ITEMS);
        initMenuItem("menutree.ware.purchases.list", PluginType.TABLE_REPORT, com.artigile.warehouse.gui.menuitems.purchase.PurchaseList.class, PermissionType.VIEW_PURCHASE_LIST);
        initMenuItem("menutree.complectingWorkerTasks.list", PluginType.CUSTOM, com.artigile.warehouse.gui.menuitems.complecting.ComplectingTaskWorkerList.class, PermissionType.VIEW_COMPLECTING_TASKS);
        initMenuItem("menutree.complectingTasks.list", PluginType.TABLE_REPORT, ComplectingTaskList.class, PermissionType.VIEW_ALL_COMPLECTING_TASKS);
        initMenuItem("menutree.inventorizations.list", PluginType.TABLE_REPORT, InventorizationList.class, PermissionType.VIEW_INVENTORIZATIONS_LIST);
        initMenuItem("menutree.inventorizationWorkerTasks.list", PluginType.CUSTOM, InventorizationTaskWorkerList.class, PermissionType.VIEW_INVENTORIZATION_TASKS);
        initMenuItem("menutree.inventorizationTasks.list", PluginType.TABLE_REPORT, InventorizationTaskList.class, PermissionType.VIEW_ALL_INVENTORIZATION_TASKS);
        initMenuItem("menutree.chargeOff.list", PluginType.TABLE_REPORT, ChargeOffList.class, PermissionType.VIEW_CHARGE_OFF_LIST);
        initMenuItem("menutree.deliveryNote.list", PluginType.TABLE_REPORT, DeliveryNoteList.class, PermissionType.VIEW_DELIVERY_NOTE_LIST);
        initMenuItem("menutree.waitingDeliveryNote.list", PluginType.CUSTOM, WaitingDeliveryNoteList.class, PermissionType.VIEW_DELIVERY_NOTE_LIST);
        initMenuItem("menutree.readyForShippingFromWarehouse.list", PluginType.CUSTOM, ReadyForShippingFromWarehouseList.class, PermissionType.VIEW_READY_FOR_SHIPPING_FROM_WAREHOUSE_LIST);
        initMenuItem("menutree.movement.list", PluginType.TABLE_REPORT, MovementList.class, PermissionType.VIEW_MOVEMENT_LIST);
        initMenuItem("menutree.purchases.contractor.product.list", PluginType.CUSTOM, FilteredContractorProductList.class, PermissionType.VIEW_CONTRACTOR_PRODUCT_LIST);
        initMenuItem("menutree.purchases.import.price.list", PluginType.TABLE_REPORT, PriceImportList.class, PermissionType.VIEW_PRICE_IMPORT_LIST);
    }

    /**
     * Initialization of a single menu item.
     *
     * @param menuNameRes     - name of menu (resource id).
     * @param pluginType      - type of plugin, that is associated with menu item.
     * @param pluginClass     - plugin class.
     * @param viewPermission  - permission to view menu item.
     */
    private void initMenuItem(@PropertyKey(resourceBundle = "i18n.warehouse") String menuNameRes, PluginType pluginType, Class pluginClass, PermissionType viewPermission) {
        jdbcTemplate.update(sqlMenuItemInsert, I18nSupport.message(menuNameRes), pluginType.name(), pluginClass.getCanonicalName(), viewPermission.name());
    }

    private void initWarehouse() {
        jdbcTemplate.update(sqlWarehouseInsert, I18nSupport.message("main.warehouse.name"));
    }

    private void initAdministrator() {
        String userGroupName = I18nSupport.message("user.group.administrators.name");
        String description = I18nSupport.message("user.group.administrators.description");
        jdbcTemplate.update(sqlUserGroupInsert, userGroupName, description, true);
        jdbcTemplate.update(sqlAdminUserGroupPermissionsInsert, userGroupName);

        String userName = "admin";
        String pwd = "admin";
        //jdbcTemplate.update(sqlUserInsert, userName, MySqlAuthenticator.encodePassword(pwd), I18nSupport.message("user.administrator.name"), true);
        jdbcTemplate.update(sqlAdminUserGroupInsert, userName, userGroupName);
        //MySqlAuthenticator.createNewUser(userName, pwd);
    }

    private void initCurrencies() {
        initCurrency(I18nSupport.message("currency.rur.sign"), I18nSupport.message("currency.rur.name"));
        initCurrency(I18nSupport.message("currency.br.sign"), I18nSupport.message("currency.br.name"));
        initCurrency(I18nSupport.message("currency.usd.sign"), I18nSupport.message("currency.usd.name"));
    }

    private void initCurrency(String currencySign, String currencyName) {
        jdbcTemplate.update(sqlCurrencyInsert, currencySign, currencyName);
    }

    private void initWareNeeds() {
        jdbcTemplate.update(sqlWareNeedInsert, 1);
    }
}
