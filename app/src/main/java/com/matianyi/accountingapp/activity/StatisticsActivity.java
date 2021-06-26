package com.matianyi.accountingapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.easyandroidanimations.library.BounceAnimation;
import com.matianyi.accountingapp.R;

public class StatisticsActivity extends AppCompatActivity {
    
    private static final String TAG = "StatisticsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // animation
        new BounceAnimation(findViewById(R.id.statistics_activity_page))
                .setBounceDistance(30)
                .setNumOfBounces(1)
                .setDuration(1500)
                .animate();

        
        // 为选项设置监听事件
        // 总体
        final View GeneralView = findViewById(R.id.general_statistics);
        GeneralView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsActivity.this, GeneralStaticsActivity.class);
                startActivity(intent);
            }
        });
        // 总支出
        final View ExpenditureRecentMonthsView = findViewById(R.id.cost_recent_months_layout);
        ExpenditureRecentMonthsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsActivity.this, ExpenditureRecentMonthsActivity.class);
                startActivity(intent);
            }
        });

        // 支出类别
        final View MostExpenditureCategoryView = findViewById(R.id.statistic_most_expenditure_layout);
        MostExpenditureCategoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsActivity.this, MostExpenditureCategoryActivity.class);
                startActivity(intent);
            }
        });

        // 总收入
        final View IncomeRecentMonthsView = findViewById(R.id.income_recent_months_layout);
        IncomeRecentMonthsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsActivity.this, IncomeRecentMonthsActivity.class);
                startActivity(intent);
            }
        });

        // 收入类别
        final View MostIncomeCategoryView = findViewById(R.id.statistic_most_income_category);
        MostIncomeCategoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsActivity.this, MostIncomeCategoryActivity.class);
                startActivity(intent);
            }
        });


        // 更多
        final View MoreStatisticItemView = findViewById(R.id.more_statistic_item_layout);
        MoreStatisticItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"该功能尚未添加，敬请期待", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
