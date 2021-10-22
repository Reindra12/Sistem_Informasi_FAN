package com.abdurrozaq.sistem_informasi.pendaftar;

import static android.view.View.GONE;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.alamat.AlamatAdapter;
import com.abdurrozaq.sistem_informasi.alamat.ProvinsiItem;
import com.abdurrozaq.sistem_informasi.apiServices.ApiServices;
import com.abdurrozaq.sistem_informasi.pembayaran.PembayaranActivity;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class PendaftarActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "YOLO";

    Spinner _sjk, _stinggal, _spendidikan, _spernah;
    String gameState, idfoto, item1, item2, item3, item4, datajk, datatinggal, datapendidikan, datapernah, snama, stempat, stanggal, spondok, sanak, ssaudara, snamawali, sttlwali, spekerjaan, srt, sdesa, skec, skab, sprov, sno, stringjk, stringwali, stringpendidikan, stringmondok, dataprov;
    private String provinsi, idalamat, dataprovensi, key;
    Button daftar;

    private RoundedImageView foto, kk, ijazah, ktp;
    EditText namasantri, tempatlahir, etprovinsi, tanggallahir, namapondok, anak, saudara, namawali, ttlwali, pekerjaan, rt, desa, kec, kab, no;
    private final int PICK_IMAGE = 12345;
    private final int TAKE_PICTURE = 6352;
    private static final int REQUEST_CAMERA_ACCESS_PERMISSION = 5674;
    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4;
    ProgressBar progressBar;
    RelativeLayout rlpondok;
    RecyclerView recyclerView;
    ArrayList<ProvinsiItem> arrayList = new ArrayList<>();
    AlamatAdapter alamatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftar);
        if (savedInstanceState != null) {
            dataprovensi = savedInstanceState.getString("dataprovensi", "");
            Toast.makeText(this, "" + dataprovensi, Toast.LENGTH_SHORT).show();
//            etprovinsi.setText(name);
            Log.e("NAME", "saved");
        } else {
//            name = savedInstanceState.getString(ENAME, "");
            Toast.makeText(this, "" + dataprovensi, Toast.LENGTH_SHORT).show();
            Log.e("NAME", "not_saved");
        }


        etprovinsi = findViewById(R.id.et_provinsi);
//        etprovinsi.setText(name);

        provinsi = getIntent().getStringExtra("provinsi");
        idalamat = getIntent().getStringExtra("idalamat");
        dataprovensi = getIntent().getStringExtra("dataprovensi");

