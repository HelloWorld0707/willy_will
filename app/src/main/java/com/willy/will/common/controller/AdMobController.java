package com.willy.will.common.controller;


import android.app.Activity;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.willy.will.R;

import static com.willy.will.main.view.MainActivity.adRequest;

public class AdMobController {
    private AdView adView;
    private Activity activity;
    private ImageView imageView;

    public AdMobController(Activity activity){
        this.activity = activity;
    }

    public void callingAdmob () {
        adView = activity.findViewById(R.id.adView);
        adView.loadAd(adRequest);
        imageView = activity.findViewById(R.id.alterImg);

        adView.setAdListener(new AdListener(){
            private boolean firstAdReceived = false;

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                firstAdReceived = true;
                imageView.setVisibility(imageView.GONE);
                adView.setVisibility(adView.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                imageView.setVisibility(imageView.VISIBLE);
                adView.setVisibility(adView.GONE);
            }
        });
    }
}