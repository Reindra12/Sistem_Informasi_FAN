package com.abdurrozaq.sistem_informasi.Mehtods;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdurrozaq.sistem_informasi.Beranda.BerandaActivity;
import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.detail_santri.DetailSantriActivity;

public class Methods {
    Context context;

    public Methods(Context context) {
        this.context = context;
    }


    public void openDialog(String type, String message) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_opsi);

        final ImageView iv_dialog_img = dialog.findViewById(R.id.iv_dialog_img);
        final TextView tv_dialog_success = dialog.findViewById(R.id.tv_dialog_success);
        final TextView tv_dialog_msg = dialog.findViewById(R.id.tv_dialog_msg);
        final TextView tv_dialog_button = dialog.findViewById(R.id.tv_dialog_yes);
        final TextView tv_dialogno = dialog.findViewById(R.id.tv_dialog_no);

        if (type.equals("success")) {
            iv_dialog_img.setImageResource(R.drawable.ic_success);
            tv_dialog_success.setText("Success!");
            tv_dialog_button.setText("IYA, HAPUS");
            tv_dialog_button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3BB54A")));
            tv_dialog_button.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        } else if (type.equals("warning")) {
            iv_dialog_img.setImageResource(R.drawable.ic_warning);
            tv_dialog_success.setText("Warning");
            tv_dialog_button.setText("IYA, HAPUS");
            tv_dialog_button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEE58")));
            tv_dialog_button.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            tv_dialog_msg.setText("Apakah Anda Yakin Ingin Menghapus " + message);

        } else if (type.equals("logout")) {
            iv_dialog_img.setImageResource(R.drawable.ic_warning);
            tv_dialog_success.setText("Warning");
            tv_dialog_button.setText("IYA, KELUAR");
            tv_dialog_button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEE58")));
            tv_dialog_button.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            tv_dialog_msg.setText("Apakah Anda Yakin Keluar " + message);

        } else {
            iv_dialog_img.setImageResource(R.drawable.ic_error);
            tv_dialog_success.setText("Error!");
            tv_dialog_button.setText("Yeah it does.");
            tv_dialog_button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D7443E")));
            tv_dialog_button.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        }


        tv_dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (type.equals("logout")){
                    BerandaActivity.getInstance().pemberitahuankeluar();
                }else{
                    DetailSantriActivity.getInstance().prosesdeletesantri();
                }
            }
        });

        tv_dialogno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

}
