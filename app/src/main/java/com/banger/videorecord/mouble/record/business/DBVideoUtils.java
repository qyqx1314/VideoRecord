package com.banger.videorecord.mouble.record.business;

import android.content.ContentValues;
import android.util.Log;

import com.banger.videorecord.mouble.product.bean.ProductClass;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.ProcessInfo;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.product.bean.BizType;
import com.banger.videorecord.mouble.setting.bean.BusinessDataInfo;
import com.banger.videorecord.mouble.product.bean.ProdClass;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.util.GsonUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhusiliang on 16/6/27.
 */
public class DBVideoUtils {
    private static final String TAG = "DBVideoUtils";

    /**
     * 查询所有的业务
     *
     * @return
     */
    public static List<BusinessInfoBean> findAllBizInfo() {
        return DataSupport.findAll(BusinessInfoBean.class);
    }


    /**
     * @param state    视频类型 1代表创建 2代表上传成功 3代表正在上传 4代表上传失败
     *                 //     * @param location 存储类型 0 代表内置 1代表外置
     * @param page     传入的查询页数
     * @param pageSize 传入的每页查询的大小
     * @return
     */
    public static List<BusinessInfoBean> findAllBizInfoByPage(int state, int page, int pageSize) {
        return DataSupport.where("state=?", "" + state).order("id desc").limit(pageSize).offset(page * pageSize).find(BusinessInfoBean.class);
    }

    public static void updateBizInfo(int id, BusinessInfoBean businessInfoBean) {
        businessInfoBean.update(id);

    }

    /**
     * 根据biz 查询改biz下面的所有视频
     *
     * @param biz 唯一标识业务号
     * @return
     */
    public static List<VideoInfoBean> findAllByBiz(String biz) {
        return DataSupport.where("bizNo =?", biz).find(VideoInfoBean.class);
    }

    /**
     * 根据biz 查询改biz下面的所有图片
     *
     * @param biz 唯一标识业务号
     * @return
     */
    public static List<ImageInfoBean> findAllByBizImage(String biz) {
        return DataSupport.where("bizNo =?", biz).find(ImageInfoBean.class);
    }


    public static List<VideoInfoBean> findAllByBizProcess(String biz, int process) {
        return DataSupport.where("bizNo =? and videoProcess=?", biz, process + "").find(VideoInfoBean.class);
    }

    public static List<ImageInfoBean> findAllPicByBizProcess(String biz, int process) {
        return DataSupport.where("bizNo =? and process=?", biz, process + "").find(ImageInfoBean.class);
    }

    /**
     * 根据用户名身份证号查找业务
     *
     * @param userName //     * @param certNum
     * @return
     */
    public static List<BusinessInfoBean> findAllByIdName(String userName, String certNumber) {
        if (userName.length() != 0 && certNumber.length() == 0) {
            return DataSupport.where("userName like ?", "%" + userName + "%").find(BusinessInfoBean.class);
        } else if (userName.length() == 0 && certNumber.length() != 0) {
            return DataSupport.where("certNumber like ?", "%" + certNumber + "%").find(BusinessInfoBean.class);
        } else {
            return DataSupport.where("userName like ? and certNumber like ?  ", "%" + userName + "%", "%" + certNumber + "%").find(BusinessInfoBean.class);
        }
    }

    /**
     * 查找所有产品
     *
     * @return
     */
    public static List<ProductDetailInfo> findAllProduct() {
        return DataSupport.findAll(ProductDetailInfo.class);

    }

    /**
     * 根据产品名和编号查找产品
     *
     * @return
     */
    public static List<ProductDetailInfo> findProductByIdNum(String input) {//,String productCode
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(input);
        if (m.matches()) {
            String productCode = input;
//        return DataSupport.where("productCode=? ", productCode).find(ProductDetailInfo.class);
            return DataSupport.where("productCode like ?", "%" + productCode + "%").find(ProductDetailInfo.class);
        } else {
            String productName = input;
//         return DataSupport.where("productName =? ", productName).find(ProductDetailInfo.class);
            return DataSupport.where("productName like ?", "%" + productName + "%").find(ProductDetailInfo.class);
        }
    }


    /**
     * //按照存储状态和内存卡位置查询video列表
     *
     * @param state    视频类型 1代表创建 2代表上传成功 3代表正在上传 4代表上传失败
     * @param location 存储类型 0 代表内置 1代表外置
     * @return
     */
    public static List<VideoInfoBean> findAllVideoByState(int state, int location) {
        return DataSupport.where("state=? and location=?", "" + state, "" + location).find(VideoInfoBean.class);
    }

    /**
     * 按照存储状态和内存卡位置查询video环节的视频列表
     *
     * @param state        视频类型 1代表创建 2代表上传成功 3代表正在上传 4代表上传失败
     * @param location     存储类型 0 代表内置 1代表外置
     * @param videoProcess 环节， 1代表环节1  2代表环节2
     * @return
     */
    public static List<VideoInfoBean> findAllVideoByState(int state, int location, int videoProcess) {
        return DataSupport.where("state=? and location=? and videoProcess=?", "" + state, "" + location, "" + videoProcess).find(VideoInfoBean.class);
    }

