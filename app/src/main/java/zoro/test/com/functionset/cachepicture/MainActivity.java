package zoro.test.com.functionset.cachepicture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import zoro.test.com.functionset.R;


public class MainActivity extends AppCompatActivity {

    private ListView gridView;
    private PhotoWallAdapter adapter;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initData();
        initView();
    }

    private void initData() {
        for(int i=0;i< Images.imageThumbUrls.length;i++){
            list.add(Images.imageThumbUrls[i]);
        }
    }

    private void initView() {
        gridView = (ListView) findViewById(R.id.photo_wall);
        adapter = new PhotoWallAdapter(this, 0, Images.imageThumbUrls,gridView);
        gridView.setAdapter(adapter);
    }

}
