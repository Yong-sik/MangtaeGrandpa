package com.grandpa.mangtae;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.io.UnsupportedEncodingException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_call);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        buttonSave = findViewById(R.id.button_save);
        editTextCallingName = findViewById(R.id.editText_calling_name);
        editTextCallingContent = findViewById(R.id.editText_calling_content);

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

        RoomDB db = RoomDB.getInstance(getApplicationContext());

        // Room에 전화 내용 저장
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = new Date();
                System.out.println(date.toString());
                callingData.setName(editTextCallingName.getText().toString());
                callingData.setContent(editTextCallingContent.getText().toString());
                callingData.setCreated(date);
                callingData.setWriter("관리자"); //사용자
                // audioPath 추가 필요
                db.callingDao().insertAll(callingData);

                // 서버에 저장되어 있는 wav 파일을 식별하기 위한 유저 고유 번호로서 기능
                // 저장버튼 누르면 local db에서 해당 uid가 있는지 확인을 먼저 함
                // 있으면 가져와서 userID에 넣음
                // 없으면 randomUUID로 생성해서 userID에 넣고 db에 저장
                String userID = UUID.randomUUID().toString();
                String name = editTextCallingName.getText().toString();
                String content = editTextCallingContent.getText().toString().replace("\n","|");
//                String[] content = editTextCallingContent.getText().toString().split("\n");
                makeVoice(userID, name, content);
//                if(TextUtils.isEmpty(content)) {
//                    final String newLine = System.getProperty("line.separator");
//                    final String[] inputText = content.split(newLine);
//
////                    String outputText = "";
////                    for(int i = 0; i < inputText.length; i++) {
////                        outputText += inputText[i];
////                        if(i != inputText.length -1) {
////                            outputText += newLine;
////                        }
////                    }
//                    makeVoice(userID, name, inputText);
//                }


//                Intent testIntent = new Intent(MakingCallActivity.this, AddressBookActivity.class);
//                startActivity(testIntent);
//                finish();
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
//        try {
//            userID = URLEncoder.encode(userID, "utf-8");
//            name = URLEncoder.encode(name, "utf-8");
//            content = URLEncoder.encode(content, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        Toast.makeText(getApplicationContext(), "음성파일 생성 요청", Toast.LENGTH_LONG).show(); //(context, message, floating time)
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://121.134.145.84/makeVoice.php" + "?userID=" + userID + "&name=" + name + "&content=" + content,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response","response : "+response.toString());
                        try{
                            JSONObject jsonObject = new JSONObject(response);
//                            System.out.println("jsonObject: " + jsonObject);
                            if (jsonObject.getString("error").equals("false")) {
//                                Toast.makeText(getApplicationContext(), jsonObject.getString("path"), Toast.LENGTH_LONG).show(); //(context, message, floating time)
                                showDialog(jsonObject.getString("path"));
                            }

//                            JSONArray jsonArray = new JSONArray(response);
//                            System.out.println("error: " + jsonArray.getJSONObject(0).get("error"));
//                            for(int reviewListIndex=0; reviewListIndex<jsonArray.length(); reviewListIndex++){
//                                String message = jsonArray.getJSONObject(reviewListIndex).get("message").toString();
//                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); //(context, message, floating time)
//                            }
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
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void showDialog(String msg) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(MakingCallActivity.this)
                .setTitle("TTS완료")
                .setMessage(msg)
                .setPositiveButton("다운로드", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "다운로드 시작", Toast.LENGTH_LONG).show(); //(context, message, floating time)
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "취소 했음", Toast.LENGTH_LONG).show(); //(context, message, floating time)
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
}