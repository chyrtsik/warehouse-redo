/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings.items;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.listeners.DataChangeAdapter;
import com.artigile.warehouse.bl.common.listeners.DataChangeListener;
import com.artigile.warehouse.bl.detail.DetailBatchService;
import com.artigile.warehouse.bl.detail.DetailSerialNumberService;
import com.artigile.warehouse.bl.lock.Utils;
import com.artigile.warehouse.bl.postings.PostingService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.plugin.FramePlugin;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.barcode.BarCodeListener;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.ReportCommandListImpl;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.core.report.decorator.ReportCommandsDecorator;
import com.artigile.warehouse.gui.core.switchable.SwitchableView;
import com.artigile.warehouse.gui.core.switchable.SwitchableViewItem;
import com.artigile.warehouse.gui.menuitems.details.batches.DetailBatchesList;
import com.artigile.warehouse.gui.menuitems.details.catalog.DetailCatalog;
import com.artigile.warehouse.gui.menuitems.details.catalog.DetailCatalogBatchesListFactory;
import com.artigile.warehouse.gui.menuitems.postings.items.command.AddItemToPostingCommand;
import com.artigile.warehouse.gui.menuitems.postings.items.command.QuickAddItemToPostingCommand;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;
import com.artigile.warehouse.utils.dto.details.DetailSerialNumberTO;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;
import com.artigile.warehouse.utils.dto.postings.PostingTOForReport;
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
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 05.02.2009
 */
public class PostingItemsEditor extends FramePlugin {
    private JPanel contentPanel;
    private JPanel detailBatchesPanel;
    private JPanel postingItemsPanel;
    private JTextField fieldState;
    private JTextField fieldNumber;
    private JTextField fieldCreateDate;
    private JTextField fieldTotalPrice;
    private JLabel fieldCurrency;
    private JTextField fieldCreatedUser;
    private JButton completePosting;
    private JSplitPane postingItemsSplitter;
    private JPanel postingItemsSplitterPanel;
    private JPanel postingPanel;
    private JButton enableBarCodesScanner;
    private JTextField fieldTotalItems;

    private PostingTOForReport posting;

    /**
     * Permission, which allows edit sale price of item.
     */
    private PermissionCommandAvailability permissionAvailability = new PermissionCommandAvailability(PermissionType.EDIT_POSTING_COMPLETION);

    private SwitchableView detailBatchesView;
    private static final String PROPERTY_DETAIL_BATCHES_VIEW_INDEX = "PostingItemsEditor.DetailBatchesView.SelectedIndex";
    private boolean currentAddingNewPostingItemsAllowed;

    /**
     * Storing information about last edited posting item.
     */
    private LastEditedItemProvider lastEditedItemProvider = new DefaultLastEditedItemProvider();

    //============================== Constructors =====================================

