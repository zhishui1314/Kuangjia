package com.anke.vehicle.kuangjia;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import Utils.OkHttpUtils;
import Utils.VolleyUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private ImageView imageView;
    String getUrl = "https://api.github.com/repos/square/okhttp/contributors";
    String url = "http://172.16.100.100:8810/";
    String imgUrl = "https://www.baidu.com/img/bd_logo1.png";
    String upUrl = "http://172.16.100.58:8080/";
    String video = "http://172.16.100.58:8080/AnchorVehicle.apk";
    private FileOutputStream fileOutputStream;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int current = msg.arg1;
                progressDialog.setProgress((int) ((current * 100) / total));
                progressDialog.show();

                if (current == total) {
                    progressDialog.dismiss();
                }
            }
        }
    };
    private long total;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = (TextView) findViewById(R.id.tvResult);
        imageView = (ImageView) findViewById(R.id.imgs);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("努力下载中。。。");

    }

    /**
     * get请求
     *
     * @param view
     */
    public void vget(View view) {
        VolleyUtils.getInstance().getStringMethod(getUrl, new VolleyUtils.VolleyUtilsListener() {
            @Override
            public void onResult(String result) {
                if (!TextUtils.isEmpty(result)) {
                    tvResult.setText(result);
                }
            }
        });
    }

    /**
     * post请求
     *
     * @param view
     */
    public void vpost(View view) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", "QueryStop");
        VolleyUtils.getInstance().postStringMethod(url, map, new VolleyUtils.VolleyUtilsListener() {
            @Override
            public void onResult(String result) {
                if (!TextUtils.isEmpty(result)) {
                    tvResult.setText(result);
                }
            }
        });
    }

    /**
     * 图片请求，可以缓存
     *
     * @param view
     */
    public void vtu(View view) {
        VolleyUtils.getInstance().imageMethod(imgUrl, imageView);

    }

    /**
     * okhttp
     *
     * @param view
     */
    public void okget(View view) {
        new Thread() {
            @Override
            public void run() {
                final String result = OkHttpUtils.getInstance(MainActivity.this).doGet(getUrl);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(result);
                    }
                });
            }
        }.start();

    }

    public void okgetsync(View view) {
        OkHttpUtils.getInstance(this).doGetAsync(getUrl, new OkHttpUtils.OnResultListener() {
            @Override
            public void onResult(String onResult) {
                if (!TextUtils.isEmpty(onResult)) {
                    tvResult.setText(onResult);
                }
            }
        });
    }

    public void okpost(View view) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("type", "QueryStop");
        new Thread() {
            @Override
            public void run() {
                final String resullt = OkHttpUtils.getInstance(MainActivity.this).doPost(url, map);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(resullt);
                    }
                });
            }
        }.start();
    }

    public void okpostsync(View view) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", "QueryStop");
        OkHttpUtils.getInstance(this).doPostAsync(url, map, new OkHttpUtils.OnResultListener() {
            @Override
            public void onResult(String onResult) {
                if (!TextUtils.isEmpty(onResult)) {
                    tvResult.setText(onResult);
                }
            }
        });
    }

    /**
     * 下载大型文件或者apk等 在子线程中
     * @param view
     */
    public void downLoad(View view) {
        OkHttpUtils.getInstance(this).dogetDownload(imgUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("下载失败", "111");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                down(response);
            }
        });
    }
    public  void downloadTUpian(View view){
        OkHttpUtils.getInstance(this).downLoadPicture(imgUrl, new OkHttpUtils.OnPictureListener() {
            @Override
            public void onResult(InputStream inputStream) {
             if (inputStream != null){
                 Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                 imageView.setImageBitmap(bitmap);

             }
            }
        });
    }
    private void down(Response response) {
        total = response.body().contentLength();
        InputStream inputStream = response.body().byteStream();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AnchorVehicle.apk";
        byte[] buff = new byte[1024];
        int length;
        int sum = 0;
        try {
            fileOutputStream = new FileOutputStream(new File(path));
            while ((length = inputStream.read(buff)) != -1) {
                sum = sum + length;
                Log.e("进度", sum + "");
                fileOutputStream.write(buff, 0, length);
                Message message = handler.obtainMessage();
                message.what = 1;
                message.arg1 = sum;
                handler.sendMessage(message);
            }
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}
