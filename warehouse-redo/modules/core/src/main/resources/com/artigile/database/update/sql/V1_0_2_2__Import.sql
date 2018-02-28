-- Create package column for ContractorProduct.

alter table ContractorProduct
  add column pack varchar(100) not null default '';
