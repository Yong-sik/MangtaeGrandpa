package com.grandpa.mangtae;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class WaitingVoiceActivity extends AppCompatActivity {

    //btn
    ImageButton accept, refuse;

    //media
    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_voice);

        //btn list
        //accept = 전화받기 / refuse = 전화거부
        accept = findViewById(R.id.ibtn_waiting_voice_accept);
        refuse = findViewById(R.id.ibtn_waiting_voice_refuse);


        //각 view에 clicklistener 할당
        accept.setOnClickListener(mClickListener);
        refuse.setOnClickListener(mClickListener);

        //전화벨
        mediaPlayer = MediaPlayer.create(this, R.raw.ddururururu);
        mediaPlayer.start();

    }

    // clicklistener 모음
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ibtn_waiting_voice_accept:
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    //기능
                    //여기서 wav파일 path 넘기면 될듯?
                    Intent intentAccept = new Intent(WaitingVoiceActivity.this, CallingVoiceActivity.class);
                    startActivity(intentAccept);
                    finish();
                    break;

                case R.id.ibtn_waiting_voice_refuse:
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    finish();
                    break;
            }
        }
    };
}