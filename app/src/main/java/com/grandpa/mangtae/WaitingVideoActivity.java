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
import com.grandpa.mangtae.Room.CallingData;
import com.grandpa.mangtae.Room.RoomDB;

public class WaitingVideoActivity extends AppCompatActivity {

    ImageButton accept, refuse;
    ImageView face;
    MediaPlayer mediaPlayer = new MediaPlayer();
    //선택된 전화의 data를 담기위한 class
    CallingData callingData = new CallingData();
    Intent receivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_video);

        receivedIntent = getIntent();
        int id = receivedIntent.getIntExtra("id", 0);
        System.out.println("id: " + id);
        getData(id);

        accept = findViewById(R.id.ibtn_waiting_video_accept);
        refuse = findViewById(R.id.ibtn_waiting_video_refuse);

        face = findViewById(R.id.iv_waiting_video);

        //전화벨
        //TODO 반복재생으로 수정 필요
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
                    mediaPlayer = null;
                    //기능
                    Intent intentAccept = new Intent(WaitingVideoActivity.this, CallingVideoActivity.class);
                    intentAccept.putExtra("audioPath", callingData.audioPath);
                    startActivity(intentAccept);
                    finish();
                    break;

                case R.id.ibtn_waiting_video_refuse:
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