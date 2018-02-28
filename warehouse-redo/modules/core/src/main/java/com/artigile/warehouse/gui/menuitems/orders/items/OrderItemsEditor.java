/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.orders.items;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.listeners.DataChangeAdapter;
import com.artigile.warehouse.bl.common.listeners.DataChangeListener;
import com.artigile.warehouse.bl.orders.OrderService;
import com.artigile.warehouse.domain.orders.OrderState;
import com.artigile.warehouse.gui.core.plugin.FramePlugin;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.core.report.decorator.ReportCommandsDecorator;
import com.artigile.warehouse.gui.core.switchable.SwitchableView;
import com.artigile.warehouse.gui.core.switchable.SwitchableViewItem;
import com.artigile.warehouse.gui.menuitems.details.batches.DetailBatchesList;
import com.artigile.warehouse.gui.menuitems.details.catalog.DetailCatalog;
import com.artigile.warehouse.gui.menuitems.details.catalog.DetailCatalogBatchesListFactory;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.properties.Properties;
import com.artigile.warehouse.utils.properties.savers.SplitPaneOrietationSaver;
import com.artigile.warehouse.utils.properties.savers.SplitPaneSaver;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 08.01.2009
 */
public class OrderItemsEditor extends FramePlugin {
    JPanel contentPanel;
    private JPanel detailBatchesPanel;
    private JPanel orderItemsPanel;
    private JTextField orderState;
    private JTextField orderNumber;
    private JTextField orderCreateDate;
    private JTextField orderContractor;
    private JTextField orderContractorBalance;
    private JLabel orderContractorBalanceCurrency;
    private JTextField orderTotalPrice;
    private JLabel orderCurrency;
    private JButton toCollection;
    private JButton toConstruction;
    private JPanel orderPanel;
    private JSplitPane splitPane;
    private JTextField orderVatRate;
    private JTextField orderVat;
    private JTextField orderTotalPriceWithVat;
    private JLabel orderCurrencyVat;
    private JLabel orderCurrencyTotalWithVat;

    private OrderTOForReport order;
    private SwitchableView detailBatchesView;

    private static final String PROPERTY_DETAIL_BATCHES_VIEW_INDEX = "OrderItemsEditor.DetailBatchesView.SelectedIndex";

