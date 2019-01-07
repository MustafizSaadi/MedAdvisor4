package com.example.ludwigprandtl.medadvisor;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    String data ;

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
            String medicine = bundle.getString("Data");
            fetchData(medicine);
        }

    }

    private void fetchData(final String medicine) {
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
