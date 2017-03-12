package com.example.android.repeatingalarm;

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


import android.content.Context;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.android.common.activities.SampleActivityBase;
import com.example.android.common.logger.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends SampleActivityBase {

    public static final String TAG = "CameraActivity";

    private Camera camera = null;
    private SurfaceView view = null;
    private SurfaceHolder holder = null;

    @Override
    protected void onStart() {
        super.onStart();

        // check if image has been already taken
        Log.i(TAG, "Camera/Check if image has been already taken for this timestamp");

        File image = imageFile();

        if (image.exists()) {
            Log.i(TAG, "Camera/Image for " + timestamp() + " alredy taken - skip");
            return;
        }

        if (isOnline()) {
            go();
        } else {
            Log.i(TAG, "Camera/Not Online - skip");
        }
    }

    /**
     * Create a chain of targets that will receive log data
     */
    @Override
    public void initializeLogging() {
        Log.i(TAG, "Camera/Ready");
    }

    private void go() {

        Log.i(TAG, "Camera/Take photo");

        try {

            Log.i(TAG, "Camera/Open");
            camera = Camera.open();

            //Get a surface
            Log.i(TAG, "Camera/New surface");
            view = new SurfaceView(getBaseContext());
            //Get a holder
            Log.i(TAG, "Camera/Get holder");
            holder = view.getHolder();
            //tells Android that this surface will have its data constantly replaced
            Log.i(TAG, "Camera/Set holder to SURFACE_TYPE_PUSH_BUFFERS");
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            // tells camera to use the holder
            Log.i(TAG, "Camera/Set camera holder");
            camera.setPreviewDisplay(holder);

            Log.i(TAG, "Camera/Set parameters");
            Camera.Parameters parameters = camera.getParameters();

            try {
                parameters.set("preview-size", getResources().getString(R.string.image_preview_size));
            } catch (Exception e) {
                Log.e(TAG, "Camera/Non supported parameter preview-size");
            } //176x144,320x240,352x288,480x320,480x360,480x368,640x480,720x480,800x480,800x600,864x480,960x540,1280x720,1920x1080,360x480
            try {
                parameters.set("picture-size", getResources().getString(R.string.image_picture_size));
            } catch (Exception e) {
                Log.e(TAG, "Camera/Non supported parameter picture-size");
            } //320x240,480x360,640x480,1024x768,1280x720,1280x960,1600x912,1600x1200,2048x1152,2048x1536,2560x1440,2560x1920,3328x1872,3264x2448,3600x2700,3672x2754,4096x2304,4096x3072,4160x3120,4608x2592,360x480
            try {
                parameters.set("picture-format", getResources().getString(R.string.image_picture_format));
            } catch (Exception e) {
                Log.e(TAG, "Camera/Non supported parameter picture-format");
            } //jpeg
            try {
                parameters.set("jpeg-quality", getResources().getString(R.string.image_jpeg_quality));
            } catch (Exception e) {
                Log.e(TAG, "Camera/Non supported parameter jpeg-quality");
            } //60,70,80,85,90
            try {
                parameters.set("orientation", getResources().getString(R.string.image_orientation));
            } catch (Exception e) {
                Log.e(TAG, "Camera/Non supported parameter orientation");
            } // portrait, landscape
            try {
                parameters.set("rotation", getResources().getString(R.string.image_rotation));
            } catch (Exception e) {
                Log.e(TAG, "Camera/Non supported parameter rotation");
            } //0,90,180,270
            try {
                parameters.set("flash-mode", getResources().getString(R.string.image_flash_mode));
            } catch (Exception e) {
                Log.e(TAG, "Camera/Non supported parameter flash-mode");
            } // off,on,auto,red-eye,torch
            try {
                parameters.set("iso-speed", getResources().getString(R.string.image_iso_speed));
            } catch (Exception e) {
                Log.e(TAG, "Camera/Non supported parameter iso-speed");
            } // auto,100,200,400,800,1600
            try {
                parameters.set("whitebalance", getResources().getString(R.string.image_whitebalance));
            } catch (Exception e) {
                Log.e(TAG, "Camera/Non supported parameter whitebalance");
            }//auto,incandescent,fluorescent,warm-fluorescent,daylight,cloudy-daylight,twilight,shade
            try {
                parameters.set("scene-mode", getResources().getString(R.string.image_scene_mode));
            } catch (Exception e) {
                Log.e(TAG, "Camera/Non supported parameter scene-mode");
            } //auto,portrait,landscape,night,night-portrait,theatre,beach,snow,sunset,steadyphoto,fireworks,sports,party,candlelight,hdr
            try {
                parameters.set("focus-mode", getResources().getString(R.string.image_focus_mode));
            } catch (Exception e) {
                Log.e(TAG, "Camera/Non supported parameter focus-mode");
            } // auto,macro,infinity,continuous-picture,continuous-video,manual,fullscan

            camera.setParameters(parameters);

            //Log.i(TAG, "Dump all camera parameters:");
            //String flattened = parameters.flatten();
            //StringTokenizer tokenizer = new StringTokenizer(flattened, ";");
            //while (tokenizer.hasMoreElements()) {
            //    Log.d(TAG, tokenizer.nextToken());
            //}

            Log.i(TAG, "Camera/Start preview");
            camera.startPreview();

            Log.i(TAG, "Camera/Take pucture");
            camera.takePicture(null, null, pictureCallback);

            Log.i(TAG, "Camera/Pucture sheduled");
        } catch (Exception e) {
            Log.e(TAG, "Camera/Cannot find camera");
            if (camera != null) {
                camera.release();
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] imageData, Camera c) {
            Log.i(TAG, "Camera/Callback");

            if (imageData != null) {
                // Create an image file name
                Log.i(TAG, "Camera/Create image file name");
                final File image = imageFile();

                FileOutputStream outputStream = null;
                try {

                    Log.i(TAG, "Camera/Create outputstream to image file");
                    outputStream = new FileOutputStream(image);

                    Log.i(TAG, "Camera/Write image file");
                    outputStream.write(imageData);
                    Log.i(TAG, "Camera/Image file written");


                } catch (Exception e) {
                    Log.e(TAG, "Camera/Cannot take/write photo");
                    return;
                } finally {
                    if (outputStream != null) try {
                        Log.i(TAG, "Camera/Close image file");
                        outputStream.close();
                        Log.i(TAG, "Camera/Image file closed");
                    } catch (IOException ex) {
                        Log.e(TAG, "Camera/Cannot close image file");
                        return;
                    }

                    if (camera != null) {
                        Log.i(TAG, "Camera/Release camera");
                        camera.release();
                        Log.i(TAG, "Camera/Camera released");
                    }

                }

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        // send image by email
                        try {


                            Log.i(TAG, "Camera/Initialize gmail sender");
                            GMailSender sender = new GMailSender(getResources().getString(R.string.email_user_name), getResources().getString(R.string.email_user_password));
                            Log.i(TAG, "Camera/Gmail sender created");

                            Log.i(TAG, "Camera/Add attachment");
                            sender.addAttachment(image.getAbsolutePath());
                            Log.i(TAG, "Camera/Attachment added");

                            Log.i(TAG, "Camera/Sending email");
                            sender.sendMail(getResources().getString(R.string.camera_name) + " " + timestamp(), getResources().getString(R.string.minitor_name), getResources().getString(R.string.email_user_name), getResources().getString(R.string.email_user_name));
                            Log.i(TAG, "Camera/Email send");
                        } catch (Exception e) {
                            Log.e(TAG, "Camera/Cannot send email");

                            try {
                                Log.i(TAG, "Camera/Delete imgae");
                                image.delete();
                                Log.i(TAG, "Camera/Image deleted");
                            } catch (Exception ed) {
                                Log.e(TAG, "Camera/Cannot delete image");
                            }
                        }
                        return null;
                    }
                }.execute();

            }
        }
    };

    private String timestamp() {
        return new SimpleDateFormat(getResources().getString(R.string.image_timestamp_format)).format(new Date());
    }


    private File imageFile() {
        String timeStamp = timestamp();
        String imageFileName = getResources().getString(R.string.image_prefix) + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(
                storageDir.getAbsolutePath() + "/" +   /* directory */
                        imageFileName +       /* prefix */
                        ".jpg"               /* suffix */

        );

        return image;

    }

}
