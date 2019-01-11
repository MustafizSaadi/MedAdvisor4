package com.example.ludwigprandtl.medadvisor;

import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.SearchView;

import java.util.ArrayList;

public class DrugList extends AppCompatActivity {

    Button AZlist,myDrug,CommercialName,GenericName;
    ArrayList<String> Druglist = new ArrayList<>();
    ArrayList<String> MyDruglist = new ArrayList<>();
    ArrayList<String> temp;

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
                return false;
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
        AZlist.setBackgroundColor(getResources().getColor(com.example.ludwigprandtl.medadvisor.R.color.colorPrimary));
        CommercialName.setBackgroundColor(getResources().getColor(com.example.ludwigprandtl.medadvisor.R.color.colorPrimary));
    }
}
