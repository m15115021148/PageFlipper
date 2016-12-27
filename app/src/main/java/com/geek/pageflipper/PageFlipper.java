package com.geek.pageflipper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * 图片的轮播
 *
 * @author chenmeng created by 2016/11/08
 */
public class PageFlipper extends ViewPager {
    private List<View> views;
    private final int START_FLIPPING = 0;
    private final int STOP_FLIPPING = 1;
    private int delayed = 3000;//设置延时的时间
    private boolean isPlay = true;//是否自动播放(默认播放)

    /**
     * 内部接口 实现view的点击事件
     */
    public interface ImageOnClickListener {
        void onClickImageListener(int position);
    }

    private ImageOnClickListener imageOnClickListener;

    public void setImageOnClickListener(ImageOnClickListener imageOnClickListener) {
        this.imageOnClickListener = imageOnClickListener;
    }

    private PagerAdapter adapter = new PagerAdapter() {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return views.indexOf(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return views == null ? 0 : views.size();
        }
    };
    private OnPageChangeListener listener = new OnPageChangeListener() {

        /**
         * 将控件位置转化为数据集中的位置
         */
        public int convert(int position) {
            return position == 0 ? views.size() - 1 : (position > views.size() ? 0 : position - 1);
        }

        @Override
        public void onPageSelected(int position) {
            if (listener2 != null) {
                listener2.onPageSelected(convert(position));
            }
        }

        @Override
        public void onPageScrolled(int position, float percent, int offset) {
            if (listener2 != null) {
                listener2.onPageScrolled(convert(position), percent, offset);
            }

            if (percent == 0) {
                if (position == 0) // 切换到倒数第二页
                    setCurrentItem((views.size() - 2) % views.size(), false);
                else if (position == views.size() - 1) // 切换到正数第二页
                    setCurrentItem(1, false);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (listener2 != null) {
                listener2.onPageScrollStateChanged(state);
            }

            switch (state) {
                case SCROLL_STATE_IDLE: // 闲置
                    if (!handler.hasMessages(START_FLIPPING) && isPlay)
                        start();//开始播放
                    break;
                case SCROLL_STATE_DRAGGING: // 拖动中
                    if (isPlay)
                        stop();//取消播放
                    break;
                case SCROLL_STATE_SETTLING: // 拖动结束
                    break;
            }
        }
    }, listener2;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case START_FLIPPING:
                    if (views.size() > 3) // 因为前后页是辅助页，所以此处3也就是只有1页
                        setCurrentItem((getCurrentItem() + 1) % views.size());
                    start();//开始播放
                    break;
                case STOP_FLIPPING:
                    handler.removeMessages(START_FLIPPING);
                    break;
            }
        }
    };

    public PageFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageFlipper(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOffscreenPageLimit(1); // 最大页面缓存数量
        setAdapter(adapter); // 适配器
        super.addOnPageChangeListener(listener); // 监听器
    }

    /**
     * 开始播放
     */
    public void start(){
        if (isPlay) {
            handler.sendEmptyMessageDelayed(START_FLIPPING, delayed);  // 延时滚动
        }
    }

    /**
     * 停止播放
     */
    public void stop(){
        if (isPlay) {
            handler.sendEmptyMessage(STOP_FLIPPING); // 取消滚动
        }
    }

    /**
     * 设置图片资源
     *
     * @param ids 本地图片资源集合
     */
    public void setViews(int[] ids) {
        this.views = new ArrayList<View>();
        int pos = 0;
        for (int i = 0; i < ids.length + 2; i++) { // 头部新增一个尾页，尾部新增一个首页
            ImageView iv = new ImageView(getContext());
            pos = i == 0 ? ids.length - 1 : (i > ids.length ? 0 : i - 1);
            iv.setImageResource(ids[pos]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            final int finalPos = pos;
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageOnClickListener != null) {
                        imageOnClickListener.onClickImageListener(finalPos);
                    }
                }
            });
            this.views.add(iv);
        }
        setCurrentItem(1); // 首页
        this.adapter.notifyDataSetChanged();
    }

    /**
     * 设置图片资源(使用xutils3加载图片)
     *
     * @param ids 网络图片url的集合
     */
    public void setViews(String[] ids) {
        this.views = new ArrayList<View>();
        int pos = 0;
        for (int i = 0; i < ids.length + 2; i++) { // 头部新增一个尾页，尾部新增一个首页
            ImageView iv = new ImageView(getContext());
            pos = i == 0 ? ids.length - 1 : (i > ids.length ? 0 : i - 1);
            x.image().bind(iv,ids[pos]);
            final int finalPos = pos;
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageOnClickListener != null) {
                        imageOnClickListener.onClickImageListener(finalPos);
                    }
                }
            });
            this.views.add(iv);
        }
        setCurrentItem(1); // 首页
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        this.listener2 = listener;
    }

    public int getDelayed() {
        return delayed;
    }

    public void setDelayed(int delayed) {
        this.delayed = delayed;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}
