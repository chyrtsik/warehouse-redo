ALTER TABLE AccountOperation ADD COLUMN operation VARCHAR(255) DEFAULT NULL;

UPDATE AccountOperation SET operation = notice;

UPDATE AccountOperation SET notice = "" WHERE notice IS NULL;

COMMIT;

ALTER TABLE AccountOperation MODIFY COLUMN notice varchar(255) NOT NULL;

ALTER TABLE Contractor MODIFY COLUMN notice VARCHAR(10000) DEFAULT NULL;
