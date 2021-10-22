package com.abdurrozaq.sistem_informasi.santri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.abdurrozaq.sistem_informasi.santri.model.SantriItem;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SantriActivity extends AppCompatActivity {
    RecyclerView recyclerViewsantri;
    ArrayList<SantriItem> arrayList = new ArrayList<>();
    SantriAdapter santriAdapter;
    SharedPreferences sharedPreferences;
    public static String sort_by= "newest";
    private String setectedNoteColor, dataadmin;
    private ImageView iv_filter_sort;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Boolean isFilter = false;
    private Boolean isOver_home = false, isScroll_home = false;
    private int page = 1;
    ImageView filter;
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_santri);

        recyclerViewsantri = findViewById(R.id.rvsantri);
        filter = findViewById(R.id.imgfilter);
         progressBar = findViewById(R.id.spin_kit);


        FoldingCube doubleBounce = new FoldingCube();
        progressBar.setIndeterminateDrawable(doubleBounce);

        prosesdatasantri();

        setectedNoteColor = "#E9A0A0A0";
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });
    }

    private void prosesdatasantri() {
        AndroidNetworking.get(ApiServices.URL + ApiServices.SANTRI)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("cek data", String.valueOf(response));

                        try {
                            JSONArray jsonArray = response.getJSONArray("santri");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                SantriItem santriItem = new SantriItem();

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                santriItem.setKodePembayaran(jsonObject.getString("kode_pembayaran"));
                                santriItem.setNama(jsonObject.getString("nama"));
                                santriItem.setTempatLahir(jsonObject.getString("tempat_lahir"));
                                santriItem.setNamaStatus(jsonObject.getString("nama_status"));
                                santriItem.setFoto(jsonObject.getString("foto"));
                                santriItem.setSantri(jsonObject.getString("santri"));
                                santriItem.setKk((jsonObject.getString("kk")));
                                santriItem.setKtp((jsonObject.getString("ktp")));
                                santriItem.setIjazah(jsonObject.getString("ijazah"));


                                arrayList.add(santriItem);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerViewsantri.setLayoutManager(layoutManager);

                                progressBar.setVisibility(View.GONE);
                                // define an adapter
                                santriAdapter = new SantriAdapter(arrayList, SantriActivity.this);
                                recyclerViewsantri.setAdapter(santriAdapter);
                                santriAdapter.notifyDataSetChanged();
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
    private void openFilterDialog() {
        Dialog dialog_rate = new Dialog(SantriActivity.this);
        dialog_rate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_rate.setContentView(R.layout.dialog_flter_sort);

        final ImageView iv_close = dialog_rate.findViewById(R.id.iv_rate_close);
        final Button submit = dialog_rate.findViewById(R.id.button_submit_filter);
        final Button cancel = dialog_rate.findViewById(R.id.button_cancel_filter);
        final TextView menunggupembayaran = dialog_rate.findViewById(R.id.tv_menunggupembayaran);
        final TextView menunggukonfirmasi = dialog_rate.findViewById(R.id.tv_menunggukonfirmasi);
//        final TextView sudahdibayar = dialog_rate.findViewById(R.id.tv_sudahdibayar);
        final TextView seleksi = dialog_rate.findViewById(R.id.tv_seleksi);

        if (sort_by =="Menunggu Pembayaran"){
            menunggupembayaran.setBackgroundResource(R.drawable.background_filter_active);
            menunggukonfirmasi.setBackgroundResource(R.drawable.background_filter_deactive);
//            sudahdibayar.setBackgroundResource(R.drawable.background_filter_deactive);
            seleksi.setBackgroundResource(R.drawable.background_filter_deactive);
            menunggupembayaran.setTextColor(ContextCompat.getColor(SantriActivity.this, R.color.colorAccent_Light));
            menunggukonfirmasi.setTextColor(Color.parseColor(setectedNoteColor));
//            sudahdibayar.setTextColor(Color.parseColor(setectedNoteColor));
            seleksi.setTextColor(Color.parseColor(setectedNoteColor));
            sort_by = "Menunggu Pembayaran";

        }else if (sort_by =="Menunggu Konfirmasi"){
            menunggupembayaran.setBackgroundResource(R.drawable.background_filter_deactive);
            menunggukonfirmasi.setBackgroundResource(R.drawable.background_filter_active);
//            sudahdibayar.setBackgroundResource(R.drawable.background_filter_deactive);
            seleksi.setBackgroundResource(R.drawable.background_filter_deactive);
            menunggupembayaran.setTextColor(Color.parseColor(setectedNoteColor));
            menunggukonfirmasi.setTextColor(ContextCompat.getColor(SantriActivity.this, R.color.colorAccent_Light));
//            sudahdibayar.setTextColor(Color.parseColor(setectedNoteColor));
            seleksi.setTextColor(Color.parseColor(setectedNoteColor));
            sort_by = "Menunggu Konfirmasi";



        }else if (sort_by =="Sudah Dikonfirmasi"){
            menunggupembayaran.setBackgroundResource(R.drawable.background_filter_deactive);
            menunggukonfirmasi.setBackgroundResource(R.drawable.background_filter_deactive);
//            sudahdibayar.setBackgroundResource(R.drawable.background_filter_active);
            seleksi.setBackgroundResource(R.drawable.background_filter_deactive);
            menunggupembayaran.setTextColor(Color.parseColor(setectedNoteColor));
            menunggukonfirmasi.setTextColor(Color.parseColor(setectedNoteColor));
//            sudahdibayar.setTextColor(ContextCompat.getColor(SantriActivity.this, R.color.colorAccent_Light));
            seleksi.setTextColor(Color.parseColor(setectedNoteColor));
            sort_by = "Sudah Dikonfirmasi";

        }else if (sort_by =="Seleksi Tes Offline"){
            menunggupembayaran.setBackgroundResource(R.drawable.background_filter_deactive);
            menunggukonfirmasi.setBackgroundResource(R.drawable.background_filter_deactive);
//            sudahdibayar.setBackgroundResource(R.drawable.background_filter_deactive);
            seleksi.setBackgroundResource(R.drawable.background_filter_active);
            menunggupembayaran.setTextColor(Color.parseColor(setectedNoteColor));
            menunggukonfirmasi.setTextColor(Color.parseColor(setectedNoteColor));
//            sudahdibayar.setTextColor(Color.parseColor(setectedNoteColor));
            seleksi.setTextColor(ContextCompat.getColor(SantriActivity.this, R.color.colorAccent_Light));
            sort_by = "Seleksi Tes Offline";
        }

        menunggupembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menunggupembayaran.setBackgroundResource(R.drawable.background_filter_active);
                menunggukonfirmasi.setBackgroundResource(R.drawable.background_filter_deactive);
//                sudahdibayar.setBackgroundResource(R.drawable.background_filter_deactive);
                seleksi.setBackgroundResource(R.drawable.background_filter_deactive);
                menunggupembayaran.setTextColor(ContextCompat.getColor(SantriActivity.this, R.color.colorAccent_Light));
                menunggukonfirmasi.setTextColor(Color.parseColor(setectedNoteColor));
//                sudahdibayar.setTextColor(Color.parseColor(setectedNoteColor));
                seleksi.setTextColor(Color.parseColor(setectedNoteColor));
                sort_by = "Menunggu Pembayaran";
            }
        });
        menunggukonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menunggupembayaran.setBackgroundResource(R.drawable.background_filter_deactive);
                menunggukonfirmasi.setBackgroundResource(R.drawable.background_filter_active);
