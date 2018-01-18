package zoro.test.com.functionset.phonephotoshow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @Author : Zoro.
 * @Date : 2017/9/21.
 * @Describe :
 */

@SuppressLint("AppCompatCustomView")
public class SquareImageView extends ImageView {

    private int photoSize;

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int screenWidth = OtherUtils.getWidthInPx(context);
        photoSize = (screenWidth - OtherUtils.dip2px(context, 4)) / 3;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(photoSize, photoSize);
    }

}
