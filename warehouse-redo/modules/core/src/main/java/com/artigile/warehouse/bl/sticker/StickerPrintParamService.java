package com.artigile.warehouse.bl.sticker;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.dao.StickerPrintParamDAO;
import com.artigile.warehouse.domain.sticker.StickerPrintParam;
import com.artigile.warehouse.utils.dto.sticker.StickerPrintParamTO;
import com.artigile.warehouse.utils.transofmers.StickerPrintParamTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Valery Barysok, 2013-07-01
 */
@Transactional(rollbackFor = BusinessException.class)
public class StickerPrintParamService {

    private StickerPrintParamDAO stickerPrintParamDAO;

    public List<StickerPrintParamTO> getStickerPrintParamsBy(long detailTypeId) {
        return StickerPrintParamTransformer.transformList(stickerPrintParamDAO.getByDetailType(detailTypeId));
    }

    public StickerPrintParam getStickerPrintParamById(long stickerPrintParamId) {
        return stickerPrintParamDAO.get(stickerPrintParamId);
    }

    public void setStickerPrintParamDAO(StickerPrintParamDAO stickerPrintParamDAO) {
        this.stickerPrintParamDAO = stickerPrintParamDAO;
    }
}
