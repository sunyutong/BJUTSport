package com.bjutsport.bjutsport;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.bjutsport.view.SlidingMenu;

public class UserActivity extends BaseActivity implements View.OnTouchListener, GestureDetector.OnGestureListener{


    //创建一个用于识别收拾的GestureDetector对象
    private GestureDetector detector = new GestureDetector(this);
    //滑动菜单
    private SlidingMenu userLayout;
    //搜索按钮
    TextView textViewSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user);
        userLayout = (SlidingMenu)findViewById(R.id.Menu_UserActivity);
        textViewSearch= (TextView) findViewById(R.id.TextView_UserActivity_To_SearchActivity);
        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_search = new Intent(UserActivity.this, SearchActivity.class);
                startActivity(intent_search);
            }
        });


        userLayout.setOnTouchListener(this);
        userLayout.setLongClickable(true);
        detector.setIsLongpressEnabled(true);
    }


    public void toggleMenu(View view) {
        userLayout.toggle();
    }

    //重写OnTouchListener的onTouch方法
    //此方法在触摸屏被触摸，即发生触摸事件（接触和抚摸两个事件，挺形象）的时候被调用。
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        detector.onTouchEvent(event);
        return true;
    }

    //在按下动作时被调用
    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }

    //在抛掷动作时被调用
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        //velocityX表示横向的移动，根据手指移动的方向切换女孩
        if(velocityX < 0){
            userLayout.closeMenu();
        }else if (velocityX > 0){
            userLayout.openMenu();
        }
        return false;
    }

    //在长按时被调用
    @Override
    public void onLongPress(MotionEvent e) {
    }

    //在滚动时调用
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    //在按住时被调用
    @Override
    public void onShowPress(MotionEvent e) {
    }

    //在抬起时被调用
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(inRangeOfView(userLayout.mContent,e)){
            userLayout.closeMenu();
        }
        return false;
    }

    //判断点击的位置是不是在Content上
    private boolean inRangeOfView(View view, MotionEvent ev){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if(ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())){
            return false;
        }
        return true;
    }

}