    public PostingItemsEditor(long postingId) {
        enableBarCodesScanner.setVisible(false);

        posting = SpringServiceContext.getInstance().getPostingsService().getPostingForReport(postingId);
        currentAddingNewPostingItemsAllowed = posting.isAddingNewPostingItemsAllowed();

        refreshPostingState();
        initListeners();
        initEditorReports();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("posting.items.editor.title", posting.getNumber());
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    protected void onFrameOpened() {
        super.onFrameOpened();

        SplitPaneSaver.restore(postingItemsSplitter, getFrameId());
        SplitPaneOrietationSaver.restore(postingItemsSplitter, PostingItemsEditor.class);

        //We should react on changes of posting to keep editor in actual state.
        SpringServiceContext.getInstance().getDataChangeNolifier().addDataChangeListener(PostingTOForReport.class, postingChangeListener);
    }

    @Override
    protected void onFrameClosed() {
        SplitPaneSaver.store(postingItemsSplitter, getFrameId());
        if (detailBatchesView != null) {
            Properties.setProperty(PROPERTY_DETAIL_BATCHES_VIEW_INDEX, String.valueOf(detailBatchesView.getSelectedViewIndex()));
        }

        super.onFrameClosed();

        //We should not forget unregister posting changes listener to prevent memory leaks.
        SpringServiceContext.getInstance().getDataChangeNolifier().removeDataChangeListener(PostingTOForReport.class, postingChangeListener);
    }

    //============================ Data change listeners ============================================

    /**
     * Listens to changes, made in posting documents. Is changed postings is being viewed in this redactor
     * refreshes UI to display changes.
     */
    private final DataChangeListener postingChangeListener = new DataChangeAdapter() {
        @Override
        public void afterChange(Object changedData) {
            PostingTOForReport changedPosting = (PostingTOForReport) changedData;
            onPostingChanged(changedPosting);
        }
    };

    private void onPostingChanged(PostingTOForReport changedPosting) {
        if (posting.getId() == changedPosting.getId()) {
            posting = changedPosting;
            refreshPostingState();
            if (posting.isAddingNewPostingItemsAllowed() != currentAddingNewPostingItemsAllowed) {
                currentAddingNewPostingItemsAllowed = posting.isAddingNewPostingItemsAllowed();
                initEditorReports();
            }
        }
    }

    //========================================= Helpers ===========================================

    private void initListeners() {
        completePosting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCompletePosting();
            }
        });
        enableBarCodesScanner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onInputFromBarCodeScanner();
            }
        });
    }

    private void onInputFromBarCodeScanner() {
        //Create listener to create and update posting items from barcode scanner.
        BarCodeListener barCodeListener = new BarCodeListener() {
            @Override
            public void onBarCodeReceived(String barCode) {
                long countToAdd = 1;
                Date shelfLifeDate = null;

                DetailBatchTO detailBatch = null;

                Long serialNumberId = DetailSerialNumberService.parseSerialNumberBarCode(barCode);
                if (serialNumberId != null) {
                    //Serial number bar code was scanned.
                    DetailSerialNumberService serialNumberService = SpringServiceContext.getInstance().getDetailSerialNumberService();
                    DetailSerialNumberTO serialNumber = serialNumberService.loadSerialNumberTOById(serialNumberId);
                    if (serialNumber != null) {
                        detailBatch = serialNumber.getDetail();
                        shelfLifeDate = serialNumber.getShelfLifeDate();
                        countToAdd = (serialNumber.getCountInPackaging() != null) ? serialNumber.getCountInPackaging() : countToAdd;
                    }
                } else {
                    //Product bar code was scanned.
                    DetailBatchService detailBatchService = SpringServiceContext.getInstance().getDetailBatchesService();
                    detailBatch = detailBatchService.getDetailBatchByBarCode(barCode);
                }

                PostingService postingService = SpringServiceContext.getInstance().getPostingsService();

                PostingItemTO samePostingItem;
                if (detailBatch != null) {
                    //Process detail batch found in catalog.
                    samePostingItem = postingService.getSamePostingItem(posting.getId(), detailBatch.getId(), shelfLifeDate);
                } else {
                    //Process unclassified item (bar code is not present in catalog).
                    samePostingItem = postingService.getSameUnclassifiedPostingItem(posting.getId(), barCode);
                }

                if (samePostingItem == null) {
                    //Create new posting item (either item itself or shelf life date differs).
                    PostingItemTO newPostingItem = detailBatch != null
                            ? new PostingItemTO(posting, detailBatch)
                            : PostingItemTO.createUnclassifiedBarCodeItem(posting, barCode);
                    newPostingItem.setOriginalCurrency(posting.getDefaultCurrency());
                    newPostingItem.setStoragePlace(posting.getDefaultStoragePlace());
                    newPostingItem.setCount(countToAdd);
                    newPostingItem.setShelfLifeDate(shelfLifeDate);
                    postingService.addItemToPosting(newPostingItem);

                    //Highlight added item.
                    lastEditedItemProvider.setLastEditedItemId(newPostingItem.getId());
                } else {
                    //Update existing posting item (just increase quantity).
                    long lastCount = samePostingItem.getCount() == null ? 0 : samePostingItem.getCount();
                    samePostingItem.setCount(lastCount + countToAdd);
                    postingService.savePostingItem(samePostingItem);

                    //Highlight updated item.
                    lastEditedItemProvider.setLastEditedItemId(samePostingItem.getId());
                }

                //Repaint to guaranty update of current item.
                postingItemsPanel.repaint();
            }
        };

        Dialogs.runMultipleBarCodeInput(barCodeListener);
    }

    private void onCompletePosting() {
        //Make posting completed.
        if (!permissionAvailability.isAvailable(null)) {
            MessageDialogs.showMessage(I18nSupport.message("warning"), I18nSupport.message("security.no.rights"));
        } else {
            try {
                SpringServiceContext.getInstance().getPostingsService().completePosting(posting.getId());
            } catch (BusinessException e) {
                MessageDialogs.showWarning(contentPanel, e.getMessage());
            } catch (RuntimeException e) {
                if (Utils.isLockFailedException(e)) {
                    MessageDialogs.showWarning(contentPanel, I18nSupport.message("locking.warning.use.locked.object"));
                } else {
                    MessageDialogs.showError(contentPanel, e);
                }
            } catch (Throwable e) {
                MessageDialogs.showError(contentPanel, e);
            }
        }
    }

    private void refreshPostingState() {
        //Init user interface of posting editor according to state of the posting.
        initPostingProperties();
        completePosting.setEnabled(posting.isPostingCompletionAllowed());
        postingPanel.revalidate();
    }

    private void initEditorReports() {
        postingItemsSplitterPanel.removeAll();

        boolean canEditPosting = posting.isAddingNewPostingItemsAllowed();
        if (canEditPosting) {
            //Init interface for editing posting, when new items may be alled into posting.
            postingItemsSplitterPanel.add(postingItemsSplitter, GridLayoutUtils.getGrowingAndFillingCellConstraints());
            initDetailBatchesList();
            initPostingItemsList(postingItemsPanel);
            enableBarCodesScanner.setVisible(true);

        } else {
            //Init interface for completed posting.
            initPostingItemsList(postingItemsSplitterPanel);
            enableBarCodesScanner.setVisible(false);
        }

        postingItemsSplitterPanel.revalidate();
    }

    private void initPostingProperties() {
        fieldState.setText(posting.getState().getName());
        fieldNumber.setText(String.valueOf(posting.getNumber()));
        fieldCreateDate.setText(posting.getCreateDateAsText());
        fieldCreatedUser.setText(posting.getCreatedUser().getDisplayName());
        fieldCurrency.setText(posting.getCurrency() == null ? null : posting.getCurrency().getSign());
        fieldTotalItems.setText(posting.getTotalItemsQuantity());
        fieldTotalPrice.setText(StringUtils.formatNumber(posting.getTotalPrice()));
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
                //Decorates detail batches list with new commands for adding posting items to list.
                DetailBatchesList detailBatchesList = new DetailBatchesList("DetailBatchesList.in.PostingEditor");
                ReportCommandsDecorator decoratedBatchesList = new ReportCommandsDecorator(detailBatchesList, createDetailBatchesDecoratedCommands());

                TableReport detailBatchesReport = new TableReport(decoratedBatchesList, PostingItemsEditor.this);
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
                DetailCatalog detailsCatalog = new DetailCatalog(PostingItemsEditor.this, new DetailCatalogBatchesListFactory() {
                    private DetailBatchesList detailBatchesList;
                    private ReportDataSource decoratedBatchesList;

                    @Override
                    public ReportDataSource createDetailBatchesDataSource(DetailGroupTO catalogGroup) {
                        if (detailBatchesList == null) {
                            //Create new list only on first request.
                            detailBatchesList = new DetailBatchesList(catalogGroup, "DetailBatchesCatalog.in.PostingEditor");
                            decoratedBatchesList = new ReportCommandsDecorator(detailBatchesList, createDetailBatchesDecoratedCommands());
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

    private ReportCommandList createDetailBatchesDecoratedCommands() {
        ReportCommandList additionalCommands = new ReportCommandListImpl();
        additionalCommands.add(new QuickAddItemToPostingCommand(posting));
        additionalCommands.add(new AddItemToPostingCommand(posting));
        additionalCommands.setDefaultCommandForRow(additionalCommands.get(0));
        return additionalCommands;
    }

    private int loadDetailBatchViewIndex() {
        //Restores previously stored detail bathes view index (to use the same view on each new posting edition).
        Integer storedViewIndex = Properties.getPropertyAsInteger(PROPERTY_DETAIL_BATCHES_VIEW_INDEX);
        return storedViewIndex == null ? -1 : storedViewIndex;
    }

    private void initPostingItemsList(JComponent container) {
        container.removeAll();
        PostingItemsList postingItemsList = new PostingItemsList(posting.getId(), lastEditedItemProvider);
        container.add(new TableReport(postingItemsList, this).getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
        container.revalidate();
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
        scrollPane1.setViewportView(panel1);
        postingPanel = new JPanel();
        postingPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(postingPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 6, new Insets(0, 0, 0, 0), -1, -1));
        postingPanel.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.createdUser"));
        panel2.add(label1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCreatedUser = new JTextField();
        fieldCreatedUser.setEditable(false);
        panel2.add(fieldCreatedUser, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.number"));
        panel2.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldNumber = new JTextField();
        fieldNumber.setEditable(false);
        panel2.add(fieldNumber, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.state"));
        panel2.add(label3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldState = new JTextField();
        fieldState.setEditable(false);
        panel2.add(fieldState, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.createDate"));
        panel2.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCreateDate = new JTextField();
        fieldCreateDate.setEditable(false);
        panel2.add(fieldCreateDate, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.total.items"));
        panel2.add(label5, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldTotalItems = new JTextField();
        fieldTotalItems.setEditable(false);
        panel2.add(fieldTotalItems, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.totalPrice"));
        panel2.add(label6, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldTotalPrice = new JTextField();
        fieldTotalPrice.setEditable(false);
        panel3.add(fieldTotalPrice, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        fieldCurrency = new JLabel();
        fieldCurrency.setText("<Currency>");
        panel3.add(fieldCurrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        postingPanel.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        completePosting = new JButton();
        this.$$$loadButtonText$$$(completePosting, ResourceBundle.getBundle("i18n/warehouse").getString("posting.command.complete"));
        panel4.add(completePosting, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        enableBarCodesScanner = new JButton();
        this.$$$loadButtonText$$$(enableBarCodesScanner, ResourceBundle.getBundle("i18n/warehouse").getString("posting.items.editor.command.input.barcode.enable"));
        panel4.add(enableBarCodesScanner, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        postingItemsSplitterPanel = new JPanel();
        postingItemsSplitterPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(postingItemsSplitterPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        postingItemsSplitter = new JSplitPane();
        postingItemsSplitter.setDividerLocation(300);
        postingItemsSplitter.setDividerSize(6);
        postingItemsSplitterPanel.add(postingItemsSplitter, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(300, 150), null, null, 0, false));
        detailBatchesPanel = new JPanel();
        detailBatchesPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        postingItemsSplitter.setLeftComponent(detailBatchesPanel);
        postingItemsPanel = new JPanel();
        postingItemsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        postingItemsSplitter.setRightComponent(postingItemsPanel);
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
