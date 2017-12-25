package com.wxyass.asynctasktest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyAsyncTask task = new MyAsyncTask();
        task.execute();//

        //
        findViewById(R.id.imageload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,ImageTest.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.toprogerssload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,ProgressBarTest.class);
                startActivity(intent);
            }
        });
    }
}
