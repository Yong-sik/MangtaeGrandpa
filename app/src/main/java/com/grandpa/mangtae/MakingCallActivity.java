package com.grandpa.mangtae;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.grandpa.mangtae.Room.CallingData;
import com.grandpa.mangtae.Room.RoomDB;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MakingCallActivity extends AppCompatActivity {

    Button buttonSave;
    EditText editTextCallingName;
    EditText editTextCallingContent;
    CallingData callingData = new CallingData();
    RoomDB db;

    // progressdialog class
    ProgressDialog customProgressDialog;

    //download 관련
    String File_Name;
    String File_extend = "wav";
    String fileURL = "http://121.134.145.84/pitchShifted_wav"; // URL
    String Save_Path;
    String Save_folder = "/mydown";
    DownloadThread dThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_call);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // 다운로드 경로를 외장메모리 사용자 지정 폴더로 함.
        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            Save_Path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + Save_folder;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        buttonSave = findViewById(R.id.button_save);
        editTextCallingName = findViewById(R.id.editText_calling_name);
        editTextCallingContent = findViewById(R.id.editText_calling_content);

        // 로딩창 객체 생성
        customProgressDialog = new ProgressDialog(this);
        // 로딩창 투명 설정
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //Spinner 선언
        Spinner categorySpinner = findViewById(R.id.spinner);

        List<String> kinds2 = Arrays.asList(getResources().getStringArray(R.array.voice_category));

        //ArryaAdapter 객체 생성. 여기서는 List로 만든걸로 써 봄
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),R.layout.spinner_calling_category,kinds2);
        adapter.setDropDownViewResource(R.layout.spinner_calling_category);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    callingData.setCategory("음성");
                }
                else if(position==1){
                    callingData.setCategory("영상");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        db = RoomDB.getInstance(getApplicationContext());

        // Room에 전화 내용 저장
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Date date = new Date();
//                System.out.println(date.toString());
//                callingData.setName(editTextCallingName.getText().toString());
//                callingData.setContent(editTextCallingContent.getText().toString());
//                callingData.setCreated(date);
//                callingData.setWriter("관리자"); //사용자
//                callingData.setAudioPath();
//                // audioPath 추가 필요
//                db.callingDao().insertAll(callingData);

                // 서버에 저장되어 있는 wav 파일을 식별하기 위한 유저 고유 번호로서 기능
                // 저장버튼 누르면 local db에서 해당 uid가 있는지 확인을 먼저 함
                // 있으면 가져와서 userID에 넣음
                // 없으면 randomUUID로 생성해서 userID에 넣고 db에 저장
                String userID = UUID.randomUUID().toString();
                String name = editTextCallingName.getText().toString();
                String content = editTextCallingContent.getText().toString().replace("\n","|");
                makeVoice(userID, name, content);
//                progressDialogTest();
            }
        });

        //변수 초기화 및 할당
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //홈화면 설정
        bottomNavigationView.setSelectedItemId(R.id.create);

        //item 선택 리스너
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    //주소록
                    case R.id.address_book:
                        startActivity(new Intent(getApplicationContext(), AddressBookActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    //전화 만들기
                    case R.id.create:
                        return true;

                    //고객센터
                    case R.id.customer_service:
                        startActivity(new Intent(getApplicationContext(), CustomerServiceActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    private void makeVoice(String userID, String name, String content){
        customProgressDialog.show();
        Toast.makeText(getApplicationContext(), "음성파일 생성 요청", Toast.LENGTH_LONG).show(); //(context, message, floating time)
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://121.134.145.84/makeVoice.php" + "?userID=" + userID + "&name=" + name + "&content=" + content,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response","response : "+response.toString());
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                                customProgressDialog.setStateText("다운로드 중..."); // 오디오파일이 하도 작아서 보이지도 않는듯;;
                                audioDownload(jsonObject.getString("path"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        System.out.println("error: " + error.getMessage());
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }){
            @Override //response를 UTF8로 변경해주는 소스코드
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, StandardCharsets.UTF_8);
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                } catch (Exception e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(    // 음성파일 생성하는 도중에 클라이언트에서 요청을 끊어버려서 요청시간 늘려주는 부분
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void audioDownload(String fn) {
        File_Name = fn;
        File dir = new File(Save_Path);
        // 폴더가 존재하지 않을 경우 폴더를 만듦
        if (!dir.exists()) {
            dir.mkdir();
        }

        // 다운로드 폴더에 동일한 파일명이 존재하는지 확인해서
        // 없으면 다운받고 있으면 해당 파일 실행시킴.
        if (!new File(Save_Path + "/" + File_Name).exists()) {

            dThread = new MakingCallActivity.DownloadThread(fileURL + "/" + File_Name,
                    Save_Path + "/" + File_Name);
            dThread.start();
        } else {
            showDownloadFile();
        }
    }

    // 다운로드 쓰레드 클래스
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

            } catch (MalformedURLException e) {
                Log.e("ERROR1", e.getMessage());
            } catch (IOException e) {
                Log.e("ERROR2", e.getMessage());
                e.printStackTrace();
            }
            mAfterDown.sendEmptyMessage(0);
        }
    }

    // 다운로드 후 동작을 정의하는 부분
    @SuppressLint("HandlerLeak")
    Handler mAfterDown = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            customProgressDialog.setStateText("전화 만들기 완료");
            Date date = new Date();
            System.out.println(date.toString());
            callingData.setName(editTextCallingName.getText().toString());
            callingData.setContent(editTextCallingContent.getText().toString());
            callingData.setCreated(date);
            callingData.setWriter("관리자"); //사용자
            //"/data/data/com.grandpa.mangtae/files/" 이부분은 안변하는데 굳이 넣어줘야하나??
            //mediaplayer에다가 넣을때 앞에 넣어주면 되는거 아닌가??
            callingData.setAudioPath(File_Name);
            db.callingDao().insertAll(callingData);
            // 파일 다운로드 종료 후 다운받은 파일을 실행시킨다.
//            showDownloadFile();
            // 다운로드 다되서 progress없애는건데 슉 사라지니까 뭔가뭔가임...
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //동작
                    customProgressDialog.dismiss();

                    // TODO 1초 정도 후에 activity 이동 하는걸로 하면 될거 같음
                    Intent intent = new Intent(getApplicationContext(), AddressBookActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }

    };

    // 다운로드 후 확인용 함수인거 같음
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