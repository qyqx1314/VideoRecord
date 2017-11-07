package com.banger.videorecord.mouble.record.business;

import android.content.Context;

import com.banger.videorecord.mouble.record.bean.LocalRecordBean;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Xuchaowen on 2016/6/21.
 * xml解析接口
 */
public interface VideoParser {

     LocalRecordBean parse(Context mContext,InputStream is) throws Exception;

     String serialize(Context mContext,LocalRecordBean videos) throws Exception;
}
