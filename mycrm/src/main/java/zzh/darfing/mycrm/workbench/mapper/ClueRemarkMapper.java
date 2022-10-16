package zzh.darfing.mycrm.workbench.mapper;

import org.apache.ibatis.annotations.Param;
import zzh.darfing.mycrm.workbench.pojo.Clue;
import zzh.darfing.mycrm.workbench.pojo.ClueRemark;

import java.util.List;

public interface ClueRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Sun Oct 09 12:36:36 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Sun Oct 09 12:36:36 CST 2022
     */
    int insert(ClueRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Sun Oct 09 12:36:36 CST 2022
     */
    int insertSelective(ClueRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Sun Oct 09 12:36:36 CST 2022
     */
    ClueRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Sun Oct 09 12:36:36 CST 2022
     */
    int updateByPrimaryKeySelective(ClueRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Sun Oct 09 12:36:36 CST 2022
     */
    int updateByPrimaryKey(ClueRemark row);

    List<ClueRemark> selectClueRemarksByClueId(String clueId);

    int insertClueRemark(ClueRemark clueRemark);

    int deleteClueRemarkById(String id);

    int updateClueRemark(ClueRemark clueRemark);

    Clue selectClueById(String id);

}