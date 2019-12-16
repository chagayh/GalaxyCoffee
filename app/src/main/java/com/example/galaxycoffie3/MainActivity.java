package com.example.galaxycoffie3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    /**
     * Create a VideoView variable, a MediaPlayer variable, and an int to hold the current
     * video position.
     **/
    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    Button button;
    ImageButton imageButton, imageButton2;
    LottieAnimationView animation;
    Context context;
    LottieAnimationView confetti;
    ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        // Hook up the VideoView to our UI.
        videoBG = findViewById(R.id.videoView);

        // Build your video Uri
        Uri uri = Uri.parse("android.resource://" // First start with this,
                + getPackageName() // then retrieve your package name,
                + "/" // add a slash,
                + R.raw.coffee_video_2); // and then finally add your video resource. Make sure it is stored
        // in the raw folder.

        // Set the new Uri to our VideoView
        videoBG.setVideoURI(uri);
        // Start the VideoView
        videoBG.start();

        // Set an OnPreparedListener for our VideoView. For more information about VideoViews,
        // check out the Android Docs: https://developer.android.com/reference/android/widget/VideoView.html
        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer = mediaPlayer;
                // We want our video to play over and over so we set looping to true.
                mMediaPlayer.setLooping(true);
                // We then seek to the current posistion if it has been set and play the video.
                if (mCurrentVideoPosition != 0) {
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
            }
        });
        final TextView getCoffeeText = findViewById(R.id.getCoffee);
        animation = findViewById(R.id.animation);
        animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCoffeeText.setText(null);
                animation.setProgress(0);
                animation.playAnimation();
                animation.setRepeatCount(Animation.INFINITE);
                animation.setEnabled(false);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        animation.setEnabled(true);
                        Intent addIntent = new Intent(getApplicationContext(), ShareOrder.class);
                        startActivity(addIntent);
                    }
                }, 1000);
            }
        });
        confetti = findViewById(R.id.confetti);
        logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confetti.setVisibility(View.VISIBLE);
                confetti.setProgress(0);
                confetti.playAnimation();
            }
        });
//        button = findViewById(R.id.button);
//        AnimationDrawable animationDrawable = (AnimationDrawable) button.getBackground();
//        animationDrawable.setEnterFadeDuration(2000);
//        animationDrawable.setExitFadeDuration(4000);
//        animationDrawable.start();
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent addIntent = new Intent(getApplicationContext(), ChooseCoffeeWindow.class);
//                startActivity(addIntent);
//            }
//        });

    }


    /**
     * We must override onPause(), onResume(), and onDestroy() to properly handle our
     * VideoView.
     **/

    @Override
    protected void onPause() {
        super.onPause();
        // Capture the current video position and pause the video.
        mCurrentVideoPosition = mMediaPlayer.getCurrentPosition();
        videoBG.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restart the video when resuming the Activity
        videoBG.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // When the Activity is destroyed, release our MediaPlayer and set it to null.
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}
