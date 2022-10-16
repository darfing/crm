package zzh.darfing.mycrm.workbench.web.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zzh.darfing.mycrm.commons.constants.Constants;
import zzh.darfing.mycrm.commons.pojo.Result;
import zzh.darfing.mycrm.commons.utils.ExcelUtil;
import zzh.darfing.mycrm.commons.utils.DateFormatUtil;
import zzh.darfing.mycrm.commons.utils.FileUtil;
import zzh.darfing.mycrm.commons.utils.UUIDUtil;
import zzh.darfing.mycrm.settings.pojo.User;
import zzh.darfing.mycrm.settings.web.service.UserService;
import zzh.darfing.mycrm.workbench.pojo.Activity;
import zzh.darfing.mycrm.workbench.pojo.ActivityRemark;
import zzh.darfing.mycrm.workbench.web.service.ActivityRemarkService;
import zzh.darfing.mycrm.workbench.web.service.ActivityService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {
    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        request.setAttribute("userList", userList);
        return "workbench/activity/index";
    }

    @RequestMapping(value = "/saveCreateActivity.do", method = RequestMethod.POST)
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        //设置市场活动的id
        activity.setId(UUIDUtil.getUUID());
        //设置市场活动的创建时间
        activity.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
        //设置市场活动的创建者
        activity.setCreateBy(user.getId());
        Result result = new Result();
        try {
            Integer integer = activityService.saveActivity(activity);
            if(integer > 0) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("系统忙,请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Constants.FAIL);
            result.setMessage("系统忙,请稍后重试...");
        }
        return result;
    }

    @RequestMapping(value = "/queryActivityByConditionForPage.do", method = RequestMethod.POST)
    @ResponseBody
    public Object queryActivityByConditionForPage(Integer pageNo,
                                                  Integer pageSize,
                                                  String name,
                                                  String owner,
                                                  String startDate,
                                                  String endDate) {
        Map<String, Object> map = new HashMap<>();
//        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        PageInfo<Activity> activityPageInfo = activityService.queryActivityByConditionForPage(map);
        int totalRows = activityService.queryCountOfActivity(map);
//        Map<String, Object> data = new HashMap<>();
//        data.put("activityPageInfo", activityPageInfo);
//        data.put("totalRows", totalRows);
//        return data;
        return activityPageInfo;
    }
    @RequestMapping("/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        activity.setEditBy(user.getId());
        activity.setEditTime(DateFormatUtil.formatDateAndTime(new Date()));
        Result returnRst = new Result();
        try {
            int result = activityService.saveEditActivity(activity);
            if(result > 0) {
                returnRst.setCode(Constants.SUCCESS);
            } else {
                returnRst.setCode(Constants.FAIL);
                returnRst.setMessage("系统繁忙，请稍后重试...");
            }
        } catch (Exception e) {
            returnRst.setCode(Constants.FAIL);
            returnRst.setMessage("系统繁忙，请稍后重试...");
            throw new RuntimeException(e);
        }
        return returnRst;
    }

    @RequestMapping(value = "/deleteActivityIds.do", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteActivityIds(String[] id) {
        Result result = new Result();
        try {
            int i = activityService.deleteActivityIds(id);
            if(i > 0) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("系统繁忙，请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("系统繁忙，请稍后重试...");
            throw new RuntimeException(e);
        }
        return result;
    }

    @RequestMapping("/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        return activityService.queryActivityById(id);
    }

    @RequestMapping("/exportAllActivities.do")
    @ResponseBody
    public ResponseEntity<byte[]> exportAllActivities() {
        List<Activity> activityList = activityService.queryAllActivities();
        String filename = "activities.xlsx";
        //获取服务器中文件的真实路径
        String realPath = "D:\\Java\\crm\\mycrm\\src\\main\\webapp\\download\\" + filename;
        //创建excel
        ExcelUtil.createExcel(realPath, activityList, Activity.class);
        //利用文件下载工具将文件返回给浏览器
        return FileUtil.fileDownUtil(realPath, filename);
    }

/*    @RequestMapping("/importActivity.do")
    @ResponseBody
    public Object importActivity(@RequestParam("activityFile") MultipartFile multipartFile, HttpSession session) {
        Result result = new Result();
        //通过multipartFile得到文件名
        String filename = multipartFile.getOriginalFilename();
        String fileType = filename.split("\\.")[1];
        *//*利用UUID产生随机的文件名*//*
        filename = UUIDUtil.getUUID() + "." + fileType;
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        try {
            *//*将文件上传到服务器的指定位置*//*
            FileUtil.fileUpload(multipartFile, filename);
            //获取服务器中文件的真实路径
            String realPath = "D:\\Java\\crm\\mycrm\\src\\main\\webapp\\upload\\" + filename;
            *//*将刚上传的excel文件导入到数据库*//*
            int count = ExcelUtil.importExcel(realPath, activityService, user);
            if (count > 0) {
                result.setCode(Constants.SUCCESS);
                result.setRetData(String.valueOf(count));
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("系统繁忙，请稍后重试...");
            }
        } catch (IOException e) {
            result.setCode(Constants.FAIL);
            result.setMessage("系统繁忙，请稍后重试...");
            throw new RuntimeException(e);
        }
        return result;
    }*/

    //优化文件导入，不写入磁盘，而是利用multipartFile获取文件输出流。
    @RequestMapping("/importActivity.do")
    @ResponseBody
    public Object importActivity(@RequestParam("activityFile") MultipartFile multipartFile, HttpSession session) {
        Result result = new Result();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        try {
            //获取输入流
            InputStream inputStream = multipartFile.getInputStream();
            /*将刚上传的excel文件导入到数据库*/
            int count = ExcelUtil.importExcel(inputStream, activityService, user);
            if (count > 0) {
                result.setCode(Constants.SUCCESS);
                result.setRetData(String.valueOf(count));
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("系统繁忙，请稍后重试...");
            }
        } catch (IOException e) {
            result.setCode(Constants.FAIL);
            result.setMessage("系统繁忙，请稍后重试...");
            throw new RuntimeException(e);
        }
        return result;
    }

    @RequestMapping("/detailActivity.do/{id}")
    public String detailActivity(HttpServletRequest request, @PathVariable("id") String id) {
        Activity activity = activityService.queryActivityById(id);
        List<ActivityRemark> activityRemarks = activityRemarkService.queryAllActivityRemarkByActivityId(id);
        request.setAttribute("activity", activity);
        request.setAttribute("remarkList", activityRemarks);
        return "workbench/activity/detail";
    }

    @RequestMapping("/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(String noteContent, String activityId, HttpSession session) {
        Result result = new Result();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        ActivityRemark activityRemark = new ActivityRemark();
        try {
            activityRemark.setNoteContent(noteContent);
            activityRemark.setActivityId(activityId);
            //设置评论创建者
            activityRemark.setCreateBy(user.getId());
            /*设置评论创建时间*/
            activityRemark.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
            /*设置评论id*/
            activityRemark.setId(UUIDUtil.getUUID());
            /*设置flag*/
            activityRemark.setEditFlag(Constants.REMARK_EDIT_FLAG_NO);
            int count = activityRemarkService.saveActivityRemark(activityRemark);
            if(count > 0) {
                result.setMessage("保存成功!");
                result.setCode(Constants.SUCCESS);
                result.setRetData(activityRemark);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("保存失败!");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("保存失败!");
        }
        return result;
    }

    @RequestMapping("/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id) {
        Result result = new Result();
        try {
            int i = activityRemarkService.deleteActivityRemarkById(id);
            if(i > 0) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("删除失败!");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("删除失败!");
        }
        return result;
    }

    @RequestMapping("/saveEditActivityRemark.do")
    @ResponseBody
    public Object saveEditActivityRemark(String id, String noteContent, HttpSession session) {
        Result result = new Result();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        ActivityRemark activityRemark = activityRemarkService.queryActivityRemarkById(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditBy(user.getId());
        activityRemark.setEditTime(DateFormatUtil.formatDateAndTime(new Date()));
        activityRemark.setEditFlag(Constants.REMARK_EDIT_FLAG_YES);
        try {
            int i = activityRemarkService.saveEditActivityRemark(activityRemark);
            if(i > 0) {
                result.setCode(Constants.SUCCESS);
                result.setRetData(activityRemark);
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

}
