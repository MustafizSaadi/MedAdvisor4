package com.example.ludwigprandtl.medadvisor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

public class DrugInfoGen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView drugGen,infoGen;
    Button reminderGen,indicationGen,contraIndicationGen,sideEffectsGen,precautionGen;
    DatabaseReference rootRef;
    DatabaseReference databaseReference;
    boolean flag,star;
    String data,medicine ;
    MyDatabase myDatabase;
    SQLiteDatabase sqLiteDatabase;
    MenuItem starButton;
    DrawerLayout drawerLayout ;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

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

        rootRef = FirebaseDatabase.getInstance().getReference();
        databaseReference = rootRef.child("Drugs").child("GenericName");

        drawerLayout = findViewById(R.id.layout_drug_info_gen);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        navigationView = findViewById(R.id.drug_info_gen_navigation);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        myDatabase = new MyDatabase(this);
        sqLiteDatabase = myDatabase.getWritableDatabase();
        data = "";
        star = false;

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
        indicationGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrugInfoGen.this,DrugDetails.class);
                intent.putExtra("Title",medicine);
                intent.putExtra("Headline",indicationGen.getText());
                intent.putExtra("Activity","Generic");
                startActivity(intent);
            }
        });
        contraIndicationGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrugInfoGen.this,DrugDetails.class);
                intent.putExtra("Title",medicine);
                intent.putExtra("Headline",contraIndicationGen.getText());
                intent.putExtra("Activity","Generic");
                startActivity(intent);
            }
        });
        precautionGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrugInfoGen.this,DrugDetails.class);
                intent.putExtra("Title",medicine);
                intent.putExtra("Headline",precautionGen.getText());
                intent.putExtra("Activity","Generic");
                startActivity(intent);
            }
        });
        sideEffectsGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrugInfoGen.this,DrugDetails.class);
                intent.putExtra("Title",medicine);
                intent.putExtra("Headline",sideEffectsGen.getText());
                intent.putExtra("Activity","Generic");
                startActivity(intent);
            }
        });
    }

    private void fetchData() {
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
        if(item.getItemId()==R.id.starButton){
            if(star){
                starButton = item;
                Drawable drawable = starButton.getIcon();

                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(new
                            PorterDuffColorFilter(Color.parseColor("white"), PorterDuff.Mode.MULTIPLY));
                }
                starButton.setIcon(drawable);

                myDatabase.deleteData(medicine,"Gen");
                star = false;
            }
            else{
                starButton = item;
                Drawable drawable = starButton.getIcon();

                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(new
                            PorterDuffColorFilter(Color.parseColor("yellow"), PorterDuff.Mode.MULTIPLY));
                }
                starButton.setIcon(drawable);

                long rowid = myDatabase.insertData(medicine,"Gen");
                star = true;
            }
        }
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_medicine,menu);

        String columns[] = {"MyGen"};
        Cursor cursor = sqLiteDatabase.query("Gen",columns,"MyGen=?",new String[]{medicine},null,null,null);
        if(cursor.getCount()==0) star = false;
        else {
            star=true;
            starButton = menu.getItem(0);
            Drawable drawable = starButton.getIcon();

            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(new
                        PorterDuffColorFilter(Color.parseColor("yellow"), PorterDuff.Mode.MULTIPLY));
            }
            starButton.setIcon(drawable);
        }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.Hm){
            Intent intent = new Intent(this,Home.class);
            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        return false;
    }
}
