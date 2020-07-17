package com.example.digitaltoken;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {


    private ArrayList<MyModel> models = new ArrayList<>();
    private CardClickListner myCardClickListener; // this will create list of array whick parameters define in our model class


    public RecyclerAdapter(ArrayList<MyModel> models, CardClickListner cardClickListner) {
        this.models = models;
        this.myCardClickListener = cardClickListner;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent,false);
        // this above line infalte the card_layout file
        return new RecyclerViewHolder(view, myCardClickListener);
        // this will return the view to holder class
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        holder.headingTV.setText(models.get(position).getMyTitle());
        holder.discTV.setText(models.get(position).getMyDisc());
        holder.imageV.setImageResource(models.get(position).getMyImg());


    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
