package com.mostafiz.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(@Nullable Context context) {
        super(context, "expense_tracker", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table expense(id INTEGER primary key autoincrement, date TEXT,category TEXT, description TEXT, amount DOUBLE ) ");
        db.execSQL("create table income(id INTEGER primary key autoincrement, date TEXT,category TEXT, description TEXT, amount DOUBLE ) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists expense");
        db.execSQL("drop table if exists income");

    }


    //--------------------------------------------------------------------------------------

    public void addexpense(String date,String category,String description,Double amount){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("date",date);
        contentValues.put("category",category);
        contentValues.put("description",description);
        contentValues.put("amount",amount);
        db.insert("expense",null,contentValues);
    }




}