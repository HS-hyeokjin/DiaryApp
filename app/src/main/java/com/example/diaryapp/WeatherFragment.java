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

        //???????????? ??????(refresh)??? ?????? ??? ??????????????? ??????
        refresh = rootView.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System??? ?????? ????????? ???????????? ????????? currentTimeMills();
                //?????? ???????????? ???????????? long????????? now ????????? ??????
                //????????? ???????????? ??? ?????? Date??? now??? ?????? date??? ??????
                city = contextEditText.getText().toString();
                CurrentCall();

                contextEditText.setText("");
            }

        });

        //volley??? ??? ??? Queue??? ??????????????? ????????? ??? ??????
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mainActivity.getApplicationContext());
        }

        return rootView;
    }

    //API KEY??? ???????????? ?????? ????????? ???????????????
    private void CurrentCall() {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&lang=kr&appid=cd3ef2bfa1fed88800228b8ab610338a";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    //?????? ??????
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);

                    //SimpleDateFormat??? ?????? ????????? ???????????? ?????? ?????? ????????? ??? ??????
                    //???,???,?????? String ????????? getDay??? ??????
                    //???,???,?????? String ????????? getTime??? ??????
                    //getDay + ????????? + getTime??? ?????? String ????????? getDate??? ??????
                    SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm:ss");
                    String getDay = simpleDateFormatDay.format(date);
                    String getTime = simpleDateFormatTime.format(date);
                    String getDate = getDay + "\n" + getTime;

                    dateView.setText(getDate);

                    //api??? ?????? json ????????? ????????? JSONObject ?????? ??????
                    JSONObject jsonObject = new JSONObject(response);

                    //?????? ??????
                    String city = jsonObject.getString("name");
                    cityView.setText(city);

                    //?????? ??????
                    JSONArray weatherJson = jsonObject.getJSONArray("weather");
                    JSONObject weatherObj = weatherJson.getJSONObject(0);

                    String weather = weatherObj.getString("description");
                    weatherView.setText(weather);

                    //?????? ??????
                    JSONObject tempK = new JSONObject(jsonObject.getString("main"));

                    double tempDo = (Math.round((tempK.getDouble("temp") - 273.15) * 100) / 100.0);
                    tempView.setText(tempDo + "??C");

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

        //request ????????? ????????? Queue??? request??? ?????? ?????????
        request.setShouldCache(false);
        requestQueue.add(request);
    }

}