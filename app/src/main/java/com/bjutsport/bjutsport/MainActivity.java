package com.bjutsport.bjutsport;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        View button_login = findViewById(R.id.Button_Login);//找到你要设透明背景的layout 的id
        button_login.getBackground().setAlpha(215);//0~255透明度值
        View button_sign = findViewById(R.id.Button_Register);//找到你要设透明背景的layout 的id
        button_sign.getBackground().setAlpha(215);//0~255透明度值

        Button ButtonLogin = (Button)findViewById(R.id.Button_Login);
        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent_login);

            }
        });

        Button ButtonRegister = (Button)findViewById(R.id.Button_Register);
        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_sign = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent_sign);
            }
        });
    }
}
