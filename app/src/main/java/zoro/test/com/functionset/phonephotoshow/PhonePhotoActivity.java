package zoro.test.com.functionset.phonephotoshow;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import zoro.test.com.functionset.R;

public class PhonePhotoActivity extends AppCompatActivity {

    private final static String TAG = "PhonePhotoActivity";

    private PhonePhotoAdapter adapter;
    private ListView listView;
    private Button button;
    private ProgressDialog progressDialog;
    private Map<String, PhotoFloder> floderMap;
    private List<Photo> photoLists = new ArrayList<>();


    private final static String ALL_PHOTO = "所有图片";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_photo);
        init();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.getpath);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotosTask.execute();
            }
        });
    }

    /**
     * 获取照片的异步任务
     */
    private AsyncTask getPhotosTask = new AsyncTask() {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PhonePhotoActivity.this, null, "loading...");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            floderMap = PhotoUtils.getPhotos(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            getPhotosSuccess();
        }
    };

    private void getPhotosSuccess() {
        progressDialog.dismiss();
        photoLists.addAll(floderMap.get(ALL_PHOTO).getPhotoList());
        Log.i(TAG, "photoLists==" + photoLists.toString());
        adapter = new PhonePhotoAdapter(photoLists, this);
        listView.setAdapter(adapter);
    }
}
