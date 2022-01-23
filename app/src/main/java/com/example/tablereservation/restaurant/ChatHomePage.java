package com.example.tablereservation.restaurant;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tablereservation.R;
import com.example.tablereservation.customer.Chat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatHomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatHomePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatHomePage(String id) {
        this.id = id;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatHomePage.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatHomePage newInstance(String param1, String param2) {
        ChatHomePage fragment = new ChatHomePage(id);
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

    LinearLayout linear_chat_name_list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_home_page, null);


        linear_chat_name_list = view.findViewById(R.id.linear_chat_name_list);

        showtName_list();


        return view;
    }

    private void showtName_list() {


        final String REQUEST_URL = "http://www.table-reservation2021.com/get_resturant_chat.php";


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

                                showName(obj.getJSONObject(i));


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
                MyData.put("user_id", id);

                return MyData;
            }


        };
        requestQueue.add(jsonRequest);


    }

    private void showName(JSONObject jsonObject) {


        try {
            JSONObject data = new JSONObject(jsonObject.getString("data"));

            View view = getLayoutInflater().inflate(R.layout.chat_name_list, null);


            TextView customer_chat_name = view.findViewById(R.id.customer_chat_name);
            TextView customer_message_number = view.findViewById(R.id.customer_message_number);

            String otherid = data.getString("customer_id");

            customer_chat_name.setText(data.getString("customer_name"));
            customer_message_number.setText(data.getString("msg_num"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(getActivity(), RestaurantChat.class);
                    i.putExtra("myid", id);
                    i.putExtra("otherid", otherid);
                    startActivity(i);


                }
            });

            linear_chat_name_list.addView(view);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}