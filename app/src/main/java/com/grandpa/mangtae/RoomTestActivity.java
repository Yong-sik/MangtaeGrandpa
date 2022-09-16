package com.grandpa.mangtae;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.grandpa.mangtae.Room.CallingData;
import com.grandpa.mangtae.Room.RoomDB;

import java.util.Date;

public class RoomTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_test);

        TextView textView = findViewById(R.id.textView);
        EditText editText = findViewById(R.id.editText);
        Button buttonAdd = findViewById(R.id.button_add);
        Button buttonUpdate = findViewById(R.id.button_update);
        Button buttonDelete = findViewById(R.id.button_delete);


        RoomDB db = RoomDB.getInstance(getApplicationContext());

        // 추가
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallingData callingData = new CallingData();

                Date date = new Date();
                System.out.println(date.toString());
                callingData.setName(editText.getText().toString());
                callingData.setCreated(date);
                db.callingDao().insertAll(callingData); //editText에 삽입 후 버튼을 누르면
                textView.setText(db.callingDao().getAll().toString()); //textView에 불러옴
            }
        });

        // 수정
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallingData callingData = new CallingData();

                Date date = new Date();
                callingData.setName(editText.getText().toString());
//                db.callingDao().update(1, callingData.name, date); //editText에 삽입 후 버튼을 누르면
                textView.setText(db.callingDao().getAll().toString()); //textView에 불러옴
            }
        });

        // 삭제
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallingData callingData = new CallingData();

                callingData.setName(editText.getText().toString());
                db.callingDao().delete(1); //editText에 삽입 후 버튼을 누르면
                textView.setText(db.callingDao().getAll().toString()); //textView에 불러옴
            }
        });
    }
}