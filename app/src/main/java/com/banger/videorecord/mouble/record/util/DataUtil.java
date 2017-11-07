package com.banger.videorecord.mouble.record.util;

import android.text.TextUtils;

import com.banger.videorecord.bean.DataBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhusiliang on 16/10/12.
 */
public class DataUtil {
    public static List<DataBase> changeArrryToList(String[] items) {
        List<DataBase> dataBases = new ArrayList<>();
        if (items != null && items.length > 0) {
            for (int i = 0; i < items.length; i++) {
                DataBase dataBase = new DataBase();
                dataBase.setId(i);
                dataBase.setValue(items[i]);
                dataBases.add(dataBase);
            }
        }
        return dataBases;
    }

    public static List<DataBase> changeStringToList(String text) {
        List<DataBase> dataBases = new ArrayList<>();
        if (!TextUtils.isEmpty(text)) {
            String[] items = text.split(",");
            if (items != null && items.length > 0) {
                for (int i = 0; i < items.length; i++) {
                    DataBase dataBase = new DataBase();
                    dataBase.setId(i);
                    dataBase.setValue(items[i]);
                    dataBases.add(dataBase);
                }
            }
        }
        return dataBases;
    }

}
