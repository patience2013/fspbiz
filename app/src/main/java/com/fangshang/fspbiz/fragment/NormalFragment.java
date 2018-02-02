package com.fangshang.fspbiz.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fangshang.fspbiz.App;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.base.BaseFragment;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2017/12/16/016 17:20
 */

public class NormalFragment extends BaseFragment{
    @BindView(R.id.tv_text)
    TextView tv_text;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_normal;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String title =getArguments().getString("title");
        tv_text.setText(title);
        if(savedInstanceState!=null){
//            String title =savedInstanceState.getString("title");
        }
//



    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
