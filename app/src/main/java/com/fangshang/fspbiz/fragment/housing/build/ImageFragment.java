package com.fangshang.fspbiz.fragment.housing.build;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.fangshang.fspbiz.App;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseFragment;
import com.fangshang.fspbiz.bean.DetailHead;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.bean.MyHouseListEvent;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.util.GlideImageLoader;
import com.orhanobut.logger.Logger;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xiong on 2018/1/23/023 10:42
 */

@SuppressLint("ValidFragment")
public class ImageFragment extends BaseFragment {
    @BindView(R.id.banner_video)
    Banner mBanner_video;
    private List<String> imagesUrl =new ArrayList<>();
    DetailHead imagehead =new DetailHead();
    private List<HttpResponseStruct.Attachment> images =new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if(null!=bundle){
            imagehead = (DetailHead) bundle.getSerializable("imagehead");
            images =imagehead.attachments;
        }
        Logger.d("images"+images.size());

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if(null!=images){
            for (int i = 0; i <images.size() ; i++) {
                imagesUrl.add(InterfaceUrl.IMG_HOST+images.get(i).filePath);
            }
        }
        mBanner_video.setBannerStyle(BannerConfig.NUM_INDICATOR);
        mBanner_video.setIndicatorGravity(BannerConfig.RIGHT);
        //设置图片加载器
        mBanner_video.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner_video.setImages(imagesUrl);
        //banner设置方法全部调用完毕时最后调用
        mBanner_video.start();
    }
    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        mBanner_video.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        mBanner_video.stopAutoPlay();
    }
}
