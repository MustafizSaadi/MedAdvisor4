package com.example.ludwigprandtl.medadvisor;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DrugRecyclerView extends RecyclerView.Adapter <DrugRecyclerView.DrugsViewHolder>{

    ArrayList<String> list;

    DrugRecyclerView(ArrayList<String> list){
        this.list = list;
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

    public class DrugsViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public DrugsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewId);
        }
    }
}
