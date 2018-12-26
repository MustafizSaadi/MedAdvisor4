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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button,symptoms_button,discardButton;
    TextView text1 ;
    TextView text2;
    TextView send;
    ListView listView;
    MyDatabase myDatabase;
    int removingPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabase = new MyDatabase(this);
        SQLiteDatabase sqLiteDatabase = myDatabase.getWritableDatabase();
    }
    protected void onStart() {

        super.onStart();
        button = findViewById(R.id.add_symptom);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchSymptoms.class);
                //  intent.putExtra("Database", myDatabase);
                startActivity(intent);
                finish();
            }
        });
        //text1.setText("No Symptoms Added");
        listView = findViewById(R.id.myTextView) ;
        // listView.setEmptyView(findViewById(R.id.empty));


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String Symptom = bundle.getString("Database");
            long rowId = myDatabase.insertData(Symptom);
            if(rowId>0)
                Toast.makeText(MainActivity.this,"New row inserted",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this,"Unsuccessful",Toast.LENGTH_SHORT).show();

            send = findViewById(R.id.send);

            showSelectedSymptoms();
            goToDisease();
            // deleteSelectedSymptoms();

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void goToDisease() {
        send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (send.getRight() - send.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Intent intent = new Intent(MainActivity.this,ProbableDisease.class);
                        startActivity(intent);

                        return true;
                    }
                }
                return false;

            }
        });
    }

    private void showSelectedSymptoms() {
        Cursor cursor = myDatabase.readData();
        if(cursor.getCount()==0)
        {
            //  text1.setText("No Symptoms Added");
            ArrayList<String> listData = new ArrayList<>();
            CustomAdapter custom = new CustomAdapter(this,listData,listData.size());
            listView.setAdapter(custom);
            custom.notifyDataSetChanged();
            return ;
        }
        else
        {


            text1.setText("");
            ArrayList<String> listData = new ArrayList<>();
            while(cursor.moveToNext()){
                listData.add(cursor.getString(1));
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
        public Object getItem(int position) {
            return null;
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
                    String selectedSymptom = (String) textView.getText();
                    // removingPosition=position;
                    long rs= myDatabase.deleteData(selectedSymptom);

                    if(rs>0)
                        Toast.makeText(context,"Successfully deleted"+selectedSymptom,Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context,"Unsuccessful",Toast.LENGTH_SHORT).show();
                    showSelectedSymptoms();
                }
            });

            return convertView;
        }
    }
}
