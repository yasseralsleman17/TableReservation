package com.example.tablereservation.customer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tablereservation.R;
import com.example.tablereservation.restaurant.RestaurantHomePage;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chat extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String restaurant_id;
    private static String id;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Chat(String restaurant_id, String id) {
        this.restaurant_id = restaurant_id;
        this.id = id;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1        Parameter 1.
     * @param param2        Parameter 2.
     * @param restaurant_id
     * @param id
     * @return A new instance of fragment Chat.
     */
    // TODO: Rename and change types and number of parameters
    public static Chat newInstance(String param1, String param2, String restaurant_id, String id) {
        Chat fragment = new Chat(restaurant_id, id);
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

    private void getallmessage() {


        final String REQUEST_URL = "http://www.table-reservation2021.com/read_all_message.php";


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray obj = new JSONArray(response);
                            Log.d("response", String.valueOf(obj));


                            for (int i = 0; i < obj.length(); i++) {

                                Log.d("\n\n\nresponse", String.valueOf(obj));

                                if (obj.getJSONObject(i).getString("code").equals("2"))
                                    viewOtherMessage(obj.getJSONObject(i));
                                else if (obj.getJSONObject(i).getString("code").equals("1"))
                                    viewmyMessage(obj.getJSONObject(i));


                            }


                            time.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    getmessage();
                                }
                            }, 0, 2000);


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

                MyData.put("r_id", restaurant_id);
                MyData.put("s_id",id );


                return MyData;
            }

        };
        requestQueue.add(jsonRequest);


    }


    private void getmessage() {


        final String REQUEST_URL = "http://www.table-reservation2021.com/read_message.php";


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray obj = new JSONArray(response);
                            Log.d("response", String.valueOf(obj));


                            for (int i = 0; i < obj.length(); i++) {

                                Log.d("\n\n\nresponse", String.valueOf(obj));

                                viewOtherMessage(obj.getJSONObject(i));


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

                MyData.put("r_id", id);
                MyData.put("s_id", restaurant_id);


                return MyData;
            }

        };
        requestQueue.add(jsonRequest);


    }


    private void viewmyMessage(JSONObject jsonObject) {

        try {
            JSONObject data = new JSONObject(jsonObject.getString("data"));
            View view = getLayoutInflater().inflate(R.layout.my_message, null);


            TextView username = view.findViewById(R.id.mymessagetext);
            username.setText(data.getString("content"));


            linearmessages.addView(view);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void sendmymessage(String mymessage) {


        View view = getLayoutInflater().inflate(R.layout.my_message, null);


        TextView username = view.findViewById(R.id.mymessagetext);
        username.setText(mymessage);


        final String REQUEST_URL = "http://www.table-reservation2021.com/send_message.php";


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.d("response", response);


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

                MyData.put("s_id", id);
                MyData.put("r_id", restaurant_id);
                MyData.put("content", mymessage);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


        linearmessages.addView(view);


    }


    private void viewOtherMessage(JSONObject jsonObject) {

        try {
            JSONObject data = new JSONObject(jsonObject.getString("data"));
            View view = getLayoutInflater().inflate(R.layout.other_message, null);


            TextView username = view.findViewById(R.id.othermessagetext);
            username.setText(data.getString("content"));


            linearmessages.addView(view);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    ImageView bt_message;
    EditText ed_message;
    String mymessage;
    LinearLayout linearmessages;
    Timer time = new Timer();

    @Override
    public void onPause() {
        time.cancel();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getallmessage();

        View view = inflater.inflate(R.layout.fragment_chat, null);


        linearmessages = view.findViewById(R.id.linearmessages);
        ed_message = view.findViewById(R.id.ed_message);
        bt_message = view.findViewById(R.id.bt_message);


        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mymessage = ed_message.getText().toString();

                if (!mymessage.isEmpty()) {

                    sendmymessage(mymessage);

                    ed_message.setText("");
                }
            }
        });


        return view;
    }
}