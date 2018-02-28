-- DROP TABLE IF EXISTS UncomplectingTask;
-- DROP TABLE IF EXISTS ComplectingTask;
-- DROP TABLE IF EXISTS MovementItem;
-- DROP TABLE IF EXISTS Movement;
-- DROP TABLE IF EXISTS InventorizationTask;
-- DROP TABLE IF EXISTS InventorizationItem;
-- DROP TABLE IF EXISTS Inventorization_StoragePlace;
-- DROP TABLE IF EXISTS Inventorization;
-- DROP TABLE IF EXISTS PostingItem;
-- DROP TABLE IF EXISTS Posting;
-- DROP TABLE IF EXISTS ChargeOffItem;
-- DROP TABLE IF EXISTS DeliveryNoteItem;
-- DROP TABLE IF EXISTS DeliveryNote;
-- DROP TABLE IF EXISTS Payment;
-- DROP TABLE IF EXISTS ChargeOff;
-- DROP TABLE IF EXISTS PurchaseItem;
-- DROP TABLE IF EXISTS Purchase;
-- DROP TABLE IF EXISTS WareneedItem;
-- DROP TABLE IF EXISTS Wareneed;
-- DROP TABLE IF EXISTS OrderSubItem;
-- DROP TABLE IF EXISTS OrderItem;
-- DROP TABLE IF EXISTS MenuItem;
-- DROP TABLE IF EXISTS WarehouseBatch;
-- DROP TABLE IF EXISTS DetailBatch;
-- DROP TABLE IF EXISTS OrderProcessingInfo;
-- DROP TABLE IF EXISTS OrderList;
-- DROP TABLE IF EXISTS PrintTemplateFieldMapping;
-- DROP TABLE IF EXISTS PrintTemplateImage;
-- DROP TABLE IF EXISTS PrintTemplate;
-- DROP TABLE IF EXISTS LockGroupOwner;
-- DROP TABLE IF EXISTS LockGroupItem;
-- DROP TABLE IF EXISTS LockGroup;
-- DROP TABLE IF EXISTS MeasureUnit;
-- DROP TABLE IF EXISTS Manufacturer;
-- DROP TABLE IF EXISTS ExchangeRate;
-- DROP TABLE IF EXISTS DetailModel;
-- DROP TABLE IF EXISTS AccountOperation;
-- DROP TABLE IF EXISTS Account;
-- DROP TABLE IF EXISTS Contact;
-- DROP TABLE IF EXISTS Contractor;
-- DROP TABLE IF EXISTS DetailGroup_DetailType;
-- DROP TABLE IF EXISTS DetailGroup;
-- DROP TABLE IF EXISTS DetailType_DetailField;
-- DROP TABLE IF EXISTS DetailType;
-- DROP TABLE IF EXISTS DetailField;
-- DROP TABLE IF EXISTS Property;
-- DROP TABLE IF EXISTS ColumnState;
-- DROP TABLE IF EXISTS ReportState;
-- DROP TABLE IF EXISTS FrameState;
-- DROP TABLE IF EXISTS Currency;
-- DROP TABLE IF EXISTS LoadPlace;
-- DROP TABLE IF EXISTS User_Warehouse;
-- DROP TABLE IF EXISTS StoragePlace;
-- DROP TABLE IF EXISTS Warehouse;
-- DROP TABLE IF EXISTS UserGroup_UserPermission;
-- DROP TABLE IF EXISTS UserPermission;
-- DROP TABLE IF EXISTS User_UserGroup;
-- DROP TABLE IF EXISTS UserGroup;
-- DROP TABLE IF EXISTS User;
-- DROP TABLE IF EXISTS ExceptionGenerator;
-- 
-- CREATE TABLE User (
--   id bigint(20) NOT NULL AUTO_INCREMENT,
--   email varchar(255) DEFAULT NULL,
--   firstName varchar(255) DEFAULT NULL,
--   lastName varchar(255) DEFAULT NULL,
--   login varchar(16) NOT NULL,
--   password varchar(255) DEFAULT NULL,
--   predefined boolean NOT NULL DEFAULT FALSE,
--   PRIMARY KEY (id),
--   UNIQUE KEY login (login)
-- );

CREATE TABLE UserGroup (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  description varchar(255) DEFAULT NULL,
  name varchar(255) NOT NULL,
  predefined boolean NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id),
  UNIQUE KEY name (name)
);

CREATE TABLE User_UserGroup (
  user_id bigint(20) NOT NULL,
  userGroup_id bigint(20) NOT NULL,
  PRIMARY KEY (user_id,userGroup_id),
  CONSTRAINT FOREIGN KEY (user_id) REFERENCES User (id)  ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (userGroup_id) REFERENCES UserGroup (id)  ON DELETE CASCADE
);

