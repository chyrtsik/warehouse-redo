-- Change configuration format for stored price list imports.

update PriceImport set adapterConf = (
  select CONCAT('{StoredFile:', f.id, '}', SUBSTRING(adapterConf, INSTR(adapterConf, ';')))
  from StoredFile f, PriceImport_StoredFile link
  where f.id = link.storedFile_id
    and link.priceImport_id = PriceImport.id
);

-- Remove unused stored files.
delete from StoredFile where not exists(
  select 1 from PriceImport_StoredFile link where link.storedFile_id = StoredFile.id
);