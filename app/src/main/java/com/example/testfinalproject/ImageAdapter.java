package com.example.testfinalproject;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder>{
    private ArrayList<Image> mImage;

    public ImageAdapter(ArrayList<Image> image) {
        mImage = image;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(layoutInflater, parent);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Image model = new Image();
        Glide.with(holder.img.getContext()).load(mImage.get(position).getImagePath()).into(holder.img);
    }
    @Override
    public int getItemCount() {
        return mImage.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img;
        ImageView iv_download;

        public MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.row_layout, parent,false));
            img = itemView.findViewById(R.id.img);
            iv_download = itemView.findViewById(R.id.iv_download);
            iv_download.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mImage.get(getAdapterPosition()).getImagePath().toString()));
            request.setTitle("download-image");
            String fileName = mImage.get(getAdapterPosition()).getImagePath().toString().split("/")[4];
            Context context = view.getContext();
            request.setDescription(fileName);
            request.setTitle(fileName);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.allowScanningByMediaScanner();
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName");
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        }
    }
}
