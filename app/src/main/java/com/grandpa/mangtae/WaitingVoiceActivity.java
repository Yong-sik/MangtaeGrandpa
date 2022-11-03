package com.grandpa.mangtae;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.grandpa.mangtae.Room.CallingData;
import com.grandpa.mangtae.Room.RoomDB;

public class WaitingVoiceActivity extends AppCompatActivity {

    //btn
    ImageButton accept, refuse;
    //media
    MediaPlayer mediaPlayer = new MediaPlayer();
    //선택된 전화의 data를 담기위한 class
    CallingData callingData = new CallingData();
    Intent receivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_voice);

        receivedIntent = getIntent();
        int id = receivedIntent.getIntExtra("id", 0);
        System.out.println("id: " + id);
        getData(id);

        //btn list
        //accept = 전화받기 / refuse = 전화거부
        accept = findViewById(R.id.ibtn_waiting_voice_accept);
        refuse = findViewById(R.id.ibtn_waiting_voice_refuse);


        //각 view에 clicklistener 할당
        accept.setOnClickListener(mClickListener);
        refuse.setOnClickListener(mClickListener);

        //전화벨
        //TODO 여기도 반복재생으로 수정 필요
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
                    mediaPlayer = null;
                    //기능
                    //여기서 wav파일 path 넘기면 될듯?
                    Intent intentAccept = new Intent(WaitingVoiceActivity.this, CallingVoiceActivity.class);
                    intentAccept.putExtra("audioPath", callingData.audioPath);
                    startActivity(intentAccept);
                    finish();
                    break;

                case R.id.ibtn_waiting_voice_refuse:
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    finish();
                    break;
            }
        }
    };
    private void getData(int id){
        RoomDB db = RoomDB.getInstance(getApplicationContext());
        callingData = db.callingDao().loadById(id);
    }
}