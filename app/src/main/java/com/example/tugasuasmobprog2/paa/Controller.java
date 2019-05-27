package com.example.tugasuasmobprog2.paa;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class Controller extends Application {

    public static final String TAG = Controller.class.getSimpleName();

    private RequestQueue mReqQueue;

    private static Controller mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized Controller getInstance() {
        return mInstance;
    }

    public RequestQueue getReqQueue() {
        if (mReqQueue == null) {
            mReqQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mReqQueue;
    }

    public <T> void addToReqQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getReqQueue().add(request);
    }

    public <T> void addToReqQueue(Request<T> request) {
        request.setTag(TAG);
        getReqQueue().add(request);
    }

    public void cancelPendingReqs(Object tag) {
        if (mReqQueue != null) {
            mReqQueue.cancelAll(tag);
        }
    }
}
