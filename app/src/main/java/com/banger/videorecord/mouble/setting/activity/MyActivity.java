package com.banger.videorecord.mouble.setting.activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.util.Constants;

public class MyActivity extends TabActivity {
	private static final String TAG = "MyActivity";
	private TabHost tab ;
	private LayoutInflater layoutInflater ;

    /** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_home);
		init();
		tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				Log.e(TAG, "onTabChanged: "+(tabId) );

			}
		});
	}

	private void init(){
		tab = getTabHost();
		layoutInflater = LayoutInflater.from(this);
		int count = Constants.HOME_TAB_ITEM_CLASS_ARRAY.length;
		for(int i=0;i<count;i++){
			TabHost.TabSpec tabSpec = tab.newTabSpec(Constants.HOME_TAB_ITEM_TAG_ARRAY[i]).
					setIndicator(getTabItemView(i)).
					setContent(getTabItemIntent(i));
			tab.addTab(tabSpec);
			tab.getTabWidget().getChildAt(i).setBackgroundResource(R.color.darkgray);
		}
	}

	private View getTabItemView(int index){
		//载入新的资源
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);
		ImageView imageView = (ImageView)view.findViewById(R.id.imageview);
		if (imageView != null)
		{
			imageView.setImageResource(Constants.HOME_TAB_ITEM_IMAGEVIEW_ARRAY[index]);
		}
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(Constants.HOME_TAB_ITEM_TEXTVIEW_ARRAY[index]);
		return view;
	}
	private Intent getTabItemIntent(int index)
	{
		Intent intent = new Intent(this, Constants.HOME_TAB_ITEM_CLASS_ARRAY[index]);
		return intent;
	}
}