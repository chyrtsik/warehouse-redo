-- Create import status column for PriceImport.

alter table PriceImport
  add column importStatus varchar(255) null;

update PriceImport set importStatus = 'COMPLETED';

alter table PriceImport
  modify column importStatus varchar(255) not null;
