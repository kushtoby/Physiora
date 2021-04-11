package com.android.physiora;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class VideoPIP extends AppCompatActivity {
VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_p_i_p);
        videoView = findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+ getPackageName() + "/" + R.raw.runthrough);
        videoView.setVideoURI(uri);
        videoView.start();
        PictureInPictureParams pictureInPictureParams = new PictureInPictureParams.Builder().build();
        enterPictureInPictureMode(pictureInPictureParams);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (isInPictureInPictureMode) {
         //   Intent intent = new Intent( VideoPIP.this, MainActivity.class);
//    startActivity(intent);
        }
                }
                }