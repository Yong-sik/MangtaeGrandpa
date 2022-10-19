package com.grandpa.mangtae;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ContactActivity extends AppCompatActivity {

    //btn
    Button send;
    ImageButton back;

    //edittext
    EditText mail, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        //btn list
        //send = 메일발송 / back = 뒤로가기(CustomerServiceActivity)
        send = findViewById(R.id.btn_contact_send);
        back = findViewById(R.id.ibtn_contact_back);

        //edit text list
        //mail = 받을메일 / content = 메일내용
        mail = findViewById(R.id.et_contact_mail);
        content = findViewById(R.id.et_contact_content);

        //각 view에 clicklistener 할당
        send.setOnClickListener(mClickListener);
        back.setOnClickListener(mClickListener);
    }

    // clicklistener 모음
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_contact_send:
                    //메일전송 gmailsender 에 송신자 id pwd필요해버림;;

                    //address / content string으로 가져옴
                    String mailText = mail.getText().toString();
                    String contentText = content.getText().toString();

                    if(mailText.length()==0 || contentText.length()==0 ){
                        //내용이 없어요
                        AlertDialog.Builder emptyText = new AlertDialog.Builder(ContactActivity.this);
                        emptyText.setTitle("알림");
                        emptyText.setMessage("메일주소와 내용은 필수 입력사항 입니다");
                        emptyText.setNegativeButton("확인",null);
                        emptyText.create().show();
                    }else{
                        //메일전송 app과 연동
                        sendMail(contentText);
                    }
                    break;

                case R.id.ibtn_contact_back:
                    finish();
                    break;
            }
        }
    };

    public void sendMail(String content) {
        Intent intentMail = new Intent(Intent.ACTION_SEND);
        intentMail .setType("*/*");

        intentMail.putExtra(Intent.EXTRA_EMAIL, "buskonjiyoon@gmail.com");
        intentMail.putExtra(Intent.EXTRA_SUBJECT, "문의 합니다.");
        intentMail.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(intentMail);
    }


}