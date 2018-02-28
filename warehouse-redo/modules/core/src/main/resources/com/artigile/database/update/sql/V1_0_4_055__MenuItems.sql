update MenuItem
set name='Склады\\Каталог товаров'
where pluginClassName='com.artigile.warehouse.gui.menuitems.details.catalog.DetailCatalog';

update MenuItem
  set name='Товары\\Перечень товаров'
  where pluginClassName='com.artigile.warehouse.gui.menuitems.details.batches.DetailBatchesList';

update MenuItem
  set name='Товары\\Перечень товаров с параметрами'
  where pluginClassName='com.artigile.warehouse.gui.menuitems.details.batchesext.DetailBatchesExtList';

update MenuItem
  set name='Товары\\Остатки'
  where pluginClassName='com.artigile.warehouse.gui.menuitems.warehouse.warehousebatch.WarehouseBatchList';
