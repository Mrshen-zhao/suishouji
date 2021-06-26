package com.matianyi.accountingapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.matianyi.accountingapp.R;
import com.matianyi.accountingapp.bean.RecordBean;
import com.matianyi.accountingapp.util.DateUtil;
import com.matianyi.accountingapp.util.GlobalUtil;

import java.util.LinkedList;

public class ListViewAdapter extends BaseAdapter {

    private LinkedList<RecordBean> records = new LinkedList<>();

    private LayoutInflater mInflater;

    private Context mContext;

    private final String TAG = "ListViewAdapter";

    public ListViewAdapter(Context context){
        this.mContext = context;
        Log.d(TAG, "ListViewAdapter: mContext " + mContext);
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(LinkedList<RecordBean> records){
        this.records = records;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListViewHolder holder;

        /*if(convertView == null){
            convertView = mInflater.inflate(R.layout.cell_list_view, null);

            RecordBean recordBean = (RecordBean) getItem(position);
            holder = new ListViewHolder(convertView, recordBean);

            convertView.setTag(holder);

            Log.d(TAG, "getView: converView is null, position is: " + position);

        }else{
            holder = (ListViewHolder) convertView.getTag();
            Log.d(TAG, "getView: converView is not null, position is: " + position);
        }*/

        convertView = mInflater.inflate(R.layout.cell_list_view, null);

        RecordBean recordBean = (RecordBean) getItem(position);
        holder = new ListViewHolder(convertView, recordBean);

        convertView.setTag(holder);

        Log.d(TAG, "getView: converView position is: " + position);


        return convertView;
    }
}

class ListViewHolder {

    TextView remarkTV;
    TextView amountTV;
    TextView timeTV;
    ImageView categoryIcon;

    ListViewHolder(View itemView, RecordBean record){
        remarkTV = itemView.findViewById(R.id.textView_remark);
        amountTV = itemView.findViewById(R.id.textView_amount);
        timeTV = itemView.findViewById(R.id.textView_time);
        categoryIcon = itemView.findViewById(R.id.imageView_category);

        remarkTV.setText(record.getRemark());

        if (record.getType() == 1){
            amountTV.setText("- " + record.getAmount());
        }else{
            amountTV.setText("+ " + record.getAmount());
        }

        timeTV.setText(DateUtil.getFormattedTime(record.getTimeStamp()));

        categoryIcon.setImageResource(GlobalUtil.getInstance().getResourceIcon(record.getCategory()));
    }

}


