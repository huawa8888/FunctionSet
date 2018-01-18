package zoro.test.com.functionset.yunzhiwu;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zoro.test.com.functionset.R;
import zoro.test.com.functionset.yunzhiwu.zxing.CommonUtil;
import zoro.test.com.functionset.yunzhiwu.zxing.activity.CaptureActivity;

/**
 * @Author : Zoro.
 * @Date : 2017/10/25.
 * @Describe :
 */

public class ScanActivity extends Activity {
    @Bind(R.id.openQrCodeScan)
    Button openQrCodeScan;
    @Bind(R.id.qrCodeText)
    TextView qrCodeText;
    @Bind(R.id.activity_main)
    RelativeLayout activityMain;
    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;

    private String devTid = "";
    private String bindKey = "";

    private String deviceSer = "";
    private String verCode = "";
    private String model = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.openQrCodeScan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openQrCodeScan:
                //打开二维码扫描界面
                if (CommonUtil.isCameraCanUse()) {
                    Intent intent = new Intent(ScanActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    if (PermissionUtils.isCameraPermission(ScanActivity.this, 0x007)) {
                        Intent intent = new Intent(ScanActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0x007:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(ScanActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("qr_scan_result");
            // obj=http://www.hekr.me?action=bind&devTid=cf5e426f2cbf4ddca42f1c44516ca0f4&bindKey=4bd3ac2e11194f39952c32e7d644008c
            //将扫描出的信息显示出来
            //网关(包含http) 摄像头不包含
            if (scanResult.contains("http")) {
                String[] GetWay = scanResult.split("&");
                devTid = GetWay[1].split("=")[1];
                bindKey = GetWay[2].split("=")[1];
                Log.i("", "devTid==" + devTid);
                Log.i("", "devTid==" + bindKey);
            } else {
                String[] GetWay = scanResult.split("\r");
                deviceSer = GetWay[1];
                verCode = GetWay[2];
                model = GetWay[3];
                Log.i("", "devTid==" + deviceSer);
                Log.i("", "devTid==" + verCode);
                Log.i("", "devTid==" + model);
            }
            qrCodeText.setText(scanResult);
        }
    }
}
