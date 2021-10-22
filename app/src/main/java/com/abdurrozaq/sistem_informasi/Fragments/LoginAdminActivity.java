package com.abdurrozaq.sistem_informasi.Fragments;

import static com.abdurrozaq.sistem_informasi.ui.login.LoginActivity.setStatusBarGradiant;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abdurrozaq.sistem_informasi.Beranda.BerandaActivity;
import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.abdurrozaq.sistem_informasi.ui.home.adapters.BeritaActivity;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginAdminActivity extends AppCompatActivity {
    private static final String TAG = "profile";
    private EditText username, password;
    private Button login;
    private TextView daftar;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        login = findViewById(R.id.btn_login);

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);

        setStatusBarGradiant(LoginAdminActivity.this);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekdata();
            }
        });

    }

    private void cekdata() {
        String user, pass;
        user = username.getText().toString();
        pass = password.getText().toString();
        if (user.equals("") || pass.equals("")) {
            Toast.makeText(this, "Silahkan Lengkapi Data Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        } else {
            prosesLogin(user, pass);
        }
    }

    private void prosesLogin(String user, String pass) {

        AndroidNetworking.post(ApiServices.URL + ApiServices.LOGIN)
                .addBodyParameter("pengguna_username", user)
                .addBodyParameter("pengguna_password", pass)
                .addBodyParameter("hak_akses", "admin")
                .setTag("data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(LoginAdminActivity.this, "pertama", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: " + response);
                        JSONObject c = response;
                        try {
                            String data = c.getString("status");
                            if (data.equals("true")) {
                                Toast.makeText(LoginAdminActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginAdminActivity.this, BerandaActivity.class);
                                startActivity(intent);
                               finish();

                                SharedPreferences pref = LoginAdminActivity.this.getSharedPreferences("akun", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();

                                    editor.putString("id", "admin");// untuk menyimpan data dalam local

                                editor.commit();
                            }else{
                                Toast.makeText(LoginAdminActivity.this, "data yang anda masukan salah", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginAdminActivity.this, "Data Yang Anda Masukan Salah", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: Failed" + anError); //untuk log pada onerror
                        Toast.makeText(LoginAdminActivity.this, "Data gagal ditambahkan", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

}