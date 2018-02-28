-- Add column for value added tax (VAT) percentage.
alter table OrderList add vatRate DECIMAL(6, 4) NULL;