package com.jacken_liu.nerippleanimator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.jacken_liu.nerippleanimator.ui.UIUtils;
import com.jacken_liu.nerippleanimator.ui.ViewCalculateUtil;

public class MainActivity extends AppCompatActivity {
    RippleAnimatorView rippleAnimatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.getInstance(this);
        setContentView(R.layout.activity_main1);

        ViewCalculateUtil.setViewLayoutParam(findViewById(R.id.iv),
                400, 400, 0, 0, 0, 0);
        rippleAnimatorView = findViewById(R.id.rav);
    }

    public void start(View view) {
        if (rippleAnimatorView.isAnimationRunning()) {
            rippleAnimatorView.stopRippleAnimation();
        } else {
            rippleAnimatorView.startRippleAnimation();
        }
    }
}
