package com.fangshang.fspbiz.fragment.housing.build;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseFragment;
import com.fangshang.fspbiz.bean.DetailHead;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.media.SampleListener;
import com.fangshang.fspbiz.media.SampleVideo;
import com.fangshang.fspbiz.media.SwitchVideoModel;
import com.fangshang.fspbiz.util.GlideImageLoader;
import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xiong on 2018/1/23/023 10:42
 */

public class VideolistFragment extends BaseFragment {
    @BindView(R.id.vp_video)
    ViewPager mVp_video;
    @BindView(R.id.tv_position)
    TextView mTv_position;
    private List<Fragment> fragvideos = new ArrayList<>();
    private MyPagerAdapter adapter;
    DetailHead videohead =new DetailHead();
    private List<HttpResponseStruct.Attachment> videos =new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_videolist;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if(null!=bundle){
            videohead = (DetailHead) bundle.getSerializable("videohead");
            videos = videohead.attachments;
        }
        Logger.d("videos"+videos.size());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if(null!=videos &&videos.size()!=0){
            for (int i = 0; i <videos.size() ; i++) {
                VideoFragment videoFragment =new VideoFragment();
                Bundle bundle =new Bundle();
                bundle.putSerializable("video",videos.get(i));
                videoFragment.setArguments(bundle);
                fragvideos.add(videoFragment);
            }
            adapter =new MyPagerAdapter(getChildFragmentManager());
            mVp_video.setAdapter(adapter);
            mVp_video.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mTv_position.setText(position+1+"/"+fragvideos.size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }
    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragvideos.size();
        }


        @Override
        public Fragment getItem(int position) {
            return fragvideos.get(position);
        }
    }
}
