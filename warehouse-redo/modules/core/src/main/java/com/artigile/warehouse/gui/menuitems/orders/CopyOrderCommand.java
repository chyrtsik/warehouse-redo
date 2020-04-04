package com.artigile.warehouse.gui.menuitems.orders;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.orders.OrderService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.orders.Order;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.chooseonealternative.ChooseOneAlternativeForm;
import com.artigile.warehouse.gui.core.report.command.CreateCopyCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.orders.items.OrderDetailItemForm;
import com.artigile.warehouse.gui.menuitems.orders.items.OrderItemsEditor;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;
import com.artigile.warehouse.utils.dto.orders.OrderTO;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

public class CopyOrderCommand extends CreateCopyCommand {

    public CopyOrderCommand() {
        super(new PermissionCommandAvailability(PermissionType.EDIT_ORDER_LIST));
    }

    @Override
    protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
        //Creating new order.
        OrderTOForReport order = new OrderTOForReport();
        OrderTOForReport src = ((OrderTOForReport)context.getCurrentReportItem());
        order.initAndCopyFrom(src);

        PropertiesForm prop = new OrderForm(order, true);
        if (Dialogs.runProperties(prop)) {
            //Saving new order
            OrderService orderService = SpringServiceContext.getInstance().getOrdersService();
            orderService.saveOrder(order);

            //Starting editor of items of the created order.
            Plugin orderContentEditor = new OrderItemsEditor(order.getId());
            WareHouse.runPlugin(orderContentEditor);

            //Adding items from the source order (interactive, as we need to pick, where to take items from)
            OrderTO srcFull = orderService.getOrderFullData(src.getId());
            for (OrderItemTO item : srcFull.getItems()){
                //Try to add new detail item to the order.
                OrderItemTO newOrderItem = new OrderItemTO(order, item);
                PropertiesForm itemProp = new OrderDetailItemForm(newOrderItem, true, false);
                if (Dialogs.runProperties(itemProp)) {
                    try {
                        orderService.addItemToOrder(newOrderItem);
                    } catch (BusinessException e) {
                        throw new ReportCommandException(e);
                    }
                }
            }

            return order;
        }
        return null;
    }
}
