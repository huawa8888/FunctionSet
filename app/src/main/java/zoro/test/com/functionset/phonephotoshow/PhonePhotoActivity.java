package zoro.test.com.functionset.phonephotoshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import zoro.test.com.functionset.R;

public class PhonePhotoActivity extends Activity {

    private final static String TAG = "PhonePhotoActivity";

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_photo);
        EventBus.getDefault().register(this);
        init();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(EB_DetailPhoneToPhonePhoto event) {
        Log.i("", "event===" + event.toString());
    }


    private void init() {
        button = (Button) findViewById(R.id.getpath);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhonePhotoActivity.this, DetailPhonePhotoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
