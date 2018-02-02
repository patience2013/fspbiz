package com.fangshang.fspbiz.weight.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.bean.HttpResponseStruct;

import java.util.List;

/**
 * Created by jiangmingxia on 2017/10/20.
 */

public class TwoSortAdapter extends BaseQuickAdapter<HttpResponseStruct.Housebean,BaseViewHolder> {
    private OnItemClickListener onItemClickListener;
    public  interface OnItemClickListener {
        void OnClick(int position, HttpResponseStruct.Housebean title);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener =onItemClickListener;
    }
    private int selectedPosition = -2;
    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }
    public TwoSortAdapter(List<HttpResponseStruct.Housebean> data) {
        super(R.layout.item_text, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HttpResponseStruct.Housebean item) {
        TextView tv_text =helper.getView(R.id.tv_text);
        helper.setText(R.id.tv_text,item.title);
        if (selectedPosition ==helper.getAdapterPosition()) {
//            helper.getView(R.id.sort).setBackgroundColor(Color.parseColor("#0BBFDE"));
            tv_text.setTextColor(Color.parseColor("#0BBFDE"));
        } else {
//            helper.getView(R.id.sort).setBackgroundColor(Color.parseColor("#FFFFFF"));
            tv_text.setTextColor(Color.parseColor("#626262"));
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition =helper.getAdapterPosition();
                onItemClickListener.OnClick(helper.getAdapterPosition(),item);
                notifyDataSetChanged();
            }
        });
    }
}
