package com.example.testfinalproject;

import android.app.Activity;
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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class ImageDownloadAdapter extends FirebaseRecyclerAdapter<ImageDownload, ImageDownloadAdapter.MyViewHolder> {

    public ImageDownloadAdapter(FirebaseRecyclerOptions<ImageDownload> options){
        super(options);

        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if(folder.isDirectory()){
            File[] files = folder.listFiles();
            for (File file: files) {
                file.getName();
            }
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(layoutInflater, parent);
    }

    protected void onBindViewHolder(MyViewHolder holder, int position, ImageDownload model){

        StorageReference storRef = FirebaseStorage.getInstance("gs://testfinalproject-66db7.appspot.com/").getReferenceFromUrl(model.getImg());
        Glide.with(holder.img.getContext()).load(storRef).into(holder.img);
        if(position == 0){
            Log.i("hldr", holder.img.getContext().toString());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img;
        ImageView iv_download;

        public MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.row_layout, parent, false));
            img = itemView.findViewById(R.id.img);
            iv_download = itemView.findViewById(R.id.iv_download);
            iv_download.setVisibility(View.VISIBLE);
            iv_download.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            ImageDownload imgDwn = getItem(getAdapterPosition());
            String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String url1 = "https://firebasestorage.googleapis.com/v0/b/testfinalproject-66db7.appspot.com/o/";
            String url2 = "?alt=media";
            String fileName = imgDwn.getImg().split("//")[1].split("/")[1];
            String finalUrl = url1 + fileName + url2;
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(finalUrl));
            String ext = fileName.split("\\.")[1];
            fileName = fileName.split("\\.")[0] + "_" + userUID + "-" + month + day + "." + ext;
            request.setTitle(fileName);
            Context context = view.getContext();
            request.setDescription(fileName);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.allowScanningByMediaScanner();
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        }
    }
}
