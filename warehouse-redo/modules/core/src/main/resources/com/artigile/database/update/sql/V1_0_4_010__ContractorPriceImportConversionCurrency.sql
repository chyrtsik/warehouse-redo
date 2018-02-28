-- add reference on the currency table
alter table ContractorPriceImport add conversionCurrency_id bigint not null default 1;
create index conversionCurrency_id on ContractorPriceImport (conversionCurrency_id);
alter table ContractorPriceImport add constraint foreign key (conversionCurrency_id) references Currency (id);

-- add exchange rate column
alter table ContractorPriceImport add conversionExchangeRate double not null;

-- update existing records
update ContractorPriceImport set conversionCurrency_id = currency_id, conversionExchangeRate = 1;