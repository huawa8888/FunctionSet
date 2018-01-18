package zoro.test.com.functionset.phonephotoshow;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import zoro.test.com.functionset.R;

public class DetailPhonePhotoActivity extends Activity {
    private final static String TAG = "DetailPhonePhotoActivity";
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.sure)
    TextView sure;
    @Bind(R.id.listView)
    GridView listView;
    @Bind(R.id.tvFloderName)
    TextView tvFloderName;
    @Bind(R.id.tvPhotoNum)
    TextView tvPhotoNum;
    @Bind(R.id.rlTabbarBottom)
    RelativeLayout rlTabbarBottom;
    @Bind(R.id.stubFloder)
    ViewStub stubFloder;

    private PhonePhotoAdapter adapter;
    private ProgressDialog progressDialog;
    private Map<String, PhotoFloder> floderMap;
    private List<Photo> photoLists = new ArrayList<>();
    private List<String> list_Path = new ArrayList<>();

    private final static String ALL_PHOTO = "所有图片";
    boolean isFloderViewInit = false;
    private ListView floderListView;

    /**
     * 文件夹列表是否处于显示状态
     */
    boolean isFloderViewShow = false;

    /**
     * 初始化文件夹列表的显示隐藏动画
     */
    AnimatorSet inAnimatorSet = new AnimatorSet();
    AnimatorSet outAnimatorSet = new AnimatorSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_phone_photo);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getPhotosTask.execute();
    }

    /**
     * 获取照片的异步任务
     */
    private AsyncTask getPhotosTask = new AsyncTask() {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DetailPhonePhotoActivity.this, null, "loading...");
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

    @SuppressLint("LongLogTag")
    private void getPhotosSuccess() {
        progressDialog.dismiss();
        for (Photo index : floderMap.get(ALL_PHOTO).getPhotoList()) {
            index.setChecked(false);
        }
        photoLists.addAll(floderMap.get(ALL_PHOTO).getPhotoList());
        adapter = new PhonePhotoAdapter(photoLists, this);
        listView.setAdapter(adapter);
        adapter.setPathSetListener(listener);
    }

    private PhonePhotoAdapter.PathSetListener listener = new PhonePhotoAdapter.PathSetListener() {
        @Override
        public void addPath(String path) {
            if (list_Path.size() == 9) {
                Toast.makeText(DetailPhonePhotoActivity.this, "已达到照片选择上限", 1).show();
            } else {
                list_Path.add(path);
                sure.setText("确定" + list_Path.size() + "/9");
            }
            if (list_Path.size() > 0) {
                tvPhotoNum.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void reducePath(String path) {
            list_Path.remove(path);
            sure.setText("确定" + list_Path.size() + "/9");
            if (list_Path.size() == 0) {
                tvPhotoNum.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 显示或者隐藏文件夹列表
     *
     * @param floders
     */
    private void toggleFloderList(final List<PhotoFloder> floders) {
        //初始化文件夹列表
        if (!isFloderViewInit) {
            ViewStub floderStub = (ViewStub) findViewById(R.id.stubFloder);
            floderStub.inflate();
            View dimLayout = findViewById(R.id.viewDismiss);
            floderListView = (ListView) findViewById(R.id.lvFloder);
            final FloderAdapter adapter = new FloderAdapter(this, floders);
            floderListView.setAdapter(adapter);
            floderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (PhotoFloder floder : floders) {
                        floder.setIsSelected(false);
                    }
                    PhotoFloder floder = floders.get(position);
                    floder.setIsSelected(true);
                    adapter.notifyDataSetChanged();

                    photoLists.clear();
                    photoLists.addAll(floder.getPhotoList());
                    if (ALL_PHOTO.equals(floder.getName())) {
//                        adapter.setIsShowCamera(isShowCamera);
                    } else {
//                        adapter.setIsShowCamera(false);
                    }
                    //这里重新设置adapter而不是直接notifyDataSetChanged，是让GridView返回顶部
                    listView.setAdapter(adapter);
                    tvPhotoNum.setText(OtherUtils.formatResourceString(getApplicationContext(), R.string.photos_num, photoLists.size()));
                    tvFloderName.setText(floder.getName());
//                    toggle();
                }
            });
            dimLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (isFloderViewShow) {
                        toggle();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            initAnimation(dimLayout);
            isFloderViewInit = true;
        }
        toggle();
    }

    /**
     * 弹出或者收起文件夹列表
     */
    private void toggle() {
        if (isFloderViewShow) {
            outAnimatorSet.start();
            isFloderViewShow = false;
        } else {
            inAnimatorSet.start();
            isFloderViewShow = true;
        }
    }

    private void initAnimation(View dimLayout) {
        ObjectAnimator alphaInAnimator, alphaOutAnimator, transInAnimator, transOutAnimator;
        //获取actionBar的高
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        /**
         * 这里的高度是，屏幕高度减去上、下tab栏，并且上面留有一个tab栏的高度
         * 所以这里减去3个actionBarHeight的高度
         */
        int height = OtherUtils.getHeightInPx(this) - 3 * actionBarHeight;
        alphaInAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0f, 0.7f);
        alphaOutAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0.7f, 0f);
        transInAnimator = ObjectAnimator.ofFloat(floderListView, "translationY", height, 0);
        transOutAnimator = ObjectAnimator.ofFloat(floderListView, "translationY", 0, height);

        LinearInterpolator linearInterpolator = new LinearInterpolator();

        inAnimatorSet.play(transInAnimator).with(alphaInAnimator);
        inAnimatorSet.setDuration(300);
        inAnimatorSet.setInterpolator(linearInterpolator);
        outAnimatorSet.play(transOutAnimator).with(alphaOutAnimator);
        outAnimatorSet.setDuration(300);
        outAnimatorSet.setInterpolator(linearInterpolator);
    }


    @OnClick({R.id.back, R.id.sure, R.id.tvFloderName, R.id.tvPhotoNum})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure:
                //返回数据
                EventBus.getDefault().post(new EB_DetailPhoneToPhonePhoto(list_Path));
                finish();
                break;
            case R.id.tvPhotoNum:
                //跳转到viewpager页面显示预览图片
                Intent intent = new Intent(DetailPhonePhotoActivity.this, ViewPagerPhotoActivity.class);
                intent.putStringArrayListExtra("photopaths", (ArrayList<String>) list_Path);
                startActivity(intent);

                break;
            case R.id.tvFloderName:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(EB_DetailPhoneToPhonePhoto event) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
