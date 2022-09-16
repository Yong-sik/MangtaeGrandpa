package com.grandpa.mangtae;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.grandpa.mangtae.Room.CallingData;
import com.grandpa.mangtae.Room.RoomDB;

import java.util.ArrayList;

public class AddressBookActivity extends AppCompatActivity {

    CallingData callingData = new CallingData();
    ArrayList<CallingData> callingDataList;
    ArrayList<CallingData> filteredList;
    CallingAdapter callingAdapter;
    Button buttonTotal;
    Button buttonVoice;
    Button buttonVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonTotal = findViewById(R.id.button_total);
        buttonVoice = findViewById(R.id.button_voice);
        buttonVideo = findViewById(R.id.button_video);

        RecyclerView recyclerViewCallingList = findViewById(R.id.recyclerView_calling_list);
        callingAdapter = new CallingAdapter();
        callingAdapter.submitData(getData());
        recyclerViewCallingList.setAdapter(callingAdapter);

        filteredList = new ArrayList<>();

        buttonVoice.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                buttonVoice.setBackgroundResource(R.color.orange);
                buttonTotal.setBackgroundResource(R.drawable.button_left_round_dark_gray);
                buttonVideo.setBackgroundResource(R.drawable.button_right_round_dark_gray);
                filterVoice();
            }
        });

        buttonVideo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                buttonVoice.setBackgroundResource(R.color.dark_gray);
                buttonTotal.setBackgroundResource(R.drawable.button_left_round_dark_gray);
                buttonVideo.setBackgroundResource(R.drawable.button_right_round_orange);
                filterVideo();
            }
        });

        buttonTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonVoice.setBackgroundResource(R.color.dark_gray);
                buttonTotal.setBackgroundResource(R.drawable.button_left_round_orange);
                buttonVideo.setBackgroundResource(R.drawable.button_right_round_dark_gray);
                filterTotal();
            }
        });
    }

    private ArrayList<CallingData> getData(){
        RoomDB db = RoomDB.getInstance(getApplicationContext());
        callingDataList = new ArrayList<>();
        callingDataList = (ArrayList<CallingData>) db.callingDao().getAll();
        System.out.println("==============callingDataList==============");
        System.out.println(callingDataList);
        return callingDataList;
    }

    public void filterTotal(){
        callingAdapter.filterList(callingDataList);
    }

    public void filterVoice() {
        filteredList.clear();

        for (int i = 0; i < callingDataList.size(); i++) {
            if (callingDataList.get(i).category.equals("음성")) {
                filteredList.add(callingDataList.get(i));
            }
        }
        callingAdapter.filterList(filteredList);
    }

    public void filterVideo() {
        filteredList.clear();

        for (int i = 0; i < callingDataList.size(); i++) {
            if (callingDataList.get(i).category.equals("영상")) {
                filteredList.add(callingDataList.get(i));
            }
        }
        callingAdapter.filterList(filteredList);
    }
}