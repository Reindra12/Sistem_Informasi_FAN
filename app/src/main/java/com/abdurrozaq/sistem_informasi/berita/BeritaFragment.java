package com.abdurrozaq.sistem_informasi.berita;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.abdurrozaq.sistem_informasi.berita.model.BeritaItem;
import com.abdurrozaq.sistem_informasi.berita.model.BeritaModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.github.ybq.android.spinkit.style.FoldingCube;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class BeritaFragment extends Fragment {
    ArrayList<BeritaItem> beritaItems = new ArrayList<>();
    BeritaItem beritaItem;
    BeritaAdapter beritaAdapter;
    RecyclerView rberita;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_berita, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rberita = view.findViewById(R.id.rberita);

        progressBar = view.findViewById(R.id.spin_kit);
        FoldingCube doubleBounce = new FoldingCube();
        progressBar.setIndeterminateDrawable(doubleBounce);

        databerita();



        super.onViewCreated(view, savedInstanceState);
    }


    private void databerita() {
        AndroidNetworking.get(ApiServices.URL + ApiServices.BERITA)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("cek data", String.valueOf(response));

                        try {
                            JSONArray jsonArray = response.getJSONArray("berita");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                beritaItem = new BeritaItem();

                               JSONObject jsonObject = jsonArray.getJSONObject(i);
                               beritaItem.setTulisanIsi(jsonObject.getString("tulisan_isi"));
                               beritaItem.setTulisanGambar(jsonObject.getString("tulisan_gambar"));

                               beritaItem.setTulisanJudul(jsonObject.getString("tulisan_judul"));


                                progressBar.setVisibility(View.INVISIBLE);
                                beritaItems.add(beritaItem);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                rberita.setLayoutManager(layoutManager);

                                // define an adapter
                                beritaAdapter = new BeritaAdapter(getActivity(),beritaItems);
                                rberita.setAdapter(beritaAdapter);

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