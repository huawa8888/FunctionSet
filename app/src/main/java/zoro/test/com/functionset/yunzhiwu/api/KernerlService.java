package zoro.test.com.functionset.yunzhiwu.api;

import com.google.gson.JsonObject;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/20.
 */
public interface KernerlService {

    @GET("getPINCode?")
    Observable<JsonObject> getPinCode(@Query("ssid") String ssid);

    @DELETE("device/{devTid}?")
    Observable<JsonObject> deleteWifiDevice(@Path("devTid") String devTid, @Query("bindKey") String bindKey);

    @DELETE("device/delSubDevice?")
    Observable<JsonObject> deleteSubDevice(@Query("devTid") String devTid, @Query("ctrlKey") String ctrlKey,@Query("subDevTid") String subDevTid);

}
