package com.geek.pageflipper;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.xutils.*;

import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initXutils();
    }

    /**
     * 初始化xutils框架
     */
    private void initXutils() {
        x.Ext.init(this);
        x.Ext.setDebug(org.xutils.BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }

    /**
     * 画点
     *
     * @param mContext 本类
     * @param layout   圆点的布局
     * @param list     圆点的集合
     * @param len      圆点的数量
     * @param select   圆点选中的图片
     * @param normal   圆点没有选中的图片
     */
    public static void drawPoint(Context mContext,
                                 LinearLayout layout, List<View> list,
                                 int len,
                                 int select, int normal) {
        list.clear();
        layout.removeAllViews();
//        int size = mContext.getResources().getDimensionPixelSize(R.dimen._16px_in720p);
        for (int i = 0; i < len; i++) {
            ImageView dot = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    size,size
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 0, 10, 0);
            if (i == 0) {
                dot.setBackgroundResource(select);
            } else {
                dot.setBackgroundResource(normal);
            }
            layout.addView(dot, params);
            list.add(dot);
        }
    }
}
