package com.fangshang.fspbiz.base;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangshang.fspbiz.R;

import java.util.ArrayList;

/**
 *  后退
 *
 * Created by zhucw on 2017/3/22.
 */

public abstract class BaseBackActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.layout_title;
    }

    private ImageView imageView;
    private TextView tv_title;
    private LinearLayout lin_back;
    private static ArrayList<Activity> activities = new ArrayList<Activity>();

    public void setTopTitle(String title){
        tv_title = (TextView) findViewById(R.id.tv_title);
        if(tv_title!=null)
            tv_title.setText(title);
        back();
    }

    /*初始化标题栏*/

    public void back(){
        imageView = (ImageView) findViewById(R.id.img_back);
        lin_back = (LinearLayout) findViewById(R.id.lin_back);
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }
}
