package com.example.digitaltoken;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context c;
    private ArrayList<Model> models; // this will create list of array whick parameters define in our model class


    public MyAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent,false);
        // this above line infalte the card_layout file
        return new MyViewHolder(view);
        // this will return the view to holder class
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.mtextViewHead.setText(models.get(position).getTitleList());
        holder.mtextViewDisc.setText(models.get(position).getDiscList());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {

                String gTiltle = models.get(position).getTitleList();
                String gDisc = models.get(position).getDiscList();

                Intent intent = new Intent(c, TokenStatus.class);
                intent.putExtra("iTitle", gTiltle);
                intent.putExtra("iDisc", gDisc);
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
