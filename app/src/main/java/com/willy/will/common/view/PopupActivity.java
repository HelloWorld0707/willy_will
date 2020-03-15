package com.willy.will.common.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public abstract class PopupActivity extends Activity {

    private int layId = 0;

    // Initialization (including layout ID)
    public PopupActivity(int layoutId) {
        super();
        layId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ~Remove title bar
        setContentView(layId);
    }

    public void cancelSetting(View view) {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    public void submitSetting(View view) {
        Intent intent = new Intent();
        if(setResults(intent)) {
            setResult(RESULT_FIRST_USER, intent);
            this.finish();
        }
    }

    // Set result data and return whether setting results is succeed
    protected abstract boolean setResults(Intent intent) ;

}
