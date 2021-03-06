package tourism.fmt.com.tourism.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Observable;

import tourism.fmt.com.tourism.ApplicationConstant;
import tourism.fmt.com.tourism.R;
import tourism.fmt.com.tourism.adapters.WeatherAdapter;
import tourism.fmt.com.tourism.eneity.Future;
import tourism.fmt.com.tourism.eneity.ToDay;
import tourism.fmt.com.tourism.net.HttpNet;
import tourism.fmt.com.tourism.net.HttpUrlConstant;
import tourism.fmt.com.tourism.utils.TourismLog;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {


    private View inflate;

    public WeatherFragment() {
        // Required empty public constructor
    }

    private ListView mList;

    private WeatherAdapter mAdpter;
    private EditText ed;
    private Button btn;
    private ToDay mToDay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflate = inflater.inflate(R.layout.fragment_weather, container, false);
        mList= (ListView) inflate.findViewById(R.id.weather);
        ed= (EditText) inflate.findViewById(R.id.edit_query);
        btn= (Button) inflate.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationConstant.mCity=ed.getText().toString();
                ed.getText().clear();
                getDatas();
            }
        });
        getDatas();
        return inflate;
    }
    Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mList.setAdapter(mAdpter);
        }
    };

    private void  getDatas(){
        HttpNet.HttpGet("http://apis.haoservice.com/weather?cityname="+ApplicationConstant.mCity+"&key="+ApplicationConstant.WEATHER_KEY, new HttpNet.HttpNetInterface() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONObject result = object.getJSONObject("result");
                    JSONArray future = result.getJSONArray("future");
                    mToDay= com.alibaba.fastjson.JSONObject.parseObject(result.getString("today").toString(),ToDay.class);
                    List<Future> futures = com.alibaba.fastjson.JSONArray.parseArray(future.toString(), Future.class);
                    if (future!=null){
                        mAdpter=new WeatherAdapter(getActivity(),futures);
                        mhandler.sendEmptyMessage(200);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



}
