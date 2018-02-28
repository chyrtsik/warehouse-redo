-- Remove temporary created fields (stub fields used just for testing).
-- Detail model fields will be dynamic (no predefined mapping will be required).
DELETE
FROM
  PrintTemplateFieldMapping
WHERE
  reportField LIKE '%.{%}';