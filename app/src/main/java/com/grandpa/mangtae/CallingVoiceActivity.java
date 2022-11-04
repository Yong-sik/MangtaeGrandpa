package com.grandpa.mangtae;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;

public class CallingVoiceActivity extends AppCompatActivity {

    //btn
    ImageButton exit;
    //img
    ImageView face;
    //media
    MediaPlayer mediaPlayer = new MediaPlayer();
    Intent receivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling_voice);

        receivedIntent = getIntent();
        String audioPath = receivedIntent.getStringExtra("audioPath");
        Log.d("audioPath"," : "+audioPath);

        //btn list
        //exit = 전화종료
        exit = findViewById(R.id.ibtn_calling_voice_exit);
        //img
        //face = 망태할아버지 얼굴
        face = findViewById(R.id.iv_calling_voice);
        //clicklistener 할당
        exit.setOnClickListener(mClickListener);

        playVoice(audioPath);

        //image view 에 glide로 이미지 넣기
        Glide.with(CallingVoiceActivity.this)
                .load(R.raw.mangtae)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(face);
    }


    View.OnClickListener mClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            finish();
        }
    };

    //망태할아버지 목소리 release version
    public void playVoice(String fileName) {
        File file = new File("/data/data/com.grandpa.mangtae/files/"+ fileName);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.fromFile(file));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });
        //wav파일 재생시간
//        int duration = mediaPlayer.getDuration();
        //재생시간 구하기 테스트용 toast
//        Toast myToast = Toast.makeText(this.getApplicationContext(),String.valueOf(duration), Toast.LENGTH_LONG);
//        myToast.show();
    }
}