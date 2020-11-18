package com.mahtiz.controlarduino;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class acivityPrinc extends AppCompatActivity {
ImageView ard;
Animation animate;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acivity_princ);
        ard=findViewById(R.id.imageView);
       animate= AnimationUtils.loadAnimation(getApplicationContext(),R.animator.animat);
       ard.startAnimation(animate);
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5000);
                    startActivity(new Intent(getApplicationContext(),ActivityMenu.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
