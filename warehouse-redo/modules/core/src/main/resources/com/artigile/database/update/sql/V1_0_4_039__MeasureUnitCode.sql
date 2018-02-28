alter table MeasureUnit add column code varchar(255) DEFAULT NULL;

create unique index unq_measure_unit_code on MeasureUnit(code);