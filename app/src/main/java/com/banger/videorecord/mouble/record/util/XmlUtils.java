package com.banger.videorecord.mouble.record.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Xml;

import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.http.util.HttpConfig;
import com.banger.videorecord.mouble.product.bean.ProductEntity;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.CustomerInfoBean;
import com.banger.videorecord.mouble.record.bean.FiledInfo;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;
import com.banger.videorecord.mouble.record.bean.ProcessInfo;
import com.banger.videorecord.mouble.record.bean.VideoEntity;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.bean.XmlMessageBean;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.record.business.VideoParser;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.util.Base64;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.DateUtil;
import com.banger.videorecord.util.FileSizeUtils;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.GsonUtil;
import com.banger.zeromq.util.FileUtil;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhusiliang on 16/6/21.
 */
public class XmlUtils implements VideoParser {
    /**
     * 上传成功之后，将所有的数据存到xml中，改变状态
     * @param appContext 全局变量
     * @return
     */
    public static String changeBusinessToXml(BusinessInfoBean businessInfoBean, List<VideoInfoBean> videoList, List<ImageInfoBean> imgList, AppContext appContext) {
        StringBuilder sb = new StringBuilder();
        if (businessInfoBean != null) {
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            sb.append("<message>");
            sb.append("<record>");
            sb.append("<businessNo>" + businessInfoBean.getBizNo() + "</businessNo>");  //唯一标示的业务号
            sb.append("<account>" + businessInfoBean.getAccount() + "</account>");           //用户信息
            sb.append("<customerName>" + businessInfoBean.getUserName() + "</customerName>");
            sb.append("<productId>" + businessInfoBean.getProductId() + "</productId>");        //产品idID
            sb.append("<productName>" + businessInfoBean.getProductName() + "</productName>"); //产品名称
            sb.append("<classType>" + businessInfoBean.getProductType() + "</classType>");
            sb.append("<businessType>" + businessInfoBean.getBizType() + "</businessType>");   //业务类型

            sb.append("<classTypeId>" + businessInfoBean.getProductTypeId() + "</classTypeId>");
            sb.append("<businessTypeId>" + businessInfoBean.getBizTypeId() + "</businessTypeId>");   //业务类型

            sb.append("<buyMoney>" + businessInfoBean.getBuyMoney() + "</buyMoney>");                  //购买金额二
            sb.append("<buyType>" + businessInfoBean.getBuyType() + "</buyType>");                //购买类型
            sb.append("<dueDate></dueDate>");             //时间
            sb.append("<state>" + businessInfoBean.getState() + "</state>");
            sb.append("<remark>" + businessInfoBean.getNote() + "</remark>");   //新增的备注
            sb.append("<createTime>" + businessInfoBean.getCreateTime() + "</createTime>");   //创建时间
            sb.append("<xmlPath>" + businessInfoBean.getXmlFilePath() + "</xmlPath>");   //新增的xml存储路径
            sb.append("<certType>" + businessInfoBean.getCertType() + "</certType>");   //还保留的证件类型
            sb.append("<certNumber>" + businessInfoBean.getCertNumber() + "</certNumber>");   //还保留的证件号码
            //新增的手机号码
            sb.append("<mobile>" + businessInfoBean.getMobile() + "</mobile>");   //
            //新增的用户额外信息。
            sb.append("<userInfo>" + Base64.encode(businessInfoBean.getUserInfo().getBytes()) + "</userInfo>");
            //新增的产品的额外信息
            sb.append("<productInfo>" + Base64.encode(businessInfoBean.getProductInfo().getBytes()) + "</productInfo>");
            //新增的环节的信息
            sb.append("<mediaInfo>" + Base64.encode(businessInfoBean.getMediaInfo().getBytes()) + "</mediaInfo>");
            //新增的环节的额外信息
            sb.append("<mediaExtraInfo>" + Base64.encode(businessInfoBean.getMediaExtraInfo().getBytes()) + "</mediaExtraInfo>");
            sb.append("</record>");
            if (videoList != null && videoList.size() > 0) {
                //拼接video节点**********************************************
                for (VideoInfoBean videoInfoBean : videoList) {
                    String path = videoInfoBean.getFilePath();
                    long fileSize = FileUtils.getFileSize(path);
                    sb.append("<video>");
                    sb.append("<deviceId>" + appContext.getIMEI() + "</deviceId>");
                    sb.append("<recordNo>" + videoInfoBean.getFileId() + "</recordNo>");                //流水号，这里就时文件名（不带后缀）
                    sb.append("<fileId>" + videoInfoBean.getFileId() + "</fileId>");                    //文件名（不带后缀）
                    sb.append("<transit>2</transit>");                            //固定值，不用修改
                    String localIp = "123.123.123.11";
                    sb.append("<ip>" + localIp + "</ip>");                        //本地IP
                    sb.append("<serverIp>" + HttpConfig.getInstance().getBaseIpNoHttp(appContext) + "</serverIp>");                        //本地IP
///              获取时间
                    String startTime = videoInfoBean.getBeginTime();
                    String endTime = videoInfoBean.getEndTime();
                    int recTime = Integer.parseInt("" + (DateUtil.getDistanceTimes(startTime, endTime)));
                    if (recTime == 0) {
                        recTime = 1;
                    }
                    sb.append("<beginTime>" + startTime + "</beginTime>");                //录像开始时间 自己取 格式 2012-12-12 12:12:12
                    sb.append("<endTime>" + endTime + "</endTime>");                    //录像结束时间 自己取 格式 2012-12-12 12:12:12
                    sb.append("<filePath>" + videoInfoBean.getFilePath() + "</filePath>");                    //路径
                    sb.append("<fileName>" + videoInfoBean.getFileName() + "</fileName>");                    //文件名
                    sb.append("<parentPath>" + Constants.FILE + videoInfoBean.getFileName() + "</parentPath>");                    //文件的相对路径
                    sb.append("<fileSize>" + fileSize + "</fileSize>");                    //文件大小
                    sb.append("<recTime>" + recTime + "</recTime>");                    //时长

                    sb.append("<account>" + businessInfoBean.getAccount() + "</account>");                    //账号，没有取员工号
                    sb.append("<employeeNo>" + businessInfoBean.getAccount() + "</employeeNo>");           //员工号,自己取
                    sb.append("<deptNo>" + "54321" + "</deptNo>");                         //机构号，没有就不填，有就填，查询用
                    sb.append("<schema>Android</schema>");                                //固定值，不用修改
                    sb.append("<businessNo>" + videoInfoBean.getBizNo() + "</businessNo>");                    //自己取 业务流水号
                    sb.append("<businessType>1</businessType>");                //业务类型，这个未定
                    sb.append("<videoProcess>" + videoInfoBean.getProcessId() + "</videoProcess>");                //视频环节
                    sb.append("<processId>" + videoInfoBean.getProcessId() + "</processId>");                //视频环节的id
                    sb.append("<processName>" + videoInfoBean.getProcessName() + "</processName>");                //视频环节
                    sb.append("<state>" + videoInfoBean.getState() + "</state>");                //视频上传状态
                    sb.append("</video>");
                }
            }
            if (imgList != null && imgList.size() > 0) {
                //拼接image节点**********************************************
                for (ImageInfoBean imageInfoBean : imgList) {
                    sb.append("<image>");
                    sb.append("<filePath>" + imageInfoBean.getFilePath() + "</filePath>");
                    sb.append("<fileName>" + imageInfoBean.getFileName() + "</fileName>");
                    sb.append("<process>" + imageInfoBean.getProcess() + "</process>");
                    sb.append("<account>" + businessInfoBean.getAccount() + "</account>");
                    sb.append("<businessNo>" + businessInfoBean.getBizNo() + "</businessNo>");                    //自己取 业务流水号
                    sb.append("<parentPath>" + Constants.FILE + imageInfoBean.getFileName() + "</parentPath>");
                    sb.append("<state>" + imageInfoBean.getState() + "</state>");
                    sb.append("<processId>" + imageInfoBean.getProcessId() + "</processId>");                //视频环节的id
                    sb.append("<processName>" + imageInfoBean.getProcessName() + "</processName>");                //视频环节
                    sb.append("</image>");
                }
            }
            sb.append("</message>");

        }
        return sb.toString();
    }

