package com.walid.photopicker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.walid.photopicker.utils.ImageLoader;
import com.walid.photopicker.utils.OtherUtils;


public class ContractIconActivity extends Activity {
    private ImageView imgControl;
    private String url;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_contract_icon);
        url = getIntent().getStringExtra("url");
        imgControl = (ImageView) findViewById(R.id.img_head);
        int screenWidth = OtherUtils.getWidthInPx(getApplicationContext());
        ImageLoader.getInstance(getApplication()).display(url, imgControl,screenWidth,screenWidth);
//        imgControl.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
//
//            @Override
//            public void onViewTap(View arg0, float arg1, float arg2) {
//                    finish();
//            }
//        });
    }

}