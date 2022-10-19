package com.grandpa.mangtae;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class CallingVoiceActivity extends AppCompatActivity {

    //btn
    ImageButton exit;

    //img
    ImageView face;

    //media
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling_voice);

        //btn list
        //exit = 전화받기
        exit = findViewById(R.id.ibtn_calling_voice_exit);

        //img
        //face = 망태할아버지 얼굴
        face = findViewById(R.id.iv_calling_voice);

        //각 view에 clicklistener 할당
        exit.setOnClickListener(mClickListener);

        //망태할아버지 목소리
        mediaPlayer = MediaPlayer.create(this, R.raw.ddururururu);
        mediaPlayer.start();

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
            mediaPlayer.stop();
            finish();
        }
    };
}