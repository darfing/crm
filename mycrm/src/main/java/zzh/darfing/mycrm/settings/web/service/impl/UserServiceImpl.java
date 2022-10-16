package zzh.darfing.mycrm.settings.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzh.darfing.mycrm.settings.mapper.UserMapper;
import zzh.darfing.mycrm.settings.pojo.User;
import zzh.darfing.mycrm.settings.web.service.UserService;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public User queryUserByLoginActAndPwd(Map map) {
        return userMapper.selectUserByLoginActAndPwd(map);
    }

    @Override
    public List<User> queryAllUsers() {
        return userMapper.selectAllUsers();
    }
}
