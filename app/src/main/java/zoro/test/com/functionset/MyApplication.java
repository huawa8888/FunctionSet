package zoro.test.com.functionset;

import android.app.Application;
import android.content.Context;



/**
 * Created by hekr_jds on 6/30 0030.
 **/
public class MyApplication extends Application {

    private String EZappKey="42b5ccd6136c4eed858bbe2a66e95d25";

    public static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
    }

    public static MyApplication getAppContext() {
        return (MyApplication) mContext;
    }
}
