package com.artigile.warehouse.gui.baselayout;

import com.artigile.warehouse.domain.MenuItem;
import org.openide.explorer.view.TreeView;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

import java.util.*;

public class BusinessOperationChildren extends Children.Keys<List<MenuItem>> {

    private List<MenuItem> folders;
    private int level;
    private TreeView view;

    public BusinessOperationChildren(List<MenuItem> folders, TreeView view) {
        this(folders, 0, view);
    }

    public BusinessOperationChildren(List<MenuItem> folders, int level, TreeView view) {
        super(false);
        this.folders = folders;
        this.level = level;
        this.view = view;
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        if (folders != null) {
            setKeys(Collections.singletonList(folders));
        }
    }

    @Override
    protected Node[] createNodes(List<MenuItem> key) {
        List<Item> nodeItems = buildItemList(key);
        List<Node> items = new ArrayList<Node>();

        for (Item item : nodeItems) {
            List<MenuItem> list = item.getItems();
            if (item.isLeaf()) {
                items.add(new BusinessOperationNode(item.getName(), list.get(0), view));
            } else {
                items.add(new BusinessOperationNode(item.getName(), list, level + 1, view));
            }
        }

        return items.toArray(new Node[items.size()]);
    }

    private List<Item> buildItemList(List<MenuItem> key) {
        java.util.Map<String, Integer> nameToIndex = new HashMap<String, Integer>();
        List<Item> nodeItems = new ArrayList<Item>();
        int index = 0;
        for (MenuItem menuItem : key) {
            StringTokenizer tokenizer = new StringTokenizer(menuItem.getName(), "\\");
            int cnt = tokenizer.countTokens();
            String nodeName = getNodeName(tokenizer, level);
            if (nodeName != null) {
                if (!nameToIndex.containsKey(nodeName)) {
                    nameToIndex.put(nodeName, index++);
                    nodeItems.add(new Item(nodeName, level+1 >= cnt));
                }
                Item cur = nodeItems.get(nameToIndex.get(nodeName));
                cur.getItems().add(menuItem);
            }
        }

        return nodeItems;
    }

    private String getNodeName(StringTokenizer tokenizer, int level) {
        if (tokenizer.countTokens() > level) {
            String curNodeName = null;
            for (int i = level; i >= 0; --i) {
                curNodeName = tokenizer.nextToken();
            }
            return curNodeName;
        }

        return null;
    }

    private static class Item {

        private String name;
        private List<MenuItem> items;
        private boolean isLeaf;

        public Item(String name, boolean isLeaf) {
            this.name = name;
            this.isLeaf = isLeaf;
            this.items = new ArrayList<MenuItem>();
        }

        public List<MenuItem> getItems() {
            return items;
        }

        public String getName() {
            return name;
        }

        public boolean isLeaf() {
            return isLeaf;
        }
    }
}
