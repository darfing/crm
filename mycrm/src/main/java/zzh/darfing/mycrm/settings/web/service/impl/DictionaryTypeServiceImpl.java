package zzh.darfing.mycrm.settings.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzh.darfing.mycrm.settings.mapper.DicValueMapper;
import zzh.darfing.mycrm.settings.mapper.DictionaryTypeMapper;
import zzh.darfing.mycrm.settings.pojo.DictionaryType;
import zzh.darfing.mycrm.settings.web.service.DictionaryTypeService;

import java.util.List;

@Service
public class DictionaryTypeServiceImpl implements DictionaryTypeService {
    @Autowired
    DictionaryTypeMapper dictionaryTypeMapper;
    @Override
    public List<DictionaryType> queryAllDictionaryType() {
        return dictionaryTypeMapper.selectAllDictionaryType();
    }

    @Override
    public int saveEditDictionaryType(DictionaryType dictionaryType) {
        return dictionaryTypeMapper.updateEditDictionaryType(dictionaryType);
    }

    @Override
    public int deleteDictionaryTypeByCodes(String[] codes) {
        return dictionaryTypeMapper.deleteByCodes(codes);
    }

    @Override
    public int saveCreateDictionaryType(DictionaryType dictionaryType) {
        return dictionaryTypeMapper.insert(dictionaryType);
    }

    @Override
    public DictionaryType queryDictionaryTypeByCode(String code) {
        return dictionaryTypeMapper.selectByPrimaryKey(code);
    }
}
