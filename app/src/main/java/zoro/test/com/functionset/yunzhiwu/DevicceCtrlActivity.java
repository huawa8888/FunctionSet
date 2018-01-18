package zoro.test.com.functionset.yunzhiwu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.hekr.sdk.ConfigType;
import me.hekr.sdk.Hekr;
import me.hekr.sdk.IConfig;
import me.hekr.sdk.http.HekrRawCallback;
import me.hekr.sdk.inter.HekrCallback;
import me.hekr.sdk.inter.HekrConfigDeviceListener;
import me.hekr.sdk.inter.HekrMsgCallback;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zoro.test.com.functionset.R;
import zoro.test.com.functionset.yunzhiwu.api.KernerlService;


/**
 * @Author : Zoro.
 * @Date : 2017/10/19.
 * @Describe :
 */

public class DevicceCtrlActivity extends Activity {

    //网关参数：
    private String devTid = "cf5e426f2cbf4ddca42f1c44516ca0f4";
    private String bindKey = "4bd3ac2e11194f39952c32e7d644008c";
    private String ctrlKey = "414f35fddfb7436ea93b2dc0cd18b91f";

    private String devTid_hongwai = "00124b000dc0306f";
    private String devTid_menci = "00124b000880f419";

    //红外插座
    private String devTid_chazuo = "ESP_2M_5CCF7F0E5F5B";
    private String bindKey_chazuo = "4c10c67a5ebc43299ea577705c6d0248";
    private String ctrlKey_chazuo = "0f6ef3c3bdba4ca59c48817fa7c60855";


    private KernerlService mService;

    private HashMap<String, String> configParam;
    //成功绑定设备数量
    private int connectSuccessDeviceCount = 0;

    private String pinCode = "";

