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
    private static final int Version_Number=1;
    //private static final String ID="_id";
    private static final String symptom = "Selected_Symptoms";
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
        }
        catch (Exception e){
            Toast.makeText(context, "Exception"+e,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {

        }
        catch(Exception e){

        }
    }

    public long insertData(String s) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(symptom, s);
        long rowId = sqLiteDatabase.insert(Table_Name, null, contentValues);
        return rowId;
    }
    public Cursor readData()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select distinct Selected_Symptoms from Symptoms;",null);
        return cursor;
    }

    public void clearData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Table_Name,null,null);
    }

    public long deleteData(String selectedSymptom) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long row_id=sqLiteDatabase.delete(Table_Name,symptom+"=?", new String[]{selectedSymptom});
        return  row_id;
    }
}