//                sudahdibayar.setBackgroundResource(R.drawable.background_filter_deactive);
                seleksi.setBackgroundResource(R.drawable.background_filter_deactive);
                menunggupembayaran.setTextColor(Color.parseColor(setectedNoteColor));
                menunggukonfirmasi.setTextColor(ContextCompat.getColor(SantriActivity.this, R.color.colorAccent_Light));
//                sudahdibayar.setTextColor(Color.parseColor(setectedNoteColor));
                seleksi.setTextColor(Color.parseColor(setectedNoteColor));
                sort_by = "Menunggu Konfirmasi";
            }
        });

        seleksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menunggupembayaran.setBackgroundResource(R.drawable.background_filter_deactive);
                menunggukonfirmasi.setBackgroundResource(R.drawable.background_filter_deactive);
//                sudahdibayar.setBackgroundResource(R.drawable.background_filter_deactive);
                seleksi.setBackgroundResource(R.drawable.background_filter_active);
                menunggupembayaran.setTextColor(Color.parseColor(setectedNoteColor));
                menunggukonfirmasi.setTextColor(Color.parseColor(setectedNoteColor));
//                sudahdibayar.setTextColor(Color.parseColor(setectedNoteColor));
                seleksi.setTextColor(ContextCompat.getColor(SantriActivity.this, R.color.colorAccent_Light));
                sort_by = "Seleksi Tertulis Offline";
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loadSortBy();
                filterSort();
                dialog_rate.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sort_by != "newest"){
                    sort_by = "newest";
                    loadSortBy();
                    filterSort();
                }
                dialog_rate.dismiss();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_rate.dismiss();
            }
        });

        dialog_rate.setCancelable(false);
        dialog_rate.setCanceledOnTouchOutside(false);
        dialog_rate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_rate.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_rate.show();
        Window window = dialog_rate.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }



    private void loadSortBy() {
        arrayList.clear();
        isOver_home = false;
        isScroll_home = false;
        page = 1;
        santriAdapter.notifyDataSetChanged();
//        getTopAd(true);
    }

    private void filterSort() {
        if (sort_by.equals("newest")){
//            iv_filter_sort.setVisibility(View.GONE);
            Toast.makeText(this, ""+sort_by, Toast.LENGTH_SHORT).show();
            prosesdatasantri();
        }else {
            Toast.makeText(this, ""+sort_by, Toast.LENGTH_SHORT).show();
            arsipdata();
//            if (iv_filter_sort.getVisibility()==View.GONE) {
//                iv_filter_sort.setVisibility(View.VISIBLE);
//            }
        }
    }

    private void arsipdata() {

        AndroidNetworking.post(ApiServices.URL + ApiServices.ARSIP)
                .addBodyParameter("nama_status", sort_by)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("cek data", String.valueOf(response));

                        try {
                            JSONArray jsonArray = response.getJSONArray("arsip");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                SantriItem santriItem = new SantriItem();

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                santriItem.setKodePembayaran(jsonObject.getString("kode_pembayaran"));
                                santriItem.setNama(jsonObject.getString("nama"));
                                santriItem.setTempatLahir(jsonObject.getString("tempat_lahir"));
                                santriItem.setNamaStatus(jsonObject.getString("nama_status"));
                                santriItem.setFoto(jsonObject.getString("foto"));
                                santriItem.setSantri(jsonObject.getString("id_santri"));

                                progressBar.setVisibility(View.GONE);
                                arrayList.add(santriItem);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerViewsantri.setLayoutManager(layoutManager);

                                // define an adapter
                                santriAdapter = new SantriAdapter(arrayList, SantriActivity.this);
                                recyclerViewsantri.setAdapter(santriAdapter);

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