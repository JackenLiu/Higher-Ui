package com.jacken_liu.anim;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.jacken_liu.R;
import com.jacken_liu.neteaseanimatorlib.LineInterpolator;
import com.jacken_liu.neteaseanimatorlib.MyObjectAnimator;

public class AnimatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);

        Button button = findViewById(R.id.btn);
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                button,
                "scaleX",
                /*
                · 这里 5 个关键帧，每两个关键帧之间变化时间为 4000/4
                · 意思是 1 变到 2 需要 1000 毫秒， 2 变到 3 需要 1000 毫秒
                · 同理 4 变到 1 也需要 1000 毫秒，所以后面动画恢复原状的速度变快
                 */
                1, 2, 3, 4, 1);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(4000);
        animator.setRepeatCount(-1);
//        animator.start();

        MyObjectAnimator animator1 = MyObjectAnimator.ofFloat(
                button,
                "scaleX",
                1, 2, 3, 1);
        animator1.setInterpolator(new LineInterpolator());
        animator1.setDuration(3000);
        animator1.start();
    }
}