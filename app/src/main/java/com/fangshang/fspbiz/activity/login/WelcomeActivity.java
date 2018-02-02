package com.fangshang.fspbiz.activity.login;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.activity.LoginActivity;
import com.fangshang.fspbiz.base.BaseActivity;
import com.fangshang.fspbiz.util.Config;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//第一次运行的引导页代码
public class WelcomeActivity extends BaseActivity implements
        OnPageChangeListener {
	@BindView(R.id.vp_welcome)
    ViewPager mVp_welcome;
	@BindView(R.id.btn_enter)
	Button mBtn_enter;

	private Context context;
	private PagerAdapter pagerAdapter;
	private ArrayList<View> views;
	private int[] images;

	public static void actionStart(Context context) {
		Intent intent = new Intent(context, WelcomeActivity.class);
		context.startActivity(intent);
	}
	@Override
	protected int getContentView() {
		return R.layout.activity_welcome;
	}

	@Override
	protected void initView() {
		context = this;
		// 创建桌面快捷方式
//		new CreateShut(this);
		// 设置引导图片
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 仅需在这设置图片 指示器和page自动添加
		images = new int[] { R.mipmap.guide_pages_one, R.mipmap.guide_pages_one,
				R.mipmap.guide_pages_one, R.mipmap.guide_pages_one,R.mipmap.guide_pages_one };
		views = new ArrayList<View>();
		for (int i = 0; i < images.length; i++) {
			// 循环加入图片
			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(new LinearLayout.LayoutParams( ConvertUtils.dp2px(20),ConvertUtils.dp2px(20)));
			imageView.setBackgroundResource(images[i]);
			views.add(imageView);
		}
		pagerAdapter = new BasePagerAdapter(views);
		mVp_welcome.setAdapter(pagerAdapter); // 设置适配器
		mVp_welcome.setOnPageChangeListener(this);  
		mBtn_enter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Config.getInstance().setBoolean(Config.FIRST_LANUCHER, false);
				startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
				finish();
			}
		});
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	// 监听viewpage
	@Override
	public void onPageSelected(int arg0) {
		// 显示最后一个图片时显示按钮
		if (arg0 == images.length - 1) {
			mBtn_enter.setVisibility(View.VISIBLE);
		} else {
			mBtn_enter.setVisibility(View.INVISIBLE);
		}
	}

	public class BasePagerAdapter extends PagerAdapter {
		private List<View> views=new ArrayList<View>();

		public BasePagerAdapter(List<View> views){
			this.views=views;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(views.get(position));
			return views.get(position);
		}}

}
