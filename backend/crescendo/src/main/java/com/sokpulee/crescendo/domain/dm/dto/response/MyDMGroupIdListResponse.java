package com.sokpulee.crescendo.domain.dm.dto.response;

import com.sokpulee.crescendo.domain.dm.entity.DmGroup;
import lombok.Getter;

import java.util.List;

@Getter
public class MyDMGroupIdListResponse {

    private List<Long> myDMGroupIdList;

    public MyDMGroupIdListResponse(List<DmGroup> dmGroupList) {
        this.myDMGroupIdList = dmGroupList.stream()
                                .map(DmGroup::getId)
                                .toList();
    }
}
