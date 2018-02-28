DROP TABLE ContractorProduct_StoredFile;

CREATE TABLE PriceImport_StoredFile(
  priceImport_id bigint(20) not null,
  storedFile_id bigint(20) not null,
  PRIMARY KEY (priceImport_id, storedFile_id),
  CONSTRAINT FOREIGN KEY (priceImport_id) REFERENCES PriceImport (id) ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (storedFile_id) REFERENCES StoredFile (id) ON DELETE CASCADE
);
