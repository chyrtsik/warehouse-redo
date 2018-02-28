-- DROP TABLE IF EXISTS ContractorProduct_StoredFile;
-- DROP TABLE IF EXISTS ContractorProduct;
-- DROP TABLE IF EXISTS StoredFile;
-- DROP TABLE IF EXISTS PriceImport;
-- DROP TABLE IF EXISTS ImportDataAdapter;


CREATE TABLE ImportDataAdapter (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  uidAdapter varchar(36) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (uidAdapter)
);

CREATE TABLE PriceImport (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20) NOT NULL,
  importDate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  contractor_id bigint(20) NOT NULL,
  currency_id bigint(20) NOT NULL,
  measureUnit_id bigint(20) NOT NULL,
  description varchar(255) DEFAULT NULL,
  itemCount int(11) NOT NULL,
  adapter_id bigint(20) NOT NULL,
  adapterConf longtext NOT NULL,
  PRIMARY KEY (id),
  CONSTRAint FOREIGN KEY (user_id) REFERENCES User (id),
  CONSTRAint FOREIGN KEY (contractor_id) REFERENCES Contractor (id),
  CONSTRAint FOREIGN KEY (currency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (measureUnit_id) REFERENCES MeasureUnit (id),
  CONSTRAint FOREIGN KEY (adapter_id) REFERENCES ImportDataAdapter (id)
);

CREATE TABLE ContractorProduct (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  description varchar(255) DEFAULT NULL,
  year int(11) DEFAULT NULL,
  quantity varchar(100) NOT NULL,
  wholesalePrice varchar(100) NOT NULL,
  retailPrice varchar(100) NOT NULL,
  currency_id bigint(20) NOT NULL,
  measureUnit_id bigint(20) NOT NULL,
  priceImport_id bigint(20) NOT NULL,
  postingDate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAint FOREIGN KEY (currency_id) REFERENCES Currency (id),
  CONSTRAINT FOREIGN KEY (measureUnit_id) REFERENCES MeasureUnit (id),
  CONSTRAint FOREIGN KEY (priceImport_id) REFERENCES PriceImport (id) ON DELETE CASCADE
);

CREATE TABLE StoredFile (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  storeDate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  content longblob,
  PRIMARY KEY (id)
);

CREATE TABLE ContractorProduct_StoredFile (
  contractorProduct_id bigint(20) NOT NULL,
  storedFile_id bigint(20) NOT NULL,
  PRIMARY KEY (contractorProduct_id, storedFile_id),
  CONSTRAint FOREIGN KEY (contractorProduct_id) REFERENCES ContractorProduct (id) ON DELETE CASCADE,
  CONSTRAint FOREIGN KEY (storedFile_id) REFERENCES StoredFile (id) ON DELETE CASCADE
);
