-- create special table, which will be used for keeping source data about imported products
CREATE TABLE ContractorProduct_SourceData (
  id BIGINT (20) NOT NULL AUTO_INCREMENT,
  sourceData TEXT NOT NULL,
  PRIMARY KEY (id)
);

-- associate created table with ContractorProduct table
ALTER TABLE ContractorProduct ADD COLUMN sourceData_id BIGINT (20) DEFAULT NULL;
ALTER TABLE ContractorProduct ADD CONSTRAINT FOREIGN KEY (sourceData_id) REFERENCES ContractorProduct_SourceData (id) ON DELETE CASCADE;