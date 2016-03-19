package com.bjutsport.bjutsport;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 邵励治 on 2016/3/19.
 */

/**
 * ActivityCollector类是一个活动管理器
 * */
public class ActivityCollector {

    //List用来暂存活动
    public static List<Activity> activities = new ArrayList<Activity>();

    //addActivity用来向List里添加一个活动
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    //removeActivity用来从List中移除一个活动
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    //finishAll方法用于将List中存储的活动全部销毁掉
    public static void finishAll() {
        for (Activity activity : activities) {
            if(!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}

