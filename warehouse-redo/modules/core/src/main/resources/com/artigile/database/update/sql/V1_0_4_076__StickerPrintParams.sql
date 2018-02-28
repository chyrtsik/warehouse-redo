create table sticker_print_param (
  id bigint(20) not null auto_increment,
  detail_type_id bigint(20) not null,
  detail_field_id bigint(20) default null,
  serial_detail_field_id bigint(20) default null,
  order_num bigint(20) not null,
  primary key (id),
  constraint foreign key (detail_type_id) references DetailType (id) on delete cascade,
  constraint foreign key (detail_field_id) references DetailField (id) on delete cascade,
  constraint foreign key (serial_detail_field_id) references DetailField (id) on delete cascade
);

alter table DetailType add column print_serial_numbers boolean not null default false;