package com.iomirea;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context ctx, Intent intent) {
            Log.e("logger name", "something");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.VIEW"));

        final SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        if (preferences.getString("token", "") != "") {
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);
            finish();
        }
        String logurl = "https://iomirea.ml/api/oauth2/authorize?response_type=code&client_id=1&redirect_uri=iomirea1://oauth2redirect&scope=user";
        WebView logauth = findViewById(R.id.logauth);
        logauth.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                if (url.startsWith("iomirea1")) { //checking the URL for scheme required
                    //and sending it within an explicit Intent
                    Intent myapp_intent = new Intent(Intent.ACTION_VIEW);
                    myapp_intent.setData(Uri.parse(url));
                    myapp_intent.putExtra("fullurl", url);
                    startActivity(myapp_intent);
                    finish();
                    return true; //this might be unnecessary because another Activity
                    //start had already been called

                }
                view.loadUrl(url); //handling non-customschemed redirects inside the WebView
                return false; // then it is not handled by default action
            }});
        WebSettings webSettings = logauth.getSettings();
        webSettings.setJavaScriptEnabled(true);
        logauth.loadUrl(logurl);


        ImageButton temp = findViewById(R.id.TokenTest);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putString("token", "crabs").commit();
                String check = preferences.getString("token", "");
                if (check != "") {
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(
                            getApplicationContext(),
                                    check,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }
}
