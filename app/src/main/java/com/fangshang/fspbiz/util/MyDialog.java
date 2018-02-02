package com.fangshang.fspbiz.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.fangshang.fspbiz.R;

/**
 * Created by xiong on 2018/1/5/005 15:56
 */

public class MyDialog extends Dialog {
    private String title;
    private String content;
    TextView mTv_content;
    TextView mTv_title;
    public MyDialog(@NonNull Context context,String title,String content) {
        super(context);
        this.title =title;
        this.content =content;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_public);
        setCanceledOnTouchOutside(false);
        mTv_content =findViewById(R.id.tv_content);
        mTv_title =findViewById(R.id.tv_title);
        mTv_content.setText(content);
        mTv_title.setText(title);

//        if(this.isShowing()){
//            changeButtonStatus();
//        }
    }

}
