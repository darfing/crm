package zzh.darfing.mycrm.workbench.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzh.darfing.mycrm.commons.utils.DateFormatUtil;
import zzh.darfing.mycrm.commons.utils.UUIDUtil;
import zzh.darfing.mycrm.settings.pojo.User;
import zzh.darfing.mycrm.workbench.mapper.CustomerMapper;
import zzh.darfing.mycrm.workbench.mapper.TranMapper;
import zzh.darfing.mycrm.workbench.pojo.Customer;
import zzh.darfing.mycrm.workbench.pojo.Tran;
import zzh.darfing.mycrm.workbench.web.service.TranService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TranServiceImpl implements TranService {
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public PageInfo<Tran> queryTranForPageAndByCondition(Map<String, Object> map) {
        PageHelper.startPage(Integer.parseInt((String) map.get("pageNo")), Integer.parseInt((String) map.get("pageSize")));
        List<Tran> trans = tranMapper.selectTranForPageAndByCondition(map);
        return new PageInfo<>(trans, 5);
    }

    @Override
    public Tran queryTranDetailById(String id) {
        return tranMapper.selectTranDetailById(id);
    }

    @Override
    public int saveCreateTran(Tran tran, User user) {
        Customer customer = customerMapper.selectCustomerByName(tran.getCustomerId());
        if(null != customer) {
            tran.setCustomerId(customer.getId());
            return tranMapper.insertCreateTran(tran);
        } else {
            /*创建一个客户*/
            Customer createCustomer = new Customer();
            createCustomer.setId(UUIDUtil.getUUID());
            createCustomer.setOwner(user.getId());
            createCustomer.setName(tran.getCustomerId());
            createCustomer.setCreateBy(user.getId());
            createCustomer.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
            createCustomer.setContactSummary(tran.getContactSummary());
            createCustomer.setNextContactTime(tran.getNextContactTime());
            createCustomer.setDescription(tran.getDescription());
            customerMapper.insert(createCustomer);
            tran.setCustomerId(createCustomer.getId());
            return tranMapper.insertCreateTran(tran);
        }
    }

    @Override
    public int saveEditTran(Tran tran, User user) {
        Customer customer = customerMapper.selectCustomerByName(tran.getCustomerId());
        if(null != customer) {
            tran.setCustomerId(customer.getId());
            return tranMapper.updateEditTran(tran);
        } else {
            /*创建一个客户*/
            Customer createCustomer = new Customer();
            createCustomer.setId(UUIDUtil.getUUID());
            createCustomer.setOwner(user.getId());
            createCustomer.setName(tran.getCustomerId());
            createCustomer.setCreateBy(user.getId());
            createCustomer.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
            createCustomer.setContactSummary(tran.getContactSummary());
            createCustomer.setNextContactTime(tran.getNextContactTime());
            createCustomer.setDescription(tran.getDescription());
            customerMapper.insert(createCustomer);
            tran.setCustomerId(createCustomer.getId());
            return tranMapper.updateEditTran(tran);
        }
    }

    @Override
    public int deleteTranByIds(String[] ids) {
        return tranMapper.deleteTranByIds(ids);
    }
}
