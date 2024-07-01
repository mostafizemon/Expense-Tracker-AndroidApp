package com.mostafiz.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(@Nullable Context context) {
        super(context, "expense_tracker", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table expense(id INTEGER primary key autoincrement, date TEXT,category TEXT, description TEXT, amount DOUBLE,currentdate TEXT,monthyear TEXT,year TEXT) ");
        db.execSQL("create table income(id INTEGER primary key autoincrement, date TEXT,category TEXT, description TEXT, amount DOUBLE,currentdate TEXT,monthyear TEXT,year TEXT ) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists expense");
        db.execSQL("drop table if exists income");

    }


    //--------------------------------------------------------------------------------------

    public void addexpense(String date,String category,String description,Double amount,String currentdate,String monthyear,String year){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("date",date);
        contentValues.put("category",category);
        if (description != null) {
            contentValues.put("description", description);
        }
        contentValues.put("amount",amount);
        contentValues.put("currentdate",currentdate);
        contentValues.put("monthyear",monthyear);
        contentValues.put("year",year);
        try {
            db.insert("expense", null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    //------------------------------------------------------------------------------------------------

    public void addincome(String date,String category,String description,Double amount,String currentdate,String monthyear,String year){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("date",date);
        contentValues.put("category",category);
        if (description != null) {
            contentValues.put("description", description);
        }
        contentValues.put("amount",amount);
        contentValues.put("currentdate",currentdate);
        contentValues.put("monthyear",monthyear);
        contentValues.put("year",year);
        try {
            db.insert("income", null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


    //------------------------------------------------------------------------------------------------
    public double calculatetotalexpense(){
        double totalexpense=0;
        String monthandyear=getcurrentmonthyear();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from expense where monthyear like ?",new String[]{monthandyear+"%"});
        if (cursor!=null & cursor.getCount()>0){
            while (cursor.moveToNext()){
                double expense=cursor.getDouble(4);
                totalexpense=totalexpense+expense;
            }
        }
        return totalexpense;
    }

    //------------------------------------------------------------------------------------------------
    public double calculatetotalincome(){
        double totalincome=0;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String monthandyear=getcurrentmonthyear();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from income where monthyear like ?",new String[]{monthandyear+"%"});
        if (cursor!=null & cursor.getCount()>0){
            while (cursor.moveToNext()){
                double income=cursor.getDouble(4);
                totalincome=totalincome+income;
            }
        }
        return totalincome;
    }

    //-----------------------------------------------------------------------------------------------------
    public Cursor getExpenseSumAmountByCategory() {
        SQLiteDatabase db = this.getReadableDatabase();
        String monthandyear=getcurrentmonthyear();
        Cursor cursor = db.rawQuery(
                "SELECT category, SUM(amount) as total FROM expense WHERE monthyear LIKE ? GROUP BY category",
                new String[]{monthandyear + "%"});
        return cursor;
    }
    public Cursor getIncomeSumAmountByCategory() {
        SQLiteDatabase db = this.getReadableDatabase();
        String monthandyear=getcurrentmonthyear();
        Cursor cursor = db.rawQuery(
                "SELECT category, SUM(amount) as total FROM income WHERE monthyear LIKE ? GROUP BY category",
                new String[]{monthandyear + "%"});
        return cursor;
    }

    public Cursor getIncomeSumAmountByMonthYear() {
        SQLiteDatabase db = this.getReadableDatabase();
        String monthandyear = getcurrentmonthyear();
        Cursor cursor = db.rawQuery(
                "SELECT monthyear, SUM(amount) as total FROM income WHERE monthyear LIKE ? GROUP BY monthyear",
                new String[]{monthandyear + "%"});
        return cursor;
    }

    public Cursor getExpenseSumAmountByMonthYear() {
        SQLiteDatabase db = this.getReadableDatabase();
        String monthandyear = getcurrentmonthyear();
        Cursor cursor = db.rawQuery(
                "SELECT monthyear, SUM(amount) as total FROM expense WHERE monthyear LIKE ? GROUP BY monthyear",
                new String[]{monthandyear + "%"});
        return cursor;
    }

    //-----------------------------------------------------------------------------------------------------
    private String getcurrentmonthyear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        return sdf.format(new java.util.Date());
    }

    public Cursor getexpensebymonth() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT monthyear, SUM(amount) as total FROM expense GROUP BY monthyear ORDER BY monthyear ASC";
        return db.rawQuery(query, null);
    }

    public Cursor getincomebymonth() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT monthyear, SUM(amount) as total FROM income GROUP BY monthyear ORDER BY monthyear ASC";
        return db.rawQuery(query, null);
    }




    public Cursor getDailyExpensesAndIncomes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query =
                "SELECT date, " +
                        "SUM(expense_amount) AS expense, " +
                        "SUM(income_amount) AS income " +
                        "FROM ( " +
                        "    SELECT currentdate AS date, amount AS expense_amount, 0 AS income_amount " +
                        "    FROM expense " +
                        "    UNION ALL " +
                        "    SELECT currentdate AS date, 0 AS expense_amount, amount AS income_amount " +
                        "    FROM income " +
                        ") " +
                        "GROUP BY date " +
                        "ORDER BY date ASC";
        return db.rawQuery(query, null);
    }
    String table=HomeFragment.selected;
    String category=HomeFragment.showcategory;
    public Cursor showcategorywisealldata(String table, String category) {
        String monthyear = getcurrentmonthyear();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id, amount, description, date FROM " + table + " WHERE category = ? AND monthyear = ?";
        return db.rawQuery(query, new String[]{category, monthyear});
    }

    public Cursor getRecentTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM (" +
                "SELECT 'expense' AS type, id, date, category, description, amount FROM expense " +
                "UNION ALL " +
                "SELECT 'income' AS type, id, date, category, description, amount FROM income " +
                ") " +
                "ORDER BY date DESC LIMIT 10";
        return db.rawQuery(query, null);
    }

    public void delete(String table,String id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+table+" where id like "+id);
    }

    public boolean updateTransaction(String id, String type, String amount, String description, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("amount", amount);
        contentValues.put("description", description);
        contentValues.put("category", category);

        int result = db.update(type, contentValues, "id = ?", new String[]{id});
        return result > 0;
    }











}
