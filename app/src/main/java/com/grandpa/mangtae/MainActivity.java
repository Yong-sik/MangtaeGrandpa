package com.grandpa.mangtae;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //변수 초기화 및 할당
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //홈화면 설정
        bottomNavigationView.setSelectedItemId(R.id.address_book);

        //item 선택 리스너
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    //주소록
                    case R.id.address_book:
                        return true;

                    //전화 만들기
//                    case R.id.

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


//        Intent intent = new Intent(MainActivity.this, WaitingVideoActivity.class);
//        startActivity(intent);
    }
}