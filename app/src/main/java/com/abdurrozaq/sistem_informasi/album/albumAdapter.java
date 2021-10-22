package com.abdurrozaq.sistem_informasi.album;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.album.model.AlbumItem;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class albumAdapter extends RecyclerView.Adapter<albumAdapter.MyHolder> {

    ArrayList<AlbumItem> arrayList ;
    Activity activity;

    public albumAdapter(ArrayList<AlbumItem> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_isi_album , parent, false);
      return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        AlbumItem albumItem = arrayList.get(position);

        holder.judul.setText(albumItem.getAlbumNama());
        Picasso.get().load(ApiServices.GAMBAR + albumItem.getAlbumCover())
                .into(holder.gambar);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView judul;
        ImageView gambar;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            judul = itemView.findViewById(R.id.tvjudul);
            gambar = itemView.findViewById(R.id.imgalbum);

        }
    }
}
