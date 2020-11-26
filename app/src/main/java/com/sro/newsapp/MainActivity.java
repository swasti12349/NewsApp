package com.sro.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    TextView emailnav,user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.nav);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        statusBar.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.my_statusbar_color));
        }
        View v = navigationView.getHeaderView(0);
        TextView receive1 = (TextView) v.findViewById(R.id.emailnav);
        TextView receive2 = (TextView) v.findViewById(R.id.user);

        receive1.setText(getIntent().getStringExtra("emailid"));
        receive2.setText(getIntent().getStringExtra("name"));


        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new home()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new home()).commit();
                        break;

                    case R.id.top:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new top()).commit();

                        break;
                    case R.id.technology:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new settings()).commit();
                        break;

                    case R.id.entertainment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new entertainment()).commit();
                        break;
                    case R.id.business:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new business()).commit();
                        break;

                    case R.id.dev:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new dev()).commit();
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

}