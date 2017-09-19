package zoro.test.com.functionset.cachepicture;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import zoro.test.com.functionset.R;
import zoro.test.com.functionset.io.DiskLruCache;


/**
 * @Author : Zoro.
 * @Date : 2017/8/21.
 * @Describe :
 */

public class PhotoWallAdapter extends ArrayAdapter<String> implements AbsListView.OnScrollListener {

    /**
     * 记录所有正在下载或等待下载的任务。
     */
    private Set<BitmapWorkerTask> taskCollection;

    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private LruCache<String, Bitmap> mMemoryCache;

    /**
     * GridView的实例
     */
    private ListView mPhotoWall;

    /**
     * 第一张可见图片的下标
     */
    private int mFirstVisibleItem;

    /**
     * 一屏有多少张图片可见
     */
    private int mVisibleItemCount;

    /**
     * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
     */
    private boolean isFirstEnter = true;

    /**
     * 硬盘缓存
     */
    private DiskLruCache mDiskLruCache;

    /**
     * 磁盘分配大小
     */
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;

    /**
     * 储存下标
     */
    private static final int DISK_CACHE_INDEX = 0;


    public PhotoWallAdapter(Context context, int textViewResourceId, String[] objects,
                            ListView photoWall) {
        super(context, textViewResourceId, objects);
        mPhotoWall = photoWall;
        taskCollection = new HashSet<BitmapWorkerTask>();
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        // 设置图片缓存大小为程序最大可用内存的1/8
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
//        File disklrucache = getDiskCacheDir(context, "bitmap");
//        if (!disklrucache.exists()) {
//            disklrucache.mkdirs();
//        }
//        try {
//            mDiskLruCache = DiskLruCache.open(disklrucache, 1, 1, DISK_CACHE_SIZE);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        mPhotoWall.setOnScrollListener(this);
    }

    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String url = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.photo_layout, null);
        } else {
            view = convertView;
        }
        final ImageView photo = (ImageView) view.findViewById(R.id.photo);
        // 给ImageView设置一个Tag，保证异步加载图片时不会乱序
        photo.setTag(url);
        setImageView(url, photo);
        return view;
    }

    /**
     * 给ImageView设置图片。首先从LruCache中取出图片的缓存，设置到ImageView上。如果LruCache中没有该图片的缓存，
     * 就给ImageView设置一张默认图片。
     *
     * @param imageUrl  图片的URL地址，用于作为LruCache的键。
     * @param imageView 用于显示图片的控件。
     */
    private void setImageView(String imageUrl, ImageView imageView) {
        Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher_round);
        }
    }

    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key    LruCache的键，这里传入图片的URL地址。
     * @param bitmap LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的Bitmap对象，或者null。
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
        if (scrollState == SCROLL_STATE_IDLE) {
            loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
        } else {
            cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        // 下载的任务应该由onScrollStateChanged里调用，但首次进入程序时onScrollStateChanged并不会调用，
        // 因此在这里为首次进入程序开启下载任务。
        if (isFirstEnter && visibleItemCount > 0) {
            loadBitmaps(firstVisibleItem, visibleItemCount);
            isFirstEnter = false;
        }
    }

    /**
     * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
     * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
     *
     * @param firstVisibleItem 第一个可见的ImageView的下标
     * @param visibleItemCount 屏幕中总共可见的元素数
     */
    //手动添加缓存
