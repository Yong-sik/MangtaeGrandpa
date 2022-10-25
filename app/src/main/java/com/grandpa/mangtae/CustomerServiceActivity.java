package com.grandpa.mangtae;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CustomerServiceActivity extends AppCompatActivity {

    //btn
    Button contact, faq;

    //img
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);

        //변수 초기화 및 할당
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //홈화면 설정
        bottomNavigationView.setSelectedItemId(R.id.customer_service);

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
//                    case R.id.

                    //고객센터
                    case R.id.customer_service:
                        return true;
                }
                return false;
            }
        });

        //btn list
        //contact = 문의하기 / faq = FAQ
        contact = findViewById(R.id.btn_cs_contact);
        faq = findViewById(R.id.btn_cs_faq);

        //img list
        //logo = 망태 로고
        logo = findViewById(R.id.iv_cs_logo);

        //각 view에 clicklistener 할당
        contact.setOnClickListener(mClickListener);
        faq.setOnClickListener(mClickListener);

//        logo view 에 glide로 이미지 넣기
        Glide.with(CustomerServiceActivity.this)
                .load(R.raw.mangtae)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(logo);

    }

    // clicklistener 모음
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_cs_contact:
                    //기능
                    Intent intentContact = new Intent(CustomerServiceActivity.this, ContactActivity.class);
                    startActivity(intentContact);
                    break;

                case R.id.btn_cs_faq:
                    Intent intentFaq = new Intent(CustomerServiceActivity.this, FaqActivity.class);
                    startActivity(intentFaq);
                    break;
            }
        }
    };
}