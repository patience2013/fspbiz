package com.fangshang.fspbiz.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseActivity;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.service.DownloadService;
import com.fangshang.fspbiz.util.DialogHelper;
import com.fangshang.fspbiz.util.TDevice;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class UpdateActivity extends BaseActivity implements View.OnClickListener,
        EasyPermissions.PermissionCallbacks {
    @BindView(R.id.tv_update_info)
    TextView mTextUpdateInfo;
    private HttpResponseStruct.Version mVersion;
    private static final int RC_EXTERNAL_STORAGE = 0x04;//存储权限

    public static void show(Activity activity, HttpResponseStruct.Version version) {
        Intent intent = new Intent(activity, UpdateActivity.class);
        intent.putExtra("version", version);
        activity.startActivityForResult(intent, 0x01);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_update;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void initData() {
        super.initData();
        setTitle("");
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mVersion = (HttpResponseStruct.Version) getIntent().getSerializableExtra("version");
        mTextUpdateInfo.setText(Html.fromHtml(mVersion.upgradeTxt));
    }

    @OnClick({R.id.btn_update, R.id.btn_cancel})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                if (!TDevice.isWifiOpen()) {
                    DialogHelper.getConfirmDialog(this, "当前非wifi环境，是否升级？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestExternalStorage();
                            finish();
                        }
                    }).show();
                } else {
                    requestExternalStorage();
                    finish();
                }
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }

    }

    @AfterPermissionGranted(RC_EXTERNAL_STORAGE)
    public void requestExternalStorage() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            DownloadService.startService(this, mVersion.url);
        } else {
            EasyPermissions.requestPermissions(this, "", RC_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        DialogHelper.getConfirmDialog(this, "温馨提示", "需要开启房算盘对您手机的存储权限才能下载安装，是否现在开启", "去开启", "取消", true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
            }
        }, null).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
