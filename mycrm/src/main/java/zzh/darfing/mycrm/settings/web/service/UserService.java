package zzh.darfing.mycrm.settings.web.service;

import zzh.darfing.mycrm.settings.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User queryUserByLoginActAndPwd(Map map);
    List<User> queryAllUsers();
}
