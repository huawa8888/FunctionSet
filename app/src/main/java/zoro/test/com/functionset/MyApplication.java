package me.hekr.demo;

import android.app.Application;

import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import me.hekr.hekrsdk.util.HekrSDK;

/**
 * Created by hekr_jds on 6/30 0030.
 **/
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        //推送服务初始化
        PushManager.getInstance().initialize(getApplicationContext());
        //初始化HekrSDK
        HekrSDK.init(getApplicationContext(), R.raw.config);
        HekrSDK.openLog(true);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(80, 80)
                .denyCacheImageMultipleSizesInMemory()
                //.writeDebugLogs()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
