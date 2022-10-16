package zzh.darfing.mycrm.workbench.mapper;

import org.apache.ibatis.annotations.Param;
import zzh.darfing.mycrm.workbench.pojo.ActivityRemark;

import java.util.List;

public interface ActivityRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Fri Oct 07 12:43:59 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Fri Oct 07 12:43:59 CST 2022
     */
    int insert(ActivityRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Fri Oct 07 12:43:59 CST 2022
     */
    int insertSelective(ActivityRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Fri Oct 07 12:43:59 CST 2022
     */
    ActivityRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Fri Oct 07 12:43:59 CST 2022
     */
    int updateByPrimaryKeySelective(ActivityRemark row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Fri Oct 07 12:43:59 CST 2022
     */
    int updateByPrimaryKey(ActivityRemark row);

    int insertActivityRemark(ActivityRemark activityRemark);

    List<ActivityRemark> selectAllActivityRemarkById(String id);

    int updateActivityRemark(ActivityRemark activityRemark);
}