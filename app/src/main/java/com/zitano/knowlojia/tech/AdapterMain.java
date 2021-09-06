package com.zitano.knowlojia.tech;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMain extends RecyclerView.Adapter<Holder_Main> {
    Context c;
    ArrayList<Model_Main> models;
    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    // The unified native ad view type.
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;


    public AdapterMain(Context c, ArrayList<Model_Main> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public Holder_Main onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //convert xml to view obj
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_main, null);
        return new Holder_Main(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_Main holder, int position) {
        //bind data to our views
        holder.mTitleTv.setText(models.get(position).getTitle());
        holder.mDescrTv.setText(models.get(position).getDescription());
        holder.mImageIv.setImageResource(models.get(position).getImg());

        //animation
        Animation animation = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        holder.itemView.startAnimation(animation);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
              /*  if (models.get(pos).getTitle().equals("Android Tricks")){
                    Intent intent = new Intent(c, AndroidTricks.class);
                    c.startActivity(intent);
                }*/
                if (models.get(pos).getTitle().equals("Kenyan DIYs")){
                    Intent intent = new Intent(c, KenyanDIYs.class);
                    c.startActivity(intent);
                }
                if (models.get(pos).getTitle().equals("Windows Tips")){
                    Intent intent = new Intent(c, WindowsTricks.class);
                    c.startActivity(intent);
                }
                if (models.get(pos).getTitle().equals("Tech Buying Guide")){
                    Intent intent = new Intent(c, TechBuyingGuide.class);
                    c.startActivity(intent);
                }
                if (models.get(pos).getTitle().equals("Photography Tips")){
                    Intent intent = new Intent(c, Photography.class);
                    c.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}

