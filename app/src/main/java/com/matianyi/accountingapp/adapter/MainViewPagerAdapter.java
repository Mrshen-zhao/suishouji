package com.matianyi.accountingapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.matianyi.accountingapp.fragment.MainFragment;
import com.matianyi.accountingapp.util.DateUtil;
import com.matianyi.accountingapp.util.GlobalUtil;

import java.util.LinkedList;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "MainViewPagerAdapter";

    LinkedList<MainFragment> fragments = new LinkedList<>();

    LinkedList<String> dates = new LinkedList<>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        initFragments();
    }

    private void initFragments(){

        dates =  GlobalUtil.getInstance().databaseHelper.getAvailableDate();

        //Log.d(TAG, "initFragments: " + dates);

        if (!dates.contains(DateUtil.getFormattedDate())){
            dates.addLast(DateUtil.getFormattedDate());
        }

        for(String date:dates){
            MainFragment fragment = new MainFragment(date);
            fragments.add(fragment);
        }
    }


    public int getLastIndex(){
        return getCount() - 1;
    }

    public void reload(){
        for (MainFragment fragment : fragments){
            fragment.reload();
        }
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public String getDateStr(int index){
        return dates.get(index);
    }

    public int getTotalCost(int index){
        return fragments.get(index).getTotalCost();
    }

    public int getTotalIncom(int index){
        return fragments.get(index).getTotalIncome();
    }
}
