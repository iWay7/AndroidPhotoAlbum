package site.iway.androidphotoalbum;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import site.iway.androidhelpers.BitmapCache;
import site.iway.androidhelpers.BitmapCallback;
import site.iway.androidhelpers.BitmapRequest;
import site.iway.androidhelpers.BitmapSource;
import site.iway.androidhelpers.ImageViewer;
import site.iway.androidhelpers.PhotoAlbum;

public class MainActivity extends Activity {

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
            String imageUrl = "http://home.iway.site:8888/test/images/image%20(" + (position + 1) + ").jpg";
            BitmapSource source = new BitmapSource(BitmapSource.TYPE_URL, imageUrl, null);
            BitmapRequest request = new BitmapRequest(source, new BitmapCallback() {
                @Override
                public void onBitmapLoadProgressChange(BitmapRequest bitmapRequest) {
                    int progress = bitmapRequest.getProgress();
                    if (progress == BitmapRequest.GET_BITMAP) {
                        BitmapSource source = bitmapRequest.getSource();
                        Bitmap bitmap = BitmapCache.get(source);
                        if (bitmap != null) {
                            Bitmap copiedBitmap = Bitmap.createBitmap(bitmap);
                            imageViewer.setBitmap(copiedBitmap);
                        }
                    }
                }
            });
            BitmapCache.requestNow(request);
            container.addView(imageViewer);
            return imageViewer;
        }

    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyPhotoAlbum myPhotoAlbum = new MyPhotoAlbum(this);
        myPhotoAlbum.setAdapter(new ViewPagerAdapter());
        setContentView(myPhotoAlbum);
    }
}
