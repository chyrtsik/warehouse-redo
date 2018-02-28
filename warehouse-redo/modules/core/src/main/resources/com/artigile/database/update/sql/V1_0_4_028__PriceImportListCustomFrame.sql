-- This script changes the type of the PriceImport panel
UPDATE MenuItem
SET pluginClassName='com.artigile.warehouse.gui.menuitems.priceimport.FilteredPriceImportList',
pluginType='CUSTOM'
WHERE pluginClassName='com.artigile.warehouse.gui.menuitems.priceimport.importing.PriceImportList';