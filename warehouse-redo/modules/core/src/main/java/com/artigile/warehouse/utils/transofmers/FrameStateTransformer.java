/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.bl.userprofile.FrameStateService;
import com.artigile.warehouse.domain.userprofile.FrameState;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.userprofile.FrameStateTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 13.09.2009
 */
public final class FrameStateTransformer {
    private FrameStateTransformer() {
    }

    public static FrameStateService getFrameStateService(){
        return SpringServiceContext.getInstance().getFrameStateService();
    }

    public static FrameStateTO transform(FrameState frameState) {
        FrameStateTO frameStateTO = new FrameStateTO();
        update(frameStateTO, frameState);
        return frameStateTO;
    }

    public static void update(FrameStateTO frameStateTO, FrameState frameState){
        frameStateTO.setId(frameState.getId());
        frameStateTO.setFrameId(frameState.getFrameId());
        frameStateTO.setUser(UserTransformer.transformUser(frameState.getUser()));
        frameStateTO.setTop(frameState.getTop());
        frameStateTO.setLeft(frameState.getLeft());
        frameStateTO.setWitdh(frameState.getWidth());
        frameStateTO.setHeight(frameState.getHeight());
        frameStateTO.setMaximazed(frameState.getMaximized());
    }

    public static List<FrameStateTO> transformList(List<FrameState> frameStateList) {
        List<FrameStateTO> list = new ArrayList<FrameStateTO>();
        for (FrameState frameState : frameStateList) {
            list.add(transform(frameState));
        }
        return list;
    }

    public static FrameState transform(FrameStateTO frameStateTO) {
        FrameState frameState = SpringServiceContext.getInstance().getFrameStateService().getById(frameStateTO.getId());
        if (frameState == null){
            frameState = new FrameState();
        }
        return frameState;
    }

    public static void update(FrameState frameState, FrameStateTO frameStateTO){
        frameState.setId(frameStateTO.getId());
        frameState.setFrameId(frameStateTO.getFrameId());
        frameState.setUser(UserTransformer.transformUser(frameStateTO.getUser()));
        frameState.setTop(frameStateTO.getTop());
        frameState.setLeft(frameStateTO.getLeft());
        frameState.setWidth(frameStateTO.getWitdh());
        frameState.setHeight(frameStateTO.getHeight());
        frameState.setMaximized(frameStateTO.getMaximazed());
    }
}
