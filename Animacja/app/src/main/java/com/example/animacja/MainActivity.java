package com.example.animacja;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView triangleImageView;
    private ValueAnimator rotateAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Path path = new Path();
        path.moveTo(dpToPx(0), dpToPx(0));
        path.lineTo(dpToPx(100), dpToPx(0));
        path.lineTo(dpToPx(50), dpToPx(100));
        path.lineTo(dpToPx(0), dpToPx(0));


        ShapeDrawable shapeDrawable = new ShapeDrawable(new PathShape(path, dpToPx(100), dpToPx(100)));
        shapeDrawable.getPaint().setColor(getResources().getColor(android.R.color.holo_orange_light));

        triangleImageView = findViewById(R.id.triangleImageView);
        triangleImageView.setImageDrawable(shapeDrawable);


        rotateAnimator = ValueAnimator.ofFloat(0f, 360f);
        rotateAnimator.setTarget(triangleImageView);
        rotateAnimator.setDuration(2000);
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimator.setInterpolator(new LinearInterpolator());


        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAnimation();
            }
        });


        Button stopButton = findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAnimation();
            }
        });


        Button stepButton = findViewById(R.id.stepButton);
        stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepAnimation();
            }
        });
    }


    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void playAnimation() {
        rotateAnimator.setFloatValues(0f, 360f);
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimator.setDuration(2000);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.start();
    }

    private void stepAnimation() {
        rotateAnimator.cancel();
        triangleImageView.setRotation(0);
        rotateAnimator.setFloatValues(triangleImageView.getRotation(), triangleImageView.getRotation() + 90);
        rotateAnimator.setDuration(10);
        rotateAnimator.setRepeatCount(0);
        rotateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                rotateAnimator.removeListener(this);
                triangleImageView.setRotation(triangleImageView.getRotation() % 360);
                playAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        rotateAnimator.start();
    }

    private void stopAnimation() {
        rotateAnimator.cancel();
    }
}
