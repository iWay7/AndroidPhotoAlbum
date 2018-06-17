package site.iway.androidphotoalbum;

import android.app.Application;

import site.iway.androidhelpers.BitmapCache;
import site.iway.androidhelpers.DeviceHelper;

/**
 * Created by iWay on 2018/3/25.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BitmapCache.setContext(this);
        BitmapCache.initialize();
    }
}
