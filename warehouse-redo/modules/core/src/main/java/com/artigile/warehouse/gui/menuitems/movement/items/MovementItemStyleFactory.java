package com.artigile.warehouse.gui.menuitems.movement.items;

import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.date.DateUtils;
import com.artigile.warehouse.utils.dto.movement.MovementItemTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;

import java.awt.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Valery Barysok, 2013-04-07
 */
public class MovementItemStyleFactory implements StyleFactory {

    private static final Style expiredStyle = new Style(new Color(255, 200, 200));

    private static final Style notExpiredStyle = new Style(null, Color.RED);

    private static final Style moreThreeMonthsStyle = new Style(null, new Color(255, 127, 39));

    private static final Style moreSixMonthsStyle = new Style(null, new Color(0, 128, 0));

    @Override
    public Style getStyle(Object rowData) {
        MovementItemTO movementItem = (MovementItemTO) rowData;
        WarehouseBatchTO warehouseBatch = movementItem.getWarehouseBatch();
        if (warehouseBatch != null) {
            Date shelfLifeDate = warehouseBatch.getShelfLifeDate();
            if (shelfLifeDate != null) {
                long months = DateUtils.getDateDifferenceLength(DateUtils.now(), shelfLifeDate, Calendar.MONTH);
                long milliseconds = DateUtils.getDateDifferenceLength(DateUtils.now(), shelfLifeDate, Calendar.MILLISECOND);
                if (months >= 6) {
                    return moreSixMonthsStyle;
                } else if (months >= 3) {
                    return moreThreeMonthsStyle;
                } else if (milliseconds >= 0) {
                    return notExpiredStyle;
                } else {
                    return expiredStyle;
                }
            }
        }
        return null;
    }
}
