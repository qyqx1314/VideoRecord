package com.banger.videorecord.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: jiumin Zhu
 * Date: 13-10-10
 * Time: 上午9:25
 */
public class FragmentTabAdapterForList implements View.OnClickListener{
    // 一个tab页面对应一个Fragment
    private List<Fragment> fragments;
    // 用于切换tab
    private View[] viewData;
    // Fragment所属的Activity
    private FragmentActivity fragmentActivity;
    // Activity中所要被替换的区域的id
    private int fragmentContentId;
    // 当前Tab页面索引
    private int currentTab;
    // 用于让调用者在切换tab时候增加新的功能
    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener;
    private boolean stateClick = true;
    public FragmentTabAdapterForList(FragmentActivity fragmentActivity,
                                     List<Fragment> fragments, int fragmentContentId, View[] viewData) {
        this.fragments = fragments;
        this.viewData = viewData;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(0));
        ft.commit();
        for (View view:viewData) {
            view.setOnClickListener(this);
        }



    }

    @Override
    public void onClick(View view) {
        for(int i = 0; i < viewData.length; i++){
            if(viewData[i].getId() == view.getId()){
                Fragment fragment = fragments.get(i);
                FragmentTransaction ft = obtainFragmentTransaction(i);
                // 暂停当前tab
                getCurrentFragment().onPause();
                //              getCurrentFragment().onStop(); // 暂停当前tab
                if(fragment.isAdded()){
                    //                    fragment.onStart(); // 启动目标tab的onStart()
                    fragment.onResume(); // 启动目标tab的onResume()
                }else{
                    ft.add(fragmentContentId, fragment);
                }
                showTab(i); // 显示目标tab
                ft.commit();
                // 如果设置了切换tab额外功能功能接口

                if(null != onRgsExtraCheckedChangedListener){
                    onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(view,
                             i);
                }

            }
        }

    }

    /**
     * 切换tab
     * @param idx 目标tab
     */
    private void showTab(int idx){
        for(int i = 0; i < fragments.size(); i++){
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            if(idx == i){
                ft.show(fragment);
            }else{
                ft.hide(fragment);
            }
            ft.commit();
        }
        // 更新目标tab为当前tab
        currentTab = idx;
    }

    /**
     * 获取一个带动画的FragmentTransaction
     * @param index 目标tab
     * @return FragmentTransaction
     */
    private FragmentTransaction obtainFragmentTransaction(int index){
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        // 设置切换动画
//        if(index > currentTab){
//            ft.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out);
//        }else{
//            ft.setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out);
//        }
        return ft;
    }

//    public int getCurrentTab() {
//        return currentTab;
//    }

    public Fragment getCurrentFragment(){
        return fragments.get(currentTab);
    }

//    public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
//        return onRgsExtraCheckedChangedListener;
//    }

    public void setOnRgsExtraCheckedChangedListener(OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
        this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
    }

    /**
     *  切换tab额外功能功能接口
     */
    public static class OnRgsExtraCheckedChangedListener{
        public void OnRgsExtraCheckedChanged(View view,  int index){
        }
    }

    public void setStateClick(boolean stateClick) {
        if (stateClick){
            for (View view:viewData) {
                view.setOnClickListener(this);
            }
        }else {
            for (View view:viewData) {
                view.setOnClickListener(null);
            }
        }
    }
}
