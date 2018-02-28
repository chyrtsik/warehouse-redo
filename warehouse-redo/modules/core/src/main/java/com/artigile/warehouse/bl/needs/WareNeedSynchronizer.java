package com.artigile.warehouse.bl.needs;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.purchase.PurchaseChangeAdapter;
import com.artigile.warehouse.bl.purchase.PurchaseService;
import com.artigile.warehouse.domain.needs.WareNeedItem;
import com.artigile.warehouse.domain.needs.WareNeedItemState;
import com.artigile.warehouse.domain.purchase.Purchase;
import com.artigile.warehouse.domain.purchase.PurchaseItem;
import com.artigile.warehouse.domain.purchase.PurchaseState;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Vadim.Zverugo
 */

/**
 * This is class reacts on changes state of purchases.
 */
@Transactional(rollbackFor = BusinessException.class)
public class WareNeedSynchronizer extends PurchaseChangeAdapter {

    /**
     * For using methods of purchase service.
     */
    private PurchaseService purchaseService;

    /**
     * For using methods of ware need service.
     */
    private WareNeedService wareNeedService;

    public void initialize() {
        purchaseService.addListener(this);   
    }

    @Override
    public void onPurchaseStateChanged(Purchase purchase) {
        List<PurchaseItem> purchaseItems;
        purchaseItems = purchase.getItems();
        WareNeedItem wareNeedItem;

        for (PurchaseItem purchaseItem : purchaseItems) {
            wareNeedItem = purchaseItem.getWareNeedItem();
            if (purchase.getState() == PurchaseState.WAITING){
                wareNeedItem.setState(WareNeedItemState.ORDERED);
            } else if (purchase.getState() == PurchaseState.SHIPPED){
                wareNeedItem.setState(WareNeedItemState.SHIPPED);
            } else if (purchase.getState() == PurchaseState.POSTING_DONE) {
                wareNeedItem.setState(WareNeedItemState.CLOSED);
            }
            wareNeedService.saveWareNeedItem(wareNeedItem);
        }
    }

    //======================== Spring setters =========================
    public void setPurchaseService(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    public void setWareNeedService(WareNeedService wareNeedService) {
        this.wareNeedService = wareNeedService;
    }
}
