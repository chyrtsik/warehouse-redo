ALTER TABLE ContractorProduct DROP FOREIGN KEY contractorproduct_ibfk_5;
ALTER TABLE ContractorProduct DROP COLUMN sourceData_id;

ALTER TABLE ContractorProduct_SourceData ADD COLUMN contractorProduct_id BIGINT (20) NOT NULL UNIQUE;