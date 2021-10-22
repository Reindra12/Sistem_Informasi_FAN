package com.abdurrozaq.sistem_informasi.berita.DetailBerita;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdurrozaq.sistem_informasi.R;
import com.squareup.picasso.Picasso;

public class DetailBeritaActivity extends AppCompatActivity {
    TextView keterangan, judul;
    String judulberita, keteranganberita, fotoberita;
    ImageView gambar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);

        judul = findViewById(R.id.tvjudul);
        keterangan = findViewById(R.id.tvketerangan);
        gambar = findViewById(R.id.imgdetail);


        judulberita = getIntent().getStringExtra("judul");
        keteranganberita = getIntent().getStringExtra("keterangan");
        fotoberita = getIntent().getStringExtra("gambar");

        judul.setText(judulberita);
        keterangan.setText(Html.fromHtml(keteranganberita, Html.FROM_HTML_MODE_COMPACT));
        Picasso.get().load(fotoberita)
                .into(gambar);


    }
}