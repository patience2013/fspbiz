package com.fangshang.fspbiz.fragment.housing.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fangshang.fspbiz.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
  //这个是checkbox的Hashmap集合
  private final HashMap<Integer, Boolean> map;
  //这个是数据集合
  private final ArrayList<String> list;

  public MyAdapter() {
    map = new HashMap<>();
    list = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      //添加30条数据
      list.add("这是条目" + i);
      map.put(i, false);
    }

  }

  /**
   * 全选
   */
  public void All() {
    Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
    boolean shouldall = false;
    for (Map.Entry<Integer, Boolean> entry : entries) {
      Boolean value = entry.getValue();
      if (!value) {
        shouldall = true;
        break;
      }
    }
    for (Map.Entry<Integer, Boolean> entry : entries) {
      entry.setValue(shouldall);
    }
    notifyDataSetChanged();
  }

  /**
   * 反选
   */
  public void neverall() {
    Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
    for (Map.Entry<Integer, Boolean> entry : entries) {
      entry.setValue(!entry.getValue());
    }
    notifyDataSetChanged();
  }

  /**
   * 单选
   *
   * @param postion
   */
  public void singlesel(int postion) {
    Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
    for (Map.Entry<Integer, Boolean> entry : entries) {
      entry.setValue(false);
    }
    map.put(postion, true);
    notifyDataSetChanged();
  }

  //这里主要初始化布局控件
  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.LayoutManager layoutManager = ((RecyclerView) parent).getLayoutManager();
    //初始化布局文件
    View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_houselist, parent, false);
    return new MyViewHolder(inflate);
  }

  @Override
  public void onBindViewHolder(final MyViewHolder holder, final int position) {
    //放入集合中的值
//    holder.txt.setText(list.get(position));
    holder.checkBox.setChecked(map.get(position));
    holder.checkBox.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        map.put(position, !map.get(position));
        //刷新适配器
        notifyDataSetChanged();
        //单选
//        singlesel(position);

      }
    });

  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    View itemView;
//    private TextView txt;
    private CheckBox checkBox;

    //初始化控件
    public MyViewHolder(View itemView) {
      super(itemView);
      this.itemView = itemView;
//      txt = (TextView) itemView.findViewById(R.id.txt);
      checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
    }
  }
}