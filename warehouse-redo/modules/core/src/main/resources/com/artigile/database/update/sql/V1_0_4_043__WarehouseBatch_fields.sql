-- Add new field to warehouse batch for tracking of buy price and receipt date (posting item has all information about this).

alter table WarehouseBatch
  add column postingItem_id bigint null default null;

alter table WarehouseBatch
  add foreign key FK_WarehouseBatch_PostingItem (postingItem_id) references PostingItem (id);

create unique index IND_WarehouseBatch_Unique 
  on WarehouseBatch(detailBatch_Id, storagePlace_Id, notice, postingItem_id);

alter table WarehouseBatch 
  drop index IND_WarehouseBatch_detailBatch_Id;

-- Add new field to track posting item of warehouse batch in detail batch history. This will allow us to
-- distinguish detail batch operations by receipt date and price.
 alter table DetailBatchOperation
  add column postingItemOfChangedWarehouseBatch_id bigint null default null;

alter table DetailBatchOperation
  add foreign key FK_DetailBatchOperation_BatchPostingItem (postingItemOfChangedWarehouseBatch_id) references PostingItem (id);