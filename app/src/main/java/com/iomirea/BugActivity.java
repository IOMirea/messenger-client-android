package com.iomirea;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iomirea.http.VolleyController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BugActivity extends AppCompatActivity {
    EditText bugEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppThemeNight);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bug);
        bugEditText = findViewById(R.id.bug_text);
        final CheckBox bug_checkbox = findViewById(R.id.bug_checkbox);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageButton sendButton = findViewById(R.id.bug_send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = getApplicationContext();

                String bugBody = bugEditText.getText().toString().trim();

                if (bugBody.length() < 5) {
                    Toast.makeText(
                            context,
                            getResources().getString(
                                    R.string.bugreport_mintext
                            ),
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                Map<String, String> data = new HashMap<>();

                data.put("body", bugBody);
                data.put("device_info", bug_checkbox.isChecked() ? getDeviceInfo() : "");
                data.put("automatic", "0");

                JsonObjectRequest reportRequest = new JsonObjectRequest(Request.Method.POST,
                        "https://iomirea.ml/api/bugreports", new JSONObject(data),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(
                                        context,
                                        getResources().getString(
                                                R.string.bugreport_delivery_success
                                        ),
                                        Toast.LENGTH_SHORT
                                ).show();
                                doubleBackToExitPressedOnce = true;
                                onBackPressed();
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
                });

                VolleyController.getInstance(getApplicationContext()).addToRequestQueue(reportRequest);
            }
        });

    }

    private String getDeviceInfo() {
        String version = "Unknown";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "App version: " + version +
                "\n OS version: " + android.os.Build.VERSION.RELEASE +
                "\n API version: " + android.os.Build.VERSION.SDK_INT +
                "\n Device name: " + android.os.Build.DEVICE +
                "\n Model: " + android.os.Build.MODEL +
                "\n Product name: " + android.os.Build.PRODUCT;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String stateSaved = savedInstanceState.getString("bug_state");

        if (stateSaved == null) {
            Toast.makeText(BugActivity.this,
                    "onRestoreInstanceState: \nNO state saved!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(BugActivity.this,
                    "onRestoreInstanceState: \nstate saved!",
                    Toast.LENGTH_LONG).show();
        }
        bugEditText.setText(stateSaved);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String stateToSave = bugEditText.getText().toString();
        outState.putString("bug_state", stateToSave);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
