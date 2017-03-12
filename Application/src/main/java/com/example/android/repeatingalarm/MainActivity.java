/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package com.example.android.repeatingalarm;


/*
# RepeatingAlarm
Take and send photo every hour(minute, day ,month, year) to a specific gmail address.

APPLICATION IS BASED/EXTENDED from Android exmaple RepeatingAlarm

Consfiguration is in : base-strings.xml

STARTUP SETTINGS
<pre><code>
 <string name="startup_register">ON</string> - when set to ON the application will Activate/Set the alarm automaticaluy without human intervention. Any other value will require the alarm to set Activate/Set using the UI - after first time the alarm will be active until application is killed/stopped from android device settings.

 <string name="startup_on_boot">ON</string> - when set to ON the application will start automaticaly after boot. Any other value will require manual start of the application.

GMAIL SETTINGS

<string name="email_user_name">sy.alfil.sl34@gmail.com</string> user name/email in gmail on which behalf the emails will be sent and will receice the emails also.

<string name="email_user_password">password</string> password for the gmail account

Subject of the email will have the following format : CEMARA timestamp e.g. : CAMERA 20170312_16
The camera text can be changed from the following setting :

<string name="camera_name">CAMERA</string>

Image file name  will have the following format : PREFIX+timestamp e.g. : ALFIL_20170312_16
The prefix can be changes from the following setting:

 <string name="image_prefix">ALFIL_</string>

!!!IMPORTANT!!!
In order he email sending to work you need to set in your google account lesser security apps to ON e.g. :

https://www.google.com/settings/security/lesssecureapps - need to be Turned ON

IMAGE SETTINGS

Depending on your device camera you can use the following settings

<string name="image_preview_size">1920x1080</string>
<string name="image_picture_size">2560x1920</string>
<string name="image_picture_format">jpeg</string>
<string name="image_jpeg_quality">85</string>
<string name="image_orientation">landscape</string>
<string name="image_rotation">0</string>
<string name="image_flash_mode">off</string>
<string name="image_iso_speed">auto</string>
<string name="image_whitebalance">auto</string>
<string name="image_scene_mode">auto</string>
<string name="image_focus_mode">continuous-picture</string>

TIME INTERVAL SETTING

The interval in which the images are taken and sent to the email is controlled from teh following setting:

 <string name="image_timestamp_format">yyyyMMdd_HH</string>

 every minute (once per minute) : yyyyMMdd_HHmm
 every hour (once per hour) : yyyyMMdd_HH
 every day (once per day): yyyyMMdd
 every month (once per month) : yyyyMM
 every year(once per year) : yyyy


</code>
</pre>



 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.example.android.common.activities.SampleActivityBase;
import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;

/**
 * A simple launcher activity containing a summary sample description
 * and a few action bar buttons.
 */
public class MainActivity extends SampleActivityBase {

    public static final String TAG = "MainActivity";

    public static final String FRAGTAG = "RepeatingAlarmFragment";

    // This value is defined and consumed by app code, so any value will work.
    // There's no significance to this sample using 0.
    public static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if not startup register ON - start fragment
        if (getResources().getString(R.string.startup_register).trim().toUpperCase().compareTo("ON") != 0) {
            setContentView(R.layout.activity_main);

            if (getSupportFragmentManager().findFragmentByTag(FRAGTAG) == null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                RepeatingAlarmFragment fragment = new RepeatingAlarmFragment();
                transaction.add(fragment, FRAGTAG);
                transaction.commit();
            }
        }
    }

    private static boolean once = false;

    @Override
    protected void onStart() {
        super.onStart();

        if (once) {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        } else {

            if (getResources().getString(R.string.startup_register).trim().toUpperCase().compareTo("ON") == 0) {
                // auto register
                registerAlarm();

                Log.i(TAG, "Main/Alarm Auto Set");
            }

            once = true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Create a chain of targets that will receive log data
     */
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        // On screen logging is only possible when fragment is created - in auto register no onscreen logging
        if (getResources().getString(R.string.startup_register).trim().toUpperCase().compareTo("ON") != 0) {
            LogFragment logFragment = (LogFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.log_fragment);
            msgFilter.setNext(logFragment.getLogView());
            logFragment.getLogView().setTextAppearance(this, R.style.Log);
            logFragment.getLogView().setBackgroundColor(Color.WHITE);
        }

        Log.i(TAG, "Main/Ready");
    }

    //WARNING!!! same code exists in RepeatingAlarmFragment - if changed here need to be changed in RepeatingAlarmFragment also !
    private void registerAlarm() {
        // BEGIN_INCLUDE (intent_fired_by_alarm)
        // First create an intent for the alarm to activate.
        // This code simply starts an Activity, or brings it to the front if it has already
        // been created.
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // END_INCLUDE (intent_fired_by_alarm)

        // BEGIN_INCLUDE (pending_intent_for_alarm)
        // Because the intent must be fired by a system service from outside the application,
        // it's necessary to wrap it in a PendingIntent.  Providing a different process with
        // a PendingIntent gives that other process permission to fire the intent that this
        // application has created.
        // Also, this code creates a PendingIntent to start an Activity.  To create a
        // BroadcastIntent instead, simply call getBroadcast instead of getIntent.
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE,
                intent, 0);

        // END_INCLUDE (pending_intent_for_alarm)

        // BEGIN_INCLUDE (configure_alarm_manager)
        // There are two clock types for alarms, ELAPSED_REALTIME and RTC.
        // ELAPSED_REALTIME uses time since system boot as a reference, and RTC uses UTC (wall
        // clock) time.  This means ELAPSED_REALTIME is suited to setting an alarm according to
        // passage of time (every 15 seconds, 15 minutes, etc), since it isn't affected by
        // timezone/locale.  RTC is better suited for alarms that should be dependant on current
        // locale.

        // Both types have a WAKEUP version, which says to wake up the device if the screen is
        // off.  This is useful for situations such as alarm clocks.  Abuse of this flag is an
        // efficient way to skyrocket the uninstall rate of an application, so use with care.
        // For most situations, ELAPSED_REALTIME will suffice.
        int alarmType = AlarmManager.ELAPSED_REALTIME;
        final int THIRTY_SEC_MILLIS = 30000;

        // The AlarmManager, like most system services, isn't created by application code, but
        // requested from the system.
        AlarmManager alarmManager = (AlarmManager)
                this.getSystemService(this.ALARM_SERVICE);

        // setRepeating takes a start delay and period between alarms as arguments.
        // The below code fires after 15 seconds, and repeats every 15 seconds.  This is very
        // useful for demonstration purposes, but horrendous for production.  Don't be that dev.
        alarmManager.setRepeating(alarmType, SystemClock.elapsedRealtime() + THIRTY_SEC_MILLIS,
                THIRTY_SEC_MILLIS, pendingIntent);
        // END_INCLUDE (configure_alarm_manager);
    }
}
