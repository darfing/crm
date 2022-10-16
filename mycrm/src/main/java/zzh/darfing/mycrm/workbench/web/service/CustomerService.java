package zzh.darfing.mycrm.workbench.web.service;

import com.github.pagehelper.PageInfo;
import zzh.darfing.mycrm.workbench.pojo.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    PageInfo<Customer> queryAllCustomersForPageAndByCondition(Map<String, Object> map);

    Customer queryCustomerDetailById(String id);

    int saveCreateCustomer(Customer customer);

    int saveEditCustomer(Customer customer);

    int deleteCustomersByIds(String[] ids);

    List<Customer> queryAllCustomer();

    List<String> queryCustomerNameByName(String name);
}
