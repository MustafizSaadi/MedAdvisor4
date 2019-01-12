package com.example.ludwigprandtl.medadvisor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.ludwigprandtl.medadvisor.R.color.colorPrimary;

public class DrugList extends AppCompatActivity {

    Button AZlist,myDrug,CommercialName,GenericName;
    RecyclerView recyclerView;
    DrugRecyclerView cAdapter;
    ArrayList<String> DruglistComm = new ArrayList<>();
    ArrayList<String> DruglistGen = new ArrayList<>();
    ArrayList<String> MyDruglistComm = new ArrayList<>();
    ArrayList<String> MyDruglistGen = new ArrayList<>();
    ArrayList<String> temp;
    DatabaseReference rootRef;
    DatabaseReference databaseReference;
    MyDatabase myDatabase;
    boolean az,md,cn,gn;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_search,menu);

        final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                cAdapter.getFilter().filter(s);
                return  false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_list);
        AZlist = findViewById(R.id.A_Z_List);
        myDrug = findViewById(R.id.myDrugs);
        CommercialName = findViewById(R.id.Commercial_Name);
        GenericName = findViewById(R.id.Generic_Name);
        recyclerView = findViewById(R.id.drugs_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cAdapter = new DrugRecyclerView();

        AZlist.setBackgroundColor(getResources().getColor(colorPrimary));
        CommercialName.setBackgroundColor(getResources().getColor(colorPrimary));
        az=true;
        cn=true;
        md=false;
        gn=false;
        rootRef = FirebaseDatabase.getInstance().getReference();
        databaseReference = rootRef.child("Drugs").child("CommercialName");
        myDatabase = new MyDatabase(this);
        SQLiteDatabase sqLiteDatabase = myDatabase.getWritableDatabase();

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapShot : dataSnapshot.getChildren()){
                    DruglistComm.add(childSnapShot.child("Name").getValue(String.class));
                }
                temp = DruglistComm;
                cAdapter.setList(temp,getString(R.string.comm),DrugList.this);
                recyclerView.setAdapter(cAdapter);
               // Toast.makeText(DrugList.this,"Adapter set",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference = rootRef.child("Drugs").child("GenericName");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapShot : dataSnapshot.getChildren()){
                    DruglistGen.add(childSnapShot.child("Name").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(md){
            if(cn){
                Cursor cursor = myDatabase.readData("Drugs");
                MyDruglistComm.clear();
                while (cursor.moveToNext()) {
                    MyDruglistComm.add(cursor.getString(0));
                }
                temp = MyDruglistComm;
                cAdapter.setList(temp,getString(R.string.gen),DrugList.this);
                recyclerView.setAdapter(cAdapter);
            }
            else if(gn){
                Cursor cursor = myDatabase.readData("Gen");
                MyDruglistGen.clear();
                while (cursor.moveToNext()) {
                    MyDruglistGen.add(cursor.getString(0));
                }
                temp = MyDruglistGen;
                cAdapter.setList(temp,getString(R.string.gen),DrugList.this);
                recyclerView.setAdapter(cAdapter);
            }
        }
        GenericName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cn)
                    CommercialName.setBackgroundColor(getResources().getColor(com.example.ludwigprandtl.medadvisor.R.color.White));
                if(!gn) {
                    GenericName.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    if (az) {
                        temp = DruglistGen;
                        cAdapter.setList(temp,getString(R.string.gen),DrugList.this);
                        recyclerView.setAdapter(cAdapter);
                    } else if (md) {
                        Cursor cursor = myDatabase.readData("Gen");
                        MyDruglistGen.clear();
                            while (cursor.moveToNext()) {
                                MyDruglistGen.add(cursor.getString(0));
                            }
                            temp = MyDruglistGen;
                            cAdapter.setList(temp,getString(R.string.gen),DrugList.this);
                            recyclerView.setAdapter(cAdapter);

                    }
                    gn = true;
                    cn = false;
                }

            }
        });

        CommercialName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gn)
                    GenericName.setBackgroundColor(getResources().getColor(com.example.ludwigprandtl.medadvisor.R.color.White));
                if(!cn) {
                    CommercialName.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    if (az) {
                        temp = DruglistComm;
                        cAdapter.setList(temp,getString(R.string.comm),DrugList.this);
                        recyclerView.setAdapter(cAdapter);
                    } else if (md) {
                        Cursor cursor = myDatabase.readData("Drugs");
                        MyDruglistComm.clear();
                            while (cursor.moveToNext()) {
                                MyDruglistComm.add(cursor.getString(0));
                            }
                            temp = MyDruglistComm;
                            cAdapter.setList(temp,getString(R.string.gen),DrugList.this);
                            recyclerView.setAdapter(cAdapter);

                    }
                    cn = true;
                    gn = false;
                }

            }
        });

        myDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(az){
                    AZlist.setBackgroundColor(getResources().getColor(R.color.White));
                    myDrug.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    if(cn){
                        Cursor cursor = myDatabase.readData("Drugs");
                        MyDruglistComm.clear();
                            while (cursor.moveToNext()) {
                                MyDruglistComm.add(cursor.getString(0));
                            }
                            temp = MyDruglistComm;
                            cAdapter.setList(temp,getString(R.string.comm),DrugList.this);
                            recyclerView.setAdapter(cAdapter);
                    }
                    else if(gn){
                        Cursor cursor = myDatabase.readData("Gen");
                        MyDruglistGen.clear();
                            while (cursor.moveToNext()) {
                                MyDruglistGen.add(cursor.getString(0));
                            }
                            temp = MyDruglistGen;
                            cAdapter.setList(temp,getString(R.string.gen),DrugList.this);
                            recyclerView.setAdapter(cAdapter);

                    }
                    az=false;
                    md=true;
                }
            }
        });

        AZlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(md){
                    myDrug.setBackgroundColor(getResources().getColor(R.color.White));
                    AZlist.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    if(cn){
                        temp = DruglistComm;
                        cAdapter.setList(temp,getString(R.string.comm),DrugList.this);
                        recyclerView.setAdapter(cAdapter);
                    }
                    else if(gn){
                        temp = DruglistGen;
                        cAdapter.setList(temp,getString(R.string.gen),DrugList.this);
                        recyclerView.setAdapter(cAdapter);

                    }
                    az=true;
                    md=false;
                }
            }
        });



    }


}
