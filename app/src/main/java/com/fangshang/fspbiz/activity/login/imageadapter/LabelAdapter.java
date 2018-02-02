package com.fangshang.fspbiz.activity.login.imageadapter;//package com.yiketang.Fragment.comment.imageadapter;
//
//import android.view.View;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.yiketang.R;
//import com.yiketang.bean.Label;
//import com.yiketang.widget.SuperTextView;
//
//import java.util.List;
//
///**
// * Created by lenovo on 2017/6/2.
// */
//
//public class LabelAdapter extends BaseQuickAdapter<Label,BaseViewHolder> {
//    public LabelAdapter(List<Label> data) {
//        super(R.layout.item_label_delete, data);
//    }
//
//    @Override
//    protected void convert(final BaseViewHolder helper, Label item) {
//        SuperTextView stv_label=helper.getView(R.id.stv_label);
//        stv_label.setText(item.getLabelName());
//        helper.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mData.remove(helper.getLayoutPosition());
//                notifyDataSetChanged();
////                notifyItemRemoved(helper.getLayoutPosition()+getHeaderLayoutCount());
////                notifyItemRangeChanged(helper.getLayoutPosition(), getItemCount());
//            }
//        });
//    }
//}
