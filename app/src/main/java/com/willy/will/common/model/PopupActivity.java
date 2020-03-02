package com.willy.will.common.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public abstract class PopupActivity extends Activity {

    private int layId = 0;

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-17
     * Created By: Shin Minyong
     * Function: Initialization (including layout ID)
     * @param layoutId
     */
    public PopupActivity(int layoutId) {
        super();
        layId = layoutId;
    }

    /**
     * Last Modified: 2020-02-17
     * Last Modified By: Shin Minyong
     * Created: -
     * Created By: -
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ~Remove title bar
        setContentView(layId);
    }

    /**
     * Last Modified: 2020-02-19
     * Last Modified By: Shin Minyong
     * Created: 2020-02-10
     * Created By: Shin Minyong
     * Function: Cancel search setting
     * @param view
     */
    public void cancelSetting(View view) {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    /**
     * Last Modified: 2020-02-26
     * Last Modified By: Shin Minyong
     * Created: 2020-02-19
     * Created By: Shin Minyong
     * Function: Submit search setting
     * @param view
     */
    public void submitSetting(View view) {
        Intent intent = new Intent();
        if(setResults(intent)) {
            setResult(RESULT_FIRST_USER, intent);
            this.finish();
        }
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-19
     * Created By: Shin Minyong
     * Function: Set result data
     * @param intent
     * @return whether setting results is succeed
     */
    protected abstract boolean setResults(Intent intent) ;

    /**
     * https://ghj1001020.tistory.com/9
     */

}
