package zoro.test.com.functionset.cameraphoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import zoro.test.com.functionset.R;

public class CameraPhotoActivity extends Activity {

    private Button on_camera;
    private ImageView iv_photo;
    private Button on_camera_big;
    private ImageView iv_photo_big;

    // 返回码
    private static final int CODE = 1;
    private static final int CODEBIG = 2;

    // 记录文件保存位置
    private String mFilePath;
    private FileInputStream is = null;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_photo);

        // 获取SD卡路径
        mFilePath = Environment.getExternalStorageDirectory().getPath();
        // 文件名
        mFilePath = mFilePath + "/" + "photo.png";

        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        iv_photo_big = (ImageView) findViewById(R.id.iv_photo_big);
        on_camera = (Button) findViewById(R.id.on_camera);
        on_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /**
                 * 我们使用Intent的方式打开系统相机
                 * 1.直接跳转相机包名，前提是你知道这个应用的包名
                 * 2.就是使用隐式Intent了，在这里我们就使用隐式intent
                 */
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 指定拍照
                // 拍照返回图片
                startActivityForResult(intent, CODE);
            }
        });
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraPhotoActivity.this, ContractIconActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("bitmap", bitmap);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        on_camera_big = (Button) findViewById(R.id.on_camera_big);
        on_camera_big.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 指定拍照
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 加载路径
                Uri uri = Uri.fromFile(new File(mFilePath));
                // 指定存储路径，这样就可以保存原图了
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 拍照返回图片
                startActivityForResult(intent, CODEBIG);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // 判断是否返回值
        if (resultCode == RESULT_OK) {
            // 判断返回值是否正确
           if (requestCode == CODEBIG) {
//                    // 获取输入流
//                    is = new FileInputStream(mFilePath);
//                    // 把流解析成bitmap
//                    bitmap = BitmapFactory.decodeStream(is);
                bitmap = BitmapFactory.decodeFile(mFilePath);
                // 设置图片
                iv_photo_big.setImageBitmap(bitmap);
                try {
                    FileOutputStream out = new FileOutputStream(mFilePath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        }
    }
}

