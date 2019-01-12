package com.example.ludwigprandtl.medadvisor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.Serializable;

public class MyDatabase extends SQLiteOpenHelper implements Serializable {
    private static final String Database_Name = "Selected_Symptoms_list.db";
    private static final String Table_Name = "Symptoms";
    private static final String Table_Name2 = "Drugs";
    private static final String Table_Name3 = "Gen";
    private static final int Version_Number=4;
    //private static final String ID="_id";
    private static final String symptom = "Selected_Symptoms";
    private static final String drug = "MyDrugs";
    private static final String gen = "MyGen";
    private Context context;
    public MyDatabase(Context context) {
        super(context, Database_Name, null, Version_Number);
        this.context=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Toast.makeText(context,"Database is Created",Toast.LENGTH_SHORT).show();
            db.execSQL("create table Symptoms (_id integer primary key AUTOINCREMENT,Selected_Symptoms varchar(20));");
            db.execSQL("create table Drugs (_id integer primary key AUTOINCREMENT, MyDrugs varchar(20));");
        }
        catch (Exception e){
            Toast.makeText(context, "Exception"+e,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if(oldVersion<newVersion){
                db.execSQL("create table Gen (_id integer primary key AUTOINCREMENT, MyGen varchar(20));");
            }

        }
        catch(Exception e){

        }
    }

    public long insertData(String s,String table) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if(table.equals(Table_Name)) {
            contentValues.put(symptom, s);
            long rowId = sqLiteDatabase.insert(Table_Name, null, contentValues);
            return rowId;
        }
        else if(table.equals(Table_Name2))
        {
            contentValues.put(drug, s);
            long rowId = sqLiteDatabase.insert(Table_Name2, null, contentValues);
            return rowId;
        }
        else{
            contentValues.put(gen, s);
            long rowId = sqLiteDatabase.insert(Table_Name3, null, contentValues);
            return rowId;
        }
    }
    public Cursor readData(String table)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor;
        if(table.equals(Table_Name))
        cursor = sqLiteDatabase.rawQuery("Select distinct Selected_Symptoms from Symptoms;",null);
        else if(table.equals(Table_Name2))
            cursor = sqLiteDatabase.rawQuery("Select distinct MyDrugs from Drugs;",null);
        else
            cursor = sqLiteDatabase.rawQuery("Select distinct MyGen from Gen;",null);
        return cursor;
    }

    public void clearData(String table) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if(table.equals(Table_Name))
        sqLiteDatabase.delete(Table_Name,null,null);
        else if(table.equals(Table_Name2))
            sqLiteDatabase.delete(Table_Name2,null,null);
        else
            sqLiteDatabase.delete(Table_Name3,null,null);
    }

    public long deleteData(String data,String table) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if(table.equals(Table_Name)) {
            long row_id = sqLiteDatabase.delete(Table_Name, symptom + "=?", new String[]{data});
            return  row_id;
        }
        else if(table.equals(Table_Name2))
        {
            long row_id = sqLiteDatabase.delete(Table_Name2,  drug+ "=?", new String[]{data});
            return  row_id;
        }
        else
        {
            long row_id = sqLiteDatabase.delete(Table_Name3, gen+ "=?", new String[]{data});
            return  row_id;
        }

    }
}
