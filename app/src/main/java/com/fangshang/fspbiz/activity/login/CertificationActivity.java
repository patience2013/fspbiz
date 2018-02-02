package com.fangshang.fspbiz.activity.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.fangshang.fspbiz.MainActivity;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.activity.login.adapter.FullyGridLayoutManager;
import com.fangshang.fspbiz.activity.login.adapter.GridImageAdapter;
import com.fangshang.fspbiz.activity.login.imageadapter.ImagePickerAdapter;
import com.fangshang.fspbiz.activity.login.imageadapter.MyLayoutManager;
import com.fangshang.fspbiz.activity.login.imageadapter.SelectDialog;
import com.fangshang.fspbiz.base.BaseBackActivity;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.interfaceUrl.JsonCallBack;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.HttpUtil;
import com.fangshang.fspbiz.util.SelectAreaDialog;
import com.fangshang.fspbiz.util.TsUtils;
import com.fangshang.fspbiz.util.ZhuanHuanUtil;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadEngine;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.UpYunUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by xiong on 2018/1/15/015 9:36
 */
public class CertificationActivity extends BaseBackActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.tv_company_type)
    TextView mTv_company_type;
    @BindView(R.id.tv_provice_city)
    TextView mTv_provice_city;
    @BindView(R.id.recyclerView)
    RecyclerView mRecycleview;
    @BindView(R.id.tv_area)
    TextView mTv_area;
    @BindView(R.id.tv_userType)
    TextView mTv_userType;
    @BindView(R.id.lin_public)
    LinearLayout mLin_public;
    @BindView(R.id.lin_broker)
    LinearLayout mLin_broker;
    @BindView(R.id.lin_provice_area)
    LinearLayout mLin_provice_area;
    @BindView(R.id.et_company_name)
    EditText mEt_company_name;
    @BindView(R.id.et_company_address)
    EditText mEt_company_address;
    @BindView(R.id.et_name)
    EditText mEt_name;
    @BindView(R.id.et_mobile)
    EditText mEt_mobile;
    @BindView(R.id.et_broker_name)
    EditText mEt_broker_name;
    @BindView(R.id.et_broker_mobile)
    EditText mEt_broker_mobile;
    @BindView(R.id.lin_businesslicense)
    LinearLayout mLin_businesslicense;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private int maxSelectNum = 1;

    private ImagePickerAdapter imgaeAdapter;
    private int maxImgCount = 1;               //允许选择图片最大数
    private List<LocalMedia> selectList = new ArrayList<>();
    private ArrayList<ImageItem> selImageList = new ArrayList<ImageItem>();
    private OptionsPickerView pvCustomOptions;
    private List<String> type = new ArrayList<>();
    private ArrayList<HttpResponseStruct.Province> proviceItems = new ArrayList<>();
    private ArrayList<ArrayList<String>> cityItems = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> areaItems = new ArrayList<>();

    private List<HttpResponseStruct.Area> areas = new ArrayList<>();

    private Thread thread;
    private boolean isLoaded = false;
    public static final int IMAGE_ITEM_ADD = -1;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private static final int REQUEST_CODE_CHOOSE = 000000;

    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private List<File> files = new ArrayList<>();
    private Gson gson = new Gson();

    //空间名
    public static String SPACE = "guanlouyi";
    private static String imgUrl = "";
    int bussType;//用户注册类型 1.企业2.经纪人3 个人

    //请求认证的数据
    public int enterpriseType = 0; //企业类型 1.企业招商2.中介公司
    public String enterpriseName;//企业名称
    public String enterpriseAddress;//企业地址
    public String contactPerson;//对接人姓名
    public String contactTele;//对接人手机
    public String enterpriseLicense;//营业执照
    public String bussRegion = ""; //业务区域
    public String ownerName; //业主姓名
    public String ownerTele;//业主手机
    public String agentName; //经纪人姓名
    public String agentTele;//经纪人手机

    private HttpResponseStruct.UserIdentityDetail userIdentityDetail;


    public static void actionStart(Context context, int bussType, HttpResponseStruct.UserIdentityDetail userIdentityDetail) {
        Intent intent = new Intent(context, CertificationActivity.class);
        intent.putExtra("bussType", bussType);
        intent.putExtra("userIdentityDetail",userIdentityDetail);
        context.startActivity(intent);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        Toast.makeText(CertificationActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
//                                initJsonData();

                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    Toast.makeText(CertificationActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(CertificationActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_certification;
    }

    @Override
    protected void initView() {
        //根据用户类型判断认证方式
        bussType = getIntent().getIntExtra("bussType", 0);
        userIdentityDetail = (HttpResponseStruct.UserIdentityDetail) getIntent().getSerializableExtra("userIdentityDetail");
        if(userIdentityDetail!=null){
            if(!"".equals(userIdentityDetail.enterpriseLicense)){
                LocalMedia localMedia=new LocalMedia();
                localMedia.setPath(userIdentityDetail.enterpriseLicense);
                localMedia.setCompressPath(userIdentityDetail.enterpriseLicense);

                selectList.add(localMedia);
            }
        }

        if (bussType == 1) {
            mTv_userType.setText("公司");
            mLin_broker.setVisibility(View.GONE);
            mLin_public.setVisibility(View.VISIBLE);
        } else if (bussType == 2) {
            mTv_userType.setText("经纪人");
            mLin_broker.setVisibility(View.VISIBLE);
            mLin_public.setVisibility(View.GONE);
            mLin_provice_area.setVisibility(View.VISIBLE);
        }
        mEt_mobile.setText(AccountHelper.getUser().getPhone());

        if(null!=userIdentityDetail){
            if(userIdentityDetail.userType==1){
                mTv_userType.setText("公司");//用户类型
                //公司类型
                if("1".equals(userIdentityDetail.enterpriseType)){
                    mTv_company_type.setText("企业招商");
                    enterpriseType =1;
                    mLin_provice_area.setVisibility(View.GONE);
//                    mLin_businesslicense.setVisibility(View.GONE);
                }else if("2".equals(userIdentityDetail.enterpriseType)){
                    mTv_company_type.setText("中介公司");
                    enterpriseType =2;
                    mLin_provice_area.setVisibility(View.VISIBLE);
//                    mLin_businesslicense.setVisibility(View.VISIBLE);
                }
                mEt_company_name.setText(userIdentityDetail.enterpriseName);//公司名称
                mEt_company_address.setText(userIdentityDetail.enterpriseAddress);//公司详细地址
                mEt_name.setText(userIdentityDetail.contactPerson);//对接人
                mEt_mobile.setText(userIdentityDetail.contactTele);//对接人手机号

            }else  if(userIdentityDetail.userType==2){
                mTv_userType.setText("经纪人");
                mEt_broker_name.setText(userIdentityDetail.agentName);//经纪人姓名
                mEt_broker_mobile.setText(userIdentityDetail.agentTele);//经纪人手机
                mTv_provice_city.setText(userIdentityDetail.bussRegion);//省市
                mTv_area.setText(userIdentityDetail.bussRegion);//业务区域
                mLin_businesslicense.setVisibility(View.VISIBLE);
                mLin_provice_area.setVisibility(View.VISIBLE);
            }else  if(userIdentityDetail.userType==3){//不需要认证
                mTv_userType.setText("个人");
            }
            if("1".equals(userIdentityDetail.enterpriseType)){
                mTv_company_type.setText("企业招商");
            }else if("2".equals(userIdentityDetail.enterpriseType)){
                mTv_company_type.setText("中介公司");
            }

        }

        setTopTitle("申请认证");
        type.add("企业招商");
        type.add("中介公司");
        getRegion();
        initCustomOptionPicker();
        certification();
//        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        initAdapter();

    }

    @OnClick({R.id.tv_submit, R.id.lin_company_type, R.id.lin_city, R.id.lin_area})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_company_type:
                pvCustomOptions.show();
                break;
            case R.id.lin_city:
                ShowPickerView();
                break;
            case R.id.lin_area:
                if (mTv_provice_city.getText().length() == 0) {
                    TsUtils.show("请先选择省市再选择区域");
                    return;
                }
                final SelectAreaDialog selectAreaDialog = new SelectAreaDialog(CertificationActivity.this, areas);
                selectAreaDialog.show();
                selectAreaDialog.setYesOnclickListener(new SelectAreaDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick(List<HttpResponseStruct.Area> selectAreas) {
                        StringBuilder sb = new StringBuilder();
                        StringBuilder sbId = new StringBuilder();
                        if (selectAreas.size() != 0) {
//                            TsUtils.show(selectAreas.size() + "");
                            for (int i = 0; i < selectAreas.size(); i++) {
                                if (i == 0) {
                                    sb.append(selectAreas.get(i).name);
                                    sbId.append(selectAreas.get(i).id);
                                } else {
                                    sb.append("、" + selectAreas.get(i).name);
                                    sbId.append("," + selectAreas.get(i).id);
                                }
                            }
                        }

                        mTv_area.setText(sb);
                        bussRegion = sbId.toString();
                        TsUtils.show(bussRegion);
                        selectAreaDialog.dismiss();
                    }
                });
                selectAreaDialog.setNoOnclickListener(new SelectAreaDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        selectAreaDialog.dismiss();
                    }
                });
                break;
            case R.id.tv_submit:
