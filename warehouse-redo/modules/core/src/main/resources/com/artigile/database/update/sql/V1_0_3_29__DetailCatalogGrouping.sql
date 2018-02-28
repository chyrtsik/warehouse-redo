-- Add fields to implement auto grouping in details catalog.
alter table DetailField
  add column catalogGroupNum INT(11) DEFAULT NULL;

alter table DetailGroup
  add column enableAutoSubGroups TINYINT(1) NOT NULL DEFAULT 0;
