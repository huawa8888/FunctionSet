package zoro.test.com.functionset.yunzhiwu.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/8/20.
 */
public class KernerlClient {

    private KernerlService mService;

    public KernerlClient() {
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl("https://user-openapi.hekr.me/")
                .client(OkHttpManager.getInstance())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit1.create(KernerlService.class);
    }

    public KernerlService getService() {
        return mService;
    }
}
