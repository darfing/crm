package zzh.darfing.mycrm.workbench.web.service;

import com.github.pagehelper.PageInfo;
import zzh.darfing.mycrm.settings.pojo.User;
import zzh.darfing.mycrm.workbench.pojo.Contacts;

import java.util.List;
import java.util.Map;

public interface ContactsService {
    PageInfo<Contacts> queryContactsForPageAndByCondition(Map<String, Object> map);

    Contacts queryContactsDetailById(String id);

    int saveCreateContacts(Contacts contacts, User user);

    int saveEditContacts(Contacts contacts, User user);

    int deleteContactsByIds(String[] ids);

    List<Contacts> queryAllContacts();
    List<Contacts> queryContactsByName(String name);
}
