package com.iomirea;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iomirea.http.VolleyController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


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
        if (preferences.getString("token", "null") != "null") {
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

                    getToken(preferences, Uri.parse(url));
//                    Intent myapp_intent = new Intent(getApplicationContext(), ChatActivity.class);
//                    myapp_intent.setData(Uri.parse(url));
//                    myapp_intent.putExtra("fullurl", url);
//                    startActivity(myapp_intent);
//                    finish();
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
                preferences.edit().putString("token", "crabs").apply();
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
    void getToken(final SharedPreferences preferences, final Uri uri){

        final Context context = getApplicationContext();
        StringRequest tokenRequest = new StringRequest(Request.Method.POST,
                "https://iomirea.ml/api/oauth2/token",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject token = new JSONObject(response);

                            preferences.edit().putString("token", token.getString("access_token")).apply();
                        } catch (org.json.JSONException e) {
                            Toast.makeText(context, getResources().getString(R.string.net_request_failed_with_message, "Access token missing from response"), Toast.LENGTH_SHORT
                            ).show();
                        }
                    Intent myapp_intent = new Intent(getApplicationContext(), ChatActivity.class);
                    startActivity(myapp_intent);
                    finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorText;

                if (error.networkResponse == null) {
                    error.printStackTrace();
                    errorText = getResources().getString(R.string.net_request_failed_with_message, error.toString());
                } else {
                    errorText = getResources().getString(R.string.net_request_failed_with_code, error.networkResponse.statusCode);
                }

                Toast.makeText(context, errorText, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF -8";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("code", uri.getQueryParameter("code"));
                params.put("client_id", "1");
                params.put("redirect_uri", "iomirea1://oauth2redirect");
                params.put("scope", "user");
                params.put("grant_type", "authorization_code");
                params.put("client_secret", "");
                return params;
            }
        };

        VolleyController.getInstance(getApplicationContext()).addToRequestQueue(tokenRequest);
    }
}
