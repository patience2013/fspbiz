package com.fangshang.fspbiz.fragment.housing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.fangshang.fspbiz.App;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.activity.LoginActivity;
import com.fangshang.fspbiz.activity.login.adapter.FullyGridLayoutManager;
import com.fangshang.fspbiz.base.BaseBackActivity;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.fragment.housing.adapter.GridVideoAdapter;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.AuthUtil;
import com.fangshang.fspbiz.util.Config;
import com.fangshang.fspbiz.util.DialogHelper;
import com.fangshang.fspbiz.util.TsUtils;
import com.fangshang.fspbiz.util.ZhuanHuanUtil;
import com.google.gson.Gson;
import com.guoxiaoxing.phoenix.compress.video.VideoCompressor;
import com.guoxiaoxing.phoenix.compress.video.format.MediaFormatStrategyPresets;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadEngine;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.UpYunUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by xiong on 2018/1/19/019 11:42
 */
@RuntimePermissions
public class AddBuildActivity extends BaseBackActivity implements AMapLocationListener {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.img_addvideo)
    ImageView mImg_addvideo;
    @BindView(R.id.lin_progress)
    LinearLayout mLin_progress;
    @BindView(R.id.web_add_build)
    WebView mWeb_add_build;

    private GridVideoAdapter adapter;
    private int maxSelectNum = 1;               //允许选择图片最大数
    private List<LocalMedia> selectList = new ArrayList<>();

    private List<File> files = new ArrayList<>();
    Gson gson =new Gson();
    private String fileUrl;

    //空间名
    public static String SPACE = "guanlouyi";

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;

    public String mLatitude="";//纬度
    public String mLongitude="";//纬度
    public String buildId="";//楼盘id



    public static void actionStart(Context context,String buildId) {
        Intent intent = new Intent(context, AddBuildActivity.class);
        intent.putExtra("buildId",buildId);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_addbuild;
    }

    @Override
    protected void initView() {
        super.initView();
        buildId =getIntent().getStringExtra("buildId");
        AddBuildActivityPermissionsDispatcher.initLocationWithPermissionCheck(this);
        setTopTitle("添加楼盘");
        initAdapter();

        WebSettings mWebSettings = mWeb_add_build.getSettings();
        mWebSettings.setJavaScriptEnabled(true);//设置支持Javascript
        mWebSettings.setUseWideViewPort(true);//设置自适应屏幕
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWeb_add_build.addJavascriptInterface(new JsInterface(this), "android");
        //添加客户端支持
        mWeb_add_build.setWebChromeClient(new WebChromeClient());

        mWeb_add_build.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                sendInfoToJs();
            }
        });
