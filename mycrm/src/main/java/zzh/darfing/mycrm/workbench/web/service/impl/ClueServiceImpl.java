package zzh.darfing.mycrm.workbench.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzh.darfing.mycrm.commons.constants.Constants;
import zzh.darfing.mycrm.commons.utils.DateFormatUtil;
import zzh.darfing.mycrm.commons.utils.UUIDUtil;
import zzh.darfing.mycrm.settings.pojo.User;
import zzh.darfing.mycrm.workbench.mapper.*;
import zzh.darfing.mycrm.workbench.pojo.*;
import zzh.darfing.mycrm.workbench.web.service.ClueService;

import java.util.*;

@Service
@Transactional
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public List<Clue> queryAllClue() {
        return clueMapper.selectAllClue();
    }

    @Override
    public PageInfo<Clue> queryAllClueByConditionForPage(Map<String, Object> map) {
        PageHelper.startPage((Integer) map.get("pageNo"), (Integer) map.get("pageSize"));
        return new PageInfo<>(clueMapper.selectAllClueByConditionForPage(map), 5);
    }

    @Override
    public int deleteClueByIds(String[] ids) {
        return clueMapper.deleteClueByIds(ids);
    }

    @Override
    public int updateEditClue(Clue clue) {
        return clueMapper.updateClue(clue);
    }

    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);
    }

    /**数据转换:
     把线索中有关公司的信息转换到客户表中
     把线索中有关个人的信息转换到联系人表中
     把线索的备注信息转换到客户备注表中一份
     把线索的备注信息转换到联系人备注表中一份
     把线索和市场活动的关联关系转换到联系人和市场活动的关联关系表中
     如果需要创建交易,还要往交易表中添加一条记录
     如果需要创建交易,还要把线索的备注信息转换到交易备注表中一份
     删除线索的备注
     删除线索和市场活动的关联关系
     删除线索*/
    @Override
    public void saveConvert(Map<String, Object> map) {
        User user = (User) map.get(Constants.SESSION_USER);
        /*1、把线索中有关公司的信息转换到客户表中*/
        Clue clue = clueMapper.selectByPrimaryKey((String) map.get("clueId"));
        Customer customer = new Customer();
        customer.setId(UUIDUtil.getUUID());
        customer.setOwner(user.getId());
        customer.setName(clue.getCompany());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setDescription(clue.getDescription());
        customer.setAddress(clue.getAddress());
        customerMapper.insert(customer);
        /*2、把线索中有关个人的信息转换到联系人表中*/
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());
        contactsMapper.insert(contacts);
        /*3、把线索的备注信息转换到客户备注表中一份*/
        List<ClueRemark> clueRemarks = clueRemarkMapper.selectClueRemarksByClueId((String) map.get("clueId"));
        List<CustomerRemark> customerRemarks = new LinkedList<>();
        List<ContactsRemark> contactsRemarks = new LinkedList<>();
        for (ClueRemark clueRemark : clueRemarks) {
            CustomerRemark customerRemark = new CustomerRemark();
            ContactsRemark contactsRemark = new ContactsRemark();
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setId(clueRemark.getId());
            customerRemark.setNoteContent(clueRemark.getNoteContent());
            customerRemark.setCreateBy(clueRemark.getCreateBy());
            customerRemark.setCreateTime(clueRemark.getCreateTime());
            customerRemark.setEditFlag(clueRemark.getEditFlag());
            customerRemarks.add(customerRemark);

            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setId(clueRemark.getId());
            contactsRemark.setNoteContent(clueRemark.getNoteContent());
            contactsRemark.setCreateBy(clueRemark.getCreateBy());
            contactsRemark.setCreateTime(clueRemark.getCreateTime());
            contactsRemark.setEditFlag(clueRemark.getEditFlag());
            contactsRemarks.add(contactsRemark);
        }
        customerRemarkMapper.insertCustomerRemarkFromClueRemark(customerRemarks);
        /*4、把线索的备注信息转换到联系人备注表中一份*/
        contactsRemarkMapper.insertContactsRemarkFromClueRemark(contactsRemarks);
        /*5、把线索和市场活动的关联关系转换到联系人和市场活动的关联关系表中*/
        String contactsId = contacts.getId();
        List<String> activityIds = clueActivityRelationMapper.selectActivityIdByClueId((String) map.get("clueId"));
        for (String activityId : activityIds) {
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contactsId);
            contactsActivityRelationMapper.insert(contactsActivityRelation);
        }
        /*6、如果需要创建交易,还要往交易表中添加一条记录 */
        if(Boolean.parseBoolean((String) map.get("isCreateTran"))) {
            Tran tran = new Tran();
            tran.setId(UUIDUtil.getUUID());
            tran.setOwner(user.getId());
            tran.setMoney((String) map.get("money"));
            tran.setName((String) map.get("name"));
            tran.setExpectedDate((String) map.get("expectedDate"));
            tran.setCustomerId(customer.getId());
            tran.setStage((String) map.get("stage"));
            /*tran.setType();*/
            tran.setSource(clue.getSource());
            tran.setActivityId((String) map.get("activityId"));
            tran.setContactsId(contactsId);
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
            tran.setContactSummary(clue.getContactSummary());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tranMapper.insert(tran);
            List<TranRemark> tranRemarks = new LinkedList<>();
            for (ClueRemark clueRemark : clueRemarks) {
                TranRemark tranRemark = new TranRemark();
                tranRemark.setId(UUIDUtil.getUUID());
                tranRemark.setTranId(tran.getId());
                tranRemark.setCreateBy(clueRemark.getCreateBy());
                tranRemark.setCreateTime(clueRemark.getCreateTime());
                tranRemark.setNoteContent(clueRemark.getNoteContent());
                tranRemark.setEditFlag(clueRemark.getEditFlag());
                tranRemarks.add(tranRemark);
            }
            /*7、如果需要创建交易,还要把线索的备注信息转换到交易备注表中一份*/
            tranRemarkMapper.insertTranRemarkFromClueRemark(tranRemarks);
            clueRemarkMapper.deleteClueRemarkById(clue.getId());
            clueActivityRelationMapper.deleteRelationsByClueId(clue.getId());
            clueMapper.deleteByPrimaryKey(clue.getId());
        }
    }
}
