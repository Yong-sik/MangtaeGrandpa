package com.grandpa.mangtae;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.grandpa.mangtae.Room.CallingData;
import com.grandpa.mangtae.Room.RoomDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DetailCallActivity extends AppCompatActivity {

    Button buttonUpdate;
    EditText editTextCallingName;
    EditText editTextCallingContent;
    CallingData callingData = new CallingData();
    Intent receivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_call);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        buttonUpdate = findViewById(R.id.button_update);
        editTextCallingName = findViewById(R.id.editText_calling_name);
        editTextCallingContent = findViewById(R.id.editText_calling_content);

        receivedIntent = getIntent();
        int id = receivedIntent.getIntExtra("id", 0);
        System.out.println("id: " + id);
        getData(id);

        editTextCallingName.setText(callingData.name);
        editTextCallingContent.setText(callingData.content);
        //Spinner 선언
        Spinner testSpinner = findViewById(R.id.spinner);

        List<String> kinds2 = Arrays.asList(getResources().getStringArray(R.array.voice_category));

        //ArryaAdapter 객체 생성. 여기서는 List로 만든걸로 써 봄
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),R.layout.spinner_calling_category,kinds2);
        adapter.setDropDownViewResource(R.layout.spinner_calling_category);

        testSpinner.setAdapter(adapter);

        if(callingData.category.equals("음성")){
            testSpinner.setSelection(0);
        }
        else{
            testSpinner.setSelection(1);
        }


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
//                parent.get
            }
        });

        RoomDB db = RoomDB.getInstance(getApplicationContext());

        // Room에서 전화 내용 수정
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = new Date();
                System.out.println(date.toString());
                callingData.setName(editTextCallingName.getText().toString());
                callingData.setContent(editTextCallingContent.getText().toString());
                callingData.setUpdateDate(date);
                // audioPath 추가 필요
                db.callingDao().update(callingData.id, callingData.name, callingData.content, callingData.updateDate, callingData.category);

                Intent testIntent = new Intent(DetailCallActivity.this, AddressBookActivity.class);
                startActivity(testIntent);
                finish();
            }
        });
    }

    private void getData(int id){
        RoomDB db = RoomDB.getInstance(getApplicationContext());
        callingData = db.callingDao().loadById(id);
    }

}