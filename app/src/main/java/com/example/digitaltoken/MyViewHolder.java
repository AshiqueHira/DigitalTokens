package com.example.digitaltoken;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MyViewHolder extends RecyclerView.ViewHolder {

    public static TextView mtextViewHead;
    public static TextView mtextViewDisc;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        this.mtextViewHead = (TextView) itemView.findViewById(R.id.title);
        this.mtextViewDisc = (TextView) itemView.findViewById(R.id.discription);
    }
}

