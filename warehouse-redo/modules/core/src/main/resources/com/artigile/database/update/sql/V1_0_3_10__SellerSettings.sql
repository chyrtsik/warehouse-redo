CREATE TABLE SellerSettings (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  contractorId bigint(20) NOT NULL,
  currencyId bigint(20) NOT NULL,
  measureUnitId bigint(20) NOT NULL,
  importAdapterUid varchar(36) NOT NULL,
  adapterConfig longtext NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY contractorId (contractorId)
);
