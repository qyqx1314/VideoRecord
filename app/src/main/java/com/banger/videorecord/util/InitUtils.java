package com.banger.videorecord.util;



import com.banger.videorecord.mouble.record.bean.LocalRecordBean;

import java.util.ArrayList;

/**
 * Created by Xuchaowen on 2016/6/11.
 * 假数据
 */
public class InitUtils {

    public static void initLocalList(ArrayList<LocalRecordBean> locallist) {
        for (int i = 0; i < 10; i++) {
            LocalRecordBean bean = new LocalRecordBean();
            bean.setUserName("猪八戒");
            bean.setTime("06/11 10:00");
            bean.setVideoType("理财");
            bean.setManageType("保本");
            bean.setManageName("智赢人生");
            bean.setIsVisiblePro(false);
            locallist.add(bean);
        }

    }
    public static void initUpList(ArrayList<LocalRecordBean> uplist) {
        for (int i = 0; i < 5; i++) {
            LocalRecordBean bean = new LocalRecordBean();
            bean.setUserName("孙悟空");
            bean.setTime("06/11 10:00");
            bean.setVideoType("理财");
            bean.setManageType("保本");
            bean.setManageName("智赢人生");
            bean.setIsVisiblePro(true);
            uplist.add(bean);
        }

    }


    public static void initUpFailList(ArrayList<LocalRecordBean> upFaillist) {
        for (int i = 0; i < 5; i++) {
            LocalRecordBean bean = new LocalRecordBean();
            bean.setUserName("沙悟净");
            bean.setTime("06/11 10:00");
            bean.setVideoType("理财");
            bean.setManageType("保本");
            bean.setManageName("智赢人生");
            bean.setIsVisiblePro(false);
            upFaillist.add(bean);
        }

    }


}