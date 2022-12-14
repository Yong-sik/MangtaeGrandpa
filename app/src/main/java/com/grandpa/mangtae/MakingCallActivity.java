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

    //download ??????
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

        // ???????????? ????????? ??????????????? ????????? ?????? ????????? ???.
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

        // ????????? ?????? ??????
        customProgressDialog = new ProgressDialog(this);
        // ????????? ?????? ??????
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //Spinner ??????
        Spinner categorySpinner = findViewById(R.id.spinner);

        List<String> kinds2 = Arrays.asList(getResources().getStringArray(R.array.voice_category));

        //ArryaAdapter ?????? ??????. ???????????? List??? ???????????? ??? ???
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),R.layout.spinner_calling_category,kinds2);
        adapter.setDropDownViewResource(R.layout.spinner_calling_category);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    callingData.setCategory("??????");
                }
                else if(position==1){
                    callingData.setCategory("??????");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        db = RoomDB.getInstance(getApplicationContext());

        // Room??? ?????? ?????? ??????
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Date date = new Date();
//                System.out.println(date.toString());
//                callingData.setName(editTextCallingName.getText().toString());
//                callingData.setContent(editTextCallingContent.getText().toString());
//                callingData.setCreated(date);
//                callingData.setWriter("?????????"); //?????????
//                callingData.setAudioPath();
//                // audioPath ?????? ??????
//                db.callingDao().insertAll(callingData);

                // ????????? ???????????? ?????? wav ????????? ???????????? ?????? ?????? ?????? ???????????? ??????
                // ???????????? ????????? local db?????? ?????? uid??? ????????? ????????? ?????? ???
                // ????????? ???????????? userID??? ??????
                // ????????? randomUUID??? ???????????? userID??? ?????? db??? ??????
                String userID = UUID.randomUUID().toString();
                String name = editTextCallingName.getText().toString();
                String content = editTextCallingContent.getText().toString().replace("\n","|");
                makeVoice(userID, name, content);
//                progressDialogTest();
            }
        });

        //?????? ????????? ??? ??????
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //????????? ??????
        bottomNavigationView.setSelectedItemId(R.id.create);

        //item ?????? ?????????
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    //?????????
                    case R.id.address_book:
                        startActivity(new Intent(getApplicationContext(), AddressBookActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                    //?????? ?????????
                    case R.id.create:
                        return true;

                    //????????????
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
        Toast.makeText(getApplicationContext(), "???????????? ?????? ??????", Toast.LENGTH_LONG).show(); //(context, message, floating time)
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://121.134.145.84/makeVoice.php" + "?userID=" + userID + "&name=" + name + "&content=" + content,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response","response : "+response.toString());
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                                customProgressDialog.setStateText("???????????? ???..."); // ?????????????????? ?????? ????????? ???????????? ?????????;;
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
            @Override //response??? UTF8??? ??????????????? ????????????
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(    // ???????????? ???????????? ????????? ????????????????????? ????????? ??????????????? ???????????? ???????????? ??????
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
        // ????????? ???????????? ?????? ?????? ????????? ??????
        if (!dir.exists()) {
            dir.mkdir();
        }

        // ???????????? ????????? ????????? ???????????? ??????????????? ????????????
        // ????????? ???????????? ????????? ?????? ?????? ????????????.
        if (!new File(Save_Path + "/" + File_Name).exists()) {

            dThread = new MakingCallActivity.DownloadThread(fileURL + "/" + File_Name,
                    Save_Path + "/" + File_Name);
            dThread.start();
        } else {
            showDownloadFile();
        }
    }

    // ???????????? ????????? ?????????
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

    // ???????????? ??? ????????? ???????????? ??????
    @SuppressLint("HandlerLeak")
    Handler mAfterDown = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            customProgressDialog.setStateText("?????? ????????? ??????");
            Date date = new Date();
            System.out.println(date.toString());
            callingData.setName(editTextCallingName.getText().toString());
            callingData.setContent(editTextCallingContent.getText().toString());
            callingData.setCreated(date);
            callingData.setWriter("?????????"); //?????????
            //"/data/data/com.grandpa.mangtae/files/" ???????????? ??????????????? ?????? ????????????????????
            //mediaplayer????????? ????????? ?????? ???????????? ????????? ???????????
            callingData.setAudioPath(File_Name);
            db.callingDao().insertAll(callingData);
            // ?????? ???????????? ?????? ??? ???????????? ????????? ???????????????.
//            showDownloadFile();
            // ???????????? ????????? progress??????????????? ??? ??????????????? ???????????????...
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //??????
                    customProgressDialog.dismiss();

                    // TODO 1??? ?????? ?????? activity ?????? ???????????? ?????? ?????? ??????
                    Intent intent = new Intent(getApplicationContext(), AddressBookActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }

    };

    // ???????????? ??? ????????? ???????????? ??????
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