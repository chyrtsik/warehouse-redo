/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.view;

import com.artigile.swingx.filter.gui.FilterHeaderManager;
import com.artigile.swingx.filter.gui.editor.TextFilterEditor;
import com.artigile.swingx.workarounds.FilterPipelineEx;
import com.artigile.swingx.workarounds.FilterUtils;
import com.artigile.swingx.workarounds.JXTableEx;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.core.print.PrintFacade;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.choose.choosedialog.sort.SortChooseForm;
import com.artigile.warehouse.gui.core.properties.dialogs.choose.choosedialog.sort.SortItem;
import com.artigile.warehouse.gui.core.report.command.ReportCommand;
import com.artigile.warehouse.gui.core.report.controller.*;
import com.artigile.warehouse.gui.core.report.format.ColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.model.ReportModel;
import com.artigile.warehouse.gui.core.report.model.TableReportModel;
import com.artigile.warehouse.gui.core.report.style.Alignment;
import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.gui.core.report.tooltip.ColumnTooltipFactory;
import com.artigile.warehouse.gui.core.report.view.cell.ReportCellEditor;
import com.artigile.warehouse.gui.core.report.view.cell.ReportCellRenderer;
import com.artigile.warehouse.gui.core.report.view.datatransfer.ReportTransferHandler;
import com.artigile.warehouse.utils.dto.userprofile.ReportStateTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.table.TableColumnExt;

import javax.print.PrintException;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * @author IoaN, 28.11.2008
 */
public class TableReportView implements ReportView {
    private JPanel mainTableViewPanel;
    private JXTableEx table;
    private JToggleButton filter;
    private JButton sort;
    private JButton refresh;
    private JScrollPane scrollPane;
    private JToolBar toolbarPane;
    private JButton customCommand;
    private JButton print;
    private JButton printPreview;
    private JToolBar.Separator printButtonsSpacer;
    private EventListenerList listeners = new EventListenerList();

    private TableReportModel tableReportModel;
    private ReportController reportController;
    private FilterHeaderManager filterHeaderManager = new FilterHeaderManager();

    private HashMap<PredefinedCommand, AbstractButton> commandToButton = new HashMap<PredefinedCommand, AbstractButton>();
    private Set<ReportSelectionListener> selectionListeners = new HashSet<ReportSelectionListener>();

    private void createUIComponents() {
        table = new JXCustomTable();
    }

    /**
     * Constructor. Used to create a table, based on programmer-defined model.
     *
     * @param tableReportModel - model, provided for the table.
     */
    public TableReportView(TableReportModel tableReportModel) {
        this.tableReportModel = tableReportModel;
        $$$setupUI$$$();
        initCommandMapping();
        initTable();
        initTableCells();
        initListeners();
    }

    private void initCommandMapping() {
        commandToButton.put(PredefinedCommand.REFRESH, refresh);
        commandToButton.put(PredefinedCommand.SORT, sort);
        commandToButton.put(PredefinedCommand.FILTER, filter);
        commandToButton.put(PredefinedCommand.PRINT, print);
        commandToButton.put(PredefinedCommand.PRINT_PREVIEW, printPreview);
    }

    public void setReportController(ReportController reportController) {
        this.reportController = reportController;
    }

    public JPanel getView() {
        return mainTableViewPanel;
    }

    public JXTableEx getTable() {
        return table;
    }

    public void addRefreshListener(TableReportViewListener l) {
        listeners.add(TableReportViewListener.class, l);
    }

    public void removeRefreshListener(TableReportViewListener l) {
        listeners.remove(TableReportViewListener.class, l);
    }

    public void fireRefresh() {
        EventObject event = new EventObject(this);
        TableReportViewListener[] listenerList = listeners.getListeners(TableReportViewListener.class);
        for (TableReportViewListener listener : listenerList) {
            listener.refresh(event);
        }
    }

    private void fireSelectionChanged() {
        for (ReportSelectionListener listener : selectionListeners) {
            listener.onSelectionChanged();
        }
    }

