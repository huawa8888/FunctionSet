package zoro.test.com.functionset.phonephotoshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import zoro.test.com.functionset.R;

/**
 * Author: walid
 * Date ： 2016/4/28 16:36
 * 图片目录适配器
 */
public class FloderAdapter extends BaseAdapter {

    private List<PhotoFloder> datas;
    private Context context;
    private int photoSize;

    public FloderAdapter(Context context, List<PhotoFloder> mDatas) {
        this.datas = mDatas;
        this.context = context;
        photoSize = OtherUtils.dip2px(context, 90);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_floder_layout, parent, false);
            holder.photoIV = (ImageView) convertView.findViewById(R.id.ivFloder);
            holder.floderNameTV = (TextView) convertView.findViewById(R.id.tvFloderName);
            holder.photoNumTV = (TextView) convertView.findViewById(R.id.tvPhotoNum);
            holder.selectIV = (ImageView) convertView.findViewById(R.id.ivFloderSelect);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.selectIV.setVisibility(View.GONE);
        holder.photoIV.setImageResource(R.drawable.ic_photo_loading);
        PhotoFloder floder = datas.get(position);
        if (floder.isSelected()) {
            holder.selectIV.setVisibility(View.VISIBLE);
        }
        holder.floderNameTV.setText(floder.getName());
        holder.photoNumTV.setText(floder.getPhotoList().size() + "张");
        Glide.with(context)
                .load(floder.getPhotoList().get(0).getPath())
                .override(photoSize,photoSize)
                .into(holder.photoIV);
        return convertView;
    }

    private class ViewHolder {
        private ImageView photoIV;
        private TextView floderNameTV;
        private TextView photoNumTV;
        private ImageView selectIV;
    }

}
