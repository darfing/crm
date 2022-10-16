package zzh.darfing.mycrm.workbench.web.service;

import com.github.pagehelper.PageInfo;
import zzh.darfing.mycrm.workbench.pojo.Activity;
import zzh.darfing.mycrm.workbench.pojo.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    /*
    List<Clue> selectAllClue();

    List<Clue> selectClueByConditions(Map<String, Object> map);

    int deleteClueByIds(@Param("ids") String ids);

    int updateClue(Clue clue);

    int insertClue(Clue clue);*/
    List<Clue> queryAllClue();
    PageInfo<Clue> queryAllClueByConditionForPage(Map<String, Object> map);
    int deleteClueByIds(String[] ids);
    int updateEditClue(Clue clue);
    int saveCreateClue(Clue clue);

    /**
     * 通过id查询线索
     *
     * @param id id
     * @return {@link Clue}
     */
    Clue queryClueById(String id);

    void saveConvert(Map<String, Object> map);
}
