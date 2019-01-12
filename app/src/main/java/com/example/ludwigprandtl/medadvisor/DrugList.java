package com.example.ludwigprandtl.medadvisor;

import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DrugList extends AppCompatActivity {

    Button AZlist,myDrug,CommercialName,GenericName;
    RecyclerView recyclerView;
    DrugRecyclerView cAdapter;
    ArrayList<String> DruglistComm = new ArrayList<>();
    ArrayList<String> DruglistGen = new ArrayList<>();
    ArrayList<String> MyDruglist = new ArrayList<>();
    ArrayList<String> temp;
    DatabaseReference rootRef;
    DatabaseReference databaseReference;
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

        AZlist.setBackgroundColor(getResources().getColor(com.example.ludwigprandtl.medadvisor.R.color.colorPrimary));
        CommercialName.setBackgroundColor(getResources().getColor(com.example.ludwigprandtl.medadvisor.R.color.colorPrimary));
        az=true;
        cn=true;
        rootRef = FirebaseDatabase.getInstance().getReference();
        databaseReference = rootRef.child("Drugs").child("CommercialName");
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapShot : dataSnapshot.getChildren()){
                    DruglistComm.add(childSnapShot.child("Name").getValue(String.class));
                }
                cAdapter = new DrugRecyclerView(DruglistComm);
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


    }

}
