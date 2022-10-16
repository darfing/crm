package zzh.darfing.mycrm.workbench.web.service;

import com.github.pagehelper.PageInfo;
import zzh.darfing.mycrm.workbench.pojo.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    Integer saveActivity(Activity activity);
    PageInfo<Activity> queryActivityByConditionForPage(Map<String, Object> map);

    int queryCountOfActivity(Map<String, Object> map);

    int saveEditActivity(Activity activity);

    int deleteActivityIds(String[] ids);

    Activity queryActivityById(String id);

    List<Activity> queryAllActivities();

    int saveActivitiesFromExcel(List<Activity> activityList);

    Activity selectActivityById(String id);

    List<Activity> queryActivityByIds(String[] ids);

    List<Activity> queryActivityForDetailByNameClueId(String activityName, String clueId);

    List<Activity> queryClueActivities(String clueId);

    List<Activity> queryActivityByName(String name);
}
