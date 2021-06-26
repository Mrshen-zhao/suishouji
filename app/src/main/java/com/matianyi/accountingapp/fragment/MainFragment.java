package com.matianyi.accountingapp.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.matianyi.accountingapp.ABaseFragment;
import com.matianyi.accountingapp.R;
import com.matianyi.accountingapp.activity.AddRecordActivity;
import com.matianyi.accountingapp.adapter.ListViewAdapter;
import com.matianyi.accountingapp.bean.RecordBean;
import com.matianyi.accountingapp.util.DateUtil;
import com.matianyi.accountingapp.util.GlobalUtil;

import java.util.LinkedList;

@SuppressLint("ValidFragment")
public class MainFragment extends ABaseFragment implements AdapterView.OnItemLongClickListener {

    private static final String TAG = "MainFragment";
    private View rootView;
    private TextView textView;
    private ListView listView;
    private ListViewAdapter listViewAdapter;

    private LinkedList<RecordBean> records;


    private String date = "";

    // MainActivity activity;

    @SuppressLint("ValidFragment")
    public MainFragment(String date){
        this.date = date;
        records = GlobalUtil.getInstance().databaseHelper.readRecords(date);

        // listViewAdapter = new ListViewAdapter(this.getActivity().getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main,container,false);
        initView();
        return rootView;
    }


    public void reload(){

        records = GlobalUtil.getInstance().databaseHelper.readRecords(date);

//        if (listViewAdapter == null){
//            listViewAdapter = new ListViewAdapter(getActivity().getApplicationContext());
//        }
        // Log.d(TAG, "reload: " + records);

        getAvailableActivity(activity -> {
            listViewAdapter = new ListViewAdapter(activity.getApplicationContext());
            Log.d(TAG, "onActivityEnabled: " + activity + " context: " + activity.getApplicationContext());
        });



        listViewAdapter.setData(records);
        listView.setAdapter(listViewAdapter);


        if (listViewAdapter.getCount()>0){
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }
    }

    private void initView(){


        textView = rootView.findViewById(R.id.day_text);
        listView = rootView.findViewById(R.id.listView);
        textView.setText(date);
        listViewAdapter = new ListViewAdapter(getContext());
        listViewAdapter.setData(records);
        listView.setAdapter(listViewAdapter);

        if (listViewAdapter.getCount() > 0){
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }

        textView.setText((DateUtil.getDateTitle(date) + "日"));

        // 设置每一笔账目的长按
        listView.setOnItemLongClickListener(this);
    }

    public int getTotalCost(){
        double totalCost = 0;
        for (RecordBean record: records){
            if (record.getType()==1){
                totalCost += record.getAmount();
            }
        }
        return (int) totalCost;
    }

    public int getTotalIncome() {
        double totalIncome = 0;
        for (RecordBean record : records) {
            if (record.getType() == 2){
                totalIncome += record.getAmount();
            }
        }
        return (int) totalIncome;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //Log.d(TAG, "onItemLongClick: index " + position + " clicked.");
        // show dialog
        showDialog(position);
        return false;
    }

    // 长按的show dialog方法
    private void showDialog(int index){
        final RecordBean selectedRecords = records.get(index);

        final String[] options = {"删除", "修改"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.create();

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: " + which + options[which]);
                // 0 -> remove, 1 -> edit
                if (which == 0) {
                    // remove
                    String uuid = selectedRecords.getUuid();
                    GlobalUtil.getInstance().databaseHelper.removeRecord(uuid);
                    // refresh
                    reload();
                    GlobalUtil.getInstance().mainActivity.updateHeader();
                } else if (which == 1) {
                    // edit
                    Intent intent = new Intent(getActivity(), AddRecordActivity.class);
                    Bundle extra = new Bundle();
                    extra.putSerializable("record", selectedRecords);
                    intent.putExtras(extra);
                    startActivityForResult(intent, 1);
                }
            }
        });
        builder.setNegativeButton("取消", null);

        builder.create().show();
    }

}