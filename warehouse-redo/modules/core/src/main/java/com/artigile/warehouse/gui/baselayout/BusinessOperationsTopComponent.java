package com.artigile.warehouse.gui.baselayout;

import com.artigile.warehouse.domain.MenuItem;
import com.artigile.warehouse.gui.baselayout.explorer.view.ButtonListView;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.ErrorManager;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.explorer.view.TreeView;
import org.openide.nodes.AbstractNode;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

@ConvertAsProperties(dtd = "-//com.artigile//BusinessOperations//EN", autostore = false)
@TopComponent.Description(preferredID = BusinessOperationsTopComponent.PREFERRED_ID,
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", position = 100, openAtStartup = true)
public final class BusinessOperationsTopComponent extends TopComponent implements ExplorerManager.Provider {

    private static BusinessOperationsTopComponent instance;

    static final String PREFERRED_ID = "BusinessOperationsTopComponent"; // NOI18N

    private final ExplorerManager manager;

    private TreeView view;

    private ButtonListView buttonListView;

    public BusinessOperationsTopComponent() {
        manager = new ExplorerManager();
        initComponents();
        associateLookup(ExplorerUtils.createLookup(manager, getActionMap()));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        if (WareHouse.isTerminal()) {
            buttonListView = new ButtonListView();
            add(buttonListView);
        } else {
            view = new BeanTreeView();
            view.setRootVisible(false);
            add(view);
        }

        setName(PREFERRED_ID);
        setName(NbBundle.getMessage(BusinessOperationsTopComponent.class, "CTL_BusinessOperationsTopComponent"));
        setToolTipText(NbBundle.getMessage(BusinessOperationsTopComponent.class, "HINT_BusinessOperationsTopComponent"));
        //putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);
        putClientProperty("TopComponentAllowDockAnywhere", Boolean.FALSE);
    }

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized BusinessOperationsTopComponent getDefault() {
        if (instance == null) {
            instance = new BusinessOperationsTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the NavigatorTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized BusinessOperationsTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find BusinessOperations component. It will not be located properly in the window system."); // NOI18N
            return getDefault();
        }
        if (win instanceof BusinessOperationsTopComponent) {
            return (BusinessOperationsTopComponent) win;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior."); // NOI18N
        return getDefault();
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    @Override
    protected void componentActivated() {
        ExplorerUtils.activateActions(manager, true);
    }

    @Override
    protected void componentDeactivated() {
        ExplorerUtils.activateActions(manager, false);
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    public void refresh(List<MenuItem> menuItems, boolean terminal) {
        if (terminal) {
            buttonListView.setMenuItems(menuItems);
        } else {
            manager.setRootContext(new AbstractNode(new BusinessOperationChildren(menuItems, view)));
        }
    }

    private static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return BusinessOperationsTopComponent.getDefault();
        }
    }
}
