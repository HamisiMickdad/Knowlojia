package com.zitano.knowlojia.tech;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getBindingAdapterPosition());
            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

    }

    //set details to recyclerview row
    public void setDetails(Context ctx, String title, String description, String image) {
        //views
        MaterialTextView mTitleTv = mView.findViewById(R.id.rTitleTv);
        MaterialTextView mDetailTv = mView.findViewById(R.id.rDescriptionTv);
        AppCompatImageView mImageIv = mView.findViewById(R.id.rImageView);

        //content/description is in html/webform, we need to convert it to simple text using jsoup library
        Document document = Jsoup.parse(description);

        //set data to views
        mTitleTv.setText(title);
        mDetailTv.setText(document.text());
        //mDetailTv.setText(description);


       /* PicassoImageGetter imageGetter = new PicassoImageGetter(mDetailTv);
        Spannable html;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            html = (Spannable) Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
        } else {
            html = (Spannable) Html.fromHtml(description, imageGetter, null);
        }
        mDetailTv.setText(html);*/
        //mDetailTv.setMovementMethod(LinkMovementMethod.getInstance());
        //Picasso.get().load(image).into(mImageIv);

        Glide.with(ctx).load(image)
                .placeholder(R.drawable.ic_loader)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .thumbnail(0.05f)
                //.transition(DrawableTransitionOptions.withCrossFade())
                .into(mImageIv);
    }
    private ViewHolder.ClickListener mClickListener;

    // Interface to handle callbacks
    public interface  ClickListener {
        void onItemClick (View view, int position);
        void onItemLongClick (View view, int position);
    }

    public void setOnClickListener (ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}



/*
public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

    }
    //set details to recyclerview row
    public void setDetails(Context ctx, String title, String description, String image, String link) {
        //views
        MaterialTextView mTitleTv = mView.findViewById(R.id.rTitleTv);
        MaterialTextView mDetailTv = mView.findViewById(R.id.rDescriptionTv);
        MaterialTextView mLinkTv = mView.findViewById(R.id.rLinkTv);
        AppCompatImageView mImageIv = mView.findViewById(R.id.rImageView);

        //set data to views
        mTitleTv.setText(title);
        mDetailTv.setText(description);
        mLinkTv.setText(link);

        */
/*Glide.with(ctx).load(image)
                .thumbnail(0.25f)
                .into(mImageIv);*//*

        Picasso.get().load(image).into(mImageIv);

    }
    private ViewHolder.ClickListener mClickListener;

    // Interface to handle callbacks
    public interface  ClickListener {
        void onItemClick (View view, int position);
        void onItemLongClick (View view, int position);
    }

    public void setOnClickListener (ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}*/

