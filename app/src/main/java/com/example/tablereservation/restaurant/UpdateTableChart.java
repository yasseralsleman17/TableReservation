package com.example.tablereservation.restaurant;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tablereservation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateTableChart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateTableChart extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateTableChart(String id) {
        this.id = id;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateTableChart.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateTableChart newInstance(String param1, String param2) {
        UpdateTableChart fragment = new UpdateTableChart(id);
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

    Button res_add_chart;
    LinearLayout linear_table_chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_table_chart, null);


        linear_table_chart = view.findViewById(R.id.linear_table_chart);


        showtablechart();

        res_add_chart = view.findViewById(R.id.res_add_chart);
        res_add_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getActivity(), AddNewTable.class);
                i.putExtra("id", id);
                startActivity(i);


            }
        });


        return view;
    }

    private void showtablechart() {


        final String REQUEST_URL = "http://www.table-reservation2021.com/get_all_table.php";


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.d("response", response);


                        try {
                            JSONArray obj = new JSONArray(response);
                            Log.d("response", String.valueOf(obj));


                            for (int i = 0; i < obj.length(); i++) {

                                Log.d("\n\n\nresponse", String.valueOf(obj));

                                showtable(obj.getJSONObject(i));


                            }
                        } catch (JSONException e) {

                            e.printStackTrace();


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
                MyData.put("resturant_id", id);

                return MyData;
            }


        };
        requestQueue.add(jsonRequest);

    }

    private void showtable(JSONObject jsonObject) {


        try {
            JSONObject data = new JSONObject(jsonObject.getString("data"));

            View view = getLayoutInflater().inflate(R.layout.table_card, null);


            TextView table_seats = view.findViewById(R.id.table_seats);
            TextView table_location = view.findViewById(R.id.table_location);
            TextView table_number = view.findViewById(R.id.table_number);
            TextView table_availability = view.findViewById(R.id.table_availability);

            ImageView imageView2 = view.findViewById(R.id.photo);
            table_seats.setText(data.getString("seat_no"));
            table_location.setText(data.getString("location"));
            table_number.setText(data.getString("table_num"));

            String download_URL = "http://www.table-reservation2021.com/images/" + data.getString("table_image") + ".JPG";
            Glide.with(UpdateTableChart.this)
                    .load(download_URL)
                    .into(imageView2);


            if (data.getString("available").equals("1"))
                table_availability.setText("available");
            else if (data.getString("available").equals("2"))
                table_availability.setText("not available");


            linear_table_chart.addView(view);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}