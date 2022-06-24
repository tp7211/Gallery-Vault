package com.example.testfinalproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.testfinalproject.Dialog.Confirm_dialog;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class PackageActivity extends AppCompatActivity {

    private RecyclerView rPackgView;
    private PackageAdapter pAdapter;
    private Query query;
    Toolbar toolbar;
    private FirebaseAuth mAuth;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Package screen");
        toolbar.setTitleTextColor(getColor(R.color.white));
               setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PackageActivity.this,MainActivity.class));

            }
        });


        query = FirebaseDatabase.getInstance("https://testfinalproject-66db7-default-rtdb.firebaseio.com/").getReference().child("packages");
        FirebaseRecyclerOptions<Package> options = new FirebaseRecyclerOptions.Builder<Package>().setQuery(query, Package.class).build();

        pAdapter = new PackageAdapter(options);
        rPackgView = findViewById(R.id.rPackgView);
        rPackgView.setAdapter(pAdapter);
        rPackgView.setLayoutManager(new LinearLayoutManager(this));

        pAdapter.startListening();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addform,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.menuOptionSave:
                new Confirm_dialog(PackageActivity.this).show("Logout!", "Are you sure you want to logout", "Logout", "NO", new DoInBackGroundCaller() {
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