package com.fangshang.fspbiz.fragment.housing.build;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseFragment;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.media.SampleListener;
import com.fangshang.fspbiz.media.SampleVideo;
import com.fangshang.fspbiz.media.SwitchVideoModel;
import com.fangshang.fspbiz.util.GlideImageLoader;
import com.fangshang.fspbiz.util.GlideLoaderUtil;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xiong on 2018/1/23/023 10:42
 */

public class VideoFragment extends BaseFragment {
    @BindView(R.id.detail_player)
    SampleVideo mDetail_player;
    private List<Integer> images =new ArrayList<>();
    private OrientationUtils orientationUtils;//屏幕旋转工具类
    ImageView imageView;//封面图片
    private boolean isPlay;
    private boolean isPause;
    private HttpResponseStruct.Attachment video;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if(null!=bundle){
            video = (HttpResponseStruct.Attachment) bundle.getSerializable("video");
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {



        //增加封面
        imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if(null!=video){
            GlideLoaderUtil.display(mContext,imageView,video.coverPic);
            //外部辅助的旋转，帮助全屏
            orientationUtils = new OrientationUtils((Activity) mContext, mDetail_player);
            //初始化不打开外部的旋转
            orientationUtils.setEnable(false);


            mDetail_player.setIsTouchWiget(true);

            mDetail_player.setRotateViewAuto(false);
            mDetail_player.setLockLand(false);
            mDetail_player.setShowFullAnimation(false);
            mDetail_player.setNeedLockFull(true);
            mDetail_player.setSeekRatio(1);

            mDetail_player.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //直接横屏
                    orientationUtils.resolveByClick();

                    //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                    mDetail_player.startWindowFullscreen(mContext, true, true);
                }
            });

            mDetail_player.setStandardVideoAllCallBack(new SampleListener() {
                @Override
                public void onPrepared(String url, Object... objects) {
                    super.onPrepared(url, objects);
                    //开始播放了才能旋转和全屏
                    orientationUtils.setEnable(true);
                    isPlay = true;
//                addViewNum(mLessonid);
                }

                @Override
                public void onAutoComplete(String url, Object... objects) {
                    super.onAutoComplete(url, objects);
                }

                @Override
                public void onClickStartError(String url, Object... objects) {
                    super.onClickStartError(url, objects);
                }

                @Override
                public void onQuitFullscreen(String url, Object... objects) {
                    super.onQuitFullscreen(url, objects);
                    if (orientationUtils != null) {
                        orientationUtils.backToProtVideo();
                    }
                }
            });
            mDetail_player.setLockClickListener(new LockClickListener() {
                @Override
                public void onClick(View view, boolean lock) {
                    if (orientationUtils != null) {
                        //配合下方的onConfigurationChanged
                        orientationUtils.setEnable(!lock);
                    }
                }
            });
            SwitchVideoModel switchVideoModel = new SwitchVideoModel("标清", InterfaceUrl.IMG_HOST+video.filePath);
            List<SwitchVideoModel> list = new ArrayList<>();
            list.add(switchVideoModel);
            mDetail_player.setUp(list, true,"测试视频");
            mDetail_player.setThumbImageView(imageView);//设置封面图
        }

    }
    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播

    }
    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
        mDetail_player.onVideoPause();//暂停
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        isPause = false;
        mDetail_player.onVideoResume();//回复暂停状态
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                if (!mDetail_player.isIfCurrentIsFullscreen()) {
                    mDetail_player.startWindowFullscreen(mContext, true, true);
//                    mDetail_player.getSwitchView().setVisibility(View.VISIBLE);

//                    mDetail_player.switchArticulation();
                }
            } else {
                //新版本isIfCurrentIsFullscreen的标志位内部提前设置了，所以不会和手动点击冲突
                if (mDetail_player.isIfCurrentIsFullscreen()) {
                    StandardGSYVideoPlayer.backFromWindowFull(mContext);
//                    mDetail_player.getSwitchView().setVisibility(View.GONE);
                }
                if (orientationUtils != null) {
                    orientationUtils.setEnable(true);
                }
            }
        }
        //监控横竖屏
        if(newConfig.orientation == 1){
            //TODO 某些操作
//            mDetail_player.getSwitchView().setVisibility(View.GONE);
//            mDetail_player.showShareView();
        }else{
//            mDetail_player.getSwitchView().setVisibility(View.VISIBLE);
//            mDetail_player.switchArticulation();
//            mDetail_player.hideShareView();
        }
    }
}
