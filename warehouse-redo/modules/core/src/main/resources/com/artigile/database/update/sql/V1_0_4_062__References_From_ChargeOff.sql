-- Reference from charge off to order.
ALTER TABLE chargeoff 
  ADD COLUMN order_id bigint(20) NULL DEFAULT NULL,
  ADD CONSTRAINT FOREIGN KEY fk_chargeoff_order (order_id) REFERENCES orderlist (id);

UPDATE chargeoff set order_id = (
  SELECT DISTINCT oi.order_id FROM orderitem oi, ordersubitem osi, complectingtask ct, chargeoffitem chi
  WHERE chi.chargeOff_id = chargeoff.id
    AND ct.chargeOffItem_id = chi.id
    AND ct.orderSubItem_id = osi.id
    AND osi.orderItem_id = oi.id);

-- Reference from charge off to movement.
ALTER TABLE chargeoff 
  ADD COLUMN movement_id bigint(20) NULL DEFAULT NULL,
  ADD CONSTRAINT FOREIGN KEY fk_chargeoff_movement (movement_id) REFERENCES movement (id);

UPDATE chargeoff set movement_id = (
  SELECT DISTINCT mi.movement_id FROM movementitem mi, complectingtask ct, chargeoffitem chi
  WHERE chi.chargeOff_id = chargeoff.id
    AND ct.chargeOffItem_id = chi.id
    AND ct.movementItem_id = mi.id);



