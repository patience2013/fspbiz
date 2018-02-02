package com.fangshang.fspbiz.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangshang.fspbiz.R;

import java.util.List;

/**
 * Created by lenovo on 2017/3/9.
 */

public class ScreeningMenu extends LinearLayout {
    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;
    //下划线高度
    private int underLineHeight = 0;
    //文字距离图标的距离
    private int textviewDrawablePadding = 0;
    //分割线颜色
    private int dividerColor = 0xfff0f0f0;
    //tab选中颜色
    private int textSelectedColor = 0xffFFFFFF;
    //tab未选中颜色
    private int textUnselectedColor = 0xff323232;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //tab字体大小
    private int menuTextSize = 18;

    //tab选中图标
    private int menuSelectedIcon;
    //tab未选中图标
    private int menuUnselectedIcon;


    public ScreeningMenu(Context context) {
        super(context, null);
    }

    public ScreeningMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScreeningMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        //为DropDownMenu添加自定义属性
        int menuBackgroundColor = 0xffffffff;
        int underlineColor = 0xffcccccc;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScreeningMenu);
        textviewDrawablePadding = a.getInteger(R.styleable.ScreeningMenu_ddtextviewDrawablePadding, textviewDrawablePadding);
        underLineHeight = a.getInteger(R.styleable.ScreeningMenu_ddunderlineHeight, underLineHeight);
        underlineColor = a.getColor(R.styleable.ScreeningMenu_ddunderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.ScreeningMenu_dddividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.ScreeningMenu_ddtextSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.ScreeningMenu_ddtextUnselectedColor, textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.ScreeningMenu_ddmenuBackgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.ScreeningMenu_ddmaskColor, maskColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.ScreeningMenu_ddmenuTextSize, menuTextSize);
        menuSelectedIcon = a.getResourceId(R.styleable.ScreeningMenu_ddmenuSelectedIcon, menuSelectedIcon);
        menuUnselectedIcon = a.getResourceId(R.styleable.ScreeningMenu_ddmenuUnselectedIcon, menuUnselectedIcon);
        a.recycle();

        //初始化tabMenuView并添加到tabMenuView
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //为tabMenuView添加下划线
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(underLineHeight)));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);

    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews, @NonNull View contentView) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }
        containerView.addView(contentView, 0);
        //设置半透明的遮罩
        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView, 1);
        maskView.setVisibility(GONE);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews, 2);

        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i), i);
        }

    }

    private void addTab(@NonNull List<String> tabTexts, int i) {
        //自定义tab布局
        final View tab = inflate(getContext(), R.layout.sorttab_layout, null);
        tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        final TextView texttab = getTabTextView(tab);

//        final TextView tab = new TextView(getContext());
        texttab.setSingleLine();
        texttab.setEllipsize(TextUtils.TruncateAt.END);
        texttab.setGravity(Gravity.CENTER);
        texttab.setCompoundDrawablePadding(dpTpPx(textviewDrawablePadding));
        texttab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        texttab.setTextColor(textUnselectedColor);
//        texttab.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
        texttab.setText(tabTexts.get(i));
        texttab.setPadding(dpTpPx(10), dpTpPx(12), dpTpPx(10), dpTpPx(12));
        //添加点击事件
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(texttab);
            }
        });
        tabMenuView.addView(tab);
        //添加分割线
        if (i < tabTexts.size() - 1) {
            View view = new View(getContext());
            view.setLayoutParams(new LayoutParams(dpTpPx(0), ViewGroup.LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(dividerColor);
            tabMenuView.addView(view);
        }
    }

    /**
     * 获取tabView中id为tv_tab的textView
     *
     * @param tabView
     * @return
     */
    private TextView getTabTextView(View tabView) {
        TextView tabtext = (TextView) tabView.findViewById(R.id.tv_tab);
        return tabtext;
    }
    /**
     * 获取tabView中id为tv_tab的textView
     *
     * @param tabView
     * @return
     */
    private LinearLayout getTabLinelayout(View tabView) {
        LinearLayout tab = (LinearLayout) tabView.findViewById(R.id.lin_tab);
        return tab;
    }
    /**
     * 获取tabView中id为img_tab的imageView
     *
     * @param tabView
     * @return
     */
    private ImageView getTabImageView(View tabView) {
        ImageView tabimg = (ImageView) tabView.findViewById(R.id.img_tab);
        return tabimg;
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (current_tab_position != -1) {
            ((TextView) getTabTextView(tabMenuView.getChildAt(current_tab_position))).setText(text);
        }
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != -1) {
            getTabTextView(tabMenuView.getChildAt(current_tab_position)).setTextColor(textUnselectedColor);
            getTabImageView(tabMenuView.getChildAt(current_tab_position)).setImageResource(menuUnselectedIcon);
//            getTabTextView(tabMenuView.getChildAt(current_tab_position)).setCompoundDrawablesWithIntrinsicBounds(null, null,
//                    getResources().getDrawable(menuUnselectedIcon), null);
            getTabLinelayout(tabMenuView.getChildAt(current_tab_position)).setBackgroundResource(R.color.white);
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            current_tab_position = -1;
        }

    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {
        System.out.println(current_tab_position);
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (target == getTabTextView(tabMenuView.getChildAt(i))) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    getTabTextView(tabMenuView.getChildAt(i)).setTextColor(textSelectedColor);
                    getTabImageView(tabMenuView.getChildAt(i)).setImageResource(menuSelectedIcon);
//                    getTabTextView(tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
//                            getResources().getDrawable(menuSelectedIcon), null);
                    getTabLinelayout(tabMenuView.getChildAt(i)).setBackgroundResource(R.color.main_yellow);
                }
            } else {
                getTabTextView(tabMenuView.getChildAt(i)).setTextColor(textUnselectedColor);
                getTabImageView(tabMenuView.getChildAt(i)).setImageResource(menuUnselectedIcon);
//                getTabTextView(tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null,
//                        getResources().getDrawable(menuUnselectedIcon), null);
                getTabLinelayout(tabMenuView.getChildAt(i)).setBackgroundResource(R.color.white);
                popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }
}
