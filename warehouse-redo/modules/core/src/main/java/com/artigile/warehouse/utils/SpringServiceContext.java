/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

/**
 * @author IoaN, 22.11.2008
 */
package com.artigile.warehouse.utils;

import com.artigile.warehouse.bl.EmailConfigService;
import com.artigile.warehouse.bl.admin.UserGroupService;
import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.bl.chargeoff.ChargeOffService;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.complecting.ComplectingTaskService;
import com.artigile.warehouse.bl.complecting.UncomplectingTaskService;
import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.bl.dataimport.DataImportService;
import com.artigile.warehouse.bl.deliveryNote.DeliveryNoteService;
import com.artigile.warehouse.bl.detail.*;
import com.artigile.warehouse.bl.directory.CarService;
import com.artigile.warehouse.bl.directory.ManufacturerService;
import com.artigile.warehouse.bl.directory.MeasureUnitService;
import com.artigile.warehouse.bl.finance.AccountService;
import com.artigile.warehouse.bl.finance.CurrencyExchangeService;
import com.artigile.warehouse.bl.finance.CurrencyService;
import com.artigile.warehouse.bl.finance.PaymentService;
import com.artigile.warehouse.bl.inventorization.inventorization.InventorizationService;
import com.artigile.warehouse.bl.inventorization.task.InventorizationTaskService;
import com.artigile.warehouse.bl.license.LicenseService;
import com.artigile.warehouse.bl.lock.LockGroupService;
import com.artigile.warehouse.bl.lock.LockingManagerService;
import com.artigile.warehouse.bl.mail.EmailService;
import com.artigile.warehouse.bl.marketProposals.MarketProposalsService;
import com.artigile.warehouse.bl.movement.MovementService;
import com.artigile.warehouse.bl.needs.WareNeedService;
import com.artigile.warehouse.bl.orders.OrderService;
import com.artigile.warehouse.bl.postings.PostingService;
import com.artigile.warehouse.bl.postings.UnclassifiedCatalogItemService;
import com.artigile.warehouse.bl.priceimport.ContractorPriceImportService;
import com.artigile.warehouse.bl.priceimport.ContractorProductService;
import com.artigile.warehouse.bl.priceimport.SellerSettingsService;
import com.artigile.warehouse.bl.print.PrintTemplatePluginFactory;
import com.artigile.warehouse.bl.print.PrintTemplateService;
import com.artigile.warehouse.bl.properties.PropertiesService;
import com.artigile.warehouse.bl.purchase.PurchaseService;
import com.artigile.warehouse.bl.sticker.StickerPrintParamService;
import com.artigile.warehouse.bl.userprofile.FrameStateService;
import com.artigile.warehouse.bl.userprofile.ReportStateService;
import com.artigile.warehouse.bl.util.async.AsynchronousTaskExecutor;
import com.artigile.warehouse.bl.util.files.StoredFileService;
import com.artigile.warehouse.bl.warehouse.StoragePlaceService;
import com.artigile.warehouse.bl.warehouse.WarehouseService;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.gui.baselayout.PathMatchingResourcePatternResolverEx;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.Properties;

/**
 * Represents simple helper class for getting spring services.
 * It's implemented as singleton.
 *
 * @author ihar
 */
public class SpringServiceContext {

    /**
     * Class instance.
     */
    private static final SpringServiceContext instance = new SpringServiceContext();
    /**
     * Application context.
     */
    private final GenericApplicationContext ctx;

    /**
     * Gets class instance.
     *
     * @return class instance
     */
    public static SpringServiceContext getInstance() {
        return instance;
    }

    /**
     * Private constructor.
     */
    private SpringServiceContext() {
        this.ctx = new GenericApplicationContext() {

            @Override
            protected ResourcePatternResolver getResourcePatternResolver() {
                return new PathMatchingResourcePatternResolverEx(this);
            }
        };
        
        //set Spring's classloader to context classloader
        this.ctx.setClassLoader(Thread.currentThread().getContextClassLoader());

        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
        xmlReader.loadBeanDefinitions("classpath:config/applicationContext.xml");
        ctx.refresh();
    }

    public GlobalDataChangeNotifier getDataChangeNolifier(){
        return ctx.getBean("globalDataChangeNotifier", GlobalDataChangeNotifier.class);
    }

    public UserService getUserService() {
        return ctx.getBean("userService", UserService.class);
    }

    public PrintTemplateService getPrintTemplateService() {
        return ctx.getBean("printTemplateService", PrintTemplateService.class);
    }

    public UserGroupService getUserGroupService() {
        return ctx.getBean("userGroupService", UserGroupService.class);
    }

    public CurrencyExchangeService getExchangeService() {
        return ctx.getBean("exchangeService", CurrencyExchangeService.class);
    }

    public ContractorService getContractorService() {
        return ctx.getBean("contractorService", ContractorService.class);
    }

    public CurrencyExchangeService getCurencyExchangeService() {
        return ctx.getBean("exchangeService", CurrencyExchangeService.class);
    }

    public DetailTypeService getDetailTypesService() {
        return ctx.getBean("detailTypeService", DetailTypeService.class);
    }

    public DetailModelService getDetailModelsService() {
        return ctx.getBean("detailModelService", DetailModelService.class);
    }

    public DetailCatalogService getDetailCatalogService() {
        return ctx.getBean("detailCatalogService", DetailCatalogService.class);
    }

    public MeasureUnitService getMeasureUnitService() {
        return ctx.getBean("measureUnitService", MeasureUnitService.class);
    }

    public ManufacturerService getManufacturerService() {
        return ctx.getBean("manufacturerService", ManufacturerService.class);
    }

