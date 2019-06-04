package com.example.kula_decyzyjna;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView main_text;
    private Drawable empty_ball;
    private Drawable front_ball;

    private boolean animation_running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animation_running = false;

        main_text = (TextView) findViewById(R.id.main_text);
        empty_ball = getResources().getDrawable(R.drawable.hw3ball_empty);
        front_ball = getResources().getDrawable(R.drawable.hw3ball_front);

        main_text.setBackground(front_ball);
        main_text.setText("");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        final AnimatorSet set = new AnimatorSet();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if ((event.values[0]>3.5 || event.values[0]<-3.5) && animation_running==false) {
            animation_running = true;

            ObjectAnimator animation;
            if (event.values[0]<0) {
                animation = ObjectAnimator.ofFloat(main_text, "translationX", -400f);
            } else {
                animation = ObjectAnimator.ofFloat(main_text, "translationX", 400f);
            }

            animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    int random = (int) (Math.abs(event.values[0] * 1000));
//                    Log.d("Random", String.valueOf(random));
                    String[] array = getResources().getStringArray(R.array.ball_strings);
                    String randomStr = array[random % array.length];

                    main_text.setBackground(empty_ball);
                    main_text.setText(randomStr);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            ObjectAnimator animation2 = ObjectAnimator.ofFloat(main_text, "translationX", 0);
            animation2.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animation_running = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    animation_running = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            AnimatorSet my_set = new AnimatorSet();
            my_set.setDuration(250);
            my_set.play(animation).before(animation2);
            my_set.start();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
