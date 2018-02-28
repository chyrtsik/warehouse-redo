alter table DetailType add column print_template_instance_id bigint(20) default null;

alter table DetailType add constraint foreign key (print_template_instance_id) references PrintTemplateInstance (id);