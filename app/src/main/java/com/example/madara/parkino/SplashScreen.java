package com.example.madara.parkino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.madara.parkino.utils.Session;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final boolean i =  Session.getInstance().isUserLoggedIn();
        final Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    if(i){
                        Intent intent = new Intent(SplashScreen.this,HomeScreen.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(SplashScreen.this,LoginScreen.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
