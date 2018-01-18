package zoro.test.com.functionset.yunzhiwu;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import me.hekr.sdk.Hekr;
import me.hekr.sdk.inter.HekrMsgCallback;

/**
 * @Author : Zoro.
 * @Date : 2017/10/20.
 * @Describe :
 */

public class DeleteDevice {
    public static String BASE_USER_URL = "https://user-openapi.hekr.me/";
    public static final String BIND_DEVICE = "device";

    /**
     * 通过webSocket删除网关下子设备
     *
     * @param randomToken randomToken
     * @param devTid      devTid
     * @param subDevTid   subDevTid
     * @param ctrlKey     ctrlKey
     * @param callback    callback(onReceived中的msg为webSocket返回回来的的code，非完整msg)
     */
    public static void deleteSubDevice(String randomToken, final String devTid, final String subDevTid, final String ctrlKey, final HekrMsgCallback callback) {
        JSONObject deleteSubDeviceObject = new JSONObject();
        JSONObject params = new JSONObject();
        try {
            params.put("devTid", devTid);
            params.put("ctrlKey", ctrlKey);
            params.put("subDevTid", subDevTid);
            if (!TextUtils.isEmpty(randomToken)) {
                params.put("randomToken", randomToken);
            }
            deleteSubDeviceObject.put("action", "devDelete");
            deleteSubDeviceObject.put("params", params);

            Hekr.getHekrClient().sendMessage(devTid, deleteSubDeviceObject, new HekrMsgCallback() {
                @Override
                public void onReceived(String msg) {
                    Log.i("删除子设备", "msg:" + msg);
                    if (!TextUtils.isEmpty(msg)) {
                        try {
                            if (TextUtils.equals(new JSONObject(msg).getString("action"), "devDeleteResp") &&
                                    TextUtils.equals(new JSONObject(msg).getJSONObject("params").getString("devTid"), devTid) &&
                                    TextUtils.equals(new JSONObject(msg).getJSONObject("params").getString("ctrlKey"), ctrlKey) &&
                                    TextUtils.equals(new JSONObject(msg).getJSONObject("params").getString("subDevTid"), subDevTid)
                                    ) {
                                int code = new JSONObject(msg).getInt("code");
                                callback.onReceived(String.valueOf(code));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onTimeout() {

                }

                @Override
                public void onError(int errorCode, String message) {

                }
            });

        } catch (JSONException e) {
        }
    }
    /**
     * 删除设备
     *
     * @param devTid        设备tid
     * @param randomToken   randomToken
     * @param actionAdapter getDataListener
     */
//    public <E> void deleteDevice(final String devTid, String bindKey, final String randomToken, final ActionAdapter<E> actionAdapter) {
//        CharSequence url = TextUtils.concat(ConstantsUtil.UrlUtil.BASE_USER_URL, ConstantsUtil.UrlUtil.BIND_DEVICE, "/", devTid, "?bindKey=", bindKey);
//
//        if (!TextUtils.isEmpty(randomToken)) {
//            url = TextUtils.concat(url, "&randomToken=", randomToken).toString();
//        }
//        BaseHttpUtils.deleteData(weakTag, url.toString()).execute(new BaseStringTokenCallBack(actionAdapter) {
//            @Override
//            public void onSuccess(String s, Call call, Response response) {
//                String randomKey = null;
//                if (!TextUtils.isEmpty(s)) {
//                    if (JSONObject.parseObject(s).containsKey("randomKey")) {
//                        randomKey = JSONObject.parseObject(s).getString("randomKey");
//                    }
//                } else {
//                    devUtil.removeDevice(devTid, "", new AbstractCacheUtil.ExecuteResult<HekrNewDeviceCache>() {
//                        @Override
//                        public void onSuccess(List<Serializable> e) {
//
//                        }
//
//                        @Override
//                        public void onFail(Throwable error) {
//
//                        }
//                    });
//
//                    cDevUtil.removeCDev(devTid, "", new AbstractCacheUtil.ExecuteResult<HekrCommonDeviceCache>() {
//                        @Override
//                        public void onSuccess(List<Serializable> e) {
//
//                        }
//
//                        @Override
//                        public void onFail(Throwable error) {
//
//                        }
//                    });
//                }
//                actionAdapter.next((E) randomKey);
//            }
//        });
//    }
}
