package com.artigile.warehouse.gui.baselayout.explorer.view;

import com.artigile.warehouse.domain.MenuItem;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;
import org.openide.LifecycleManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Valery Barysok, 2013-07-29
 */
public class ButtonListView extends JScrollPane {

    static final Logger LOG = Logger.getLogger(ButtonListView.class.getName());

    static final long serialVersionUID = 1L;

    transient protected JPanel panel;

    public ButtonListView() {
        initializeList();

        setBorder(BorderFactory.createEmptyBorder());
        setViewportBorder(BorderFactory.createEmptyBorder());
    }

    private void initializeList() {
        panel = createPanel();
        setViewportView(panel);
        revalidate();
    }

    protected JPanel createPanel() {
        JXPanel panel = new JXPanel();
        panel.setLayout(new VerticalLayout());
        return panel;
    }

    @Override
    public void requestFocus() {
        panel.requestFocus();
    }

    @Override
    public boolean requestFocusInWindow() {
        return panel.requestFocusInWindow();
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        panel.removeAll();
        for (final MenuItem menuItem : menuItems) {
            String name = menuItem.getName();
            int pos = name.lastIndexOf('\\');
            if (pos != -1 && pos + 1 < name.length()) {
                name = name.substring(pos + 1);
            }
            JXButton button = new JXButton(name);
            button.setPreferredSize(new Dimension(-1, 50));
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    WareHouse.open(menuItem);
                }
            });
            panel.add(button);
        }

        JXButton button = new JXButton(I18nSupport.message("menutree.exit"));
        button.setPreferredSize(new Dimension(-1, 50));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LifecycleManager.getDefault().exit();
            }
        });
        panel.add(button);

        panel.revalidate();
    }
}