//                httppost("fwef");
                if (files.size() != 0) {
                    Luban.with(CertificationActivity.this)
                            .load(files.get(0))                                   // 传人要压缩的图片列表
                            .ignoreBy(100)                                  // 忽略不压缩图片的大小
//                .setTargetDir(getPath())                        // 设置压缩后文件存储位置
                            .setCompressListener(new OnCompressListener() { //设置回调
                                @Override
                                public void onStart() {
                                    // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                }

                                @Override
                                public void onSuccess(File file) {
                                    // TODO 压缩成功后调用，返回压缩后的图片文件
                                    upImage(file);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // TODO 当压缩过程出现问题时调用
                                }
                            }).launch();    //启动压缩
                } else {
                    httppost("");
                }


                break;
        }
    }

    private void initCustomOptionPicker() {
        //条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = type.get(options1);
                if (tx.equals("企业招商")) {
                    enterpriseType = 1;
//                    mLin_provice_area.setVisibility(View.GONE);
                } else if (tx.equals("中介公司")) {
                    enterpriseType = 2;
                    mLin_provice_area.setVisibility(View.VISIBLE);
                }
                mTv_company_type.setText(tx);

            }
        })
                .setLayoutRes(R.layout.pup_company_type, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_ensure);
                        final TextView tv_cancel = (TextView) v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });
                        tv_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });


                    }
                })
                .isDialog(false)
                .build();

        pvCustomOptions.setPicker(type);//添加数据

    }

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = proviceItems.get(options1).getPickerViewText() +
                        cityItems.get(options1).get(options2);
                areas = proviceItems.get(options1).childRegions.get(options2).childRegions;

