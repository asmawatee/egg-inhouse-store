package com.example.user.appstore.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.appstore.R;

public class EggInHouseActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egg_in_house);

        Handler handler =new Handler() ;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(EggInHouseActivity.this,LoginActivity.class));
                finish();
            }
        },2500);

    }
}
