package com.example.tablereservation.customer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tablereservation.R;
import com.example.tablereservation.restaurant.AddNewTable;
import com.example.tablereservation.restaurant.UpdateTableChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableChart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableChart extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String restaurant_id;
    private static String id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TableChart(String restaurant_id, String id) {
        this.restaurant_id = restaurant_id;
        this.id = id;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TableChart.
     */
    // TODO: Rename and change types and number of parameters
    public static TableChart newInstance(String param1, String param2) {
        TableChart fragment = new TableChart(restaurant_id, id);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    LinearLayout linear_table_chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_table_chart, null);

        linear_table_chart = view.findViewById(R.id.linear_table_chart);

        final String REQUEST_URL = "http://www.table-reservation2021.com/get_available_table.php";

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.d("response", response);
                        Log.d("restaurant_id", restaurant_id);


                        try {
                            JSONArray obj = new JSONArray(response);
                            Log.d("response", String.valueOf(obj));


                            for (int i = 0; i < obj.length(); i++) {

                                Log.d("\n\n\nresponse", String.valueOf(obj));

                                tablechartlist(obj.getJSONObject(i));


                            }
                        } catch (JSONException e) {

                            e.printStackTrace();


                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("code").equals("-1"))
                                    Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();


                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }


                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("mStatusCode", String.valueOf(mStatusCode));


                return super.parseNetworkResponse(response);


            }

            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("resturant_id", restaurant_id);


                return MyData;
            }


        };
        requestQueue.add(jsonRequest);


        return view;
    }

    private void tablechartlist(JSONObject jsonObject) {


        try {
            JSONObject data = new JSONObject(jsonObject.getString("data"));

            String table_id = data.getString("sr_no");
            View view = getLayoutInflater().inflate(R.layout.reserve_table, null);


            TextView table_seats = view.findViewById(R.id.table_seats);
            TextView table_location = view.findViewById(R.id.table_location);
            TextView table_number = view.findViewById(R.id.table_number);
            TextView timetv = view.findViewById(R.id.time);
            TextView datetv = view.findViewById(R.id.date);

            ImageView imageView2 = view.findViewById(R.id.photo);
            String download_URL = "http://www.table-reservation2021.com/images/" + data.getString("table_image") + ".JPG";
            Glide.with(TableChart.this)
                    .load(download_URL)
                    .into(imageView2);

            table_seats.setText(data.getString("seat_no"));
            table_location.setText(data.getString("location"));
            table_number.setText(data.getString("table_num"));


            Button btn_add_reserve_table, btn_add_time_date;

            btn_add_reserve_table = view.findViewById(R.id.btn_add_reserve_table);
            btn_add_time_date = view.findViewById(R.id.btn_add_time_date);

            final String[] time = new String[1];
            final String[] date = new String[1];


            btn_add_time_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TimePickerDialog TimePickerDialog = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        TimePickerDialog = new TimePickerDialog(
                                getActivity(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        time[0] = hourOfDay + ":" + minute;
                                        timetv.setText(time[0]);
                                    }
                                },
                                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                                Calendar.getInstance().get(Calendar.MINUTE),
                                DateFormat.is24HourFormat(getActivity()));
                    }
                    TimePickerDialog.show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        DatePickerDialog datePickerDialog = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            datePickerDialog = new DatePickerDialog(getActivity(),
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            date[0] = dayOfMonth + "/" + month + "/" + year;
                                            datetv.setText(date[0]);
                                        }
                                    }
                                    ,
                                    Calendar.getInstance().get(Calendar.YEAR),
                                    Calendar.getInstance().get(Calendar.MONTH),
                                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        }
                        datePickerDialog.show();
                    }


                }
            });


            btn_add_reserve_table.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String  date_txt= datetv.getText().toString();
                    String  time_txt = timetv.getText().toString();

                    if (time_txt.isEmpty() || date_txt.isEmpty()) {
                        Toast.makeText(getActivity(),"Select date and time !",Toast.LENGTH_SHORT).show();
                        return;
                    }


                    final String REQUEST_URL = "http://www.table-reservation2021.com/reserve_table.php";


                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest jsonRequest = new StringRequest(
                            Request.Method.POST,
                            REQUEST_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {


                                    Log.d("response", response);

                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getString("code").equals("1"))
                                            Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();

                                        Intent i = new Intent(getActivity(), RestaurantDetails.class);
                                        i.putExtra("restaurant_id", restaurant_id);
                                        i.putExtra("id", id);
                                        startActivity(i);


                                        if (obj.getString("code").equals("-1"))
                                            Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();


                                    } catch (JSONException jsonException) {
                                        jsonException.printStackTrace();
                                    }

                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }

                            }) {
                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            int mStatusCode = response.statusCode;
                            Log.d("mStatusCode", String.valueOf(mStatusCode));


                            return super.parseNetworkResponse(response);


                        }

                        protected Map<String, String> getParams() {
                            Map<String, String> MyData = new HashMap<String, String>();
                            MyData.put("user_id", id);
                            MyData.put("r_id", restaurant_id);
                            MyData.put("table_num", table_id);
                            MyData.put("date", date_txt);
                            MyData.put("time", time_txt);
                            try {
                                MyData.put("person_num", data.getString("seat_no"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            return MyData;
                        }


                    };
                    requestQueue.add(jsonRequest);


                }
            });


            linear_table_chart.addView(view);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}