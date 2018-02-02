package com.fangshang.fspbiz.weight;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.weight.adapter.OneSortAdapter;
import com.fangshang.fspbiz.weight.adapter.ThreeSortAdapter;
import com.fangshang.fspbiz.weight.adapter.TwoSortAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangmingxia on 2017/10/20.
 */

public class ThreeSortHouseLinelayout extends LinearLayout {
    private static final String TAG = "ThreeSortHouseLinelayou";
    private String onedata = null;
    private String twodata =null;
    private Context context;
    private RecyclerView rv_one, rv_two,rv_three;
    HttpResponseStruct.HouseSourceData houseSourceData;
    private List<HttpResponseStruct.Housebean> datas = new ArrayList<>();
    private List<HttpResponseStruct.Housebean> datas2 = new ArrayList<>();
    private List<HttpResponseStruct.Housebean> datas3 =new ArrayList<>();

    public String buildingId;
    public String floorId;
    public String houseId;

    int oneposition;
    private String onetitle,twotitle,threetitel;
    private HttpResponseStruct.HouseIdTitle houseIdTitle;
    public ThreeSortHouseLinelayout(Context context) {
        super(context);
        this.context = context;
        houseIdTitle =new HttpResponseStruct.HouseIdTitle();
        houseIdTitle.buildingId="";
        houseIdTitle.floorId="";
        houseIdTitle.houseId="";
        getData();
        LayoutInflater inflaterone = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View header =inflaterone.inflate(R.layout.header_text,null);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.threesortlinelayout, this);
        rv_one = (RecyclerView) findViewById(R.id.rv_one);
        rv_two = (RecyclerView) findViewById(R.id.rv_two);
        rv_three =(RecyclerView) findViewById(R.id.rv_three);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        final LinearLayoutManager gm = new LinearLayoutManager(context);
        rv_two.setLayoutManager(gm);
        final LinearLayoutManager threelm = new LinearLayoutManager(context);
        rv_three.setLayoutManager(threelm);

        final OneSortAdapter adapter =new OneSortAdapter(datas);
        Log.i(TAG, "NewThreeSortHouseLinelayout: "+datas.size()+"datas1"+datas2.size()+"datas3"+datas3.size());
        adapter.setOnItemClickListener(new OneSortAdapter.OnItemClickListener() {
            @Override
            public void OnClick(int position, HttpResponseStruct.Housebean housebean) {
                datas2.clear();
                datas3.clear();
                if(position==0){
                    houseIdTitle.buildingId="";
                    houseIdTitle.floorId="";
                    houseIdTitle.houseId="";
                    clickListener.OnClick("全部",houseIdTitle);
                }else{
                    houseIdTitle.buildingId =housebean.id;//buildid

                    onetitle=housebean.title;
                    oneposition =position-1;

                    HttpResponseStruct.Housebean housebean1 =new HttpResponseStruct.Housebean();
                    housebean1.title ="全部";
                    housebean1.id ="";
                    datas2.add(housebean1);
                    try {
                        for (int i = 0; i <houseSourceData.list.get(position-1).floorlist.size() ; i++) {
                            HttpResponseStruct.Housebean housebean2 =new HttpResponseStruct.Housebean();
                            housebean2.title =houseSourceData.list.get(position-1).floorlist.get(i).name;
                            housebean2.id =houseSourceData.list.get(position-1).floorlist.get(i).id+"";
                            datas2.add(housebean2);
                        }
                    }catch (Exception e){

                    }
                    final TwoSortAdapter adapter1 =new TwoSortAdapter(datas2);
                    adapter1.setOnItemClickListener(new TwoSortAdapter.OnItemClickListener() {
                        @Override
                        public void OnClick(int position, HttpResponseStruct.Housebean housebean) {
                            if(position==0){
                                datas3.clear();
                                houseIdTitle.floorId="";
                                houseIdTitle.houseId="";
                                clickListener.OnClick(onetitle,houseIdTitle);
                            }else{
                                houseIdTitle.floorId =housebean.id;//floorId
                                twotitle =housebean.title;
                                datas3.clear();
                                HttpResponseStruct.Housebean housebean1 =new HttpResponseStruct.Housebean();
                                housebean1.title ="全部";
                                housebean1.id ="";
                                datas3.add(housebean1);
                                try {
                                    for (int i = 0; i <houseSourceData.list.get(oneposition).floorlist.get(position-1).houselist.size() ; i++) {
                                        HttpResponseStruct.Housebean housebean2 =new HttpResponseStruct.Housebean();
                                        housebean2.title =houseSourceData.list.get(oneposition).floorlist.get(position-1).houselist.get(i).houseName;
                                        housebean2.id =houseSourceData.list.get(oneposition).floorlist.get(position-1).houselist.get(i).id+"";
                                        datas3.add(housebean2);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                ThreeSortAdapter adapter2 =new ThreeSortAdapter(datas3);
                                adapter2.setOnItemClickListener(new ThreeSortAdapter.OnItemClickListener() {
                                    @Override
                                    public void OnClick(int position, HttpResponseStruct.Housebean title) {
                                        if(position==0){
                                            houseIdTitle.houseId="";
                                            clickListener.OnClick(onetitle+"/"+twotitle,houseIdTitle);
                                        }else {
                                            houseIdTitle.houseId =title.id;//houseId
                                            clickListener.OnClick(onetitle+"/"+twotitle+"/"+title.title,houseIdTitle);
                                        }

                                    }
                                });
                                rv_three.setAdapter(adapter2);
                            }
                        }
                    });
                    rv_two.setAdapter(adapter1);
                }

            }
        });

        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_one.setLayoutManager(lm);
        rv_one.setAdapter(adapter);
    }
    public void getData(){
        datas.clear();
        HttpResponseStruct.Housebean housebean1 =new HttpResponseStruct.Housebean();
        housebean1.title ="全部";
        housebean1.id ="";
        datas.add(housebean1);
        Map<String,String> parmars =new HashMap<>();
//        HttpNetwork.getInstance().request(parmars, "/fspsaas/api/building/getAllBuildingDate", new HttpNetwork.NetResponseCallback() {
//            @Override
//            public void onResponse(HttpResponsePacket response) throws Exception {
//                Log.i(TAG, "onResponse: "+response.getData().toString());
//                if("00000".equals(response.resultCode)){
//                    houseSourceData =new Gson().fromJson(response.getData(), HttpResponseStruct.HouseSourceData.class);
//                    for (int i = 0; i <houseSourceData.list.size() ; i++) {
//                        HttpResponseStruct.Housebean housebean2 =new HttpResponseStruct.Housebean();
//                        housebean2.title =houseSourceData.list.get(i).name;
//                        housebean2.id =houseSourceData.list.get(i).id+"";
//                        datas.add(housebean2);
//                    }
//                }
//            }
//        });
    }
    private ThreeSortHouseLinelayout.OnItemClickListener clickListener;

    public void setItemClickListener(ThreeSortHouseLinelayout.OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static interface OnItemClickListener {
        void OnClick(String title, HttpResponseStruct.HouseIdTitle houseIdTitle);
    }

    ;

    public ThreeSortHouseLinelayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }
}
