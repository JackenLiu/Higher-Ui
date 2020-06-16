package com.jacken_liu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.jacken_liu.path.PathBezierCurveActivity;
import com.jacken_liu.path.PathMeasureActivity;
import com.jacken_liu.path.PathPrimaryActivity;

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
        final Class toActivity = PathMeasureActivity.class;

        startActivity(new Intent(this, toActivity));
    }
}