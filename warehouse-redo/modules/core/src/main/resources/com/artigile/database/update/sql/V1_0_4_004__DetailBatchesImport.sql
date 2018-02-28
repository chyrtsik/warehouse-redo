-- New columns in detail batch import.
alter table DetailBatchImport
  add column insertedItemsCount int(11) DEFAULT NULL,
  add column updatedItemsCount int(11) DEFAULT NULL,
  add column errorItemsCount int(11) DEFAULT NULL;
