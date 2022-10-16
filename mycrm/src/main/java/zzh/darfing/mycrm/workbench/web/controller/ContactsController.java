package zzh.darfing.mycrm.workbench.web.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import zzh.darfing.mycrm.commons.constants.Constants;
import zzh.darfing.mycrm.commons.pojo.Result;
import zzh.darfing.mycrm.commons.utils.DateFormatUtil;
import zzh.darfing.mycrm.commons.utils.UUIDUtil;
import zzh.darfing.mycrm.settings.pojo.DicValue;
import zzh.darfing.mycrm.settings.pojo.User;
import zzh.darfing.mycrm.settings.web.service.DictionaryValueService;
import zzh.darfing.mycrm.settings.web.service.UserService;
import zzh.darfing.mycrm.workbench.pojo.Contacts;
import zzh.darfing.mycrm.workbench.pojo.Customer;
import zzh.darfing.mycrm.workbench.web.service.ContactsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/contacts")
public class ContactsController {
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private UserService userService;
    @Autowired
    private DictionaryValueService dictionaryValueService;

    @RequestMapping("/index.do")
    public String index(HttpServletRequest request) {
        List<User> users = userService.queryAllUsers();
        List<DicValue> appellationList = dictionaryValueService.queryAllDicValueByTypeCode("appellation");
        List<DicValue> sources = dictionaryValueService.queryAllDicValueByTypeCode("source");
        request.setAttribute("userList", users);
        request.setAttribute("sourceList", sources);
        request.setAttribute("appellationList", appellationList);
        return "workbench/contacts/index";
    }

    @RequestMapping("/queryContactsForPageAndByCondition.do")
    @ResponseBody
    public Object queryContactsForPageAndByCondition(@RequestParam Map<String, Object> map) {
        return contactsService.queryContactsForPageAndByCondition(map);
    }

    @RequestMapping("/saveCreateContacts.do")
    @ResponseBody
    public Object saveCreateContacts(Contacts contacts, HttpSession session) {
        Result result = new Result();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        contacts.setId(UUIDUtil.getUUID());
        contacts.setCreateBy(user.getId());
        contacts.setOwner(user.getId());
        contacts.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
        try {
            int i = contactsService.saveCreateContacts(contacts, user);
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

    @RequestMapping("/queryContactsDetailById.do")
    @ResponseBody
    public Object queryContactsDetailById(String id) {
        return contactsService.queryContactsDetailById(id);
    }

    @RequestMapping("/saveEditContacts.do")
    @ResponseBody
    public Object saveEditContacts(Contacts contacts, HttpSession session) {
        Result result = new Result();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        contacts.setEditBy(user.getId());
        contacts.setEditTime(DateFormatUtil.formatDateAndTime(new Date()));
        try {
            int i = contactsService.saveEditContacts(contacts, user);
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

    @RequestMapping("/deleteContacts.do")
    @ResponseBody
    public Object deleteContacts(String[] id) {
        Result result = new Result();
        try {
            int i = contactsService.deleteContactsByIds(id);
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
