package zzh.darfing.mycrm.workbench.web.service;

import org.apache.ibatis.annotations.Param;
import zzh.darfing.mycrm.workbench.pojo.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    List<String> queryActivityIdByClueId(String clueId);
    int saveBounds(List<ClueActivityRelation> list);

    int deleteBoundByActivityIdAndClueId(String activityId, String clueId);
}
