package com.example.ludwigprandtl.medadvisor;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiseaseDetails extends AppCompatActivity {
    TextView DiseaseName;
    TextView Reasons;
    TextView Symptoms;
    TextView MedicinalTreatment;
    TextView NonMedicinalTreatment;
    TextView Preventions;
    DatabaseReference databaseReference;
    DatabaseReference rootRef;
    ArrayList<String> reason,symptom,MedTreat,NonMedTreat,Prevention;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_details);
        DiseaseName = findViewById(R.id.DiseaseName);
        Reasons = findViewById(R.id.reasons);
        Symptoms = findViewById(R.id.symptoms);
        MedicinalTreatment = findViewById(R.id.medicinal_treatment);
        NonMedicinalTreatment = findViewById(R.id.non_medicinal_treatment);
        Preventions = findViewById(R.id.preventions);
        rootRef = FirebaseDatabase.getInstance().getReference();
        databaseReference = rootRef.child("disease");
    }
    protected void onStart() {

        super.onStart();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            final String disease = bundle.getString("Data");
            DiseaseName.setText(disease);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot childSnapShot : dataSnapshot.getChildren()){
                        String val = childSnapShot.child("name").getValue(String.class);
                        if(val.equals(disease)){
                            reason = new ArrayList<>();
                            symptom = dataFromDataBase("symptoms",childSnapShot);
                            MedTreat = dataFromDataBase("medicines",childSnapShot);
                            NonMedTreat = dataFromDataBase("Non_Med",childSnapShot);
                            Prevention = dataFromDataBase("Prevention",childSnapShot);
                            reason.add(childSnapShot.child("short_descript").getValue(String.class));
                            if(reason.get(0).equals("None")) {
                                reason.clear();
                                reason.add("No Data Found");
                            }
                            showData();
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

                ArrayList<String> dataFromDataBase(String val,DataSnapshot childSnapShot){
                    ArrayList<String> temp = new ArrayList<>();
                    String val1;
                    temp.clear();
                    DataSnapshot childSnapShot2 = childSnapShot.child(val);

                    for(DataSnapshot childSnapShot3 : childSnapShot2.getChildren()){
                        val1 = childSnapShot3.getValue(String.class);
                        if(val1.equals("None")){
                            temp.add("No data Found");
                            break;
                        }
                        else
                            temp.add(val1);
                    }

                    return  temp;
                }
            });
        }
    }
    private void showData() {
        String val = addComma(reason);
        Reasons.setText(val);
        val = addComma(symptom);
        Symptoms.setText(val);
        val = addComma(MedTreat);
        MedicinalTreatment.setText(val);
        val = addComma(NonMedTreat);
        NonMedicinalTreatment.setText(val);
        val = addComma(Prevention);
        Preventions.setText(val);
    }

    private String addComma(ArrayList<String> reason) {
        String ans="";
        for(int i=0;i<reason.size();i++){
            ans+=reason.get(i);
            if(i!=reason.size()-1) ans+=",";
        }
        return ans;
    }
}
