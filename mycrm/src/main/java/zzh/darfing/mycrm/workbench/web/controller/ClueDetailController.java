package zzh.darfing.mycrm.workbench.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zzh.darfing.mycrm.commons.constants.Constants;
import zzh.darfing.mycrm.commons.pojo.Result;
import zzh.darfing.mycrm.commons.utils.DateFormatUtil;
import zzh.darfing.mycrm.commons.utils.UUIDUtil;
import zzh.darfing.mycrm.settings.pojo.DicValue;
import zzh.darfing.mycrm.settings.pojo.User;
import zzh.darfing.mycrm.settings.web.service.DictionaryValueService;
import zzh.darfing.mycrm.workbench.pojo.Activity;
import zzh.darfing.mycrm.workbench.pojo.Clue;
import zzh.darfing.mycrm.workbench.pojo.ClueActivityRelation;
import zzh.darfing.mycrm.workbench.pojo.ClueRemark;
import zzh.darfing.mycrm.workbench.web.service.ActivityService;
import zzh.darfing.mycrm.workbench.web.service.ClueActivityRelationService;
import zzh.darfing.mycrm.workbench.web.service.ClueRemarkService;
import zzh.darfing.mycrm.workbench.web.service.ClueService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/clue")
public class ClueDetailController {
    @Autowired
    private ClueService clueService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;
    @Autowired
    private ActivityService activityService;

    @Autowired
    private DictionaryValueService dictionaryValueService;

    @RequestMapping("/detail.do/{id}")
    public String detail(HttpServletRequest request, @PathVariable("id") String id) {
        //查询线索详细信息
        Clue clue = clueService.queryClueById(id);
        /*查询该线索对应的备注*/
        List<ClueRemark> clueRemarks = clueRemarkService.queryClueRemarksByClueId(id);
        /*查询与该线索关联的活动*/
        List<Activity> activityList = activityService.queryClueActivities(id);
        request.setAttribute("clue", clue);
        request.setAttribute("remarkList", clueRemarks);
        request.setAttribute("activityList", activityList);
        return "workbench/clue/detail";
    }

    @RequestMapping("/saveCreateClueRemark.do")
    @ResponseBody
    public Object saveCreateClueRemark(ClueRemark clueRemark, HttpSession session) {
        Result result = new Result();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        clueRemark.setId(UUIDUtil.getUUID());
        clueRemark.setCreateBy(user.getId());
        clueRemark.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
        clueRemark.setEditFlag(Constants.REMARK_EDIT_FLAG_NO);
        try {
            int i = clueRemarkService.saveClueRemark(clueRemark);
            if (i > 0) {
                result.setCode(Constants.SUCCESS);
                result.setMessage("保存成功");
                result.setRetData(clueRemark);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("服务器繁忙，请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙，请稍后重试...");
        }
        return result;
    }

    @RequestMapping("/queryActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameClueId(String activityName, String clueId) {
        Result result = new Result();
        try {
            List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(activityName, clueId);
            if (null != activityList) {
                result.setCode(Constants.SUCCESS);
                result.setRetData(activityList);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("查询结果为空");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙，请稍后重试...");
        }
        return result;
    }

    @RequestMapping("/saveBound.do")
    @ResponseBody
    public Object saveBound(String[] id) {
        Result result = new Result();
        int i = 0;
        int l = id.length - 1;
        List<ClueActivityRelation> list = new ArrayList<>();
        while (i < l) {
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setActivityId(id[i]);
            clueActivityRelation.setClueId(id[l]);
            list.add(clueActivityRelation);
            i++;
        }
        try {
            int count = clueActivityRelationService.saveBounds(list);
            List<Activity> activityList = activityService.queryClueActivities(id[l]);
            if(count > 0) {
                result.setCode(Constants.SUCCESS);
                result.setRetData(activityList);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("服务器繁忙，请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙，请稍后重试...");
        }
        return result;
    }

    @RequestMapping("/saveUnbound.do")
    @ResponseBody
    public Object saveUnbound(String activityId, String clueId) {
        Result result = new Result();
        try {
            int i = clueActivityRelationService.deleteBoundByActivityIdAndClueId(activityId, clueId);
            if(i > 0) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("服务器繁忙，请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙，请稍后重试...");
        }
        return result;
    }

    @RequestMapping("/toConvert.do/{id}")
    public String toConvert(@PathVariable("id") String id, HttpServletRequest request) {
        Clue clue = clueService.queryClueById(id);
        List<DicValue> stageList = dictionaryValueService.queryAllDicValueByTypeCode("stage");
        List<Activity> activityList = activityService.queryClueActivities(id);
        request.setAttribute("clue", clue);
        request.setAttribute("stageList", stageList);
        request.setAttribute("activityList", activityList);
        return "workbench/clue/convert";
    }

    @RequestMapping("/convertClue.do")
    @ResponseBody
    public Object convertClue(@RequestParam Map<String, Object> map, HttpSession session) {
        Result result = new Result();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        map.put(Constants.SESSION_USER, user);
        try {
            clueService.saveConvert(map);
            result.setCode(Constants.SUCCESS);
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙，请稍后重试...");
        }
        return result;
    }

/*    @RequestMapping("/queryActivityWithClueId.do")
    public Object queryActivityWithClueId(String activityName, String clueId) {
        Result result = new Result();
        try {
            List<Activity> activityList = activityService.queryClueActivities(clueId);
            if(null != activityList) {
                result.setCode(Constants.SUCCESS);
                result.setRetData(activityList);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("服务器繁忙，请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙，请稍后重试...");
        }
        return result;
    }*/


}
