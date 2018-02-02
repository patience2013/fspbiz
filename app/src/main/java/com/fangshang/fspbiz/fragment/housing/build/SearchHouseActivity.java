package com.fangshang.fspbiz.fragment.housing.build;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseActivity;
import com.fangshang.fspbiz.fragment.housing.sqldb.MySearchHistory;
import com.fangshang.fspbiz.fragment.housing.sqldb.SearchHistory;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/31/031 10:46
 */

public class SearchHouseActivity extends BaseActivity {
    @BindView(R.id.et_search)
    EditText mEt_search;
    @BindView(R.id.flowlayout)
    TagFlowLayout mFlowlayout;


    private List<SearchHistory> list =new ArrayList<>();

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchHouseActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_searchhouse;
    }

    @Override
    protected void initView() {
        super.initView();
        queryHistory();

        initAdapter();
    }

    public void initAdapter(){
        mFlowlayout.setAdapter(new TagAdapter<SearchHistory>(list) {
            @Override
            public View getView(FlowLayout parent, int position, SearchHistory searchHistory) {
                TextView tv = (TextView) LayoutInflater.from(SearchHouseActivity.this).inflate(R.layout.item_search_history,
                        parent, false);
                tv.setText(searchHistory.getName());
                return tv;
            }
        });
        mFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String text =list.get(position).getName();
                mEt_search.setText(text);
                if(!"".equals(text)){
                    SearchHouseListActivity.actionStart(SearchHouseActivity.this,text);
                }
                return true;
            }
        });
    }
    @OnClick({R.id.lin_back,R.id.tv_search,R.id.img_del})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.tv_search:
                String text =mEt_search.getText().toString().trim();
                if(!"".equals(text)){
                    SearchHistory searchHistory =new SearchHistory();
                    searchHistory.setName(text);
                    MySearchHistory.insertHistory(searchHistory);
                    SearchHouseListActivity.actionStart(this,text);
                }
                queryHistory();
                break;
            case R.id.img_del:
                clearHistory();
                break;
        }
    }
    private void queryHistory() {
        list.clear();
        list.addAll(MySearchHistory.queryAll());
        Collections.reverse(list);
        initAdapter();
//        adapter.notifyDataSetChanged();
    }
    private void clearHistory() {
        list.clear();
        MySearchHistory.clearAll();
        initAdapter();
//        adapter.notifyDataSetChanged();
    }
}
