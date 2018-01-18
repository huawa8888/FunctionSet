package zoro.test.com.functionset.phonephotoshow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import zoro.test.com.functionset.R;
import zoro.test.com.functionset.phonephotoshow.photoview.PhotoView;
import zoro.test.com.functionset.phonephotoshow.photoview.PhotoViewAttacher;

public class ViewPagerPhotoActivity extends Activity {

    private static String TAG = "ViewPagerPhotoActivity";

    @Bind(R.id.viewpager)
    ViewPager viewpager;

    //PhotoView是自定义View，关联文件在photoview包中，然后记得compile 'com.facebook.fresco:fresco:0.5.2'
    private PhotoView photoViewZ;
    private LinearLayout linearLayout;

    private SamplePagerAdapter samplePagerAdapter;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_photo);
        ButterKnife.bind(this);
        list = getIntent().getStringArrayListExtra("photopaths");
        samplePagerAdapter = new SamplePagerAdapter();
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(samplePagerAdapter);
        viewpager.setCurrentItem(1);
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            LayoutInflater inflater=getLayoutInflater();
            linearLayout = (LinearLayout) inflater.inflate(R.layout.photo_item,null);
            photoViewZ = (PhotoView) linearLayout.findViewById(R.id.photoView);
            Glide.with(ViewPagerPhotoActivity.this)
                    .load(list.get(position))
                    .into(photoViewZ);

            photoViewZ.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                }
            });

            container.addView(linearLayout, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            return linearLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
