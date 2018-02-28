-- Change detail batch bar code field to string (leading zeros were not stored in old implementation).
alter table detailbatch
  modify column barcode VARCHAR(64) NULL DEFAULT NULL;