    public CarService getCarService() {
        return ctx.getBean("carService", CarService.class);
    }

    public WarehouseService getWarehouseService() {
        return ctx.getBean("warehouseService", WarehouseService.class);
    }

    public StoragePlaceService getStoragePlaceService() {
        return ctx.getBean("storagePlaceService", StoragePlaceService.class);
    }

    public DetailBatchService getDetailBatchesService() {
        return ctx.getBean("detailBatchService", DetailBatchService.class);
    }

    public DetailSerialNumberService getDetailSerialNumberService() {
        return ctx.getBean("detailSerialNumberService", DetailSerialNumberService.class);
    }

    public DetailBatchReservesService getDetailBatchReservesService() {
        return ctx.getBean("detailBatchReservesService", DetailBatchReservesService.class);
    }

    public DetailBatchHistoryService getDetailBatchHistoryService() {
        return ctx.getBean("detailBatchHistoryService", DetailBatchHistoryService.class);
    }

    public MarketProposalsService getMarketProposalsService() {
        return ctx.getBean("marketProposalsService", MarketProposalsService.class);
    }

    public CurrencyService getCurrencyService() {
        return ctx.getBean("currencyService", CurrencyService.class);
    }

    public WarehouseBatchService getWarehouseBatchService() {
        return ctx.getBean("warehouseBatchService", WarehouseBatchService.class);
    }

    public OrderService getOrdersService() {
        return ctx.getBean("orderService", OrderService.class);
    }

    public PostingService getPostingsService() {
        return ctx.getBean("postingService", PostingService.class);
    }

    public ReportStateService getReportStateService() {
        return ctx.getBean("reportStateService", ReportStateService.class);
    }

    public EmailService getEmailService() {
        return ctx.getBean("emailService", EmailService.class);
    }

    public WareNeedService getWareNeedsService() {
        return ctx.getBean("wareNeedService", WareNeedService.class);
    }

    public PurchaseService getPurchaseService() {
        return ctx.getBean("purchaseService", PurchaseService.class);
    }

    public ComplectingTaskService getComplectingTaskService(){
        return (ComplectingTaskService) ctx.getBean("complectingTaskService");
    }

    public UncomplectingTaskService getUncomplectingTaskService() {
        return ctx.getBean("uncomplectingTaskService", UncomplectingTaskService.class);
    }

    public InventorizationService getInventorizationService() {
        return ctx.getBean("inventorizationService", InventorizationService.class);
    }

    public FrameStateService getFrameStateService() {
        return ctx.getBean("frameStateService", FrameStateService.class);
    }

    public InventorizationTaskService getInventorizationTaskService() {
        return ctx.getBean("inventorizationTaskService", InventorizationTaskService.class);
    }

    public ChargeOffService getChargeOffService() {
        return ctx.getBean("chargeOffService", ChargeOffService.class);
    }

    public DeliveryNoteService getDeliveryNoteService() {
        return ctx.getBean("deliveryNoteService", DeliveryNoteService.class);
    }

    public MovementService getMovementService() {
        return ctx.getBean("movementService", MovementService.class);
    }

    public LockGroupService getLockGroupService() {
        return ctx.getBean("lockGroupService", LockGroupService.class);
    }

    public LockingManagerService getLockingManagerService() {
        return ctx.getBean("lockingManagerService", LockingManagerService.class);
    }

    public SessionFactory getSessionFactory() {
        return ctx.getBean("sessionFactory", SessionFactory.class);
    }

    public PropertiesService getPropertiesService() {
        return ctx.getBean("propertiesService", PropertiesService.class);
    }

    public PaymentService getPaymentService() {
        return ctx.getBean("paymentService", PaymentService.class);
    }

    public AccountService getAccountService() {
        return ctx.getBean("accountService", AccountService.class);
    }
    
    public ContractorProductService getContractorProductService() {
        return ctx.getBean("contractorProductService", ContractorProductService.class);
    }
    
    public ContractorPriceImportService getContractorPriceImportService() {
        return ctx.getBean("contractorPriceImportService", ContractorPriceImportService.class);
    }

    public Properties getApplicationProperties() {
        return ctx.getBean("applicationProperties", Properties.class);
    }

    public AsynchronousTaskExecutor getAsynchronousTaskExecutor() {
        return ctx.getBean("asynchronousTaskExecutor", AsynchronousTaskExecutor.class);
    }

    public StoredFileService getStoredFileService() {
        return ctx.getBean("storedFileService", StoredFileService.class);
    }

    public LicenseService getLicenseService() {
        return ctx.getBean("licenseService", LicenseService.class);
    }

    public SellerSettingsService getSellerSettingsService() {
        return ctx.getBean("sellerSettingsService", SellerSettingsService.class);
    }

    public DataImportService getDataImportService() {
        return ctx.getBean("dataImportService", DataImportService.class);
    }

    public DetailBatchImportService getDetailBatchImportService() {
        return ctx.getBean("detailBatchImportService", DetailBatchImportService.class);
    }

    public EmailConfigService getEmailConfigService() {
        return ctx.getBean("emailConfigService", EmailConfigService.class);
    }

    public UnclassifiedCatalogItemService getUnclassifiedCatalogItemService() {
        return ctx.getBean("unclassifiedCatalogItemService", UnclassifiedCatalogItemService.class);
    }

    public StickerPrintParamService getStickerPrintParamService() {
        return ctx.getBean("stickerPrintParamService", StickerPrintParamService.class);
    }

    public PrintTemplatePluginFactory getPrintTemplatePluginFactory() {
        return ctx.getBean(PrintTemplatePluginFactory.class);
    }
}
