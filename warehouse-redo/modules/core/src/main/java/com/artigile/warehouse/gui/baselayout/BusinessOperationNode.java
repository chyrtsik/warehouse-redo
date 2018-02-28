package com.artigile.warehouse.gui.baselayout;

import com.artigile.warehouse.domain.MenuItem;
import org.openide.actions.OpenAction;
import org.openide.cookies.OpenCookie;
import org.openide.explorer.view.TreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import javax.swing.*;
import java.awt.*;
import java.beans.BeanInfo;
import java.util.List;

public class BusinessOperationNode extends AbstractNode implements OpenCookie {

    private MenuItem item;
    private OpenAction openAction = SystemAction.get(OpenAction.class);
    private TreeView view;

    public BusinessOperationNode(String displayName, MenuItem item, TreeView view) {
        this(displayName, item, new InstanceContent(), view);
    }

    public BusinessOperationNode(String displayName, MenuItem item, InstanceContent content, TreeView view) {
        super(Children.LEAF, new AbstractLookup(content));
        content.add(this);
        setDisplayName(displayName);
        this.item = item;
        this.view = view;

    }

    public BusinessOperationNode(String displayName, List<MenuItem> folders, int level, TreeView view) {
        this(displayName, folders, level, new InstanceContent(), view);
    }

    public BusinessOperationNode(String displayName, List<MenuItem> folders, int level, InstanceContent content, TreeView view) {
        super(new BusinessOperationChildren(folders, level, view), new AbstractLookup(content));
        content.add(this);
        setDisplayName(displayName);
        this.view = view;
    }

    public MenuItem getItem() {
        return item;
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{openAction.createContextAwareInstance(getLookup())};
    }

    @Override
    public Action getPreferredAction() {
        return openAction.createContextAwareInstance(getLookup());
    }

    @Override
    public void open() {
        if (item != null) {
            WareHouse.open(item);
        } else if (!isLeaf()) {
            if (view.isExpanded(this)) {
                view.collapseNode(this);
            } else {
                view.expandNode(this);
            }
        }
    }

    @Override
    public Image getIcon(int type) {
        Image result = null;
        if (isLeaf()) {
            result = ImageUtilities.loadImage("images/task.png");
        }
        if (result == null && type == BeanInfo.ICON_COLOR_16x16) {
            result = icon2Image("Nb.Explorer.Folder.icon"); // NOI18N
        }
        if (result == null) {
            result = icon2Image("Tree.closedIcon"); // NOI18N
        }
        if (result == null) {
            result = super.getIcon(type);
        }
        return result;
    }

    @Override
    public Image getOpenedIcon(int type) {
        Image result = null;
        if (isLeaf()) {
            result = ImageUtilities.loadImage("images/task.png");
        }
        if (result == null && type == BeanInfo.ICON_COLOR_16x16) {
            result = icon2Image("Nb.Explorer.Folder.openedIcon"); // NOI18N
        }
        if (result == null) {
            result = icon2Image("Tree.openIcon"); // NOI18N
        }
        if (result == null) {
            result = super.getOpenedIcon(type);
        }
        return result;
    }

    private static Image icon2Image(String key) {
        Object obj = UIManager.get(key);
        if (obj instanceof Image) {
            return (Image) obj;
        }

        if (obj instanceof Icon) {
            Icon icon = (Icon) obj;
            return ImageUtilities.icon2Image(icon);
        }

        return null;
    }
}
