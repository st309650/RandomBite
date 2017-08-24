package com.sutr.practice;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class Slash_screen extends AppCompatActivity {

    //ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash_screen);
       // imageView = (ImageView) findViewById(R.id.imageView1);

        Thread myThread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(800); //.95s
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        Bundle bndlanimation =
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.ani1,R.anim.ani2).toBundle();
                        startActivity(intent, bndlanimation);
                    }else{
                        startActivity(intent);
                    }
                   // overridePendingTransition(R.animator.slide_in, R.animator.slide_out);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        myThread.start();
    }
}
