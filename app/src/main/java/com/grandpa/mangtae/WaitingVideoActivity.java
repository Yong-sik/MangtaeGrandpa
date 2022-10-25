package com.grandpa.mangtae;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class WaitingVideoActivity extends AppCompatActivity {

    ImageButton accept, refuse;
    ImageView face;
    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_video);

        accept = findViewById(R.id.ibtn_waiting_video_accept);
        refuse = findViewById(R.id.ibtn_waiting_video_refuse);

        face = findViewById(R.id.iv_waiting_video);

        //전화벨
        mediaPlayer = MediaPlayer.create(this, R.raw.ddururururu);
        mediaPlayer.start();

        //각 view에 clicklistener 할당
        accept.setOnClickListener(mClickListener);
        refuse.setOnClickListener(mClickListener);

        //image view 에 glide로 이미지 넣기
        Glide.with(WaitingVideoActivity.this)
                .load(R.raw.mangtae)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(face);
    }

    // clicklistener 모음
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ibtn_waiting_video_accept:
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    //기능
                    Intent intentAccept = new Intent(WaitingVideoActivity.this, CallingVideoActivity.class);
                    startActivity(intentAccept);
                    finish();
                    break;

                case R.id.ibtn_waiting_video_refuse:
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    finish();
                    break;
            }
        }
    };
}