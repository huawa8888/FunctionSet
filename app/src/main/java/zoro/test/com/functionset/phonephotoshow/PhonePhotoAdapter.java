package zoro.test.com.functionset.phonephotoshow;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    private PathSetListener mListener;
    private List<String> list_Path = new ArrayList<>();

    public PhonePhotoAdapter(List<Photo> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public void setPathSetListener(PathSetListener listener) {
        mListener = listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.phonephoto_item, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.view);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.checkBox = (ImageView) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(!mList.get(position).getChecked()){
            holder.checkBox.setSelected(false);
        }else{
            holder.checkBox.setSelected(true);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mList.get(position).getChecked()) {
                    list_Path.add(mList.get(position).getPath());
                    if(!(list_Path.size()>9)){
                        mList.get(position).setChecked(true);
                        mListener.addPath(mList.get(position).getPath());
                        holder.checkBox.setSelected(true);
                    }else{
                        Toast.makeText(mContext, "已达到照片选择上限", 1).show();
                    }
                } else {
                    list_Path.remove(mList.get(position).getPath());
                    mList.get(position).setChecked(false);
                    mListener.reducePath(mList.get(position).getPath());
                    holder.checkBox.setSelected(false);
                }
            }
        });
        Glide.with(mContext)
                .load(mList.get(position).getPath())
                .into(holder.image);
        return convertView;

    }

    class ViewHolder {
        LinearLayout layout;
        ImageView image;
        ImageView checkBox;
    }

    public interface PathSetListener {
        void addPath(String path);

        void reducePath(String path);
    }

}
