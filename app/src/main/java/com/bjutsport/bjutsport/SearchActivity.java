package com.bjutsport.bjutsport;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by 邵励治 on 2016/3/24.
 * 由UserActivity的搜索框出生，死于后退按钮
 */

public class SearchActivity extends Activity {

    Button buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);

        buttonBack = (Button)findViewById(R.id.Button_SearchActivity_Back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
