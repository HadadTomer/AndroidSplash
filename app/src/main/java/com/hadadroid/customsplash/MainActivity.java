package com.hadadroid.customsplash;

import android.app.Activity;
import android.os.Bundle;

import com.hadadroid.splash.SplashBuilder;
import com.hadadroid.splash.SplashBuilder.OnCompleteListener;
import com.hadadroid.splash.SplashBuilder.SplashTask;

public class MainActivity extends Activity {

    private SplashTask splashTask = new SplashTask() {
        @Override
        public void run(final OnCompleteListener listener) {

            // Write here your code that will be executed
            // while the splash screen is displayed.
            // Don't forget to call listener.onComplete()
            // when your task ends.

            listener.onComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // show splash screen
        SplashBuilder
                .with(this, savedInstanceState)
                .setSplashTask(splashTask)
                .setLayoutId(R.layout.activity_splash)
                .setMinimumTimeout(4000) // 4 seconds
                .setMaximumTimeout(7000) // 7 seconds
                .overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                .show();

        setContentView(R.layout.activity_main);
    }
}