    private String token = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicecrtl);
        ButterKnife.bind(this);
        configParam = new HashMap<>();
    }

    @OnClick({R.id.login, R.id.bindGetWay, R.id.bindGetWaySubDevice, R.id.deleteGetWaySubDevice, R.id.deleteGetWay, R.id.bindWifiDevice, R.id.logout, R.id.getPinCode, R.id.deleteWifiDevice, R.id.openSocket, R.id.closeSocket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                Hekr.getHekrUser().login("15757870997", "123456", new HekrCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DevicceCtrlActivity.this, "Login Sucess!", Toast.LENGTH_LONG).show();
                        Log.i("", "token==" + Hekr.getHekrUser().getToken());
                    }

                    @Override
                    public void onError(int errorCode, String message) {
                        Toast.makeText(DevicceCtrlActivity.this, "Login Fail!", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.bindGetWay:
                connectSuccessDeviceCount = 0;
                configParam.clear();
                configParam.put("devTid", devTid);
                configParam.put("bindKey", bindKey);
                IConfig iConfig = Hekr.getHekrConfig().getConfig(ConfigType.GATEWAY_DEV);
                //1.判断是否绑定,
                iConfig.startConfig(this, configParam, new HekrConfigDeviceListener() {
                    @Override
                    public void getNewDevice(JSONObject newDeviceBean) {
                        Log.i("", "newDeviceBean==" + newDeviceBean.toString());
                        Toast.makeText(DevicceCtrlActivity.this, "Bind GetWay Success!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void getDeviceSuccess() {
                    }

                    @Override
                    public void getDeviceFail(int errorCode, String message) {
                        Toast.makeText(DevicceCtrlActivity.this, "Bind GetWay Fail!", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.deleteGetWay:
                mService = HttpUtils.deleteDevice();
                mService.deleteWifiDevice(devTid, bindKey)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<JsonObject>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i("", "Throwable==" + e.getMessage().toString());
                                Toast.makeText(DevicceCtrlActivity.this, "网关解绑失败!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNext(JsonObject jsonObject) {
                                Toast.makeText(DevicceCtrlActivity.this, "网关解绑成功!", Toast.LENGTH_LONG).show();
                                Log.i("", "jsonObject==" + jsonObject);
                            }
                        });
                break;
            //门磁传感器：01557a76e5d2
            //人体红外传感器：014b58bb6edc
            case R.id.bindGetWaySubDevice:
                connectSuccessDeviceCount = 0;
                configParam.clear();
                configParam.put("devTid", devTid);
                configParam.put("ctrlKey", ctrlKey);
                configParam.put("subMid", "01557a76e5d2");
                IConfig iConfigs = Hekr.getHekrConfig().getConfig(ConfigType.GATEWAY_SUB_DEV);
                Hekr.getHekrClient().receiveMessage(null, new HekrMsgCallback() {
                    @Override
                    public void onReceived(String msg) {
                        msg.contains("devSend");

                        // received message
                    }

                    @Override
                    public void onTimeout() {
                        // timeout
                    }

                    @Override
                    public void onError(int errorCode, String message) {

                    }
                });
                // Refresh Token
                Hekr.getHekrUser().refreshToken(new HekrRawCallback() {
                    @Override
                    public void onSuccess(int httpCode, byte[] bytes) {
                        // You can get new token here

                        Hekr.getHekrUser().getToken();
                    }

                    @Override
                    public void onError(int httpCode, byte[] bytes) {
                        // Logout when get error
                    }
                });
                if (Hekr.getHekrClient().isOnline()) {
                    iConfigs.startConfig(DevicceCtrlActivity.this, configParam, new HekrConfigDeviceListener() {
                        @Override
                        public void getNewDevice(JSONObject device) {
                            Log.i("", "子设备配网命令返回值:" + device.toString());
                        }

                        @Override
                        public void getDeviceSuccess() {
                            Toast.makeText(DevicceCtrlActivity.this, "子设备绑定成功!", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void getDeviceFail(int error, String errorMsg) {
                            Log.i("", "子设备配网命令返回值:超时");
                        }
                    });
                } else {
                    Toast.makeText(DevicceCtrlActivity.this, "当前网络不可用!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.deleteGetWaySubDevice:
                deleteSubDevice();
                break;
            case R.id.bindWifiDevice:
                mService = HttpUtils.getPinCode();
                mService.getPinCode("zje_office")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<JsonObject>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(DevicceCtrlActivity.this, "网络访问失败!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNext(JsonObject jsonObject) {
                                Toast.makeText(DevicceCtrlActivity.this, "网络访问成功!", Toast.LENGTH_LONG).show();
                                pinCode = jsonObject.get("PINCode").toString().substring(1, 7);
                                Log.i("", "jsonObject==" + pinCode);
                                configParam.clear();
                                configParam.put("pinCode", pinCode);
                                configParam.put("ssid", "zje_office");
                                configParam.put("password", "zje360wifi!#");
                                IConfig iConfigss = Hekr.getHekrConfig().getConfig(ConfigType.COMMON_DEV);
                                iConfigss.startConfig(DevicceCtrlActivity.this, configParam, new HekrConfigDeviceListener() {
                                    @Override
                                    public void getNewDevice(JSONObject newDeviceBean) {
                                        Toast.makeText(DevicceCtrlActivity.this, "绑定Wi-Fi成功!", Toast.LENGTH_LONG).show();

                                        Log.i("", "newDeviceBean==" + newDeviceBean.toString());
                                    }

                                    @Override
                                    public void getDeviceSuccess() {
                                    }

                                    @Override
                                    public void getDeviceFail(int errorCode, String message) {
                                        Log.i("", "message==" + message.toString());
                                        Toast.makeText(DevicceCtrlActivity.this, "绑定Wi-Fi失败!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                break;
            case R.id.deleteWifiDevice:
                mService = HttpUtils.deleteDevice();
                mService.deleteWifiDevice(devTid_chazuo, bindKey_chazuo)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<JsonObject>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i("", "Throwable==" + e.getMessage().toString());
                                Toast.makeText(DevicceCtrlActivity.this, "插座解绑失败!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNext(JsonObject jsonObject) {
                                Toast.makeText(DevicceCtrlActivity.this, "插座解绑成功!", Toast.LENGTH_LONG).show();
                                Log.i("", "jsonObject==" + jsonObject);
                            }
                        });
                break;
            case R.id.openSocket:
                controlSocket(0);
                break;

            case R.id.closeSocket:
                controlSocket(1);
                break;

            case R.id.logout:
                Hekr.getHekrUser().logout(new HekrCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DevicceCtrlActivity.this, "退出成功!", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(int errorCode, String message) {
                        Toast.makeText(DevicceCtrlActivity.this, "退出失败!", Toast.LENGTH_LONG).show();
                    }
                });
//                Hekr.getHekrClient().deceiveMessage();
                break;

            case R.id.getPinCode:
                mService = HttpUtils.getPinCode();
                mService.getPinCode("zje_office")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<JsonObject>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(DevicceCtrlActivity.this, "网络访问失败!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNext(JsonObject jsonObject) {
                                Toast.makeText(DevicceCtrlActivity.this, "网络访问成功!", Toast.LENGTH_LONG).show();
                                pinCode = jsonObject.get("PINCode").toString();
                                Log.i("", "jsonObject==" + pinCode);
                            }
                        });
                break;
        }
    }

    private void controlSocket(int status) {
        JSONObject openSocket = new JSONObject();
        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("cmdId", 20);
            data.put("Power", status);
            params.put("data", data);
            params.put("devTid", devTid_chazuo);
            params.put("ctrlKey", ctrlKey_chazuo);
            openSocket.put("action", "appSend");
            openSocket.put("params", params);
            Hekr.getHekrClient().sendMessage(devTid, openSocket, new HekrMsgCallback() {
                @Override
                public void onReceived(String msg) {
                    Log.i("", "msg===" + msg);
                }

                @Override
                public void onTimeout() {
                }

                @Override
                public void onError(int errorCode, String message) {
                    Log.i("", "message===" + message);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void deleteSubDevice() {
        JSONObject deleteSubDeviceObject = new JSONObject();
        JSONObject params = new JSONObject();

        try {
            params.put("devTid", devTid);
            params.put("ctrlKey", ctrlKey);
            params.put("subDevTid", devTid_menci);
            deleteSubDeviceObject.put("action", "devDelete");
            deleteSubDeviceObject.put("params", params);
            Hekr.getHekrClient().sendMessage(devTid, deleteSubDeviceObject, new HekrMsgCallback() {
                @Override
                public void onReceived(String msg) {
                    Toast.makeText(DevicceCtrlActivity.this, "子设备解绑成功!", Toast.LENGTH_LONG).show();
                    Log.i("", "msg===" + msg);
                }

                @Override
                public void onTimeout() {
                    Toast.makeText(DevicceCtrlActivity.this, "子设备解绑超时!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(int errorCode, String message) {
                    Log.i("", "message===" + message);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过webSocket删除网关下子设备     *
     * * @param randomToken randomToken
     * * @param devTid      devTid
     * * @param subDevTid   subDevTid
     * * @param ctrlKey     ctrlKey
     * * @param callback    callback(onReceived中的msg为webSocket返回回来的的code，非完整msg)
     */
    public void deleteSubDevice(String randomToken, final String devTid, final String subDevTid, final String ctrlKey, final HekrMsgCallback callback) {
        JSONObject deleteSubDeviceObject = new JSONObject();
        JSONObject params = new JSONObject();
        try {
            params.put("devTid", devTid);
            params.put("ctrlKey", ctrlKey);
            params.put("subDevTid", devTid_menci);
            if (!TextUtils.isEmpty(randomToken)) {
                params.put("randomToken", randomToken);
            }
            deleteSubDeviceObject.put("action", "devDelete");
            deleteSubDeviceObject.put("params", params);
            Hekr.getHekrClient().sendMessage(devTid, deleteSubDeviceObject, new HekrMsgCallback() {
                @Override
                public void onReceived(String msg) {
                    Toast.makeText(DevicceCtrlActivity.this, "子设备解绑成功!", Toast.LENGTH_LONG).show();
                    Log.i("删除子设备", "msg:" + msg);
                    if (!TextUtils.isEmpty(msg)) {
                        try {
                            if (TextUtils.equals(new JSONObject(msg).getString("action"), "devDeleteResp") && TextUtils.equals(new JSONObject(msg).getJSONObject("params").getString("devTid"), devTid) && TextUtils.equals(new JSONObject(msg).getJSONObject("params").getString("ctrlKey"), ctrlKey) && TextUtils.equals(new JSONObject(msg).getJSONObject("params").getString("subDevTid"), subDevTid)) {
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
                    Toast.makeText(DevicceCtrlActivity.this, "子设备解绑超时!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(int errorCode, String message) {
                    Toast.makeText(DevicceCtrlActivity.this, "子设备解绑失败!", Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {

        }
    }
}


/**
 * 删除网关下面的子设备
 */
//    private void deleteSubDevice() {
//        JSONObject deleteSubDeviceObject = new JSONObject();
//        JSONObject params = new JSONObject();
//
//        try {
//            params.put("devTid", devTid);
//            params.put("ctrlKey", ctrlKey);
//            params.put("subDevTid", devTid_hongwai);
//            deleteSubDeviceObject.put("action", "devDelete");
//            deleteSubDeviceObject.put("params", params);
//            MsgUtil.sendMsg(DevicceCtrlActivity.this, devTid, deleteSubDeviceObject, new DataReceiverListener() {
//                @Override
//                public void onReceiveSuccess(String msg) {
//                    Log.i("", "devicesLists==" + msg);
//                }
//
//                @Override
//                public void onReceiveTimeout() {
//
//                }
//            }, true);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    private void deleteGetWay() {

//        JSONObject deleteSubDeviceObject = new JSONObject();
//        JSONObject params = new JSONObject();
//
//        try {
//            params.put("devTid", devTid);
//            params.put("ctrlKey", ctrlKey);
//            deleteSubDeviceObject.put("action", "devDelete");
//            deleteSubDeviceObject.put("params", params);
//            MsgUtil.sendMsg(DevicceCtrlActivity.this, devTid,, deleteSubDeviceObject, new DataReceiverListener() {
//                @Override
//                public void onReceiveSuccess(String msg) {
//                    Log.i("", "devicesLists==" + msg);
//                }
//
//                @Override
//                public void onReceiveTimeout() {
//
//                }
//            }, true);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//                Toast.makeText(DevicceCtrlActivity.this, " Bind Start!", Toast.LENGTH_LONG).show();
//                smartConfig.startConfig("zje_ofice", "zje360wifi!#", 60, new SmartConfig.NewDeviceListener() {
//                    //单次配网时间内查询到的所有新设备(回调每次查询到的新设备)
//                    @Override
//                    public void getDeviceList(List<NewDeviceBean> newDeviceList) {
//                        Toast.makeText(DevicceCtrlActivity.this, "Bind Success1!", Toast.LENGTH_LONG).show();
//                    }
//
//                    //单次配网时间内查询到的新设备(一旦有新的设备就会触发该回调接口)
//                    //只有newDeviceBean中属性bindResultCode值为0才算真正将该设备绑定到了自己账号下
//                    @Override
//                    public void getNewDevice(NewDeviceBean newDeviceBean) {
//                        Log.i("", "devicesLists==" + newDeviceBean.toString());
//                        if ("0".equals(newDeviceBean.getBindResultCode())) {
//                            Toast.makeText(DevicceCtrlActivity.this, "Bind Success!", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    //单次配网时间内查到新设备
//                    @Override
//                    public void getDeviceSuccess() {
//                        Toast.makeText(DevicceCtrlActivity.this, "Bind Success2!", Toast.LENGTH_LONG).show();
//
//                    }
//
//                    //单次配网时间内未查询到任何新设备
//                    @Override
//                    public void getDeviceFail() {
//                        Toast.makeText(DevicceCtrlActivity.this, "Bind Success3!", Toast.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public void getPinCodeFail() {
//                        Toast.makeText(DevicceCtrlActivity.this, "Bind Success4!", Toast.LENGTH_LONG).show();
//
//                    }
//                });


//    public void startConfigGateWaySubDevice(Context context, String devTid, String
//            ctrlKey, Object subDevTid, int cmdId, String subMid, int overtime,
//                                            DataReceiverListener dataReceiverListener) {
//
//        JSONObject jsonObject = new JSONObject();
//        JSONObject params = new JSONObject();
//        JSONObject data = new JSONObject();
//        try {
//            data.put("cmdId", cmdId);
//            data.put("subMid", subMid);
//            data.put("overtime", overtime);
//
//            params.put("devTid", devTid);
//            params.put("ctrlKey", ctrlKey);
//        /*params.put("subDevTid","");*/
//            params.put("data", data);
//
//            jsonObject.put("action", "appSend");
//            jsonObject.put("params", params);
//
//            MsgUtil.sendMsg(context, devTid, jsonObject, dataReceiverListener, true);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


//                            Log.i(TAG, "子设备配网命令返回值:" + device.toString());
//
//                            SubDeviceConfigBean subDeviceConfigBean = JSON.parseObject(device.toString(), SubDeviceConfigBean.class);
//
//                            if (subDeviceConfigBean != null &&
//                                    subDeviceConfigBean.getParams() != null &&
//                                    !TextUtils.isEmpty(subDeviceConfigBean.getParams().getSubMid()) &&
//                                    !TextUtils.isEmpty(subDeviceConfigBean.getParams().getDevTid())) {
//                                if (TextUtils.equals(subDeviceConfigBean.getParams().getSubMid(), product.getMid()) &&
//                                        TextUtils.equals(subDeviceConfigBean.getParams().getDevTid(), gateWayDevice.getDevTid())) {
//
//                                    //CommonDeviceBean newDeviceBean = new CommonDeviceBean();
//                                    //newDeviceBean.setLogo(product.getLogo());
//                                    //newDeviceBean.setBindResultCode(0);
//
//                                    //构建新的网关子设备
//                                    CommonDeviceBean newDevice = new CommonDeviceBean();
//
//                                    newDevice.setDevTid(subDeviceConfigBean.getParams().getSubDevTid());
//                                    newDevice.setParentDevTid(subDeviceConfigBean.getParams().getDevTid());
//                                    newDevice.setParentCtrlKey(gateWayDevice.getCtrlKey());
//                                    newDevice.setDevType("SUB");
//                                    newDevice.setOnline(true);
//                                    if (product!=null&&
//                                            product.getAppDisplayName()!=null&&
//                                            !TextUtils.isEmpty(product.getAppDisplayName().getZh_CN())) {
//                                        newDevice.setDeviceName(product.getAppDisplayName().getZh_CN());
//                                    } else {
//                                        newDevice.setDeviceName(product.getName());
//                                    }
//
//                                    newDevice.setLogo(product.getLogo());
//                                    newDevice.setBindResultCode(0);
//                                    //newDevice.setGateWaySubDeviceBean(gateWaySubDeviceBean);
//
//                                    EventBus.getDefault().postSticky(new RefreshEvent(RefreshEvent.REFRESH_DEVICE, RefreshEvent.ADD_DEVICE_REFRESH, hashMap));
//
//                                    successConfigDevice.add(newDevice);
//                                    connectSuccessDeviceCount = successConfigDevice.size();
//                                    if (connectSuccessDeviceCount > 0) {
//                                        config_tip_tv.setText(new StringBuilder().append(connectSuccessDeviceCount).append(getString(R.string.activity_device_link_connect_device_number_tip)));
//                                    }
//                                    m_Data.add(newDevice);
//                                    mRecyclerView.setVisibility(View.VISIBLE);
//                                    btnSwitch(UI_STATUS_BACK_TO_HOME_VISIBLE, true);
//                                    mAdapter.notifyDataSetChanged();
//                                }
//                            }
