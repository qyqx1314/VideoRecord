package com.banger.videorecord.mouble.setting.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.mouble.product.bean.BizType;
import com.banger.videorecord.mouble.product.bean.ListProducts;
import com.banger.videorecord.mouble.product.bean.ProductClass;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.mouble.product.business.ProductBusinessImp;
import com.banger.videorecord.mouble.product.business.ProductBusinessInt;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;
import com.banger.videorecord.mouble.record.business.CameraBus;
import com.banger.videorecord.mouble.record.business.ContentCallBack;
import com.banger.videorecord.mouble.record.business.ContentClickListener;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.record.widget.ErrorDialog;
import com.banger.videorecord.mouble.record.widget.RoundProgressBar;
import com.banger.videorecord.mouble.setting.activity.LoginActivity;
import com.banger.videorecord.mouble.setting.bean.BusinessDataInfo;
import com.banger.videorecord.mouble.setting.bean.OffPackageListHodler;
import com.banger.videorecord.mouble.setting.bean.OfflineImportHistory;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.DateUtil;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.GsonUtil;
import com.banger.videorecord.util.ToastUtil;
import com.banger.zeromq.util.FileUtil;
import com.microcredit.adapter.bean.ResIdBean;
import com.microcredit.adapter.bus.CenterAdapterInf;
import com.microcredit.adapter.widget.CenterAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Xuchaowen on 2016/7/13.
 * 离线数据包列表
 */
@EFragment(R.layout.fragment_off_package)
public class OffPackageListFragment extends Fragment {
    private static final String TAG = "OffPackageListFragment";
    @ViewById
    ListView offData_list;
    @Bean(ProductBusinessImp.class)
    ProductBusinessInt productBus;
    private Map<String,String> map =null;
    private CenterAdapter adapter;
    private ResIdBean resIdBean;
    ArrayList<String> list=new ArrayList<>();

    @AfterViews
    void initViews(){
//        getPermissions();
        initData();
        initListViews();
        adapter = new CenterAdapter(new CenterAdapterInf() {
            @Override
            public void bind(View view, ResIdBean resIdBean) {
                OffPackageListHodler holder = new OffPackageListHodler();
                resIdBean.setAdpterObject(holder);
                holder.setDataPackageName((TextView) view.findViewById(R.id.package_name));
                holder.setOffDownload((RelativeLayout) view.findViewById(R.id.iv_off));
                holder.setProgress((RoundProgressBar) view.findViewById(R.id.pb_up));
            }

            @Override
            public void setData(ResIdBean resIdBean, int position) {
                OffPackageListHodler holder  = (OffPackageListHodler) resIdBean.getAdpterObject();
//                LocalRecordBean bean = (LocalRecordBean) resIdBean.getList().get(position);
                holder.getDataPackageName().setText(resIdBean.getList().get(position).toString());
            }

            @Override
            public void setListener(Object object, final Object data) {
                OffPackageListHodler holder = (OffPackageListHodler) object;

//                LocalRecordBean bean = (LocalRecordBean) data;
//                ContentClickListener addCheckListener = new ContentClickListener(getActivity(), null, holder, new ContentCallBack() {
//                    @Override
//                    public void Click(Context context, Object bean, Object holder) {
////                        LocalRecordBean recordBean = (LocalRecordBean) bean;
////                        OffPackageListHodler hodler= (OffPackageListHodler) holder;
////                        hodler.getOffDownload().setVisibility(View.GONE);
////                        hodler.getProgress().setVisibility(View.VISIBLE);
////                        hodler.getProgress().setProgress(50);
//
//                    }
//                });
                holder.getOffDownload().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null!=map&&null!=map.get(data.toString())){
                            Log.e(TAG, "onClick: "+data.toString() );
                            updateProductDB(map.get(data.toString()));
                            OfflineImportHistory OfflineImportHistory = new OfflineImportHistory();
                            OfflineImportHistory.setOfflineName(data.toString());
                            OfflineImportHistory.setImportTime(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
                            OfflineImportHistory.save();
                            DataSupport.findAll(OfflineImportHistory.class);
                            ToastUtil.showShortToast(getActivity(),"导入成功");
                        }
                    }
                });
            }
        }, resIdBean);

        offData_list.setAdapter(adapter);
    }
    void updateProductDB(String path) {
        //Constants.CLASS_PATH + "offline.txt"
        String string = FileUtils.ReadTxtFile(path);
        ListProducts listProducts = GsonUtil.getInstance().json2Bean(string, ListProducts.class);

        if (null != listProducts && null != listProducts.getProduct()) {
            List<ProductDetailInfo> list = listProducts.getProduct();
            for (int i = 0; i < list.size(); i++) {
                ProductDetailInfo info = list.get(i);
                productBus.updateProductDB(info);
                Log.e(TAG, "aa: " + info.toString());
            }
            List<ProductDetailInfo> list1=DBVideoUtils.findAllProduct();
            Log.e(TAG, "updateProductDB: ==="+list1.get(0).getBizType());

            if(null != listProducts && null != listProducts.getBizandring()){
                DBVideoUtils.deleteBusinessAll();
                ArrayList<BusinessDataInfo> bizandringlist=listProducts.getBizandring();
                DBVideoUtils.saveBizType(bizandringlist);
            }

            List<BizType> bizList = DBVideoUtils.findAllBizType();
            Log.e(TAG, "bizList: " + bizList.size());
            List<ProductClass> productList = DataSupport.findAll(ProductClass.class);
            Log.e(TAG, "productList: " + productList.size());
            List<ProductDetailInfo> productDetailInfoList =DataSupport.findAll(ProductDetailInfo.class);
//            Log.e(TAG, "productDetailInfoList: " + productDetailInfoList.size());
            Log.e(TAG, "productDetailInfoList: " + productDetailInfoList.get(0).toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        adapter.notifyDataSetInvalidated();
    }

    private void initData() {
        //查询指定文件夹下 的所有文件
        list.clear();
        if (null!=map){
            map= null;
        }
        map =FileUtils.getMediaVideo(Constants.OFFLINE_PATH);
        Log.e(TAG, "initData: "+map.size() );
        Set set = map.keySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            list.add(key);
            Log.e(TAG, "initData: --key---"+key+"---value---"+map.get(key) );
        }
    }
    private void initListViews() {
        resIdBean = new ResIdBean();
        resIdBean.setLayoutId(R.layout.item_data_package);
        resIdBean.setContext(getActivity());
        resIdBean.setList(list);
    }

    @Click
    void ll_path(){

  }



}
