package com.iomirea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.util.Locale;


public class ChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences themePref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (themePref.getBoolean("DarkTheme", false)) {
            setTheme(R.style.AppThemeNight);
        }
        SharedPreferences lang = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Locale locale = new Locale(lang.getString("language", "en"));
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        // TODO: move this to intent handler
        if (preferences.getString("token", "null").equals("null")) {
            Intent logout_intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(logout_intent);
            finish();
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
    public void onResume(){
        SharedPreferences themePref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (themePref.getBoolean("DarkTheme", false)) {
            setTheme(R.style.AppThemeNight);
        }
        super.onResume();
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
            SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String scheme = preferences.getString("token", "");
            Toast.makeText(
                    getApplicationContext(),
                    scheme + preferences2.getString("language", "en"),
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
            this.recreate();
//            Intent logout_intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(logout_intent);
//            finish();
        } else if (id == R.id.nav_log_version) {
            getIntent();
            Intent log_intent = new Intent(getApplicationContext(), LogActivity.class);
            startActivity(log_intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
