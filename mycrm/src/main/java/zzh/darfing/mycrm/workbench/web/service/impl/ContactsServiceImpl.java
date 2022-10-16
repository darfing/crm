package zzh.darfing.mycrm.workbench.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzh.darfing.mycrm.commons.utils.DateFormatUtil;
import zzh.darfing.mycrm.commons.utils.UUIDUtil;
import zzh.darfing.mycrm.settings.pojo.User;
import zzh.darfing.mycrm.workbench.mapper.ContactsMapper;
import zzh.darfing.mycrm.workbench.mapper.CustomerMapper;
import zzh.darfing.mycrm.workbench.pojo.Contacts;
import zzh.darfing.mycrm.workbench.pojo.Customer;
import zzh.darfing.mycrm.workbench.web.service.ContactsService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public PageInfo<Contacts> queryContactsForPageAndByCondition(Map<String, Object> map) {
        PageHelper.startPage(Integer.parseInt((String) map.get("pageNo")), Integer.parseInt((String) map.get("pageSize")));
        List<Contacts> contacts = contactsMapper.selectContactsForPageAndByCondition(map);
        return new PageInfo<>(contacts, 5);
    }

    @Override
    public Contacts queryContactsDetailById(String id) {
        return contactsMapper.selectContactsDetailById(id);
    }

    @Override
    public int saveCreateContacts(Contacts contacts, User user) {
        Customer customer = customerMapper.selectCustomerByName(contacts.getCustomerId());
        if(null!=customer) {
            //保存该联系人
            contacts.setCustomerId(customer.getId());
            return contactsMapper.insertCreateContacts(contacts);
        } else {
            /*创建一个客户*/
            Customer createCustomer = new Customer();
            createCustomer.setId(UUIDUtil.getUUID());
            createCustomer.setOwner(user.getId());
            createCustomer.setName(contacts.getCustomerId());
            createCustomer.setCreateBy(user.getId());
            createCustomer.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
            createCustomer.setContactSummary(contacts.getContactSummary());
            createCustomer.setNextContactTime(contacts.getNextContactTime());
            createCustomer.setDescription(contacts.getDescription());
            createCustomer.setAddress(contacts.getAddress());
            customerMapper.insert(createCustomer);
            contacts.setCustomerId(createCustomer.getId());
            return contactsMapper.insertCreateContacts(contacts);
        }
    }

    @Override
    public int saveEditContacts(Contacts contacts, User user) {
        Customer customer = customerMapper.selectCustomerByName(contacts.getCustomerId());
        if(null!=customer) {
            contacts.setCustomerId(customer.getId());
            return contactsMapper.updateEditContacts(contacts);
        } else {
            /*创建一个客户*/
            Customer createCustomer = new Customer();
            createCustomer.setId(UUIDUtil.getUUID());
            createCustomer.setOwner(user.getId());
            createCustomer.setName(contacts.getCustomerId());
            createCustomer.setCreateBy(user.getId());
            createCustomer.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
            createCustomer.setContactSummary(contacts.getContactSummary());
            createCustomer.setNextContactTime(contacts.getNextContactTime());
            createCustomer.setDescription(contacts.getDescription());
            createCustomer.setAddress(contacts.getAddress());
            customerMapper.insert(createCustomer);
            contacts.setCustomerId(createCustomer.getId());
            return contactsMapper.updateEditContacts(contacts);
        }

    }

    @Override
    public int deleteContactsByIds(String[] ids) {
        return contactsMapper.deleteContactsByIds(ids);
    }

    @Override
    public List<Contacts> queryAllContacts() {
        return contactsMapper.selectAllContacts();
    }

    @Override
    public List<Contacts> queryContactsByName(String name) {
        return contactsMapper.selectContactsByName(name);
    }
}
