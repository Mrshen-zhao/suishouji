package com.matianyi.accountingapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.matianyi.accountingapp.bean.RecordBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RecordDatabaseHelper extends SQLiteOpenHelper {

    private String TAG ="RecordDatabaseHelper";

    public static final String DB_NAME = "Record";

    private static final String CREATE_RECORD_DB = "create table Record ("
            + "id integer primary key autoincrement, "
            + "uuid text, "
            + "type integer, "
            + "category, "
            + "remark text, "
            + "amount double, "
            + "time integer, "
            + "date date )";

    public RecordDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        Map<String, Double> mostCategory = getMostIncomeCategory();

        Log.d(TAG, "RecordDatabaseHelper: " + mostCategory);

        Log.d(TAG, "RecordDatabaseHelper: " + getAvailableMonths());

        Log.d(TAG, "RecordDatabaseHelper: " + getIncomeCategoryTotalAmountThisMonth("04", "Salary"));

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECORD_DB);
        // Log.d(TAG, "onCreate: " + CREATE_RECORD_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addRecord(RecordBean bean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uuid",bean.getUuid());
        values.put("type",bean.getType());
        values.put("category",bean.getCategory());
        values.put("remark",bean.getRemark());
        values.put("amount",bean.getAmount());
        values.put("date",bean.getDate());
        values.put("time",bean.getTimeStamp());
        db.insert(DB_NAME,null,values);
        //Log.d(TAG,bean.getUuid()+"added "+values);
        values.clear();
    }

    public void  removeRecord(String uuid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_NAME,"uuid = ?",new String[]{uuid});
    }

    public void editRecord(String uuid,RecordBean record){
        removeRecord(uuid);
        record.setUuid(uuid);
        addRecord(record);
    }

    public LinkedList<RecordBean> readRecords(String dateStr){
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where date = ? order by time asc",new String[]{dateStr});
        if (cursor.moveToFirst()){
            do{
                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                long timeStamp = cursor.getLong(cursor.getColumnIndex("time"));

                RecordBean record = new RecordBean();
                record.setUuid(uuid);
                record.setType(type);
                record.setCategory(category);
                record.setRemark(remark);
                record.setAmount(amount);
                record.setDate(date);
                record.setTimeStamp(timeStamp);

                records.add(record);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    public LinkedList<String> getAvailableDate(){

        LinkedList<String> dates = new LinkedList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record order by date asc",new String[]{});
        if (cursor.moveToFirst()){
            do{
                String date = cursor.getString(cursor.getColumnIndex("date"));
                if (!dates.contains(date)){
                    dates.add(date);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }

    // 获取数据库中所有可用月份
    public ArrayList<String> getAvailableMonths(){
        ArrayList<String> availableMonths = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        //String currentDate = DateUtil.getFormattedDate();
        //availableMonths.add(currentDate.split("-")[1]);

        String sql = "select date from Record";

        try (Cursor cursor = db.rawQuery(sql, new String[]{})){
            if (cursor.moveToFirst()) {
                do {
                    String candidateMonth = cursor.getString(cursor
                            .getColumnIndex("date"))
                            .split("-")[1];
                    if (!availableMonths.contains(candidateMonth)) {
                        availableMonths.add(candidateMonth);
                    }
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.d(TAG, "getCostThisMonth exception: " + e.toString());
        }

        return availableMonths;
    }

    // 获取本月总支出
    public double getExpenditureThisMonth(String thisMonth){
        double expenditureThisMonth = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = DateUtil.getFormattedDate();

        String firstDateThisMonth = currentDate.split("-")[0] + "-"
                                + thisMonth + "-00";
        String lastDateThisMonth = currentDate.split("-")[0] + "-"
                                + thisMonth + "-32";

        String sql = "select sum(amount) from Record where date > ? and date < ? and type = 1";

        try (Cursor cursor = db.rawQuery(sql, new String[]{firstDateThisMonth, lastDateThisMonth})) {
            if (cursor.moveToFirst()) {
                do {
                    expenditureThisMonth = cursor.getDouble(0);
                    Log.d(TAG, "amount index " + expenditureThisMonth);
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.d(TAG, "getCostThisMonth exception: " + e.toString());
        }
        return expenditureThisMonth;
    }

    // 获取本月总收入
    public double getIncomeThisMonth(String thisMonth){
        double incomeThisMonth = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = DateUtil.getFormattedDate();

        String firstDateThisMonth = currentDate.split("-")[0] + "-"
                + thisMonth + "-00";
        String lastDateThisMonth = currentDate.split("-")[0] + "-"
                + thisMonth + "-32";

        String sql = "select sum(amount) from Record where date > ? and date < ? and type=2";

        try (Cursor cursor = db.rawQuery(sql, new String[]{firstDateThisMonth, lastDateThisMonth})) {
            if (cursor.moveToFirst()) {
                do {
                    incomeThisMonth = cursor.getDouble(0);
                    Log.d(TAG, "amount index " + incomeThisMonth);
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.d(TAG, "getCostThisMonth exception: " + e.toString());
        }
        Log.d(TAG, "getIncomeThisMonth: " + incomeThisMonth);
        return incomeThisMonth;
    }

    // 获取当前种类本月总收入
    public double getIncomeCategoryTotalAmountThisMonth(String thisMonth, String category){
        double incomeCategoryTotalAmountThisMonth = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = DateUtil.getFormattedDate();

        String firstDateThisMonth = currentDate.split("-")[0] + "-"
                + thisMonth + "-00";
        String lastDateThisMonth = currentDate.split("-")[0] + "-"
                + thisMonth + "-32";

        String sql = "select sum(amount) from Record where date > ? and date < ? and category = ? and type = 2";

        try (Cursor cursor = db.rawQuery(sql, new String[]{firstDateThisMonth, lastDateThisMonth, category})) {
            if (cursor.moveToFirst()) {
                do {
                    incomeCategoryTotalAmountThisMonth = cursor.getDouble(0);// 获取当月当前种类总数额
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.d(TAG, "getCostThisMonth exception: " + e.toString());
        }

        return incomeCategoryTotalAmountThisMonth;
    }

    public Map<String, Double> getMostIncomeCategory(){
        Map<String, Double> mostCategory = new HashMap<>();
        ArrayList<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "select distinct category from Record where type = 2";

        // 获取所有的存在种类
        try (Cursor cursor = db.rawQuery(sql, new String[]{})) {
            {
                if (cursor.moveToFirst()) {
                    do {
                        categories.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }
                // Log.d(TAG, "getMostCategory: " + categories);
            }
        }catch (Exception e) {
            Log.d(TAG, "getMostCategory: exception: " + e.toString());
        }

        // 获取每个类别的当月总额
        for (String month:getAvailableMonths()){
            for (String category : categories) {
                if (!mostCategory.containsKey(category)){ // 结果集中不包含当前的键直接put
                    mostCategory.put(category,
                            getIncomeCategoryTotalAmountThisMonth(month, category));
                }else{ // 结果集中已经存在上一个月的键值对
                    double addedValue = mostCategory.get(category); // 获取当前的值
                    addedValue += getIncomeCategoryTotalAmountThisMonth(month, category); // 加上新的值
                    mostCategory.put(category, addedValue); // put操作
                }

            }
        }

        return mostCategory;
    }

    // 获取当前种类本月总支出
    public double getExpenditureCategoryTotalAmountThisMonth(String thisMonth, String category){
        double expenditureCategoryTotalAmountThisMonth = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = DateUtil.getFormattedDate();

        String firstDateThisMonth = currentDate.split("-")[0] + "-"
                + thisMonth + "-00";
        String lastDateThisMonth = currentDate.split("-")[0] + "-"
                + thisMonth + "-32";

        String sql = "select sum(amount) from Record where date > ? and date < ? and category = ? and type = 1";

        try (Cursor cursor = db.rawQuery(sql, new String[]{firstDateThisMonth, lastDateThisMonth, category})) {
            if (cursor.moveToFirst()) {
                do {
                    expenditureCategoryTotalAmountThisMonth = cursor.getDouble(0);// 获取当月当前种类总数额
                    // Log.d(TAG, "amount index " + expenditureCategoryTotalAmountThisMonth);
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.d(TAG, "getCostThisMonth exception: " + e.toString());
        }

        return expenditureCategoryTotalAmountThisMonth;
    }

    public Map<String, Double> getMostExpenditureCategory(){
        Map<String, Double> mostCategory = new HashMap<>();
        ArrayList<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "select distinct category from Record where type = 1";

        // 获取所有的存在种类
        try (Cursor cursor = db.rawQuery(sql, new String[]{})) {
            {
                if (cursor.moveToFirst()) {
                    do {
                        categories.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }
                // Log.d(TAG, "getMostCategory: " + categories);
            }
        }catch (Exception e) {
            Log.d(TAG, "getMostCategory: exception: " + e.toString());
        }
        // 获取每个类别的当月总额
        for (String month:getAvailableMonths()){
            for (String category : categories) {
                if (!mostCategory.containsKey(category)){
                    mostCategory.put(category,
                            getExpenditureCategoryTotalAmountThisMonth(month, category));
                }else {
                    double addedValue = mostCategory.get(category);
                    addedValue += getExpenditureCategoryTotalAmountThisMonth(month, category);
                    mostCategory.put(category, addedValue);
                }
            }
        }
        return mostCategory;

    }
}