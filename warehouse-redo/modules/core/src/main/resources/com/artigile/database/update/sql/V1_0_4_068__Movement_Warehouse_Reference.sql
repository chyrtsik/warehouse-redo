-- Move references to warehouse from movement items to movement itself (only one source and target warehouse is allowed from now).
-- 1. Destimation warehouse in movement.
ALTER TABLE movement ADD COLUMN toWarehouse_id bigint(20) NULL DEFAULT NULL;
UPDATE movement set toWarehouse_id = (SELECT toWarehouse_id FROM movementitem mi WHERE mi.movement_id = movement.id AND NOT mi.toWarehouse_id IS NULL LIMIT 1);
UPDATE movement set toWarehouse_id = (SELECT id FROM warehouse LIMIT 1) where toWarehouse_id is null;
ALTER TABLE movement ADD CONSTRAINT fk_movement_to_warehouse FOREIGN KEY (toWarehouse_id) REFERENCES warehouse(id);
ALTER TABLE movement MODIFY COLUMN toWarehouse_id bigint(20) NOT NULL;

ALTER TABLE movementitem DROP FOREIGN KEY movementitem_ibfk_2;
ALTER TABLE movementitem DROP COLUMN toWarehouse_id;
ALTER TABLE movementitem DROP FOREIGN KEY movementitem_ibfk_5;
ALTER TABLE movementitem DROP COLUMN toStoragePlace_id;

-- 2. Source warehouse in movement.
ALTER TABLE movement ADD COLUMN fromWarehouse_id bigint(20) NULL DEFAULT NULL;
UPDATE movement set fromWarehouse_id = (SELECT fromWarehouse_id FROM movementitem mi WHERE mi.movement_id = movement.id AND NOT mi.fromWarehouse_id IS NULL LIMIT 1);
UPDATE movement set fromWarehouse_id = (SELECT id FROM warehouse LIMIT 1) where fromWarehouse_id is null;
ALTER TABLE movement ADD CONSTRAINT fk_movement_from_warehouse FOREIGN KEY (fromWarehouse_id) REFERENCES warehouse(id);
ALTER TABLE movement MODIFY COLUMN fromWarehouse_id bigint(20) NOT NULL;

ALTER TABLE movementitem DROP FOREIGN KEY movementitem_ibfk_8;
ALTER TABLE movementitem DROP COLUMN fromWarehouse_id;