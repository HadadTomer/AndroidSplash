package com.hadadroid.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

public class SplashBuilder {

    public interface OnCompleteListener {
        void onComplete();
    }

    /**
     * Implement this interface to run the code you wish
     * to execute while the splash screen is displayed.
     * You must call {@link OnCompleteListener#onComplete()}
     * when your task has finished.
     */
    public interface SplashTask {
        void run(OnCompleteListener listener);
    }

    private SplashBuilder() {
    }

    public static Builder with(Activity activity, Bundle savedInstanceState) {
        if (activity == null) {
            throw new IllegalArgumentException("activity can't be null");
        }

        return new Builder(activity, savedInstanceState);
    }

    public static class Builder implements Serializable {

        private Activity activity = null;
        private Bundle savedInstanceState;

        private int layoutId = 0;
        private long minimumTimeout, maximumTimeout;
        private int enterAnim, exitAnim;
        private SplashTask splashTask = null;

        private Builder(Activity activity, Bundle savedInstanceState) {
            this.activity = activity;
            this.savedInstanceState = savedInstanceState;
        }

        /**
         * Set the splash activity content from a layout resource.
         * If not set, the default pre-defined layout will be used.
         * @param layoutId Resource ID to be inflated
         * @return current Builder instance
         */
        public Builder setLayoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        /**
         * Set the minimum display time for the splash screen.
         * If not set, the minimum timeout will set to 3 seconds by default.
         * @param minimumTimeout splash minimum display time in milliseconds.
         * @return current Builder instance
         */
        public Builder setMinimumTimeout(long minimumTimeout) {
            if (minimumTimeout <= 0) {
                throw new IllegalArgumentException("minimumTimeout must be greater than zero");
            }

            if (maximumTimeout != 0 && minimumTimeout > maximumTimeout) {
                throw new IllegalArgumentException("minimumTimeout can't be greater than maximumTimeout");
            }

            this.minimumTimeout = minimumTimeout;
            return this;
        }

        /**
         * Set the maximum display time for the splash screen.
         * @param maximumTimeout splash maximum display time in milliseconds.
         * @return current Builder instance
         */
        public Builder setMaximumTimeout(long maximumTimeout) {
            if (maximumTimeout <= 0) {
                throw new IllegalArgumentException("maximumTimeout must be greater than zero");
            }

            if (minimumTimeout != 0 && minimumTimeout > maximumTimeout) {
                throw new IllegalArgumentException("maximumTimeout can't be less than minimumTimeout");
            }

            this.maximumTimeout = maximumTimeout;
            return this;
        }

        /**
         * Set {@link SplashTask} to run while splash screen is displayed.
         * @param splashTask task that will run while the splash screen is displayed
         * @return current Builder instance
         */
        public Builder setSplashTask(SplashTask splashTask) {
            this.splashTask = splashTask;
            return this;
        }

        /**
         * Set an explicit transition animation to perform after {@link SplashActivity} finishes.
         * see {@link Activity#overridePendingTransition(int, int)}
         * @return current Builder instance
         */
        public Builder overridePendingTransition(int enterAnim, int exitAnim) {
            this.enterAnim = enterAnim;
            this.exitAnim = exitAnim;
            return this;
        }

        /**
         * Start {@link SplashActivity} and show the splash screen.
         */
        public void show() {
            if (savedInstanceState == null) {
                Intent intent = new Intent(activity, SplashActivity.class);

                Bundle args = new Bundle();
                args.putInt(SplashActivity.LAYOUT_ID, layoutId);
                args.putInt(SplashActivity.ENTER_ANIM, enterAnim);
                args.putInt(SplashActivity.EXIT_ANIM, exitAnim);
                args.putLong(SplashActivity.MINIMUM_TIMEOUT, minimumTimeout);
                args.putLong(SplashActivity.MAXIMUM_TIMEOUT, maximumTimeout);
                SplashActivity.setSplashTask(splashTask);

                intent.putExtra(SplashActivity.SPLASH_ARGUMENTS, args);
                activity.startActivity(intent);
            }
        }
    }
}
