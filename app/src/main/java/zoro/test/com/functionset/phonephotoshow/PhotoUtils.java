package zoro.test.com.functionset.phonephotoshow;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : Zoro.
 * @Date : 2017/9/19.
 * @Describe :
 */

public class PhotoUtils {
    public static Map<String, PhotoFloder> getPhotos(Context context) {
        Map<String, PhotoFloder> floderMap = new HashMap<>();

        String allPhotosKey = "所有图片";
        PhotoFloder allFloder = new PhotoFloder();
        allFloder.setName(allPhotosKey);
        allFloder.setDirPath(allPhotosKey);
        allFloder.setPhotoList(new ArrayList<Photo>());
        floderMap.put(allPhotosKey, allFloder);

        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();

        // 只查询jpeg和png的图片
        Cursor cursor = contentResolver.query(imageUri, null, MediaStore.Images.Media.MIME_TYPE + " in(?, ?)",
                new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED + " desc");

        int pathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            // 获取图片的路径
            String path = cursor.getString(pathIndex);
            // 获取该图片的父路径名
            File parentFile = new File(path).getParentFile();
            if (parentFile == null) {
                continue;
            }
            String dirPath = parentFile.getAbsolutePath();
            if (floderMap.containsKey(dirPath)) {
                Photo photo = new Photo(path);
                PhotoFloder photoFloder = floderMap.get(dirPath);
                photoFloder.getPhotoList().add(photo);
                floderMap.get(allPhotosKey).getPhotoList().add(photo);
            } else {
                // 初始化imageFloder
                PhotoFloder photoFloder = new PhotoFloder();
                List<Photo> photoList = new ArrayList<Photo>();
                Photo photo = new Photo(path);
                photoList.add(photo);
                photoFloder.setPhotoList(photoList);
                photoFloder.setDirPath(dirPath);
                photoFloder.setName(dirPath.substring(dirPath.lastIndexOf(File.separator) + 1, dirPath.length()));
                floderMap.put(dirPath, photoFloder);
                floderMap.get(allPhotosKey).getPhotoList().add(photo);
            }
        }
        cursor.close();
        return floderMap;
    }

}
