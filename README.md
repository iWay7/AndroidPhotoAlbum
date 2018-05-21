# AndroidPhotoAlbum
Android 相册。

### 本应用的示例

![image](https://github.com/iWay7/AndroidPhotoAlbum/blob/master/sample.gif)   

#### 本示例基于 AndroidHelpers/ImageViewer 库，访问 https://github.com/iWay7/AndroidImageViewer 添加依赖。

#### 使用方法：
##### 首先创建一个 ViewPager 的适配器：
```
private class ViewPagerAdapter extends PagerAdapter {
    @Override
    public int getCount() {
        return 157;
    }
    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == obj;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageViewer imageViewer = new ImageViewer(MainActivity.this);
        imageViewer.setMultiSamplingEnabled(true);
        imageViewer.setTag(position);
        BitmapSource source = new BitmapSourceURL("http://home.iway.site:8888/test/images/image%20(" + (position + 1) + ").jpg");
        BitmapInfoListener listener = new BitmapInfoListener() {
            @Override
            public void onBitmapInfoChange(BitmapInfo bitmapInfo) {
                int progress = bitmapInfo.getProgress();
                if (progress == BitmapInfo.GET_BITMAP) {
                    bitmapInfo.lockBitmap();
                    Bitmap bitmap = bitmapInfo.getBitmap();
                    if (bitmap != null) {
                        Bitmap copiedBitmap = Bitmap.createBitmap(bitmap);
                        imageViewer.setBitmap(copiedBitmap);
                    }
                    bitmapInfo.unlockBitmap();
                }
            }
        };
        BitmapCache.get(source, listener);
        container.addView(imageViewer);
        return imageViewer;
    }
}
```

##### 在适配器的 instantiateItem 方法中，返回的 View 必须是 ImageViewer 视图。

##### 然后创建一个基于 PhotoAlbum 视图并重写 isImageViewerInPosition 方法：
```
private class MyPhotoAlbum extends PhotoAlbum {
    public MyPhotoAlbum(Context context) {
        super(context);
    }
    public MyPhotoAlbum(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected boolean isImageViewerInPosition(ImageViewer imageViewer, int position) {
        Object tag = imageViewer.getTag();
        return tag != null && tag.equals(position);
    }
}
```

##### 这里 isImageViewerInPosition 必须要实现判断一个 ImageViewer 是否在某个位置的逻辑。示例中的方法使用 View 的 Tag 保存。