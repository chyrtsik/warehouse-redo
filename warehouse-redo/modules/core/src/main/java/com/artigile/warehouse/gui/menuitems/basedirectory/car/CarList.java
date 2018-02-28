package com.artigile.warehouse.gui.menuitems.basedirectory.car;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.cars.CarTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Valery Barysok, 2013-01-23
 */
public class CarList extends ReportDataSourceBase {

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(CarTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("basedirectory.car.list.brand"), "brand"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("basedirectory.car.list.state.number"), "stateNumber"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("basedirectory.car.list.full.name"), "fullName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("basedirectory.car.list.owner"), "owner"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("basedirectory.car.list.trailer"), "trailer"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("basedirectory.car.list.description"), "description"));
        return reportInfo;
    }

    @Override
    public String getReportTitle() {
        return I18nSupport.message("basedirectory.car.list.title");
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new CarEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getCarService().getAll();
    }
}
