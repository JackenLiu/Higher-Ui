package com.jacken_liu.nemusicscreenadapt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UiUtils.getInstance(this);

        // 直接修改 view 的宽高方式
        textView = findViewById(R.id.tv);
        ViewCalculateUtil.setViewRelativeLayoutParam(textView, 540, 80,
                0, 0, 0, 0, true);
        ViewCalculateUtil.setTextSize(textView, 40);
    }
}
