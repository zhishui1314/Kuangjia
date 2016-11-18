package Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
public class OkHttpUtils {
    private static OkHttpClient okHttpClient;
    private static OkHttpUtils inStance = null;
    private final Handler mHandler;

    private OkHttpUtils(Context context) {
        File sdcache = context.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize))
                .build();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpUtils getInstance(Context context) {
        if (inStance == null) {
            synchronized (OkHttpUtils.class) {
                inStance = new OkHttpUtils(context);
            }
        }
        return inStance;
    }

    /**
     * get同步请求
     *
     * @param url
     * @return
     */
    public String doGet(String url) {
        String result = "";
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            } else {
                result = "请求失败";
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = "请求异常：" + e.getMessage();
        }
        return result;
    }

    /**
     * get异步请求
     *
     * @param url
     * @param onResultListener 自定义的接口，向外暴漏，以便传值
     */
    public void doGetAsync(String url, final OnResultListener onResultListener) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResultListener.onResult(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            onResultListener.onResult(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /**
     * post同步请求
     *
     * @param url
     * @param map
     * @return
     */
    public String doPost(String url, Map<String, String> map) {
        String result = "";
        FormBody.Builder builder = new FormBody.Builder();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            FormBody formBody = builder.build();
            Request request = new Request.Builder().url(url).post(formBody).build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    result = response.body().string();
                } else {
                    result = "请求失败";
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = "请求异常：" + e.getMessage();
            }
        }
        return result;
    }

    /**
     * 下载图片
     *
     * @param url
     * @param onPictureListener 自定义的接口，向外暴漏，以便传值
     */
    public void dogetSyncPicture(String url, final OnPictureListener onPictureListener) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onPictureListener.onResult(response.body().byteStream());
                    }
                });
            }
        });
    }

    /**
     * post异步请求
     *
     * @param url
     * @param map
     * @param onResultListener 自定义的接口，向外暴漏，以便传值
     */
    public void doPostAsync(String url, Map<String, String> map, final OnResultListener onResultListener) {
        FormBody.Builder builder = new FormBody.Builder();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            FormBody formBody = builder.build();
            Request request = new Request.Builder().url(url).post(formBody).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onResultListener.onResult(e.getMessage());
                        }
                    });

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                onResultListener.onResult(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
        }
    }


    public interface OnResultListener {
        void onResult(String onResult);
    }

    public interface OnPictureListener {
        void onResult(InputStream inputStream);
    }
}
