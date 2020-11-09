package com.jacken_liu.xfermodes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jacken_liu.R;

public class XfermodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new XfermodeView(this));
        // 官方修改 demo
//        setContentView(new XfermodesView(this));
//        setContentView(new XfermodeEraserView(this));
        setContentView(R.layout.activity_xfermode);

        MyScrollView myScrollView = findViewById(R.id.msv);
        XfermodeEraserView eraserView = findViewById(R.id.xev);
        eraserView.setMyScrollView(myScrollView);
    }
}
