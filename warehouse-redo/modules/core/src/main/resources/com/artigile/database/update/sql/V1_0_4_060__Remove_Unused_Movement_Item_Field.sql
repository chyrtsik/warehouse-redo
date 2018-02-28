-- Remove reference to delivery note as it is not used.
ALTER TABLE movementitem DROP FOREIGN KEY movementitem_ibfk_4;
ALTER TABLE movementitem DROP COLUMN deliveryNoteItem_id;