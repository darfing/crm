package zzh.darfing.mycrm.workbench.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzh.darfing.mycrm.workbench.mapper.ActivityMapper;
import zzh.darfing.mycrm.workbench.mapper.ClueActivityRelationMapper;
import zzh.darfing.mycrm.workbench.pojo.Activity;
import zzh.darfing.mycrm.workbench.pojo.ClueActivityRelation;
import zzh.darfing.mycrm.workbench.web.service.ClueActivityRelationService;

import java.util.List;

@Service
@Transactional
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Override
    public List<String> queryActivityIdByClueId(String clueId) {
        return clueActivityRelationMapper.selectActivityIdByClueId(clueId);
    }

    @Override
    public int saveBounds(List<ClueActivityRelation> list) {
        return clueActivityRelationMapper.insertBounds(list);
    }

    @Override
    public int deleteBoundByActivityIdAndClueId(String activityId, String clueId) {
        return clueActivityRelationMapper.deleteBoundByActivityIdAndClueId(activityId, clueId);
    }

}
