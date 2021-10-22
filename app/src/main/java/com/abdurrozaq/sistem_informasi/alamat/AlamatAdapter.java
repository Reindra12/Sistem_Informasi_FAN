package com.abdurrozaq.sistem_informasi.alamat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abdurrozaq.sistem_informasi.R;

import java.util.ArrayList;

public class AlamatAdapter extends RecyclerView.Adapter<AlamatAdapter.MyHolder> {

    Activity activity;
    ArrayList<ProvinsiItem> arrayList;
    private RecyclerItemClickListener listener;
    ProvinsiItem provinsiItem;

    public AlamatAdapter(Activity activity, ArrayList<ProvinsiItem> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    public AlamatAdapter(Activity activity1,  ArrayList<ProvinsiItem> arrayList1, RecyclerItemClickListener listitem) {
        this.activity = activity1;
        this.arrayList = arrayList1;
        this.listener = listitem;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alamat, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
         provinsiItem = arrayList.get(position);

        holder.provinsi = provinsiItem.getNama();
        holder.nama.setText(holder.provinsi);
        holder.idalamat = provinsiItem.getId();
        holder.dataprov = provinsiItem.getDataid();

        holder.bind(provinsiItem, listener);
//        holder.calamat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, PendaftarActivity.class);
//                intent.putExtra("provinsi", holder.provinsi);
//                intent.putExtra("idalamat", holder.idalamat);
//                intent.putExtra("dataprovensi", holder.dataprov);
//
////                Toast.makeText(activity, ""+holder.idalamat, Toast.LENGTH_SHORT).show();
//                activity.startActivity(intent);
////                activity.finish();
//            }
//        });
    }

    @Override
    public int getItemCount() {

        return arrayList.size();

    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView nama;
        CardView calamat;
        String provinsi, idalamat, dataprov;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.tvjudul);
            calamat = itemView.findViewById(R.id.cvalamat);
        }

        public void bind(final ProvinsiItem listltemCategory, final RecyclerItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onClickListener(listltemCategory, getLayoutPosition(), provinsi, idalamat);
                }
            });
        }
    }

    public interface RecyclerItemClickListener{
        void onClickListener(ProvinsiItem listltemCategory, int position, String id, String nama);

    }

}
