package com.grandpa.mangtae;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.grandpa.mangtae.Room.CallingData;
import com.grandpa.mangtae.Room.RoomDB;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        Spinner testSpinner = findViewById(R.id.spinner);

        List<String> kinds2 = Arrays.asList(getResources().getStringArray(R.array.voice_category));

        //ArryaAdapter 객체 생성. 여기서는 List로 만든걸로 써 봄
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),R.layout.spinner_calling_category,kinds2);
        adapter.setDropDownViewResource(R.layout.spinner_calling_category);
        testSpinner.setAdapter(adapter);

        testSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                callingData.setWriter("관리자");
                // audioPath 추가 필요
                db.callingDao().insertAll(callingData);

                Intent testIntent = new Intent(MakingCallActivity.this, AddressBookActivity.class);
                startActivity(testIntent);
                finish();
            }
        });
    }
}