    public OrderItemsEditor(long orderId) {
        OrderService ordersService = SpringServiceContext.getInstance().getOrdersService();
        order = ordersService.getOrder(orderId);

        initListeners();
        refreshControls();
        initDetailBatchesList();
        initOrderItemsList();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("order.items.editor.title", order.getNumber());
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    private final DataChangeListener orderChangeListener = new DataChangeAdapter() {
        @Override
        public void afterChange(Object changedData) {
            OrderTOForReport changedOrder = (OrderTOForReport) changedData;
            if (order.getId() == changedOrder.getId()) {
                order = changedOrder;
                refreshControls();
            }
        }
    };

    @Override
    protected void onFrameOpened() {
        super.onFrameOpened();

        //We should react on changes of order to keep editor in actual state.
        SpringServiceContext.getInstance().getDataChangeNolifier().addDataChangeListener(OrderTOForReport.class, orderChangeListener);
        SplitPaneSaver.restore(splitPane, getFrameId());
        SplitPaneOrietationSaver.restore(splitPane, OrderItemsEditor.class);
    }

    @Override
    protected void onFrameClosed() {
        SplitPaneSaver.store(splitPane, getFrameId());
        if (detailBatchesView != null) {
            Properties.setProperty(PROPERTY_DETAIL_BATCHES_VIEW_INDEX, String.valueOf(detailBatchesView.getSelectedViewIndex()));
        }
        super.onFrameClosed();

        //We should not forget to unregister order changes listener to prevent memory leaks.
        SpringServiceContext.getInstance().getDataChangeNolifier().removeDataChangeListener(OrderTOForReport.class, orderChangeListener);
    }

    private int loadDetailBatchViewIndex() {
        //Restores previously stored detail bathes view index (to use the same view on each new order edition).
        Integer storedViewIndex = Properties.getPropertyAsInteger(PROPERTY_DETAIL_BATCHES_VIEW_INDEX);
        return storedViewIndex == null ? -1 : storedViewIndex;
    }

    //========================================= Helpers ===========================================

    private void initListeners() {
        toCollection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SpringServiceContext.getInstance().getOrdersService().makeOrderReadyForComplecting(order.getId());
                } catch (BusinessException ex) {
                    MessageDialogs.showWarning(contentPanel, ex.getMessage());
                    return;
                }
                initOrderProperties();
                initOrderCommands();
            }
        });

        toConstruction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SpringServiceContext.getInstance().getOrdersService().returnOrderToConstruction(order.getId());
                } catch (BusinessException ex) {
                    MessageDialogs.showWarning(contentPanel, ex.getMessage());
                    return;
                }
                initOrderProperties();
                initOrderCommands();
            }
        });
    }

    private void refreshControls() {
        //Init user interface of movement editor according to state of the movement.
        initOrderProperties();
        initOrderCommands();
        orderItemsPanel.revalidate();
    }

    private void initOrderProperties() {
        orderState.setText(order.getState().getName());
        orderNumber.setText(String.valueOf(order.getNumber()));
        orderCreateDate.setText(order.getCreateDateAsText());
        orderContractor.setText(order.getContractor().getName());
        orderContractorBalance.setText(StringUtils.formatNumber(order.getContractor().getAccount(order.getCurrency().getId()).getCurrentBalance()));
        orderContractorBalanceCurrency.setText(order.getCurrency().getSign());
        orderTotalPrice.setText(StringUtils.formatNumber(order.getTotalPrice()));
        orderVatRate.setText(order.getVatRateAsText());
        orderVat.setText(StringUtils.formatNumber(order.getVat()));
        orderTotalPriceWithVat.setText(StringUtils.formatNumber(order.getTotalPriceWithVat()));

        String currencySign = order.getCurrency().getSign();
        orderCurrency.setText(currencySign);
        orderCurrencyVat.setText(currencySign);
        orderCurrencyTotalWithVat.setText(currencySign);
    }

    private void initOrderCommands() {
        if (order.getState() == OrderState.CONSTRUCTION) {
            //Order can be sent to collection.
            toCollection.setVisible(true);
            toConstruction.setVisible(false);
        } else if (order.getState() == OrderState.READY_FOR_COLLECTION) {
            //Order can be returned for construction (adding new or removing old items).
            toCollection.setVisible(false);
            toConstruction.setVisible(true);
        } else {
            //No operations with order are allowed.
            toCollection.setVisible(false);
            toConstruction.setVisible(false);
        }
        orderPanel.revalidate();
    }

    private void initDetailBatchesList() {
        //Initialize switchable view for providing multiple ways or details batch selection.
        java.util.List<SwitchableViewItem> viewItems = new ArrayList<SwitchableViewItem>();

        //1. Price list view.
        viewItems.add(new SwitchableViewItem() {
            @Override
            public String getName() {
                return I18nSupport.message("detail.batches.list.title");
            }

            @Override
            public Component getCreateViewComponent() {
                //Decorates detail batches list with new default command, which adds new item to the
                //order items list.
                DetailBatchesList detailBatchesList = new DetailBatchesList("DetailBatchesList.in.OrderEditor");
                ReportCommandsDecorator decoratedBatchesList = new ReportCommandsDecorator(detailBatchesList, new AddItemToOrderCommand(order));
                TableReport detailBatchesReport = new TableReport(decoratedBatchesList, OrderItemsEditor.this);
                return detailBatchesReport.getContentPanel();
            }
        });

        //2. Details catalog view.
        viewItems.add(new SwitchableViewItem() {
            @Override
            public String getName() {
                return I18nSupport.message("detail.catalog.list.properties.title");
            }

            @Override
            public Component getCreateViewComponent() {
                DetailCatalog detailsCatalog = new DetailCatalog(OrderItemsEditor.this, new DetailCatalogBatchesListFactory() {
                    private DetailBatchesList detailBatchesList;
                    private ReportDataSource decoratedBatchesList;

                    @Override
                    public ReportDataSource createDetailBatchesDataSource(DetailGroupTO catalogGroup) {
                        if (detailBatchesList == null) {
                            //Create new list only on first request.
                            //Also decorate detail batches list in catalog with new default command, which adds
                            //new item to the order items list.
                            detailBatchesList = new DetailBatchesList(catalogGroup, "DetailBatchesCatalog.in.OrderEditor");
                            decoratedBatchesList = new ReportCommandsDecorator(detailBatchesList, new AddItemToOrderCommand(order));
                        } else {
                            //Reuse created data source (just change filter to load appropriate list of details).
                            detailBatchesList.setCatalogGroup(catalogGroup);
                        }
                        return decoratedBatchesList;
                    }
                }, true);
                return detailsCatalog.getContentPanel();
            }
        });

        detailBatchesView = new SwitchableView(viewItems, loadDetailBatchViewIndex());
        detailBatchesPanel.add(detailBatchesView.getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    private void initOrderItemsList() {
        OrderItemsList orderItemsList = new OrderItemsList(order);
        orderItemsPanel.add(new TableReport(orderItemsList, this).getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        orderPanel = new JPanel();
        orderPanel.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
        scrollPane1.setViewportView(orderPanel);
        splitPane = new JSplitPane();
        splitPane.setDividerLocation(246);
        splitPane.setDividerSize(6);
        orderPanel.add(splitPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(300, 150), null, null, 0, false));
        detailBatchesPanel = new JPanel();
        detailBatchesPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane.setLeftComponent(detailBatchesPanel);
        orderItemsPanel = new JPanel();
        orderItemsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane.setRightComponent(orderItemsPanel);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        orderPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        toCollection = new JButton();
        this.$$$loadButtonText$$$(toCollection, ResourceBundle.getBundle("i18n/warehouse").getString("order.command.toComplecting"));
        panel2.add(toCollection, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        toConstruction = new JButton();
        this.$$$loadButtonText$$$(toConstruction, ResourceBundle.getBundle("i18n/warehouse").getString("order.command.abortComplecting"));
        panel2.add(toConstruction, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.createDate"));
        panel3.add(label1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderCreateDate = new JTextField();
        orderCreateDate.setEditable(false);
        panel3.add(orderCreateDate, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.contractor"));
        panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderContractor = new JTextField();
        orderContractor.setEditable(false);
        panel3.add(orderContractor, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.number"));
        panel3.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderNumber = new JTextField();
        orderNumber.setEditable(false);
        panel3.add(orderNumber, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.state"));
        panel3.add(label4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderState = new JTextField();
        orderState.setEditable(false);
        panel3.add(orderState, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(1, 4, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        orderContractorBalance = new JTextField();
        orderContractorBalance.setEditable(false);
        panel4.add(orderContractorBalance, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(70, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.contractorBalance"));
        panel4.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderContractorBalanceCurrency = new JLabel();
        orderContractorBalanceCurrency.setText("<Currency>");
        panel4.add(orderContractorBalanceCurrency, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.totalPrice"));
        panel6.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderCurrency = new JLabel();
        orderCurrency.setText("<Currency>");
        panel6.add(orderCurrency, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderTotalPrice = new JTextField();
        orderTotalPrice.setEditable(false);
        panel6.add(orderTotalPrice, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(120, -1), null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.vatRate"));
        panel7.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderVatRate = new JTextField();
        orderVatRate.setEditable(false);
        panel7.add(orderVatRate, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel8, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.vat"));
        panel8.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderCurrencyVat = new JLabel();
        orderCurrencyVat.setText("<Currency>");
        panel8.add(orderCurrencyVat, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderVat = new JTextField();
        orderVat.setEditable(false);
        panel8.add(orderVat, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(120, -1), null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel9, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.totalPriceWithVat"));
        panel9.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderCurrencyTotalWithVat = new JLabel();
        orderCurrencyTotalWithVat.setText("<Currency>");
        panel9.add(orderCurrencyTotalWithVat, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderTotalPriceWithVat = new JTextField();
        orderTotalPriceWithVat.setEditable(false);
        panel9.add(orderTotalPriceWithVat, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(120, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }
}
