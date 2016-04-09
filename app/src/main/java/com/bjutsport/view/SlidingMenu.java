package com.bjutsport.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.bjutsport.bjutsport.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by 邵励治 on 2016/3/27.
 * 根据慕课网教学视频搞的：QQ5.0侧滑菜单
 */
public class SlidingMenu extends HorizontalScrollView {

    //横向水平滚动条中肯定有个LinearLayout
    private LinearLayout mWapper;
    //菜单栏是个ViewGroup的子类
    private ViewGroup mMenu;
    //内容区域：主界面
    public ViewGroup mContent;
    //手机屏幕的宽度
    private int mScreenWidth;
    private int mMenuWidth;
    //Menu与右侧的距离
    private int mMenuRightPadding = 50;
    private boolean once;
    private boolean isOpen;

    public SlidingMenu(Context context) {
        this(context, null);
    }
    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //获取我们定义的属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenu_rightPadding:
                    mMenuRightPadding = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
                    break;
            }
        }

        //TypedArray需要释放
        a.recycle();
        //获取手机屏幕的宽度
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
    }

    /**
     * 设置子View的宽和高
     * 设置自己的宽和高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!once) {
            //滚动条里面也就一个元素
            mWapper = (LinearLayout) getChildAt(0);
            //LinearLayout里面的第一个元素：菜单
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            //LinearLayout里面的第二个元素:主界面
            mContent = (ViewGroup) mWapper.getChildAt(1);
            //高度是match_parent，不需要显式设置。但是宽度，这里显式设置一下。
            //menu的宽度就等于屏幕的宽度减去右侧保留的宽度
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            //显式设置一下Content的宽度——就是屏幕的宽度
            mContent.getLayoutParams().width = mScreenWidth;
            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //我们需要这样一个效果：即先显示Content，menu在左侧隐藏。所以在这里设置一个偏移量。
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //为了防止多次调用
        if (changed) {
            //scrollTO(x,y)使用方法说明：x为正值时，滚动条向右移动，内容区域向左移动
            //移动一个menu的宽度，这样就正好将其隐藏住了
            this.scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                //隐藏在左边的宽度就是getScrollX
                int scrollX = getScrollX();

                if (scrollX >= (mMenuWidth * 3 / 4)) {
                    //ScrollTO是瞬间完成，smoothScrollTo是有一个动画的感觉
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                } else {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    public void openMenu() {
        if (isOpen) return;
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }
    public void closeMenu() {
        if (!isOpen) return;
        //ScrollTO是瞬间完成，smoothScrollTo是有一个动画的感觉
        this.smoothScrollTo(mMenuWidth, 0);
        isOpen = false;
    }
    /**
     * 切换菜单
     */
    public void toggle() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }
//    /**
//     * 抽屉式菜单
//     */
//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        super.onScrollChanged(l, t, oldl, oldt);
//        float scale = l * 1.0f / mMenuWidth;
//        //调用属性动画，设置TranslationX
//        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale);
//    }
}