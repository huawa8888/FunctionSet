package zoro.test.com.functionset.jsbridge;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

import zoro.test.com.functionset.R;

/**
 * @Author : Zoro.
 * @Date : 2018/5/4.
 * @Describe :功能的实现重点在于assets文件夹下面的WebViewJavascriptBridge.js
 */

public class JSBridgeActivity extends Activity {

    private BridgeWebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jsbridge_activity);
        webView = (BridgeWebView) findViewById(R.id.bridge_WebView);
        webView.setDefaultHandler(new DefaultHandler());
        webView.loadUrl("file:///android_asset/chart.html");
        webView.registerHandler("getData", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack("My name is Zoro");
            }
        });
    }
}