//        Toast.makeText(this, "" + provinsi, Toast.LENGTH_SHORT).show();

        progressBar = findViewById(R.id.spin_kit);
        FoldingCube doubleBounce = new FoldingCube();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(GONE);

        namasantri = findViewById(R.id.et_namasantri);
        tempatlahir = findViewById(R.id.et_tempatlahir);
        tanggallahir = findViewById(R.id.et_tanggallahir);
        namapondok = findViewById(R.id.et_namapondok);
        anak = findViewById(R.id.et_anak);
        saudara = findViewById(R.id.et_saudara);
        namawali = findViewById(R.id.et_namawali);
        ttlwali = findViewById(R.id.et_ttl);
        pekerjaan = findViewById(R.id.et_pekerjaan);
        rt = findViewById(R.id.et_rtrw);
        desa = findViewById(R.id.et_desa);
        kec = findViewById(R.id.et_kecamatan);
        kab = findViewById(R.id.et_kabupaten);
        no = findViewById(R.id.et_nohpwali);

        rlpondok = findViewById(R.id.relativepondok);

        _sjk = findViewById(R.id.sp_jeniskelamin);
        _stinggal = findViewById(R.id.sptinggal);
        _spendidikan = findViewById(R.id.sppendidikan);
        _spernah = findViewById(R.id.spmondok);


        foto = findViewById(R.id.imgcalonsantri);
        ijazah = findViewById(R.id.add_2);
        kk = findViewById(R.id.add_3);
        ktp = findViewById(R.id.add_4);


        desa.setOnClickListener(this);
        etprovinsi.setOnClickListener(this);
        kec.setOnClickListener(this);
        kab.setOnClickListener(this);
        kk.setOnClickListener(this);
        foto.setOnClickListener(this);
        ktp.setOnClickListener(this);
        ijazah.setOnClickListener(this);


        validasidataalamat();

        validasipondok();

        daftar = findViewById(R.id.btndaftarsantri);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cekkelangkapandata()) {
                    progressBar.setVisibility(View.VISIBLE);
                    proses();
                }

            }
        });

        datajk = _sjk.getSelectedItem().toString();
        item1 = datajk;
        _sjk.setSelection(0);

        datatinggal = _stinggal.getSelectedItem().toString();
        item2 = datatinggal;
        _stinggal.setSelection(0);

        datapendidikan = _spendidikan.getSelectedItem().toString();
        item3 = datapendidikan;
        _spendidikan.setSelection(0);

        datapernah = _spernah.getSelectedItem().toString();
        item4 = datapernah;
        _spernah.setSelection(0);

        _spernah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                validasipondok();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private boolean validasidataalamat() {
        if (dataprovensi == null) {
            Toast.makeText(this, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dataprovensi.equals("provinsi")) {
            Toast.makeText(this, "data provinsi" + dataprovensi, Toast.LENGTH_SHORT).show();
            etprovinsi.setText(provinsi);
            return false;
        } else if (dataprovensi.equals("kota_kabupaten")) {
            Toast.makeText(this, "data kabupaten" + dataprovensi, Toast.LENGTH_SHORT).show();
            kab.setText(provinsi);
            return false;
        } else if (dataprovensi.equals("kecamatan")) {
            Toast.makeText(this, "data Kecamatan" + dataprovensi, Toast.LENGTH_SHORT).show();
            kec.setText(provinsi);
            return false;
        } else if (dataprovensi.equals("kelurahan")) {
            Toast.makeText(this, "data desa" + dataprovensi, Toast.LENGTH_SHORT).show();
            desa.setText(provinsi);
            return false;
        }
        return true;
    }


    private void tampilkameradialog() {

        final Dialog main_dialog;

        main_dialog = new Dialog(PendaftarActivity.this);
        main_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        main_dialog.setContentView(R.layout.camera_dialog);

        final Button camera = main_dialog.findViewById(R.id.camera);
        final Button folder = main_dialog.findViewById(R.id.folder);
        final ImageView close = main_dialog.findViewById(R.id.close);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && ActivityCompat.checkSelfPermission(PendaftarActivity.this, Manifest.permission.CAMERA)
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
                pilihgalery();
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

    private void pilihgalery() {
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
        if (name == null) {
            Toast.makeText(PendaftarActivity.this, "Silahkan Pilih Foto Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        }
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
//                    bitmap = BitmapFactory.decodeStream(inputStream);

                    if (idfoto == "1") {
                        bitmap1 = BitmapFactory.decodeStream(inputStream);
                        foto.setImageBitmap(bitmap1);
                    } else if (idfoto == "2") {
                        bitmap2 = BitmapFactory.decodeStream(inputStream);
                        ijazah.setImageBitmap(bitmap2);
                    } else if (idfoto == "3") {
                        bitmap3 = BitmapFactory.decodeStream(inputStream);
                        kk.setImageBitmap(bitmap3);
                    } else if (idfoto == "4") {
                        bitmap4 = BitmapFactory.decodeStream(inputStream);
                        ktp.setImageBitmap(bitmap4);
                    } else {
                        Toast.makeText(this, "Pilih Foto", Toast.LENGTH_SHORT).show();
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                if (idfoto == "1") {
                    bitmap1 = (Bitmap) extras.get("data");
                    foto.setImageBitmap(bitmap1);
                } else if (idfoto == "2") {
                    bitmap2 = (Bitmap) extras.get("data");
                    ijazah.setImageBitmap(bitmap2);
                } else if (idfoto == "3") {
                    bitmap3 = (Bitmap) extras.get("data");
                    kk.setImageBitmap(bitmap3);
                } else if (idfoto == "4") {
                    bitmap4 = (Bitmap) extras.get("data");
                    ktp.setImageBitmap(bitmap4);
                }
//                bitmap = (Bitmap) extras.get("data");

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


    private boolean cekkelangkapandata() {
        if (namasantri.getText().toString().trim().isEmpty()) {
            namasantri.setError("Lengkapi Data Diri Anda");
            namasantri.requestFocus();
            return false;
        } else if (tempatlahir.getText().toString().trim().isEmpty()) {
            tempatlahir.setError("Lengkapi Data Diri Anda");
            tempatlahir.requestFocus();
            return false;
        } else if (tanggallahir.getText().toString().trim().isEmpty()) {
            tanggallahir.setError("Lengkapi Data Diri Anda");
            tanggallahir.requestFocus();
            return false;
        } else if (etprovinsi.getText().toString().trim().isEmpty()) {
            etprovinsi.setError("Masukan Nama Lahir");
            etprovinsi.requestFocus();
            return false;
        } else if (anak.getText().toString().trim().isEmpty()) {
            anak.setError("Lengkapi Data Diri Anda");
            anak.requestFocus();
            return false;
        } else if (saudara.getText().toString().trim().isEmpty()) {
            saudara.setError("Lengkapi Data Diri Anda");
            saudara.requestFocus();
            return false;
        } else if (namawali.getText().toString().trim().isEmpty()) {
            namawali.setError("Lengkapi Data Diri Anda");
            namawali.requestFocus();
            return false;
        } else if (ttlwali.getText().toString().trim().isEmpty()) {
            ttlwali.setError("Lengkapi Data Diri Anda");
            ttlwali.requestFocus();
            return false;
        } else if (pekerjaan.getText().toString().trim().isEmpty()) {
            pekerjaan.setError("Lengkapi Data Diri Anda");
            pekerjaan.requestFocus();
            return false;
        } else if (rt.getText().toString().trim().isEmpty()) {
            rt.setError("Lengkapi Data Diri Anda");
            rt.requestFocus();
            return false;
        } else if (desa.getText().toString().trim().isEmpty()) {
            desa.setError("Lengkapi Data Diri Anda");
            desa.requestFocus();
            return false;
        } else if (kec.getText().toString().trim().isEmpty()) {
            kec.setError("Masukan Tanggal Lahir");
            kec.requestFocus();
            return false;
        } else if (kab.getText().toString().trim().isEmpty()) {
            kab.setError("Lengkapi Data Diri Anda");
            kab.requestFocus();
            return false;
        } else if (no.getText().toString().trim().isEmpty()) {
            no.setError("Lengkapi Data Diri Anda");
            no.requestFocus();
            return false;
        } else if (_sjk.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Lengkapi Data Diri Anda", Toast.LENGTH_SHORT).show();
            _sjk.requestFocus();
            return false;
        } else if (_spernah.getSelectedItemPosition() == 0) {

            Toast.makeText(this, "lengkapi Data Diri Anda", Toast.LENGTH_SHORT).show();
            _spernah.requestFocus();

            return false;
        } else if (_spendidikan.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "lengkapi Data Diri Anda", Toast.LENGTH_SHORT).show();
            _spendidikan.requestFocus();
            return false;
        } else if (_stinggal.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "lengkapi Data Diri Anda", Toast.LENGTH_SHORT).show();
            _stinggal.requestFocus();
            return false;
        }

        snama = namasantri.getText().toString();
        stempat = tempatlahir.getText().toString();
        stanggal = tanggallahir.getText().toString();
        stanggal = tanggallahir.getText().toString();
//        spondok = namapondok.getText().toString();
        sanak = anak.getText().toString();
        ssaudara = saudara.getText().toString();
        snamawali = namawali.getText().toString();
        sttlwali = ttlwali.getText().toString();
        spekerjaan = pekerjaan.getText().toString();
        srt = rt.getText().toString();
        sdesa = desa.getText().toString();
        skec = kec.getText().toString();

//       kab.setText(provinsi);
        skab = kab.getText().toString();

        sno = no.getText().toString();


        stringjk = _sjk.getSelectedItem().toString();
        stringpendidikan = _spendidikan.getSelectedItem().toString();
        stringmondok = _spernah.getSelectedItem().toString();
        stringwali = _stinggal.getSelectedItem().toString();

        return true;

    }

    public void validasipondok() {
        String data;
        data = _spernah.getSelectedItem().toString();
        if (data.equals("YA")) {
            spondok = namapondok.getText().toString().trim();
            rlpondok.setVisibility(View.VISIBLE);
        } else if (data.equals("TIDAK")) {
            spondok = "Tidak Pernah Mondok";
            rlpondok.setVisibility(GONE);
        }

    }


    private void proses() {
        File imageFile = persistImage(bitmap1, "santri");
        File imageFile2 = persistImage(bitmap2, "ijazah");
        File imageFile3 = persistImage(bitmap3, "kk");
        File imageFile4 = persistImage(bitmap4, "ktp");
        AndroidNetworking.upload(ApiServices.URL + ApiServices.DAFTAR)
                .addMultipartFile("foto", imageFile)
                .addMultipartParameter("nama", snama)
                .addMultipartParameter("tempat_lahir", stempat)
                .addMultipartParameter("tanggal_lahir", stanggal)
                .addMultipartParameter("jk", stringjk)
                .addMultipartParameter("tinggalbersama", stringwali)
                .addMultipartParameter("pendidikanterakhir", stringpendidikan)
                .addMultipartParameter("pernahmondok", stringmondok)
                .addMultipartParameter("namapondok", spondok)
                .addMultipartParameter("anakkeberapa", sanak)
                .addMultipartParameter("berapasaudara", ssaudara)
                .addMultipartParameter("nama_wali", snamawali)
                .addMultipartParameter("pekerjaan", spekerjaan)
                .addMultipartParameter("ttl", sttlwali)
                .addMultipartParameter("rtrw", srt)
                .addMultipartParameter("desa", sdesa)
                .addMultipartParameter("kecamatan", skec)
                .addMultipartParameter("kabupaten", skab)
                .addMultipartParameter("no", sno)
                .addMultipartFile("ijazah", imageFile2)
                .addMultipartFile("kk", imageFile3)
                .addMultipartFile("ktp", imageFile4)
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

                        Log.d(TAG, "onResponse: " + response);
//                        Toast.makeText(PendaftarActivity.this, "" + response, Toast.LENGTH_SHORT).show();

                        JSONObject dataresponse = response;
                        try {
                            JSONObject datasantri = dataresponse.getJSONObject("data");

                            if (datasantri != null) {

                                progressBar.setVisibility(GONE);
                                Toast.makeText(PendaftarActivity.this, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PendaftarActivity.this, PembayaranActivity.class);
                                intent.putExtra("id_santri", datasantri.getString("id_santri"));
                                startActivity(intent);

                            } else {
                                progressBar.setVisibility(GONE);
                                Toast.makeText(PendaftarActivity.this, "Lengkapi data", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(GONE);
                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        progressBar.setVisibility(GONE);
                        Log.d(TAG, "onError: " + error);
                        Toast.makeText(PendaftarActivity.this, "Periksa Data Anda", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(PendaftarActivity.this, ""+error.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgcalonsantri:
                tampilkameradialog();
                idfoto = "1";
                break;

            case R.id.add_2:
                tampilkameradialog();
                idfoto = "2";
                break;

            case R.id.add_3:
                tampilkameradialog();
                idfoto = "3";
                break;

            case R.id.add_4:
                tampilkameradialog();
                idfoto = "4";
                break;
            case R.id.et_provinsi:
                dataprov = "provinsi";
                key = "provinsi";
                opendialog(dataprov, key);
                arrayList.clear();

                break;
            case R.id.et_kabupaten:
                dataprov = "kota?id_provinsi=" + idalamat;
                key = "kota_kabupaten";
                Toast.makeText(this, ""+idalamat, Toast.LENGTH_SHORT).show();
                opendialog(dataprov, key);

                arrayList.clear();
                alamatAdapter.notifyDataSetChanged();

                break;
            case R.id.et_kecamatan:
                dataprov = "kecamatan?id_kota=" + idalamat;
                key = "kecamatan";
                Toast.makeText(this, ""+idalamat, Toast.LENGTH_SHORT).show();
                opendialog(dataprov, key);

                arrayList.clear();
                alamatAdapter.notifyDataSetChanged();
                break;

            case R.id.et_desa:
                dataprov = "kelurahan?id_kecamatan=" + idalamat;
                key = "kelurahan";
                Toast.makeText(this, ""+idalamat, Toast.LENGTH_SHORT).show();
                opendialog(dataprov, key);

                arrayList.clear();
                alamatAdapter.notifyDataSetChanged();

                break;
        }


    }

    private void opendialog(String dataprov, String key) {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet);

        LinearLayout editLayout = dialog.findViewById(R.id.layoutEdit);
        recyclerView = dialog.findViewById(R.id.recycler);
//        LinearLayout uploadLayout = dialog.findViewById(R.id.layoutUpload);
//        LinearLayout printLayout = dialog.findViewById(R.id.layoutPrint);

        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(PendaftarActivity.this, "Edit is Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
//                Toast.makeText(MainActivity.this,"Share is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


        AndroidNetworking.get(ApiServices.PROVINSI + dataprov)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONArray jsonArray = response.getJSONArray(PendaftarActivity.this.key);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                ProvinsiItem provinsiItem = new ProvinsiItem();

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                provinsiItem.setNama(jsonObject.getString("nama"));
                                provinsiItem.setId(jsonObject.getString("id"));
                                provinsiItem.setDataid(key);

                                progressBar.setVisibility(View.GONE);
                                arrayList.add(provinsiItem);


                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);

                                alamatAdapter = new AlamatAdapter(PendaftarActivity.this, arrayList, new AlamatAdapter.RecyclerItemClickListener() {
                                    @Override
                                    public void onClickListener(ProvinsiItem listltemCategory, int position, String nama, String id) {

                                        idalamat = id;
                                        Toast.makeText(PendaftarActivity.this, "" + nama, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        if (key.equals("provinsi")){
                                            etprovinsi.setText(nama);
                                        }else if(key.equals("kota_kabupaten")){
                                            kab.setText(nama);
                                        }else if (key.equals("kecamatan")){
                                            kec.setText(nama);
                                        }else{
                                            desa.setText(nama);
                                        }


                                    }


                                });

                                recyclerView.setAdapter(alamatAdapter);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onResponse: " + anError);
                        Toast.makeText(PendaftarActivity.this, "" + anError, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setAdapter() {

        AlamatAdapter alamatAdapter = new AlamatAdapter(PendaftarActivity.this, arrayList, new AlamatAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(ProvinsiItem listltemCategory, int position, String provinsi, String nama) {

                Toast.makeText(PendaftarActivity.this, "" + nama, Toast.LENGTH_SHORT).show();

                etprovinsi.setText(nama);

            }


        });

        recyclerView.setAdapter(alamatAdapter);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.e("NAME", "Saving");
        savedInstanceState.putString("dataprovensi", provinsi);
    }

//    private void prosesalamat() {
//        AndroidNetworking.get(ApiServices.PROVINSI + dataprov)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d(TAG, "onResponse: " + response);
//                        try {
//                            JSONArray jsonArray = response.getJSONArray(key);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//
//                                ProvinsiItem provinsiItem = new ProvinsiItem();
//
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                                provinsiItem.setNama(jsonObject.getString("nama"));
//                                provinsiItem.setId(jsonObject.getString("id"));
//                                provinsiItem.setDataid(key);
//
//                                progressBar.setVisibility(View.GONE);
//                                arrayList.add(provinsiItem);
//
//
//                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                                recyclerView.setLayoutManager(layoutManager);
//                                AlamatAdapter alamatAdapter = new AlamatAdapter(PendaftarActivity.this, arrayList);
//                                recyclerView.setAdapter(alamatAdapter);
//
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        Log.d(TAG, "onResponse: " + anError);
//                        Toast.makeText(PendaftarActivity.this, "" + anError, Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

}

