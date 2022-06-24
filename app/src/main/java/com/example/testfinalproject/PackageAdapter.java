package com.example.testfinalproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PackageAdapter extends FirebaseRecyclerAdapter<Package, PackageAdapter.MyViewHolder> {
    public PackageAdapter(FirebaseRecyclerOptions<Package> options) {
        super(options);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtPackgName;
        TextView txtNum;
        RelativeLayout rl_bg;
        ImageView imgPackg;

        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.package_layout, parent, false));
            txtPackgName = itemView.findViewById(R.id.txtPackgName);
            txtNum = itemView.findViewById(R.id.txtNum);
            rl_bg = itemView.findViewById(R.id.rl_bg);
            imgPackg = itemView.findViewById(R.id.imgPackg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Package p = getItem(getAdapterPosition());
            Context myContext = view.getContext();
            Intent i = new Intent(myContext, ImageDownloadActivity.class);
            i.putExtra("downloads", Integer.toString(p.getDownloads()));
            myContext.startActivity(i);
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(layoutInflater, parent);
    }

    public void onBindViewHolder(MyViewHolder holder, int position, Package model) {
        StorageReference storRef = FirebaseStorage.getInstance("gs://testfinalproject-66db7.appspot.com/").getReferenceFromUrl(model.getImg());
        Glide.with(holder.imgPackg.getContext()).load(storRef).into(holder.imgPackg);
        // setting the background color of cards according to the package
        if (position == 0) {
            holder.rl_bg.setBackgroundColor(Color.parseColor("#C0C0C0"));
        }
        if (position == 2) {
            holder.rl_bg.setBackgroundColor(Color.parseColor("#e5e4e2"));
        }
        holder.txtPackgName.setText(model.getPackg_name());
        holder.txtNum.setText(Integer.toString(model.getDownloads()));
    }
}
