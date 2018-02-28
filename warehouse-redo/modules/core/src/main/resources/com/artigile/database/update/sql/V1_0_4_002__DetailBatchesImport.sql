-- Schema changes for detail batches import.

-- 1. Create generic DataImport table (for mapping of common import data) and specialized tables.
create table DataImport(
  id bigint(20) NOT NULL,
  user_id bigint(20) NOT NULL,
  importDate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  importStatus varchar(255) NOT NULL,
  description varchar(255) DEFAULT NULL,
  adapter_id bigint(20) NOT NULL,
  adapterConf longtext NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (user_id) REFERENCES User (id),
  CONSTRAINT FOREIGN KEY (adapter_id) REFERENCES ImportDataAdapter (id)
);

-- Holds data specific for contractor price-list import.
create table ContractorPriceImport(
  dataImport_id bigint(20) NOT NULL,
  contractor_id bigint(20) NOT NULL,
  currency_id bigint(20) NOT NULL,
  measureUnit_id bigint(20) NOT NULL,
  itemCount int(11) NOT NULL,
  ignoredItemCount int(11) NOT NULL,
  PRIMARY KEY (dataImport_id),
  CONSTRAINT FOREIGN KEY (dataImport_id) REFERENCES DataImport (id) ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (contractor_id) REFERENCES Contractor (id),
  CONSTRAINT FOREIGN KEY (currency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (measureUnit_id) REFERENCES MeasureUnit (id)
);

-- Holds data specific for detail batches (self price-list) import.
create table DetailBatchImport(
  dataImport_id bigint(20) NOT NULL,
  detailType_id bigint(20) NOT NULL,
  currency_id bigint(20) NOT NULL,
  measureUnit_id bigint(20) NOT NULL,
  PRIMARY KEY (dataImport_id),
  CONSTRAINT FOREIGN KEY (dataImport_id) REFERENCES DataImport (id) ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (detailType_id) REFERENCES DetailType (id),
  CONSTRAINT FOREIGN KEY (currency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (measureUnit_id) REFERENCES MeasureUnit (id)
);

-- 2. Migrate existing data import data.
-- 2.1. Contractor product migration. 
insert into DataImport(id, user_id, importDate, importStatus, description, adapter_id, adapterConf)
  select id, user_id, importDate, importStatus, description, adapter_id, adapterConf from PriceImport;

insert into ContractorPriceImport(dataImport_id, contractor_id, currency_id, measureUnit_id, itemCount, ignoredItemCount)
  select id, contractor_id, currency_id, measureUnit_id, itemCount, 0 from PriceImport;

alter table ContractorProduct 
  add column priceImport_dataImport_id BIGINT(20) NULL;

update ContractorProduct set priceImport_dataImport_id = priceImport_id;

alter table ContractorProduct 
  modify column priceImport_dataImport_id BIGINT(20) NOT NULL;

alter table ContractorProduct
  add constraint foreign key fk_contractorproduct_dataimport (priceImport_dataImport_id) 
  references ContractorPriceImport(dataImport_id) on delete cascade;

alter table ContractorProduct drop foreign key contractorproduct_ibfk_3;

alter table ContractorProduct drop column priceImport_id;

-- 2.2. Stored file migration.
alter table StoredFile 
  add column dataImport_id BIGINT(20) DEFAULT NULL;

alter table StoredFile
  add constraint foreign key dataImport_id (dataImport_id) 
  references DataImport(id) on delete cascade;

update StoredFile set dataImport_id = priceImport_id;

alter table StoredFile drop foreign key storedfile_ibfk_1;

alter table StoredFile drop column priceImport_id;

-- 3. Enable primary key generation for DataImport.
alter table DataImport
  modify column id bigint(20) NOT NULL AUTO_INCREMENT;


-- 4. Remove unused tables.
drop table PriceImport;