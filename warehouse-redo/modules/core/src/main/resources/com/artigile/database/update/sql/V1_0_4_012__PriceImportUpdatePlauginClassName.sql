-- Incorrect migration. See V1_0_4_028...
update MenuItem
set pluginClassName='com.artigile.warehouse.gui.menuitems.priceimport.FilteredPriceImportList', pluginType='CUSTOM'
where pluginClassName='com.artigile.warehouse.gui.menuitems.priceimport.PriceImportList';