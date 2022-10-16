package zzh.darfing.mycrm.workbench.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzh.darfing.mycrm.workbench.mapper.ActivityMapper;
import zzh.darfing.mycrm.workbench.pojo.Activity;
import zzh.darfing.mycrm.workbench.web.service.ActivityService;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Override
    public Integer saveActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public PageInfo<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
        PageHelper.startPage((Integer) map.get("pageNo"), (Integer) map.get("pageSize"));
        return new PageInfo<>(activityMapper.selectActivityByConditionForPage(map), 5);
    }

    @Override
    public int queryCountOfActivity(Map<String, Object> map) {
        return activityMapper.selectCountOfActivity(map);
    }

    @Override
    public int saveEditActivity(Activity activity) {
        return activityMapper.updateEditActivity(activity);
    }

    @Override
    public int deleteActivityIds(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public Activity queryActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public List<Activity> queryAllActivities() {
        return activityMapper.selectAllActivities();
    }

    @Override
    public int saveActivitiesFromExcel(List<Activity> activityList) {
        return activityMapper.insertActivitiesFromExcel(activityList);
    }

    @Override
    public Activity selectActivityById(String id) {
        return activityMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Activity> queryActivityByIds(String[] ids) {
        return activityMapper.selectActivityByIds(ids);
    }

    @Override
    public List<Activity> queryActivityForDetailByNameClueId(String activityName, String clueId) {
        return activityMapper.selectActivityForDetailByNameClueId(activityName, clueId);
    }

    @Override
    public List<Activity> queryClueActivities(String clueId) {
        return activityMapper.selectClueActivities(clueId);
    }

    @Override
    public List<Activity> queryActivityByName(String name) {
        return activityMapper.selectActivityByName(name);
    }
}
