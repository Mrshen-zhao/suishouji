package com.matianyi.accountingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.matianyi.accountingapp.R;

import java.util.List;

public class GeneralStatisticListViewAdapter extends BaseAdapter {

    private List<String> statements;
    private LayoutInflater inflater;
    public GeneralStatisticListViewAdapter(){}

    public GeneralStatisticListViewAdapter(List<String> statements, Context context){
        this.statements = statements;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return statements==null?0:statements.size();
    }

    @Override
    public String getItem(int position) {
        return statements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.general_statistic_cell_list_view, null);
        String statement = getItem(position);
        TextView textView = view.findViewById(R.id.textView_statements);
        textView.setText(getItem(position));
        return view;
    }
}
