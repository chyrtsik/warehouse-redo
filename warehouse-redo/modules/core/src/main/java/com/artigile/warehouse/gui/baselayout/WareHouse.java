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

import com.artigile.swingx.auth.JXTerminalLoginPane;
import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.bl.license.LicenseCheckResult;
import com.artigile.warehouse.bl.license.LicenseService;
import com.artigile.warehouse.domain.MenuItem;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.plugin.PluginParams;
import com.artigile.warehouse.gui.core.plugin.PluginType;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.gui.core.report.controller.TableFramePlugin;
import com.artigile.warehouse.gui.menuitems.warehouse.warehouselist.WarehouseSelectForm;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.configuration.impl.Server;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.preferences.SystemPreferencesUtils;
import com.artigile.warehouse.utils.xml.ServersInitializer;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXLoginPane.JXLoginDialog;
import org.jdesktop.swingx.JXLoginPane.Status;
import org.openide.LifecycleManager;
import org.openide.util.ImageUtilities;
import org.openide.windows.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WareHouse {

    private static boolean terminal;

    //=================================== User session support ================================
    public static UserSessionInfo userSessionInfo = null;

    public static UserSessionInfo getUserSession() {
        return userSessionInfo;
    }

    public static boolean isTerminal() {
        return terminal;
    }

    public static void setTerminal(boolean terminal) {
        WareHouse.terminal = terminal;
    }

    //============================ User session listeners support ============================
    private static List<UserSessionListener> userSessionListeners = new ArrayList<UserSessionListener>();

    public static void addUserSessionListener(UserSessionListener listener){
        if (!userSessionListeners.contains(listener)){
            userSessionListeners.add(listener);
        }
    }

    public static void removeUserSessionListener(UserSessionListener listener){
        userSessionListeners.remove(listener);
    }

    private static void fireSessionBegin(UserSessionInfo userSessionInfo) {
        for (UserSessionListener listener : userSessionListeners){
            listener.onUserSessionBegin(userSessionInfo);
        }
    }

    //============================ Coordinates helpers ==========================================
    public static int getGlobalWidth() {
        Frame mainWindow = WindowManager.getDefault().getMainWindow();
        return mainWindow != null ? mainWindow.getWidth() : 0;
    }

    public static int getGlobalHeight() {
        Frame mainWindow = WindowManager.getDefault().getMainWindow();
        return mainWindow != null ? mainWindow.getHeight() : 0;
    }

    public static int getGlobalLeft() {
        Frame mainWindow = WindowManager.getDefault().getMainWindow();
        return mainWindow != null ? mainWindow.getX() : 0;
    }

    public static int getGlobalTop() {
        Frame mainWindow = WindowManager.getDefault().getMainWindow();
        return mainWindow != null ? mainWindow.getY() : 0;
    }

    /**
     * Use this method to obtain main frame of the application.
     *
     * @return main frame of the application.
     */
    public static Frame getMainFrame() {
        return WindowManager.getDefault().getMainWindow();
    }

    public static ClassLoader getMainLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static void init() {
        ServersInitializer serversInitializer = ServersInitializer.getInstance();
        List<String> servers = serversInitializer.getServerNames();

        //1. Login into the system.
        final UserLoginService loginService = new UserLoginService();
        boolean isTerminal = isTerminal();
        JXLoginPane loginPane = new JXTerminalLoginPane(loginService, null, null, servers, isTerminal);
        Image banner = ImageUtilities.loadImage("images/login.png");
        if (banner != null) {
            loginPane.setBanner(banner);
        }
        JXLoginDialog loginDialog = new JXLoginDialog((Frame) null, loginPane);
        loginService.setOwnerComponent(loginDialog);
        loginDialog.setVisible(true);
        Status status = loginDialog.getStatus();
        if (status != JXLoginPane.Status.SUCCEEDED) {
            loginDialog.dispose();
            LifecycleManager.getDefault().exit();
            return;
        }

        //2. Check license.
        UserService userService = SpringServiceContext.getInstance().getUserService();
        UserTO loggedUser = userService.getUserByLogin(loginPane.getUserName());

        LicenseService licenseService = SpringServiceContext.getInstance().getLicenseService();
        LicenseCheckResult licenseResultCheckResult = licenseService.checkLicense();
        if (!licenseResultCheckResult.isValid()){
            //There are no valid licences.
            boolean userCanEditLicences = licenseService.canUserEditLicenses(loggedUser.getId());
            if (userCanEditLicences){
                //Allow user to continue because he has permission to work with licence list.
                //We need him to work with application to enter new licence data.
                MessageDialogs.showWarning(I18nSupport.message("license.invalid.message.administrator", licenseResultCheckResult.getDescription()));
            }
            else{
                //Ordinary user is not allowed to work with application until license problem is fixed.
                MessageDialogs.showWarning(I18nSupport.message("license.invalid.message.usual.user", licenseResultCheckResult.getDescription()));
                LifecycleManager.getDefault().exit();
            }
        }
        else if (licenseResultCheckResult.getDaysToExpire() != null && licenseResultCheckResult.getDaysToExpire() <= 14){
            //Notify user about expiring license.
            MessageDialogs.showInfo(I18nSupport.message("license.expire.soon.warning", licenseResultCheckResult.getDaysToExpire()));
        }

        UserSessionInfo newUserSessionInfo = new UserSessionInfo();
        newUserSessionInfo.setUser(loggedUser);
        newUserSessionInfo.setDatabaseName(loginService.getServer());

        //3. Choose warehouse (if needed by current application configuration).
        Properties applicationProperties = SpringServiceContext.getInstance().getApplicationProperties();
        if (Boolean.valueOf(applicationProperties.getProperty("choose.warehouse.on.login")) && !isTerminal) {
            WarehouseSelectForm warehouseForm = new WarehouseSelectForm();
            PropertiesDialog wareHouseDialog = new PropertiesDialog(warehouseForm);
            if (!wareHouseDialog.run()) {
                LifecycleManager.getDefault().exit();
                return;
            }
            newUserSessionInfo.setUserWarehouse(warehouseForm.getWarehouse());
        }

        //4. Broadcast new session info.
        userSessionInfo = newUserSessionInfo;
        fireSessionBegin(userSessionInfo);

        //5. Initialize task tree.
        refreshMenuTree();

        //6. Configure system tooltips
        configureSystemTooltips();

        //7. Load system preferences
        SystemPreferencesUtils.applySysOptions();
    }

    private static void configureSystemTooltips() {
        ToolTipManager.sharedInstance().setInitialDelay(200);
        ToolTipManager.sharedInstance().setReshowDelay(200);
    }

    public static void refreshMenuTree() {
        UserService userService = SpringServiceContext.getInstance().getUserService();
        List<MenuItem> menuItems = userService.loadMenuItemsForUser(getUserSession().getUser().getId());
        BusinessOperationsTopComponent botc = BusinessOperationsTopComponent.findInstance();
        botc.refresh(menuItems, isTerminal());
    }

    /**
     * Loads plugin.
     *
     * @param pluginType type of plugin to be used.
     * @param pluginClassName class path to plugin
     * @return plugin instance.
     */
    private static Plugin loadPlugin(PluginType pluginType, String pluginClassName) {
        try {
            if (pluginType == PluginType.TABLE_REPORT) {
                //TableReportView tableReport plugin.
                Class dataSourceDefinition = Class.forName(pluginClassName);
                ReportDataSource dataSource = (ReportDataSource) dataSourceDefinition.getConstructor().newInstance();
                return new TableFramePlugin(dataSource);
            } else if (pluginType == PluginType.CUSTOM) {
                //Non predefined type of plugin.
                Class pluginDefinition = Class.forName(pluginClassName);
                return (Plugin) pluginDefinition.getConstructor().newInstance();
            } else {
                throw new RuntimeException("Unsupported plugin type.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method may be used from anywhere from the application to execute plugins in
     * the MDI child frames.
     *
     * @param plugin plugin to be executed.
     */
    public static void runPlugin(Plugin plugin) {
        try {
            //TODO:
            PluginParams params = new PluginParams(null);
            plugin.run(params);
        } catch (Throwable e) {
            MessageDialogs.showError(getMainFrame(), e);
        }
    }

    /**
     * This method may be used for starting report with given data source.
     *
     * @param reportDataSource implementation of data source if this report.
     */
    public static void runReportPlugin(ReportDataSource reportDataSource) {
        Plugin tableReportPlugin = new TableFramePlugin(reportDataSource);
        runPlugin(tableReportPlugin);
    }

    public static void open(MenuItem item) {
        if (item != null) {
            Plugin plugin = loadPlugin(item.getPluginType(), item.getPluginClassName());
            runPlugin(plugin);
        }
    }
}
