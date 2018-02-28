-- Create table 'email_config'
CREATE TABLE email_config(
  id BIGINT (20) NOT NULL AUTO_INCREMENT,
  serverHost VARCHAR (100) NOT NULL,
  serverPort INT (11) NOT NULL,
  accountUsername VARCHAR (100) NOT NULL,
  accountPassword VARCHAR (100) NOT NULL,
  messageSubject VARCHAR (100),
  warehouse_id BIGINT (20),
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (warehouse_id) REFERENCES warehouse (id) ON DELETE CASCADE
);

