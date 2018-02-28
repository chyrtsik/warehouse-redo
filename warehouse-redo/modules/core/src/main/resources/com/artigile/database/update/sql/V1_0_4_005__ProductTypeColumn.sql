alter table ContractorProduct add productType varchar(255) default null;

update ContractorProduct set productType="";

alter table ContractorProduct modify productType varchar(255) not null;