package com.example.androiddev.tt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Request request = RequestUtil.getSearchRequest("Zholtkevych");
        Request request = RequestUtil.getAuthRequest();
        Volley.newRequestQueue(this).add(request);
        startActivity(new Intent(this, DesignActivity.class));
    }

    protected void getPin() {
        String url = "https://api.twitter.com/oauth/authenticate";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}
