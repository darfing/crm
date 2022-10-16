package zzh.darfing.mycrm.workbench.web.controller;

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
import zzh.darfing.mycrm.workbench.pojo.Activity;
import zzh.darfing.mycrm.workbench.pojo.Contacts;
import zzh.darfing.mycrm.workbench.pojo.Customer;
import zzh.darfing.mycrm.workbench.pojo.Tran;
import zzh.darfing.mycrm.workbench.web.service.ActivityService;
import zzh.darfing.mycrm.workbench.web.service.ContactsService;
import zzh.darfing.mycrm.workbench.web.service.CustomerService;
import zzh.darfing.mycrm.workbench.web.service.TranService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/transaction")
public class TransactionController {
    @Autowired
    private TranService tranService;
    @Autowired
    private DictionaryValueService dictionaryValueService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("/index.do")
    public String index(HttpServletRequest request) {
        //查询所有客户
        List<Customer> customers = customerService.queryAllCustomer();
        /*查询所有用户*/
        List<User> users = userService.queryAllUsers();
        /*查询所有联系人*/
        List<Contacts> contacts = contactsService.queryAllContacts();
        /*查询所有stage*/
        List<DicValue> stageList = dictionaryValueService.queryAllDicValueByTypeCode("stage");
        /*查询所有source*/
        List<DicValue> sourceList = dictionaryValueService.queryAllDicValueByTypeCode("source");
        /*查询所有transactionType*/
        List<DicValue> transactionTypeList = dictionaryValueService.queryAllDicValueByTypeCode("transactionType");
        request.setAttribute("customers", customers);
        request.setAttribute("contacts", contacts);
        request.setAttribute("users", users);
        request.setAttribute("stageList", stageList);
        request.setAttribute("sourceList", sourceList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        return "workbench/transaction/index";
    }

    @RequestMapping("/queryTransactionForPageAndByCondition.do")
    @ResponseBody
    public Object queryTransactionForPageAndByCondition(@RequestParam Map<String, Object> map) {
        return tranService.queryTranForPageAndByCondition(map);
    }

    @RequestMapping("/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(Tran tran, HttpSession session) {
        Result result = new Result();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        tran.setId(UUIDUtil.getUUID());
        tran.setCreateBy(user.getId());
        tran.setOwner(user.getId());
        tran.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
        try {
            int i = tranService.saveCreateTran(tran, user);
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

    @RequestMapping("/queryTranDetailById.do")
    @ResponseBody
    public Object queryTranDetailById(String id) {
        return tranService.queryTranDetailById(id);
    }

    @RequestMapping("/saveEditTran.do")
    @ResponseBody
    public Object saveEditTran(Tran tran, HttpSession session) {
        Result result = new Result();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        tran.setEditBy(user.getId());
        tran.setEditTime(DateFormatUtil.formatDateAndTime(new Date()));
        try {
            int i = tranService.saveEditTran(tran, user);
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

    @RequestMapping("/deleteTran.do")
    @ResponseBody
    public Object deleteTran(String[] id) {
        Result result = new Result();
        try {
            int i = tranService.deleteTranByIds(id);
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

    @RequestMapping("/toSave.do")
    public String toSave(HttpServletRequest request) {
        /*查询所有用户*/
        List<User> users = userService.queryAllUsers();
        /*查询所有联系人*/
        List<Contacts> contacts = contactsService.queryAllContacts();
        /*查询所有stage*/
        List<DicValue> stageList = dictionaryValueService.queryAllDicValueByTypeCode("stage");
        /*查询所有source*/
        List<DicValue> sourceList = dictionaryValueService.queryAllDicValueByTypeCode("source");
        /*查询所有transactionType*/
        List<DicValue> transactionTypeList = dictionaryValueService.queryAllDicValueByTypeCode("transactionType");
        request.setAttribute("contacts", contacts);
        request.setAttribute("users", users);
        request.setAttribute("stageList", stageList);
        request.setAttribute("sourceList", sourceList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        return "workbench/transaction/save";
    }

    @RequestMapping("/queryPossibility.do")
    @ResponseBody
    public Object queryPossibility(String id) {
        return dictionaryValueService.queryDicValueById(id);
    }

    @RequestMapping("/queryActivityByName.do")
    @ResponseBody
    public Object queryActivityByName(String name) {
        Result result = new Result();
        try {
            List<Activity> activityList = activityService.queryActivityByName(name);
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
    }
    @RequestMapping("/queryContactsByName.do")
    @ResponseBody
    public Object queryContactsByName(String name) {
        Result result = new Result();
        try {
            List<Contacts> contacts = contactsService.queryContactsByName(name);
            if(null != contacts) {
                result.setCode(Constants.SUCCESS);
                result.setRetData(contacts);
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

    @RequestMapping("/queryCustomerNameByName.do")
    @ResponseBody
    public Object queryCustomerNameByName(String customerName) {
        return customerService.queryCustomerNameByName(customerName);
    }
}
