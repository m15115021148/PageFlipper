# PageFlipper
轮播图片 带导航圈 即可加载网络图片 有可加载本地图片
部分代码如下:
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
    
  使用xutils3加载图片
