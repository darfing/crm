package zzh.darfing.mycrm.workbench.web.service;

import zzh.darfing.mycrm.workbench.pojo.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    int saveActivityRemark(ActivityRemark activityRemark);
    List<ActivityRemark> queryAllActivityRemarkByActivityId(String id);

    int deleteActivityRemarkById(String id);

    int saveEditActivityRemark(ActivityRemark activityRemark);

    ActivityRemark queryActivityRemarkById(String id);
}
