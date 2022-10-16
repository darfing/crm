package zzh.darfing.mycrm.workbench.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzh.darfing.mycrm.workbench.mapper.ActivityRemarkMapper;
import zzh.darfing.mycrm.workbench.pojo.ActivityRemark;
import zzh.darfing.mycrm.workbench.web.service.ActivityRemarkService;
import java.util.List;

@Transactional
@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;

    @Override
    public int saveActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.insertActivityRemark(activityRemark);
    }

    @Override
    public List<ActivityRemark> queryAllActivityRemarkByActivityId(String id) {
        return activityRemarkMapper.selectAllActivityRemarkById(id);
    }

    @Override
    public int deleteActivityRemarkById(String id) {
        return activityRemarkMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int saveEditActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.updateActivityRemark(activityRemark);
    }

    @Override
    public ActivityRemark queryActivityRemarkById(String id) {
        return activityRemarkMapper.selectByPrimaryKey(id);
    }
}
