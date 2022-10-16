package zzh.darfing.mycrm.workbench.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzh.darfing.mycrm.workbench.mapper.ClueRemarkMapper;
import zzh.darfing.mycrm.workbench.pojo.Clue;
import zzh.darfing.mycrm.workbench.pojo.ClueRemark;
import zzh.darfing.mycrm.workbench.web.service.ClueRemarkService;

import java.util.List;

@Service
@Transactional
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Override
    public List<ClueRemark> queryClueRemarksByClueId(String clueId) {
        return clueRemarkMapper.selectClueRemarksByClueId(clueId);
    }

    @Override
    public int saveClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.insertClueRemark(clueRemark);
    }

    @Override
    public int deleteClueRemarkById(String id) {
        return clueRemarkMapper.deleteClueRemarkById(id);
    }

    @Override
    public int saveEditClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.updateClueRemark(clueRemark);
    }

    @Override
    public Clue queryClueById(String id) {
        return clueRemarkMapper.selectClueById(id);
    }
}
