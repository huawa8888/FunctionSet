package zoro.test.com.functionset.yunzhiwu.api;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import zoro.test.com.functionset.MyApplication;

/**
 * Created by wukewei on 16/5/26.
 */
public class OkHttpManager {

    private static OkHttpClient mOkHttpClient;
    private static OkHttpClient mOkHttpClient2;

    public static OkHttpClient getInstance() {
        if (mOkHttpClient == null) {
            synchronized (OkHttpManager.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .readTimeout(15, TimeUnit.SECONDS)
                            .addInterceptor(new HttpCacheInterceptor())
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }

    public static OkHttpClient getInstanceNoHead() {
        if (mOkHttpClient2 == null) {
            synchronized (OkHttpManager.class) {
                if (mOkHttpClient2 == null) {
                    mOkHttpClient2 = new OkHttpClient.Builder()
                            .readTimeout(15, TimeUnit.SECONDS)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return mOkHttpClient2;
    }

    static class HttpCacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            String verson = "";
            if (TextUtils.isEmpty(verson)) {
                verson = getShareString("VersonName");
            }
            Request request = chain.request().newBuilder()
                    .addHeader("app-version", verson)
                    .addHeader("test", "1") // 0是外网 1是内网
                    .addHeader("refer", "1").build();
            return chain.proceed(request);
        }
    }

    public static String getShareString(String name) {
        String str = "";
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        if (sp != null) {
            str = sp.getString(name, "");
        }
        return str;
    }


}
