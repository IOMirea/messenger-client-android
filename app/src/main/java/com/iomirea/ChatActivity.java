package com.iomirea;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
//
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_secure) {
            // Создание секретноого чата
        } else if (id == R.id.nav_group) {
            // Создание беседы
        } else if (id == R.id.nav_tools) {
            // Открываем настройки
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bug) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.bug_report, null);

            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            final PopupWindow bug_report = new PopupWindow(popupView, width, height, true);

            bug_report.showAtLocation(findViewById(R.id.avatar), Gravity.CENTER, 0, 0);

            Button bug_close = popupView.findViewById(R.id.bug_close);
            Button bug_send = popupView.findViewById(R.id.bug_send);

            final CheckBox bug_checkbox = popupView.findViewById(R.id.bug_checkbox);
            final EditText bug_body = popupView.findViewById(R.id.bug_text);

            // TODO: ограничить минимальную длину сообщения пользователя

            bug_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bug_report.dismiss();
                }
            });

            bug_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context context = getApplicationContext();

                    RequestQueue queue = Volley.newRequestQueue(context);
                    StringRequest reportRequest = new StringRequest(Request.Method.POST,
                            "https://iomirea.ml/api/v0/bugreports",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(
                                            context,
                                            getResources().getString(
                                                    R.string.bugreport_delivery_success
                                            ),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    bug_report.dismiss();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse == null) {
                                error.printStackTrace();
                            } else {
                                Toast.makeText(context, getResources().getString(
                                        R.string.bugreport_delivery_fail,
                                        error.networkResponse.statusCode
                                        ), Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded; charset=UTF-8";
                        }

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("body", bug_body.getText().toString());
                            params.put("device_info", bug_checkbox.isChecked() ? getDeviceInfo() : "");
                            params.put("automatic", "0");

                            return params;
                        }
                    };

                    queue.add(reportRequest);
                }
            });

            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    bug_report.dismiss();
                    return true;
                }
            });
        } else if (id == R.id.nav_info) {
            // Окошко с полезной информацией
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String getDeviceInfo() {
        String version = "unfound";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "app version: " + version +
                "\n OS version: " + android.os.Build.VERSION.RELEASE +
                "\n API version: " + android.os.Build.VERSION.SDK_INT +
                "\n Device name: " + android.os.Build.DEVICE +
                "\n Model: " + android.os.Build.MODEL +
                "\n Product name: " + android.os.Build.PRODUCT;
    }
}