    public static String changeMediaToXml(XmlMessageBean xmlMessageBean, AppContext appContext) {
        StringBuilder sb = new StringBuilder();
        List<VideoInfoBean> list = xmlMessageBean.getVideoList();
        BusinessInfoBean bean = xmlMessageBean.getBusinessInfoBean();

        if (bean != null && list.size() > 0) {
            sb.append("<message>");
            sb.append("<record>");
            sb.append("<businessNo>" + bean.getBizNo() + "</businessNo>");           //证件类型
            sb.append("<account>" + bean.getAccount() + "</account>");           //用户信息
            sb.append("<productId>" + bean.getProductId() + "</productId>");        //产品idID
            sb.append("<productName>" + bean.getProductName() + "</productName>"); //产品名称
            sb.append("<classType>" + bean.getProductTypeId() + "</classType>");
            sb.append("<buyMoney>" + bean.getBuyMoney() + "</buyMoney>");                  //购买金额二
            sb.append("<buyType>" + bean.getBuyType() + "</buyType>");                //购买类型
            sb.append("<dueDate></dueDate>");             //时间
            sb.append("<dueDate>" + bean.getDueDate() + "</dueDate>");                  //时间
            sb.append("<businessType>" + bean.getBizTypeId() + "</businessType>");   //业务类型
            sb.append("<remark>" + bean.getNote() + "</remark>");   //新增的备注
            //添加之前
            sb.append("<customerName>" + bean.getUserName() + "</customerName>");   //用户姓名
            sb.append("<certType>" + bean.getCertType() + "</certType>");   //证件类型
            sb.append("<certNum>" + bean.getCertNumber() + "</certNum>");   //证件号码
            sb.append("<mobilePhone>" + bean.getMobile() + "</mobilePhone>");   //用户手机号码
            //用户的额外字段
            sb.append("<customerFields>" + changeStringToCus(bean.getUserInfo()) + "</customerFields>");
            //视频的额外字段
            sb.append("<recordFields>" + changeStringToVideo(bean.getMediaExtraInfo()) + "</recordFields>");
            sb.append("<mobilePhone>" + bean.getMobile() + "</mobilePhone>");   //用户手机号码
            sb.append("</record>");

            for (VideoInfoBean videoInfoBean : list) {
                long fileSize = FileUtils.getFileSize(videoInfoBean.getFilePath());
                sb.append("<video>");
                sb.append("<deviceId>" + appContext.getIMEI() + "</deviceId>");
                sb.append("<recordNo>" + videoInfoBean.getFileId() + "</recordNo>");                //流水号，这里就时文件名（不带后缀）
                sb.append("<fileId>" + videoInfoBean.getFileId() + "</fileId>");    //videoInfoBean.getFileName()                //文件名（不带后缀）
                sb.append("<transit>2</transit>");                            //固定值，不用修改
                sb.append("<ip>" + "123.123.123.11" + "</ip>");                        //本地IP
                sb.append("<serverIp>" + HttpConfig.getInstance().getBaseIpNoHttp(appContext) + "</serverIp>");                        //本地IP
                sb.append("<beginTime>" + videoInfoBean.getBeginTime() + "</beginTime>");                //录像开始时间 自己取 格式 2012-12-12 12:12:12
                sb.append("<endTime>" + videoInfoBean.getEndTime() + "</endTime>");                    //录像结束时间 自己取 格式 2012-12-12 12:12:12
                sb.append("<fileName>" + videoInfoBean.getFileName() + "</fileName>");                    //文件名
                sb.append("<fileSize>" + fileSize + "</fileSize>");                    //文件大小
                sb.append("<recTime>" + videoInfoBean.getRecTime() + "</recTime>");                    //时长
                sb.append("<account>" + videoInfoBean.getAccount() + "</account>");                    //账号，没有取员工号
                sb.append("<employeeNo>" + videoInfoBean.getAccount() + "</employeeNo>");           //员工号,自己取
                sb.append("<deptNo>" + "54321" + "</deptNo>");                         //机构号，没有就不填，有就填，查询用
                sb.append("<schema>Android</schema>");                                //固定值，不用修改
                sb.append("<businessNo>" + videoInfoBean.getBizNo() + "</businessNo>");                    //自己取 业务流水号
                sb.append("<businessType>1</businessType>");                //业务类型，这个未定
                sb.append("<videoProcess>" + videoInfoBean.getProcessId() + "</videoProcess>");                //业务类型，这个未定
                sb.append("</video>");
            }
            sb.append("</message>");

        }
        return sb.toString();

    }


