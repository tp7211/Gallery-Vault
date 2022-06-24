package com.example.testfinalproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testfinalproject.Dialog.Confirm_dialog;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    RelativeLayout btnPackages;
    RelativeLayout btnSearch;
    RelativeLayout btnLogout;
    public int noOfDownloads;
    TextView tv_pkg;
    private FirebaseAuth mAuth;
    public ArrayList<Image> myImageList = new ArrayList<>();
    public ArrayList<String> elementNames = new ArrayList<>();

    DatabaseReference reference;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), 0);
        } else {
            Toast.makeText(this, "Welcome " + mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ask for permission to access external storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }

        btnPackages = findViewById(R.id.btnPackages);
        btnSearch = findViewById(R.id.btnSearch);
        btnLogout = findViewById(R.id.btnLogout);
        toolbar = findViewById(R.id.toolbar);
        tv_pkg = findViewById(R.id.tv_pkg);
        toolbar.setTitle("Selection screen");
        toolbar.setTitleTextColor(getColor(R.color.white));


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            reference = FirebaseDatabase.getInstance("https://testfinalproject-66db7-default-rtdb.firebaseio.com/").getReference("user_package");
            // getting child document which has UID as its key
            reference.child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            noOfDownloads = Integer.parseInt(String.valueOf(dataSnapshot.child("downloads").getValue()));
                            // check if user has selected a package or not and changing the text and intended activity accordingly
                            if (noOfDownloads > 0) {
                                tv_pkg.setText("Download Images");

                                btnPackages.setOnClickListener(v -> {
                                    Intent imageDownloadIntent = new Intent(getApplicationContext(), ImageDownloadActivity.class);
                                    imageDownloadIntent.putExtra("noOfDownsRemain", String.valueOf(noOfDownloads));
                                    startActivity(imageDownloadIntent);
                                });
                            }
                        }
                    }
                }
            });
        }

        btnPackages.setOnClickListener(v -> {
            Intent packageIntent = new Intent(getApplicationContext(), PackageActivity.class);
            startActivity(packageIntent);
        });

        btnSearch.setOnClickListener(v -> {
            Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(searchIntent);
        });

        btnLogout.setOnClickListener(v -> {
            new Confirm_dialog(MainActivity.this).show("Logout!", "Are you sure you want to logout", "Logout", "NO", new DoInBackGroundCaller() {
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
//            mAuth = FirebaseAuth.getInstance();
//            mAuth.signOut();
//            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), 0);

//            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(mainIntent);
        });
    }


    @Override
    public void onBackPressed() {

        new Confirm_dialog(MainActivity.this).show("Exit App!", "Are you sure you want to exit App", "YES", "NO", new DoInBackGroundCaller() {
            @Override
            public void doTask() {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
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
    }
}