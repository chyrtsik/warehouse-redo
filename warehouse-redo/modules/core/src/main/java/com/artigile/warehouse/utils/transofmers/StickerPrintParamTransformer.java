package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.details.DetailField;
import com.artigile.warehouse.domain.details.DetailType;
import com.artigile.warehouse.domain.sticker.StickerPrintParam;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.sticker.StickerPrintParamTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Valery Barysok, 2013-07-01
 */
public class StickerPrintParamTransformer {

    private StickerPrintParamTransformer() {
    }

    public static StickerPrintParamTO transform(StickerPrintParam stickerPrintParam) {
        StickerPrintParamTO stickerPrintParamTO = new StickerPrintParamTO(stickerPrintParam.getId());
        update(stickerPrintParamTO, stickerPrintParam);
        return stickerPrintParamTO;
    }

    public static List<StickerPrintParamTO> transformList(List<StickerPrintParam> stickerPrintParams) {
        List<StickerPrintParamTO> stickerPrintParamTOs = new ArrayList<StickerPrintParamTO>();
        for (StickerPrintParam stickerPrintParam : stickerPrintParams) {
            stickerPrintParamTOs.add(transform(stickerPrintParam));
        }
        return stickerPrintParamTOs;
    }

    public static void update(StickerPrintParamTO stickerPrintParamTO, StickerPrintParam stickerPrintParam) {
        Long id = stickerPrintParam.getId();
        stickerPrintParamTO.setId(id != null ? id : 0L);
        stickerPrintParamTO.setDetailTypeId(stickerPrintParam.getDetailType().getId());
        stickerPrintParamTO.setDetailField(DetailTypesTransformer.transformDetailTypeField(stickerPrintParam.getDetailField()));
        stickerPrintParamTO.setSerialDetailField(DetailTypesTransformer.transformDetailTypeField(stickerPrintParam.getSerialDetailField()));
        stickerPrintParamTO.setOrderNum(stickerPrintParam.getOrderNum());
    }

    public static StickerPrintParam transform(StickerPrintParamTO stickerPrintParamTO, DetailType detailType, DetailField detailField, DetailField serialDetailField) {
        if (stickerPrintParamTO == null) {
            return null;
        }
        StickerPrintParam stickerPrintParam = SpringServiceContext.getInstance().getStickerPrintParamService().getStickerPrintParamById(stickerPrintParamTO.getId());
        if (stickerPrintParam == null) {
            stickerPrintParam = new StickerPrintParam();
        }
        update(stickerPrintParam, stickerPrintParamTO, detailType, detailField, serialDetailField);
        return stickerPrintParam;
    }

    public static List<StickerPrintParam> transformTOList(List<StickerPrintParamTO> stickerPrintParamTOs, DetailType detailType) {
        List<StickerPrintParam> stickerPrintParams = new ArrayList<StickerPrintParam>();
        Map<Long, DetailField> idToField = buildIdToField(detailType.getFields());
        Map<Long, DetailField> idToSerialField = buildIdToField(detailType.getSerialNumberFields());
        for (StickerPrintParamTO stickerPrintParamTO : stickerPrintParamTOs) {
            DetailFieldTO detailFieldTO = stickerPrintParamTO.getDetailField();
            DetailField detailField = idToField.get(detailFieldTO != null ? detailFieldTO.getId() : null);
            DetailFieldTO serialDetailFieldTO = stickerPrintParamTO.getSerialDetailField();
            DetailField serialDetailField = idToSerialField.get(serialDetailFieldTO != null ? serialDetailFieldTO.getId() : null);
            stickerPrintParams.add(transform(stickerPrintParamTO, detailType, detailField, serialDetailField));
        }
        return stickerPrintParams;
    }

    private static Map<Long, DetailField> buildIdToField(List<DetailField> detailFields) {
        Map<Long, DetailField> result = new HashMap<Long, DetailField>();
        addFields(result, detailFields);
        return result;
    }

    private static void addFields(Map<Long, DetailField> result, List<DetailField> fields) {
        for (DetailField detailField : fields) {
            result.put(detailField.getId(), detailField);
        }
    }

    public static void update(StickerPrintParam stickerPrintParam, StickerPrintParamTO stickerPrintParamTO, DetailType detailType, DetailField detailField, DetailField serialDetailField) {
        stickerPrintParam.setDetailType(detailType);
        stickerPrintParam.setDetailField(detailField);
        stickerPrintParam.setSerialDetailField(serialDetailField);
        stickerPrintParam.setOrderNum(stickerPrintParamTO.getOrderNum());
    }
}