    public static String changeStringToCus(String jsonString) {
        StringBuffer sb = new StringBuffer();
        CustomerInfoBean bean = GsonUtil.getInstance().json2Bean(jsonString, CustomerInfoBean.class);
        if (bean != null) {
            if (bean.getTemplateFields() != null && bean.getTemplateFields().size() > 0) {
                for (FiledInfo info : bean.getTemplateFields()) {
                    sb.append("<entry> ");
                    sb.append("<key>" + info.getFieldColName() + "</key>");
                    sb.append("<value>" + info.getValue() + "</value>");
                    sb.append("</entry> ");
                }
            }
        }
        return sb.toString();
    }

    public static String changeStringToVideo(String jsonString){
        StringBuffer sb = new StringBuffer();
        Type type=new TypeToken<List<FiledInfo>>(){}.getType();
        List<FiledInfo> list=GsonUtil.getInstance().json2List(jsonString,type);
        if (list != null && list.size() > 0) {
            for (FiledInfo info : list) {
                sb.append("<entry> ");
                sb.append("<key>" + info.getFieldColName() + "</key>");
                sb.append("<value>" + info.getValue() + "</value>");
                sb.append("</entry> ");
            }
        }
        return sb.toString();
    }

    //生成业务信息提交到服务器的xml文件
    public static String businessToXml(BusinessInfoBean businessInfoBean) {
        StringBuilder sb = new StringBuilder();
        sb.append("<message>");
        sb.append("<record>");
        sb.append("<businessNo>" + businessInfoBean.getBizNo() + "</businessNo>");           //证件类型
        sb.append("<account>" + businessInfoBean.getAccount() + "</account>");           //用户姓名
        sb.append("<productId>" + businessInfoBean.getProductId() + "</productId>");        //产品idID
        sb.append("<productName>" + businessInfoBean.getProductName() + "</productName>"); //产品名称
        sb.append("<classType>" + (TextUtils.isEmpty(businessInfoBean.getProductType()) ? "" : businessInfoBean.getProductType()) + "</classType>"); //父类类型
        sb.append("<buyMoney>" + businessInfoBean.getBuyMoney() + "</buyMoney>");                  //购买金额二
        sb.append("<buyType>" + businessInfoBean.getBuyType() + "</buyType>");                //购买类型
        sb.append("<dueDate>" + businessInfoBean.getDueDate() + "</dueDate>");                  //时间
        sb.append("<businessType>1</businessType>");   //业务类型
        sb.append("</record>");
        sb.append("</message>");
        return sb.toString();
    }


