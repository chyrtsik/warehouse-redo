-- Fix detailBatch currency column.
alter table DetailBatch modify currency_id bigint(20) not null;

-- Add columns for the second sell price.
alter table DetailBatch add column currency2_id bigint(20) default null;
alter table DetailBatch add column sellPrice2 decimal(14,4) default null;

