package com.fangshang.fspbiz.fragment.me.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseBackActivity;

import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/6/006 14:10
 */

public class AccountpassActivity extends BaseBackActivity {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AccountpassActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_accountpass;
    }

    @Override
    protected void initView() {
        setTopTitle("账号密码");
    }
    @OnClick({R.id.lin_account,R.id.lin_mobile,R.id.lin_pass})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_account:
                SetUserNameActivity.actionStart(this);
                break;
            case R.id.lin_mobile:
                ChangeMobileActivity.actionStart(this);
                break;
            case R.id.lin_pass:
                ChangePassActivity.actionStart(this);
                break;
        }
    }
}
