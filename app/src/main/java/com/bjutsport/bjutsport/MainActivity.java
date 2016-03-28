package com.bjutsport.bjutsport;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends BaseActivity {


    /**
     * 控件声明
     * */

    //登录按钮
    Button buttonLogin;
    //注册按钮
    Button buttonRegister;
    //跳过登陆按钮
    Button buttonSkipLogin;


    /**
     * UI线程
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    /**
     * 控件绑定
     * */

        //登录按钮
        buttonLogin = (Button) findViewById(R.id.Button_MainActivity_Login);
        //注册按钮
        buttonRegister = (Button) findViewById(R.id.Button_MainActivity_Register);
        //跳过登陆按钮
        buttonSkipLogin = (Button) findViewById(R.id.Button_MainActivity_Skip_Login);


    /**
     * UI设定
     * */

        // 透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 半透明登录按钮
        buttonLogin.getBackground().setAlpha(230);//0~255透明度值
        // 半透明注册按钮
        buttonRegister.getBackground().setAlpha(230);//0~255透明度值


    /**
     * 点击事件
     * */

        //点击登录按钮->进入LoginActivity
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });

        //点击注册按钮->进入VerificationActivity
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_verification = new Intent(MainActivity.this, VerificationActivity.class);
                Bundle bundle = new Bundle();
                String state = "register";
                //传送核实状态到VerificationActivity
                bundle.putString("state", state);
                intent_verification.putExtras(bundle);
                startActivity(intent_verification);
            }
        });

        //点击跳过登录按钮->进入UserActivity界面
        buttonSkipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_register = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent_register);
                ActivityCollector.finishAll();
            }
        });

    }

}
