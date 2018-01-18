package zoro.test.com.functionset;

import android.app.Application;
import android.content.Context;

import me.hekr.sdk.HekrSDK;


/**
 * Created by hekr_jds on 6/30 0030.
 **/
public class MyApplication extends Application {

    private String EZappKey="42b5ccd6136c4eed858bbe2a66e95d25";

    public static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化SDK
        HekrSDK.init(this, R.raw.config);
        // 是否开启日志，默认开启
        HekrSDK.enableDebug(true);
        mContext = this.getApplicationContext();
    }

    public static MyApplication getAppContext() {
        return (MyApplication) mContext;
    }
}
