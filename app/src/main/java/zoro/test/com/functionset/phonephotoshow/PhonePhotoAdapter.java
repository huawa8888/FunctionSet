package zoro.test.com.functionset.phonephotoshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import zoro.test.com.functionset.R;

/**
 * @Author : Zoro.
 * @Date : 2017/9/19.
 * @Describe :
 */

public class PhonePhotoAdapter extends BaseAdapter {

    private List<Photo> mList = new ArrayList<>();
    private Context mContext;

    public PhonePhotoAdapter(List<Photo> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.phonephoto_item, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.view);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext)
                .load(mList.get(position).getPath())
                .into(holder.image);
        return convertView;

    }
    class ViewHolder {
        LinearLayout layout;
        ImageView image;
    }

}
