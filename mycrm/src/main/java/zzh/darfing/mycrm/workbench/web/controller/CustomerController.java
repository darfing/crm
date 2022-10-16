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
import zzh.darfing.mycrm.settings.pojo.User;
import zzh.darfing.mycrm.settings.web.service.UserService;
import zzh.darfing.mycrm.workbench.pojo.Customer;
import zzh.darfing.mycrm.workbench.web.service.CustomerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @RequestMapping("/index.do")
    public String index(HttpServletRequest request) {
        List<User> users = userService.queryAllUsers();
        request.setAttribute("userList", users);
        return "workbench/customer/index";
    }

    @RequestMapping("/queryAllCustomersForPageAndByCondition.do")
    @ResponseBody
    public Object queryAllCustomersForPageAndByCondition(@RequestParam Map<String, Object> map) {
        return customerService.queryAllCustomersForPageAndByCondition(map);
    }

    @RequestMapping("/saveCreateCustomer.do")
    @ResponseBody
    public Object saveCreateClue(Customer customer, HttpSession session) {
        Result result = new Result();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        customer.setId(UUIDUtil.getUUID());
        customer.setCreateBy(user.getId());
        customer.setOwner(user.getId());
        customer.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
        try {
            int i = customerService.saveCreateCustomer(customer);
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

    @RequestMapping("/queryCustomerDetailById.do")
    @ResponseBody
    public Object queryCustomerDetailById(String id) {
        return customerService.queryCustomerDetailById(id);
    }

    @RequestMapping("/saveEditCustomer.do")
    @ResponseBody
    public Object saveEditCustomer(Customer customer, HttpSession session) {
        Result result = new Result();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        customer.setEditBy(user.getId());
        customer.setEditTime(DateFormatUtil.formatDateAndTime(new Date()));
        try {
            int i = customerService.saveEditCustomer(customer);
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

    @RequestMapping("/deleteCustomer.do")
    @ResponseBody
    public Object deleteCustomer(String[] id) {
        Result result = new Result();
        try {
            int i = customerService.deleteCustomersByIds(id);
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
