package com.xgx.dabainian;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = "http://u.uin.com/ddm/uin_pic/ftp/my/welcome.png";
        SplashScreen config = new SplashScreen(SplashActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(3000)
                .withBackgroundDrawable(url);


        //set your own animations

        //customize all TextViews

        //create the view
        View easySplashScreenView = config.create();

        setContentView(easySplashScreenView);

    }

}
