package com.matianyi.accountingapp.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.matianyi.accountingapp.R;
import com.matianyi.accountingapp.bean.AboutPageListItemBean;

import java.util.List;

public class AboutPageListViewApapter extends BaseAdapter {

    private List<AboutPageListItemBean> aboutPageListItemBeans;
    private LayoutInflater inflater;
    public AboutPageListViewApapter(){}

    public AboutPageListViewApapter(List<AboutPageListItemBean> aboutPageListItemBeans, Context context){
        this.aboutPageListItemBeans = aboutPageListItemBeans;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return aboutPageListItemBeans == null?0:aboutPageListItemBeans.size();
    }

    @Override
    public AboutPageListItemBean getItem(int position) {
        return aboutPageListItemBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.about_page_listitem_view, null);
        AboutPageListItemBean aboutPageListItemBean = getItem(position);

        ImageView imageView = view.findViewById(R.id.about_page_item_icon);
        TextView textView = view.findViewById(R.id.about_page_info);

        imageView.setImageResource(aboutPageListItemBean.getItemIcon());
        textView.setText(aboutPageListItemBean.getInfo());
        textView.setGravity(Gravity.BOTTOM);

        return view;
    }
}
