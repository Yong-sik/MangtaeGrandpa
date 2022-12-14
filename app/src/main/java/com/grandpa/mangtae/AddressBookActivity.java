package com.grandpa.mangtae;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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

        //?????? ????????? ??? ??????
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //????????? ??????
        bottomNavigationView.setSelectedItemId(R.id.address_book);

        //item ?????? ?????????
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    //?????????
                    case R.id.address_book:
                        return true;

                    //?????? ?????????
                    case R.id.create:
                        startActivity(new Intent(getApplicationContext(), MakingCallActivity.class));
                        overridePendingTransition(0,0);
                        finish();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        callingAdapter.submitData(getData());
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
            if (callingDataList.get(i).category.equals("??????")) {
                filteredList.add(callingDataList.get(i));
            }
        }
        callingAdapter.filterList(filteredList);
    }

    public void filterVideo() {
        filteredList.clear();

        for (int i = 0; i < callingDataList.size(); i++) {
            if (callingDataList.get(i).category.equals("??????")) {
                filteredList.add(callingDataList.get(i));
            }
        }
        callingAdapter.filterList(filteredList);
    }
}