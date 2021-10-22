package com.abdurrozaq.sistem_informasi.ui.home.adapters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abdurrozaq.sistem_informasi.Beranda.BerandaActivity;
import com.abdurrozaq.sistem_informasi.Fragments.Profile;
import com.abdurrozaq.sistem_informasi.R;
import com.abdurrozaq.sistem_informasi.berita.BeritaFragment;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.github.ybq.android.spinkit.style.FoldingCube;

import static com.abdurrozaq.sistem_informasi.ui.login.LoginActivity.setStatusBarGradiant;

public class BeritaActivity extends AppCompatActivity {

    private BubbleNavigationLinearView bubbleNavigationLinearView;
    private FragmentTransaction fragmentTransaction;
    private SharedPreferences sharedPreferences;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita);
        setStatusBarGradiant(BeritaActivity.this);





        cekid();
        bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);
        bubbleNavigationLinearView.setBadgeValue(0, "30");
        bubbleNavigationLinearView.setBadgeValue(1, null); //invisible badge
//        bubbleNavigationLinearView.setBadgeValue(2, "7");
//        bubbleNavigationLinearView.setBadgeValue(3, "2");
//        bubbleNavigationLinearView.setBadgeValue(4, "");
        id = getIntent().getStringExtra("id");
        if (id == null){
            bubbleNavigationLinearView.setVisibility(View.VISIBLE);
        }else{
            bubbleNavigationLinearView.setVisibility(View.INVISIBLE);
        }

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new BeritaFragment());
        fragmentTransaction.commit();
        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                switch (position) {
                    case 0:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new BeritaFragment());
                        fragmentTransaction.commit();
                        break;

                    case 1:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        sharedPreferences = getSharedPreferences("akun", MODE_PRIVATE);
                        String id = sharedPreferences.getString("id", "");
                        Toast.makeText(BeritaActivity.this, id, Toast.LENGTH_SHORT).show();
                        if (id.equals("")) {
                            fragmentTransaction.replace(R.id.fragment_container, new Profile());
                        } else {
                            Intent intent = new Intent(getApplicationContext(), BerandaActivity.class);
                            startActivity(intent);
                            finish();
//
                        }

                        fragmentTransaction.commit();
                        break;

//                    case 2:
//                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.fragment_container, new Informasi());
//                        fragmentTransaction.commit();
//                        break;
//                    case 3:
//                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.fragment_container, new Berita());
//                        fragmentTransaction.commit();
//                        break;

                }


            }
        });
    }

    private void cekid() {


    }
}