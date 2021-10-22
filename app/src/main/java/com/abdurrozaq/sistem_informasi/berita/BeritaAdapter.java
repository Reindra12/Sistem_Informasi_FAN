package com.abdurrozaq.sistem_informasi.berita;

import static com.abdurrozaq.sistem_informasi.apiServices.ApiServices.GAMBAR;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.berita.DetailBerita.DetailBeritaActivity;
import com.abdurrozaq.sistem_informasi.berita.model.BeritaItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.MyHolder> {
    Context context;
    List<BeritaItem> beritaItems;
    BeritaItem beritaItem;
    Activity activity;


    public BeritaAdapter(Activity activity, List<BeritaItem> beritaItems) {
//        this.context = context;
        this.activity = activity;
        this.beritaItems = beritaItems;
    }




    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_berita, parent, false);
        return new MyHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
    beritaItem = beritaItems.get(position);

    holder.judul.setText(beritaItems.get(position).getTulisanJudul());
    holder.diskripsi.setText(Html.fromHtml(beritaItem.getTulisanIsi(), Html.FROM_HTML_MODE_COMPACT));


    holder.judulberita =beritaItems.get(position).getTulisanJudul();
    holder.keterangan = beritaItems.get(position).getTulisanIsi();

    holder.foto = GAMBAR+beritaItems.get(position).getTulisanGambar();


        Picasso.get()
                .load(GAMBAR+beritaItems.get(position).getTulisanGambar())
                .placeholder(R.drawable.person_background_oval)
                .error(R.drawable.person_background_oval)
                .into(holder.gambarberita);

        holder.cardberita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailBeritaActivity.class);
                intent.putExtra("judul", holder.judulberita);
                intent.putExtra("keterangan", holder.keterangan);
                intent.putExtra("gambar", holder.foto);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beritaItems.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView judul, diskripsi;
        private CardView cardberita;
        private ImageView gambarberita;
        String keterangan, judulberita, foto;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            judul = itemView.findViewById(R.id.tvjudul);
            diskripsi = itemView.findViewById(R.id.tvdiskripsi);
            gambarberita = itemView.findViewById(R.id.imgberita);
            cardberita = itemView.findViewById(R.id.cberita);
        }
    }
}
