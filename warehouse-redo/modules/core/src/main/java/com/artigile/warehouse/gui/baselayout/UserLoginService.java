/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.baselayout;

import com.artigile.warehouse.bl.common.exceptions.CannotPerformOperationException;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.authentification.MySqlAuthenticator;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.exception.FlywayException;
import com.googlecode.flyway.core.metadatatable.MetaDataTableRow;
import com.googlecode.flyway.core.migration.Migration;
import com.googlecode.flyway.core.migration.MigrationProvider;
import org.jdesktop.swingx.auth.LoginService;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * @author Valery Barysok, 28.03.2010
 */
public class UserLoginService extends LoginService {

    private Component ownerComponent;

    public UserLoginService() {
    }

    @Override
    public boolean authenticate(String name, char[] password,
                                String server) throws Exception {
        if (name.equals("")) {
            return false;
        }

        if (!MySqlAuthenticator.updateConnectionProperties(server)) {
            return false;
        }
        
        String pwd = String.valueOf(password);
        try {
            //Update database schema.
            if (!updateDatabaseIfNeeds(name, pwd)) {
                return false;
            }

            //Perform database authentication.
            if (!MySqlAuthenticator.loginIntoDb(name, pwd)) {
                return false;
            }

            // Initialize spring beans (this is a little trick to prevent long loading after login window disappears).
            // This trick does not work before database update because hibernate in action here.
            SpringServiceContext.getInstance().getUserService();
        }
        catch (CannotPerformOperationException e) {
            //Rethrow original exception (this exception should be shown to the user).
            throw e;
        }
        catch (Throwable ex) {
            LoggingFacade.logError(this, ex);
            throw new Exception(ex);
        }

        setServer(server);
        return true;
    }

    private boolean updateDatabaseIfNeeds(String name, String pwd) throws ClassNotFoundException, SQLException, CannotPerformOperationException {
        Properties props = MySqlAuthenticator.getConnectionProperties();
        props.setProperty("user", name);
        props.setProperty("password", MySqlAuthenticator.encodePassword(pwd));

        String databaseClassName = props.getProperty("database.driver");
        Class.forName(databaseClassName);

        DataSource dataSource = new DriverManagerDataSource(MySqlAuthenticator.getDatabaseUrl().getDatabaseUrl(), props);

        CustomFlyway flyway = new CustomFlyway();
        flyway.setBasePackage("com.artigile.database.update.java");
        flyway.setBaseDir("com/artigile/database/update/sql");
        flyway.setDataSource(dataSource);

        MetaDataTableRow status = flyway.status();
        if (status == null) {
            flyway.init();
        }

        if (!flyway.haveChangesToApply()) {
            //No database update is needed.
            return true;
        }

        if (!name.equalsIgnoreCase("admin")) {
            //Only administrator is allowed to perform database update.
            //TODO: Refactor and remove message dialog.
            MessageDialogs.showWarning(ownerComponent, I18nSupport.message("database.migration.must.login.as.admin"));
            throw new CannotPerformOperationException(I18nSupport.message("database.migration.must.login.as.admin"));
        }

        if (!MessageDialogs.showConfirm(ownerComponent,
                I18nSupport.message("database.migration.confirm.title"),
                I18nSupport.message("database.migration.confirm.message"))) {
            //TODO: Refactor and remove message dialog.
            MessageDialogs.showWarning(ownerComponent, I18nSupport.message("database.migration.was.cancelled"));
            throw new CannotPerformOperationException(I18nSupport.message("database.migration.was.cancelled"));
        }

        MetaDataTableRow preMigrateStatus = flyway.status();
        try {
            flyway.migrate();
        } catch (FlywayException e) {
            //TODO: Refactor and remove message dialog.
            MessageDialogs.showError(ownerComponent, I18nSupport.message("database.migration.failed"));
            throw new CannotPerformOperationException(I18nSupport.message("database.migration.failed"), e);
        }
        MetaDataTableRow postMigrateStatus = flyway.status();

        boolean isMigrated = preMigrateStatus == null && postMigrateStatus != null ||
                preMigrateStatus != null && postMigrateStatus != null && preMigrateStatus.compareTo(postMigrateStatus) != 0;
        if (isMigrated) {
            SpringServiceContext.getInstance().getLockingManagerService().prepareLockSupport();
        }

        MessageDialogs.showMessage(ownerComponent,
                 I18nSupport.message("database.migration.success.title"),
                 I18nSupport.message("database.migration.success.message"));

        return true;
    }

    public void setOwnerComponent(Component ownerComponent) {
        this.ownerComponent = ownerComponent;
    }

    /**
     * Flyway implementation allowing to determine if there are any database changes to be applied.
     */
    private static class CustomFlyway extends Flyway {

        public boolean haveChangesToApply() {
            //1. Get a list of all possible data schema migrations.
            MigrationProvider migrationProvider = new MigrationProvider(getBasePackage(), getBaseDir(),
                    getEncoding(), getSqlMigrationPrefix(), getSqlMigrationSuffix(), getPlaceholders(),
                    getPlaceholderPrefix(), getPlaceholderSuffix());
            List<Migration> availableMigrations = migrationProvider.findAvailableMigrations();
            if (availableMigrations.isEmpty()) {
                return false;
            }

            MetaDataTableRow status = status();
            if (status == null) {
                return true;
            }

            return status.getVersion().compareTo(availableMigrations.get(0).getVersion()) < 0;
        }
    }
}
