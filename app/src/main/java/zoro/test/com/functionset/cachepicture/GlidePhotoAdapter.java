package zoro.test.com.functionset.cachepicture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import zoro.test.com.functionset.R;


/**
 * @Author : Zoro.
 * @Date : 2017/8/21.
 * @Describe :
 */

public class GlidePhotoAdapter extends BaseAdapter {

    private List<String> list = new ArrayList<>();
    private Context context;

    public GlidePhotoAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.photo_layout, null);
        } else {
            view = convertView;
        }
        ImageView photo = (ImageView) view.findViewById(R.id.photo);
        Glide.with(context)
                .load(list.get(position))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .into(photo);
        return view;
    }

}
