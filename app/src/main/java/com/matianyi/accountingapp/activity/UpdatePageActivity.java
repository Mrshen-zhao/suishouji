package com.matianyi.accountingapp.activity;

import android.app.Activity;
import android.os.Looper;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.matianyi.accountingapp.R;

public class UpdatePageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_update_page, null);
        setContentView(view);

        //setContentView(R.layout.activity_update_page);

        final TextView waitingText = findViewById(R.id.waiting_text);

        final ProgressBar progressBar = findViewById(R.id.progress_bar);


        final ProgressBar  bar= (ProgressBar) findViewById(R.id.progress_bar);
        final TextView textView= (TextView) findViewById(R.id.tv_progress);
        new Thread(){
            @Override
            public void run() {
                int i=0;
                while(i<100){
                    i++;
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int j=i;
                    bar.setProgress(i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(j+"%");
                        }
                    });
                }
                waitingText.setVisibility(View.INVISIBLE);
                Looper.prepare();
                Toast.makeText(UpdatePageActivity.this,
                        "哦豁！你已经是最新版了！", Toast.LENGTH_LONG).show();
                finish();
                Looper.loop();
            }
        }.start();
    }
}
