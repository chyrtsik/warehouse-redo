-- Split print templates model into type and instance (to allow create different instances of the same print template).

CREATE TABLE PrintTemplateInstance(
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) DEFAULT NULL,
  printTemplate_id BIGINT(20) NOT NULL,
  templateData LONGBLOB,
  defaultTemplate BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id),
  UNIQUE KEY name (name),
  CONSTRAINT FOREIGN KEY (printTemplate_id) REFERENCES PrintTemplate (id)
);

insert into PrintTemplateInstance(name, description, printTemplate_id, templateData, defaultTemplate)
  select name, description, id, templateData, true from PrintTemplate;

ALTER TABLE PrintTemplate
        DROP COLUMN description,
        DROP COLUMN templateData;