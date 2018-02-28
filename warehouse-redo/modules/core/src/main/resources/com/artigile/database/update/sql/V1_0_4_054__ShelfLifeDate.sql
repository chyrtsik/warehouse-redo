alter table ChargeOffItem
    add column shelf_life_date datetime;

alter table DeliveryNoteItem
    add column shelf_life_date datetime;

alter table MovementItem
    add column shelf_life_date datetime;

create index detailBatch_Id
    on WarehouseBatch(detailBatch_Id);

drop index IND_WarehouseBatch_Unique
    on WarehouseBatch;

create unique index IND_WarehouseBatch_Unique
    on WarehouseBatch(detailBatch_Id, storagePlace_Id, notice, postingItem_id, shelf_life_date);
