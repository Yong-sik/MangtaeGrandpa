package com.grandpa.mangtae;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class CallingVideoActivity extends AppCompatActivity {

    ImageButton exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling_video);

        exit = findViewById(R.id.ibtn_calling_video_exit);

        exit.setOnClickListener(mClickListener);

    }

    View.OnClickListener mClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}