    @Override
    public LocalRecordBean parse(Context mContext, InputStream is) throws Exception {

        LocalRecordBean localRecordBean = null;
        XmlPullParser parser = Xml.newPullParser(); //由android.util.Xml创建一个XmlPullParser实例
        parser.setInput(is, "UTF-8");               //设置输入流 并指明编码方式
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
//                case XmlPullParser.START_DOCUMENT:
//
//                    break;
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("record")) {
                        localRecordBean = new LocalRecordBean();
                    }
//                    else if(parser.getName().equals("deviceId")){//设备id
//                        eventType = parser.next();
//                        localRecordBean.setTime(parser.getText());
//                    }else if(parser.getName().equals("recordNo")){//流水号
//                        eventType = parser.next();
//                        localRecordBean.setTime(parser.getText());
//                    }else if(parser.getName().equals("fileId")){//文件ID
//                        eventType = parser.next();
//                        localRecordBean.setTime(parser.getText());
//                    }
                    else if (parser.getName().equals("productName")) {//产品名
                        eventType = parser.next();
                        localRecordBean.setManageName(parser.getText());
                    } else if (parser.getName().equals("classType")) {//产品分类
                        eventType = parser.next();
                        localRecordBean.setVideoType(parser.getText());
                    }
//                    else if(parser.getName().equals("fileId")){//xml文件路径
//                        eventType = parser.next();
//                        localRecordBean.setXmlPath(parser.getText() + ".txt");
//                    }
//                        else if(parser.getName().equals("transit")){//固定值
//                        eventType = parser.next();
//                        localRecordBean.setTime(parser.getText());
//                    }else if(parser.getName().equals("ip")){//本地Ip
//                        eventType = parser.next();
//                        localRecordBean.setTime(parser.getText());
//                    }
                    else if (parser.getName().equals("beginTime")) {//开始时间
                        eventType = parser.next();
                        localRecordBean.setTime(parser.getText());
                    }
//                    else if(parser.getName().equals("endTime")){//结束时间
//                        eventType = parser.next();
//                        localRecordBean.setTime(parser.getText());
//                    }
                    else if (parser.getName().equals("fileName")) {//视频文件名
                        eventType = parser.next();
//                        localRecordBean.setPath(app.getSaveState(mContext,Constants.VIDEO_PATH) + "/" + parser.getText());
                        localRecordBean.setPath(parser.getText());
                    }
//                    else if(parser.getName().equals("fileSize")){//文件大小
//                        eventType = parser.next();
//                        localRecordBean.setUserName(parser.getText());
//                    }
//                    else if(parser.getName().equals("recTime")){//时长
//                        eventType = parser.next();
//                        localRecordBean.setUserName(parser.getText());
//                    }
//                    else if(parser.getName().equals("account")){//账号
//                        eventType = parser.next();
//                        localRecordBean.setUserName(parser.getText());
//                    }
                    else if (parser.getName().equals("customerName")) {//客户姓名
                        eventType = parser.next();
                        localRecordBean.setUserName(parser.getText());
                    }
//                    else if(parser.getName().equals("schema")){//固定值
//                        eventType = parser.next();
//                        localRecordBean.setUserName(parser.getText());
//                    }
//                    else if(parser.getName().equals("businessNo")){//业务流水号
//                        eventType = parser.next();
//                        localRecordBean.setUserName(parser.getText());
//                    }
                    else if (parser.getName().equals("businessType")) {//业务类型
                        eventType = parser.next();
                        localRecordBean.setVideoType(parser.getText());
                    }
                    break;
//                case XmlPullParser.END_TAG:
//                    if (parser.getName().equals("record")) {
//                        localRecordBean=null;
//                    }
//                    break;
            }
            eventType = parser.next();
        }
        return localRecordBean;
    }

    public static XmlMessageBean praseXml(InputStream inputStream) {

        XmlPullParser parser = Xml.newPullParser();
        XmlMessageBean xmlMessageBean = null;
        BusinessInfoBean businessInfoBean = null;
        List<VideoInfoBean> videoList = null;
        List<ImageInfoBean> imageList = null;
        List<ProcessInfo> processInfoList =null;
        List<FiledInfo> filedInfoList = null;
        try {
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            VideoInfoBean currentVideoBean = null;
            ImageInfoBean currentImageBean = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT://文档开始事件,可以进行数据初始化处理
                        xmlMessageBean = new XmlMessageBean();
                        videoList = new ArrayList<>();
                        imageList = new ArrayList<>();
                        processInfoList = new ArrayList<>();
                        filedInfoList = new ArrayList<>();
                        break;

                    case XmlPullParser.START_TAG://开始元素事件
                        String name = parser.getName();
                        //解析record节点
                        if (name.equalsIgnoreCase("record")) {
                            businessInfoBean = new BusinessInfoBean();
                        } else if (businessInfoBean != null) {
                            if (name.equalsIgnoreCase("businessNo")) {
                                businessInfoBean.setBizNo(parser.nextText());
                            } else if (name.equalsIgnoreCase("bizNo")) {
                                businessInfoBean.setBizNo(parser.nextText());
                            } else if (name.equalsIgnoreCase("account")) {
                                businessInfoBean.setAccount(parser.nextText());
                            } else if (name.equalsIgnoreCase("certType")) {
                                businessInfoBean.setCertType(Integer.parseInt(parser.nextText()));
                            } else if (name.equalsIgnoreCase("certNumber")) {
                                businessInfoBean.setCertNumber((parser.nextText()));
                            } else if (name.equalsIgnoreCase("productId")) {
                                businessInfoBean.setProductId(Integer.parseInt(parser.nextText()));
                            } else if (name.equalsIgnoreCase("productName")) {
                                businessInfoBean.setProductName((parser.nextText()));
                            } else if (name.equalsIgnoreCase("classTypeId")) {
                                businessInfoBean.setProductTypeId(Integer.parseInt(parser.nextText()));
                            } else if (name.equalsIgnoreCase("classType")) {
                                businessInfoBean.setProductType(parser.nextText());
                            }  else if (name.equalsIgnoreCase("businessType")) {
                                businessInfoBean.setBizType(parser.nextText());
                            }  else if (name.equalsIgnoreCase("businessTypeId")) {
                                businessInfoBean.setBizTypeId(Integer.parseInt(parser.nextText()));
                            } else if (name.equalsIgnoreCase("buyMoney")) {
                                businessInfoBean.setBuyMoney(Integer.parseInt(parser.nextText()));
                            } else if (name.equalsIgnoreCase("buyType")) {
                                businessInfoBean.setBuyType(Integer.parseInt(parser.nextText()));
                            } else if (name.equalsIgnoreCase("dueDate")) {
                                businessInfoBean.setDueDate((parser.nextText()));
                            } else if (name.equalsIgnoreCase("customerName")) {
                                businessInfoBean.setUserName((parser.nextText()));
                            } else if (name.equalsIgnoreCase("state")) {
                                businessInfoBean.setState(Integer.parseInt(parser.nextText()));
                            } else if (name.equalsIgnoreCase("createTime")) {
                                businessInfoBean.setCreateTime((parser.nextText()));
                            } else if (name.equalsIgnoreCase("xmlPath")) {
                                businessInfoBean.setXmlFilePath((parser.nextText()));
                            }else if (name.equalsIgnoreCase("mobile")) {
                                businessInfoBean.setMobile((parser.nextText()));
                            }
                            else if (name.equalsIgnoreCase("userInfo")) {
                                String ss=parser.nextText();
                                businessInfoBean.setUserInfo(Base64.getDecode(ss));
                            }
                            else if (name.equalsIgnoreCase("productInfo")) {
                                businessInfoBean.setProductInfo(Base64.getDecode(parser.nextText()));
                            }
                            else if (name.equalsIgnoreCase("mediaInfo")) {
                                businessInfoBean.setMediaInfo(Base64.getDecode(parser.nextText()));
                            }
                            else if (name.equalsIgnoreCase("mediaExtraInfo")) {
                                businessInfoBean.setMediaExtraInfo(Base64.getDecode(parser.nextText()));
                            }
                        }
                        //解析video节点
                        if (name.equalsIgnoreCase("video")) {
                            currentVideoBean = new VideoInfoBean();
                        } else if (currentVideoBean != null) {
                            if (name.equalsIgnoreCase("fileId")) {
                                currentVideoBean.setFileId(parser.nextText());
                            } else if (name.equalsIgnoreCase("beginTime")) {
                                currentVideoBean.setBeginTime(parser.nextText());
                            } else if (name.equalsIgnoreCase("endTime")) {
                                currentVideoBean.setEndTime(parser.nextText());
                            } else if (name.equalsIgnoreCase("filePath")) {
                                currentVideoBean.setFilePath(parser.nextText());
                            } else if (name.equalsIgnoreCase("fileName")) {
                                currentVideoBean.setFileName(parser.nextText());
                            } else if (name.equalsIgnoreCase("fileSize")) {
                                currentVideoBean.setFileSize(Long.parseLong(parser.nextText()));
                            } else if (name.equalsIgnoreCase("recTime")) {
                                currentVideoBean.setRecTime(Integer.parseInt(parser.nextText()));
                            } else if (name.equalsIgnoreCase("videoProcess")) {
                                currentVideoBean.setVideoProcess(Integer.parseInt(parser.nextText()));
                            } else if (name.equalsIgnoreCase("businessNo")) {
                                currentVideoBean.setBizNo((parser.nextText()));
                            } else if (name.equalsIgnoreCase("state")) {
                                currentVideoBean.setState(Integer.parseInt(parser.nextText()));
                            }else if (name.equalsIgnoreCase("processId")) {
                                currentVideoBean.setProcessId(parser.nextText());
                            } else if (name.equalsIgnoreCase("processName")) {
                                currentVideoBean.setProcessName(parser.nextText());
                            }
                        }
                        //解析图片的
                        if (name.equalsIgnoreCase("image")) {
                            currentImageBean = new ImageInfoBean();
                        } else if (currentImageBean != null) {
                            if (name.equalsIgnoreCase("filePath")) {
                                currentImageBean.setFilePath(parser.nextText());
                            } else if (name.equalsIgnoreCase("fileName")) {
                                currentImageBean.setFileName(parser.nextText());
                            } else if (name.equalsIgnoreCase("process")) {
                                currentImageBean.setProcess(Integer.parseInt(parser.nextText()));
                            } else if (name.equalsIgnoreCase("state")) {
                                currentImageBean.setState(Integer.parseInt(parser.nextText()));
                            }else if (name.equalsIgnoreCase("processId")) {
                                currentImageBean.setProcessId(((parser.nextText())));
                            } else if (name.equalsIgnoreCase("processName")) {
                                currentImageBean.setProcessName(((parser.nextText())));
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG://结束元素事件
                        if (parser.getName().equalsIgnoreCase("record")) {
                            xmlMessageBean.setBusinessInfoBean(businessInfoBean);
                            businessInfoBean = null;
                        }
                        //添加video
                        if (parser.getName().equalsIgnoreCase("video")) {
                            videoList.add(currentVideoBean);
                            xmlMessageBean.setVideoList(videoList);
                            currentVideoBean = null;
                        }
                        if (parser.getName().equalsIgnoreCase("image")) {
                            imageList.add(currentImageBean);
                            xmlMessageBean.setImageList(imageList);
                            currentImageBean = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (xmlMessageBean.getBusinessInfoBean() != null) {
            businessInfoBean = xmlMessageBean.getBusinessInfoBean();
            imageList = xmlMessageBean.getImageList();
            videoList = xmlMessageBean.getVideoList();
            if (imageList != null && imageList.size() > 0) {
                for (ImageInfoBean imageInfoBean : imageList) {
                    imageInfoBean.setBizNo(businessInfoBean.getBizNo());
                }
            }
            if (videoList != null && videoList.size() > 0) {
                for (VideoInfoBean videoInfoBean : videoList) {
                    videoInfoBean.setProductId(businessInfoBean.getProductId());
                    videoInfoBean.setProductName(businessInfoBean.getProductName());
                    videoInfoBean.setProductType(businessInfoBean.getProductType());
                }
            }
        }
        return xmlMessageBean;
    }


    /**
     * XML文件解析
     */
    @Override
    public String serialize(Context mContext, LocalRecordBean videos) throws Exception {
        return null;
    }

    public static String changeCharset(String str, String newCharset) throws UnsupportedEncodingException {
        if (str != null) {
            //用默认字符编码解码字符串。与系统相关，中文windows默认为GB2312
            byte[] bs = str.getBytes();
            return new String(bs, newCharset);    //用新的字符编码生成字符串
        }
        return null;
    }
}
