package com.example.ludwigprandtl.medadvisor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

public class ProbableDisease extends AppCompatActivity {
    MyDatabase myDatabase;
    Set<String> list = new HashSet<String>();
    ArrayList<String> SymptomsInDatabase = new ArrayList<>();
    ArrayList<check> pq = new ArrayList<>();
    DatabaseReference rootRef;
    DatabaseReference databaseReference;
    ArrayList<String> str = new ArrayList<>();
    ListView listView;
    ArrayAdapter<String> adapter;
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
          fetchFromFirebase();
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
                showInActivity();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void showInActivity() {
        str.clear();
        Collections.sort(pq,new ValueComparator());
       for(int i=0;i<3&&i<pq.size();i++){
           temp = pq.get(i);
           str.add(temp.getDisease());
           Toast.makeText(ProbableDisease.this, "" + temp.getDisease(), Toast.LENGTH_SHORT).show();
       }
        listView = findViewById(R.id.ProbableDieases);
        adapter = new ArrayAdapter<String>(this,R.layout.disease_list,R.id.textViewId,str);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = adapter.getItem(position);
                Intent intent = new Intent(ProbableDisease.this,DiseaseDetails.class);
                intent.putExtra("Data",value);
                startActivity(intent);
            }
        });
    }
}
