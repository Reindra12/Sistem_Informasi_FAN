package com.abdurrozaq.sistem_informasi.pembayaran;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.abdurrozaq.sistem_informasi.Beranda.BerandaActivity;
import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.abdurrozaq.sistem_informasi.pendaftar.PendaftarActivity;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.github.ybq.android.spinkit.style.FoldingCube;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class UploadActivity extends AppCompatActivity {
    private Button btnkonfirmasi;

    private ImageView gambar;
    private final int PICK_IMAGE = 12345;
    private final int TAKE_PICTURE = 6352;
    private static final int REQUEST_CAMERA_ACCESS_PERMISSION = 5674;
    private Bitmap bitmap;
    SharedPreferences sharedPreferences;
    String id, sbankpengirim, snamapemilik, snomorrekening, sjumlahpembayaran, skodepembayaran, srekening_pesantren;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        progressBar = findViewById(R.id.spin_kit);
        FoldingCube doubleBounce = new FoldingCube();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.INVISIBLE);


        gambar = findViewById(R.id.imguploadbukti);
        btnkonfirmasi = findViewById(R.id.btn_uploadbukti);

        terimadata();

        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkameradialog();
            }
        });
        btnkonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cekdata()) {
                    simpanKonfirmasi();
                    progressBar.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void terimadata() {
        sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);
        snamapemilik = sharedPreferences.getString("namapemilik", "");
        sbankpengirim = sharedPreferences.getString("bankpengirim", "");
        snomorrekening = sharedPreferences.getString("nomorrekening", "");
        sjumlahpembayaran = sharedPreferences.getString("jumlahpembayaran", "");
        id = sharedPreferences.getString("santri", "");
        skodepembayaran = sharedPreferences.getString("kode_pembayaran", "");
        srekening_pesantren = sharedPreferences.getString("rekening_pesantren", "");

    }


    private void tampilkameradialog() {

        final Dialog main_dialog;

        main_dialog = new Dialog(UploadActivity.this);
        main_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        main_dialog.setContentView(R.layout.camera_dialog);

        final Button camera = main_dialog.findViewById(R.id.camera);
        final Button folder = main_dialog.findViewById(R.id.folder);
        final ImageView close = main_dialog.findViewById(R.id.close);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && ActivityCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_ACCESS_PERMISSION);
                } else {
                    gambardarikamera();
                }

                main_dialog.dismiss();
            }
        });


        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihdarigalery();
                main_dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_dialog.dismiss();
            }
        });


        main_dialog.setCancelable(false);
        main_dialog.setCanceledOnTouchOutside(false);
        main_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        main_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        main_dialog.show();
        Window window = main_dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void pilihdarigalery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }

    }

    private void gambardarikamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, TAKE_PICTURE);
        }
    }

    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }

        return imageFile;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    gambar.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                gambar.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gambardarikamera();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private boolean cekdata() {
        if (gambar.equals("")) {
            Toast.makeText(this, "Silahkan Pilih Gambar Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void simpanKonfirmasi() {
        File imageFile = persistImage(bitmap, "pembayaran");
        AndroidNetworking.upload(ApiServices.URL + ApiServices.PEMBAYARAN)
                .addMultipartFile("bukti_bayar", imageFile)
                .addMultipartParameter("id_santri", id)
                .addMultipartParameter("nama_bank", sbankpengirim)
                .addMultipartParameter("no_bank", snomorrekening)
                .addMultipartParameter("kode_pembayarans", skodepembayaran)
                .addMultipartParameter("jumlah_pembayaran", sjumlahpembayaran)
                .addMultipartParameter("rekening_pesantren", srekening_pesantren)
                .addMultipartParameter("nama_pengirim", snamapemilik)

                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "onResponse: " + response);
//                        Toast.makeText(PendaftarActivity.this, "" + response, Toast.LENGTH_SHORT).show();

                        Toast.makeText(UploadActivity.this, "Pembayaran Berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UploadActivity.this, BerandaActivity.class);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(UploadActivity.this, "Gagal " , Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        // handle error
                    }
                });
    }

}