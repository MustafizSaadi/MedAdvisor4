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

public class DrugDetails extends AppCompatActivity {

    TextView Headline,details;
    String title,headline,activity;

    DatabaseReference rootRef,databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_details);

        Headline = findViewById(R.id.drug_detail_name);
        details = findViewById(R.id.drug_details);

        rootRef = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            title = bundle.getString("Title");
            setTitle(title);
            headline = bundle.getString("Headline");
            Headline.setText(headline);
            activity = bundle.getString("Activity");
            fetchData(title,headline,activity);
        }
    }

    private void fetchData(final String title, final String headline, String activity) {
        if(activity.equals("Commercial")){
            databaseReference = rootRef.child("Drugs").child("CommercialName");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean flag = false;
                    for(DataSnapshot childSnapShot: dataSnapshot.getChildren()){
                        if(childSnapShot.child("Name").getValue(String.class).equals(title)){
                            flag = true;
                            if(headline.equals("Dosage Info")){
                                details.setText(childSnapShot.child("Dosage Info").getValue(String.class));
                            }
                            else if(headline.equals("Market Price")){
                                details.setText(childSnapShot.child("Market Price").getValue(String.class));
                            }
                            else{
                                String generic = childSnapShot.child("Generic").getValue(String.class);
                                fetchDataFromGeneric(generic,headline);

                            }
                        }
                        if(flag) break;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
            fetchDataFromGeneric(title,headline);

    }

    private void fetchDataFromGeneric(final String generic, final String headline) {
        databaseReference = rootRef.child("Drugs").child("GenericName");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean flag = false;
                for(DataSnapshot childSnapShot : dataSnapshot.getChildren()){
                    if(childSnapShot.child("Name").getValue(String.class).equals(generic)){
                        flag =true;
                        details.setText(childSnapShot.child(headline).getValue(String.class));
                    }
                    if(flag) break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
