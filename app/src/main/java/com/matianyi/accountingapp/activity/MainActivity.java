package com.matianyi.accountingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.easyandroidanimations.library.BounceAnimation;
import com.matianyi.accountingapp.R;
import com.matianyi.accountingapp.adapter.MainViewPagerAdapter;
import com.matianyi.accountingapp.util.BuilderManager;
import com.matianyi.accountingapp.util.DateUtil;
import com.matianyi.accountingapp.util.GlobalUtil;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    
    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private MainViewPagerAdapter pagerAdapter;

    private TickerView expenditureAmountText;
    private TickerView incomeAmountText;
    private TextView dateText;

    private int currentPagePosition = 0;

    private BoomMenuButton rightBmb;

    RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // animation
        new BounceAnimation(findViewById(R.id.activity_main))
                .setBounceDistance(20)
                .setNumOfBounces(6)
                .setDuration(150)
                .animate();

        GlobalUtil.getInstance().setContext(getApplicationContext());

        GlobalUtil.getInstance().mainActivity = this;

        setMyActionBar();

        // 设置ticker view显示钱数
        handleTickerView();

        // 设置日期显示
        dateText = findViewById(R.id.date_text);

        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(this);

        // 让用户打开时显示最后一天
        viewPager.setCurrentItem(pagerAdapter.getLastIndex());

        try {
            handleFab();
        }catch (Exception e){
            Log.d(TAG, "onCreate: " + e.toString());
        }
        // update header
        updateHeader();

        handleRefreshView();

    }

    // smart refresh
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
//        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
//            @Override
//            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
//                //指定为经典Footer，默认是 BallPulseFooter
//                return new ClassicsFooter(context).setDrawableSize(20);
//            }
//        });
    }


    private void handleTickerView(){
        expenditureAmountText = findViewById(R.id.amount_text);
        expenditureAmountText.setCharacterLists(TickerUtils.provideNumberList());
        incomeAmountText = findViewById(R.id.amount_income_text);
        incomeAmountText.setCharacterLists(TickerUtils.provideNumberList());
    }

    private void setMyActionBar(){
        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setElevation(10);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View actionBar = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) actionBar.findViewById(R.id.title_text);
        mTitleTextView.setText("今日");
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0,0);

        //BoomMenuButton leftBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_left_bmb);
        rightBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_right_bmb);

        rightBmb.setButtonEnum(ButtonEnum.Ham);
        rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_3);
        rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);
        for (int i = 0; i < rightBmb.getPiecePlaceEnum().pieceNumber(); i++)
            //rightBmb.addBuilder(BuilderManager.getHamButtonBuilder());
            //自定义添加方法
            addBuilder();
    }

    // 菜单builder
    private void addBuilder(){
        rightBmb.addBuilder(new HamButton.Builder()
            .normalImageRes(BuilderManager.getImageResource())
                .normalTextRes(BuilderManager.getTextResource())
                .listener(index -> {
                    switch (index) {
                        case 0:
                            Log.d(TAG, "onBoomButtonClick: " + 0);
                            Intent intent0 = new Intent(MainActivity.this, StatisticsActivity.class);
                            startActivity(intent0);
                            break;
                        case 1:
                            Log.d(TAG, "onBoomButtonClick: " + 1);
                            Intent intent1 = new Intent(MainActivity.this, AboutPageActivity.class);
                            startActivity(intent1);
                            break;
                        case 2:
                            Log.d(TAG, "onBoomButtonClick: " + 2);
                            Intent intent2 = new Intent(MainActivity.this, UpdatePageActivity.class);
                            startActivity(intent2);
                            break;
                    }
                })
                .shadowEffect(true)
                .pieceColor(Color.BLACK)
                .normalColor(Color.parseColor("#ffffff"))
                .normalTextColor(Color.BLACK)
                .textGravity(Gravity.CENTER)
                .textSize(24)
        );
    }

    protected void handleFab(){
        // 为+按钮设置启动activity
        findViewById(R.id.floating_add_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                startActivityForResult(intent, 1);

            }
        });

        // 为+按钮设置长按选项
        findViewById(R.id.floating_add_button).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCustomRecordActivity.class);
                startActivityForResult(intent, 2);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
//            pagerAdapter.reload();
//            updateHeader();
            refreshSelf();
        }

        if (requestCode == 2){
            refreshSelf();
        }
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        currentPagePosition = i;
        updateHeader();
    }

    public void updateHeader(){
        String amount = String.valueOf(pagerAdapter.getTotalCost(currentPagePosition));
        String incomeAmount = String.valueOf(pagerAdapter.getTotalIncom(currentPagePosition));
        expenditureAmountText.setText(amount);
        incomeAmountText.setText(incomeAmount);

        String date = pagerAdapter.getDateStr(currentPagePosition);
        dateText.setText(DateUtil.getWeekDay(date));
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    // 按下返回键时将应用放在后台
    // 解决fab重复添加的问题
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        moveTaskToBack(true);
    }

    public void handleRefreshView(){
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(500/*,false*/);//传入false表示刷新失败
                refreshSelf();
            }
        });
        /*refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(300*//*,false*//*);//传入false表示加载失败
            }
        });*/
    }

    public void refreshSelf(){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();//关闭自己
        overridePendingTransition(0, 0);
    }
}