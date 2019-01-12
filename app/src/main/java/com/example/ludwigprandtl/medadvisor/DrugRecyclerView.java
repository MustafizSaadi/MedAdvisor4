package com.example.ludwigprandtl.medadvisor;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

public class DrugRecyclerView extends RecyclerView.Adapter <DrugRecyclerView.DrugsViewHolder> implements Filterable {

    ArrayList<String> list;
    ArrayList<String> listFull;

    DrugRecyclerView(ArrayList<String> list){
        this.list = list;
        listFull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public DrugsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.disease_list,viewGroup,false);

        return new DrugsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrugsViewHolder drugsViewHolder, int i) {
        String data = list.get(i);
        drugsViewHolder.textView.setText(data);
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
        public DrugsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewId);
        }
    }
}
