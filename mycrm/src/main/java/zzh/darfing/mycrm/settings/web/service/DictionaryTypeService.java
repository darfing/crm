package zzh.darfing.mycrm.settings.web.service;

import zzh.darfing.mycrm.settings.pojo.DictionaryType;

import java.util.List;
import java.util.Map;

public interface DictionaryTypeService {
    List<DictionaryType> queryAllDictionaryType();

    int saveEditDictionaryType(DictionaryType dictionaryType);

    int deleteDictionaryTypeByCodes(String[] codes);

    int saveCreateDictionaryType(DictionaryType dictionaryType);

    DictionaryType queryDictionaryTypeByCode(String code);
}
