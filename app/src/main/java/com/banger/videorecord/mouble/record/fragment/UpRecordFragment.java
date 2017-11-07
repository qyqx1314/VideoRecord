package com.banger.videorecord.mouble.record.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.result.BaseResult;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.record.adapter.UpQueueAdapter;
import com.banger.videorecord.mouble.record.adapter.UpQueueNewAdapter;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;
import com.banger.videorecord.mouble.record.bean.UpdateListInfo;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.bean.XmlMessageBean;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.record.util.XmlUtils;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.ToastUtil;
import com.banger.videorecord.widget.MySelectDialog;
import com.banger.zeromq.zmq.IUploadFileState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Xuchaowen on 2016/6/8.
 * 上传队列页面
 */
@EFragment(R.layout.fragment_up_record)
public class UpRecordFragment extends Fragment {
    private static final String TAG = "UpRecordFragment";
    @ViewById
    ListView up_list;
    @ViewById
    TextView no_toast;//无条目提示
    @App
    AppContext appContext;
    //显示列表的对象集合
    private List<XmlMessageBean> uploadList = new ArrayList<>();
    //正在上传的对象集合
    private List<XmlMessageBean> uploadListIng = new ArrayList<>();
    //正在上传的对象
    private XmlMessageBean uploadBean;
    //正在上传的的image集合
    private List<ImageInfoBean> imageListIng = new ArrayList<>();
    private List<VideoInfoBean> videoListIng = new ArrayList<>();
    //正在上传的图像类
    private ImageInfoBean imageBeanIng;
    //查询数据库得到的所有业务信息
    List<BusinessInfoBean> businessList = new ArrayList<>();
    private UpdateListInfo updateListInfo;
    //列表显示的适配器
    private UpQueueNewAdapter adapter;
    //当前控制上传个数
    MyBroadcastReciver myBroadcastReciver = new MyBroadcastReciver();

