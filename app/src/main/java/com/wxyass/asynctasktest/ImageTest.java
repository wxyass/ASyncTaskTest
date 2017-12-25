package com.wxyass.asynctasktest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URL;

/**
 * Created by yangwenmin on 2017/12/25.
 */

public class ImageTest extends Activity {

    private ImageView imageView;
    private ProgressBar progressBar;
    private static String imageUrl = "http://img.my.csdn.net/uploads/201504/12/1428806103_9476.png";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = (ImageView)findViewById(R.id.image);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        // 设置传递进去的参数
        new MyAsyncTask().execute(imageUrl);
    }



    class MyAsyncTask extends AsyncTask<String,Void,Bitmap>{

        // 可以传递进来多个参数,是在task.execute()中传递的.分别对应params[0],params[1],params[2]...
        @Override
        protected Bitmap doInBackground(String... params) {
            // 获取传递进来的参数
            String url = params[0];
            Bitmap bitmap = null;
            URLConnection connection;
            InputStream is;
            try {
                connection = new URL(url).openConnection();
                is = connection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                // 通过decodeStream解析输入流
                bitmap = BitmapFactory.decodeStream(bis);
                is.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 将bitmap作为返回值
            return bitmap;
        }

        // 异步执行前
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 显示
            progressBar.setVisibility(View.VISIBLE);
        }

        // 异步执行后
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progressBar.setVisibility(View.GONE);
            imageView.setImageBitmap(bitmap);

        }
    }
}
