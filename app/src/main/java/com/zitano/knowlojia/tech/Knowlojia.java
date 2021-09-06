package com.zitano.knowlojia.tech;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.preference.PreferenceManager;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class Knowlojia extends MultiDexApplication {
    public static final String TAG = Knowlojia.class
            .getSimpleName();

    //private RequestQueue mRequestQueue;

    private static Knowlojia mInstance;
    private static Context context;

    @Override
    public void onCreate() {

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mInstance = this;
        Knowlojia.context = getApplicationContext();
        //AudienceNetworkAds.initialize(this);
        //AdSettings.addTestDevice("53571afe-9459-4e6f-ab34-43d6e6e274d5");
        FirebaseDatabase.getInstance();
        /*MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });*/
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String themePref = sharedPreferences.getString("themePref", ThemeHelper.DEFAULT_MODE);
        ThemeHelper.applyTheme(themePref);


        super.onCreate();
    }
    public  static Context getAppContext() {
        return Knowlojia.context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}

