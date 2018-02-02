package com.fangshang.fspbiz.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.activity.login.imageadapter.MyLayoutManager;
import com.fangshang.fspbiz.bean.HttpResponseStruct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiong on 2018/1/5/005 15:56
 */

public class SelectAreaDialog extends Dialog {
    TextView mTv_cancel,mTv_ensure;
    RecyclerView mRv_list;
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器
    private List<HttpResponseStruct.Area> areas;
    private List<HttpResponseStruct.Area> selectAreas =new ArrayList<>();
    private AreaAdapter adapter;
    private Activity mContenx;
    public SelectAreaDialog(@NonNull Activity context, List<HttpResponseStruct.Area> areas) {
        super(context);
        this.areas =areas;
        this.mContenx =context;
        clearData();
    }
    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(onNoOnclickListener onNoOnclickListener) {
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(onYesOnclickListener onYesOnclickListener) {
        this.yesOnclickListener = onYesOnclickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.dialog_selectarea,
                null);
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                600));
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);//设置占满全屏
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
//        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = mContenx.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        onWindowAttributesChanged(wl);

//        setContentView(R.layout.dialog_selectarea);
        setCanceledOnTouchOutside(true);
//        this.getWindow().setGravity(Gravity.BOTTOM);
        mTv_cancel =findViewById(R.id.tv_cancel);
        mTv_ensure =findViewById(R.id.tv_ensure);
        mRv_list =findViewById(R.id.rv_list);
        initEvent();
        initAdapter();
//        if(this.isShowing()){
//            changeButtonStatus();
//        }
    }


    public void initAdapter(){
        adapter =new AreaAdapter(areas);
        MyLayoutManager mlm =new MyLayoutManager(mContenx);
        mRv_list.setLayoutManager(mlm);
        mRv_list.setAdapter(adapter);
    }
    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        mTv_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick(selectAreas);
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        mTv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }
    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick(List<HttpResponseStruct.Area> areas);
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }
    class AreaAdapter extends BaseQuickAdapter<HttpResponseStruct.Area,BaseViewHolder>{

        public AreaAdapter(@Nullable List<HttpResponseStruct.Area> data) {
            super(R.layout.item_selectarea, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final HttpResponseStruct.Area item) {
            final TextView tv_text =helper.getView(R.id.tv_area);
            helper.setText(R.id.tv_area,item.name);
            if(item.isCheck){
                tv_text.setTextColor(mContenx.getResources().getColor(R.color.main_yellow));
            }else{
                tv_text.setTextColor(mContenx.getResources().getColor(R.color.main_gray));
            }
            helper.getView(R.id.tv_area).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TsUtils.show(item.isCheck+"");
                    if(item.isCheck){
                        item.isCheck =false;
                        selectAreas.remove(item);
                        tv_text.setTextColor(mContenx.getResources().getColor(R.color.main_gray));
                    }else{
                        item.isCheck =true;
                        selectAreas.add(item);
                        tv_text.setTextColor(mContenx.getResources().getColor(R.color.main_yellow));
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
    public void clearData(){
        for(HttpResponseStruct.Area area :areas){
            area.isCheck =false;
        }
    }
}