    public static List<VideoInfoBean> findVideoByBiz(int state, int location, int videoProcess) {
        return DataSupport.where("state=? and location=? and videoProcess=?", "" + state, "" + location, "" + videoProcess).find(VideoInfoBean.class);
    }

    /**
     * @param state    视频类型 1代表创建 2代表上传成功 3代表正在上传 4代表上传失败
     * @param location 存储类型 0 代表内置 1代表外置
     * @param page     传入的查询页数
     * @param pageSize 传入的每页查询的大小
     * @return
     */
    public static List<VideoInfoBean> findAllVideoByPage(int state, int location, int page, int pageSize) {
        return DataSupport.where("state=? and location=?", "" + state, "" + location).order("id desc").limit(pageSize).offset(page * pageSize).find(VideoInfoBean.class);
    }

    /**
     * 根据id更新视频数据库
     *
     * @param id            视频的主键，自动建立不用管
     * @param videoInfoBean 该id主键的对象
     */
    public static void updateVideo(int id, VideoInfoBean videoInfoBean) {
        videoInfoBean.update(id);
    }

    public static void updateVideo(int id, BusinessInfoBean businessInfoBean) {
        businessInfoBean.update(id);
    }

    /**
     * 根据id 更新业务数据库
     *
     * @param id               业务的主键
     * @param businessInfoBean 该id主键的对象
     */
    public static void updateBusinessInfo(int id, BusinessInfoBean businessInfoBean) {
        businessInfoBean.update(id);
    }


    public static List<BusinessInfoBean> findAllByStateAccount(int state, String account) {
        return DataSupport.where("state =? and  account= ?", "" + state, account).find(BusinessInfoBean.class);
    }

    public static List<ImageInfoBean> findAllImageByProcess(String biz, int process) {
        return DataSupport.where("bizNo=? and process =?", biz, "" + process).find(ImageInfoBean.class);
    }

    public static List<ImageInfoBean> findAllImageByBiz(String biz) {
        return DataSupport.where("bizNo=? ", biz).find(ImageInfoBean.class);
    }

    /**
     * 根据id删除照片
     *
     * @param id
     * @return
     */
    public static void deleteImageById(int id) {
        DataSupport.delete(ImageInfoBean.class, id);
    }


    public static List<BusinessInfoBean> findAllBusinessByBiz(String biz) {
        return DataSupport.where("bizNo =?", "" + biz).find(BusinessInfoBean.class);
    }


    /**
     * 根据id删除视频数据库的元素
     *
     * @param id 该视频的id
     */
    //根据biz，删除video数据库
    public static void deleteVideo(int id) {
        DataSupport.delete(VideoInfoBean.class, id);
    }

    //根据biz，删除video数据库
    public static void deleteImage(int id) {
        DataSupport.delete(ImageInfoBean.class, id);
    }

    //根据biz，删除businessInfo
    public static void deleteBusinessInfo(int id) {
        DataSupport.delete(BusinessInfoBean.class, id);
    }

    //根据biz，删除businessInfo
    public static void deleteBusinessAll() {
        DataSupport.deleteAll(BusinessInfoBean.class);
    }


    //查找业务类型列表数据库
    public static List<BizType> findAllBizType() {

        return DataSupport.findAll(BizType.class);
    }

    //查找所有的视频环节
    public static List<ProcessInfo> findAllpro() {

        return DataSupport.findAll(ProcessInfo.class);
    }

    public static List<ProcessInfo> findAllproByType() {

        return DataSupport.findAll(ProcessInfo.class);
    }



    /**
     * //保存业务列表类型数据库和产品类型列表数据库
     *
     * @param list 数据库查询到的实体类
     */
    public static void saveBizType(ArrayList<BusinessDataInfo> list) {
        DataSupport.deleteAll(BizType.class);
        DataSupport.deleteAll(ProductClass.class);
        DataSupport.deleteAll(ProcessInfo.class);
        for (BusinessDataInfo dataInfo : list) {
            BizType bizType = dataInfo.getBizType();
            bizType.setProcessInfo("{\"processinfos\":"+GsonUtil.getInstance().toJson(dataInfo.getProcesses())+"}");
            bizType.setVideoMoreData("{\"videomore\":"+GsonUtil.getInstance().toJson(dataInfo.getTemplateFields())+"}");
            bizType.save();
            ArrayList<ProdClass> prodList = dataInfo.getProdClass();
            if (prodList != null && prodList.size() > 0) {
                for (ProdClass prodClass : prodList) {
                    ProductClass myClass = new ProductClass();
                    myClass.setClassId(prodClass.getClassId());
                    myClass.setTypeId(prodClass.getTypeId());
                    myClass.setName(prodClass.getClassName());
                    myClass.save();
                }
            }
        }
    }

    /**
     * 根据产品id查询所有的产品类型
     *
     * @param typeId
     * @return
     */
    public static List<ProductClass> findAllProductType(int typeId) {
        return DataSupport.where("typeId =?", "" + typeId).find(ProductClass.class);
    }

