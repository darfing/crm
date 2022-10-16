package zzh.darfing.mycrm.workbench.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzh.darfing.mycrm.workbench.mapper.CustomerMapper;
import zzh.darfing.mycrm.workbench.pojo.Customer;
import zzh.darfing.mycrm.workbench.web.service.CustomerService;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public PageInfo<Customer> queryAllCustomersForPageAndByCondition(Map<String, Object> map) {
        PageHelper.startPage(Integer.parseInt((String) map.get("pageNo")), Integer.parseInt((String) map.get("pageSize")));
        return new PageInfo<>(customerMapper.selectAllCustomersForPageAndByCondition(map), 5);
    }

    @Override
    public Customer queryCustomerDetailById(String id) {
        return customerMapper.selectCustomerDetailById(id);
    }

    @Override
    public int saveCreateCustomer(Customer customer) {
        return customerMapper.insertCreateCustomer(customer);
    }

    @Override
    public int saveEditCustomer(Customer customer) {
        return customerMapper.updateEditCustomer(customer);
    }

    @Override
    public int deleteCustomersByIds(String[] ids) {
        return customerMapper.deleteCustomersByIds(ids);
    }

    @Override
    public List<Customer> queryAllCustomer() {
        return customerMapper.selectAllCustomer();
    }

    @Override
    public List<String> queryCustomerNameByName(String name) {
        return customerMapper.selectCustomerNameByName(name);
    }
}
