-- Create proper indexes on warehouse batch.
alter table WarehouseBatch drop FOREIGN KEY warehousebatch_ibfk_1;
drop index detailBatch_id on WarehouseBatch;
create unique index IND_WarehouseBatch_detailBatch_Id on WarehouseBatch(detailBatch_Id, storagePlace_Id, notice);
alter table WarehouseBatch add constraint FOREIGN KEY warehousebatch_ibfk_1 (detailBatch_id) references DetailBatch(id);
