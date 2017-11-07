package com.banger.videorecord.mouble.record.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.banger.videorecord.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

	List<Fragment> fragmentList = new ArrayList<Fragment>();
	private String tabTitles[] = new String[]{"本地记录","上传队列","上传成功"};
	private Context context;
	public FragmentAdapter(FragmentManager fm,Context context, List<Fragment> fragmentList) {
		super(fm);
		this.context = context;
		this.fragmentList = fragmentList;
	}
	public View getTabView(int position) {
		View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
		TextView tv = (TextView) v.findViewById(R.id.news_title);
		tv.setText(tabTitles[position]);
		ImageView img = (ImageView) v.findViewById(R.id.imageView);
		//img.setImageResource(imageResId[position]);
		return v;
	}
	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}
	@Override
	public CharSequence getPageTitle(int position) {
		return tabTitles[position];
	}



}
