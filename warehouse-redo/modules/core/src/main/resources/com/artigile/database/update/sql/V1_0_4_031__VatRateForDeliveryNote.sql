-- Adding VAT rate to delivery notes.
alter table DeliveryNote add vatRate DECIMAL(6, 4) NULL;

-- Copy VAT rate from orders for old delivery notes.
update DeliveryNote set vatRate = (
  select distinct o.vatRate from OrderList o, OrderItem oi, OrderSubItem osi, ComplectingTask ct, ChargeOffItem choi, DeliveryNoteItem dni
    where o.id = oi.order_id
      and oi.id = osi.orderItem_id
      and osi.id = ct.orderSubItem_id
      and ct.chargeOffItem_id = choi.id
      and choi.deliveryNoteItem_id = dni.id
      and dni.deliveryNote_id = DeliveryNote.id
);

