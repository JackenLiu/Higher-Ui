package com.jacken_liu.neteaseeventlib;

import com.jacken_liu.neteaseeventlib.listener.OnClickListener;
import com.jacken_liu.neteaseeventlib.listener.OnTouchListener;


public class Activity {
    public static void main(String[] args) {
        ViewGroup viewGroup = new ViewGroup(0, 0, 1080, 1920);
        viewGroup.setName("viewGroup first");
        ViewGroup viewGroup1 = new ViewGroup(0, 0, 500, 500);
        viewGroup1.setName("viewGroup second");
        View view = new View(0, 0, 200, 200);
        view.setName("view first");
        View view1 = new View(0, 0, 300, 300);
        view1.setName("view second");

        viewGroup1.addView(view);
        viewGroup1.addView(view1);
        viewGroup.addView(viewGroup1);

        viewGroup.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                System.out.println("------------- viewGroup first onTouch");
                return false;
            }
        });
        viewGroup1.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                System.out.println("------------- viewGroup second onTouch");
                return false;
            }
        });
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                System.out.println("------------- view first onTouch");
                return false;
            }
        });
        view1.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                System.out.println("------------- view second onTouch");
                return false;
            }
        });
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("------------- view first onClick");
            }
        });

        /*
        顶层容器开始分发事件
         */
        MotionEvent motionEvent = new MotionEvent(100,100);
        motionEvent.setActionMasked(MotionEvent.ACTION_DOWN);
        viewGroup.dispatchTouchEvent(motionEvent);

        System.out.println("--------------------------------------");

        MotionEvent motionEvent1 = new MotionEvent(100,100);
        motionEvent1.setActionMasked(MotionEvent.ACTION_UP);
        viewGroup.dispatchTouchEvent(motionEvent1);
    }

}
