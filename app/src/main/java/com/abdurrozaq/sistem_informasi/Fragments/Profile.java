package com.abdurrozaq.sistem_informasi.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abdurrozaq.sistem_informasi.Beranda.BerandaActivity;
import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.abdurrozaq.sistem_informasi.ui.login.LoginActivity;
import com.abdurrozaq.sistem_informasi.ui.register.RegisterActivity;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import static com.abdurrozaq.sistem_informasi.ui.login.LoginActivity.setStatusBarGradiant;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment implements View.OnClickListener {

    private static final String TAG = "profile";
    private EditText username, password;
    private Button login;
    private TextView daftar, loginadmin;
    private SharedPreferences sharedPreferences;
//    String activityData= getArguments().getString("dataKey");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        login = view.findViewById(R.id.btn_login);
        loginadmin = view.findViewById(R.id.tvloginadmin);

        username = view.findViewById(R.id.et_username);
        password = view.findViewById(R.id.et_password);
        daftar = view.findViewById(R.id.tvregister);


        daftar.setOnClickListener(this);
        login.setOnClickListener(this);
        loginadmin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                cekdata();
                break;
            case R.id.tvregister:
                startActivity(new Intent(getContext(), RegisterActivity.class));
                break;
            case R.id.tvloginadmin:
                startActivity(new Intent(getContext(), LoginAdminActivity.class));
                break;
        }
    }

    private void cekdata() {
        String user, pass;
        user = username.getText().toString();
        pass = password.getText().toString();
        if (user.equals("") || pass.equals("")) {
            Toast.makeText(getContext(), "Silahkan Lengkapi Data Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        } else {
            prosesLogin(user, pass);
        }
    }


    private void prosesLogin(String user, String pass) {
        AndroidNetworking.post(ApiServices.URL + ApiServices.LOGIN)
                .addBodyParameter("pengguna_username", user)
                .addBodyParameter("pengguna_password", pass)
                .addBodyParameter("hak_akses", "user")
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
                            if (data.equals("true")) {
                                Toast.makeText(getContext(), "Berhasil Login", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), BerandaActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                
                                SharedPreferences pref = getContext().getSharedPreferences("akun", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                try {
                                    editor.putString("id", c.getString("status"));// untuk menyimpan data dalam local

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(),"" + e, Toast.LENGTH_SHORT).show();
                                }
                                editor.commit();
                            }else{
                                Toast.makeText(getContext(), "data yang anda masukan salah", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Data Yang Anda Masukan Salah", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: Failed" + anError); //untuk log pada onerror
                        Toast.makeText(getContext(), "Data gagal ditambahkan", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}