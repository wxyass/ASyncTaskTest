package com.wxyass.asynctasktest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by yangwenmin on 2017/12/25.
 */

public class ProgressBarTest extends Activity {

    private ProgressBar mProgressBar;
    private MyAsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        mProgressBar = findViewById(R.id.loadprogressbar);

        mTask = new MyAsyncTask();
        mTask.execute();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mTask!=null&&
                mTask.getStatus() == AsyncTask.Status.RUNNING){
            // cannerl方法只是将对应的AsyncTask标记为cancel状态,并不是真正的取消线程的执行. 去doInBackground取消
            mTask.cancel(true);
        }
    }

    class MyAsyncTask extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i=0;i<100;i++){
                if(isCancelled()){
                    break;
                }
                publishProgress(i);// 参数可变
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        // 获取publishProgress(i)传递进去的参数,分别对应values[0], values[1], values[2]...
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(isCancelled()){
                return;
            }
            // 获取进度更新值
            mProgressBar.setProgress(values[0]);
        }
    }
}
