package com.example.ludwigprandtl.medadvisor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchSymptoms extends AppCompatActivity {
    ListView listview;
    SearchView searchView;
    DataContainer dataContainer;
    ArrayList<DataContainer> DataContainerArray = new ArrayList<>();
    List<String> SymptomsList = new ArrayList<>();
    DatabaseReference databaseReference;
    DatabaseReference rootRef;
    ArrayList<String> fever_clarity = new ArrayList<>();
    boolean[] checked;
    String arr[];
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_symptoms);
        listview = findViewById(R.id.SymptomsId);
        searchView = findViewById(R.id.search_symptom);
    }
    public void onStart() {

        super.onStart();
        SymptomsList.clear();
        Toast.makeText(SearchSymptoms.this,""+SymptomsList.size(),Toast.LENGTH_SHORT).show();
        adapter = new ArrayAdapter<String>(this, R.layout.disease_list, R.id.textViewId, SymptomsList);
        listview.setAdapter(adapter);

        rootRef = FirebaseDatabase.getInstance().getReference();
        databaseReference = rootRef.child("symptoms");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot chidSnapShot : dataSnapshot.getChildren()){
                    dataContainer = chidSnapShot.getValue(DataContainer.class);
                    DataContainerArray.add(dataContainer);
                    SymptomsList.add(dataContainer.symptom);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Start_Searching();
    }


    public void Start_Searching(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            AlertDialog.Builder fever_builder = new AlertDialog.Builder(SearchSymptoms.this);
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String[] value = {(String) adapter.getItem(position)};
                final String extra[] = new String[5];
                Toast.makeText(SearchSymptoms.this, value[0],Toast.LENGTH_SHORT).show();
                databaseReference = rootRef.child("sym_clarity");
                final String finalS_id = value[0];
                fever_clarity.clear();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String question = new String();
                        boolean activity_flag = false;
                        for(DataSnapshot childSnapShot : dataSnapshot.getChildren()){
                            final String value1 = (String) childSnapShot.child("Symptom").getValue(String.class);
                            if(value1.equals(finalS_id)){
                                activity_flag=true;
                                for(DataSnapshot childSnapShot2 : childSnapShot.child("Clarity").getChildren()){
                                    String value2 = childSnapShot2.getValue(String.class);
                                    fever_clarity.add(value2);

                                }
                                question = (String) childSnapShot.child("question").getValue(String.class);
                                Toast.makeText(SearchSymptoms.this, "matched",Toast.LENGTH_SHORT).show();
                                fever_builder.setTitle(question);
                                arr = new String[fever_clarity.size()];
                                arr = fever_clarity.toArray(arr);
                                fever_builder.setSingleChoiceItems(arr, -1 , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        extra[0] = arr[which];
                                        Toast.makeText(SearchSymptoms.this, value[0]+extra[0],Toast.LENGTH_SHORT).show();
                                    }
                                });
                                fever_builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        value[0] +="(" + extra[0] + ")";
                                        Toast.makeText(SearchSymptoms.this, value[0],Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent intent = new Intent(SearchSymptoms.this,MainActivity.class);
                                        intent.putExtra("Database",value[0]);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                AlertDialog fever_dialog = fever_builder.create();
                                fever_dialog.show();
                                break;
                            }
                        }
                        if(!activity_flag) {
                            Intent intent = new Intent(SearchSymptoms.this, MainActivity.class);
                            intent.putExtra("Database", value[0]);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });
    }
}
