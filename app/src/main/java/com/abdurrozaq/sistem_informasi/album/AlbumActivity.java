package com.abdurrozaq.sistem_informasi.album;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.album.model.AlbumItem;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.ybq.android.spinkit.style.FoldingCube;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {
    RecyclerView recyclerViewalbum;
    ArrayList<AlbumItem> arrayList = new ArrayList<>();
    albumAdapter albumAdapter;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        recyclerViewalbum = findViewById(R.id.rv_album);

        progressBar = findViewById(R.id.spin_kit);
        FoldingCube doubleBounce = new FoldingCube();
        progressBar.setIndeterminateDrawable(doubleBounce);

        prosesdapatalbum();


    }

    private void prosesdapatalbum() {

        AndroidNetworking.get(ApiServices.URL + ApiServices.ALBUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("cek data", String.valueOf(response));

                        try {
                            JSONArray jsonArray = response.getJSONArray("album");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                AlbumItem albumItem = new AlbumItem();

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                albumItem.setAlbumNama(jsonObject.getString("album_nama"));
                                albumItem.setAlbumCover(jsonObject.getString("album_cover"));
//                                albumItem.setAlbumNama(jsonObject.getString("album_nama"));
//                                albumItem.setAlbumNama(jsonObject.getString("album_nama"));
//                                albumItem.setAlbumNama(jsonObject.getString("album_nama"));
                                progressBar.setVisibility(View.GONE);
                                arrayList.add(albumItem);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerViewalbum.setLayoutManager(layoutManager);

                                // define an adapter
                                albumAdapter = new albumAdapter(arrayList, AlbumActivity.this);
                                recyclerViewalbum.setAdapter(albumAdapter);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("MYAPP", "unexpected JSON exception", anError);
                    }
                });

    }
}