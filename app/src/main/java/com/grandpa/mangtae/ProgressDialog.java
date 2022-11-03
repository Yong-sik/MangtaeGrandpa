package com.grandpa.mangtae;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressDialog extends Dialog {

    TextView progressText;
    ProgressBar progressBar;

    public ProgressDialog(Context context) {
        super(context);

        //dialog 제목 안보이게
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
        setCancelable(false); // 외부 클릭시 dialog창 종료를 막음

        progressText = findViewById(R.id.tv_progressText);
        progressBar = findViewById(R.id.progressBar);
    }

    public void setStateText(String msg) {
        progressText.setText(msg);
    }
    public void setProgressBarEnd() {
//        progressBar.setIndeterminateDrawable();
    }
}
