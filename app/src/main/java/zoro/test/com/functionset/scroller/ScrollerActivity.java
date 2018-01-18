package zoro.test.com.functionset.scroller;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import zoro.test.com.functionset.R;

/**
 * @Author : Zoro.
 * @Date : 2017/9/19.
 * @Describe :
 */

public class ScrollerActivity extends Activity {
    private Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        DisplayMetrics dm1 = getResources().getDisplayMetrics();
        int h = dm.heightPixels;
        int w = dm.widthPixels;
        int h1 = dm1.heightPixels;
        int w1 = dm1.widthPixels;
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ScrollerActivity.this,"短按",1).show();
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ScrollerActivity.this,"长按",1).show();
                return true;
            }
        });
    }
}