CREATE TABLE UserPermission (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  description varchar(255) DEFAULT NULL,
  name varchar(255) NOT NULL,
  rightType varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name (name),
  UNIQUE KEY rightType (rightType)
);

CREATE TABLE UserGroup_UserPermission (
  userGroup_id bigint(20) NOT NULL,
  userPermission_id bigint(20) NOT NULL,
  PRIMARY KEY (userGroup_id,userPermission_id),
  CONSTRAINT FOREIGN KEY (userPermission_id) REFERENCES UserPermission (id)  ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (userGroup_id) REFERENCES UserGroup (id)  ON DELETE CASCADE
);

CREATE TABLE Warehouse (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  notice varchar(255) DEFAULT NULL,
  stickerPrinter varchar(255) DEFAULT NULL,
  usualPrinter varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name (name)
);

CREATE TABLE StoragePlace (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  availableForPosting boolean NOT NULL DEFAULT FALSE,
  fillingDegree bigint(20) DEFAULT NULL,
  notice varchar(255) DEFAULT NULL,
  sign varchar(255) NOT NULL,
  parentStoragePlace_id bigint(20) DEFAULT NULL,
  warehouse_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (parentStoragePlace_id) REFERENCES StoragePlace (id),
  CONSTRAINT FOREIGN KEY (warehouse_id) REFERENCES Warehouse (id)
);

CREATE TABLE User_Warehouse (
  user_id bigint(20) NOT NULL,
  warehouse_id bigint(20) NOT NULL,
  PRIMARY KEY (user_id,warehouse_id),
  CONSTRAINT FOREIGN KEY (user_id) REFERENCES User (id)  ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (warehouse_id) REFERENCES Warehouse (id)  ON DELETE CASCADE
);

CREATE TABLE LoadPlace (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  description varchar(255) DEFAULT NULL,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name (name)
);

CREATE TABLE Currency (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  defaultCurrency boolean NOT NULL DEFAULT FALSE,
  name varchar(255) NOT NULL,
  sign varchar(100) NOT NULL,
  uidCurrency varchar(36) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name (name),
  UNIQUE KEY sign (sign),
  UNIQUE KEY uidCurrency (uidCurrency)
);

CREATE TABLE FrameState (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  frameId varchar(255) NOT NULL,
  height double DEFAULT NULL,
  x double DEFAULT NULL,
  maximized boolean NOT NULL DEFAULT FALSE,
  y double DEFAULT NULL,
  width double DEFAULT NULL,
  user_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY frameId (frameId),
  CONSTRAINT FOREIGN KEY (user_id) REFERENCES User (id)
);

CREATE TABLE ReportState (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  filterVisible boolean NOT NULL DEFAULT FALSE,
  reportMajor varchar(255) NOT NULL,
  reportMinor varchar(255) NOT NULL,
  user_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (user_id) REFERENCES User (id)
);

CREATE TABLE ColumnState (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  filterText varchar(255) DEFAULT NULL,
  identifier varchar(255) NOT NULL,
  visible boolean NOT NULL DEFAULT FALSE,
  width int(11) DEFAULT NULL,
  reportState_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (reportState_id) REFERENCES ReportState (id)
);

CREATE TABLE Property (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  propKey varchar(255) NOT NULL,
  propValue varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY propKey (propKey)
);

CREATE TABLE DetailField (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  displayOrder bigint(20) DEFAULT NULL,
  enumValues text,
  fieldIndex int(11) DEFAULT NULL,
  mandatory boolean NOT NULL DEFAULT FALSE,
  name varchar(255) NOT NULL,
  predefined boolean NOT NULL DEFAULT FALSE,
  predefinedType varchar(255) DEFAULT NULL,
  sortNum bigint(20) DEFAULT NULL,
  template varchar(255) DEFAULT NULL,
  type varchar(255) NOT NULL,
  uniqueValue boolean NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id)
);

CREATE TABLE DetailType (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  description varchar(255) DEFAULT NULL,
  name varchar(255) NOT NULL,
  sortNum int(11) DEFAULT NULL,
  detailBatchMiscField_id bigint(20) NOT NULL,
  detailBatchNameField_id bigint(20) NOT NULL,
  detailBatchTypeField_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name (name),
  UNIQUE KEY detailBatchTypeField_id (detailBatchTypeField_id),
  UNIQUE KEY detailBatchNameField_id (detailBatchNameField_id),
  UNIQUE KEY detailBatchMiscField_id (detailBatchMiscField_id),
  CONSTRAINT FOREIGN KEY (detailBatchTypeField_id) REFERENCES DetailField (id),
  CONSTRAINT FOREIGN KEY (detailBatchNameField_id) REFERENCES DetailField (id),
  CONSTRAINT FOREIGN KEY (detailBatchMiscField_id) REFERENCES DetailField (id)
);

