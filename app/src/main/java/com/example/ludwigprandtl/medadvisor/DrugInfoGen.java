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

public class DrugInfoGen extends AppCompatActivity {

    TextView drugGen,infoGen;
    Button reminderGen,indicationGen,contraIndicationGen,sideEffectsGen,precautionGen,dosageInfoGen;
    DatabaseReference rootRef;
    DatabaseReference databaseReference;
    boolean flag;
    String data ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_info_gen);

        drugGen = findViewById(R.id.drug_gen);
        infoGen  = findViewById(R.id.info_gen);
        reminderGen = findViewById(R.id.addReminderGen);
        indicationGen = findViewById(R.id.indicationsGen);
        contraIndicationGen = findViewById(R.id.contra_indicationsGen);
        sideEffectsGen = findViewById(R.id.side_effectsGen);
        precautionGen = findViewById(R.id.precautionsGen);
        dosageInfoGen = findViewById(R.id.dosage_infoGen);

        rootRef = FirebaseDatabase.getInstance().getReference();
        databaseReference = rootRef.child("Drugs").child("GenericName");
        data = "";
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String medicine = bundle.getString("Data");
            fetchData(medicine);
        }
    }

    private void fetchData(final String medicine) {
        drugGen.setText(medicine);
        flag = false;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapShot : dataSnapshot.getChildren()){
                    DataSnapshot childSnapShot2 = childSnapShot.child("Name");
                    String value = childSnapShot2.getValue(String.class);
                    if(value.equals(medicine)){
                        flag = true;
                        data+="SubClass: "+ childSnapShot.child("SubClass").getValue(String.class)+ "\n";
                        data+="Class: "+ childSnapShot.child("Class").getValue(String.class)+ "\n";
                        data+="Types: ";
                        boolean f1=false;
                        for(DataSnapshot childSnapshot3 : childSnapShot.child("Types").getChildren()){
                            if(!f1){
                                data+=childSnapshot3.getValue(String.class);
                                f1=true;
                            }
                            else{
                                data+=","+childSnapshot3.getValue(String.class);
                            }

                        }
                        data+="\n";
                    }
                    if(flag) break;
                }
                infoGen.setText(data);
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
