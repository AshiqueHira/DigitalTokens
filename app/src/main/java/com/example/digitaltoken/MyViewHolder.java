package com.example.digitaltoken;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView mtextViewHead;
    TextView mtextViewDisc;
    ImageView mImageView;

    private ItemClickListener itemClickListener;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        this.mImageView = itemView.findViewById(R.id.imageIcon);
        this.mtextViewHead = (TextView) itemView.findViewById(R.id.title);
        this.mtextViewDisc = (TextView) itemView.findViewById(R.id.discription);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClickListener(v, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic) {
        this.itemClickListener = ic;
    }
}

