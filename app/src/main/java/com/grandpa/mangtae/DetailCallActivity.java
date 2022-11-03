package com.grandpa.mangtae;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.grandpa.mangtae.Room.CallingData;
import com.grandpa.mangtae.Room.RoomDB;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DetailCallActivity extends AppCompatActivity {

//    Button buttonUpdate;
//    EditText editTextCallingName;
//    EditText editTextCallingContent;
    TextView textViewCallingName, textViewCallingContent, textViewCallingCategory;
    CallingData callingData = new CallingData();
    Intent receivedIntent;
    MediaPlayer mediaPlayer = new MediaPlayer();
    RoomDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_call);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        buttonUpdate = findViewById(R.id.button_update);
//        editTextCallingName = findViewById(R.id.editText_calling_name);
//        editTextCallingContent = findViewById(R.id.editText_calling_content);
        textViewCallingName = findViewById(R.id.tv_calling_name);
        textViewCallingContent = findViewById(R.id.tv_calling_content);
        textViewCallingCategory = findViewById(R.id.tv_calling_category);

        receivedIntent = getIntent();
        int id = receivedIntent.getIntExtra("id", 0);
        System.out.println("id: " + id);
        getData(id);

//        editTextCallingName.setText(callingData.name);
//        editTextCallingContent.setText(callingData.content);
        textViewCallingName.setText(callingData.name);
        textViewCallingContent.setText(callingData.content);
        textViewCallingCategory.setText(callingData.category);

        //Spinner 선언
//        Spinner testSpinner = findViewById(R.id.spinner);
//
//        List<String> kinds2 = Arrays.asList(getResources().getStringArray(R.array.voice_category));
//
//        //ArryaAdapter 객체 생성. 여기서는 List로 만든걸로 써 봄
//        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),R.layout.spinner_calling_category,kinds2);
//        adapter.setDropDownViewResource(R.layout.spinner_calling_category);
//
//        testSpinner.setAdapter(adapter);
//
//        if(callingData.category.equals("음성")){
//            testSpinner.setSelection(0);
//        }
//        else{
//            testSpinner.setSelection(1);
//        }
//
//
//        testSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position==0){
//                    callingData.setCategory("음성");
//                }
//                else if(position==1){
//                    callingData.setCategory("영상");
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
////                parent.get
//            }
//        });
//
//        RoomDB db = RoomDB.getInstance(getApplicationContext());

        // Room에서 전화 내용 수정
//        buttonUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Date date = new Date();
//                System.out.println(date.toString());
//                callingData.setName(editTextCallingName.getText().toString());
//                callingData.setContent(editTextCallingContent.getText().toString());
//                callingData.setUpdateDate(date);
//                // audioPath 추가 필요
//                db.callingDao().update(callingData.id, callingData.name, callingData.content, callingData.updateDate, callingData.category);
//
//                Intent testIntent = new Intent(DetailCallActivity.this, AddressBookActivity.class);
//                startActivity(testIntent);
//                finish();
//            }
//        });

        //변수 초기화 및 할당
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //홈화면 설정
//        bottomNavigationView.setSelectedItemId(R.id.customer_service);
        //item 선택 리스너
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    //전화 받기
                    case R.id.recive_call:
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        if(callingData.category.equals("음성")) {
                            Intent intentVoice = new Intent(getApplicationContext(), WaitingVoiceActivity.class);
                            intentVoice.putExtra("id", callingData.id);
                            startActivity(intentVoice);
                            overridePendingTransition(0,0);
                            finish();
                        }else{
                            Intent intentVideo = new Intent(getApplicationContext(), WaitingVideoActivity.class);
                            intentVideo.putExtra("id", callingData.id);
                            startActivity(intentVideo);
                            overridePendingTransition(0,0);
                            finish();
                        }
                        return true;

                    //미리듣기
                    case R.id.preview_call:
                        //TODO 미리듣기를 반복해서 누를경우 예외처리 해야함
                        if(!mediaPlayer.isPlaying()) {
                            File file = new File("/data/data/com.grandpa.mangtae/files/"+ callingData.audioPath);
                            if(mediaPlayer == null) {
                                mediaPlayer = new MediaPlayer();
                            }
                            try {
                                mediaPlayer.setDataSource(getApplicationContext(), Uri.fromFile(file));
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                item.setIcon(R.drawable.ic_preview_stop);
                                item.setTitle("그만듣기");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //강제로 stop 시킬때는 동작 하지 않음
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                public void onCompletion(MediaPlayer mp) {
                                    mediaPlayer.stop();
                                    mediaPlayer.reset();
                                    item.setIcon(R.drawable.ic_preview_call);
                                    item.setTitle("미리듣기");
                                }
                            });
                        }else{
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            item.setIcon(R.drawable.ic_preview_call);
                            item.setTitle("미리듣기");
                        }
                        return true;

                    //전화 삭제
                    case R.id.delete_call:
                        if(mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                        }
                        deleteAlert();
                        return true;
                }
                return false;
            }
        });
    }

    private void getData(int id){
        db = RoomDB.getInstance(getApplicationContext());
        callingData = db.callingDao().loadById(id);
    }
    private void deleteAudio(String fileName) {
        try {
            File file = new File("/data/data/com.grandpa.mangtae/files/" + fileName);
            if(file.exists()) {
                file.delete();
                db.callingDao().delete(callingData.id);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }
    private void deleteAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이 전화를 삭제 할까요?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mediaPlayer.release();
                mediaPlayer = null;
                deleteAudio(callingData.audioPath);
            }
        });
        builder.setNegativeButton("아니요", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}