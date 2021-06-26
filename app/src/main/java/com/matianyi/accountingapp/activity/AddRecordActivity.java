package com.matianyi.accountingapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.matianyi.accountingapp.R;
import com.matianyi.accountingapp.adapter.CategoryRecyclerAdapter;
import com.matianyi.accountingapp.bean.RecordBean;
import com.matianyi.accountingapp.util.GlobalUtil;


public class AddRecordActivity extends AppCompatActivity implements View.OnClickListener, CategoryRecyclerAdapter.OnCategoryClickListener {

    private static String TAG = "AddRecordActivity";

    private EditText editText;

    private String userInput = "";

    private TextView amountText;

    private RecyclerView recyclerView;
    private CategoryRecyclerAdapter recyclerAdapter;

    private String category = "支出";
    private RecordBean.RecordType type = RecordBean.RecordType.RECORD_TYPE_EXPENSE;
    private String remark = category;

    RecordBean record = new RecordBean();

    private boolean inEdit = false;

    private int flag = 1;

    private boolean dotClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        handleView();
        handleListener();
        handleDot();
        handleTypeChange();
        handleBackSpace();
        handleDone();
        handleClear();

        RecordBean record = (RecordBean) getIntent().getSerializableExtra("record");

        if (record != null){
            Log.d(TAG, "onCreate: getintent: " + record.getRemark());
            inEdit = true;
            this.record = record;
        }
    }

    private void handleView(){
        amountText = findViewById(R.id.textView_amount);
        editText = findViewById(R.id.editText);

        editText.setText(remark);


        // 设置recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new CategoryRecyclerAdapter(getApplicationContext());
        recyclerView.setAdapter(recyclerAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerAdapter.notifyDataSetChanged();
        recyclerAdapter.setOnCategoryClickListener(this);
    }


    private void handleListener(){
        findViewById(R.id.keyboard_one).setOnClickListener(this);
        findViewById(R.id.keyboard_two).setOnClickListener(this);
        findViewById(R.id.keyboard_three).setOnClickListener(this);
        findViewById(R.id.keyboard_four).setOnClickListener(this);
        findViewById(R.id.keyboard_five).setOnClickListener(this);
        findViewById(R.id.keyboard_six).setOnClickListener(this);
        findViewById(R.id.keyboard_seven).setOnClickListener(this);
        findViewById(R.id.keyboard_eight).setOnClickListener(this);
        findViewById(R.id.keyboard_nine).setOnClickListener(this);
        findViewById(R.id.keyboard_zero).setOnClickListener(this);
    }

    private void handleClear(){
        findViewById(R.id.keyboard_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInput="";
                dotClicked = false;
                updateAmountText();
                amountText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);
            }
        });
    }

    private void handleDot(){
        findViewById(R.id.keyboard_dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 避免出现多个小数点
                if (!userInput.contains(".")){
                    userInput += ".";
                }
                Log.d(TAG, "onClick: dotClicked: inside listener: " + dotClicked);
            }
        });

        Log.d(TAG, "handleDot: dotClicked: outside listener: " + dotClicked);
    }

    private void handleTypeChange(){
        findViewById(R.id.keyboard_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE){
                    type = RecordBean.RecordType.RECORD_TYPE_INCOME;
                }else {
                    type = RecordBean.RecordType.RECORD_TYPE_EXPENSE;
                }

                recyclerAdapter.changeType(type);
                category = recyclerAdapter.getSelected();

                // 修改图标
                if (flag == 0){
                    ((ImageButton)findViewById(R.id.keyboard_type)).setImageResource(R.drawable.ic_compare_arrows_red_400_24dp);
                    flag=1;
                }else {
                    ((ImageButton) findViewById(R.id.keyboard_type)).setImageResource(R.drawable.ic_compare_arrows_green_400_24dp);
                    flag=0;
                }
            }
        });
    }

    private void handleBackSpace(){
        findViewById(R.id.keyboard_backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInput.length() > 0){
                    userInput = userInput.substring(0, userInput.length() - 1);
                }
                // 如果最后一位是小数点，直接删除
                if (userInput.length() > 0 && userInput.charAt(userInput.length() - 1) == '.') {
                    userInput = userInput.substring(0, userInput.length() - 1);
                }
                // 恢复大小
                if (userInput.equals("")){
                    amountText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);
                }
                updateAmountText();
            }
        });
    }

    private void handleDone(){
        findViewById(R.id.keyboard_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userInput.equals("")&&!userInput.equals("0")){
                    double amount = Double.valueOf(userInput);

                    record.setAmount(amount);
                    if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE){
                        record.setType(1);
                    }else{
                        record.setType(2);
                    }

                    record.setCategory(recyclerAdapter.getSelected());
                    record.setRemark(editText.getText().toString());

                    Log.d(TAG, "onClick: record details:"
                            + record.getUuid() + record.getCategory() + record.getAmount());

                    if (inEdit){
                        GlobalUtil.getInstance().databaseHelper.editRecord(record.getUuid(), record);
                    }else {
                        GlobalUtil.getInstance().databaseHelper.addRecord(record);
                    }

                    Log.d(TAG, "onClick: " + userInput);

                    Toast.makeText(getApplicationContext(),"完成", Toast.LENGTH_SHORT).show();

                    finish();

                    // Log.d(TAG, "Done! Record: " + record.getUuid() + " " + record.getCategory() + " " + record.getAmount());
                }else {
                    Toast.makeText(getApplicationContext(),"钱数不可为0", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: " + userInput);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String input = button.getText().toString();
        Log.d(TAG,"Click userinput length is: " + userInput.length());

        // 解决一开始就按下.的问题
        if (userInput.startsWith(".")){
            userInput = "0.";
        }
        int integerLength = 0;

        // 处理过大数据的输入问题
        if (!userInput.contains(".")) {
            integerLength = userInput.length();
            Log.d(TAG, "onClick: integer: " + integerLength);
            if (integerLength >= 3 && integerLength <= 5) {
                GlobalUtil.getInstance().handleTextViewStyle(amountText);
            } else if (integerLength > 5 && integerLength <= 7) {
                GlobalUtil.getInstance().handleTextViewStyle(amountText);
            } else if (integerLength > 7){
                Toast.makeText(getApplicationContext(), "你哪来的那么多钱？", Toast.LENGTH_SHORT).show();
                userInput = amountText.getText().toString();
            }
        }

        // 小数点之后只能有两位
        if (userInput.contains(".")){
            if (userInput.split("\\.").length == 1
                    || userInput.split("\\.")[1].length() < 2)
                userInput += input;
        }else{
            userInput += input;
        }

        updateAmountText();

    }

    // 更新数字面板
    private void updateAmountText(){
        Log.d(TAG, "UserInput is : " + userInput);

        if (userInput.contains(".")){
            // 输入中包含小数点
            if (userInput.split("\\.").length == 1){ // 11.这种情况
                amountText.setText(userInput + "00");
            }else if (userInput.split("\\.")[1].length() == 1){ // 11.1
                amountText.setText(userInput + "0");
            }else if (userInput.split("\\.")[1].length() == 2){ // 11.11
                amountText.setText(userInput);
            }
        }else{ // 处理整数
            if (userInput.equals("")){
                amountText.setText("0.00");
            }else {
                amountText.setText(userInput + ".00");
            }
        }
    }

    @Override
    public void onClick(String category) {
        this.category = category;
        //Log.d(TAG, "Onclick: category" + category);
        editText.setText(category);
    }
}
