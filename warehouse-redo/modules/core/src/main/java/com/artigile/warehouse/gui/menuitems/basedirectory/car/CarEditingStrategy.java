package com.artigile.warehouse.gui.menuitems.basedirectory.car;

import com.artigile.warehouse.bl.directory.CarService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.cars.CarTO;

/**
 * @author Valery Barysok, 2013-01-23
 */
public class CarEditingStrategy implements ReportEditingStrategy {

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateCarCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateCarCommand());
        commands.add(new DeleteCarCommand());
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenCarCommand());
        }
    }

    //==================== Commands ===========================================
    private class CreateCarCommand extends CreateCommand {

        protected CreateCarCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            CarTO carTO = new CarTO();
            PropertiesForm prop = new CarForm(carTO, true);
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                getCarService().save(carTO);
                return carTO;
            }
            return null;
        }
    }

    private class DeleteCarCommand extends DeleteCommand {

        protected DeleteCarCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            getCarService().remove((CarTO) deletingItem);
            return true;
        }
    }

    private class OpenCarCommand extends PropertiesCommandBase {

        protected OpenCarCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            CarTO carTO = (CarTO) editingItem;
            PropertiesForm prop = new CarForm(carTO, getEditAvailability().isAvailable(context));
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                getCarService().save(carTO);
                return true;
            }
            return false;
        }
    }

    //====================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_CAR_LIST);
    }

    private CarService getCarService() {
        return SpringServiceContext.getInstance().getCarService();
    }
}
