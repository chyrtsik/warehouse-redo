package com.artigile.installer.wizard.components.panels;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.netbeans.installer.product.components.Group;
import org.netbeans.installer.product.components.StatusInterface;
import org.netbeans.installer.utils.helper.swing.NbiCheckBox;
import java.util.LinkedList;
import org.netbeans.installer.product.Registry;
import org.netbeans.installer.product.RegistryType;
import org.netbeans.installer.product.components.Product;
import org.netbeans.installer.product.dependencies.Conflict;
import org.netbeans.installer.product.dependencies.Requirement;
import org.netbeans.installer.utils.ErrorManager;
import org.netbeans.installer.utils.StringUtils;
import org.netbeans.installer.utils.helper.Dependency;
import org.netbeans.installer.utils.helper.Status;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import org.netbeans.installer.utils.UiUtils;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.installer.product.RegistryNode;
import org.netbeans.installer.utils.ResourceUtils;
import org.netbeans.installer.utils.helper.swing.NbiButton;
import org.netbeans.installer.utils.helper.swing.NbiDialog;
import org.netbeans.installer.utils.helper.swing.NbiFrame;
import org.netbeans.installer.utils.helper.swing.NbiLabel;
import org.netbeans.installer.utils.helper.swing.NbiList;
import org.netbeans.installer.utils.helper.swing.NbiPanel;
import org.netbeans.installer.utils.helper.swing.NbiScrollPane;
import org.netbeans.installer.utils.helper.swing.NbiTextPane;
import static org.netbeans.installer.wizard.components.panels.ErrorMessagePanel.ErrorMessagePanelSwingUi.*;

/**
 *
 * @author Valery.Barysok
 */
public class CustomizeSelectionDialog extends NbiDialog {

    private WelcomePanel panel;
    private Runnable callback;
    private List<RegistryNode> registryNodes;
    private NbiLabel messageLabel;
    private NbiList componentsList;
    private NbiScrollPane componentsScrollPane;
    private NbiTextPane descriptionPane;
    private NbiScrollPane descriptionScrollPane;
    private NbiPanel componentPanel;
    private NbiLabel sizesLabel;
    private NbiLabel errorLabel;
    private NbiButton okButton;
    private NbiButton cancelButton;
    private NbiPanel buttonsPanel;
    private Icon errorIcon;
    private Icon warningIcon;
    private Icon emptyIcon;

    public CustomizeSelectionDialog(NbiFrame parent, WelcomePanel panel, Runnable callback, List<RegistryNode> registryNodes) {
        super(parent);
        this.panel = panel;
        this.callback = callback;
        this.registryNodes = registryNodes;

        errorIcon = new ImageIcon(
                getClass().getClassLoader().getResource(ERROR_ICON));
        warningIcon = new ImageIcon(
                getClass().getClassLoader().getResource(WARNING_ICON));
        emptyIcon = new ImageIcon(
                getClass().getClassLoader().getResource(EMPTY_ICON));

        setDefaultMinimumSize();
        initComponents();
    }

    private void setDefaultMinimumSize() {
        switch (UiUtils.getLAF()) {
            case WINDOWS_CLASSIC:
            case WINDOWS_XP:
                setMinimumSize(new Dimension(560, 420 + EXTRA_SIZE));
                break;
            case GTK:
                setMinimumSize(new Dimension(660, 500 + EXTRA_SIZE));
                break;
            case AQUA:
                setMinimumSize(new Dimension(550, 410 + EXTRA_SIZE));
                break;
            case MOTIF:
            case METAL:
                setMinimumSize(new Dimension(620, 460 + EXTRA_SIZE));
                break;
            default:
                setMinimumSize(new Dimension(560, 420 + EXTRA_SIZE));
                break;
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            initialize();
        }

        super.setVisible(visible);
    }

