package com.geek.pageflipper;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements PageFlipper.ImageOnClickListener{
    private MainActivity mContext;
    @ViewInject(R.id.pagerflipper)
    private PageFlipper mPf;
    @ViewInject(R.id.main_indictor)
    private LinearLayout mLayout;//导航圈的布局
    private List<View> mDotList = new ArrayList<>();//圈的集合

    public static final int[] images = {R.drawable.img_nature1,R.drawable.img_nature2,R.drawable.img_nature3};

//    public static final String[] images = {
//
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initViewPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPf.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPf.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPf.stop();
    }

    /**
     * 初始化viewpage
     */
    private void initViewPage() {
        mPf.setViews(images);
        mPf.setImageOnClickListener(this);
        MyApplication.drawPoint(this, mLayout, mDotList, images.length, R.drawable.indictor_select, R.drawable.indictor_normal);
        mPf.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mDotList.size(); i++) {
                    if (i == position) {
                        mDotList.get(position).setBackgroundResource(R.drawable.indictor_select);
                    } else {
                        mDotList.get(i).setBackgroundResource(R.drawable.indictor_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClickImageListener(int position) {
        Toast.makeText(mContext,"点击了第"+(position+1)+"张",Toast.LENGTH_SHORT).show();
    }
}
