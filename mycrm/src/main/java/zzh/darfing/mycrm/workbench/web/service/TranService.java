package zzh.darfing.mycrm.workbench.web.service;

import com.github.pagehelper.PageInfo;
import zzh.darfing.mycrm.settings.pojo.User;
import zzh.darfing.mycrm.workbench.pojo.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    PageInfo<Tran> queryTranForPageAndByCondition(Map<String, Object> map);

    Tran queryTranDetailById(String id);

    int saveCreateTran(Tran tran, User user);

    int saveEditTran(Tran tran, User user);

    int deleteTranByIds(String[] ids);
}
