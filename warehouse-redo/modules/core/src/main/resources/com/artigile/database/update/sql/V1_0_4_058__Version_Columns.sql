alter table Account add column version bigint(20) not null default 0;
alter table AccountOperation add column version bigint(20) not null default 0;
alter table car add column version bigint(20) not null default 0;
alter table ChargeOff add column version bigint(20) not null default 0;
alter table ChargeOffItem add column version bigint(20) not null default 0;
alter table ColumnState add column version bigint(20) not null default 0;
alter table ComplectingTask add column version bigint(20) not null default 0;
alter table Contact add column version bigint(20) not null default 0;
alter table Contractor add column version bigint(20) not null default 0;
alter table ContractorProduct add column version bigint(20) not null default 0;
alter table Currency add column version bigint(20) not null default 0;
alter table CurrencyWord add column version bigint(20) not null default 0;
alter table DataImport add column version bigint(20) not null default 0;
alter table DeliveryNote add column version bigint(20) not null default 0;
alter table DeliveryNoteItem add column version bigint(20) not null default 0;
alter table DetailBatch add column version bigint(20) not null default 0;
alter table DetailBatchOperation add column version bigint(20) not null default 0;
alter table DetailField add column version bigint(20) not null default 0;
alter table DetailGroup add column version bigint(20) not null default 0;
alter table DetailModel add column version bigint(20) not null default 0;
alter table DetailType add column version bigint(20) not null default 0;
alter table email_config add column version bigint(20) not null default 0;
alter table ExchangeRate add column version bigint(20) not null default 0;
alter table FrameState add column version bigint(20) not null default 0;
alter table Inventorization add column version bigint(20) not null default 0;
alter table InventorizationItem add column version bigint(20) not null default 0;
alter table InventorizationTask add column version bigint(20) not null default 0;
alter table License add column version bigint(20) not null default 0;
alter table LoadPlace add column version bigint(20) not null default 0;
alter table LockGroup add column version bigint(20) not null default 0;
alter table Manufacturer add column version bigint(20) not null default 0;
alter table MeasureUnit add column version bigint(20) not null default 0;
alter table MenuItem add column version bigint(20) not null default 0;
alter table Movement add column version bigint(20) not null default 0;
alter table MovementItem add column version bigint(20) not null default 0;
alter table OrderList add column version bigint(20) not null default 0;
alter table OrderItem add column version bigint(20) not null default 0;
alter table OrderProcessingInfo add column version bigint(20) not null default 0;
alter table OrderSubItem add column version bigint(20) not null default 0;
alter table Payment add column version bigint(20) not null default 0;
alter table Posting add column version bigint(20) not null default 0;
alter table PostingItem add column version bigint(20) not null default 0;
alter table PrintTemplate add column version bigint(20) not null default 0;
alter table PrintTemplateFieldMapping add column version bigint(20) not null default 0;
alter table PrintTemplateImage add column version bigint(20) not null default 0;
alter table PrintTemplateInstance add column version bigint(20) not null default 0;
alter table Property add column version bigint(20) not null default 0;
alter table Purchase add column version bigint(20) not null default 0;
alter table PurchaseItem add column version bigint(20) not null default 0;
alter table ReportState add column version bigint(20) not null default 0;
alter table SellerSettings add column version bigint(20) not null default 0;
alter table Shipping add column version bigint(20) not null default 0;
alter table StoragePlace add column version bigint(20) not null default 0;
alter table StoredFile add column version bigint(20) not null default 0;
alter table unclassified_catalog_item add column version bigint(20) not null default 0;
alter table UncomplectingTask add column version bigint(20) not null default 0;
alter table User add column version bigint(20) not null default 0;
alter table UserGroup add column version bigint(20) not null default 0;
alter table UserPermission add column version bigint(20) not null default 0;
alter table Warehouse add column version bigint(20) not null default 0;
alter table WarehouseBatch add column version bigint(20) not null default 0;
alter table WareNeed add column version bigint(20) not null default 0;
alter table WareNeedItem add column version bigint(20) not null default 0;
