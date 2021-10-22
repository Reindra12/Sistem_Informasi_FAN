package com.abdurrozaq.sistem_informasi.Beranda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abdurrozaq.sistem_informasi.Mehtods.Methods;
import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.album.AlbumActivity;
import com.abdurrozaq.sistem_informasi.detail_santri.DetailSantriActivity;
import com.abdurrozaq.sistem_informasi.informasi.InformasiActivity;
import com.abdurrozaq.sistem_informasi.pendaftar.PendaftarActivity;
import com.abdurrozaq.sistem_informasi.santri.SantriActivity;
import com.abdurrozaq.sistem_informasi.ui.home.adapters.BeritaActivity;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ViewListener;

public class BerandaActivity extends AppCompatActivity implements View.OnClickListener {
    CarouselView banner;
    TextView keluar;
    private SharedPreferences sharedPreferences;
    int[] sampleImages = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};
    long kembali;
    LinearLayout linearsantri, linearLayoutdaftar, linearLayoutalbum, linearLayoutinformasi, linearLayoutberita;
    Methods methods;
    String user;
    private static BerandaActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        instance = this;


        banner = findViewById(R.id.imgbanner);
        keluar = findViewById(R.id.tvkeluar);

        banner.setPageCount(sampleImages.length);
        banner.setViewListener(viewListener);
        banner.setSlideInterval(3000);

        linearsantri = findViewById(R.id.linearsantri);
        linearLayoutdaftar = findViewById(R.id.lineardaftar);
        linearLayoutalbum = findViewById(R.id.llalbum);
        linearLayoutinformasi = findViewById(R.id.llinformasi);
        linearLayoutberita = findViewById(R.id.llberita);

        linearsantri.setOnClickListener(this);
        linearLayoutdaftar.setOnClickListener(this);
        linearLayoutalbum.setOnClickListener(this);
        linearLayoutinformasi.setOnClickListener(this);
        linearLayoutberita.setOnClickListener(this);

        methods = new Methods(this);
        cekid();

        banner.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
//                Toast.makeText(BerandaActivity.this, "Clicked item: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methods.openDialog("logout", user);

            }
        });


    }

    public void pemberitahuankeluar() {
        SharedPreferences.Editor editor = getSharedPreferences("akun", MODE_PRIVATE).edit();
        editor.putString("id", null);
        editor.apply();

        Intent intent = new Intent(BerandaActivity.this, BeritaActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(BerandaActivity.this, "Berhasil Keluar", Toast.LENGTH_LONG).show();

    }

    private void cekid() {
        sharedPreferences = getSharedPreferences("akun", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        user = sharedPreferences.getString("username", "");


        if (id.equals("admin")) {
            sharedPreferences = getSharedPreferences("dataadmin", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("admin", "admin");
            editor.commit();

            linearLayoutdaftar.setVisibility(View.GONE);
        } else {
            linearLayoutdaftar.setVisibility(View.VISIBLE);
        }
    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {

            View customView = getLayoutInflater().inflate(R.layout.view_custom, null);

            ImageView fruitImageView = (ImageView) customView.findViewById(R.id.fruitImageView);

            fruitImageView.setImageResource(sampleImages[position]);

            banner.setIndicatorGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

            return customView;
        }
    };

    @Override
    public void onBackPressed() {
        if (kembali + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Klik Sekali Lagi Untuk Keluar Dari Aplikasi", Toast.LENGTH_SHORT).show();

        }

        kembali = System.currentTimeMillis();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearsantri:
                Intent intent = new Intent(BerandaActivity.this, SantriActivity.class);
                startActivity(intent);
                break;
            case R.id.lineardaftar:
                Intent d = new Intent(BerandaActivity.this, PendaftarActivity.class);
                startActivity(d);
                break;
            case R.id.llalbum:
                Intent a = new Intent(BerandaActivity.this, AlbumActivity.class);
                startActivity(a);
                break;
            case R.id.llinformasi:

                Intent i = new Intent(BerandaActivity.this, InformasiActivity.class);
                startActivity(i);
                break;
            case R.id.llberita:
                Intent b = new Intent(BerandaActivity.this, BeritaActivity.class);
                b.putExtra("id", "1");
                startActivity(b);
                break;
        }

    }
    public static BerandaActivity getInstance() {
        return instance;
    }
}
