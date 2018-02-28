update MenuItem
  set name='Товары\\Каталог товаров'
  where pluginClassName='com.artigile.warehouse.gui.menuitems.details.catalog.DetailCatalog';

update MenuItem
  set name='Операции с товаром\\Перемещение товара'
  where pluginClassName='com.artigile.warehouse.gui.menuitems.movement.MovementList';

update MenuItem
  set name='Товары\\Импорт прайс-листа'
  where pluginClassName='com.artigile.warehouse.gui.menuitems.details.batchesImport.DetailBatchesImportList';

update MenuItem
  set name='Склады\\Остатки'
  where pluginClassName='com.artigile.warehouse.gui.menuitems.warehouse.warehousebatch.WarehouseBatchList';
