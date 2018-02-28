ALTER TABLE CurrencyWord ADD fractionalPart boolean NOT NULL DEFAULT FALSE;
ALTER TABLE CurrencyWord ADD fractionalPrecision int(11) NOT NULL DEFAULT 2;
ALTER TABLE CurrencyWord ADD fractionalUnitWord varchar(255) DEFAULT NULL;
ALTER TABLE CurrencyWord ADD fractionalTwoUnitsWord varchar(255) DEFAULT NULL;
ALTER TABLE CurrencyWord ADD fractionalFiveUnitsWord varchar(255) DEFAULT NULL;
ALTER TABLE CurrencyWord ADD fractionalGender varchar(255) DEFAULT NULL;
