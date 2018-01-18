package zoro.test.com.functionset.cameraphoto;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import zoro.test.com.functionset.R;


public class ContractIconActivity extends Activity {
    private ImageView imgControl;
    private String url;
    private Bitmap bitmap;

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
        Bundle b = getIntent().getExtras();
        bitmap = (Bitmap) b.getParcelable("bitmap");
        Log.i("", "bitmap==" + (bitmap == null));
        imgControl = (ImageView) findViewById(R.id.img_head);
//        Glide.with(this)
//                .load(url)
//                .into(imgControl);
        imgControl.setImageBitmap(bitmap);
    }

}