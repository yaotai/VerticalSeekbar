package com.wl.verticalseekbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.wl.vertical_seekbar.Logs;
import com.wl.vertical_seekbar.VerticalSeekbar;

public class MainActivity extends AppCompatActivity {
private VerticalSeekbar startVertical;
private int pro=50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startVertical=(VerticalSeekbar)findViewById(R.id.startVertical);
        startVertical.setOnProgressCall(new VerticalSeekbar.OnProgressCall() {
            @Override
            public void change(int progress) {
                pro=progress;
                Logs.print("setOnProgressCall progress:"+progress+", pro:"+pro);
            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro++;
                if (pro>=100){
                    pro=100;
                }
                startVertical.setProgress(pro);
            }
        });

        findViewById(R.id.min).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro--;
            if (pro<=0){
                pro=0;
            }
            startVertical.setProgress(pro);
            }
        });
    }
}