package com.example.ludwigprandtl.medadvisor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

public class ProbableDisease extends AppCompatActivity {
    MyDatabase myDatabase;
    Set<String> list = new HashSet<String>();
    ArrayList<String> SymptomsInDatabase = new ArrayList<>();
    PriorityQueue<check> pq = new PriorityQueue<>();
    DatabaseReference rootRef;
    DatabaseReference databaseReference;
    String str[] = new String[100];
    ListView listView;
    ArrayAdapter adapter;
    check temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_probable_disease);
        myDatabase = new MyDatabase(this);
        SQLiteDatabase sqLiteDatabase = myDatabase.getWritableDatabase();
    }
    public void onStart(){
        super.onStart();
        Cursor cursor = myDatabase.readData();
        while(cursor.moveToNext()){
            list.add(cursor.getString(1)) ;
        }
           rootRef = FirebaseDatabase.getInstance().getReference();
          databaseReference = rootRef.child("disease");

        //  fetchFromFirebase();
        Toast.makeText(ProbableDisease.this, "" + list.size(), Toast.LENGTH_SHORT).show();
    }

    private void fetchFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()){
                    DataSnapshot childSnapShot2 = childSnapShot.child("symptoms");
                    for (DataSnapshot childSnapShot3 : childSnapShot2.getChildren()){
                        SymptomsInDatabase.add(childSnapShot3.getValue(String.class));
                    }
                    Iterator<String> it = list.iterator();
                    int cnt=0;
                    while(it.hasNext()){
                        String value = it.next();
                        for(int i=0;i<SymptomsInDatabase.size();i++){
                            if(value.equals(SymptomsInDatabase.get(i))){
                                cnt++;
                                break;
                            }
                        }
                    }
                    pq.add(new check(childSnapShot.child("name").getValue(String.class),cnt));
                    SymptomsInDatabase.clear();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        showInActivity();
    }
    private void showInActivity() {
       int i=0;
       while(!pq.isEmpty()){
           temp = pq.poll();
           str[i] = new String();
           str[i++]=temp.getDisease();
           if(i==3) break;
       }
        listView = findViewById(R.id.ProbableDieases);
        adapter = new ArrayAdapter(this,R.layout.disease_list,R.id.textViewId,str);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
