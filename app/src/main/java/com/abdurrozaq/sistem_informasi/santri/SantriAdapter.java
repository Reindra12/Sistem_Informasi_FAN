package com.abdurrozaq.sistem_informasi.santri;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.abdurrozaq.sistem_informasi.pembayaran.PembayaranActivity;
import com.abdurrozaq.sistem_informasi.santri.model.SantriItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SantriAdapter extends RecyclerView.Adapter<SantriAdapter.MyHolder> {


    ArrayList<SantriItem> arrayList;
    SantriItem santriItem;
    Activity activity;
    Context context;

    public SantriAdapter(ArrayList<SantriItem> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_santri, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        santriItem = arrayList.get(position);
        holder.namasantri.setText(santriItem.getNama());
        holder.alamatsantri.setText(santriItem.getTempatLahir());
        holder.ketsantri.setText(santriItem.getNamaStatus());
        holder.kode.setText(santriItem.getKodePembayaran());

        holder.status = santriItem.getNamaStatus();
        holder.id = santriItem.getSantri();
        Picasso.get().load(ApiServices.GAMBAR + santriItem.getFoto())
                .into(holder.foto);

        holder.linearsantri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, PembayaranActivity.class);
                intent.putExtra("status", holder.status);
                intent.putExtra("id_santri", holder.id);
                intent.putExtra("api", holder.api);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView namasantri, alamatsantri, ketsantri, kode;
        String status,id, api, dataadmin;
        LinearLayout linearsantri;
        ImageView foto;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            linearsantri = itemView.findViewById(R.id.linearsantri);
            kode = itemView.findViewById(R.id.tvkodepembayaran);
            namasantri = itemView.findViewById(R.id.tvnamasantri);
            alamatsantri = itemView.findViewById(R.id.tvalamatsantri);
            ketsantri = itemView.findViewById(R.id.tvketerangan);
            foto = itemView.findViewById(R.id.imgsantri);

        }
    }
}
