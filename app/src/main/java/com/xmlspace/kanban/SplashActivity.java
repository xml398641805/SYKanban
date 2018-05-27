package com.xmlspace.kanban;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onStart() {
        super.onStart();

        VideoView playVideo=(VideoView)findViewById(R.id.videoView);
        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.splash);
        MediaController mediaController=new MediaController(this);
        try{
            playVideo.setMediaController(mediaController);
            playVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    StartTask();
                }
            });
            playVideo.setVideoURI(uri);
            playVideo.start();
        }catch(Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void StartTask(){
        //Toast.makeText(this, "完成", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(SplashActivity.this,MaterialChart.class);

        startActivity(intent);
        //overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_left);
    }
}
