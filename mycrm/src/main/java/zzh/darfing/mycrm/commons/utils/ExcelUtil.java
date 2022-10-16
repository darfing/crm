package zzh.darfing.mycrm.commons.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import zzh.darfing.mycrm.settings.pojo.User;
import zzh.darfing.mycrm.workbench.pojo.Activity;
import zzh.darfing.mycrm.workbench.web.service.ActivityService;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class ExcelUtil {
    /*记录插入的活动对象个数*/
    private static int activityCounts = 0;
    public static void createExcel(String filename, List<Activity> list, Class clzz) {
        for (int i = 0; i < list.size(); i++) {
            /*将id换为易懂的1,2,3,4...序号*/
            list.get(i).setId("" + (i + 1));
        }
        /**/
        EasyExcel.write(filename, clzz).sheet().doWrite(list);
    }

//    public static int importExcel(String fileName, ActivityService activityService, User user) {
//        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
//        EasyExcel.read(fileName, Activity.class, new ReadListener<Activity>() {
//            /**
//             * 单次缓存的数据量
//             */
//            public static final int BATCH_COUNT = 5;
//            /**
//             *临时存储
//             */
//            private List<Activity> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
//
//            @Override
//            public void invoke(Activity data, AnalysisContext context) {
//                //为活动id赋值
//                data.setId(UUIDUtil.getUUID());
//                data.setOwner(user.getId());
//                data.setEditBy(user.getId());
//                data.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
//                cachedDataList.add(data);
//                if (cachedDataList.size() >= BATCH_COUNT) {
//                    int count = saveData();
//                    activityCounts += count;
//                    // 存储完成清理 list
//                    cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
//                }
//            }
//
//            @Override
//            public void doAfterAllAnalysed(AnalysisContext context) {
//                if(cachedDataList.size() > 0) {
//                    int count = saveData();
//                    activityCounts += count;
//                }
//            }
//
//            /**
//             * 加上存储数据库
//             */
//            private int saveData() {
//                return activityService.saveActivitiesFromExcel(cachedDataList);
//            }
//        }).sheet().doRead();
//        return activityCounts;
//    }

    public static int importExcel(InputStream inputStream, ActivityService activityService, User user) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(inputStream, Activity.class, new ReadListener<Activity>() {
            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 5;
            /**
             *临时存储
             */
            private List<Activity> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            @Override
            public void invoke(Activity data, AnalysisContext context) {
                //为活动id赋值
                data.setId(UUIDUtil.getUUID());
                data.setOwner(user.getId());
                data.setEditBy(user.getId());
                data.setCreateBy(user.getId());
                data.setCreateTime(DateFormatUtil.formatDateAndTime(new Date()));
                cachedDataList.add(data);
                if (cachedDataList.size() >= BATCH_COUNT) {
                    int count = saveData();
                    activityCounts += count;
                    // 存储完成清理 list
                    cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                if(cachedDataList.size() > 0) {
                    int count = saveData();
                    activityCounts += count;
                }
            }

            /**
             * 加上存储数据库
             */
            private int saveData() {
                return activityService.saveActivitiesFromExcel(cachedDataList);
            }
        }).sheet().doRead();
        return activityCounts;
    }
}
