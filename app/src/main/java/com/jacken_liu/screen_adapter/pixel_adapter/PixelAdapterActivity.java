package com.jacken_liu.screen_adapter.pixel_adapter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jacken_liu.R;

public class PixelAdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixel_adapter);

        final TextView textView = findViewById(R.id.tv);
        textView.post(new Runnable() {
            @Override
            public void run() {
                Log.d("TextView", textView.getWidth() + "  " + textView.getHeight());
                Log.d("ScreenAdapterLayout",
                        ((ScreenAdapterLayout) textView.getParent()).getWidth() + "  "
                                + ((ScreenAdapterLayout) textView.getParent()).getHeight());
            }
        });
    }
}