package zzh.darfing.mycrm.workbench.mapper;

import org.apache.ibatis.annotations.Param;
import zzh.darfing.mycrm.workbench.pojo.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbg.generated Sun Oct 09 12:36:36 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbg.generated Sun Oct 09 12:36:36 CST 2022
     */
    int insert(ClueActivityRelation row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbg.generated Sun Oct 09 12:36:36 CST 2022
     */
    int insertSelective(ClueActivityRelation row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbg.generated Sun Oct 09 12:36:36 CST 2022
     */
    ClueActivityRelation selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbg.generated Sun Oct 09 12:36:36 CST 2022
     */
    int updateByPrimaryKeySelective(ClueActivityRelation row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbg.generated Sun Oct 09 12:36:36 CST 2022
     */
    int updateByPrimaryKey(ClueActivityRelation row);

    List<String> selectActivityIdByClueId(String clueId);

    int insertBounds(@Param("list") List<ClueActivityRelation> list);

    int deleteBoundByActivityIdAndClueId(@Param("activityId") String activityId, @Param("clueId") String clueId);

    int deleteRelationsByClueId(String clueId);
}