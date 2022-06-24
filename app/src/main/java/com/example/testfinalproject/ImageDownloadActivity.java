package com.example.testfinalproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.testfinalproject.Dialog.Confirm_dialog;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class ImageDownloadActivity extends AppCompatActivity {

    private Query iquery;
    private ImageDownloadAdapter iAdapter;
    private RecyclerView rDownloadView;
    private FirebaseAuth mAuth;
    int noOfDownsRemain;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_download);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Download images");
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImageDownloadActivity.this,MainActivity.class));

            }
        });
        Intent i = getIntent();
        // checking if the intent has come from package activity or not, if yes then setting the package in firebase database
        if (i.hasExtra("downloads")) {
            int downloads = Integer.parseInt(i.getStringExtra("downloads"));
            noOfDownsRemain = downloads;
            mAuth = FirebaseAuth.getInstance();
            String uid = mAuth.getCurrentUser().getUid();
            Date dt = new Date();
            UserPackage up = new UserPackage(uid, downloads, dt);
            FirebaseDatabase.getInstance().getReference().child("user_package").child(uid).setValue(up);
        }
        else {
            noOfDownsRemain = Integer.parseInt(i.getStringExtra("noOfDownsRemain"));
        }

        // noOfDownsRemain to restrict the number of downloads as per package selected
        iquery = FirebaseDatabase.getInstance("https://testfinalproject-66db7-default-rtdb.firebaseio.com/").getReference().child("images").limitToFirst(noOfDownsRemain);
        FirebaseRecyclerOptions<ImageDownload> options = new FirebaseRecyclerOptions.Builder<ImageDownload>().setQuery(iquery, ImageDownload.class).build();

        iAdapter = new ImageDownloadAdapter(options);
        rDownloadView = findViewById(R.id.rDownloadView);
        rDownloadView.setAdapter(iAdapter);
        rDownloadView.setLayoutManager(new GridLayoutManager(this, 2));
        iAdapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addform, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menuOptionSave:
                new Confirm_dialog(ImageDownloadActivity.this).show("Logout!", "Are you sure you want to logout", "Logout", "NO", new DoInBackGroundCaller() {
                    @Override
                    public void doTask() {
                        finish();
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), 0);
                    }

                    @Override
                    public void doTaskComplete() {

                    }
                }, new DoInBackGroundCaller() {
                    @Override
                    public void doTask() {

                    }

                    @Override
                    public void doTaskComplete() {

                    }
                });
                return true;
            default:
                return true;
        }

    }
}