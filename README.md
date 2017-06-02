# README #

Simple lightweight and easy-to-use splash library for android.

this library already implemented splash timers combining with background task logic for you. All that left for you to do is to customize it to match your needs.

## Installation ###

Add the following dependency into your app-level build.gradle

```groovy
dependencies {
    compile 'com.hadadroid:android-splash:1.0.0'
}
```

Add the following activity to your AndroidManifest.xml

```xml
<activity
    android:name="com.hadadroid.splash.SplashActivity"
    android:screenOrientation="portrait"
    android:theme="@android:style/Theme.NoTitleBar.Fullscreen"/>
```
    
## Usage ##

In your `OnCreate` method of your main activity, before calling `setContentView()`, call the static function
```java
SplashBuilder
        .with(this, savedInstanceState)
        .show();
```

__That's it! A splash screen will appear while your app is launching.__

## Customizing the Splash Screen ##

Here are several methods that can help you configure your splash screen:

* `setSplashTask` - add code that will run while the splash screen is displayed.
* `setLayoutId` - set your layout resource instead of using a pre-defined splash screen.
* `setMinimumTimeout` - define the minimum display time for the splash screen. 
* `setMaximumTimeout` - define the maximum display time for the splash screen. Note that this parameter only relevant if you set `SplashTask`.   
* `overridePendingTransition` - set an explicit transition animation to perform after SplashActivity finishes.

## Example ##

```java
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

```

### License ###

Apache 2.0