//                Toast.makeText(CertificationActivity.this,tx,Toast.LENGTH_SHORT).show();
                mTv_provice_city.setText(tx);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(proviceItems, cityItems);//三级选择器
        pvOptions.show();
    }

    public void certification() {
        final long date = new Date().getTime();
        final String number = HttpUtil.getRandom();
        HttpUtil.getSignature(CertificationActivity.this, date + "", number, new HttpUtil.NetListener() {
            @Override
            public void success(String sign) {

            }
        });
    }

    public void upImage(File file) {
        final Map<String, Object> paramsMap = new HashMap<>();
        //上传空间
        paramsMap.put(Params.BUCKET, SPACE);
        //保存路径，任选其中一个
        paramsMap.put(Params.SAVE_KEY, "/fsp/image/" + ZhuanHuanUtil.getYearDate() + "/" + new Date().getTime() + ".png");


//        Locale locale = Locale.US;
//        Date d = new Date();
//        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", locale);
//        format.setTimeZone(TimeZone.getTimeZone("GMT"));
//        System.out.println(format.format(d));
//        paramsMap.put(Params.DATE, format.format(d));

        paramsMap.put(Params.CONTENT_MD5, UpYunUtils.md5Hex(file));
        UploadEngine.getInstance().formUpload(file, paramsMap, "newfsp", UpYunUtils.md5("newfsp123456"), new UpCompleteListener() {
            @Override
            public void onComplete(boolean isSuccess, String result) {
                Logger.d(result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getString("code").equals("200")) {
                        imgUrl = obj.getString("url");
                        httppost(imgUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new UpProgressListener() {
            @Override
            public void onRequestProgress(long bytesWrite, long contentLength) {

            }
        });
    }


    ArrayList<ImageItem> images = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    imgaeAdapter.setImages(selImageList);
                    files.clear();
                    for (int i = 0; i < selImageList.size(); i++) {
                        files.add(new File(selImageList.get(i).path));
                    }
                }

            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                selImageList = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (selImageList != null) {
                    files.clear();
                    imgaeAdapter.setImages(selImageList);
                    for (int i = 0; i < selImageList.size(); i++) {
                        files.add(new File(selImageList.get(i).path));
                    }
                }
            }
        }

        //新的图片选择器
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
//                        Log.i("图片-----》", media.getPath());
                        Logger.d("图片-----》"+media.getCompressPath());
                        files.add(new File(media.getCompressPath()));
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    public void httppost(final String imgUrl) {
        enterpriseName = mEt_company_name.getText().toString();
        enterpriseAddress = mEt_company_address.getText().toString();
        contactPerson = mEt_name.getText().toString();
        contactTele = mEt_mobile.getText().toString();
        enterpriseLicense = imgUrl;
//        bussRegion =mTv_area.getText().toString();//所属区域
        agentName = mEt_broker_name.getText().toString();
        agentTele = mEt_broker_mobile.getText().toString();
        if (enterpriseType == 0 && bussType != 2) {
            TsUtils.show("请选择公司类型");
            return;
        }
        if ("".equals(enterpriseName) && bussType != 2) {
            TsUtils.show("请填写您的公司名称");
            return;
        }
        if ("".equals(contactTele) && bussType != 2) {
            TsUtils.show("请填写对接人手机号");
            return;
        }

        if (bussType == 2) {
            if ("".equals(agentName)) {
                TsUtils.show("请填写经纪人姓名");
                return;
            }
            if ("".equals(agentTele)) {
                TsUtils.show("请填写经纪人手机号");
                return;
            }
        }
//中介公司和经纪人需选择所属区域
        if (enterpriseType == 2 || bussType == 2) {
            if ("".equals(bussRegion)) {
                TsUtils.show("请填写所属区域");
                return;
            }
        }
        final long date = new Date().getTime();
        final String number = HttpUtil.getRandom();
        HttpUtil.getSignature(CertificationActivity.this, date + "", number, new HttpUtil.NetListener() {
            @Override
            public void success(String sign) {
                HttpRequestStruct.UserIdentity userIdentity = new HttpRequestStruct.UserIdentity();
                HttpRequestStruct.MsgReqWithToken msgReq = new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(), sign, date + "", number);
                userIdentity.msgReq = msgReq;
                userIdentity.enterpriseType = enterpriseType;
                userIdentity.enterpriseName = enterpriseName;
                userIdentity.enterpriseAddress = enterpriseAddress;
                userIdentity.contactPerson = contactPerson;
                userIdentity.contactTele = contactTele;
                userIdentity.enterpriseLicense = imgUrl;
                userIdentity.bussRegion = bussRegion;
                userIdentity.ownerName = ownerName;
                userIdentity.ownerTele = ownerTele;
                userIdentity.agentName = agentName;
                userIdentity.agentTele = agentTele;
                String json = gson.toJson(userIdentity);
                OkGo.<BaseBean<HttpResponseStruct.UserIdentity>>post(InterfaceUrl.USERIDENTITY)
                        .tag(this)
                        .upJson(json)
                        .execute(new JsonCallBack<BaseBean<HttpResponseStruct.UserIdentity>>() {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.UserIdentity>> response) {
                                Logger.d(response.body().getResultCode() + "msg" + response.body().getResultMsg());
                                if ("00000".equals(response.body().getResultCode())) {
                                    TsUtils.show("已提交认证");
                                    MainActivity.actionStart(CertificationActivity.this);
                                } else {
                                    TsUtils.show(response.body().getResultMsg());
                                }
                            }
                        });
            }
        });
    }

    public void getRegion() {
        final long date = new Date().getTime();
        final String number = HttpUtil.getRandom();
        HttpUtil.getSignature(CertificationActivity.this, date + "", number, new HttpUtil.NetListener() {
            @Override
            public void success(String sign) {
                HttpRequestStruct.RegionReq regionReq =new HttpRequestStruct.RegionReq();
                HttpRequestStruct.MsgReqWithToken userIdentity = new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(), sign, date + "", number);
                regionReq.msgReq =userIdentity;
                regionReq.isOpen ="1";
                String json = gson.toJson(regionReq);
                OkGo.<BaseBean<HttpResponseStruct.AreaData>>post(InterfaceUrl.GET_REGION)
                        .tag(this)
                        .upJson(json)
                        .execute(new DialogCallback<BaseBean<HttpResponseStruct.AreaData>>(CertificationActivity.this) {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.AreaData>> response) {
                                Logger.d(date + "number" + number);
                                Logger.d(response.body().getData().regions.get(0).name);
                                List<HttpResponseStruct.Province> provinces = new ArrayList<>();
                                provinces = response.body().getData().regions;
                                proviceItems = (ArrayList<HttpResponseStruct.Province>) response.body().getData().regions;
                                Logger.d(proviceItems.get(0).name);

                                for (int i = 0; i < provinces.size(); i++) {
                                    ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
                                    ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

                                    for (int c = 0; c < provinces.get(i).childRegions.size(); c++) {//遍历该省份的所有城市
                                        String CityName = provinces.get(i).childRegions.get(c).name;
                                        CityList.add(CityName);//添加城市

                                        ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                                        //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                                        if (provinces.get(i).childRegions.get(c).childRegions == null
                                                || provinces.get(i).childRegions.get(c).childRegions.size() == 0) {
                                            City_AreaList.add("");
                                        } else {

                                            for (int d = 0; d < provinces.get(i).childRegions.get(c).childRegions.size(); d++) {//该城市对应地区所有数据
                                                String AreaName = provinces.get(i).childRegions.get(c).childRegions.get(d).name;

                                                City_AreaList.add(AreaName);//添加该城市所有地区数据
                                            }
                                        }
                                        Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                                    }

                                    /**
                                     * 添加城市数据
                                     */
                                    cityItems.add(CityList);

                                    /**
                                     * 添加地区数据
                                     */
                                    areaItems.add(Province_AreaList);
                                }
                            }
                        });

            }
        });
    }

    private void initAdapter() {

        if (selImageList == null) {
            selImageList = new ArrayList<>();
        }
        //图片list
        imgaeAdapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        imgaeAdapter.setOnItemClickListener(this);

        mRecycleview.setLayoutManager(new GridLayoutManager(this, 4));
        mRecycleview.setHasFixedSize(true);
        mRecycleview.setAdapter(imgaeAdapter);

        MyLayoutManager mlm = new MyLayoutManager(this);
        mlm.setScrollEnabled(false);
        mlm.setOrientation(LinearLayoutManager.HORIZONTAL);

        //新的图片选择器
        FullyGridLayoutManager manager = new FullyGridLayoutManager(CertificationActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(CertificationActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
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
                            PictureSelector.create(CertificationActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(CertificationActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(CertificationActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });

    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:

                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");

                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                /**
                                 * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                                 *
                                 * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                                 *
                                 * 如果实在有所需要，请直接下载源码引用。
                                 */
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                ImagePicker.getInstance().setMultiMode(true);
                                ImagePicker.getInstance().setImageLoader(new GlideUploadImageLoader());   //设置图片加载器
                                ImagePicker.getInstance().setShowCamera(true);  //显示拍照按钮
                                ImagePicker.getInstance().setCrop(false);        //允许裁剪（单选才有效）
                                ImagePicker.getInstance().setSaveRectangle(true); //是否按矩形区域保存
                                ImagePicker.getInstance().setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
                                ImagePicker.getInstance().setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
                                ImagePicker.getInstance().setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
                                ImagePicker.getInstance().setOutPutX(1000);//保存文件的宽度。单位像素
                                ImagePicker.getInstance().setOutPutY(1000);//保存文件的高度。单位像素
                                Intent intent = new Intent(CertificationActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                ImagePicker.getInstance().setMultiMode(true);
                                ImagePicker.getInstance().setImageLoader(new GlideUploadImageLoader());   //设置图片加载器
                                ImagePicker.getInstance().setShowCamera(false);  //显示拍照按钮
                                ImagePicker.getInstance().setCrop(false);        //允许裁剪（单选才有效）
                                ImagePicker.getInstance().setSaveRectangle(true); //是否按矩形区域保存
                                ImagePicker.getInstance().setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
                                ImagePicker.getInstance().setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
                                ImagePicker.getInstance().setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
                                ImagePicker.getInstance().setOutPutX(1000);//保存文件的宽度。单位像素
                                ImagePicker.getInstance().setOutPutY(1000);//保存文件的高度。单位像素
                                Intent intent1 = new Intent(CertificationActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
                                //                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }
                    }
                }, names);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) imgaeAdapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            boolean mode = true;
            if (mode) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(CertificationActivity.this)
                        .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .previewVideo(false)// 是否可预览视频
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
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled() // 裁剪是否可旋转图片
                        //.scaleEnabled()// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()//显示多少秒以内的视频or音频也可适用
                        //.recordVideoSecond()//录制视频秒数 默认60s
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
}
