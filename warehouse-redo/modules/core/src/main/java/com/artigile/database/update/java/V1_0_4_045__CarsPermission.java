package com.artigile.database.update.java;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.plugin.PluginType;
import com.artigile.warehouse.gui.menuitems.basedirectory.car.CarList;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.jetbrains.annotations.PropertyKey;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Valery Barysok, 2013-01-23
 */
public class V1_0_4_045__CarsPermission implements JavaMigration {

    private static final String sqlUserPermissionInsert = "INSERT INTO UserPermission (rightType, NAME) VALUES (?, ?);";

    private static final String sqlMenuItemInsert = "insert into MenuItem (name, pluginType, pluginClassName, viewPermission_id) values (?, ?, ?, (select id from UserPermission where rightType=?));";

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;
        initUserPermission(PermissionType.VIEW_CAR_LIST, "permission.view.car.list");
        initUserPermission(PermissionType.EDIT_CAR_LIST, "permission.edit.car.list");

        initMenuItem("menutree.basedirectory.car.list", PluginType.TABLE_REPORT, CarList.class, PermissionType.VIEW_CAR_LIST);
    }

    private void initMenuItem(@PropertyKey(resourceBundle = "i18n.warehouse") String menuNameRes, PluginType pluginType, Class pluginClass, PermissionType viewPermission) {
        jdbcTemplate.update(sqlMenuItemInsert, I18nSupport.message(menuNameRes), pluginType.name(), pluginClass.getCanonicalName(), viewPermission.name());
    }

    private void initUserPermission(PermissionType permissionType, @PropertyKey(resourceBundle = "i18n.warehouse") String permissionNameRes) {
        jdbcTemplate.update(sqlUserPermissionInsert, permissionType.name(), I18nSupport.message(permissionNameRes));
    }
}
