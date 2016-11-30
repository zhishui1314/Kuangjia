package Utils;


import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.anke.vehicle.kuangjia.MyApplication;
import com.anke.vehicle.kuangjia.R;

import java.util.Map;

/**
 * Created by Administrator on 2016/11/9 0009.
 */
public class VolleyUtils {

    private final RequestQueue mRequestQueue;
    private static VolleyUtils volleyUtils;

    private VolleyUtils(){
        mRequestQueue = MyApplication.getRequestQueue();
    }
    public static VolleyUtils getInstance(){
        if (volleyUtils == null){
            synchronized (VolleyUtils.class){
                volleyUtils = new VolleyUtils();
            }
        }
        return volleyUtils;
    }


    /**
     * get请求String
     *
     * @param url
     */

    public void getStringMethod(String url, final VolleyUtilsListener volleyUtilsListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
             volleyUtilsListener.onResult(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyUtilsListener.onResult(volleyError.getMessage());//这句话的值在醒目中可以改变，
            }
        });
        mRequestQueue.add(stringRequest);

    }

    /**
     * post请求String
     *
     * @param url
     */
    public void postStringMethod(String url, final Map map, final VolleyUtilsListener volleyUtilsListener) {
        StringRequest stringRequest = new MyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                volleyUtilsListener.onResult(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyUtilsListener.onResult(volleyError.getMessage());//这句话的值在醒目中可以改变，
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return map;
            }
        };
        mRequestQueue.add(stringRequest);
    }

    /**
     * 网上下载图片请求。且具有缓存效果
     *
     * @param url
     * @param imageView
     */
    public void imageMethod(String url, ImageView imageView) {
        ImageLoader imageLoader = new ImageLoader(MyApplication.getRequestQueue(), new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.mipmap.ic_launcher, //正在加载的显示的图片
                R.mipmap.ic_launcher);//加载失败的显示的图片
        imageLoader.get(url, listener, 200, 200);//最后两个参数显示的宽和高，当然这个方法有重载方法
    }

    public class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }

    /**
     * 向外暴漏接口回调
     */
    public interface VolleyUtilsListener {
        //请求结果
        void onResult(String result);

    }

}
