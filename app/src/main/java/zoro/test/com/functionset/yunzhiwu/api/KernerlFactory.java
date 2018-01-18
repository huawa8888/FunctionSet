package com.mob.linyl.api;

/**
 * Created by Administrator on 2016/8/19.
 */
public class KernerlFactory {
    private static KernerlService mKernerlService = null;
    private static final Object WATCH = new Object();

    public static KernerlService getKernerlApi(){
        synchronized (WATCH) {
            if (mKernerlService == null) {
                mKernerlService = new KernerlClient().getService();
            }
        }
        return mKernerlService;
    }
}
