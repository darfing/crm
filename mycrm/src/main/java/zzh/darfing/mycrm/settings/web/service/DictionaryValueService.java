package zzh.darfing.mycrm.settings.web.service;

import zzh.darfing.mycrm.settings.pojo.DicValue;

import java.util.List;

public interface DictionaryValueService {
    List<DicValue> queryAllDicValue();

    DicValue queryDicValueById(String id);

    int saveEditDicValue(DicValue dicValue);

    int saveCreateDicValue(DicValue dicValue);

    int deleteDicValueByIds(String[] ids);

    List<DicValue> queryAllDicValueByTypeCode(String typeCode);
}
