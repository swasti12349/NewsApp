package com.sro.newsapp;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


public class rv extends RecyclerView.Adapter<rv.myViewHolder> {

    private String[] data;
    private String[] des;
    Context context;
    private String[] imgLinks;
    private String[] newsLinks;

    public rv(String[] data, Context context, String[] imgLinks, String[] newsLinks, String[] des) {
        this.data = data;
        this.des = des;
        this.context = context;
        this.imgLinks = imgLinks;
        this.newsLinks = newsLinks;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageView;
        private TextView des;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textView =itemView.findViewById(R.id.tv);
            imageView =itemView.findViewById(R.id.image);
            des =itemView.findViewById(R.id.des);

        }
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
        myViewHolder vh = new myViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {

        holder.textView.setText(data[position]);
        holder.des.setText(des[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                int colorInt = Color.parseColor("#FF3700B3");
                builder.setToolbarColor(colorInt);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(context,Uri.parse(newsLinks[position]));

            }
        });
        Picasso.with(context).load(imgLinks[position]).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return data.length;
    }




}
