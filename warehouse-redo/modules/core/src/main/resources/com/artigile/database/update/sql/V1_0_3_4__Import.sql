-- Create table for storing application licences.

CREATE TABLE license (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  dateApplied TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  appliedByUser_id bigint(20) NOT NULL,
  licenseData text NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FOREIGN KEY (appliedByUser_id) REFERENCES User (id)  ON DELETE RESTRICT
);