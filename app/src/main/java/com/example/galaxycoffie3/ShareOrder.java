package com.example.galaxycoffie3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareOrder extends AppCompatActivity {

    String type = "image/*";
    private ImageView userPicture;
    private android.net.Uri file;
    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), "CameraDemo");
    boolean secondPress = false;
    Data data = Data.getSingleton();
    LottieAnimationView camera_button, finger_gesture, publishToInstegram;
    ConstraintLayout myLayout;
    String mediaPath, imName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_order);

        myLayout = findViewById(R.id.relativeLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        userPicture = (ImageView) findViewById(R.id.user_picture);
        camera_button = findViewById(R.id.cameraButton);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            camera_button.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        publishToInstegram = findViewById(R.id.share_instegram);
        publishToInstegram.setVisibility(View.INVISIBLE);
        publishToInstegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createInstagramIntent(type, mediaPath);
            }
        });
        finger_gesture = findViewById(R.id.finger_gesture);
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                imName = "IMG_" + timeStamp + ".jpg";
                String filename = "/" + imName;
                mediaPath = mediaStorageDir.getPath() + File.separator + filename;
                camera_button.setProgress(0);
                camera_button.playAnimation();
                finger_gesture.setVisibility(View.INVISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        file = Uri.fromFile(getOutputMediaFile());
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                        startActivityForResult(intent, 100);
                    }
                }, 800);
            }
        });
        finger_gesture.setProgress(0);
        finger_gesture.playAnimation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                camera_button.setEnabled(true);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                userPicture.setImageURI(file);
                userPicture.setBackground(getDrawable(R.drawable.image_frame));
                publishToInstegram.setVisibility(View.VISIBLE);
                publishToInstegram.setProgress(0);
                publishToInstegram.playAnimation();
            }
        }


    }

    private File getOutputMediaFile() {
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return new File(mediaStorageDir.getPath() + File.separator +
                imName);
    }


    private void createInstagramIntent(String type, String mediaPath) {

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }

    @Override
    public void onBackPressed() {
        //your method call
        if (secondPress) {
            Intent addIntent = new Intent(getApplicationContext(), MainActivity.class);
            addIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(addIntent);
            data.shopCart.clear();
            finish();
        } else {
            secondPress = true;
            Toast.makeText(ShareOrder.this, "Press again to return to the Start Page", Toast.LENGTH_SHORT).show();
        }
    }
}