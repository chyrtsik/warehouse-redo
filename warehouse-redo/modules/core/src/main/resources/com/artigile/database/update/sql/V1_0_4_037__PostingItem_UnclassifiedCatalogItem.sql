-- Add unclassified catalog items table.
create table unclassified_catalog_item(
  id bigint not null auto_increment,
  barcode varchar(64) null default null,
  primary key (id)
);

-- Modify posting item to support unclassified catalog items.
alter table postingitem modify column detailbatch_id bigint null;
alter table postingitem modify column countMeas_id bigint null;
alter table postingitem add column unclassifiedcatalogitem_id bigint null default null;
alter table postingitem add constraint foreign key (unclassifiedcatalogitem_id) references unclassified_catalog_item (id);