    private void initComponents() {
        //workaround with minimum size for JDK5
        addComponentListener(new ComponentAdapter()   {

            @Override
            public void componentResized(ComponentEvent e) {
                setSize(Math.max(getSize().width, getMinimumSize().width),
                        Math.max(getSize().height, getMinimumSize().height));
            }
        });
        messageLabel = new NbiLabel();

        componentsList = new NbiList();
        componentsList.setCellRenderer(
                new ComponentsListCellRenderer());
        componentsList.setBorder(
                new EmptyBorder(5, 5, 5, 5));
        componentsList.getSelectionModel().setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        componentsList.getSelectionModel().addListSelectionListener(
                new ListSelectionListener()   {

            @Override
                    public void valueChanged(ListSelectionEvent event) {
                        updateDescription();
                    }
                });
        componentsList.addMouseListener(new MouseAdapter()   {

            @Override
            public void mouseReleased(MouseEvent event) {
                int index = componentsList.locationToIndex(event.getPoint());
                if (index != -1) {
                    final MouseEvent newEvent = new MouseEvent(
                            (Component) event.getSource(),
                            event.getID(),
                            event.getWhen(),
                            event.getModifiers(),
                            event.getX() - componentsList.indexToLocation(index).x,
                            event.getY() - componentsList.indexToLocation(index).y,
                            event.getClickCount(),
                            event.isPopupTrigger(),
                            event.getButton());

                    componentsList.getCellRenderer().getListCellRendererComponent(
                            componentsList,
                            componentsList.getModel().getElementAt(index),
                            index,
                            true,
                            true).dispatchEvent(newEvent);
                }
            }
        });
        componentsList.getActionMap().put(
                KEYBOARD_TOGGLE_ACTION_NAME,
                new AbstractAction(KEYBOARD_TOGGLE_ACTION_NAME)   {

            @Override
                    public void actionPerformed(ActionEvent event) {
                        ComponentsListModel model =
                                (ComponentsListModel) componentsList.getModel();

                        model.toggleSelection(componentsList.getSelectedIndex());
                    }
                });
        componentsList.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false),
                KEYBOARD_TOGGLE_ACTION_NAME);

        componentsScrollPane = new NbiScrollPane(componentsList);
        componentsScrollPane.setVerticalScrollBarPolicy(
                NbiScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        messageLabel.setLabelFor(componentsScrollPane);

        descriptionPane = new NbiTextPane();
        descriptionPane.setBorder(
                new EmptyBorder(5, 5, 5, 5));

        descriptionScrollPane = new NbiScrollPane(descriptionPane);
        descriptionScrollPane.setVerticalScrollBarPolicy(
                NbiScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        descriptionScrollPane.setBorder(
                new TitledBorder(FEATURE_DESCRIPTION_TITLE));
        descriptionScrollPane.setPreferredSize(new Dimension(200, 10));

        componentPanel = new NbiPanel();
        componentPanel.setLayout(new BorderLayout(6, 0));
        componentPanel.add(componentsScrollPane, BorderLayout.CENTER);
        componentPanel.add(descriptionScrollPane, BorderLayout.EAST);

        sizesLabel = new NbiLabel();
        errorLabel = new NbiLabel();

        okButton = new NbiButton();
        okButton.addActionListener(new ActionListener()   {

            @Override
            public void actionPerformed(ActionEvent event) {
                okButtonPressed();
            }
        });

        cancelButton = new NbiButton();
        cancelButton.addActionListener(new ActionListener()   {

            @Override
            public void actionPerformed(ActionEvent event) {
                cancelButtonPressed();
            }
        });

        buttonsPanel = new NbiPanel();
        buttonsPanel.add(okButton, new GridBagConstraints(
                0, 0, // x, y
                1, 1, // width, height
                1.0, 0.0, // weight-x, weight-y
                GridBagConstraints.LINE_END, // anchor
                GridBagConstraints.NONE, // fill
                new Insets(0, 0, 0, 0), // padding
                0, 0));                           // padx, pady - ???)
        buttonsPanel.add(cancelButton, new GridBagConstraints(
                1, 0, // x, y
                1, 1, // width, height
                0.0, 0.0, // weight-x, weight-y
                GridBagConstraints.CENTER, // anchor
                GridBagConstraints.NONE, // fill
                new Insets(0, 6, 0, 0), // padding
                0, 0));                           // padx, pady - ???)

        getContentPane().add(messageLabel, new GridBagConstraints(
                0, 0, // x, y
                1, 1, // width, height
                1.0, 0.0, // weight-x, weight-y
                GridBagConstraints.LINE_START, // anchor
                GridBagConstraints.BOTH, // fill
                new Insets(11, 11, 0, 11), // padding
                0, 0));                           // padx, pady - ???
        getContentPane().add(componentPanel, new GridBagConstraints(
                0, 1, // x, y
                1, 1, // width, height
                1.0, 1.0, // weight-x, weight-y
                GridBagConstraints.CENTER, // anchor
                GridBagConstraints.BOTH, // fill
                new Insets(6, 11, 0, 11), // padding
                0, 0));                           // padx, pady - ???
        getContentPane().add(sizesLabel, new GridBagConstraints(
                0, 2, // x, y
                1, 1, // width, height
                1.0, 0.0, // weight-x, weight-y
                GridBagConstraints.LINE_START, // anchor
                GridBagConstraints.HORIZONTAL, // fill
                new Insets(6, 11, 0, 11), // padding
                0, 0));                           // padx, pady - ???
        getContentPane().add(errorLabel, new GridBagConstraints(
                0, 3, // x, y
                1, 1, // width, height
                1.0, 0.0, // weight-x, weight-y
                GridBagConstraints.LINE_START, // anchor
                GridBagConstraints.HORIZONTAL, // fill
                new Insets(11, 11, 0, 11), // padding
                0, 0));                           // padx, pady - ???
        getContentPane().add(buttonsPanel, new GridBagConstraints(
                0, 4, // x, y
                1, 1, // width, height
                1.0, 0.0, // weight-x, weight-y
                GridBagConstraints.CENTER, // anchor
                GridBagConstraints.BOTH, // fill
                new Insets(11, 11, 11, 11), // padding
                0, 0));                           // padx, pady - ???

        setTitle(panel.getProperty(WelcomePanel.CUSTOMIZE_TITLE_PROPERTY));
        setModal(true);
        setDefaultCloseOperation(NbiDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()   {

            @Override
            public void windowClosing(WindowEvent e) {
                cancelButtonPressed();
            }
        });
        getRootPane().setDefaultButton(okButton);
        getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true),
                CANCEL_ACTION_NAME);
        getRootPane().getActionMap().put(CANCEL_ACTION_NAME, new AbstractAction()  {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonPressed();
            }
        });

        // l&f-specific tweaks
        if (UIManager.getLookAndFeel().getID().equals("GTK")) {
            descriptionPane.setOpaque(true);
        }
    }

    private void initialize() {
        if (!isThereAnythingVisibleToInstall()) {
            messageLabel.setText(panel.getProperty(WelcomePanel.MESSAGE_UNINSTALL_PROPERTY));
        } else if (!isThereAnythingVisibleToUninstall()) {
            messageLabel.setText(panel.getProperty(WelcomePanel.MESSAGE_INSTALL_PROPERTY));
        } else {
            messageLabel.setText(panel.getProperty(WelcomePanel.MESSAGE_INSTALL_PROPERTY));
        }

        componentsList.setModel(
                new ComponentsListModel(registryNodes));

        descriptionPane.setContentType(
                panel.getProperty(WelcomePanel.COMPONENT_DESCRIPTION_CONTENT_TYPE_PROPERTY));

        if (!isThereAnythingVisibleToInstall()) {
            sizesLabel.setVisible(false);
        }

        updateDescription();
        updateSizes();
        updateErrorMessage();

        okButton.setText(panel.getProperty(WelcomePanel.OK_BUTTON_TEXT_PROPERTY));
        cancelButton.setText(panel.getProperty(WelcomePanel.CANCEL_BUTTON_TEXT_PROPERTY));

        componentsList.setVisibleRowCount(componentsList.getModel().getSize());
        componentsList.getUI().getPreferredSize(componentsList);
        componentsScrollPane.getViewport().setPreferredSize(
                componentsList.getPreferredScrollableViewportSize());
        pack();
        final Dimension currentSize = getSize();
        final Dimension minimumSize = getMinimumSize();
        setPreferredSize(new Dimension(
                (minimumSize.width > currentSize.width
                ? minimumSize.width : currentSize.width),
                (minimumSize.height > currentSize.height
                ? minimumSize.height : currentSize.height)));
        pack();
    }

    private void updateDescription() {
        final RegistryNode node = (RegistryNode) componentsList.getSelectedValue();

        if (node != null) {
            descriptionPane.setText(node.getDescription());
        } else {
            descriptionPane.setText(panel.getProperty(
                    WelcomePanel.DEFAULT_COMPONENT_DESCRIPTION_PROPERTY));
        }

        descriptionPane.setCaretPosition(0);
    }

    private void updateSizes() {
        final Registry registry = Registry.getInstance();

        long installationSize = 0;
        long downloadSize = 0;
        for (Product product : registry.getProductsToInstall()) {
            installationSize += product.getRequiredDiskSpace();
            downloadSize += product.getDownloadSize();
        }

        String template = panel.getProperty(WelcomePanel.SIZES_LABEL_TEXT_NO_DOWNLOAD_PROPERTY);
        for (RegistryNode remoteNode : registry.getNodes(RegistryType.REMOTE)) {
            if (remoteNode.isVisible()) {
                template = panel.getProperty(WelcomePanel.SIZES_LABEL_TEXT_PROPERTY);
            }
        }

        if (installationSize == 0) {
            sizesLabel.setText(StringUtils.format(
                    template,
                    panel.getProperty(WelcomePanel.DEFAULT_INSTALLATION_SIZE_PROPERTY),
                    panel.getProperty(WelcomePanel.DEFAULT_DOWNLOAD_SIZE_PROPERTY)));
        } else {
            sizesLabel.setText(StringUtils.format(
                    template,
                    StringUtils.formatSize(installationSize),
                    StringUtils.formatSize(downloadSize)));
        }
    }

    private String validateInput() {
        final Registry registry = Registry.getInstance();

        final List<Product> toInstall =
                registry.getProducts(Status.TO_BE_INSTALLED);
        final List<Product> toUninstall =
                registry.getProducts(Status.TO_BE_UNINSTALLED);

        if ((toInstall.isEmpty()) && (toUninstall.isEmpty())) {
            if (isThereAnythingVisibleToInstall()
                    && Boolean.getBoolean(Registry.SUGGEST_INSTALL_PROPERTY)) {
               return panel.getProperty(WelcomePanel.ERROR_NO_CHANGES_INSTALL_ONLY_PROPERTY);
            }
            if (isThereAnythingVisibleToUninstall()
                    && Boolean.getBoolean(Registry.SUGGEST_UNINSTALL_PROPERTY)) {
                return panel.getProperty(WelcomePanel.ERROR_NO_CHANGES_UNINSTALL_ONLY_PROPERTY);
            }
            return panel.getProperty(WelcomePanel.ERROR_NO_CHANGES_INSTALL_ONLY_PROPERTY);
        }

        for (Product product : toInstall) {
            for (Dependency requirement : product.getDependencies(Requirement.class)) {
                List<Product> requirees = registry.getProducts(requirement);

                boolean satisfied = false;

                for (Product requiree : requirees) {
                    if ((requiree.getStatus() == Status.TO_BE_INSTALLED)
                            || (requiree.getStatus() == Status.INSTALLED)) {
                        satisfied = true;
                        break;
                    }
                }

                if (!satisfied) {
                    return StringUtils.format(
                            panel.getProperty(WelcomePanel.ERROR_REQUIREMENT_INSTALL_PROPERTY),
                            getProductNameByGroup(product),
                            getProductNameByGroup(requirees.get(0)));
                }
            }

            for (Dependency conflict : product.getDependencies(Conflict.class)) {
                List<Product> conflictees = registry.getProducts(conflict);

                boolean satisfied = true;
                Product unsatisfiedConflict = null;

                for (Product conflictee : conflictees) {
                    if ((conflictee.getStatus() == Status.TO_BE_INSTALLED)
                            || (conflictee.getStatus() == Status.INSTALLED)) {
                        satisfied = false;
                        unsatisfiedConflict = conflictee;
                        break;
                    }
                }

                if (!satisfied) {
                    return StringUtils.format(
                            panel.getProperty(WelcomePanel.ERROR_CONFLICT_INSTALL_PROPERTY),
                            getProductNameByGroup(product),
                            getProductNameByGroup(unsatisfiedConflict));
                }
            }
        }

        for (Product product : toUninstall) {
            for (Product dependent : registry.getProducts()) {
                if ((dependent.getStatus() == Status.TO_BE_UNINSTALLED)
                        || (dependent.getStatus() == Status.NOT_INSTALLED)) {
                    continue;
                }

                for (Dependency requirement : dependent.getDependencies(Requirement.class)) {
                    final List<Product> requirees = registry.getProducts(requirement);

                    if (requirees.contains(product)) {
                        boolean satisfied = false;
                        for (Product requiree : requirees) {
                            if (requiree.getStatus() == Status.INSTALLED) {
                                satisfied = true;
                                break;
                            }
                        }

                        if (!satisfied) {
                            return StringUtils.format(
                                    panel.getProperty(WelcomePanel.ERROR_REQUIREMENT_UNINSTALL_PROPERTY),
                                    product.getDisplayName(),
                                    dependent.getDisplayName());
                        }
                    }
                }
            }
        }

        return null;
    }

    private String getProductNameByGroup(Product product) {
        return product.getDisplayName();
    }

    private void updateErrorMessage() {
        final String errorMessage = validateInput();

        if (errorMessage == null) {
            final String warningMessage = panel.getWarningMessage();
            if (warningMessage != null) {
                errorLabel.setIcon(warningIcon);
                errorLabel.setText(warningMessage);
            } else {
                errorLabel.setIcon(emptyIcon);
                errorLabel.clearText();
            }
            okButton.setEnabled(true);
        } else {
            errorLabel.setIcon(errorIcon);
            errorLabel.setText(errorMessage);
            okButton.setEnabled(false);
        }
    }

    private void okButtonPressed() {
        final String errorMessage = validateInput();

        if (errorMessage != null) {
            ErrorManager.notifyError(errorMessage);
        } else {
            setVisible(false);
            callback.run();
        }
    }

    private void cancelButtonPressed() {
        final ComponentsListModel model =
                (ComponentsListModel) componentsList.getModel();

        model.cancelChanges();
        setVisible(false);
    }

    private boolean isThereAnythingVisibleToInstall() {
        final Registry registry = Registry.getInstance();

        final List<Product> toInstall = new LinkedList<Product>();
        toInstall.addAll(registry.getProducts(Status.NOT_INSTALLED));
        toInstall.addAll(registry.getProducts(Status.TO_BE_INSTALLED));

        for (Product product : toInstall) {
            if (product.isVisible()) {
                return true;
            }
        }

        return false;
    }

    private boolean isThereAnythingVisibleToUninstall() {
        final Registry registry = Registry.getInstance();

        final List<Product> toUninstall = new LinkedList<Product>();
        toUninstall.addAll(registry.getProducts(Status.INSTALLED));
        toUninstall.addAll(registry.getProducts(Status.TO_BE_UNINSTALLED));

        for (Product product : toUninstall) {
            if (product.isVisible()) {
                return true;
            }
        }

        return false;
    }

    public class ComponentsListModel implements ListModel {

        private List<RegistryNode> registryNodes;
        private Map<Product, Status> initialStatuses;
        private final List<ListDataListener> listeners;

        public ComponentsListModel(List<RegistryNode> registryNodes) {
            this.registryNodes = registryNodes;

            initialStatuses = new HashMap<Product, Status>();
            listeners = new LinkedList<ListDataListener>();

            for (RegistryNode node : registryNodes) {
                if (node instanceof Product) {
                    final Product product = (Product) node;

                    initialStatuses.put(product, product.getStatus());
                }
            }
        }

        @Override
        public int getSize() {
            return registryNodes.size();
        }

        @Override
        public Object getElementAt(int index) {
            return registryNodes.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener listener) {
            synchronized (listeners) {
                listeners.add(listener);
            }
        }

        @Override
        public void removeListDataListener(ListDataListener listener) {
            synchronized (listeners) {
                listeners.remove(listener);
            }
        }

        public void toggleSelection(int index) {
            if ((index < 0) || (index >= registryNodes.size())) {
                // just return - do not generate an error as this situation can
                // easily arise under valid circumstances
                return;
            }
            RegistryNode node = registryNodes.get(index);
            if (node instanceof StatusInterface) {
                StatusInterface product = (StatusInterface) node;
                switch (product.getStatus()) {
                    case INSTALLED:
                        break;
                    case TO_BE_UNINSTALLED:
                        break;
                    case NOT_INSTALLED:
                        product.setStatus(Status.TO_BE_INSTALLED);
                        break;
                    case TO_BE_INSTALLED:
                        product.setStatus(Status.NOT_INSTALLED);
                        break;
                }

                fireRowChanged(index);
                updateSizes();
                updateErrorMessage();
            }
        }

        private void fireRowChanged(int index) {
            final ListDataListener[] clone;
            synchronized (listeners) {
                clone = listeners.toArray(new ListDataListener[listeners.size()]);
            }

            final ListDataEvent event = new ListDataEvent(
                    this,
                    ListDataEvent.CONTENTS_CHANGED,
                    index,
                    index);

            for (ListDataListener listener : clone) {
                listener.contentsChanged(event);
            }
        }

        public void cancelChanges() {
            for (RegistryNode node : registryNodes) {
                if (node instanceof Product) {
                    final Product product = (Product) node;

                    product.setStatus(initialStatuses.get(product));
                }
            }
        }
    }

    public class ComponentsListCellRenderer implements ListCellRenderer {

        private List<CellEditorListener> listeners =
                new LinkedList<CellEditorListener>();
        private NbiPanel panel;
        private NbiCheckBox checkBox;
        private NbiLabel titleLabel;
        private int currentIndex = -1;

        public ComponentsListCellRenderer() {
            initComponents();
        }

        @Override
        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean selected,
                boolean focus) {
            currentIndex = index;

            if (selected) {
                titleLabel.setOpaque(true);

                titleLabel.setForeground(list.getSelectionForeground());
                titleLabel.setBackground(list.getSelectionBackground());
            } else {
                titleLabel.setOpaque(false);

                titleLabel.setForeground(list.getForeground());
                titleLabel.setBackground(list.getBackground());
            }

            if (value instanceof RegistryNode) {
                final RegistryNode node = (RegistryNode) value;

                final String title =
                        " " + node.getDisplayName() + " ";
                final String tooltip = title;
                titleLabel.setText(title);
                titleLabel.setToolTipText(tooltip);

                if (node instanceof Product) {
                    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.PLAIN));
                    checkBox.setVisible(true);
                    checkBox.setToolTipText(tooltip);
                    switch (((StatusInterface) value).getStatus()) {
                        case INSTALLED:
                            titleLabel.setText(StringUtils.format(
                                    LIST_INSTALLED_PRODUCT_TEXT, titleLabel.getText()));
                            checkBox.setSelected(true);
                            checkBox.setEnabled(false);
                            break;
                        case TO_BE_INSTALLED:
                            checkBox.setSelected(true);
                            checkBox.setEnabled(true);
                            break;
                        default:
                            checkBox.setSelected(false);
                            checkBox.setEnabled(true);
                    }
                } else if (node instanceof Group) {
                    titleLabel.setText(title);
                    titleLabel.setToolTipText(tooltip);
                    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
                    checkBox.setVisible(false);
                }
            }

            titleLabel.setPreferredSize(titleLabel.getMinimumSize());

            // l&f-specific tweaks
            if (UIManager.getLookAndFeel().getID().equals("GTK")) {
                panel.setOpaque(false);
            }

            return panel;
        }

        private void initComponents() {

            panel = new NbiPanel();
            panel.setLayout(new GridBagLayout());
            panel.setOpaque(false);
            panel.addMouseListener(new MouseAdapter()  {

                @Override
                public void mouseReleased(MouseEvent event) {
                    if (checkBox.isVisible()
                            && checkBox.getBounds().contains(event.getPoint())) {
                        ComponentsListModel model =
                                (ComponentsListModel) componentsList.getModel();

                        model.toggleSelection(currentIndex);
                    }
                }
            });

            checkBox = new NbiCheckBox();
            checkBox.setOpaque(false);

            final Dimension preferredSize = checkBox.getPreferredSize();
            preferredSize.height = preferredSize.height - 2;
            preferredSize.width = preferredSize.width - 2;

            checkBox.setPreferredSize(preferredSize);

            titleLabel = new NbiLabel();
            titleLabel.setFocusable(false);
            titleLabel.setPreferredSize(preferredSize);

            panel.add(checkBox, new GridBagConstraints(
                    0, 0, // x, y
                    1, 1, // width, height
                    0.0, 0.0, // weight-x, weight-y
                    GridBagConstraints.CENTER, // anchor
                    GridBagConstraints.BOTH, // fill
                    new Insets(0, 0, 0, 0), // padding
                    0, 0));                           // padx, pady - ???);
            panel.add(titleLabel, new GridBagConstraints(
                    1, 0, // x, y
                    1, 1, // width, height
                    1.0, 0.0, // weight-x, weight-y
                    GridBagConstraints.LINE_START, // anchor
                    GridBagConstraints.HORIZONTAL, // fill
                    new Insets(0, 0, 0, 0), // padding
                    0, 0));                           // padx, pady - ???);
        }
    }
    public static final String KEYBOARD_TOGGLE_ACTION_NAME =
            "checkbox.update"; // NOI18N
    public static final String FEATURE_DESCRIPTION_TITLE =
            ResourceUtils.getString(CustomizeSelectionDialog.class,
            "CSD.feature.description.title"); // NOI18N
    public static final String LIST_INSTALLED_PRODUCT_TEXT =
            ResourceUtils.getString(CustomizeSelectionDialog.class,
            "CSD.list.product.installed");//NOI18N
    private static final String CANCEL_ACTION_NAME =
            "evaluate.cancel"; // NOI18N
    private static final int EXTRA_SIZE = 15;
}
