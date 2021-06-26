package com.matianyi.accountingapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.matianyi.accountingapp.R;
import com.matianyi.accountingapp.adapter.AboutPageListViewApapter;
import com.matianyi.accountingapp.bean.AboutPageListItemBean;

import java.util.LinkedList;

public class AboutPageActivity extends AppCompatActivity {

    private static String TAG = "AboutPageActivity";

    private ListView listView;
    private AboutPageListViewApapter listViewApapter;
    LinkedList<AboutPageListItemBean> aboutPageListItemBeans;
    LinkedList<Integer> iconResources;
    LinkedList<String> info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        listView = findViewById(R.id.about_page_list_view);
        aboutPageListItemBeans = new LinkedList<>();
        iconResources = new LinkedList<>();
        iconResources.add(R.drawable.person_item);
        iconResources.add(R.drawable.version);
        info = new LinkedList<>();
        info.add("作者: 发际线总和我作队");
        info.add("Version: 0.0.1beta");
        for (int i=0; i<2; i++){
            AboutPageListItemBean aboutPageListItemBean = new AboutPageListItemBean();
            aboutPageListItemBean.setItemIcon(iconResources.get(i));
            aboutPageListItemBean.setInfo(info.get(i));
            aboutPageListItemBeans.add(aboutPageListItemBean);
        }
        listViewApapter = new AboutPageListViewApapter(aboutPageListItemBeans, AboutPageActivity.this);
        listView.setAdapter(listViewApapter);
        handleBack();
    }

    public void handleBack(){
        findViewById(R.id.about_page_back_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