    @Override
    public List getSelectedItems() {
        if (table.getSelectedRow() != -1) {
            ReportModel model = (ReportModel) table.getModel();
            if (model != null) {
                List<Object> selectedItems = new ArrayList<Object>();
                for (int selectedRow : table.getSelectedRows()) {
                    int modelRow = table.convertRowIndexToModel(selectedRow);
                    if (modelRow >= 0 && modelRow < model.getItemCount()) {
                        selectedItems.add(model.getItem(modelRow));
                    }
                }
                return selectedItems;
            }
        }
        return null;
    }

    @Override
    public List getDisplayedItems() {
        ReportModel model = (ReportModel) table.getModel();
        FilterPipeline filters = table.getFilters();
        int viewItemsCount = filters.getOutputSize();
        List<Object> items = new ArrayList<Object>(viewItemsCount);
        for (int viewRow = 0; viewRow < viewItemsCount; viewRow++) {
            int modelRow = filters.convertRowIndexToModel(viewRow);
            items.add(model.getItem(modelRow));
        }
        return items;
    }

    @Override
    public Component getViewComponent() {
        return table;
    }

    @Override
    public JPanel getContentPanel() {
        return mainTableViewPanel;
    }

    @Override
    public void addSelectionListener(ReportSelectionListener listener) {
        selectionListeners.add(listener);
    }

    @Override
    public void removeSelectionListener(ReportSelectionListener listener) {
        selectionListeners.remove(listener);
    }

    @Override
    public int getSelectedColumn() {
        if (table.getSelectedColumnCount() != 1) {
            return -1;
        }
        return table.convertColumnIndexToModel(table.getSelectedColumn());
    }

    public FilterHeaderManager getFilterHeaderManager() {
        return filterHeaderManager;
    }

    /**
     * Returns filter text for column with index {@code index}.
     *
     * @param index column index.
     * @return filter text for column with index {@code index}.
     */
    public String getFilteredText(int index) {
        return ((TextFilterEditor) filterHeaderManager.getFilteredColumns().get(index).getFilterEditor()).getText();
    }

    /**
     * Sets the filtered text to the column with index = {@code index}
     *
     * @param index        column index.
     * @param filteredText text for column with index {@code index}.
     */
    public void setFilteredText(int index, String filteredText) {
        ((TextFilterEditor) filterHeaderManager.getFilteredColumns().get(index).getFilterEditor()).setText(filteredText);
    }

    /**
     * Used for retrieving style (custom style decoration) for given table cell.
     *
     * @param row    row of table cell.
     * @param column column of table cell.
     * @return style for the table cell specified.
     */
    public Style getCellStyle(int row, int column) {
        int modelColumn = table.convertColumnIndexToModel(column);
        StyleFactory styleFactory = tableReportModel.getReportDataSource().getReportInfo().getColumnStyleFactory(modelColumn);
        FilterPipeline filters = table.getFilters();
        return styleFactory.getStyle(tableReportModel.getItem(filters.convertRowIndexToModel(row)));
    }

