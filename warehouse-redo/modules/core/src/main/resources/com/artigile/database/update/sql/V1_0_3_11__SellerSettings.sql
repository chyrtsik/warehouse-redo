delete from SellerSettings;

commit;

alter table SellerSettings add column user_id bigint(20) not null;

alter table SellerSettings add constraint foreign key (user_id) references user (id) on delete cascade;