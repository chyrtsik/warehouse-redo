package com.artigile.warehouse.domain.sticker;

import com.artigile.warehouse.domain.details.DetailField;
import com.artigile.warehouse.domain.details.DetailType;

import javax.persistence.*;

/**
 * @author Valery Barysok, 2013-07-01
 */
@Entity
@Table(name = "sticker_print_param")
public class StickerPrintParam {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "detail_type_id", referencedColumnName = "id", nullable = false)
    private DetailType detailType;

    @ManyToOne
    @JoinColumn(name = "detail_field_id", unique = true)
    private DetailField detailField;

    @ManyToOne
    @JoinColumn(name = "serial_detail_field_id", unique = true)
    private DetailField serialDetailField;

    @Column(name = "order_num")
    private long orderNum;

    public StickerPrintParam() {
    }

    public StickerPrintParam(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DetailType getDetailType() {
        return detailType;
    }

    public void setDetailType(DetailType detailType) {
        this.detailType = detailType;
    }

    public DetailField getDetailField() {
        return detailField;
    }

    public void setDetailField(DetailField detailField) {
        this.detailField = detailField;
    }

    public DetailField getSerialDetailField() {
        return serialDetailField;
    }

    public void setSerialDetailField(DetailField serialDetailField) {
        this.serialDetailField = serialDetailField;
    }

    public long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(long orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StickerPrintParam that = (StickerPrintParam) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
