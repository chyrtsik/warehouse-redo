package com.artigile.installer.products.warehouseclient;

import com.artigile.installer.products.warehouseclient.wizard.panels.WarehouseClientPanel;
import com.artigile.installer.utils.applications.NetBeansRCPUtils;
import org.netbeans.installer.product.components.Product;
import org.netbeans.installer.product.components.ProductConfigurationLogic;
import org.netbeans.installer.utils.*;
import org.netbeans.installer.utils.exceptions.InitializationException;
import org.netbeans.installer.utils.exceptions.InstallationException;
import org.netbeans.installer.utils.exceptions.NativeException;
import org.netbeans.installer.utils.exceptions.UninstallationException;
import org.netbeans.installer.utils.helper.RemovalMode;
import org.netbeans.installer.utils.progress.Progress;
import org.netbeans.installer.utils.system.launchers.LauncherResource;
import org.netbeans.installer.utils.system.shortcut.FileShortcut;
import org.netbeans.installer.utils.system.shortcut.LocationType;
import org.netbeans.installer.utils.system.shortcut.Shortcut;
import org.netbeans.installer.wizard.Wizard;
import org.netbeans.installer.wizard.components.WizardComponent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ConfigurationLogic extends ProductConfigurationLogic {

    private List<WizardComponent> wizardComponents;

    public ConfigurationLogic() throws InitializationException {
        wizardComponents = Wizard.loadWizardComponents(
                WIZARD_COMPONENTS_URI,
                getClass().getClassLoader());
    }

    @Override
    public List<WizardComponent> getWizardComponents() {
        return wizardComponents;
    }

    @Override
    public boolean allowModifyMode() {
        return false;
    }

    @Override
    public void install(Progress progress) throws InstallationException {
        final Product product = getProduct();
        final File installLocation = product.getInstallationLocation();

        if (SystemUtils.isMacOS()) {
            File f = new File(installLocation, ICON_MACOSX);
            if (!f.exists()) {
                try {
                    FileUtils.writeFile(f,
                            ResourceUtils.getResource(ICON_MACOSX_RESOURCE,
                            getClass().getClassLoader()));
                    getProduct().getInstalledFiles().add(f);
                } catch (IOException e) {
                    LogManager.log("... cannot handle icns icon " + f, e); // NOI18N
                }
            }
        }

        if (Boolean.parseBoolean(getProperty(WarehouseClientPanel.CREATE_DESKTOP_SHORTCUT_PROPERTY))) {
            LogManager.logIndent(
                    "creating the desktop shortcut for the application"); // NOI18N
            if (!SystemUtils.isMacOS()) {
                try {
                    progress.setDetail(getString("CL.install.desktop")); // NOI18N

                    if (SystemUtils.isCurrentUserAdmin()) {
                        LogManager.log(
                                "... current user is an administrator " + // NOI18N
                                "-- creating the shortcut for all users"); // NOI18N

                        SystemUtils.createShortcut(
                                getDesktopShortcut(installLocation),
                                LocationType.ALL_USERS_DESKTOP);

                        product.setProperty(
                                DESKTOP_SHORTCUT_LOCATION_PROPERTY,
                                ALL_USERS_PROPERTY_VALUE);
                    } else {
                        LogManager.log(
                                "... current user is an ordinary user " + // NOI18N
                                "-- creating the shortcut for the current " + // NOI18N
                                "user only"); // NOI18N

                        SystemUtils.createShortcut(
                                getDesktopShortcut(installLocation),
                                LocationType.CURRENT_USER_DESKTOP);

                        getProduct().setProperty(
                                DESKTOP_SHORTCUT_LOCATION_PROPERTY,
                                CURRENT_USER_PROPERTY_VALUE);
                    }
                } catch (NativeException e) {
                    LogManager.unindent();

                    LogManager.log(
                            getString("CL.install.error.desktop"), // NOI18N
                            e);
                }
            } else {
                LogManager.log(
                        "... skipping this step as we're on Mac OS"); // NOI18N
            }
        }
        LogManager.logUnindent("... done"); // NOI18N

        if (Boolean.parseBoolean(getProperty(WarehouseClientPanel.CREATE_START_MENU_SHORTCUT_PROPERTY))) {
            LogManager.logIndent(
                    "creating the start menu shortcut for the application"); // NOI18N
            try {
                progress.setDetail(getString("CL.install.start.menu")); // NOI18N

                if (SystemUtils.isCurrentUserAdmin()) {
                    LogManager.log(
                            "... current user is an administrator " + // NOI18N
                            "-- creating the shortcut for all users"); // NOI18N

                    SystemUtils.createShortcut(
                            getStartMenuShortcut(installLocation),
                            LocationType.ALL_USERS_START_MENU);

                    getProduct().setProperty(
                            START_MENU_SHORTCUT_LOCATION_PROPERTY,
                            ALL_USERS_PROPERTY_VALUE);
                } else {
                    LogManager.log(
                            "... current user is an ordinary user " + // NOI18N
                            "-- creating the shortcut for the current " + // NOI18N
                            "user only"); // NOI18N

                    SystemUtils.createShortcut(
                            getStartMenuShortcut(installLocation),
                            LocationType.CURRENT_USER_START_MENU);

                    getProduct().setProperty(
                            START_MENU_SHORTCUT_LOCATION_PROPERTY,
                            CURRENT_USER_PROPERTY_VALUE);
                }
            } catch (NativeException e) {
                LogManager.log(
                        getString("CL.install.error.start.menu"), // NOI18N
                        e);
            }
            LogManager.logUnindent(
                    "... done"); // NOI18N
        }

        File javaHome = new File(System.getProperty("java.home"));
        File target = new File(installLocation, "jre");
        try {
            FileUtils.copyFile(javaHome, target, true);
        } catch (IOException e) {
            throw new InstallationException("Cannot copy JRE",e);
        }
        SystemUtils.getNativeUtils().addUninstallerJVM(new LauncherResource(false, target));
    }

    @Override
    public void uninstall(Progress progress) throws UninstallationException {
        final Product product = getProduct();
        final File installLocation = product.getInstallationLocation();

        if (Boolean.parseBoolean(getProperty(WarehouseClientPanel.CREATE_START_MENU_SHORTCUT_PROPERTY))) {
            try {
                progress.setDetail(getString("CL.uninstall.start.menu")); // NOI18N

                final String shortcutLocation =
                        getProduct().getProperty(START_MENU_SHORTCUT_LOCATION_PROPERTY);

                if ((shortcutLocation == null)
                        || shortcutLocation.equals(CURRENT_USER_PROPERTY_VALUE)) {
                    SystemUtils.removeShortcut(
                            getStartMenuShortcut(installLocation),
                            LocationType.CURRENT_USER_START_MENU,
                            true);
                } else {
                    SystemUtils.removeShortcut(
                            getStartMenuShortcut(installLocation),
                            LocationType.ALL_USERS_START_MENU,
                            true);
                }
            } catch (NativeException e) {
                LogManager.log(
                        getString("CL.uninstall.error.start.menu"), // NOI18N
                        e);
            }
        }

        if (Boolean.parseBoolean(getProperty(WarehouseClientPanel.CREATE_DESKTOP_SHORTCUT_PROPERTY))) {
            if (!SystemUtils.isMacOS()) {
                try {
                    progress.setDetail(getString("CL.uninstall.desktop")); // NOI18N

                    final String shortcutLocation = getProduct().getProperty(
                            DESKTOP_SHORTCUT_LOCATION_PROPERTY);

                    if ((shortcutLocation == null)
                            || shortcutLocation.equals(CURRENT_USER_PROPERTY_VALUE)) {
                        SystemUtils.removeShortcut(
                                getDesktopShortcut(installLocation),
                                LocationType.CURRENT_USER_DESKTOP,
                                false);
                    } else {
                        SystemUtils.removeShortcut(
                                getDesktopShortcut(installLocation),
                                LocationType.ALL_USERS_DESKTOP,
                                false);
                    }
                } catch (NativeException e) {
                    LogManager.log(
                            getString("CL.uninstall.error.desktop"), // NOI18N
                            e);
                }
            }
        }


        if (Boolean.getBoolean("remove.app.userdir")) {
            try {
                progress.setDetail(getString("CL.uninstall.remove.userdir")); // NOI18N
                LogManager.logIndent("Removing application`s userdir... ");
                File userDir = NetBeansRCPUtils.getApplicationUserDirFile(installLocation);
                LogManager.log("... application userdir location : " + userDir);
                if (FileUtils.exists(userDir) && FileUtils.canWrite(userDir)) {
                    FileUtils.deleteFile(userDir, true);
                    FileUtils.deleteEmptyParents(userDir);
                }
                LogManager.log("... application userdir totally removed");
            } catch (IOException e) {
                LogManager.log("Can`t remove application userdir", e);
            } finally {
                LogManager.unindent();
            }
        }

        File jre = new File(installLocation, "jre");
        if (jre.exists()) {
            try {
                for (File file : FileUtils.listFiles(jre).toList()) {
                    FileUtils.deleteOnExit(file);
                }
                FileUtils.deleteOnExit(installLocation);
            } catch (IOException ignored) {
            }
        }

        progress.setPercentage(Progress.COMPLETE);
    }

    @Override
    public String getExecutable() {
        if (SystemUtils.isWindows()) {
            return EXECUTABLE_WINDOWS;
        } else {
            return EXECUTABLE_UNIX;
        }
    }

    @Override
    public String getIcon() {
        if (SystemUtils.isWindows()) {
            return ICON_WINDOWS;
        } else if (SystemUtils.isMacOS()) {
            return ICON_MACOSX;
        } else {
            return ICON_UNIX;
        }
    }

    @Override
    public RemovalMode getRemovalMode() {
        return RemovalMode.LIST;
    }

    @Override
    public boolean registerInSystem() {
        return true;
    }

    @Override
    public boolean requireLegalArtifactSaving() {
        return false;
    }

    @Override
    public boolean requireDotAppForMacOs() {
        return true;
    }

    @Override
    public boolean wrapForMacOs() {
        return true;
    }

    private Shortcut getDesktopShortcut(final File directory) {
        return getShortcut(
                getStrings("CL.desktop.shortcut.name"), // NOI18N
                getStrings("CL.desktop.shortcut.description"), // NOI18N
                getString("CL.desktop.shortcut.path"), // NOI18N
                directory);
    }

    private Shortcut getStartMenuShortcut(final File directory) {
        if (SystemUtils.isMacOS()) {
            return getShortcut(
                    getStrings("CL.start.menu.shortcut.name.macosx"), // NOI18N
                    getStrings("CL.start.menu.shortcut.description"), // NOI18N
                    getString("CL.start.menu.shortcut.path"), // NOI18N
                    directory);
        } else {
            return getShortcut(
                    getStrings("CL.start.menu.shortcut.name"), // NOI18N
                    getStrings("CL.start.menu.shortcut.description"), // NOI18N
                    getString("CL.start.menu.shortcut.path"), // NOI18N
                    directory);
        }
    }

    private Shortcut getShortcut(
            final Map<Locale, String> names,
            final Map<Locale, String> descriptions,
            final String relativePath,
            final File location) {
        final File icon;
        final File executable;

        if (SystemUtils.isWindows()) {
            icon = new File(location, ICON_WINDOWS);
        } else if (SystemUtils.isMacOS()) {
            icon = new File(location, ICON_MACOSX);
        } else {
            icon = new File(location, ICON_UNIX);
            LogManager.log("... icon file: " + icon);
            if (!FileUtils.exists(icon)) {
                LogManager.log("... icon file does not exist: " + icon);
                InputStream is = null;
                is = ResourceUtils.getResource(ICON_UNIX_RESOURCE, this.getClass().getClassLoader());
                if (is != null) {
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(icon);
                        StreamUtils.transferData(is, fos);
                        is.close();
                        fos.close();
                        getProduct().getInstalledFiles().add(icon);
                    } catch (IOException e) {
                        LogManager.log(e);
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                }
            }
        }

        if (SystemUtils.isWindows()) {
            executable = new File(location, EXECUTABLE_WINDOWS);
        } else {
            executable = new File(location, EXECUTABLE_UNIX);
        }
        final String name = names.get(new Locale(StringUtils.EMPTY_STRING));
        final FileShortcut shortcut = new FileShortcut(name, executable);
        shortcut.setNames(names);
        shortcut.setDescriptions(descriptions);
        shortcut.setCategories(SHORTCUT_CATEGORIES);
        shortcut.setFileName(SHORTCUT_FILENAME);
        shortcut.setIcon(icon);
        shortcut.setRelativePath(relativePath);
        shortcut.setWorkingDirectory(location);
        shortcut.setModifyPath(true);

        return shortcut;
    }

    public static final String WIZARD_COMPONENTS_URI =
            "resource:" + // NOI18N
            "com/artigile/installer/products/warehouseclient/wizard.xml"; // NOI18N
    public static final String ICON_MACOSX =
            ResourceUtils.getString(ConfigurationLogic.class, "CL.app.name") + ".icns"; // NOI18N
    public static final String ICON_MACOSX_RESOURCE =
            "com/artigile/installer/products/warehouseclient/" + ResourceUtils.getString(ConfigurationLogic.class, "CL.app.name") + ".icns"; // NOI18N
    public static final String SHORTCUT_FILENAME =
            ResourceUtils.getString(ConfigurationLogic.class, "CL.app.name") + ".desktop"; // NOI18N
    public static final String[] SHORTCUT_CATEGORIES = new String[] {
        "Application"
    };
    public static final String BIN_SUBDIR = "bin/";
    public static final String EXECUTABLE_WINDOWS =
            BIN_SUBDIR
            + ResourceUtils.getString(ConfigurationLogic.class, "CL.app.name") + ".exe"; // NOI18N
    public static final String EXECUTABLE_UNIX =
            BIN_SUBDIR
            + ResourceUtils.getString(ConfigurationLogic.class, "CL.app.name"); // NOI18N
    public static final String ICON_WINDOWS = EXECUTABLE_WINDOWS;
    public static final String ICON_UNIX =
            ResourceUtils.getString(ConfigurationLogic.class,
            "CL.unix.icon.name"); // NOI18N
    public static final String ICON_UNIX_RESOURCE =
            ResourceUtils.getString(ConfigurationLogic.class,
            "CL.unix.icon.resource"); // NOI18N
    private static final String DESKTOP_SHORTCUT_LOCATION_PROPERTY =
            "desktop.shortcut.location"; // NOI18N
    private static final String START_MENU_SHORTCUT_LOCATION_PROPERTY =
            "start.menu.shortcut.location"; // NOI18N
    private static final String ALL_USERS_PROPERTY_VALUE =
            "all.users"; // NOI18N
    private static final String CURRENT_USER_PROPERTY_VALUE =
            "current.user"; // NOI18N
}
