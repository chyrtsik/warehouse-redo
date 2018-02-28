ALTER TABLE Contact ADD COLUMN appointment varchar(255) DEFAULT NULL;

CREATE TABLE Shipping (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  address varchar(255) DEFAULT NULL,
  courier varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  note varchar(255) DEFAULT NULL,
  contractor_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (contractor_id) REFERENCES Contractor (id)
);