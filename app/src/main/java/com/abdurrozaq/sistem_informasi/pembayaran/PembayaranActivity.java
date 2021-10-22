package com.abdurrozaq.sistem_informasi.pembayaran;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.abdurrozaq.sistem_informasi.detail_santri.DetailSantriActivity;
import com.abdurrozaq.sistem_informasi.informasi.InformasiActivity;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PembayaranActivity extends AppCompatActivity {
    TextView nama, alamat, nomorhp, kodepembayaran, tvstatus;
    EditText bankpengirim, namapemilik, nomorrekening, jumlahpembayaran, bangkpengirim1, namapemilik1, nomorrekening1, jumlahpembayaran1;
    String id, bank, namaakun, nomor, jumlah, status, sfoto;
    Button detailsantri, upload, konfirmasi;
    SharedPreferences sharedPreferences;
    RoundedImageView fotobukti;
    String admin;
    RelativeLayout rlpembayaran, rlbuktibayar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        nama = findViewById(R.id.tv_nama);
        alamat = findViewById(R.id.tvalamatsantri);
        nomorhp = findViewById(R.id.tv_nomorhp);
        kodepembayaran = findViewById(R.id.tvkodepembayaran);
        tvstatus = findViewById(R.id.tv_status);
        rlpembayaran = findViewById(R.id.relativepembayaran);
        rlbuktibayar = findViewById(R.id.relativesudahbayar);


        bangkpengirim1 = findViewById(R.id.et_bank1);
        namapemilik1 = findViewById(R.id.et_namaakun1);
        nomorrekening1 = findViewById(R.id.et_nomorrekening1);
        jumlahpembayaran1 = findViewById(R.id.et_jumlahpembayaran1);
        fotobukti = findViewById(R.id.imgbuktipembayaran);

        detailsantri = findViewById(R.id.btn_detailtosantri);
        upload = findViewById(R.id.btn_upload);
        konfirmasi = findViewById(R.id.btn_upload1);


        namapemilik = findViewById(R.id.et_namaakun);
        bankpengirim = findViewById(R.id.et_bank);
        nomorrekening = findViewById(R.id.et_nomorrekening);
        jumlahpembayaran = findViewById(R.id.et_jumlahpembayaran);

        progressBar = findViewById(R.id.spin_kit);
        FoldingCube doubleBounce = new FoldingCube();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);




        id = getIntent().getStringExtra("id_santri");
        status = getIntent().getStringExtra("status");

        sharedPreferences = getSharedPreferences("akun", MODE_PRIVATE);
        admin = sharedPreferences.getString("id", "");


        perolehdataapi();


        detailsantri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ke detail santri
                Intent intent = new Intent(PembayaranActivity.this, DetailSantriActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                prosesupload

                if (validasidata()) {
                    startActivity(new Intent(PembayaranActivity.this, UploadActivity.class));
                }
            }
        });


    }

    private void perolehdataapi() {
        AndroidNetworking.get(ApiServices.URL + ApiServices.SUDAHBAYAR + id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("santri");
                            Log.d(TAG, "onResponse: " + response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                Toast.makeText(PembayaranActivity.this, "Pembayaran Berhasil", Toast.LENGTH_SHORT).show();
                                nama.setText(jsonObject.getString("nama"));
                                kodepembayaran.setText(jsonObject.getString("kode_pembayaran"));
                                alamat.setText(jsonObject.getString("tempat_lahir"));
                                nomorhp.setText(jsonObject.getString("no"));
                                status = jsonObject.getString("nama_status");
                                bank = jsonObject.getString("nama_bank");
                                namaakun = jsonObject.getString("nama_pengirim");
                                nomor = jsonObject.getString("no_bank");
                                jumlah = jsonObject.getString("jumlah_pembayaran");
                                sfoto = jsonObject.getString("bukti_bayar");

                                progressBar.setVisibility(View.INVISIBLE);

                                tvstatus.setText(status);

                                if (admin.equals("admin")) {
                                    validasiadmin();
                                } else {
                                    validasiuser();
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: " + e);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "onResponse: " + anError);

                    }
                });
    }

    private void validasiuser() {
        if (status.equals("Menunggu Pembayaran")) {
            rlpembayaran.setVisibility(View.VISIBLE);
        } else if (status.equals("Seleksi Tertulis OFFLINE")) {

            rlbuktibayar.setVisibility(View.VISIBLE);
            rlpembayaran.setVisibility(View.INVISIBLE);
            konfirmasi.setText("Lihat Jadwal");
            konfirmasi.setVisibility(View.VISIBLE);
            perolehdatapembayaran();
        } else {
            rlpembayaran.setVisibility(View.INVISIBLE);
        }

    }

    private void validasiadmin() {

        if (status.equals("Menunggu Pembayaran")) {
            rlpembayaran.setVisibility(View.INVISIBLE);
        } else if (status.equals("Seleksi Tertulis OFFLINE")) {
            rlbuktibayar.setVisibility(View.VISIBLE);
            rlpembayaran.setVisibility(View.INVISIBLE);
            konfirmasi.setText("Lihat Jadwal");
            konfirmasi.setVisibility(View.VISIBLE);
            perolehdatapembayaran();
        } else if (status.equals("Menunggu Konfirmasi")) {
            perolehdatapembayaran();
            rlbuktibayar.setVisibility(View.VISIBLE);
            rlpembayaran.setVisibility(GONE);
        }
    }

    private void perolehdatapembayaran() {
        bangkpengirim1.setText(bank);
        namapemilik1.setText(namaakun);
        nomorrekening1.setText(nomor);
        jumlahpembayaran1.setText(jumlah);

        Picasso.get().load(ApiServices.GAMBAR + sfoto)
                .into(fotobukti);

        fotobukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PembayaranActivity.this, FullDisplayImageActivity.class);
                intent.putExtra("foto", sfoto);
                startActivity(intent);
            }
        });


        konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("Seleksi Tertulis OFFLINE")) {


                    Intent intent = new Intent(PembayaranActivity.this, InformasiActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ProsesUpdateStatus();
                }
            }
        });

    }

    private void ProsesUpdateStatus() {
        AndroidNetworking.post(ApiServices.URL + ApiServices.UPDATESTATUS)
                .addBodyParameter("id_santri", id)
                .addBodyParameter("nama_status", "Seleksi Tertulis OFFLINE")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(PembayaranActivity.this, "Pembayaran Berhasil", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(PembayaranActivity.this, "GAGAL" , Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private boolean validasidata() {
        String sbankpengirim, snamapemilik, snomorrekening;
        int jumlah = 300000;
        String sjumlahpembayaran;


        if (bankpengirim.getText().toString().trim().isEmpty()) {
            bankpengirim.setError("Masukan Bank Pengirim");
            bankpengirim.requestFocus();
            return false;
        } else if (namapemilik.getText().toString().trim().isEmpty()) {
            namapemilik.setError("Masukan Nama Pemilik");
            bankpengirim.requestFocus();
            return false;

        } else if (nomorrekening.getText().toString().trim().isEmpty()) {
            nomorrekening.setError("Masukan Nomor Rekening");
            nomorrekening.requestFocus();
            return false;
        } else if (jumlahpembayaran.getText().toString().trim().isEmpty()) {
            jumlahpembayaran.setError("Masukan Jumlah Pembayaran");
            jumlahpembayaran.requestFocus();
            sjumlahpembayaran = jumlahpembayaran.getText().toString().trim();


            return false;


        }


        sjumlahpembayaran = jumlahpembayaran.getText().toString().trim();
        sbankpengirim = bankpengirim.getText().toString().trim();
        snamapemilik = namapemilik.getText().toString().trim();
        snomorrekening = nomorrekening.getText().toString().trim();

        int jumlahbayar = Integer.parseInt(sjumlahpembayaran);
        if (jumlahbayar < jumlah) {
            Toast.makeText(this, "Pembayaran Harus Rp.300.000", Toast.LENGTH_SHORT).show();
            return false;
        }


        sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("bankpengirim", sbankpengirim);
        editor.putString("namapemilik", snamapemilik);
        editor.putString("nomorrekening", snomorrekening);
        editor.putString("santri", id);
        editor.putString("jumlahpembayaran", sjumlahpembayaran);
        editor.putString("rekening_pesantren", "0826459818");

        editor.commit();
        return true;
    }
}


