package zoro.test.com.functionset.yunzhiwu.api;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author : Zoro.
 * @Date : 2017/7/3.
 * @Describe :
 */

public class SchedulersCompat {

//    private final static Observable.Transformer ioTransformer = o -> ((Observable)o).subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread());
//    private final static Observable.Transformer ioTransformer = new Observable.subscribeOn(Schedulers.io()) //指定 subscribe() 发生在 IO 线程
//            .observeOn(AndroidSchedulers.mainThread()); //指定 Subscriber 的回调发生在主线程

    private final static Observable.Transformer ioTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object o) {
            return ((Observable) o).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static <T> Observable.Transformer<T, T> applyIoSchedulers() {
        return (Observable.Transformer<T, T>) ioTransformer;
    }
}