//    private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
//        try {
//            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
//                String imageUrl = Images.imageThumbUrls[i];
//                Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
//                Log.i("", "bitmap1==" + (bitmap == null));
//                if (bitmap == null) {
//                    ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imageUrl);
//                    try {
//                        String key = hashKeyForDisk(imageUrl);
//                        DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
//                        Log.i("","snapShot=="+(snapShot != null));
//                        if (snapShot != null) {
//                            InputStream is = snapShot.getInputStream(0);
//                            Bitmap bitmap1 = BitmapFactory.decodeStream(is);
//                            if (bitmap1 != null) {
//                                imageView.setImageBitmap(bitmap1);
//                            } else {
//                                BitmapWorkerTask task = new BitmapWorkerTask();
//                                taskCollection.add(task);
//                                task.execute(imageUrl);
//                            }
//                        } else {
//                            imageView.setImageResource(R.mipmap.ic_launcher_round);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imageUrl);
//                    if (imageView != null && bitmap != null) {
//                        imageView.setImageBitmap(bitmap);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        try {
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
                String imageUrl = Images.imageThumbUrls[i];
                Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
                if (bitmap == null) {
                    BitmapWorkerTask task = new BitmapWorkerTask();
                    taskCollection.add(task);
                    task.execute(imageUrl);
                } else {
                    ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imageUrl);
                    if (imageView != null && bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public void cancelAllTasks() {
        if (taskCollection != null) {
            for (BitmapWorkerTask task : taskCollection) {
                task.cancel(true);
            }
        }
    }
    /**
     * 异步下载图片的任务。
     *
     * @author guolin
     */
    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        /**
         * 图片的URL地址
         */
        private String imageUrl;

        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];
            // 在后台开始下载图片
            Bitmap bitmap = downloadBitmap(params[0]);
            if (bitmap != null) {
                // 图片下载完成后缓存到LrcCache中
                addBitmapToMemoryCache(params[0], bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
            ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imageUrl);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            Log.i("","Bitmap===="+(bitmap==null));
            taskCollection.remove(this);
        }

        /**
         * 建立HTTP请求，并获取Bitmap对象。
         *
         * @param imageUrl
         *            图片的URL地址
         * @return 解析后的Bitmap对象
         */
        private Bitmap downloadBitmap(String imageUrl) {
            Bitmap bitmap = null;
            HttpURLConnection con = null;
            try {
                URL url = new URL(imageUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(10 * 1000);
                bitmap = BitmapFactory.decodeStream(con.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return bitmap;
        }

    }

//    /**
//     * 异步下载图片的任务。
//     *
//     * @author guolin
//     */
//    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
//
//        /**
//         * 图片的URL地址
//         */
//        private String imageUrl;
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            imageUrl = params[0];
//            // 在后台开始下载图片
//            Bitmap bitmap = downloadBitmap(params[0]);
//            if (bitmap != null) {
//                // 图片下载完成后缓存到LrcCache中
//                addBitmapToMemoryCache(params[0], bitmap);
//            }
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            // 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
//            ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imageUrl);
//            if (imageView != null && bitmap != null) {
//                imageView.setImageBitmap(bitmap);
//            }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Log.i("","imageUrl====="+imageUrl);
//                        String key = hashKeyForDisk(imageUrl);
//                        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
//                        Log.i("", "editor==" + (editor != null));
//                        if (editor != null) {
//                            OutputStream outputStream = editor.newOutputStream(0);
//                            if (downloadUrlToStream(imageUrl, outputStream)) {
//                                editor.commit();
//                            } else {
//                                editor.abort();
//                            }
//                        }
//                        mDiskLruCache.flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//            taskCollection.remove(this);
//        }
//
//        /**
//         * 建立HTTP请求，并获取Bitmap对象。
//         *
//         * @param imageUrl 图片的URL地址
//         * @return 解析后的Bitmap对象
//         */
//        private Bitmap downloadBitmap(String imageUrl) {
//            Bitmap bitmap = null;
//            HttpURLConnection con = null;
//            try {
//                URL url = new URL(imageUrl);
//                con = (HttpURLConnection) url.openConnection();
//                con.setConnectTimeout(5 * 1000);
//                con.setReadTimeout(10 * 1000);
//                bitmap = BitmapFactory.decodeStream(con.getInputStream());
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (con != null) {
//                    con.disconnect();
//                }
//            }
//            return bitmap;
//        }
//    }
//
//    public File getDiskCacheDir(Context context, String uniqueName) {
//        String cachePath;
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
//                || !Environment.isExternalStorageRemovable()) {
//            cachePath = context.getExternalCacheDir().getPath();
//        } else {
//            cachePath = context.getCacheDir().getPath();
//        }
//        return new File(cachePath + File.separator + uniqueName);
//    }
//
//    public String hashKeyForDisk(String key) {
//        String cacheKey;
//        try {
//            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
//            mDigest.update(key.getBytes());
//            cacheKey = bytesToHexString(mDigest.digest());
//        } catch (NoSuchAlgorithmException e) {
//            cacheKey = String.valueOf(key.hashCode());
//        }
//        return cacheKey;
//    }
//
//    private String bytesToHexString(byte[] bytes) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < bytes.length; i++) {
//            String hex = Integer.toHexString(0xFF & bytes[i]);
//            if (hex.length() == 1) {
//                sb.append('0');
//            }
//            sb.append(hex);
//        }
//        return sb.toString();
//    }
//
//    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
//        HttpURLConnection urlConnection = null;
//        BufferedOutputStream out = null;
//        BufferedInputStream in = null;
//        try {
//            final URL url = new URL(urlString);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
//            out = new BufferedOutputStream(outputStream, 8 * 1024);
//            int b;
//            while ((b = in.read()) != -1) {
//                out.write(b);
//            }
//            return true;
//        } catch (final IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (final IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
}