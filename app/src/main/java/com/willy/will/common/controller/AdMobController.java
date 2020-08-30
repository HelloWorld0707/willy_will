package com.willy.will.common.controller;


import android.app.Activity;

import com.google.android.gms.ads.AdView;
import com.willy.will.R;

import static com.willy.will.main.view.MainActivity.adRequest;

public class AdMobController {
    private AdView adView;
    private Activity activity;

    public AdMobController(Activity activity){
        this.activity = activity;
    }

    public void callingAdmob () {
        adView = activity.findViewById(R.id.adView);
        adView.loadAd(adRequest);
    }
}