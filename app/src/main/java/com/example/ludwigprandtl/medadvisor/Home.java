package com.example.ludwigprandtl.medadvisor;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Home extends AppCompatActivity {

    ImageButton SymptomChecker,MedicationReminder,DrugMedication;
    MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SymptomChecker = findViewById(R.id.symptom_checker);
        MedicationReminder = findViewById(R.id.medication_reminder);
        DrugMedication = findViewById(R.id.drugs_medication);
        myDatabase = new MyDatabase(this);
        SQLiteDatabase sqLiteDatabase = myDatabase.getWritableDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SymptomChecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabase.clearData();
                Intent intent = new Intent(Home.this,MainActivity.class);
                startActivity(intent);
            }
        });

        MedicationReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        DrugMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,DrugInfoGen.class);
                intent.putExtra("Data","Paracetamol");
                startActivity(intent);
            }
        });


    }


}
