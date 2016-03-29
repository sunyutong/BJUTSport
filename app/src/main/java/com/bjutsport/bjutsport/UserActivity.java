package com.bjutsport.bjutsport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.bjutsport.view.SlidingMenu;

public class UserActivity extends Activity {

    private SlidingMenu mLeftMenu;

    //搜索按钮
    TextView textViewSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user);

        mLeftMenu = (SlidingMenu) findViewById(R.id.Menu_UserActivity);
        textViewSearch= (TextView) findViewById(R.id.TextView_UserActivity_To_SearchActivity);

        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_search = new Intent(UserActivity.this,SearchActivity.class);
                startActivity(intent_search);
            }
        });
    }

    public void toggleMenu(View view) {
        mLeftMenu.toggle();
    }

}