    /**
     * 根据产品id查询所有的产品类型
     *
     * @param typeId
     * @return
     */
    public static List<BizType> findAllType(int typeId) {
        return DataSupport.where("typeId =?", "" + typeId).find(BizType.class);
    }

    public static List<ProductClass> findAllProductClass(int typeId, int classId) {
        return DataSupport.where("typeId =? and classId =?", "" + typeId, "" + classId).find(ProductClass.class);
    }

    public static List<ProductDetailInfo> findProductDs(int bizId, int classId, int productId) {
        return DataSupport.where("bizType =? and productClass=? and productId=? ", bizId + "", classId + "", "" + productId).find(ProductDetailInfo.class);
    }


    public static boolean updateType(int typeId, String typeName, int id) {
        ContentValues values = new ContentValues();
        values.put("typeId", typeId);
        values.put("typeName", typeName);
        int state = DataSupport.update(BizType.class, values, id);
        Log.e(TAG, "updateType: " + state);
        if (state > 0) {
            return true;
        }
        return false;
    }

    public static boolean updateProductClass(int typeId, int classId, String name, int id) {
        ContentValues values = new ContentValues();
        values.put("typeId", typeId);
        values.put("classId", classId);
        values.put("name", name);
        int state = DataSupport.update(ProductClass.class, values, id);
        Log.e(TAG, "updateType: " + state);
        if (state > 0) {
            return true;
        }
        return false;
    }

    public static boolean updateProductDs(ProductDetailInfo productDetailInfo, int id) {
        ContentValues values = new ContentValues();
        values.put("typeId", productDetailInfo.getBizType());
        values.put("typeName", productDetailInfo.getBizTypeName());
        values.put("productId", productDetailInfo.getProductId());
        values.put("productCode", productDetailInfo.getProductCode());
        values.put("productName", productDetailInfo.getProductName());
        values.put("productClass", productDetailInfo.getProductClass());
        if (null != productDetailInfo.getProductClassName()) {
            values.put("productClassName", productDetailInfo.getProductClassName());
        } else {
            values.put("productClassName", "");
        }
        values.put("updateDate", productDetailInfo.getUpdateDate());
        int state = DataSupport.update(ProductDetailInfo.class, values, id);
        Log.e(TAG, "updateType: " + state);
        if (state > 0) {
            return true;
        }
        return false;
    }

    /**
     * 将接口返回的集合存库
     *
     * @param list 接口返回的实体类的集合
     */
    //保存所有产品详情列表
    public static void saveProductDetail(ArrayList<ProductDetailInfo> list) {
        DataSupport.deleteAll(ProductDetailInfo.class);
        for (ProductDetailInfo productDetailInfo : list) {
            boolean state = productDetailInfo.save();
            Log.e(TAG, "saveProductDetail: "+state );
        }
    }

    /**
     * //查询所有产品详情列表 根据业务id查询 因为产品类型可能为空
     *
     * @param bizId 业务id
     * @return
     */

    public static List<ProductDetailInfo> findProductDetail(int bizId) {
        return DataSupport.where("bizType =?", bizId + "").find(ProductDetailInfo.class);
    }

    public static List<ProductDetailInfo> findProductDetailo(int bizId) {
        return DataSupport.where("bizType =? and productClass=?", bizId + "", "" + 0).find(ProductDetailInfo.class);
    }

    /**
     * //查询所有产品详情列表
     *
     * @param bizId   业务id 及大类id
     * @param classId 产品类型id  小类id
     * @return
     */

    public static List<ProductDetailInfo> findProductDetail(int bizId, int classId) {
        return DataSupport.where("bizType =? and productClass=? ", bizId + "", classId + "").find(ProductDetailInfo.class);
    }

    /**
     * 更新一条image数据，上传状态
     *
     * @param imageInfoBean 要更新的对象
     * @param state         图片状态 1没有上传 2上传中 3上传成功 4上传失败
     */
    public static void updateImageInfo(ImageInfoBean imageInfoBean, int state) {
        imageInfoBean.setState(state);
        imageInfoBean.update(imageInfoBean.getId());
    }

    /**
     * 更新一条录像状态
     *
     * @param videoInfoBean
     * @param state
     */
    public static void updateVideoInfo(VideoInfoBean videoInfoBean, int state) {
        videoInfoBean.setState(state);
        videoInfoBean.update(videoInfoBean.getId());
    }

    /**
     * 更新一条业务信息状态
     *
     * @param businessInfoBean
     * @param state
     */
    public static void updateVideoInfo(BusinessInfoBean businessInfoBean, int state) {
        businessInfoBean.setState(state);
        businessInfoBean.update(businessInfoBean.getId());
    }

    public static List<VideoInfoBean> findVideoByProcess(String bizNo, String processId) {
        return DataSupport.where("bizNo =? and  processId= ?", bizNo, processId).find(VideoInfoBean.class);
    }

    public static List<ImageInfoBean> findImageByProcess(String bizNo, String processId) {
        return DataSupport.where("bizNo =? and  processId= ?", bizNo, processId).find(ImageInfoBean.class);
    }

}
