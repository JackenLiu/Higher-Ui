package com.jacken_liu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.jacken_liu.anim.AnimatorActivity;
import com.jacken_liu.path.PathBezierCurveActivity;
import com.jacken_liu.path.PathMeasureActivity;
import com.jacken_liu.path.PathPrimaryActivity;
import com.jacken_liu.screen_adapter.percent.PercentAdapterActivity;
import com.jacken_liu.screen_adapter.pixel_adapter.PixelAdapterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final Class toActivity = CanvasPrimaryActivity.class;

//        final Class toActivity = PaintColorFilterActivity.class;
//        final Class toActivity = PaintGradientActivity.class;
//        final Class toActivity = PaintInitActivity.class;

//        final Class toActivity = PathPrimaryActivity.class;
//        final Class toActivity = PathEffectActivity.class;
//        final Class toActivity = PathBezierCurveActivity.class;
//        final Class toActivity = PathMeasureActivity.class;

//        final Class toActivity = AnimatorActivity.class;

//        final Class toActivity = PixelAdapterActivity.class;
        final Class toActivity = PercentAdapterActivity.class;

        startActivity(new Intent(this, toActivity));
    }
}