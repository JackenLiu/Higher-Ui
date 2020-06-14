package com.jacken_liu.path;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Path 的基本应用
 */
public class PathPrimaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    static class PathPrimaryView extends View {

        private Paint mPaint;
        private Path mPath;

        public PathPrimaryView(Context context) {
            super(context);
        }
    }

}