package com.fangshang.fspbiz.fragment.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseBackActivity;
import com.fangshang.fspbiz.util.MyDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/6/006 14:10
 */

public class SetUserNameActivity extends BaseBackActivity {
    @BindView(R.id.et_username)
    EditText mEt_username;
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SetUserNameActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_setusername;
    }

    @Override
    protected void initView() {
        setTopTitle("设置用户名");
    }
    @OnClick({R.id.btn_submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_submit:
//                View view1 =View.inflate(this,R.layout.dialog_public,null);
//                DialogHelper.getConfirmDialog(this,"",view1,null).show();
                final MyDialog dialog =new MyDialog(this,"提交成功!","即将跳转到我的页面");
                dialog.show();
                new CountDownTimer(1500, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        dialog.dismiss();
                        finish();
                    }
                }.start();
                break;
        }
    }
}
