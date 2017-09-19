package zoro.test.com.functionset.cachepicture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import zoro.test.com.functionset.R;


/**
 * @Author : Zoro.
 * @Date : 2017/8/16.
 * @Describe :
 */

public class ListViewActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> list = new ArrayList<>();
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        listView = (ListView) findViewById(R.id.listView);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 30; i++) {
            list.add("HAHA" + i);
        }
        adapter = new ListViewAdapter(this, list);
        listView.setAdapter(adapter);
    }
}
