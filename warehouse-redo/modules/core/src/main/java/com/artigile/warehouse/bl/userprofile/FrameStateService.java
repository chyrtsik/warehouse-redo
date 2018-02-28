/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.userprofile;

import com.artigile.warehouse.dao.FrameStateDAO;
import com.artigile.warehouse.domain.userprofile.FrameState;
import com.artigile.warehouse.utils.dto.userprofile.FrameStateTO;
import com.artigile.warehouse.utils.transofmers.FrameStateTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for frame state
 *
 * @author Borisok V.V., 13.09.2009
 */
@Transactional
public class FrameStateService {

    private FrameStateDAO frameStateDAO;

    public void save(FrameStateTO frameStateTO) {
        FrameState frameState = FrameStateTransformer.transform(frameStateTO);
        FrameStateTransformer.update(frameState, frameStateTO);
        frameStateDAO.save(frameState);
        FrameStateTransformer.update(frameStateTO, frameState);
    }

    public void remove(FrameStateTO frameStateTO) {
        FrameState frameState = FrameStateTransformer.transform(frameStateTO);
        frameStateDAO.remove(frameState);
    }

    public FrameStateTO get(long id) {
        return FrameStateTransformer.transform(frameStateDAO.get(id));
    }

    public List<FrameStateTO> getAll() {
        return FrameStateTransformer.transformList(frameStateDAO.getAll());
    }

    public List<FrameStateTO> getByFilter(FrameStateFilter filter) {
        return FrameStateTransformer.transformList(frameStateDAO.getByFilter(filter));
    }

    public FrameState getById(long frameStateId) {
        return frameStateDAO.get(frameStateId);
    }

    //============================ Spring setters ===============================

    public void setFrameStateDAO(FrameStateDAO frameStateDAO) {
        this.frameStateDAO = frameStateDAO;
    }
}
