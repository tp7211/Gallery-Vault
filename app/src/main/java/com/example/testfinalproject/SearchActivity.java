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
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testfinalproject.Dialog.Confirm_dialog;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SearchActivity extends AppCompatActivity {

    TextInputLayout txtFind;
    Button btnFind;
    RecyclerView rView;
    Toolbar toolbar;
    private FirebaseAuth mAuth;

    RecyclerView.Adapter adapter;
    public ArrayList<Image> myImageList = new ArrayList<>();
    public ArrayList<String> elementNames = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
        mAuth = FirebaseAuth.getInstance();

        txtFind = findViewById(R.id.txtFind);
        btnFind = findViewById(R.id.btnFind);
        rView = findViewById(R.id.rView);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Search screen");
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this,MainActivity.class));

            }
        });
        rView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new ImageAdapter(myImageList);
        rView.setAdapter(adapter);
        btnFind.setOnClickListener(v -> {
            String srchImg = txtFind.getEditText().getText().toString();
            if(srchImg.trim().length() > 0){
                if(myImageList.size() > 0){
                    myImageList.clear();
                    elementNames.clear();
                }
                String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=653a0e19ade4364b08ac00b7c7250732&page=1&extras=url_l,description&format=json&nojsoncallback=%3F&safe_search=1&sort=relevance&text=" + srchImg;
                JsonObjectRequest myRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray pics = response.getJSONObject("photos").getJSONArray("photo");
                                    for(int i = 0; i < pics.length(); i++){
                                        JSONObject objectInArray = pics.getJSONObject(i);
                                        // check if "url_l" is present or not, if yes then add to the arraylist
                                        if(objectInArray.has("url_l")){
                                            elementNames.add(objectInArray.getString("url_l"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // running an iterator to add fetched images to myImageList of type Image
                                Iterator itr2 = elementNames.iterator();
                                while(itr2.hasNext()) {
                                    Image localImage = new Image();
                                    localImage.setImagePath(itr2.next().toString());
                                    myImageList.add(localImage);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("haksjdhaksjd", error.getMessage());
                            }
                        });
                queue.add(myRequest);
            }
        });
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
                new Confirm_dialog(SearchActivity.this).show("Logout!", "Are you sure you want to logout", "Logout", "NO", new DoInBackGroundCaller() {
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