package com.abdurrozaq.sistem_informasi.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "register";
    EditText nama, katasandi, nomor, email;
     Button daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nama = findViewById(R.id.et_username);
        katasandi = findViewById(R.id.et_password);
        email = findViewById(R.id.et_email);
        nomor = findViewById(R.id.et_nomor);
        daftar = findViewById(R.id.btn_daftar);

        daftar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_daftar:
                cekdata();
        }
    }

    private void cekdata() {
        String snama, snomor, semail, skatasandi;

        snama = nama.getText().toString();
        skatasandi = katasandi.getText().toString();
        snomor = nomor.getText().toString();
        semail = email.getText().toString();

        if (snama.equals("")||semail.equals("")||skatasandi.equals("")||snomor.equals("")){
            Toast.makeText(this, "Lengkapi Data Anda!!", Toast.LENGTH_SHORT).show();
        }else{
            prosesdaftar(snama, snomor, semail, skatasandi);
        }

    }

    private void prosesdaftar(String snama, String snomor, String semail, String skatasandi) {
        AndroidNetworking.post(ApiServices.URL + ApiServices.REGISTER)
                .addBodyParameter("pengguna_nama", snama)
                .addBodyParameter("pengguna_username", semail)
                .addBodyParameter("pengguna_password", skatasandi)
                .addBodyParameter("pengguna_nohp", snomor)

                .setTag("data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, "onResponse: " + response);
                        JSONObject c = response;
                        try {
                            String data = c.getString("status");
                            if (data.equals("true")){
                                Toast.makeText(RegisterActivity.this, "Berhasil Daftar", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                        SharedPreferences pref = getSharedPreferences("akun", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        try {
//                            editor.putString("token", c.getString("token"));// untuk menyimpan data dalam local
                            editor.putString("nama", c.getString("pengguna_nama"));
                            editor.putString("email", c.getString("pengguna_username"));
                            editor.putString("nohp", c.getString("pengguna_nohp"));

//                            Toast.makeText(login.this, c.getString("name"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Data Yang Anda Masukan Salah", Toast.LENGTH_SHORT).show();
                        }
                        editor.commit();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: Failed" + anError); //untuk log pada onerror
                        Toast.makeText(RegisterActivity.this, "Cek Koneksi Anda", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}