package com.grandpa.mangtae;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.grandpa.mangtae.Room.CallingData;
import com.grandpa.mangtae.Room.RoomDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class RoomTestActivity extends AppCompatActivity implements View.OnClickListener {

    String File_Name = "whynobab.wav";
    String File_extend = "wav";

    String fileURL = "http://13.209.221.111/"; // URL
    String Save_Path;
    String Save_folder = "/mydown";

    ProgressBar loadingBar;
    DownloadThread dThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_test);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Button btn = (Button) findViewById(R.id.downbtn);
        btn.setOnClickListener(this);

        loadingBar = (ProgressBar) findViewById(R.id.Loading);

        // 다운로드 경로를 외장메모리 사용자 지정 폴더로 함.
        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            Save_Path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + Save_folder;
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if (view.getId() == R.id.downbtn) {
            File dir = new File(Save_Path);
            // 폴더가 존재하지 않을 경우 폴더를 만듦
            if (!dir.exists()) {
                dir.mkdir();
            }

            // 다운로드 폴더에 동일한 파일명이 존재하는지 확인해서
            // 없으면 다운받고 있으면 해당 파일 실행시킴.
            if (!new File(Save_Path + "/" + File_Name).exists()) {
                loadingBar.setVisibility(View.VISIBLE);
                dThread = new DownloadThread(fileURL + "/" + File_Name,
                        Save_Path + "/" + File_Name);
                dThread.start();
            } else {
                showDownloadFile();
            }
        }
    }

    // 다운로드 쓰레드로 돌림..
    class DownloadThread extends Thread {
        String ServerUrl;
        String LocalPath;

        DownloadThread(String serverPath, String localPath) {
            ServerUrl = serverPath;
            LocalPath = localPath;
        }

        @Override
        public void run() {
            URL imgurl;
            int Read;
            try {
                imgurl = new URL(ServerUrl);
                HttpURLConnection conn = (HttpURLConnection) imgurl
                        .openConnection();
                int len = conn.getContentLength();
                byte[] tmpByte = new byte[len];
                InputStream is = conn.getInputStream();
                File file = new File(LocalPath);
                FileOutputStream fos = openFileOutput(File_Name, Context.MODE_PRIVATE);

                for (;;) {
                    Read = is.read(tmpByte);
                    if (Read <= 0) {
                        break;
                    }
                    fos.write(tmpByte, 0, Read);
                }
                is.close();
                fos.close();
                conn.disconnect();
                System.out.println("여까지 옴");

            } catch (MalformedURLException e) {
                Log.e("ERROR1", e.getMessage());
            } catch (IOException e) {
                Log.e("ERROR2", e.getMessage());
                e.printStackTrace();
            }
            mAfterDown.sendEmptyMessage(0);
        }
    }

    @SuppressLint("HandlerLeak")
    Handler mAfterDown = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            loadingBar.setVisibility(View.GONE);
            // 파일 다운로드 종료 후 다운받은 파일을 실행시킨다.
            showDownloadFile();
        }

    };

    private void showDownloadFile() {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        System.out.println("Save_Path: " + Save_Path);
        @SuppressLint("SdCardPath")
        File file = new File("/data/data/com.grandpa.mangtae/files/"+ File_Name);
        MediaPlayer mediaPlayer = new MediaPlayer();
//        Toast.makeText(getApplicationContext(), String.valueOf(Uri.fromFile(file)) , Toast.LENGTH_SHORT).show(); //(context, message, floating time)
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.fromFile(file));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}