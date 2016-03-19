package com.bjutsport.bjutsport;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by 邵励治 on 2016/3/19.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
