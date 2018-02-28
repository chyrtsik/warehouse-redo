-- Convert print templates to use stored files
ALTER TABLE printtemplateinstance
ADD COLUMN templateFile_id bigint(20) NULL DEFAULT NULL;

ALTER TABLE printtemplateinstance
ADD CONSTRAINT FOREIGN KEY fk_printtemplateinstance_templatefile (templateFile_id) REFERENCES storedfile (id);

-- Enable Jasper support (and customer data used in Jasper only).
ALTER TABLE printtemplateinstance
ADD COLUMN printEngine varchar(10) NULL NULL DEFAULT 'Pentaho';

ALTER TABLE printtemplateinstance
ADD COLUMN compiledTemplate_id bigint NULL DEFAULT NULL;

ALTER TABLE printtemplateinstance
ADD FOREIGN KEY fk_printtemplateinstance_compiledtemplate (compiledTemplate_id) REFERENCES storedfile (id);

-- Migrate to the new data format.
INSERT INTO storedfile(`name`, content)
  SELECT CONCAT('template-', id, '.xml'), templateData FROM printtemplateinstance;

UPDATE printtemplateinstance
  set templateFile_id = (SELECT id FROM storedfile where SUBSTRING(`name`, 10, INSTR(`name`, '.xml') - 10) = printtemplateinstance.id);

ALTER TABLE printtemplateinstance DROP COLUMN templateData;
