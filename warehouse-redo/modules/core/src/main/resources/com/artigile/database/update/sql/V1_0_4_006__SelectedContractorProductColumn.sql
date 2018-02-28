-- Add column for selected contractor products.
alter table ContractorProduct add selected boolean not null default false;