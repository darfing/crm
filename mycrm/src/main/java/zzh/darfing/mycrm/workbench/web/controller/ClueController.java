package zzh.darfing.mycrm.workbench.web.controller;

import com.github.pagehelper.PageInfo;
import com.sun.org.apache.bcel.internal.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zzh.darfing.mycrm.commons.constants.Constants;
import zzh.darfing.mycrm.commons.pojo.Result;
import zzh.darfing.mycrm.commons.utils.DateFormatUtil;
import zzh.darfing.mycrm.commons.utils.UUIDUtil;
import zzh.darfing.mycrm.settings.pojo.DicValue;
import zzh.darfing.mycrm.settings.pojo.User;
import zzh.darfing.mycrm.settings.web.service.DictionaryValueService;
import zzh.darfing.mycrm.settings.web.service.UserService;
import zzh.darfing.mycrm.workbench.pojo.Clue;
import zzh.darfing.mycrm.workbench.web.service.ClueService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController {
    @Autowired
    private ClueService clueService;

    @Autowired
    private UserService userService;
    @Autowired
    private DictionaryValueService dictionaryValueService;

    @RequestMapping("/index.do")
    public String index(HttpServletRequest request) {
        List<User> users = userService.queryAllUsers();
        List<DicValue> ClueStatus = dictionaryValueService.queryAllDicValueByTypeCode("clueState");
        List<DicValue> appellationList = dictionaryValueService.queryAllDicValueByTypeCode("appellation");
        List<DicValue> sources = dictionaryValueService.queryAllDicValueByTypeCode("source");
        request.setAttribute("userList", users);
        request.setAttribute("clueStateList", ClueStatus);
        request.setAttribute("sourceList", sources);
        request.setAttribute("appellationList", appellationList);
        return "workbench/clue/index";
    }

    @RequestMapping("/queryClueByConditionForPage.do")
    @ResponseBody
    public Object queryClueByConditionForPage(int pageNo, int pageSize,
                                              String fullname, String company,
                                              String mphone, String phone,
                                              String owner, String source, String state) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("fullname", fullname);
        map.put("company", company);
        map.put("mphone", mphone);
        map.put("phone", phone);
        map.put("owner", owner);
        map.put("source", source);
        map.put("state", state);
        return clueService.queryAllClueByConditionForPage(map);
    }

    @RequestMapping("/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session) {
        Result result = new Result();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateBy(user.getId());
        clue.setOwner(user.getId());
        clue.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
        try {
            int i = clueService.saveCreateClue(clue);
            if(i > 0) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("服务器繁忙,请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙,请稍后重试...");
        }
        return result;
    }

    @RequestMapping("/queryClueById.do")
    @ResponseBody
    public Object queryClueById(String id) {
        return clueService.queryClueById(id);
    }

    @RequestMapping("/saveEditClue.do")
    @ResponseBody
    public Object saveEditClue(Clue clue, HttpSession session) {
        Result result = new Result();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        clue.setEditBy(user.getId());
        clue.setEditTime(DateFormatUtil.formatDateAndTime(new Date()));
        try {
            int i = clueService.updateEditClue(clue);
            if(i > 0) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("服务器繁忙,请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙,请稍后重试...");
        }
        return result;
    }

    @RequestMapping("/deleteClue.do")
    @ResponseBody
    public Object deleteClue(String[] id) {
        Result result = new Result();
        try {
            int i = clueService.deleteClueByIds(id);
            if(i > 0) {
                result.setCode(Constants.SUCCESS);
            } else {
                result.setCode(Constants.FAIL);
                result.setMessage("服务器繁忙,请稍后重试...");
            }
        } catch (Exception e) {
            result.setCode(Constants.FAIL);
            result.setMessage("服务器繁忙,请稍后重试...");
        }
        return result;
    }
}