CREATE TABLE DetailType_DetailField (
  detailType_id bigint(20) NOT NULL,
  detailField_id bigint(20) NOT NULL,
  UNIQUE KEY detailField_id (detailField_id),
  CONSTRAINT FOREIGN KEY (detailField_id) REFERENCES DetailField (id),
  CONSTRAINT FOREIGN KEY (detailType_id) REFERENCES DetailType (id)
);

CREATE TABLE DetailGroup (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  description varchar(255) DEFAULT NULL,
  name varchar(255) NOT NULL,
  sortNum int(11) DEFAULT NULL,
  parentGroup_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (parentGroup_id) REFERENCES DetailGroup (id)
);

CREATE TABLE DetailGroup_DetailType (
  detailGroup_id bigint(20) NOT NULL,
  detailType_id bigint(20) NOT NULL,
  PRIMARY KEY (detailGroup_id,detailType_id),
  CONSTRAINT FOREIGN KEY (detailGroup_id) REFERENCES DetailGroup (id),
  CONSTRAINT FOREIGN KEY (detailType_id) REFERENCES DetailType (id)
);

CREATE TABLE Contractor (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  address varchar(255) DEFAULT NULL,
  bankAccount varchar(255) DEFAULT NULL,
  bankAddress varchar(255) DEFAULT NULL,
  bankCode varchar(255) DEFAULT NULL,
  bankFullData varchar(255) DEFAULT NULL,
  bankShortData varchar(255) DEFAULT NULL,
  country varchar(255) DEFAULT NULL,
  name varchar(255) NOT NULL,
  notice varchar(255) DEFAULT NULL,
  okpo varchar(255) DEFAULT NULL,
  rating int(11) DEFAULT NULL,
  uidContractor varchar(36) DEFAULT NULL,
  unp varchar(255) DEFAULT NULL,
  webSiteURL varchar(255) DEFAULT NULL,
  defaultCurrency_id bigint(20) DEFAULT NULL,
  defaultShippingAddress_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name (name),
  UNIQUE KEY uidContractor (uidContractor),
  CONSTRAINT FOREIGN KEY (defaultShippingAddress_id) REFERENCES LoadPlace (id),
  CONSTRAINT FOREIGN KEY (defaultCurrency_id) REFERENCES Currency (id)
);

CREATE TABLE Contact (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  email varchar(255) DEFAULT NULL,
  fullName varchar(255) DEFAULT NULL,
  icqId varchar(255) DEFAULT NULL,
  notice varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  skypeId varchar(255) DEFAULT NULL,
  contractor_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (contractor_id) REFERENCES Contractor (id)
);

CREATE TABLE Account (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  currentBalance decimal(14,4) NOT NULL,
  contractor_id bigint(20) NOT NULL,
  currency_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (contractor_id) REFERENCES Contractor (id),
  CONSTRAINT FOREIGN KEY (currency_id) REFERENCES Currency (id)
);

CREATE TABLE AccountOperation (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  changeOfBalance decimal(14,4) NOT NULL,
  initialBalance decimal(14,4) NOT NULL,
  newBalance decimal(14,4) NOT NULL,
  notice varchar(255) DEFAULT NULL,
  operationDateTime datetime NOT NULL,
  account_id bigint(20) NOT NULL,
  performedUser_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (account_id) REFERENCES Account (id),
  CONSTRAINT FOREIGN KEY (performedUser_id) REFERENCES User (id)
);

CREATE TABLE DetailModel (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  field1 varchar(255) DEFAULT NULL,
  field10 varchar(255) DEFAULT NULL,
  field11 varchar(255) DEFAULT NULL,
  field12 varchar(255) DEFAULT NULL,
  field13 varchar(255) DEFAULT NULL,
  field14 varchar(255) DEFAULT NULL,
  field15 varchar(255) DEFAULT NULL,
  field16 varchar(255) DEFAULT NULL,
  field17 varchar(255) DEFAULT NULL,
  field18 varchar(255) DEFAULT NULL,
  field19 varchar(255) DEFAULT NULL,
  field2 varchar(255) DEFAULT NULL,
  field20 varchar(255) DEFAULT NULL,
  field21 varchar(255) DEFAULT NULL,
  field22 varchar(255) DEFAULT NULL,
  field23 varchar(255) DEFAULT NULL,
  field24 varchar(255) DEFAULT NULL,
  field25 varchar(255) DEFAULT NULL,
  field26 varchar(255) DEFAULT NULL,
  field27 varchar(255) DEFAULT NULL,
  field28 varchar(255) DEFAULT NULL,
  field29 varchar(255) DEFAULT NULL,
  field3 varchar(255) DEFAULT NULL,
  field30 varchar(255) DEFAULT NULL,
  field4 varchar(255) DEFAULT NULL,
  field5 varchar(255) DEFAULT NULL,
  field6 varchar(255) DEFAULT NULL,
  field7 varchar(255) DEFAULT NULL,
  field8 varchar(255) DEFAULT NULL,
  field9 varchar(255) DEFAULT NULL,
  name varchar(255) NOT NULL,
  sortNum int(11) DEFAULT NULL,
  detailType_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (detailType_id) REFERENCES DetailType (id)
);

