package zzh.darfing.mycrm.settings.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzh.darfing.mycrm.settings.mapper.DicValueMapper;
import zzh.darfing.mycrm.settings.pojo.DicValue;
import zzh.darfing.mycrm.settings.web.service.DictionaryValueService;

import java.util.List;

@Service
public class DictionaryValueServiceImpl implements DictionaryValueService {
    @Autowired
    private DicValueMapper dicValueMapper;
    @Override
    public List<DicValue> queryAllDicValue() {
        return dicValueMapper.selectAllDicValue();
    }

    @Override
    public DicValue queryDicValueById(String id) {
        return dicValueMapper.selectByPrimaryKey(id);
    }

    @Override
    public int saveEditDicValue(DicValue dicValue) {
        return dicValueMapper.updateEditDicValue(dicValue);
    }

    @Override
    public int saveCreateDicValue(DicValue dicValue) {
        return dicValueMapper.insert(dicValue);
    }

    @Override
    public int deleteDicValueByIds(String[] ids) {
        return dicValueMapper.deleteByIds(ids);
    }

    @Override
    public List<DicValue> queryAllDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectAllDicValueByTypeCode(typeCode);
    }
}
