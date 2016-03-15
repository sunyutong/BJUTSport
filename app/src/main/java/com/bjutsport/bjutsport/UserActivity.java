package com.bjutsport.bjutsport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //获取测试按钮
        Button button_test = (Button) findViewById(R.id.Button_Back_to_MainActivity);
        //点击返回主界面
        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_User = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent_User);
                finish();
            }
        });

    }
}
