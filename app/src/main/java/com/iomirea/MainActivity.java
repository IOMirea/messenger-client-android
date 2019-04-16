package com.iomirea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        if (preferences.getString("token", "") != "") {
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);
            finish();
        }

        preferences.edit().putString("token", "MA==.ekU5.CZ7eHwhU97J0uKeDMOuaQQdeaUM").commit();
        WebView logauth = findViewById(R.id.logauth);
        logauth.loadUrl("https://iomirea.ml/login");
        ImageButton temp = findViewById(R.id.TokenTest);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
