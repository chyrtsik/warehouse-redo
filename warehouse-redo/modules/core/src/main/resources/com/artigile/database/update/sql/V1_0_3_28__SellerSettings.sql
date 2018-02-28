alter table SellerSettings drop key contractorId;
alter table SellerSettings add unique key contractorId (contractorId, user_id);
