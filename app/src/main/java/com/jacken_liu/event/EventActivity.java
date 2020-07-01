package com.jacken_liu.event;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jacken_liu.neteaseeventlib.MotionEvent;
import com.jacken_liu.neteaseeventlib.ViewGroup;
import com.jacken_liu.neteaseeventlib.listener.OnClickListener;
import com.jacken_liu.neteaseeventlib.listener.OnTouchListener;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MyTextView myTextView = new MyTextView(this);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setBackgroundColor(Color.RED);
        relativeLayout.addView(myTextView, layoutParams);
        setContentView(relativeLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTextView.setText("不会吧" + Math.random());

            }
        });

        // 参考 lib 包
    }

    class MyTextView extends androidx.appcompat.widget.AppCompatTextView {
        public MyTextView(Context context) {
            super(context);
        }

        public MyTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            Log.d("onMeasure", "===============");
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            Log.d("onLayout", "===============");

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.d("onDraw", "===============");

        }
    }

}