CREATE TABLE CurrencyWord (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  currency_id bigint(20) NOT NULL,
  unitWord varchar(255) DEFAULT NULL,
  twoUnitsWord varchar(255) DEFAULT NULL,
  fiveUnitsWord varchar(255) DEFAULT NULL,
  gender varchar(255) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (currency_id) REFERENCES Currency (id) ON DELETE CASCADE
);