package com.example.ludwigprandtl.medadvisor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

public class DrugRecyclerView extends RecyclerView.Adapter <DrugRecyclerView.DrugsViewHolder> implements Filterable {

    ArrayList<String> list;
    ArrayList<String> listFull;
    String activity;
    Context context;

    void setList(ArrayList<String> list,String activity,Context context){
        this.list = list;
        listFull = new ArrayList<>(list);
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public DrugsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.disease_list,viewGroup,false);

        return new DrugsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrugsViewHolder drugsViewHolder, final int i) {
        String data = list.get(i);
        drugsViewHolder.textView.setText(data);
        drugsViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(context,"item clicked",Toast.LENGTH_SHORT).show();
                if(activity.equals("CommercialName")){
                    Intent intent = new Intent(context,DrugInfoComm.class);
                    intent.putExtra("Data",list.get(i));
                    context.startActivity(intent);
                }
                else if(activity.equals("GenericName")){
                    Intent intent = new Intent(context,DrugInfoGen.class);
                    intent.putExtra("Data",list.get(i));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<String> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length()==0){
                filteredList.addAll(listFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(int i=0;i<listFull.size();i++){
                    String temp = listFull.get(i);
                    if(temp.toLowerCase().trim().equals(filterPattern)){
                        filteredList.add(temp);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                list.clear();
                list.addAll((Collection<? extends String>) results.values);
                notifyDataSetChanged();
        }
    };

    public class DrugsViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        LinearLayout linearLayout;
        public DrugsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewId);
            linearLayout = itemView.findViewById(R.id.diseases_list_id);
        }
    }
}