//        progressBarSum.setMax(100);
        mWeb_add_build.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                progressBarSum.setProgress(newProgress);
                if (newProgress >= 100) {
//                    progressBarSum.setVisibility(View.GONE);
                }
            }
        });
        mWeb_add_build.loadUrl(App.H5_HOST+"newBuilding.html?create");
        try {
            getVideoThum("/fsp/video/201801/24/1516792803703.mp4 ");
        } catch (Exception e) {
            e.printStackTrace();
        }

        initLocation();
    }
    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void initLocation(){
        //声明mlocationClient对象
        mlocationClient = new AMapLocationClient(this);
//声明mLocationOption对象
        mLocationOption = new AMapLocationClientOption();
//设置返回地址信息，默认为true
        mLocationOption.setNeedAddress(true);
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //启动定位
        mlocationClient.startLocation();
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Logger.d("定位sdk");
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                mLatitude =aMapLocation.getLatitude()+"";//获取纬度
                mLongitude =aMapLocation.getLongitude()+"";//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                Logger.d("纬度"+aMapLocation.getLatitude()+"经度"+aMapLocation.getLongitude());
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Logger.d("ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
    //在java中调用js代码
    public void sendInfoToJs() {
        //调用js中的函数：showInfoFromJava(msg)
        if("".equals(buildId)){
            mWeb_add_build.loadUrl("javascript:getRegion('" + AccountHelper.getUser().getToken() +","+AccountHelper.getUser().getBussType()+","+mLongitude +","+mLatitude+"')");
        }else{
            mWeb_add_build.loadUrl("javascript:getRegion('" + AccountHelper.getUser().getToken() +","+AccountHelper.getUser().getBussType()+","+mLongitude +","+mLatitude+","+buildId+"')");
        }

    }
    public void getVideoImsList(String path){
//        TsUtils.show(path);
        mWeb_add_build.loadUrl("javascript:getVideoImsList('" + path+"')");
    }
    public void initAdapter(){
        //新的图片选择器
        FullyGridLayoutManager manager = new FullyGridLayoutManager(AddBuildActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridVideoAdapter(AddBuildActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(AddBuildActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(AddBuildActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(AddBuildActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
    }
    @OnClick({R.id.img_addvideo,R.id.lin_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_addvideo:
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(0);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(AddBuildActivity.this).externalPicturePreview(0, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(AddBuildActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(AddBuildActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }else{
                    PictureSelector.create(AddBuildActivity.this)
                            .openCamera(PictureMimeType.ofVideo())
                            .previewVideo(true)
                            .videoQuality(0)
                            .videoMaxSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                            .videoMinSecond(5)// 显示多少秒以内的视频or音频也可适用 int
                            .recordVideoSecond(10)//视频秒数录制 默认60s int
                            .forResult(PictureConfig.CHOOSE_REQUEST);
                }
                break;
            case R.id.lin_back:
                if (mWeb_add_build.canGoBack()) {
                    mWeb_add_build.goBack();
                }else{
                    DialogHelper.getConfirmDialog(this, getString(R.string.the_info_is_not_already_save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
                }
                break;
        }
    }
    public void upImage(File file){
        //表单上传
        final Map<String, Object> paramsMap = new HashMap<>();
        //上传空间
        paramsMap.put(Params.BUCKET, SPACE);
        //保存路径，任选其中一个
        paramsMap.put(Params.SAVE_KEY, "/fsp/image/" + ZhuanHuanUtil.getYearDate() + "/" + new Date().getTime() + ".png");
//        paramsMap.put("apps",)
        //上传结果回调
        paramsMap.put(Params.CONTENT_MD5, UpYunUtils.md5Hex(file));
        UploadEngine.getInstance().formUpload(file, paramsMap, "newfsp", UpYunUtils.md5("newfsp123456"), new UpCompleteListener() {
            @Override
            public void onComplete(boolean isSuccess, String result) {
                Logger.d(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getString("code").equals("200")) {
                        mLin_progress.setVisibility(View.GONE);
                        fileUrl = obj.getString("url");
                        getVideoImsList(fileUrl);
                        Logger.d(fileUrl);
//                        httppost(imgUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new UpProgressListener() {
            @Override
            public void onRequestProgress(long bytesWrite, long contentLength) {
                Logger.d((100 * bytesWrite) / contentLength + "%");
            }
        });
    }
    public void upVideo(File file){
        //表单上传
        final Map<String, Object> paramsMap = new HashMap<>();
        //上传空间
        paramsMap.put(Params.BUCKET, SPACE);
        //保存路径，任选其中一个
        paramsMap.put(Params.SAVE_KEY, "/fsp/video/" + ZhuanHuanUtil.getYearDate() + "/" + new Date().getTime() + ".mp4");
//        paramsMap.put("apps",)
        //上传结果回调
        paramsMap.put(Params.CONTENT_MD5, UpYunUtils.md5Hex(file));
        //初始化JSONArray，上传预处理（异步）参数详见 http://docs.upyun.com/cloud/image/#_6，
        JSONArray array = new JSONArray();

        //初始化JSONObject
        JSONObject json = new JSONObject();

        final String imgUrl ="/fsp/video/" + ZhuanHuanUtil.getYearDate() + "/" + new Date().getTime()+".png";
        //json 添加 name 属性
        try {
            json.put("name", "naga");
            json.put("type", "thumbnail");
            json.put("avopts", "/o/true/f/png");

            //json 添加 save_as 属性
            json.put("save_as", imgUrl);

            //json 添加 notify_url 属性
            json.put("notify_url", "http://httpbin.org/post");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //将json 对象放入 JSONArray
        array.put(json);

        //添加异步作图参数 APPS
        paramsMap.put("apps", array);
        UploadEngine.getInstance().formUpload(file, paramsMap, "newfsp", UpYunUtils.md5("newfsp123456"), new UpCompleteListener() {
            @Override
            public void onComplete(boolean isSuccess, String result) {
                Logger.d(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getString("code").equals("200")) {
                        mLin_progress.setVisibility(View.GONE);
                        fileUrl = obj.getString("url");
                        getVideoImsList(fileUrl+","+imgUrl);
                        Logger.d(fileUrl);
//                        httppost(imgUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new UpProgressListener() {
            @Override
            public void onRequestProgress(long bytesWrite, long contentLength) {
                Logger.d((100 * bytesWrite) / contentLength + "%");
            }
        });
    }

    public void getVideoThum(String videopath) throws Exception {
        String key = "newfsp";
        String secret = AuthUtil.md5("newfsp123456");
        String method = "POST";
        String uri = "guanlouyi/snapshot";
        String data =AuthUtil.getRfc1123Time();
        HttpRequestStruct.JieTu jieTu =new HttpRequestStruct.JieTu(videopath,"00:00:00","/fsp/video/" + ZhuanHuanUtil.getYearDate() + "/" + new Date().getTime() + ".png");
        final String json =gson.toJson(jieTu);
        Logger.d(secret+"DATA___"+data+"SIGN____"+AuthUtil.sign(key,secret,method,uri,data,"","")+"JSON___"+json);
        OkGo.<String>post("http://p1.api.upyun.com/"+SPACE+"/snapshot/")
                .tag(this)
                .upJson(json)
                .headers("Date",AuthUtil.getRfc1123Time())
                .headers("Authorization", AuthUtil.sign(key,secret,method,uri,AuthUtil.getRfc1123Time(),"",""))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(json);
                            Logger.d(response.body().toString());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //新的图片选择器
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    files.clear();//清除图片或视频
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
//                        Log.i("图片-----》", media.getPath());
                        Logger.d("图片-----》"+media.getPath());
                        files.add(new File(media.getPath()));
                    }
                    yasuoVideo(selectList.get(0).getPath());
//                    yasuoImage(files.get(0));

//                    Glide.with(this).load(selectList.get(0).getPath()).into(mImg_addvideo);
//                    adapter.setList(selectList);
//                    adapter.notifyDataSetChanged();
                    break;
                case 1000:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    files.clear();//清除图片或视频
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
//                        Log.i("图片-----》", media.getPath());
                        Logger.d("图片-----》"+media.getPath());
                        files.add(new File(media.getPath()));
                    }
//                    yasuoVideo(selectList.get(0).getPath());
                    yasuoImage(files.get(0));

//                    Glide.with(this).load(selectList.get(0).getPath()).into(mImg_addvideo);
//                    adapter.setList(selectList);
//                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
    private GridVideoAdapter.onAddPicClickListener onAddPicClickListener = new GridVideoAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            boolean mode = true;
            if (mode) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(AddBuildActivity.this)
                        .openGallery(PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .previewVideo(true)// 是否可预览视频
                        .enablePreviewAudio(false) // 是否可播放音频
                        .isCamera(true)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                        .enableCrop(false)// 是否裁剪
                        .compress(true)// 是否压缩
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        //.compressSavePath(getPath())//压缩图片保存地址
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                        .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                        .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                        .circleDimmedLayer(false)// 是否圆形裁剪
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(false)// 是否开启点击声音
                        .selectionMedia(selectList)// 是否传入已选图片
                        .videoMaxSecond(10)
                        .videoMinSecond(5)
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled() // 裁剪是否可旋转图片
                        //.scaleEnabled()// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
//                        .videoSecond()//显示多少秒以内的视频or音频也可适用
                        .recordVideoSecond(10)//录制视频秒数 默认60s
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            } else {
//                // 单独拍照
//                PictureSelector.create(MainActivity.this)
//                        .openCamera(chooseMode)// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
//                        .theme(themeId)// 主题样式设置 具体参考 values/styles
//                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
//                        .minSelectNum(1)// 最小选择数量
//                        .selectionMode(cb_choose_mode.isChecked() ?
//                                PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
//                        .previewImage(cb_preview_img.isChecked())// 是否可预览图片
//                        .previewVideo(cb_preview_video.isChecked())// 是否可预览视频
//                        .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
//                        .isCamera(cb_isCamera.isChecked())// 是否显示拍照按钮
//                        .enableCrop(cb_crop.isChecked())// 是否裁剪
//                        .compress(cb_compress.isChecked())// 是否压缩
//                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                        .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
//                        .isGif(cb_isGif.isChecked())// 是否显示gif图片
//                        .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
//                        .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
//                        .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                        .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                        .openClickSound(cb_voice.isChecked())// 是否开启点击声音
//                        .selectionMedia(selectList)// 是否传入已选图片
//                        .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
//                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
//                        //.cropCompressQuality(90)// 裁剪压缩质量 默认为100
//                        .minimumCompressSize(100)// 小于100kb的图片不压缩
//                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
//                        //.rotateEnabled() // 裁剪是否可旋转图片
//                        //.scaleEnabled()// 裁剪是否可放大缩小图片
//                        //.videoQuality()// 视频录制质量 0 or 1
//                        //.videoSecond()////显示多少秒以内的视频or音频也可适用
//                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            }
        }

    };
//    public void getImage(String url){
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
////获取网络视频
////        retriever.setDataSource(url, new HashMap<String, String>());
////获取本地视频
//retriever.setDataSource(url);
//        Bitmap bitmap = retriever.getFrameAtTime();
//        FileOutputStream outStream = null;
//        outStream = new FileOutputStream(new File(AddBuildActivity.this.getExternalCacheDir().getAbsolutePath() + "/" + videoName + ".png"));
//        bitmap.compress(Bitmap.CompressFormat.PNG, 10, outStream);
//        outStream.close();
//        retriever.release();
//    }
    public void yasuoVideo(String path){
        final File compressFile;
        try {
            File compressCachePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fangsuanpan");
            compressCachePath.mkdir();
            compressFile = File.createTempFile("compress", ".mp4", compressCachePath);
        } catch (IOException e) {
            Toast.makeText(this, "Failed to create temporary file.", Toast.LENGTH_LONG).show();
            return;
        }
        VideoCompressor.Listener listener = new VideoCompressor.Listener() {
            @Override
            public void onTranscodeProgress(double progress) {
                mLin_progress.setVisibility(View.VISIBLE);
                Logger.d("progress"+progress);
            }

            @Override
            public void onTranscodeCompleted() {
                String compressPath = compressFile.getAbsolutePath();
                Logger.d(compressPath);
//                mLin_progress.setVisibility(View.GONE);
                upVideo(new File(compressPath));
            }

            @Override
            public void onTranscodeCanceled() {
                Logger.d("onTranscodeCanceled");

            }

            @Override
            public void onTranscodeFailed(Exception exception) {
                Logger.d(exception.toString());

            }
        };
        try {
            VideoCompressor.with().asyncTranscodeVideo(path, compressFile.getAbsolutePath(),
                    MediaFormatStrategyPresets.createAndroid480pFormatStrategy(), listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void yasuoImage(File file){
        Luban.with(AddBuildActivity.this)
                .load(file)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
//                .setTargetDir(getPath())                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        mLin_progress.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        upImage(file);
//                        upVideo(file);
                    }


                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩
    }



    //    public void yasuotongbu(String path){
//        final File compressFile;
//        try {
//            File compressCachePath = new File(this.getCacheDir(), "outputs");
//            compressCachePath.mkdir();
//            compressFile = File.createTempFile("compress", ".mp4", compressCachePath);
//        } catch (IOException e) {
//            Toast.makeText(this, "Failed to create temporary file.", Toast.LENGTH_LONG).show();
////            return null;
//        }
//
//        try {
//            String compressPath =  VideoCompressor.with().syncTranscodeVideo(path, compressFile.getAbsolutePath(),
//                    MediaFormatStrategyPresets.createAndroid480pFormatStrategy());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && mWeb_add_build.canGoBack()) {
            mWeb_add_build.goBack();
            return true;
        }else{
            DialogHelper.getConfirmDialog(this, getString(R.string.the_info_is_not_already_save), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            }).show();
        }
        return super.onKeyDown(keyCode, event);
    }
class JsInterface {
    private Context mContext;

    public JsInterface(Context context) {
        this.mContext = context;
    }

    @JavascriptInterface
    public String getToken() {
        if (!"".equals(AccountHelper.getUser().getToken())) {
            return AccountHelper.getUser().getToken();
        }
        return "";
    }

    @JavascriptInterface
    public void back() {
        TsUtils.show("提交成功");
        Intent intent = new Intent();
//        setResult(PactionManagementActivity.RESULT_BACK,intent);
        finish();
    }

    //登陆验证失败的
    @JavascriptInterface
    public void login() {
        TsUtils.show("登陆信息已过期，请重新登陆");
        Config.getInstance().set(Config.PASSWORD, "");
        Config.getInstance().set(Config.USERNAME, "");
        Config.getInstance().set(Config.TOKEN, "");
        AccountHelper.clearUserCache();
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
//        LoginActivity.actionStart(mContext);
    }

    //添加视频
    @JavascriptInterface
    public void getVideoThumAndUrl() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(AddBuildActivity.this)
                .openGallery(PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                        .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                .videoMaxSecond(10)
                .videoMinSecond(5)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled() // 裁剪是否可旋转图片
                //.scaleEnabled()// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
//                        .videoSecond()//显示多少秒以内的视频or音频也可适用
                .recordVideoSecond(10)//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }
    @JavascriptInterface
    public void getImageUrl() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(AddBuildActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                        .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                .videoMaxSecond(10)
                .videoMinSecond(5)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled() // 裁剪是否可旋转图片
                //.scaleEnabled()// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
//                        .videoSecond()//显示多少秒以内的视频or音频也可适用
                .recordVideoSecond(10)//录制视频秒数 默认60s
                .forResult(1000);//结果回调onActivityResult code
//        return "http://www.prc-magazine.com/sc/files/2012/08/Tianjin-Sales-Centre.jpg_"+fileUrl;
    }
}

    @Override
    protected void onStop() {
        super.onStop();
        mlocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        AddBuildActivity.onRequestPermissionsResult(this, requestCode, grantResults);
        AddBuildActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }
}
