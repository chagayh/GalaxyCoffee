package com.example.galaxycoffie3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

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
    Context context;


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
        button = findViewById(R.id.btLogInButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(getApplicationContext(), ChooseCoffeeWindow.class);
                startActivity(addIntent);
            }
        });
        imageButton = findViewById(R.id.instagram_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                try {
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(context, "Instagram not found, please install", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageButton2 = findViewById(R.id.trip_adviser_button);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* set our trip adviser link*/
                Uri uri = Uri.parse("http://www.tripadvisor.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

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
