-- Rename class of details catalog to reflect it's actual function.
update menuitem set pluginClassName = 'com.artigile.warehouse.gui.menuitems.details.catalog.DetailCatalog'
where pluginClassName = 'com.artigile.warehouse.gui.menuitems.details.catalog.DetailTreeCatalogProperties';
