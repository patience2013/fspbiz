package com.fangshang.fspbiz.fragment.me.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.fangshang.fspbiz.MainActivity;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseBackActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/6/006 14:10
 */

public class MeInfoActivity extends BaseBackActivity {
    private static int REQUEST_CODE_CHOOSE=1000;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MeInfoActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_meinfo;
    }

    @Override
    protected void initView() {
        setTopTitle("个人资料");
    }
    @OnClick({R.id.lin_head})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_head:
                Matisse.from(MeInfoActivity.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(9)
//                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
                break;
        }
    }
    List<Uri> mSelected;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }
}