    /**
     * Initializes table cells.
     */
    private void initTableCells() {
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            // Custom rendering and editing of table cells.
            ColumnInfo columnInfo = tableReportModel.getReportDataSource().getReportInfo().getColumns().get(i);
            ColumnFormatFactory cellFormatFactory = columnInfo.getColumnFormatFactory();
            ColumnTooltipFactory cellTooltipFactory = columnInfo.getColumnTooltipFactory();

            table.getColumn(i).setCellRenderer(new ReportCellRenderer(cellFormatFactory, cellTooltipFactory));
            table.getColumn(i).setCellEditor(new ReportCellEditor(new JTextField()));

            // Custom comparator of values
            if (columnInfo.getSortingComparator() != null) {
                TableColumnExt columnExt = table.getColumnExt(i);
                if (columnExt.isSortable()) {
                    columnExt.setComparator(columnInfo.getSortingComparator());
                }
            }
        }
    }

    /**
     * Initializes table.
     */
    private void initTable() {
        table.updateUI();
        table.setModel(tableReportModel);
        table.setFilters(new FilterPipelineEx());
        table.setRowSelectionAllowed(true);

        //Sorting initialization.
        ReportOptions reportOptions = getReportOptions();
        table.setSortable(reportOptions.isSortEnabled());

        //Drag and drop initialization.
        table.setDragEnabled(reportOptions.isDragRowsEnabled());
        table.setDropMode(DropMode.INSERT_ROWS);
        table.setTransferHandler(new ReportTransferHandler(tableReportModel, this, JTableDropLocationTranslator.getInstance()));

        //Toolbar initialization.
        toolbarPane.setVisible(reportOptions.isShowToolbar());
        initPredefinedToolbarCommands();
        initCustomToolbarCommand(reportOptions.getCustomToolbarCommand());

        //Row number column initialization.
        //TODO: Disabled until proper row number column will be implemented.
        //JXCustomTable rowNumberTable = new JXCustomTable();
        //rowNumberTable.setSortable(false);
        //new JRowNumberTable(table, rowNumberTable, scrollPane);
    }

    private void initPredefinedToolbarCommands() {
        //Remove unused commands from the toolbar.
        Set<PredefinedCommand> commands = getReportOptions().getPredefinedCommands();
        for (Map.Entry<PredefinedCommand, AbstractButton> entry : commandToButton.entrySet()) {
            if (!commands.contains(entry.getKey())) {
                toolbarPane.remove(entry.getValue());
            }
        }

        if (!commands.contains(PredefinedCommand.PRINT) && !commands.contains(PredefinedCommand.PRINT_PREVIEW)) {
            //Remove extra toolbar separator.
            toolbarPane.remove(printButtonsSpacer);
        }
    }

    private void initCustomToolbarCommand(ReportCommand customToolbarCommand) {
        if (customToolbarCommand != null) {
            //Custom command is presented.
            customCommand.setVisible(true);
            customCommand.setText(customToolbarCommand.getName(reportController));
            customCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    reportController.onCommand(getReportOptions().getCustomToolbarCommand());
                }
            });
        } else {
            //There is no custom commands.
            customCommand.setVisible(false);
        }
    }

    private ReportOptions getReportOptions() {
        return tableReportModel.getReportDataSource().getReportInfo().getOptions();
    }

    /**
     * Initializes component listeners.
     */
    private void initListeners() {
        tableReportModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.INSERT && e.getColumn() == TableModelEvent.ALL_COLUMNS) {
                    //Table rows has been inserted. So, we should select new rows.
                    table.clearSelection();
                    for (int modelRow = e.getFirstRow(); modelRow <= e.getLastRow(); modelRow++) {
                        int viewRow = table.convertRowIndexToView(modelRow);
                        if (viewRow != -1) {
                            table.addRowSelectionInterval(viewRow, viewRow);
                        }
                    }

                    //Scroll table to make selection visible.
                    int firstViewRow = table.convertRowIndexToView(e.getFirstRow());
                    int lastViewRow = table.convertRowIndexToView(e.getLastRow());
                    table.scrollRowToVisible(lastViewRow);
                    if (firstViewRow != lastViewRow && firstViewRow != -1) {
                        table.scrollRowToVisible(firstViewRow);
                    }
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            //Needs to be registered with main table to enable mouse right click row selection.
            public void mousePressed(MouseEvent e) {
                updateTableSelectionOnMouseEvent(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                updateTableSelectionOnMouseEvent(e);
            }

            private void updateTableSelectionOnMouseEvent(MouseEvent e) {
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                int col = table.columnAtPoint(p);

                int selectedRows[] = table.getSelectedRows();
                int selectedCols[] = table.getSelectedColumns();
                for (int selectedRow : selectedRows) {
                    if (selectedRow == row) {
                        for (int selectedCol : selectedCols) {
                            if (selectedCol == col) {
                                //When item under mouse is in selection range, selection need not to be changed.
                                return;
                            }
                        }
                    }
                }

                if ((col == -1) || (row == -1)) {
                    //Mouse was pressed not over table item. So, there is nothing to select.
                    table.clearSelection();
                    return;
                }

                if (SwingUtilities.isRightMouseButton(e)) {
                    table.setRowSelectionInterval(row, row);
                    table.setColumnSelectionInterval(col, col);
                    table.invalidate();
                }
            }
        });

        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F5) {
                    fireRefresh();
                }
            }
        });

        table.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                table.setSelectionBackground(new Color(202, 219, 248));
            }

            @Override
            public void focusLost(FocusEvent e) {
                table.setSelectionBackground(Color.lightGray);
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                fireSelectionChanged();
            }
        });
        table.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                fireSelectionChanged();
            }
        });

        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRefreshDataList();
            }
        });

        sort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSortDataList();
            }
        });

        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onFilterShowHide();
            }
        });

        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPrint();
            }
        });

        printPreview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPrintPreview();
            }
        });
    }

    private void onPrint() {
        //Perform print (with printer choosing).
        ReportPrintProvider printProvider = getReportOptions().getPrintProvider();
        PrintTemplateType[] printTemplates = printProvider.getReportPrintTemplates();
        Object printableObject = printProvider.getReportDataForPrinting();
        try {
            PrintFacade.print(printableObject, printTemplates);
        } catch (PrintException e) {
            LoggingFacade.logError(this, e);
            MessageDialogs.showWarning(e.getLocalizedMessage());
        }
    }

    private void onPrintPreview() {
        //Perform print preview.
        ReportPrintProvider printProvider = getReportOptions().getPrintProvider();
        PrintTemplateType[] printTemplates = printProvider.getReportPrintTemplates();
        Object printableObject = printProvider.getReportDataForPrinting();
        try {
            PrintFacade.printPreview(printableObject, printTemplates);
        } catch (PrintException e) {
            LoggingFacade.logError(this, e);
            MessageDialogs.showWarning(e.getLocalizedMessage());
        }
    }

    private void onRefreshDataList() {
        fireRefresh();
    }

    public void resetFilter() {
        filterHeaderManager.resetFilter();
    }

    public void toggleFilter() {
        // TODO: think about proper solution
        filter.doClick();
    }

    private void onFilterShowHide() {
        // Toggle filter header
        filterHeaderManager.setVisible(!filterHeaderManager.getVisible());
        updateFilterHeaderManager();
    }

    private void updateFilterHeaderManager() {
        if (filterHeaderManager.getVisible()) {
            ReportDataSource dataSource = tableReportModel.getReportDataSource();
            List<ReportStateTO> filteredList = TableHeaderMenuManager.getDestinationList(this, dataSource);
            if (!filteredList.isEmpty()) {
                ReportStateTO reportState = filteredList.get(0);
                List<TableHeaderMenuManager.FilterHelper> visibleColumns = TableHeaderMenuManager.buildVisibleColumns(reportState, dataSource.getReportInfo());
                int i = 0;
                for (TableHeaderMenuManager.FilterHelper helper : visibleColumns) {
                    setFilteredText(i++, helper.getFilterText());
                }
            }
        } else {
            TableHeaderMenuManager.storeReportState(this, tableReportModel.getReportDataSource());
        }
    }


    //================================= Sorting ============================================

    private List<SortItem> buildSourceSortList() {
        List<SortItem> list = new ArrayList<SortItem>();
        TableModel tm = table.getModel();
        for (int i = 0; i < tm.getColumnCount(); ++i) {
            int viewIndex = table.convertColumnIndexToView(i);
            if (viewIndex != -1) {
                list.add(new SortItem(tm.getColumnName(i), i));
            }
        }
        return list;
    }

    private List<SortItem> buildDestinationSortList() {
        return new ArrayList<SortItem>();
    }

    private void onSortDataList() {
        List<SortItem> sourceList = buildSourceSortList();
        List<SortItem> destinationList = buildDestinationSortList();
        if (Dialogs.runProperties(new SortChooseForm(I18nSupport.message("sort.configure"), sourceList, destinationList))) {
            FilterUtils.sortByColumns(table, destinationList);
        }
    }

    /**
     * Sorts report data by given field name.
     *
     * @param fieldName column field name
     * @param sortOrder specified how to sort by given column field ({@link javax.swing.SortOrder})
     */
    public void sortByReportField(String fieldName, SortOrder sortOrder) {
        List<ColumnInfo> columnsInfo = tableReportModel.getReportDataSource().getReportInfo().getColumns();
        for (int i = 0; i < columnsInfo.size(); ++i) {
            ColumnInfo columnInfo = columnsInfo.get(i);
            if (columnInfo.getField().equals(fieldName)) {
                SortItem sortItem = new SortItem(fieldName, i);
                sortItem.setSortOrder(sortOrder);
                FilterUtils.sortByColumns(table, Collections.singletonList(sortItem));
                return;
            }
        }
        throw new IllegalArgumentException("TableReportView.sortByReportField: fieldName is invalid name of table model field.");
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainTableViewPanel = new JPanel();
        mainTableViewPanel.setLayout(new FormLayout("fill:d:grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow"));
        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(30);
        CellConstraints cc = new CellConstraints();
        mainTableViewPanel.add(scrollPane, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
        table.setAutoResizeMode(0);
        table.setCellSelectionEnabled(true);
        table.setColumnControlVisible(false);
        table.setDoubleBuffered(true);
        table.setFillsViewportHeight(true);
        table.setHorizontalScrollEnabled(false);
        table.setInheritsPopupMenu(true);
        table.setPreferredScrollableViewportSize(new Dimension(150, 100));
        table.setShowVerticalLines(true);
        table.setSurrendersFocusOnKeystroke(true);
        table.setVerifyInputWhenFocusTarget(false);
        table.putClientProperty("terminateEditOnFocusLost", Boolean.FALSE);
        scrollPane.setViewportView(table);
        toolbarPane = new JToolBar();
        toolbarPane.setFloatable(false);
        mainTableViewPanel.add(toolbarPane, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        refresh = new JButton();
        refresh.setEnabled(true);
        refresh.setIcon(new ImageIcon(getClass().getResource("/images/refresh.png")));
        refresh.setText("");
        refresh.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("table.toolbar.refresh"));
        toolbarPane.add(refresh);
        sort = new JButton();
        sort.setIcon(new ImageIcon(getClass().getResource("/images/sort_options.png")));
        sort.setText("");
        sort.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("table.toolbar.sort"));
        toolbarPane.add(sort);
        filter = new JToggleButton();
        filter.setIcon(new ImageIcon(getClass().getResource("/images/filter.png")));
        filter.setText("");
        filter.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("table.toolbar.filter"));
        toolbarPane.add(filter);
        printButtonsSpacer = new JToolBar.Separator();
        toolbarPane.add(printButtonsSpacer);
        print = new JButton();
        print.setIcon(new ImageIcon(getClass().getResource("/images/print.png")));
        print.setSelected(false);
        print.setText("");
        print.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("table.toolbar.print"));
        toolbarPane.add(print);
        printPreview = new JButton();
        printPreview.setIcon(new ImageIcon(getClass().getResource("/images/printPreview.png")));
        printPreview.setText("");
        printPreview.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("table.toolbar.printPreview"));
        toolbarPane.add(printPreview);
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        toolbarPane.add(toolBar$Separator1);
        final Spacer spacer1 = new Spacer();
        toolbarPane.add(spacer1);
        customCommand = new JButton();
        customCommand.setText("<Custom Command>");
        toolbarPane.add(customCommand);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainTableViewPanel;
    }

    //Subclass for table is used to avoid strange behavior of cell renderer, when it is
    //impossible to show different cells of table with different styles.
    private class JXCustomTable extends JXTableEx {

        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            // Decorating cell with custom styles.
            Component component = super.prepareRenderer(renderer, row, column);
            Style style = getCellStyle(row, column);

            // Apply style if it defined
            if (style != null) {
                if (!isCellSelected(row, column)) {
                    component.setBackground(style.getBackground());
                }
                component.setForeground(style.getForeground());

                // Content alignment in the cell
                Alignment hContentAlign = style.getHorizontalContentAlign();
                if (hContentAlign != null) {
                    ((ReportCellRenderer) component).setHorizontalAlignment(hContentAlign.getAlignmentType());
                }
                Alignment vContentAlign = style.getVerticalContentAlign();
                if (vContentAlign != null) {
                    ((ReportCellRenderer) component).setVerticalAlignment(vContentAlign.getAlignmentType());
                }
            } else {
                //Just remove all standard colors inversion as they are unreadable.
                component.setForeground(null);
            }

            return component;
        }
    }
}
