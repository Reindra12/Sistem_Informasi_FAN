package com.abdurrozaq.sistem_informasi.pembayaran;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class FullDisplayImageActivity extends AppCompatActivity {
    RoundedImageView foto;
    String sfoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_display_image);

        foto = findViewById(R.id.imgfulldisplay);


        sfoto = getIntent().getStringExtra("foto");
        Picasso.get().load(ApiServices.GAMBAR+sfoto)
                .into(foto);

        Toast.makeText(this, ""+sfoto, Toast.LENGTH_SHORT).show();


    }
}