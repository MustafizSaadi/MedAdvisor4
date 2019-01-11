package com.example.ludwigprandtl.medadvisor;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DrugInfoComm extends AppCompatActivity {
    TextView drug,info;
    Button reminder,indication,contraIndication,sideEffects,precaution,dosageInfo,marketPrice;
    DatabaseReference rootRef;
    DatabaseReference databaseReference;
    boolean flag;
    String data,medicine ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_info_comm);
        drug = findViewById(R.id.drug);
        info = findViewById(R.id.info);
        reminder = findViewById(R.id.addReminder);
        indication = findViewById(R.id.indications);
        contraIndication = findViewById(R.id.contra_indications);
        sideEffects = findViewById(R.id.side_effects);
        precaution = findViewById(R.id.precautions);
        dosageInfo = findViewById(R.id.dosage_info);
        marketPrice = findViewById(R.id.market_price);
        rootRef = FirebaseDatabase.getInstance().getReference();
        databaseReference = rootRef.child("Drugs").child("CommercialName");
        data = "";

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            medicine = bundle.getString("Data");
            fetchData();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        indication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrugInfoComm.this,DrugDetails.class);
                intent.putExtra("Title",medicine);
                intent.putExtra("Headline",indication.getText());
                intent.putExtra("Activity","Commercial");
                startActivity(intent);
            }
        });
        contraIndication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrugInfoComm.this,DrugDetails.class);
                intent.putExtra("Title",medicine);
                intent.putExtra("Headline",contraIndication.getText());
                intent.putExtra("Activity","Commercial");
                startActivity(intent);
            }
        });
        sideEffects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrugInfoComm.this,DrugDetails.class);
                intent.putExtra("Title",medicine);
                intent.putExtra("Headline",sideEffects.getText());
                intent.putExtra("Activity","Commercial");
                startActivity(intent);
            }
        });
        precaution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrugInfoComm.this,DrugDetails.class);
                intent.putExtra("Title",medicine);
                intent.putExtra("Headline",precaution.getText());
                intent.putExtra("Activity","Commercial");
                startActivity(intent);
            }
        });
        dosageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrugInfoComm.this,DrugDetails.class);
                intent.putExtra("Title",medicine);
                intent.putExtra("Headline",dosageInfo.getText());
                intent.putExtra("Activity","Commercial");
                startActivity(intent);
            }
        });
        marketPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrugInfoComm.this,DrugDetails.class);
                intent.putExtra("Title",medicine);
                intent.putExtra("Headline",marketPrice.getText());
                intent.putExtra("Activity","Commercial");
                startActivity(intent);
            }
        });
    }

    private void fetchData() {
        drug.setText(medicine);
        flag = false;
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapShot : dataSnapshot.getChildren()){
                    DataSnapshot childSnapShot2 = childSnapShot.child("Name");
                    String value = childSnapShot2.getValue(String.class);
                    if(value.equals(medicine)){
                        flag = true;
                        data+="Generic: "+ childSnapShot.child("Generic").getValue(String.class)+ "\n";
                        data+="Type: "+ childSnapShot.child("Type").getValue(String.class)+ "\n";
                        data+="Company Name: "+childSnapShot.child("Company").getValue(String.class)+ "\n";
                        data+="Ingredients: "+childSnapShot.child("Ingredients").getValue(String.class)+ "\n";
                    }
                    if(flag) break;
                }
                info.setText(data);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_medicine,menu);
        return super.onCreateOptionsMenu(menu);
    }


}
