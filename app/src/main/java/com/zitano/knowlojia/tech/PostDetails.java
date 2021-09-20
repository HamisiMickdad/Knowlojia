package com.zitano.knowlojia.tech;

import static androidx.webkit.WebSettingsCompat.FORCE_DARK_OFF;
import static androidx.webkit.WebSettingsCompat.FORCE_DARK_ON;
import static com.zitano.knowlojia.tech.Knowlojia.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;

public class PostDetails extends AppCompatActivity {
    MaterialTextView mTitleTv;
    AppCompatImageView mImageIv;
    Bitmap bitmap;

    Button mSaveBtn, mShareBtn, mWallBtn;

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    private AdView mAdView;
    private WebView webView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        //initialize views
        //mDetailTv = findViewById(R.id.descriptionTv);
        webView = findViewById(R.id.webView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitleTv = findViewById(R.id.titleTv);
        mImageIv = findViewById(R.id.imageView);
        mShareBtn = findViewById(R.id.shareBtn);
        // Get Data from intent
        String image = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("description");

        //action bar
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("Hair Style Details");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultFontSize(20);
        //webSettings.setTextZoom(webSettings.getTextZoom()-50); // where 90 is 90%; default value is ... 100
        //settings.setTextSize(WebSettings.TextSize.NORMAL);
        //webSettings.setTextSize(WebSettings.TextSize.LARGER);
        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    WebSettingsCompat.setForceDark(webView.getSettings(), FORCE_DARK_ON);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    WebSettingsCompat.setForceDark(webView.getSettings(), FORCE_DARK_OFF);
                    break;
            }
        }

        //set data to views
        mTitleTv.setText(title);
        actionBar.setTitle(title);

        Document document = Jsoup.parse(desc);
        document.select("img").attr("style", "display: inline-block;height: auto;max-width: 100%;");
        document.select("figure").attr("style", "width: 80%"); // find all figures and set with to 80%
        //document.select("font-size").attr("style", "xx-large;");
        document.select("iframe").attr("style", "width: 100%"); // find all iframes and set with to 100%
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        String s = document.html();

        //Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Microsoft_Sans_Serif_Regular_font.ttf");
        //String pish = "<html><head><style type=\"text/css\">@font-face {font-family: sans-serif;src: "+typeface+"}body {font-family: sans-serif;font-size: large;text-align: justify;}</style></head><body>";
        //String pas = "</body></html>";
        //content contains webpage like html, so load in webview
        //webView.loadDataWithBaseURL(null,  "<style>img{display: contents;height: auto;}</style>"+String.valueOf(s), "text/html", "UTF-8", null);
        //webView.loadDataWithBaseURL(null, "<style>img{display: inline-block;height: auto;max-width: 100%;}</style>" + s, "text/html", "UTF-8", null);
        String encodedHtml = Base64.encodeToString(s.getBytes(), Base64.NO_PADDING);
        //String encodedHtml = pish + s + pas;
        webView.loadData(encodedHtml, "text/html", "base64");

        //mDetailTv.setText(desc);



       /* PicassoImageGetter imageGetter = new PicassoImageGetter(mDetailTv);
        Spannable html;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            html = (Spannable) Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
        } else {
            html = (Spannable) Html.fromHtml(desc, imageGetter, null);
        }
        mDetailTv.setMovementMethod(LinkMovementMethod.getInstance());
        mDetailTv.setText(html);
*/
        /*Picasso
                .get()
                .load(image)
                //.resize(50, 50)
                .centerCrop()
                .fit()
                .into(mImageIv);*/
        //Glide.with(getApplicationContext()).load(image).into(mImageIv);
        Glide.with(getApplicationContext()).load(image)
                .placeholder(R.drawable.ic_loader)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .thumbnail(0.05f)
                .centerCrop()
                //.transition(DrawableTransitionOptions.withCrossFade())
                .into(mImageIv);

        //Share button click handle
        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shareImage();

                sharePost();

            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



    }

    private void sharePost() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String s = mTitleTv.getText().toString();
        String shareBody = "Hey learn more about " + s + " from the Knowlojia app. Get the app right here" + " https://play.google.com/store/apps/details?id=" + getPackageName();
        //sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Best Football Predictions App on Play Store");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(sharingIntent);

    }


    private void shareImage() {
        try {
            //get image from Image View as  bitmap
            bitmap = ((BitmapDrawable)mImageIv.getDrawable()).getBitmap();
            //get title and description and save to string s
            //String s = mTitleTv.getText().toString() + "\n" + mDetailTv.getText().toString();

            File file = new File(getExternalCacheDir(), "sample.jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            //intent to share image & text
           /* Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, s);//put the text
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/jpg");
            startActivity(Intent.createChooser(intent, "Share via"));*/
            Intent share = new Intent("android.intent.action.SEND");
            //share.putExtra(Intent.EXTRA_TEXT, s);//put the text
            //share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            share.setType("image/jpeg");
            Uri uri = FileProvider.getUriForFile(PostDetails.this, BuildConfig.APPLICATION_ID+ ".provider", (file));
            share.putExtra("android.intent.extra.STREAM", uri);

            startActivity(Intent.createChooser(share, "Share via"));


        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // Handle back press go back to the previous activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.about) {
            showDialogAbout();
        } else if (id == R.id.settings) {
            startActivity(new Intent(PostDetails.this, Settings_Activity.class));
           /* Fragment mFragment = null;
            mFragment = new SettingsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, mFragment).addToBackStack(null).commit();*/
        } else if (id == R.id.rate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(PostDetails.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PostDetails.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
