package com.iomirea;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iomirea.http.VolleyController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        // TODO: move this to intent handler
        if (!preferences.getString("token", "").equals("1")) {
            Uri data = getIntent().getData();
            getToken(preferences, data);
        }
//        registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.VIEW"));
        //временный счетчик, позже удалить
        int counter = 1;
        final RecyclerView chatlist = findViewById(R.id.mainlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        chatlist.setLayoutManager(linearLayoutManager);

        final ChatAdapter chatAdapter = new ChatAdapter(counter, getApplicationContext());
        chatlist.setAdapter(chatAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_secure) {
            // Создание секретноого чата
            SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
            String scheme = preferences.getString("token", "");
            Toast.makeText(
                    getApplicationContext(),
                    scheme,
                    Toast.LENGTH_SHORT
            ).show();
        } else if (id == R.id.nav_group) {
            // Создание беседы
        } else if (id == R.id.nav_tools) {
            // Открываем настройки
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bug) {
            getIntent();
            Intent bug_intent = new Intent(getApplicationContext(),BugActivity.class);
            bug_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(bug_intent);
        } else if (id == R.id.nav_info) {
            // Окошко с полезной информацией
        } else if (id == R.id.nav_out) {
            SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
            preferences.edit().remove("token").apply();
            Intent logout_intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(logout_intent);
            finish();
        } else if (id == R.id.nav_log_version) {
            getIntent();
            Intent log_intent = new Intent(getApplicationContext(), LogActivity.class);
            startActivity(log_intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void getToken(final SharedPreferences preferences, final Uri uri){
        // TODO: move this to a separate activity

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
