package com.banger.videorecord.mouble.setting.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.mouble.record.util.ZipTool;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Xuchaowen on 2016/6/1.
 * 文件列表
 */
public class FileListActivity extends ListActivity {

    //    private ImageView imageBack;
//    private ListView listView;
    private ArrayList<String> items = null;
    private ArrayList<String> paths = null;
    private String rootPath = "/";
    private TextView mPath;
    int msgValue = -1;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        mPath = (TextView) findViewById(R.id.mPath);
        mPath.setTextColor(Color.RED);
        getFileDir(rootPath);
    }

    private void getFileDir(String filePath) {
        mPath.setText(filePath);
        ListView lv = getListView();
        items = new ArrayList<String>();
        paths = new ArrayList<String>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (!filePath.equals(rootPath)) {
            items.add("Back To " + rootPath);
            paths.add(rootPath);
            items.add("Back to ../");
            paths.add(file.getParent());
        }
        for (File fileTemp : files) {
            items.add(fileTemp.getName());
            paths.add(fileTemp.getPath());
        }
        adapter = new ArrayAdapter<String>(FileListActivity.this, R.layout.file_now, items);
        setListAdapter(adapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final File file = new File(paths.get(position));
                if (file.getName().contains(".zip")) {//判断解压或压缩的是文件还是文件夹
                    new AlertDialog.Builder(FileListActivity.this)
                            .setTitle("Message")
                            .setMessage("是否解压此文件")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ZipTool.unzip(file.getAbsolutePath(), file.getParent()+"/AAA");

                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(FileListActivity.this)
                            .setTitle("Message")
                            .setMessage("是否压缩文件")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ZipTool.zip(file.getPath() + ".zip", new File[]{file});
                                }
                            }).show();
                }

                return false;
            }
        });
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final File file = new File(paths.get(position));
        if (file.canRead()) {
            if (file.isDirectory()) {
                getFileDir(paths.get(position));
            } else {
                if (file.getName().contains(".zip")) {//对文件进行解压和压缩
                    new AlertDialog.Builder(this)
                            .setTitle("Message")
                            .setMessage("是否解压")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ZipTool.zip(file.getPath() + ".zip", new File[]{file});
                                    adapter.notifyDataSetChanged();
                                }
                            }).show();

                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Message")
                            .setMessage("是否对" + file.getName() + "进行压缩")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    msgValue = ZipTool.zip(file.getPath()+ ".zip", new File[]{file});
                                    adapter.notifyDataSetChanged();
                                }
                            }).show();

                }
            }
        }
    }

}
