package com.artigile.warehouse.utils.dto.sticker;

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;

/**
 * @author Valery Barysok, 2013-07-01
 */
public class StickerPrintParamTO extends EqualsByIdImpl {

    private long id;

    private long detailTypeId;

    private DetailFieldTO detailField;

    private DetailFieldTO serialDetailField;

    private long orderNum;

    public StickerPrintParamTO() {
    }

    public StickerPrintParamTO(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDetailTypeId() {
        return detailTypeId;
    }

    public void setDetailTypeId(long detailTypeId) {
        this.detailTypeId = detailTypeId;
    }

    public DetailFieldTO getDetailField() {
        return detailField;
    }

    public void setDetailField(DetailFieldTO detailField) {
        this.detailField = detailField;
    }

    public DetailFieldTO getSerialDetailField() {
        return serialDetailField;
    }

    public void setSerialDetailField(DetailFieldTO serialDetailField) {
        this.serialDetailField = serialDetailField;
    }

    public long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(long orderNum) {
        this.orderNum = orderNum;
    }
}
