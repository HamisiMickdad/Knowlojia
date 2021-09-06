package com.zitano.knowlojia.tech;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

public class Holder_Main extends RecyclerView.ViewHolder implements View.OnClickListener {

    //views
    AppCompatImageView mImageIv;
    TextView mTitleTv, mDescrTv;

    ItemClickListener itemClickListener;

    public Holder_Main(@NonNull View itemView) {
        super(itemView);
        this.mImageIv = itemView.findViewById(R.id.modelImageIv);
        this.mTitleTv = itemView.findViewById(R.id.modelTitleTv);
        this.mDescrTv = itemView.findViewById(R.id.modelDescrTv);

        mDescrTv.setVisibility(View.GONE);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v, getLayoutPosition());
    }
    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }
}

