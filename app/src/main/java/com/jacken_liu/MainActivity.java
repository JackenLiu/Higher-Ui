package com.jacken_liu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.jacken_liu.path.PathEffectActivity;
import com.jacken_liu.path.PathPrimaryActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final Class toActivity = CanvasPrimaryActivity.class;

//        final Class toActivity = PaintColorFilterActivity.class;
//        final Class toActivity = PaintGradientActivity.class;
//        final Class toActivity = PaintInitActivity.class;

        final Class toActivity = PathPrimaryActivity.class;
//        final Class toActivity = PathEffectActivity.class;

        startActivity(new Intent(this, toActivity));
    }
}