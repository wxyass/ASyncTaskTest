package com.wxyass.asynctasktest;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by yangwenmin on 2017/12/24.
 *
 * AsyncTask<Void,Void,Void>
 * Params:      参数类型:启动任务时输入参数的类型
 * Progress:    进度值类型: 后台任务执行中返回进度值的类型
 * Result:      返回值类型: 后台执行任务完成后返回结果的类型
 */

public class MyAsyncTask extends AsyncTask<Void,Void,Void> {

    // 必须要实现的方法 , 执行耗时操作(异步执行后台线程将要完成的任务)
    @Override
    protected Void doInBackground(Void... voids) {
        Log.d("MyAsyncTask","doInBackground");
        publishProgress();// 传入进度值,?
        return null;
    }

    // 下面3个方法时 非必须要实现的

    // 执行后台耗时操作前被调用(即:执行doInBackground()方法前,先执行这个方法),通常用户完成一些初始化操作
    @Override
    protected void onPreExecute() {
        Log.d("MyAsyncTask","onPreExecute");
        super.onPreExecute();
    }

    // 当doInBackground()完成后,系统会自动调用,会接收doInBackground()方法中最后return的值
    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d("MyAsyncTask","onPostExecute");
        super.onPostExecute(aVoid);
    }

    // 在doInBackground方法中 每执行publishProgress()时,都将会执行此方法,并将传入的值带过来
    @Override
    protected void onProgressUpdate(Void... values) {
        Log.d("MyAsyncTask","onProgressUpdate");
        super.onProgressUpdate(values);
    }
}