CREATE TABLE ExchangeRate (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  rate decimal(14,4) NOT NULL,
  fromCurrency_id bigint(20) NOT NULL,
  toCurrency_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY fromCurrency_id (fromCurrency_id,toCurrency_id),
  CONSTRAINT FOREIGN KEY (fromCurrency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (toCurrency_id) REFERENCES Currency (id)
);

CREATE TABLE Manufacturer (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  notice varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name (name)
);

CREATE TABLE MeasureUnit (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  defaultMeasureUnit boolean NOT NULL DEFAULT FALSE,
  name varchar(255) NOT NULL,
  notice varchar(255) DEFAULT NULL,
  sign varchar(255) NOT NULL,
  uidMeasureUnit varchar(36) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name (name),
  UNIQUE KEY sign (sign),
  UNIQUE KEY uidMeasureUnit (uidMeasureUnit)
);

CREATE TABLE LockGroup (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createDate datetime NOT NULL,
  lockType varchar(255) NOT NULL,
  createUser_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (createUser_id) REFERENCES User (id)
);

CREATE TABLE LockGroupItem(
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  entity VARCHAR(100) NOT NULL,
  dropTable CHAR(1) NOT NULL DEFAULT 'n',
  PRIMARY KEY (id)
);

CREATE TABLE LockGroupOwner(
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  entity VARCHAR(100) NOT NULL,
  dropTable CHAR(1) NOT NULL DEFAULT 'n',
  PRIMARY KEY (id)
);

CREATE TABLE PrintTemplate (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  description varchar(255) DEFAULT NULL,
  name varchar(255) NOT NULL,
  templateData longblob,
  templateType varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name (name),
  UNIQUE KEY templateType (templateType)
);

CREATE TABLE PrintTemplateImage (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  imageData longblob,
  imageFileName varchar(255) DEFAULT NULL,
  name varchar(255) NOT NULL,
  notice varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name (name)
);

CREATE TABLE PrintTemplateFieldMapping (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  objectField varchar(255) DEFAULT NULL,
  reportField varchar(255) NOT NULL,
  image_id bigint(20) DEFAULT NULL,
  printTemplate_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (printTemplate_id) REFERENCES PrintTemplate (id),
  CONSTRAINT FOREIGN KEY (image_id) REFERENCES PrintTemplateImage (id)
);

CREATE TABLE OrderList (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createDate date NOT NULL,
  deleted boolean NOT NULL DEFAULT FALSE,
  loadDate date DEFAULT NULL,
  notice varchar(255) DEFAULT NULL,
  number bigint(20) NOT NULL,
  reservingType varchar(255) NOT NULL,
  state varchar(255) NOT NULL,
  contractor_id bigint(20) NOT NULL,
  createdUser_id bigint(20) NOT NULL,
  currency_id bigint(20) NOT NULL,
  loadPlace_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (contractor_id) REFERENCES Contractor (id),
  CONSTRAINT FOREIGN KEY (createdUser_id) REFERENCES User (id),
  CONSTRAINT FOREIGN KEY (currency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (loadPlace_id) REFERENCES LoadPlace (id)
);

CREATE TABLE OrderProcessingInfo (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  complectedItemsCount bigint(20) NOT NULL,
  itemsCount bigint(20) NOT NULL,
  order_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY order_id (order_id),
  CONSTRAINT FOREIGN KEY (order_id) REFERENCES OrderList (id)
);

CREATE TABLE DetailBatch (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  acceptance varchar(255) DEFAULT NULL,
  barCode bigint(20) DEFAULT NULL,
  buyPrice decimal(14,4) DEFAULT NULL,
  misc varchar(255) DEFAULT NULL,
  name varchar(255) NOT NULL,
  nomenclatureArticle varchar(255) DEFAULT NULL,
  notice varchar(255) DEFAULT NULL,
  sellPrice decimal(14,4) DEFAULT NULL,
  sortNum int(11) DEFAULT NULL,
  type varchar(255) DEFAULT NULL,
  uidGoods varchar(36) DEFAULT NULL,
  year int(11) DEFAULT NULL,
  countMeas_id bigint(20) NOT NULL,
  currency_id bigint(20) DEFAULT NULL,
  manufacturer_id bigint(20) DEFAULT NULL,
  model_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY barCode (barCode),
  UNIQUE KEY nomenclatureArticle (nomenclatureArticle),
  UNIQUE KEY uidGoods (uidGoods),
  CONSTRAINT FOREIGN KEY (model_id) REFERENCES DetailModel (id),
  CONSTRAINT FOREIGN KEY (countMeas_id) REFERENCES MeasureUnit (id),
  CONSTRAINT FOREIGN KEY (currency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (manufacturer_id) REFERENCES Manufacturer (id)
);

CREATE TABLE WarehouseBatch (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount bigint(20) NOT NULL,
  needRecalculate boolean NOT NULL DEFAULT FALSE,
  notice varchar(255) DEFAULT NULL,
  reservedCount bigint(20) NOT NULL,
  detailBatch_id bigint(20) NOT NULL,
  storagePlace_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY detailBatch_id (detailBatch_id,notice),
  CONSTRAINT FOREIGN KEY (detailBatch_id) REFERENCES DetailBatch (id),
  CONSTRAINT FOREIGN KEY (storagePlace_id) REFERENCES StoragePlace (id)
);

CREATE TABLE MenuItem (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  pluginClassName varchar(255) NOT NULL,
  pluginType varchar(255) NOT NULL,
  viewPermission_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name (name),
  CONSTRAINT FOREIGN KEY (viewPermission_id) REFERENCES UserPermission (id)
);

CREATE TABLE OrderItem (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount bigint(20) NOT NULL,
  deleted boolean NOT NULL DEFAULT FALSE,
  notice varchar(255) DEFAULT NULL,
  number bigint(20) NOT NULL,
  price decimal(14,4) NOT NULL,
  processingResult varchar(255) DEFAULT NULL,
  reserved boolean NOT NULL DEFAULT FALSE,
  state varchar(255) NOT NULL,
  text varchar(255) DEFAULT NULL,
  detailBatch_id bigint(20) DEFAULT NULL,
  order_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (order_id) REFERENCES OrderList (id),
  CONSTRAINT FOREIGN KEY (detailBatch_id) REFERENCES DetailBatch (id)
);

CREATE TABLE OrderSubItem (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount bigint(20) NOT NULL,
  deleted boolean NOT NULL DEFAULT FALSE,
  notice varchar(255) DEFAULT NULL,
  number bigint(20) NOT NULL,
  processingResult varchar(255) DEFAULT NULL,
  state varchar(255) NOT NULL,
  warehouseNotice varchar(255) DEFAULT NULL,
  orderItem_id bigint(20) NOT NULL,
  storagePlace_id bigint(20) NOT NULL,
  warehouseBatch_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (warehouseBatch_id) REFERENCES WarehouseBatch (id),
  CONSTRAINT FOREIGN KEY (orderItem_id) REFERENCES OrderItem (id),
  CONSTRAINT FOREIGN KEY (storagePlace_id) REFERENCES StoragePlace (id)
);

CREATE TABLE Wareneed (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
);

CREATE TABLE WareneedItem (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount bigint(20) DEFAULT NULL,
  createDateTime datetime NOT NULL,
  maxPrice decimal(14,4) DEFAULT NULL,
  minYear bigint(20) DEFAULT NULL,
  misc varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  notice varchar(255) DEFAULT NULL,
  number bigint(20) NOT NULL,
  state varchar(255) NOT NULL,
  subNumber bigint(20) DEFAULT NULL,
  createdUser_id bigint(20) NOT NULL,
  currency_id bigint(20) DEFAULT NULL,
  customer_id bigint(20) DEFAULT NULL,
  detailBatch_id bigint(20) DEFAULT NULL,
  wareNeed_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (customer_id) REFERENCES Contractor (id),
  CONSTRAINT FOREIGN KEY (createdUser_id) REFERENCES User (id),
  CONSTRAINT FOREIGN KEY (currency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (detailBatch_id) REFERENCES DetailBatch (id),
  CONSTRAINT FOREIGN KEY (wareNeed_id) REFERENCES Wareneed (id)
);

CREATE TABLE Purchase (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createDate date NOT NULL,
  notice varchar(255) DEFAULT NULL,
  number bigint(20) NOT NULL,
  state varchar(255) NOT NULL,
  contractor_id bigint(20) NOT NULL,
  createdUser_id bigint(20) NOT NULL,
  currency_id bigint(20) NOT NULL,
  loadPlace_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY number (number),
  CONSTRAINT FOREIGN KEY (contractor_id) REFERENCES Contractor (id),
  CONSTRAINT FOREIGN KEY (createdUser_id) REFERENCES User (id),
  CONSTRAINT FOREIGN KEY (currency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (loadPlace_id) REFERENCES LoadPlace (id)
);

CREATE TABLE PurchaseItem (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount bigint(20) DEFAULT NULL,
  misc varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  notice varchar(255) DEFAULT NULL,
  number bigint(20) NOT NULL,
  price decimal(14,4) DEFAULT NULL,
  countMeas_id bigint(20) NOT NULL,
  purchase_id bigint(20) NOT NULL,
  wareNeedItem_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (purchase_id) REFERENCES Purchase (id),
  CONSTRAINT FOREIGN KEY (countMeas_id) REFERENCES MeasureUnit (id),
  CONSTRAINT FOREIGN KEY (wareNeedItem_id) REFERENCES WareneedItem (id)
);

CREATE TABLE ChargeOff (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  notice varchar(255) DEFAULT NULL,
  number bigint(20) NOT NULL,
  performDate datetime NOT NULL,
  reason varchar(255) DEFAULT NULL,
  performer_id bigint(20) NOT NULL,
  warehouse_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY number (number),
  CONSTRAINT FOREIGN KEY (warehouse_id) REFERENCES Warehouse (id),
  CONSTRAINT FOREIGN KEY (performer_id) REFERENCES User (id)
);

CREATE TABLE Payment (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  dateTime datetime NOT NULL,
  notice varchar(255) DEFAULT NULL,
  paymentSum decimal(14,4) NOT NULL,
  acceptedUser_id bigint(20) NOT NULL,
  accountOperation_id bigint(20) DEFAULT NULL,
  contractor_id bigint(20) NOT NULL,
  paymentCurrency_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (contractor_id) REFERENCES Contractor (id),
  CONSTRAINT FOREIGN KEY (paymentCurrency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (acceptedUser_id) REFERENCES User (id),
  CONSTRAINT FOREIGN KEY (accountOperation_id) REFERENCES AccountOperation (id)
);

CREATE TABLE DeliveryNote (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  notice varchar(255) DEFAULT NULL,
  number bigint(20) NOT NULL,
  state varchar(255) NOT NULL,
  chargeOff_id bigint(20) NOT NULL,
  contractor_id bigint(20) DEFAULT NULL,
  currency_id bigint(20) NOT NULL,
  destinationWarehouse_id bigint(20) DEFAULT NULL,
  payment_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY number (number),
  CONSTRAINT FOREIGN KEY (destinationWarehouse_id) REFERENCES Warehouse (id),
  CONSTRAINT FOREIGN KEY (contractor_id) REFERENCES Contractor (id),
  CONSTRAINT FOREIGN KEY (chargeOff_id) REFERENCES ChargeOff (id),
  CONSTRAINT FOREIGN KEY (currency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (payment_id) REFERENCES Payment (id)
);

CREATE TABLE DeliveryNoteItem (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount bigint(20) NOT NULL,
  number bigint(20) NOT NULL,
  price decimal(14,4) DEFAULT NULL,
  warehouseBatchNotice varchar(255) DEFAULT NULL,
  countMeas_id bigint(20) NOT NULL,
  deliveryNote_id bigint(20) NOT NULL,
  detailBatch_id bigint(20) NOT NULL,
  storagePlace_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (deliveryNote_id) REFERENCES DeliveryNote (id),
  CONSTRAINT FOREIGN KEY (storagePlace_id) REFERENCES StoragePlace (id),
  CONSTRAINT FOREIGN KEY (countMeas_id) REFERENCES MeasureUnit (id),
  CONSTRAINT FOREIGN KEY (detailBatch_id) REFERENCES DetailBatch (id)
);

CREATE TABLE ChargeOffItem (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount bigint(20) NOT NULL,
  notice varchar(255) DEFAULT NULL,
  number bigint(20) NOT NULL,
  warehouseNotice varchar(255) DEFAULT NULL,
  chargeOff_id bigint(20) NOT NULL,
  countMeas_id bigint(20) NOT NULL,
  deliveryNoteItem_id bigint(20) DEFAULT NULL,
  detailBatch_id bigint(20) NOT NULL,
  storagePlace_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (chargeOff_id) REFERENCES ChargeOff (id),
  CONSTRAINT FOREIGN KEY (storagePlace_id) REFERENCES StoragePlace (id),
  CONSTRAINT FOREIGN KEY (countMeas_id) REFERENCES MeasureUnit (id),
  CONSTRAINT FOREIGN KEY (deliveryNoteItem_id) REFERENCES DeliveryNoteItem (id),
  CONSTRAINT FOREIGN KEY (detailBatch_id) REFERENCES DetailBatch (id)
);

CREATE TABLE Posting (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createDate date NOT NULL,
  notice varchar(255) DEFAULT NULL,
  number bigint(20) NOT NULL,
  state varchar(255) NOT NULL,
  contractor_id bigint(20) DEFAULT NULL,
  createdUser_id bigint(20) NOT NULL,
  currency_id bigint(20) DEFAULT NULL,
  defaultCurrency_id bigint(20) DEFAULT NULL,
  defaultStoragePlace_id bigint(20) DEFAULT NULL,
  defaultWarehouse_id bigint(20) DEFAULT NULL,
  deliveryNote_id bigint(20) DEFAULT NULL,
  purchase_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY number (number),
  CONSTRAINT FOREIGN KEY (contractor_id) REFERENCES Contractor (id),
  CONSTRAINT FOREIGN KEY (defaultStoragePlace_id) REFERENCES StoragePlace (id),
  CONSTRAINT FOREIGN KEY (defaultWarehouse_id) REFERENCES Warehouse (id),
  CONSTRAINT FOREIGN KEY (deliveryNote_id) REFERENCES DeliveryNote (id),
  CONSTRAINT FOREIGN KEY (defaultCurrency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (createdUser_id) REFERENCES User (id),
  CONSTRAINT FOREIGN KEY (currency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (purchase_id) REFERENCES Purchase (id)
);

CREATE TABLE PostingItem (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount bigint(20) DEFAULT NULL,
  notice varchar(255) DEFAULT NULL,
  number bigint(20) NOT NULL,
  originalPrice decimal(14,4) DEFAULT NULL,
  price decimal(14,4) DEFAULT NULL,
  salePrice decimal(14,4) DEFAULT NULL,
  countMeas_id bigint(20) NOT NULL,
  detailBatch_id bigint(20) NOT NULL,
  originalCurrency_id bigint(20) DEFAULT NULL,
  posting_id bigint(20) NOT NULL,
  storagePlace_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (detailBatch_id) REFERENCES DetailBatch (id),
  CONSTRAINT FOREIGN KEY (storagePlace_id) REFERENCES StoragePlace (id),
  CONSTRAINT FOREIGN KEY (countMeas_id) REFERENCES MeasureUnit (id),
  CONSTRAINT FOREIGN KEY (originalCurrency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (posting_id) REFERENCES Posting (id)
);

CREATE TABLE Inventorization (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  closeDate datetime DEFAULT NULL,
  createDate datetime NOT NULL,
  notice varchar(255) DEFAULT NULL,
  number bigint(20) NOT NULL,
  result varchar(255) DEFAULT NULL,
  state varchar(255) NOT NULL,
  type varchar(255) NOT NULL,
  chargeOff_id bigint(20) DEFAULT NULL,
  closeUser_id bigint(20) DEFAULT NULL,
  createUser_id bigint(20) NOT NULL,
  posting_id bigint(20) DEFAULT NULL,
  warehouse_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (createUser_id) REFERENCES User (id),
  CONSTRAINT FOREIGN KEY (chargeOff_id) REFERENCES ChargeOff (id),
  CONSTRAINT FOREIGN KEY (warehouse_id) REFERENCES Warehouse (id),
  CONSTRAINT FOREIGN KEY (closeUser_id) REFERENCES User (id),
  CONSTRAINT FOREIGN KEY (posting_id) REFERENCES Posting (id)
);

CREATE TABLE Inventorization_StoragePlace (
  inventorization_id bigint(20) NOT NULL,
  storagePlace_id bigint(20) NOT NULL,
  PRIMARY KEY (inventorization_id,storagePlace_id),
  CONSTRAINT FOREIGN KEY (inventorization_id) REFERENCES Inventorization (id),
  CONSTRAINT FOREIGN KEY (storagePlace_id) REFERENCES StoragePlace (id)
);

CREATE TABLE InventorizationItem (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  neededCount bigint(20) NOT NULL,
  number bigint(20) NOT NULL,
  processingResult varchar(255) DEFAULT NULL,
  state varchar(255) NOT NULL,
  chargeOffItem_id bigint(20) DEFAULT NULL,
  countMeas_id bigint(20) NOT NULL,
  detailBatch_id bigint(20) NOT NULL,
  inventorization_id bigint(20) NOT NULL,
  postingItem_id bigint(20) DEFAULT NULL,
  storagePlace_id bigint(20) NOT NULL,
  warehouseBatch_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (detailBatch_id) REFERENCES DetailBatch (id),
  CONSTRAINT FOREIGN KEY (storagePlace_id) REFERENCES StoragePlace (id),
  CONSTRAINT FOREIGN KEY (countMeas_id) REFERENCES MeasureUnit (id),
  CONSTRAINT FOREIGN KEY (postingItem_id) REFERENCES PostingItem (id),
  CONSTRAINT FOREIGN KEY (inventorization_id) REFERENCES Inventorization (id),
  CONSTRAINT FOREIGN KEY (chargeOffItem_id) REFERENCES ChargeOffItem (id),
  CONSTRAINT FOREIGN KEY (warehouseBatch_id) REFERENCES WarehouseBatch (id)
);

CREATE TABLE InventorizationTask (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  foundCount bigint(20) DEFAULT NULL,
  inventorizationType varchar(255) NOT NULL,
  number bigint(20) NOT NULL,
  printed boolean NOT NULL DEFAULT FALSE,
  processingResult varchar(255) DEFAULT NULL,
  state varchar(255) NOT NULL,
  workBegin datetime DEFAULT NULL,
  workCreate datetime DEFAULT NULL,
  workEnd datetime DEFAULT NULL,
  countMeas_id bigint(20) NOT NULL,
  detailBatch_id bigint(20) NOT NULL,
  inventorizationItem_id bigint(20) NOT NULL,
  storagePlace_id bigint(20) NOT NULL,
  worker_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY inventorizationItem_id (inventorizationItem_id),
  CONSTRAINT FOREIGN KEY (inventorizationItem_id) REFERENCES InventorizationItem (id),
  CONSTRAINT FOREIGN KEY (storagePlace_id) REFERENCES StoragePlace (id),
  CONSTRAINT FOREIGN KEY (worker_id) REFERENCES User (id),
  CONSTRAINT FOREIGN KEY (countMeas_id) REFERENCES MeasureUnit (id),
  CONSTRAINT FOREIGN KEY (detailBatch_id) REFERENCES DetailBatch (id)
);

CREATE TABLE Movement (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  beginDate date DEFAULT NULL,
  createDate date NOT NULL,
  endDate date DEFAULT NULL,
  notice varchar(255) DEFAULT NULL,
  number bigint(20) NOT NULL,
  result varchar(255) DEFAULT NULL,
  state varchar(255) NOT NULL,
  createUser_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY number (number),
  CONSTRAINT FOREIGN KEY (createUser_id) REFERENCES User (id)
);

CREATE TABLE MovementItem (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount bigint(20) DEFAULT NULL,
  number bigint(20) NOT NULL,
  processingResult varchar(255) DEFAULT NULL,
  state varchar(255) NOT NULL,
  warehouseNotice varchar(255) DEFAULT NULL,
  countMeas_id bigint(20) NOT NULL,
  deliveryNoteItem_id bigint(20) DEFAULT NULL,
  detailBatch_id bigint(20) DEFAULT NULL,
  fromStoragePlace_id bigint(20) NOT NULL,
  fromWarehouse_id bigint(20) NOT NULL,
  movement_id bigint(20) NOT NULL,
  toStoragePlace_id bigint(20) DEFAULT NULL,
  toWarehouse_id bigint(20) DEFAULT NULL,
  warehouseBatch_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (movement_id) REFERENCES Movement (id),
  CONSTRAINT FOREIGN KEY (toWarehouse_id) REFERENCES Warehouse (id),
  CONSTRAINT FOREIGN KEY (countMeas_id) REFERENCES MeasureUnit (id),
  CONSTRAINT FOREIGN KEY (deliveryNoteItem_id) REFERENCES DeliveryNoteItem (id),
  CONSTRAINT FOREIGN KEY (toStoragePlace_id) REFERENCES StoragePlace (id),
  CONSTRAINT FOREIGN KEY (detailBatch_id) REFERENCES DetailBatch (id),
  CONSTRAINT FOREIGN KEY (fromStoragePlace_id) REFERENCES StoragePlace (id),
  CONSTRAINT FOREIGN KEY (fromWarehouse_id) REFERENCES Warehouse (id),
  CONSTRAINT FOREIGN KEY (warehouseBatch_id) REFERENCES WarehouseBatch (id)
);

CREATE TABLE ComplectingTask (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  foundCount bigint(20) DEFAULT NULL,
  neededCount bigint(20) NOT NULL,
  printed boolean NOT NULL DEFAULT FALSE,
  state varchar(255) NOT NULL,
  stickerPrinted boolean NOT NULL DEFAULT FALSE,
  workBegin datetime DEFAULT NULL,
  workEnd datetime DEFAULT NULL,
  chargeOffItem_id bigint(20) DEFAULT NULL,
  movementItem_id bigint(20) DEFAULT NULL,
  orderSubItem_id bigint(20) DEFAULT NULL,
  worker_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (orderSubItem_id) REFERENCES OrderSubItem (id),
  CONSTRAINT FOREIGN KEY (worker_id) REFERENCES User (id),
  CONSTRAINT FOREIGN KEY (movementItem_id) REFERENCES MovementItem (id),
  CONSTRAINT FOREIGN KEY (chargeOffItem_id) REFERENCES ChargeOffItem (id)
);

CREATE TABLE UncomplectingTask (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  countToProcess bigint(20) NOT NULL,
  createDate datetime DEFAULT NULL,
  doneDate datetime DEFAULT NULL,
  state varchar(255) NOT NULL,
  type varchar(255) NOT NULL,
  orderSubItem_id bigint(20) DEFAULT NULL,
  worker_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (orderSubItem_id) REFERENCES OrderSubItem (id),
  CONSTRAINT FOREIGN KEY (worker_id) REFERENCES User (id)
);

CREATE TABLE ExceptionGenerator(
  dummy VARCHAR(255) NOT NULL,
  PRIMARY KEY (dummy)
);

INSERT INTO ExceptionGenerator VALUES ('ERR_LOCK_001'), ('ERR_LOCK_002'), ('ERR_LOCK_003');

COMMIT;
