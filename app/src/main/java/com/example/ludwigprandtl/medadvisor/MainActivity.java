package com.example.ludwigprandtl.medadvisor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button,symptoms_button,discardButton;
    TextView text2;
    ImageButton send;
    ListView listView;
    MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabase = new MyDatabase(this);
       // myDatabase.clearData();
        SQLiteDatabase sqLiteDatabase = myDatabase.getWritableDatabase();
        Toast.makeText(this, "I am in onCreate", Toast.LENGTH_SHORT).show();
    }
    protected void onStart() {

        Toast.makeText(this, "I am in onStart", Toast.LENGTH_SHORT).show();
        super.onStart();
        button = findViewById(R.id.add_symptom);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchSymptoms.class);
                startActivity(intent);
              //  finish();
            }
        });
        listView = findViewById(R.id.myTextView) ;
        listView.setEmptyView(findViewById(R.id.empty));


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String Symptom = bundle.getString("Database");
                long rowId = myDatabase.insertData(Symptom);
          /*  if(rowId>0)
                Toast.makeText(MainActivity.this,"New row inserted",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this,"Unsuccessful",Toast.LENGTH_SHORT).show();*/

                send = findViewById(R.id.send);

                goToDisease();
                showSelectedSymptoms();
            }
            // deleteSelectedSymptoms();

        }

    @SuppressLint("ClickableViewAccessibility")
    private void goToDisease() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = myDatabase.readData();
                if(cursor.getCount()!=0) {
                    Intent intent = new Intent(MainActivity.this, ProbableDisease.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void showSelectedSymptoms() {
        Cursor cursor = myDatabase.readData();
        if(cursor.getCount()==0)
        {
            ArrayList<String> listData = new ArrayList<>();
            button.setText("Add Symptom");
            CustomAdapter custom = new CustomAdapter(this,listData,listData.size());
            listView.setAdapter(custom);
            custom.notifyDataSetChanged();
            return ;
        }
        else
        {

            ArrayList<String> listData = new ArrayList<>();
            button.setText("Add More Symptoms");
            while(cursor.moveToNext()){
                listData.add(cursor.getString(0));
                //Toast.makeText(EnterSymptoms.this,cursor.getString(1),Toast.LENGTH_LONG).show();
            }
            Toast.makeText(MainActivity.this,""+listData.size(),Toast.LENGTH_SHORT).show();
            CustomAdapter custom = new CustomAdapter(this,listData,listData.size());
            listView.setAdapter(custom);
            custom.notifyDataSetChanged();
            listData.clear();

        }
    }
    public class CustomAdapter extends BaseAdapter {
        ArrayList<String> symptoms_list = new ArrayList<>();
        LayoutInflater inflater;
        Context context;
        View v;
        TextView textView;
        int len;
        CustomAdapter(Context context, ArrayList<String> symptoms_list,int len){
            for( int i=0;i<len;i++){
                this.symptoms_list.add(symptoms_list.get(i));
            }
            this.context=context;
            this.len=len;
        }
        @Override
        public int getCount() {
            return len;
        }

        @Override
        public String getItem(int position) {
            return symptoms_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(convertView==null){
                inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v=inflater.inflate(R.layout.temp_text,parent,false);
                convertView=v;
            }
            textView=convertView.findViewById(R.id.textViewId);
            try {
                textView.setText(symptoms_list.get(position));
            }
            catch (Exception e){
                Log.d("tag", ""+position);
            }
            discardButton = convertView.findViewById(R.id.discardButton);

            discardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedSymptom = getItem(position);
                    Toast.makeText(context,""+selectedSymptom,Toast.LENGTH_SHORT).show();
                    long rs= myDatabase.deleteData(selectedSymptom);

                   /* if(rs>0)
                        Toast.makeText(context,"Successfully deleted"+selectedSymptom,Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context,"Unsuccessful",Toast.LENGTH_SHORT).show();*/
                    showSelectedSymptoms();
                }
            });

            return convertView;
        }
    }
}
