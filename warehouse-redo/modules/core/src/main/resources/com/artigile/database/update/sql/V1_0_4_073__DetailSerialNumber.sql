-- Table for serial numbers.
CREATE TABLE DetailSerialNumber (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  detail_id bigint(20) NOT NULL,
  version bigint(20) not null default 0,
  field1 varchar(255) DEFAULT NULL,
  field10 varchar(255) DEFAULT NULL,
  field11 varchar(255) DEFAULT NULL,
  field12 varchar(255) DEFAULT NULL,
  field13 varchar(255) DEFAULT NULL,
  field14 varchar(255) DEFAULT NULL,
  field15 varchar(255) DEFAULT NULL,
  field16 varchar(255) DEFAULT NULL,
  field17 varchar(255) DEFAULT NULL,
  field18 varchar(255) DEFAULT NULL,
  field19 varchar(255) DEFAULT NULL,
  field2 varchar(255) DEFAULT NULL,
  field20 varchar(255) DEFAULT NULL,
  field21 varchar(255) DEFAULT NULL,
  field22 varchar(255) DEFAULT NULL,
  field23 varchar(255) DEFAULT NULL,
  field24 varchar(255) DEFAULT NULL,
  field25 varchar(255) DEFAULT NULL,
  field26 varchar(255) DEFAULT NULL,
  field27 varchar(255) DEFAULT NULL,
  field28 varchar(255) DEFAULT NULL,
  field29 varchar(255) DEFAULT NULL,
  field3 varchar(255) DEFAULT NULL,
  field30 varchar(255) DEFAULT NULL,
  field4 varchar(255) DEFAULT NULL,
  field5 varchar(255) DEFAULT NULL,
  field6 varchar(255) DEFAULT NULL,
  field7 varchar(255) DEFAULT NULL,
  field8 varchar(255) DEFAULT NULL,
  field9 varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (detail_id) REFERENCES DetailBatch (id)
);

-- Ability to store list fields of serial numbers in detail type.
CREATE TABLE DetailType_SerialNumberField (
  detailType_id bigint(20) NOT NULL,
  detailField_id bigint(20) NOT NULL,
  UNIQUE KEY detailField_id (detailField_id),
  CONSTRAINT FOREIGN KEY (detailField_id) REFERENCES DetailField (id),
  CONSTRAINT FOREIGN KEY (detailType_id) REFERENCES DetailType (id)
);