    @AfterViews
    void initViews() {
        receiveBroadcast(Constants.UP_BROADCAST);
        initUploadList();
        if (uploadList != null && uploadList.size() > 0) {
            up_list.setVisibility(View.VISIBLE);
            no_toast.setVisibility(View.GONE);
            upLoadMedia();
        } else {
            up_list.setVisibility(View.GONE);
            no_toast.setVisibility(View.VISIBLE);
        }
        //重新上传的回调
        adapter = new UpQueueNewAdapter(getActivity(), uploadList, new UpQueueNewAdapter.OnFailClickListener() {
            @Override
            public void videoFailCallBack(XmlMessageBean bean, int process) {
                bean.setReloadState(process);
                if (!uploadListIng.contains(bean)) {
                    uploadListIng.add(bean);
                    if (uploadListIng.size() == 1) {
                        upLoadMedia();
                    }
                }
            }

        });
        up_list.setAdapter(adapter);
        up_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final XmlMessageBean bean = uploadList.get(position);
                if (!isUpload(uploadList.get(position))) {
                    MySelectDialog dialog = new MySelectDialog(getActivity(), "确定取消这条上传记录么？", "确定", "取消", new MySelectDialog.OnCustomDialogListener() {
                        @Override
                        public void button1(Dialog dialog) {
                            uploadList.remove(bean);
                            uploadListIng.remove(bean);
                            bean.getBusinessInfoBean().setState(1);
                            writeToSdCard(bean);
                            DBVideoUtils.updateBizInfo(bean.getBusinessInfoBean().getId(), bean.getBusinessInfoBean());
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void button2(Dialog dialog) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                return false;
            }
        });
    }

    private boolean isUpload(XmlMessageBean bean) {
        boolean result = false;
        List<VideoInfoBean> videoList = bean.getVideoList();
        List<ImageInfoBean> imageList = bean.getImageList();
        if (videoList != null && videoList.size() > 0) {
            for (VideoInfoBean videoInfoBean : videoList) {
                if (videoInfoBean.getState() == 2) {
                    result = true;
                }
            }
        }
        if (imageList != null && imageList.size() > 0) {
            for (ImageInfoBean imageInfoBean : imageList) {
                if (imageInfoBean.getState() == 2) {
                    result = true;
                }
            }
        }
        return result;
    }


    /**
     * 初始化上传队列的数据，uploadListIng 控制上传队列  uploadList控制视图显示
     */
    private void initUploadList() {
        if (uploadList != null) {
            uploadList.clear();
        } else {
            uploadList = new ArrayList<>();
        }
        String account = SharedPrefsUtil.getInstance().getStringValue(getActivity(), Constants.USER_NAME, "admin");
        businessList = DBVideoUtils.findAllByStateAccount(2, account);
        if (businessList != null && businessList.size() > 0) {
            for (BusinessInfoBean businessInfoBean : businessList) {
                XmlMessageBean bean = new XmlMessageBean();
                List<VideoInfoBean> videoList = DBVideoUtils.findAllByBiz(businessInfoBean.getBizNo());
                List<ImageInfoBean> imageList = DBVideoUtils.findAllImageByBiz(businessInfoBean.getBizNo());
                bean.setImageList(imageList);
                bean.setBusinessInfoBean(businessInfoBean);
                bean.setVideoList(videoList);
                uploadList.add(bean);
            }
            uploadListIng.addAll(uploadList);
        }
    }

    /**
     * 循环一条条的开始上传
     */
    private void upLoadMedia() {
        if (uploadListIng != null && uploadListIng.size() > 0) {
            XmlMessageBean bean = uploadListIng.get(0);
            int index = uploadList.indexOf(bean);
            uploadBean = uploadList.get(index);
            uploadListIng.remove(0);
            if (judgeIsLost(uploadBean)) {
                uploadList.remove(uploadBean);
                upLoadMedia();
                return;
            }
            addImageToList();
            addVideoToList();
            String stringXml = XmlUtils.changeMediaToXml(uploadBean, appContext);
            saveBusiness(stringXml);
        }
    }

    /**
     * 判断文件是否缺失，如果缺失直接下一条
     */
    private boolean judgeIsLost(XmlMessageBean bean) {
        boolean isLost = false;
        BusinessInfoBean businessInfoBean = bean.getBusinessInfoBean();
        List<VideoInfoBean> videoList = bean.getVideoList();
        List<ImageInfoBean> imageList = bean.getImageList();
        if (videoList != null && videoList.size() > 0) {
            for (VideoInfoBean videoInfoBean : videoList) {
                if (TextUtils.isEmpty(videoInfoBean.getFilePath())) {
                    isLost = true;
                } else {
                    File file = new File(videoInfoBean.getFilePath());
                    if (!file.exists()) {
                        isLost = true;
                    }
                }
            }
        }
        if (imageList != null && imageList.size() > 0) {
            for (ImageInfoBean imageInfoBean : imageList) {
                if (TextUtils.isEmpty(imageInfoBean.getFilePath())) {
                    isLost = true;
                } else {
                    File file = new File(imageInfoBean.getFilePath());
                    if (!file.exists()) {
                        isLost = true;
                    }
                }
            }
        }
        if (isLost) {
            businessInfoBean.setState(1);
            writeToSdCard(bean);
            DBVideoUtils.updateBizInfo(businessInfoBean.getId(), businessInfoBean);

        }
        return isLost;
    }

    /**
     * 将未上传的图片，放到上传队列
     */
    private void addImageToList() {
        imageListIng = new ArrayList<>();
        if (uploadBean.getImageList() != null && uploadBean.getImageList().size() > 0) {
            for (ImageInfoBean imageInfoBean : uploadBean.getImageList()) {
                if (imageInfoBean.getState() != 3) {
                    imageListIng.add(imageInfoBean);
                }
            }
        }
    }

    private void addVideoToList() {
        videoListIng = new ArrayList<>();
        if (uploadBean.getVideoList() != null && uploadBean.getVideoList().size() > 0) {
            for (VideoInfoBean videoInfoBean : uploadBean.getVideoList()) {
                if (videoInfoBean.getState() != 3) {
                    videoListIng.add(videoInfoBean);
                }
            }
        }
    }


    /**
     * 保存业务信息，保存成功之后开始上传业务
     *
     * @param param 传递给服务器的xml字段
     */
    void saveBusiness(String param) {
        HttpTool httpTool = RetrofitUtils.createApi(getActivity(), HttpTool.class);
        httpTool.uploadForStr(param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<BaseResult>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError business: " + e.getMessage());
                        ToastUtil.showShortToast(getActivity(), "网络异常，请稍后重试");
                    }

                    @Override
                    public void onNext(BaseResult base) {
                        Log.e(TAG, "onNext  business BaseResult: " + base);
                        if (base != null) {
                            if (base.getResult() == 0) {
                                upLoadImages();
                                upLoadVideos();
                            } else {
                                ToastUtil.showShortToast(getActivity(), base.getError());
                            }

                        } else {
                            ToastUtil.showShortToast(getActivity(), "网络异常，请稍后重试");
                        }

                    }
                });
    }

    /**
     * 控制图片一张张上传
     */
    private void upLoadImages() {
        //区分是重新上传还是不重新上传
        if (imageListIng != null && imageListIng.size() > 0) {
            ImageInfoBean imageInfoBean = imageListIng.get(0);
            int index = uploadBean.getImageList().indexOf(imageInfoBean);
            imageBeanIng = uploadBean.getImageList().get(index);
            imageListIng.remove(0);
            upLoadImage(imageBeanIng);
        } else {
            isDoneToNext();
        }
    }

    /**
     * 批量上传视频文件
     */
    private void upLoadVideos() {
        if (videoListIng != null && videoListIng.size() > 0) {
            System.out.println("zzzz video size+" + videoListIng.size());
            for (VideoInfoBean videoInfoBean : videoListIng) {
                if (FileUtils.isExistFile(videoInfoBean.getFilePath())) {
                    DBVideoUtils.updateVideoInfo(videoInfoBean, 2);
                    appContext.getUploadVideoServer().uploadFileTask(videoInfoBean.getFilePath(), ttsHandler, videoInfoBean.getVideoProcess(), videoInfoBean.getBizNo());
                    adapter.notifyDataSetChanged();
                } else {
                    DBVideoUtils.updateVideoInfo(videoInfoBean, 3);
                    adapter.notifyDataSetChanged();
                    isDoneToNext();

                }
            }
            videoListIng.clear();
        } else {
            isDoneToNext();
        }
    }


    /**
     * 单独一张图片上传的接口方法
     */
    void upLoadImage(ImageInfoBean imageInfoBean) {
        DBVideoUtils.updateImageInfo(imageInfoBean, 2);
        HttpTool httpTool = RetrofitUtils.createApi(getActivity(), HttpTool.class);
        RequestBody requestBody1 = null, requestBody2 = null, requestBody3 = null, requestBody4 = null, requestBody5 = null;
        File file = null;
        if (!TextUtils.isEmpty(imageInfoBean.getFilePath())) {
            file = new File(imageInfoBean.getFilePath());
            //如果文件不存在，则继续上传
            if (!file.exists()) {
                //删除数据库的文件，接着上传图片
                DBVideoUtils.updateImageInfo(imageInfoBean, 3);
                upLoadImages();
                return;
            }
            requestBody1 =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
        }
        MultipartBody.Part body = null;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/otcet-stream"),file);
        body = MultipartBody.Part.createFormData("file",file.getName(),requestBody);
        String fileName = file.getName();
        String bizNo = imageInfoBean.getBizNo();
        httpTool.uploadImageFile(body,RequestBody.create(null, fileName),RequestBody.create(null,
                bizNo))
//        httpTool.uploadImage(imageInfoBean.getBizNo() + ",jpg" + "," + imageInfoBean.getState(),
//                requestBody1, requestBody2, requestBody3, requestBody4, requestBody5)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResult>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError image: " + e.getMessage());
                        DBVideoUtils.updateImageInfo(imageBeanIng, 4);
                        adapter.notifyDataSetChanged();
                        upLoadImages();
                    }

                    @Override
                    public void onNext(BaseResult result) {
                        Log.e(TAG, "onNext: image-->>" + result);
                        if (result != null) {
                            if (result.getResult() == 0) {
                                DBVideoUtils.updateImageInfo(imageBeanIng, 3);
                                adapter.notifyDataSetChanged();
                                upLoadImages();
                            } else {
                                DBVideoUtils.updateImageInfo(imageBeanIng, 4);
                                adapter.notifyDataSetChanged();
                                upLoadImages();
                            }
                        } else {
                            ToastUtil.showShortToast(getActivity(), "网络异常，请稍后重试");
                            DBVideoUtils.updateImageInfo(imageBeanIng, 4);
                            adapter.notifyDataSetChanged();
                            upLoadImages();
                        }

                    }
                });
    }

    /**
     * 如果上传结束之后，上传下一条
     */
    private void isDoneToNext() {
        boolean isImageDone = false;
        boolean isVideoDone = false;
        int videoFailNum = 0;
        int imageFailNum = 0;
        if (imageListIng == null || imageListIng.size() == 0) {
            isImageDone = true;
        }
        if (videoListIng == null || videoListIng.size() == 0) {
            isVideoDone = true;
        }
        for (VideoInfoBean videoInfoBean : videoListIng) {
            if (videoInfoBean.getState() != 3) {
                videoFailNum++;
            }
        }
        for (ImageInfoBean imageInfoBean : uploadBean.getImageList()) {
            if (imageInfoBean.getState() != 3) {
                imageFailNum++;
            }
        }

        if (videoFailNum == 0 && imageFailNum == 0) {
            DBVideoUtils.updateVideoInfo(uploadBean.getBusinessInfoBean(), 3);
            writeToSdCard(uploadBean);
            uploadList.remove(uploadBean);
            adapter.notifyDataSetChanged();
        }

        if (isImageDone && isVideoDone) {
            upLoadMedia();
        }

    }


    MyHandler ttsHandler = new MyHandler();

    /**
     * uploading   上传中
     * failed   上传失败   新增上传业务
     * error              新增上传业务
     * success;上传成功    新增上传业务UploadFileTask
     */
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (null != msg && null != msg.obj) {
                //返回上传状态
                IUploadFileState iUploadFileState = (IUploadFileState) msg.obj;
                String backState = iUploadFileState.getState().toString().trim();
                if (backState.equals("waiting")) {
                } else if (backState.equals("uploading")) {
                    for (VideoInfoBean videoInfoBean : uploadBean.getVideoList()) {
                        if (videoInfoBean.getFilePath().equals(iUploadFileState.getFilename())) {
                            videoInfoBean.setProgressPercent(iUploadFileState.getProgress());
                            adapter.notifyDataSetChanged();
                        }
                    }

                } else if (backState.equals("success")) {
                    for (VideoInfoBean videoInfoBean : uploadBean.getVideoList()) {
                        if (videoInfoBean.getFilePath().equals(iUploadFileState.getFilename())) {
                            DBVideoUtils.updateVideoInfo(videoInfoBean, 3);
                            adapter.notifyDataSetChanged();
                            isDoneToNext();
                        }
                    }

                } else if (backState.equals("error") || backState.equals("failed")) {
                    //判断是否一个业务上传完毕,传递下一个文件
                    for (VideoInfoBean videoInfoBean : uploadBean.getVideoList()) {
                        if (videoInfoBean.getFilePath().equals(iUploadFileState.getFilename())) {
                            DBVideoUtils.updateVideoInfo(videoInfoBean, 4);
                            adapter.notifyDataSetChanged();
                            isDoneToNext();
                        }
                    }
                }

            }
        }
    }

    private void receiveBroadcast(String broadcast) {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(broadcast);
        if (null != myBroadcastReciver && null != intentFilter) {
            getActivity().registerReceiver(myBroadcastReciver, intentFilter);
        }
    }

    private class MyBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.UP_BROADCAST)) {
                updateListInfo = (UpdateListInfo) intent.getSerializableExtra("updateListInfo");
                if (updateListInfo != null) {
                    addBeanToList();
                    if (uploadListIng != null && uploadListIng.size() > 0) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    //将本地记录添加到上传队列
    private void addBeanToList() {
        List<LocalRecordBean> list = updateListInfo.getList();
        if (list != null && list.size() > 0) {
            up_list.setVisibility(View.VISIBLE);
            no_toast.setVisibility(View.GONE);
            for (LocalRecordBean localRecordBean : list) {
                if (!TextUtils.isEmpty(localRecordBean.getBiz())) {
                    List<BusinessInfoBean> businessList = DBVideoUtils.findAllBusinessByBiz(localRecordBean.getBizNo());
                    List<VideoInfoBean> videoList = DBVideoUtils.findAllByBiz(localRecordBean.getBizNo());
                    List<ImageInfoBean> imageList = DBVideoUtils.findAllImageByBiz(localRecordBean.getBizNo());
                    if (businessList != null) {
                        XmlMessageBean xmlMessageBean = new XmlMessageBean();
                        xmlMessageBean.setBusinessInfoBean(businessList.get(0));
                        xmlMessageBean.setVideoList(videoList);
                        xmlMessageBean.setImageList(imageList);
                        for (VideoInfoBean bean1 : videoList) {
                            System.out.println("zzzz video state received" + bean1.getState());
                        }
                        uploadList.add(xmlMessageBean);
                        writeToSdCard(xmlMessageBean);
                        uploadListIng.add(xmlMessageBean);
                    }
                }

            }
            if (uploadListIng.size() <= list.size()) {
                upLoadMedia();
            }
        } else {
            up_list.setVisibility(View.GONE);
            no_toast.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myBroadcastReciver);
    }

    //更新本地xml文件
    private void writeToSdCard(XmlMessageBean xmlMessageBean) {
        if (FileUtils.isSdState()) {
            File file;
            if (appContext.getLoginState() == 0) {
                file = new File(Constants.ONLINE_XML_PATH);
            } else {
                file = new File(Constants.OFFLINE_XML_PATH);
            }

            if (!file.exists()) {
                file.mkdirs();
            }
            //ImageFormat.JPEG
            File xmlFile = new File(file, xmlMessageBean.getBusinessInfoBean().getBizNo() + ".xml");
            String xml = XmlUtils.changeBusinessToXml(xmlMessageBean.getBusinessInfoBean(), xmlMessageBean.getVideoList(), xmlMessageBean.getImageList(), appContext);
            FileUtils.writeFile(xmlFile.getAbsolutePath(), xml, true);
        }

    }

}
