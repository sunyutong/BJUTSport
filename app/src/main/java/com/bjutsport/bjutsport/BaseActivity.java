package com.bjutsport.bjutsport;

import android.app.Activity;
import android.os.Bundle;


/**
 * Created by 邵励治 on 2016/3/19.
 * 注册、登录相关活动的父类
 */

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //向活动管理器中添加此活动
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //从活动管理器中移除此活动
        ActivityCollector.removeActivity(this);
    }
}
