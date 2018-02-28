package com.artigile.warehouse.gui.core.properties.dialogs.browser;

/**
 * @author Vadim.Zverugo
 *
 * Model for check boxes used in tree tables.
 */
public interface CheckBoxTreeCellModel {
    /**
     * Returns true is given item is checked.
     * @param item
     * @return
     */
    boolean isItemChecked(Object item);

    /**
     * Called to mark item as checked.
     * @param item
     */
    void checkItem(Object item);

    /**
     * Called to mark item as unchecked.
     * @param item
     */
    void unCheckItem(Object item);
}
