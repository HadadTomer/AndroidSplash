package com.hadadroid.splash;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadadroid.splash.SplashBuilder.OnCompleteListener;
import com.hadadroid.splash.SplashBuilder.SplashTask;

public class SplashActivity extends Activity {

    public static final String LAYOUT_ID = "layout_id";
    public static final String MINIMUM_TIMEOUT = "minimumTimeout";
    public static final String MAXIMUM_TIMEOUT = "maximumTimeout";
    public static final String SPLASH_ARGUMENTS = "parameters";
    public static final String ENTER_ANIM = "enterAnim";
    public static final String EXIT_ANIM = "exitAnim";

    private static final long DEFAULT_MIN_TIMEOUT = 3 * 1000; // 3 seconds by default
    private static SplashTask splashTask = null;

    private boolean isFinished;

    private int layoutId;
    private int enterAnim, exitAnim;
    private long minimumTimeout, maximumTimeout;

    public static void setSplashTask(SplashTask splashTask) {
        SplashActivity.splashTask = splashTask;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initArgs();
        setLayout();

        final long startTime = SystemClock.elapsedRealtime();

        if (maximumTimeout > 0) {
            finishSplash(maximumTimeout);
        }

        splashTask.run(new OnCompleteListener() {
            @Override
            public void onComplete() {
                finishSplash(minimumTimeout - (SystemClock.elapsedRealtime() - startTime));
            }
        });
    }

    private void initArgs() {
        isFinished = false;

        Bundle args = getIntent().getBundleExtra(SPLASH_ARGUMENTS);
        layoutId = args.getInt(LAYOUT_ID);
        minimumTimeout = args.getLong(MINIMUM_TIMEOUT) != 0 ? args.getLong(MINIMUM_TIMEOUT) : DEFAULT_MIN_TIMEOUT;
        maximumTimeout = args.getLong(MAXIMUM_TIMEOUT);
        enterAnim = args.getInt(ENTER_ANIM);
        exitAnim = args.getInt(EXIT_ANIM);

        if (splashTask == null) {
            splashTask = new SplashTask() {
                @Override
                public void run(OnCompleteListener listener) {
                    // empty splash task
                    // just call onComplete()

                    listener.onComplete();
                }
            };
        }
    }

    private void setLayout() {
        if (layoutId == 0) {
            // no layout set -> use the default layout

            setContentView(R.layout.pre_defined_splash);
            TextView appName = (TextView) findViewById(R.id.splash_app_name);
            ImageView appIcon = (ImageView) findViewById(R.id.splash_app_icon);

            appName.setText(getAppName());
            appIcon.setImageDrawable(getAppIconDrawable());

        } else {
            setContentView(layoutId);
        }
    }

    private String getAppName() {
        Context context = getApplicationContext();
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    private Drawable getAppIconDrawable() {
        Context context = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(context.getApplicationInfo().icon, null);
        }
        return context.getResources().getDrawable(context.getApplicationInfo().icon);
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    private void finishSplash(long delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishSplash();
            }
        }, delay);
    }

    private void finishSplash() {
        if (!isFinished) {
            synchronized (this) {
                if (!isFinished) {
                    isFinished = true;
                    splashTask = null;
                    finish();
                    if (enterAnim != 0 || exitAnim != 0) {
                        overridePendingTransition(enterAnim, exitAnim);
                    }
                }
            }
        }
    }
}
