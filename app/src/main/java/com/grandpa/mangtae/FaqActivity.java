package com.grandpa.mangtae;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class FaqActivity extends AppCompatActivity {

    Button contact, expand1, expand2, expand3;
    ImageButton back;
    TextView expand1Text, expand2Text, expand3Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        //btn list
        //contact = 문의하기 / back = 뒤로가기(CustomerServiceActivity)
        //expand = 내용들
        contact = findViewById(R.id.btn_faq_contact);
        expand1 = findViewById(R.id.btn_faq_expand1);
        expand2 = findViewById(R.id.btn_faq_expand2);
        expand3 = findViewById(R.id.btn_faq_expand3);
        back = findViewById(R.id.ibtn_faq_back);

        //text view list
        expand1Text = findViewById(R.id.tv_faq_expand1);
        expand2Text = findViewById(R.id.tv_faq_expand2);
        expand3Text = findViewById(R.id.tv_faq_expand3);

        //각 view에 clicklistener 할당
        contact.setOnClickListener(mClickListener);
        back.setOnClickListener(mClickListener);
        expand1.setOnClickListener(mClickListener);
        expand2.setOnClickListener(mClickListener);
        expand3.setOnClickListener(mClickListener);

    }

    // clicklistener 모음
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                //뒤로가기
                case R.id.ibtn_faq_back:
                    finish();
                    break;

                //문의하기
                case R.id.btn_faq_contact:
                    Intent intentCustomerService = new Intent(FaqActivity.this, ContactActivity.class);
                    startActivity(intentCustomerService);
                    finish();
                    break;

                //1번 FAQ
                case R.id.btn_faq_expand1:
                    //기능
                    expandText(expand1Text);
                    break;

                //2번 FAQ
                case R.id.btn_faq_expand2:
                    expandText(expand2Text);
                    break;

                //3번 FAQ
                case R.id.btn_faq_expand3:
                    expandText(expand3Text);
                    break;

            }
        }
    };

    public void expandText(TextView textView) {
        if(textView.getVisibility()==View.VISIBLE){
            textView.setVisibility(View.GONE);
            textView.animate().setDuration(200).rotation(180f);
        }else{
            textView.setVisibility(View.VISIBLE);
            textView.animate().setDuration(200).rotation(0f);
        }
    }

}