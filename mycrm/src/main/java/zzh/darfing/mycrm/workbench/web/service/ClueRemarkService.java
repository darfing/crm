package zzh.darfing.mycrm.workbench.web.service;

import zzh.darfing.mycrm.workbench.pojo.Clue;
import zzh.darfing.mycrm.workbench.pojo.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    /*    List<ClueRemark> selectClueRemarksByClueId(String clueId);

    int insertClueRemark(Clue clue);

    int deleteClueRemarkById(String id);

    int updateClueRemark(Clue clue);

    Clue selectClueById(String id);*/

    List<ClueRemark> queryClueRemarksByClueId(String clueId);

    int saveClueRemark(ClueRemark clueRemark);

    int deleteClueRemarkById(String id);

    int saveEditClueRemark(ClueRemark clueRemark);

    Clue queryClueById(String id);

}
