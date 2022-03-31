package com.example.diaryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WeatherFragment extends Fragment {
    MainActivity mainActivity;
    DiaryFragment diaryFragment;
    DrawupFragment drawupFragment;

    static RequestQueue requestQueue;
    public ImageButton refresh;
    public TextView dateView, cityView, weatherView, tempView;
    public EditText contextEditText;
    public String city;
    CharSequence[] date = {null};

    public WeatherFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mainActivity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        diaryFragment = new DiaryFragment();
        drawupFragment = new DrawupFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_weather, container, false);

        dateView = rootView.findViewById(R.id.dateView);
        cityView = rootView.findViewById(R.id.cityView);
        weatherView = rootView.findViewById(R.id.weatherView);
        tempView = rootView.findViewById(R.id.tempView);
        contextEditText = rootView.findViewById(R.id.contextEditText);

        //새로고침 버튼(refresh)의 선언 및 클릭이벤트 정의
        refresh = rootView.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System의 현재 시간을 가져오는 메서드 currentTimeMills();
                //위의 메서드의 리턴값을 long타입의 now 변수에 저장
                //날짜를 객체화할 수 있는 Date를 now로 받아 date로 선언
                city = contextEditText.getText().toString();
                CurrentCall();

                contextEditText.setText("");
            }

        });

        //volley를 쓸 때 Queue가 비어있으면 새로운 큐 생성
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mainActivity.getApplicationContext());
        }

        return rootView;
    }

    //API KEY를 사용하여 날씨 데이터 가져오기기
    private void CurrentCall() {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&lang=kr&appid=cd3ef2bfa1fed88800228b8ab610338a";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    //날짜 설정
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);

                    //SimpleDateFormat을 통해 다양한 형식으로 현재 시간 받아올 수 있음
                    //년,월,일을 String 형식의 getDay에 저장
                    //시,분,초를 String 형식의 getTime에 저장
                    //getDay + 줄바꿈 + getTime을 합쳐 String 형태의 getDate를 선언
                    SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm:ss");
                    String getDay = simpleDateFormatDay.format(date);
                    String getTime = simpleDateFormatTime.format(date);
                    String getDate = getDay + "\n" + getTime;

                    dateView.setText(getDate);

                    //api로 받은 json 파일을 받아줄 JSONObject 객체 선언
                    JSONObject jsonObject = new JSONObject(response);

                    //도시 설정
                    String city = jsonObject.getString("name");
                    cityView.setText(city);

                    //날씨 설정
                    JSONArray weatherJson = jsonObject.getJSONArray("weather");
                    JSONObject weatherObj = weatherJson.getJSONObject(0);

                    String weather = weatherObj.getString("description");
                    weatherView.setText(weather);

                    //기온 설정
                    JSONObject tempK = new JSONObject(jsonObject.getString("main"));

                    double tempDo = (Math.round((tempK.getDouble("temp") - 273.15) * 100) / 100.0);
                    tempView.setText(tempDo + "°C");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };

        //request 받기를 멈추고 Queue에 request를 넣어 저장함
        request.setShouldCache(false);
        requestQueue.add(request);
    }

}