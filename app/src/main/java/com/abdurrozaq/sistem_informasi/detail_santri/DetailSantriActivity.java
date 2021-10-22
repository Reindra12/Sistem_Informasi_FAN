package com.abdurrozaq.sistem_informasi.detail_santri;

import static android.view.View.GONE;
import static com.abdurrozaq.sistem_informasi.apiServices.ApiServices.DELETESANTRI;
import static com.abdurrozaq.sistem_informasi.apiServices.ApiServices.GAMBAR;
import static com.abdurrozaq.sistem_informasi.apiServices.ApiServices.URL;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abdurrozaq.sistem_informasi.Beranda.BerandaActivity;
import com.abdurrozaq.sistem_informasi.Mehtods.Methods;
import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.SharedPref.Setting;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.abdurrozaq.sistem_informasi.pembayaran.FullDisplayImageActivity;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailSantriActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "";

    EditText _sjk, _stinggal, _spendidikan, _spernah;
    String id, stringfoto, stringkk, stringktp, stringijazah, snama;

    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    private RoundedImageView foto, kk, ktp, ijazah, deletesantri;
    EditText status, namasantri, tempatlahir, tanggallahir, namapondok, anak, saudara, namawali, ttlwali, pekerjaan, rt, desa, kec, kab, no;
    Methods methods;

    private static DetailSantriActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_santri);
        instance = this;

        namasantri = findViewById(R.id.et_namasantri);
        tempatlahir = findViewById(R.id.et_tempatlahir);
        tanggallahir = findViewById(R.id.et_tanggallahir);
        namapondok = findViewById(R.id.et_namapondok);

        namapondok.setVisibility(GONE);
        anak = findViewById(R.id.et_anak);
        saudara = findViewById(R.id.et_saudara);
        namawali = findViewById(R.id.et_namawali);
        ttlwali = findViewById(R.id.et_ttl);
        pekerjaan = findViewById(R.id.et_pekerjaan);
        rt = findViewById(R.id.et_rtrw);
        desa = findViewById(R.id.et_desa);
        kec = findViewById(R.id.et_kecamatan);
        kab = findViewById(R.id.et_kabupaten);
        no = findViewById(R.id.et_nohpwali);
        status = findViewById(R.id.etstatus);

        _sjk = findViewById(R.id.sp_jeniskelamin);
        _stinggal = findViewById(R.id.sptinggal);
        _spendidikan = findViewById(R.id.sppendidikan);
        _spernah = findViewById(R.id.spmondok);

        foto = findViewById(R.id.imgcalonsantri);
        ijazah = findViewById(R.id.add_2);
        kk = findViewById(R.id.add_3);
        ktp = findViewById(R.id.add_4);
        deletesantri = findViewById(R.id.imgdelete);


        foto.setOnClickListener(this);
        ijazah.setOnClickListener(this);
        kk.setOnClickListener(this);
        ktp.setOnClickListener(this);
        deletesantri.setOnClickListener(this);

        id = getIntent().getStringExtra("id");

        requestapi();
        cekid();

        methods = new Methods(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Setting.Dark_Mode) {
                progressDialog = new ProgressDialog(DetailSantriActivity.this, R.style.ThemeDialog2);
            } else {
                progressDialog = new ProgressDialog(DetailSantriActivity.this, R.style.ThemeDialog);
            }
        } else {
            progressDialog = new ProgressDialog(DetailSantriActivity.this);
        }

        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        deletesantri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                methods.openDialog("success", snama + "?");


            }

        });


    }

    public static DetailSantriActivity getInstance() {
        return instance;
    }

    private void cekid() {
        sharedPreferences = getSharedPreferences("akun", MODE_PRIVATE);
        String idadmin = sharedPreferences.getString("id", "");


        if (idadmin.equals("admin")) {
            sharedPreferences = getSharedPreferences("dataadmin", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("admin", "admin");
            editor.commit();

            deletesantri.setVisibility(View.VISIBLE);
        } else {
            deletesantri.setVisibility(GONE);
        }


    }


    private void requestapi() {
        AndroidNetworking.get(ApiServices.URL + ApiServices.SANTRIBYID + id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("santri");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);


                                snama = jsonObject.getString("nama");

                                namasantri.setText(snama);
                                tempatlahir.setText(jsonObject.getString("tempat_lahir"));
                                no.setText(jsonObject.getString("no"));
                                tanggallahir.setText(jsonObject.getString("tanggal_lahir"));
                                namapondok.setText(jsonObject.getString("namapondok"));
                                anak.setText(jsonObject.getString("anakkeberapa"));
                                saudara.setText(jsonObject.getString("berapasaudara"));
                                namawali.setText(jsonObject.getString("nama_wali"));
                                ttlwali.setText(jsonObject.getString("ttl"));
                                pekerjaan.setText(jsonObject.getString("pekerjaan"));
                                rt.setText(jsonObject.getString("rtrw"));
                                desa.setText(jsonObject.getString("desa"));
                                kec.setText(jsonObject.getString("kecamatan"));
                                kab.setText(jsonObject.getString("kabupaten"));
                                status.setText(jsonObject.getString("nama_status"));


                                _sjk.setText(jsonObject.getString("jk"));
                                _stinggal.setText(jsonObject.getString("tinggalbersama"));
                                _spendidikan.setText(jsonObject.getString("pendidikanterakhir"));
                                _spernah.setText(jsonObject.getString("pernahmondok"));
                                stringfoto = jsonObject.getString("foto");
                                stringkk = jsonObject.getString("kk");
                                stringktp = jsonObject.getString("ktp");
                                stringijazah = jsonObject.getString("ijazah");


                                Picasso.get().load(GAMBAR + stringfoto)
                                        .into(foto);

                                Picasso.get().load(GAMBAR + jsonObject.getString("ijazah"))
                                        .into(ijazah);

                                Picasso.get().load(GAMBAR + jsonObject.getString("kk"))
                                        .into(kk);

                                Picasso.get().load(GAMBAR + jsonObject.getString("ktp"))
                                        .into(ktp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgcalonsantri:
                Intent intent = new Intent(DetailSantriActivity.this, FullDisplayImageActivity.class);
                intent.putExtra("foto", stringfoto);
//                Toast.makeText(this, ""+stringfoto, Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.add_2:
                Intent kedua = new Intent(DetailSantriActivity.this, FullDisplayImageActivity.class);
                kedua.putExtra("foto", stringijazah);
                startActivity(kedua);
                break;
            case R.id.add_3:
                Intent ketiga = new Intent(DetailSantriActivity.this, FullDisplayImageActivity.class);
                ketiga.putExtra("foto", stringkk);
                startActivity(ketiga);
                break;
            case R.id.add_4:
                Intent keempat = new Intent(DetailSantriActivity.this, FullDisplayImageActivity.class);
                keempat.putExtra("foto", stringktp);
                startActivity(keempat);
                break;

        }
    }

    public void prosesdeletesantri() {
        AndroidNetworking.post(URL + DELETESANTRI)
                .addBodyParameter("id_santri", id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailSantriActivity.this, "BERHASIL", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailSantriActivity.this, BerandaActivity.class));

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(DetailSantriActivity.this, "" + anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}