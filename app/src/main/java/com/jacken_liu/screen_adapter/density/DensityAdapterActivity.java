package com.jacken_liu.screen_adapter.density;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jacken_liu.R;

public class DensityAdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        · 修改的是整个 app 的 density
        · 在 ui 稿统一页面参考宽度的前提下，一般在 application 或 baseActivity 里使用
         */
        DensityUtils.setDensity(getApplication(), this);

        setContentView(R.layout.activity_density_adapter);
    }
}