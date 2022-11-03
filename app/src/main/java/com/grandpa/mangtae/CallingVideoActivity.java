package com.grandpa.mangtae;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class CallingVideoActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PERMISSIONS = 10;
    public static final String REQUIRED_PERMISSIONS = Manifest.permission.CAMERA;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;


    //btn
    ImageButton exit;
    //img
    ImageView video;
    //media
    MediaPlayer mediaPlayer = new MediaPlayer();
    PreviewView previewView;
    Intent receivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling_video);

        receivedIntent = getIntent();
        String audioPath = receivedIntent.getStringExtra("audioPath");
        Log.d("audioPath"," : "+audioPath);

        if (allPermissionsGranted()) {
            startCamera(); // 카메라 실행
        } else {
            ActivityCompat.requestPermissions(
                    this, new String[]{REQUIRED_PERMISSIONS}, REQUEST_CODE_PERMISSIONS);
        }

        previewView = findViewById(R.id.camera_preview);
        //btn list
        //exit = 전화종료
        exit = findViewById(R.id.ibtn_calling_video_exit);

        //img
        video = findViewById(R.id.iv_video_mangtae);

        //clickListener 할당
        exit.setOnClickListener(mClickListener);

        playVideo(audioPath);

    }

    View.OnClickListener mClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    //망태할아버지 목소리 release version
    public void playVideo(String fileName) {
        File file = new File("/data/data/com.grandpa.mangtae/files/"+ fileName);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.fromFile(file));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Glide.with(this)
                .asGif()  // Load as animated GIF
                .load(R.raw.batman_monster)  // Call your GIF here (url, raw, etc.)
                .into(video);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                Glide.with(CallingVideoActivity.this)
                        .load(R.raw.batman_monster_0)  // Call your GIF here (url, raw, etc.)
                        .into(video);

                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });
    }

    //권한 체크
    public boolean allPermissionsGranted() {
        int permission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS);
        if(permission == PackageManager.PERMISSION_DENIED) {
            //권한 없음
            return false;
        }else {
            //권한 있음
            return true;
        }
    }

    //권한 요청 결과
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //동의 했을 경우
                } else {
                    //거부했을 경우
                    Toast toast = Toast.makeText(this, "기능 사용을 위한 권한 동의가 필요합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                break;
        }
    }

    //카메라 실행
    public void startCamera() {
        //1. cameraProvider 요청
        //ProcessCameraProvider는 Camera의 생명주기를 LifeCycleOwner의 생명주기에 Binding함
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        //2. CameraProvider를 요청한 후, 뷰를 만들 때 초기화에 성공했는지 확인
        cameraProviderFuture.addListener(() -> {
            //3. 카메라를 선택하고 생명주기에 binding
            //3-1 Preview를 만든다 -> Preview를 통해서 카메라 미리보기 화면을 구현
            //surfaceProvider는 데이터를 받을 준비가 되었다는 신호를 카메라에게 보내준다.
            //setSurfaceProvider는 PreviewView에 SurfaceProvider를 제공해 준다.

            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        //카메라를 선택하는 부분
        //CameraSelector.LENS_FACING_FRONT -> 전방카메라
        //CameraSelector.LENS_FACING_BACK -> 후방카메라
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
    }
}