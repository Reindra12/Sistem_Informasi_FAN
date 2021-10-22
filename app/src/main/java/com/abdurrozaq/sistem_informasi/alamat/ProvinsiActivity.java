package com.abdurrozaq.sistem_informasi.alamat;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdurrozaq.sistem_informasi.Mehtods.Methods;
import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProvinsiActivity extends AppCompatActivity {
    ArrayList<ProvinsiItem> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    ResponseAlamat responseAlamat;
    ProgressBar progressBar;
    ImageView search;
    //    private Nemosofts nemosofts;
    private Toolbar toolbar;
    private EditText searchView;
    Methods methods;
    private String sdataprov, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provinsi);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
        toolbar = findViewById(R.id.toolbar_search);
        recyclerView = findViewById(R.id.recycler);
        progressBar = findViewById(R.id.progressBar_load);


        sdataprov = getIntent().getStringExtra("dataprov");
        key = getIntent().getStringExtra("key");

        prosesalamat();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        searchView = findViewById(R.id.et_search);
        searchView.setText(getIntent().getExtras().getString("SearchText"));
//        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    performSearch();
//                    return true;
//                }
//                return false;
//            }
//        });
//        search = findViewById(R.id.iv_search);
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                performSearch();
//            }
//        });
//
//
//        arrayList = new ArrayList<>();
//
//        methods = new Methods(this, new InterAdListener() {
//            @Override
//            public void onClick(int position, String type) {
//                NavigationUtil.PostDetailsActivity(SearchActivity.this, position, arrayList);
//            }
//        });
//
//        nemosofts = new Nemosofts(this, new InterClickListener() {
//            @Override
//            public void onClick(String type) {
//                getData();
//            }
//
//            @Override
//            public void onEnd(Boolean flag, String success, String message, String type) {
//
//            }
//        });
//


    }

    private void prosesalamat() {
        AndroidNetworking.get(ApiServices.PROVINSI + sdataprov)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONArray jsonArray = response.getJSONArray(key);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                ProvinsiItem provinsiItem = new ProvinsiItem();

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                provinsiItem.setNama(jsonObject.getString("nama"));
                                provinsiItem.setId(jsonObject.getString("id"));
                                provinsiItem.setDataid(key);

                                progressBar.setVisibility(View.GONE);
                                arrayList.add(provinsiItem);


                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                AlamatAdapter alamatAdapter = new AlamatAdapter(ProvinsiActivity.this, arrayList);
                                recyclerView.setAdapter(alamatAdapter);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onResponse: " + anError);
                        Toast.makeText(ProvinsiActivity.this, "" + anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
