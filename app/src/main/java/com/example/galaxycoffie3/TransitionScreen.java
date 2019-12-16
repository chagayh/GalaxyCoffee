package com.example.galaxycoffie3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;

import com.airbnb.lottie.LottieAnimationView;
import com.tomer.fadingtextview.FadingTextView;

public class TransitionScreen extends AppCompatActivity {
    LottieAnimationView animation;
    Data data = Data.getSingleton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_screen);


        String[] texts = {data.costumerName + ", Your Order is Being Prepared",
                "#GalaxyBucks\nExplore Our Galaxy"};
        FadingTextView fadingTextView = findViewById(R.id.costumerMessage);
        fadingTextView.setTexts(texts);

        animation = findViewById(R.id.animation);
        animation.setProgress(0);
        animation.playAnimation();
        animation.setRepeatCount(Animation.INFINITE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                Intent addIntent = new Intent(getApplicationContext(), ShareOrder.class);
                addIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(addIntent);
                finish();
            }
        }, 6500);

    }

    @Override
    public void onBackPressed() {
        //your method call
    }


}
