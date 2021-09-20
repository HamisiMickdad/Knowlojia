package com.zitano.knowlojia.tech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.BuildConfig;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    // The number of native ads to load and display.
    public static final int NUMBER_OF_ADS = 5;

    // The AdLoader used to load ads.
    private AdLoader adLoader;

    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();


    RecyclerView mRecyclerView;
    AdapterMain AdapterMain;
    CardView card_view;
    Context ctx;
    TextView modelTitleTv, modelDescrTv;
    private AdView mAdView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FirebaseMessaging.getInstance().subscribeToTopic("recipes");


        FirebaseMessaging.getInstance().subscribeToTopic("zitano");
        //recyclerview
        mRecyclerView = findViewById(R.id.recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        card_view = findViewById(R.id.card_view);
        modelTitleTv = findViewById(R.id.modelTitleTv);
        modelDescrTv = findViewById(R.id.modelDescrTv);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        AdapterMain = new AdapterMain(this, getPlayers());
        mRecyclerView.setAdapter(AdapterMain);
        //mRecyclerView.setNestedScrollingEnabled(true);
        modelDescrTv.setText("Mobile App Tips");
        modelTitleTv.setText("Mobile App Tips");
        setSupportActionBar(toolbar);
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this, AndroidTricks.class);
               startActivity(intent);
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    //add models to arraylist
    private ArrayList<Model_Main> getPlayers(){
        ArrayList<Model_Main> models = new ArrayList<>();

        Model_Main P = new Model_Main();
       /* P.setTitle("Android Tricks");
        P.setDescription("Android Tricks");
        P.setImg(R.drawable.android_tricks);
        models.add(P);*/

        /*P = new Model_Main();*/
        P.setTitle("Kenyan DIYs");
        P.setDescription("Kenyan DIYs");
        P.setImg(R.drawable.kenyan_diys);
        models.add(P);

        P = new Model_Main();
        P.setTitle("Windows Tips");
        P.setDescription("Windows Tricks");
        P.setImg(R.drawable.windows_tricks);
        models.add(P);

        P = new Model_Main();
        P.setTitle("Tech Buying Guide");
        P.setDescription("Tech Buying Guide");
        P.setImg(R.drawable.tech_buying_guide);
        models.add(P);

        P = new Model_Main();
        P.setTitle("Photography Tips");
        P.setDescription("Photography Tips");
        P.setImg(R.drawable.photography);
        models.add(P);

        return models;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //showInterstitials();
            finish();
        } else if (id == R.id.about) {
            showDialogAbout();
        } else if (id == R.id.terms_of_use) {
            startActivity(new Intent(MainActivity.this, Terms_of_Use.class));
        }
        else if (id == R.id.privacy) {
            startActivity(new Intent(MainActivity.this, Privacy_Policy.class));
        } else if (id == R.id.settings) {
            startActivity(new Intent(MainActivity.this, Settings_Activity.class));
          /*  Fragment mFragment = null;
            mFragment = new SettingsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, mFragment)
                    .addToBackStack(null)
                    .commit();*/

            return true;
        } else if (id == R.id.rate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.menu_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey check out Knowlojia tech blog app, that offers free DIYs(Do it Yourself) tutorials for free. Whether you are fascinated by technology or not, you will still learn a thing or two. Install the app from this link here https://play.google.com/store/apps/details?id=" + getPackageName();
            //sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Best Football Predictions App on Play Store");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(sharingIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialogAbout() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.about);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.tv_version)).setText("Version " + BuildConfig.VERSION_NAME);
        ((View) dialog.findViewById(R.id.bt_getcode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}

